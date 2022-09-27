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
    public Organisation getOrganisationByID(int organisationId) {
        try {
            final String SELECT_ORGANISATION_BY_ID = "SELECT * FROM Organisation WHERE OrganisationID = ?";
            Organisation organisation = jdbc.queryForObject(SELECT_ORGANISATION_BY_ID, new OrganisationMapper(), organisationId);
            organisation = assosciateLocation(organisation);
            return organisation;
        } 
        catch(DataAccessException ex) {
            return null;
        }            
    }

    @Override
    public List<Organisation> getAllOrganisations() {
        final String GET_ALL_ORGANISATIONS = "SELECT * FROM Organisation";
        List<Organisation> organisations = jdbc.query(GET_ALL_ORGANISATIONS, new OrganisationMapper());
        
        organisations.forEach(org -> {
            org = assosciateLocation(org);
            org.setMembers(assosciateSupers(org.getOrganisationID()));
        });
        return organisations;
    }

    @Override
    @Transactional
    public Organisation addOrganisation(Organisation organisation) {
        final String INSERT_ORGANISATION = "INSERT INTO Organisation(OrganisationName, Alignment, Description, LocationID, PhoneNumber) VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_ORGANISATION, 
                organisation.getName(), 
                organisation.getAlignment(),
                organisation.getDescription(),
                organisation.getLocationID(),
                organisation.getPhone());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organisation.setOrganisationID(newId);
        return organisation;            
    }

    @Override
    @Transactional
    public void deleteOrganisationByID(int organisationId) {
        final String DELETE_ORGANISATION = "DELETE FROM Organisation WHERE OrganisationID = ?";
        jdbc.update(DELETE_ORGANISATION, organisationId);
    }

    @Override
    public void editOrganisation(Organisation organisation) {
        final String UPDATE_LOCATION = "UPDATE Organisation SET OrganisationName = ?, Alignment = ?, "
                + " Description = ?, LocationID = ?, PhoneNumber = ? WHERE OrganisationID = ?";
        jdbc.update(UPDATE_LOCATION, organisation.getName(), organisation.getAlignment(), 
                organisation.getDescription(), organisation.getLocationID(),
                organisation.getPhone(), 
                organisation.getOrganisationID());
    }

    private Organisation assosciateLocation(Organisation organisation) {
        final String GET_LOCATION_ORGANISATION = "SELECT * FROM Location WHERE LocationID = ?";
        organisation.setLocation(jdbc.queryForObject(GET_LOCATION_ORGANISATION, new LocationDaoDbImpl.LocationMapper(), organisation.getLocationID()));
        return organisation;
    }

    @Override
    public Organisation getOrganisationByName(String orgName) {
        final String GET_ORG_BY_NAME = "SELECT * FROM Organisation WHERE OrganisationName = ?";
        Organisation organisation = jdbc.queryForObject(GET_ORG_BY_NAME, new OrganisationMapper(), orgName);
        organisation = assosciateLocation(organisation);
        return organisation;
    }

    private List<SuperPerson> assosciateSupers(int organisationId) {
        final String GET_SUPERS_IN_ORG = "SELECT * FROM SuperPerson WHERE organisationId = ?";
        List<SuperPerson> superPersons = jdbc.query(GET_SUPERS_IN_ORG, new SuperPersonDaoDbImpl.SuperPersonMapper(), organisationId);
        return superPersons;
    }
    
    
    public static final class OrganisationMapper implements RowMapper<Organisation> {

        @Override
        public Organisation mapRow(ResultSet rs, int index) throws SQLException {
            Organisation organisation = new Organisation();
            organisation.setOrganisationID(rs.getInt("organisationId"));
            organisation.setName(rs.getString("organisationName"));
            organisation.setDescription(rs.getString("organisationDesc"));
            organisation.setLocationID(rs.getInt("locationId"));
            organisation.setPhone(rs.getString("organisationPhone"));
            organisation.setAlignment(rs.getString("Alignment"));
            
            return organisation;
        }
    }
}
