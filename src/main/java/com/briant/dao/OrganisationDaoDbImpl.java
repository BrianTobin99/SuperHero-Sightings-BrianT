package com.briant.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.briant.dto.Organisation;
import com.briant.dto.SuperPerson;


@Repository
public class OrganisationDaoDbImpl implements OrganisationDao {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Organisation getOrganisationById(int organisationId) {
        try {
            final String SELECT_ORGANIsATION_BY_ID = "SELECT * FROM organisation WHERE organisationId = ?";
            Organisation organisation = jdbc.queryForObject(SELECT_ORGANIsATION_BY_ID, new OrganisationMapper(), organisationId);
            organisation = assosciateLocation(organisation);
            return organisation;
        } 
        catch(DataAccessException ex) {
            return null;
        }            
    }

    @Override
    public List<Organisation> getAllOrganisations() {
        final String GET_ALL_ORGANIsATIONS = "SELECT * FROM organisation";
        List<Organisation> organisations = jdbc.query(GET_ALL_ORGANIsATIONS, new OrganisationMapper());
        
        organisations.forEach(org -> {
            org = assosciateLocation(org);
            org.setMembers(assosciateSupers(org.getOrganisationId()));
        });
        return organisations;
    }

    @Override
    @Transactional
    public Organisation addOrganisation(Organisation organisation) {
        final String INSERT_ORGANIsATION = "INSERT INTO organisation(locationId, organisationType, organisationName, organisationDesc, organisationPhone) VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_ORGANIsATION, 
                organisation.getLocationId(),
                organisation.getType(),
                organisation.getName(), 
                organisation.getDescription(),
                organisation.getPhone());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organisation.setOrganisationId(newId);
        return organisation;            
    }

    @Override
    @Transactional
    public void deleteOrganisationById(int organisationId) {
        final String DELETE_ORGHEROVILLAIN = "DELETE ohv.* FROM organisationsuperperson ohv WHERE ohv.organisationId = ?";
        jdbc.update(DELETE_ORGHEROVILLAIN, organisationId);
        
        final String DELETE_ORGANIsATION = "DELETE o.* FROM organisation o WHERE organisationId = ?";
        jdbc.update(DELETE_ORGANIsATION, organisationId);
    }

    @Override
    public void editOrganisation(Organisation organisation) {
        final String UPDATE_LOCATION = "UPDATE organisation SET locationId = ?, organisationType = ?, "
                + " organisationName = ?, organisationDesc = ?, organisationPhone = ? WHERE organisationId = ?";
        jdbc.update(UPDATE_LOCATION, organisation.getLocationId(), organisation.getType(), 
                organisation.getName(), organisation.getDescription(), organisation.getPhone(), 
                organisation.getOrganisationId());
    }

    private Organisation assosciateLocation(Organisation organisation) {
        final String GET_LOCATION_ORGANIsATION = "SELECT * FROM location WHERE locationId = ?";
        organisation.setLocation(jdbc.queryForObject(GET_LOCATION_ORGANIsATION, new LocationDaoDbImpl.LocationMapper(), organisation.getLocationId()));
        return organisation;
    }

    @Override
    public Organisation getOrganisationByName(String orgName) {
        final String GET_ORG_BY_NAME = "SELECT * FROM organisation WHERE organisationName = ?";
        Organisation organisation = jdbc.queryForObject(GET_ORG_BY_NAME, new OrganisationMapper(), orgName);
        organisation = assosciateLocation(organisation);
        return organisation;
    }

    private List<SuperPerson> assosciateSupers(int organisationId) {
        final String GET_SUPERS_IN_ORG = "SELECT p.* FROM organisationsuperperson osp "
                + "JOIN superperson p ON p.superPersonId = osp.superPersonId "
                + "WHERE organisationId = ?";
        List<SuperPerson> superPersons = jdbc.query(GET_SUPERS_IN_ORG, new SuperPersonDaoDbImpl.SuperPersonMapper(), organisationId);
        return superPersons;
    }
    
    
    public static final class OrganisationMapper implements RowMapper<Organisation> {

        @Override
        public Organisation mapRow(ResultSet rs, int index) throws SQLException {
            Organisation organisation = new Organisation();
            organisation.setOrganisationId(rs.getInt("organisationId"));
            organisation.setName(rs.getString("organisationName"));
            organisation.setDescription(rs.getString("organisationDesc"));
            organisation.setLocationId(rs.getInt("locationId"));
            organisation.setPhone(rs.getString("organisationPhone"));
            organisation.setType(rs.getString("organisationType"));
            
            return organisation;
        }
    }
}
