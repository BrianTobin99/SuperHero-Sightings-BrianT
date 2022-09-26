package com.briant.dao;

import com.briant.entities.Organization;
import com.briant.entities.SuperPerson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class OrganizationDaoDbImpl implements OrganizationDao {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Organization getOrganizationById(int organizationId) {
        try {
            final String SELECT_ORGANIZATION_BY_ID = "SELECT * FROM organization WHERE organizationId = ?";
            Organization organization = jdbc.queryForObject(SELECT_ORGANIZATION_BY_ID, new OrganizationMapper(), organizationId);
            organization = assosciateLocation(organization);
            return organization;
        } 
        catch(DataAccessException ex) {
            return null;
        }            
    }

    @Override
    public List<Organization> getAllOrganizations() {
        final String GET_ALL_ORGANIZATIONS = "SELECT * FROM organization";
        List<Organization> organizations = jdbc.query(GET_ALL_ORGANIZATIONS, new OrganizationMapper());
        
        organizations.forEach(org -> {
            org = assosciateLocation(org);
            org.setMembers(assosciateSupers(org.getOrganizationId()));
        });
        return organizations;
    }

    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORGANIZATION = "INSERT INTO organization(locationId, organizationType, organizationName, organizationDesc, organizationPhone) VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_ORGANIZATION, 
                organization.getLocationId(),
                organization.getType(),
                organization.getName(), 
                organization.getDescription(),
                organization.getPhone());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setOrganizationId(newId);
        return organization;            
    }

    @Override
    @Transactional
    public void deleteOrganizationById(int organizationId) {
        final String DELETE_ORGHEROVILLAIN = "DELETE ohv.* FROM organizationsuperperson ohv WHERE ohv.organizationId = ?";
        jdbc.update(DELETE_ORGHEROVILLAIN, organizationId);
        
        final String DELETE_ORGANIZATION = "DELETE o.* FROM organization o WHERE organizationId = ?";
        jdbc.update(DELETE_ORGANIZATION, organizationId);
    }

    @Override
    public void editOrganization(Organization organization) {
        final String UPDATE_LOCATION = "UPDATE organization SET locationId = ?, organizationType = ?, "
                + " organizationName = ?, organizationDesc = ?, organizationPhone = ? WHERE organizationId = ?";
        jdbc.update(UPDATE_LOCATION, organization.getLocationId(), organization.getType(), 
                organization.getName(), organization.getDescription(), organization.getPhone(), 
                organization.getOrganizationId());
    }

    private Organization assosciateLocation(Organization organization) {
        final String GET_LOCATION_ORGANIZATION = "SELECT * FROM location WHERE locationId = ?";
        organization.setLocation(jdbc.queryForObject(GET_LOCATION_ORGANIZATION, new LocationDaoDbImpl.LocationMapper(), organization.getLocationId()));
        return organization;
    }

    @Override
    public Organization getOrganizationByName(String orgName) {
        final String GET_ORG_BY_NAME = "SELECT * FROM organization WHERE organizationName = ?";
        Organization organization = jdbc.queryForObject(GET_ORG_BY_NAME, new OrganizationMapper(), orgName);
        organization = assosciateLocation(organization);
        return organization;
    }

    private List<SuperPerson> assosciateSupers(int organizationId) {
        final String GET_SUPERS_IN_ORG = "SELECT p.* FROM organizationsuperperson osp "
                + "JOIN superperson p ON p.superPersonId = osp.superPersonId "
                + "WHERE organizationId = ?";
        List<SuperPerson> superPersons = jdbc.query(GET_SUPERS_IN_ORG, new SuperPersonDaoDbImpl.SuperPersonMapper(), organizationId);
        return superPersons;
    }
    
    
    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization = new Organization();
            organization.setOrganizationId(rs.getInt("organizationId"));
            organization.setName(rs.getString("organizationName"));
            organization.setDescription(rs.getString("organizationDesc"));
            organization.setLocationId(rs.getInt("locationId"));
            organization.setPhone(rs.getString("organizationPhone"));
            organization.setType(rs.getString("organizationType"));
            
            return organization;
        }
    }
}
