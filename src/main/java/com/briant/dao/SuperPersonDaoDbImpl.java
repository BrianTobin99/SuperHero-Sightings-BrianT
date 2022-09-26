package com.briant.dao;

import com.briant.entities.Organization;
import com.briant.entities.Power;
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
public class SuperPersonDaoDbImpl implements SuperPersonDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public SuperPerson getSuperById(int superPersonId) {
        try {
            final String SELECT_SUPER_PERSON_BY_ID = "SELECT * FROM superperson WHERE superPersonId = ?";
            SuperPerson superPerson = jdbc.queryForObject(SELECT_SUPER_PERSON_BY_ID, new SuperPersonMapper(), superPersonId);
            superPerson = assosciatePowers(superPerson);
            superPerson = assosciateOrganizations(superPerson);
            return superPerson;
        } 
        catch(DataAccessException ex) {
            return null;
        }    
    }
    
    @Override
    public List<SuperPerson> getAllHeroes() {
        final String GET_ALL_HEROES = "SELECT * FROM superperson WHERE isVillain = ?";
        List<SuperPerson> heroes = jdbc.query(GET_ALL_HEROES, new SuperPersonMapper(), false);
        heroes.forEach(hero -> {
            hero = assosciatePowers(hero);
            hero = assosciateOrganizations(hero);
        });
        return heroes;
    }
    
    @Override
    public List<SuperPerson> getAllVillains() {
        final String GET_ALL_HEROES = "SELECT * FROM superperson WHERE isVillain = ?";
        List<SuperPerson> villains = jdbc.query(GET_ALL_HEROES, new SuperPersonMapper(), true);
        villains.forEach(villain -> {
            villain = assosciatePowers(villain);
            villain = assosciateOrganizations(villain);
        });
        return villains;
    }

    @Override
    @Transactional
    public SuperPerson addSuper(SuperPerson superPerson) {
        final String INSERT_SUPER_PERSON = "INSERT INTO superperson(sName, sDescription, isVillain) VALUES(?,?,?)";
        jdbc.update(INSERT_SUPER_PERSON, superPerson.getName(), superPerson.getDescription(), superPerson.isVillain());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superPerson.setSuperPersonId(newId);
        final String INSERT_SUPER_PERSON_POWERS = "INSERT INTO superpersonpower(powerId, superPersonId) VALUES (?,?)";
        final String INSERT_SUPER_PERSON_ORGANIZATIONS = "INSERT INTO organizationsuperperson(organizationId, superPersonId) VALUES (?,?)";
        if(superPerson.getPowers() != null){
            superPerson.getPowers().forEach(power -> {
                jdbc.update(INSERT_SUPER_PERSON_POWERS, power.getPowerId(), superPerson.getSuperPersonId());
            });
        }
        if(superPerson.getOrganizations() != null){
            superPerson.getOrganizations().forEach(org -> {
                jdbc.update(INSERT_SUPER_PERSON_ORGANIZATIONS, org.getOrganizationId(), superPerson.getSuperPersonId());
            });
        }
        
        return superPerson;    
    }

    @Override
    @Transactional
    public void deleteSuperById(int superPersonId) {
        final String DELETE_SIGHTING = "DELETE hvs.* FROM sighting hvs WHERE superPersonId = ?";
        jdbc.update(DELETE_SIGHTING, superPersonId);
        
        final String DELETE_ORGSUPERPERSON = "DELETE ohv.* FROM organizationsuperperson ohv WHERE superPersonId = ?";
        jdbc.update(DELETE_ORGSUPERPERSON, superPersonId);
        
        final String DELETE_SUPERPERSONPOWER = "DELETE hvp.* FROM superpersonpower hvp WHERE superPersonId = ?";
        jdbc.update(DELETE_SUPERPERSONPOWER, superPersonId);
        
        final String DELETE_SUPERPERSON = "DELETE hv.* FROM superperson hv WHERE superPersonId = ?";
        jdbc.update(DELETE_SUPERPERSON, superPersonId);
    }

    @Override
    public void editSuper(SuperPerson superperson) {
        final String UPDATE_SUPERPERSON = "UPDATE superperson SET sName = ?, sDescription = ?, isVillain = ? WHERE superPersonId = ?";
        jdbc.update(UPDATE_SUPERPERSON, superperson.getName(), superperson.getDescription(), superperson.isVillain(), superperson.getSuperPersonId());    
    }

    private SuperPerson assosciatePowers(SuperPerson superPerson) {
        final String GET_HERO_POWERS = "SELECT * FROM superperson sp "
                + "JOIN superpersonpower spp ON sp.superPersonId = spp.superPersonId "
                + "JOIN power p ON spp.powerId = p.powerId "
                + "WHERE sp.superPersonId = ?";
        List<Power> powers = jdbc.query(GET_HERO_POWERS, new PowerPersonMapper(), superPerson.getSuperPersonId());
        superPerson.setPowers(powers);
        return superPerson;
    }

    private SuperPerson assosciateOrganizations(SuperPerson superPerson) {
       final String GET_HERO_ORGANIZATIONS = "SELECT * FROM superperson sp "
                + "JOIN organizationsuperperson osp ON sp.superPersonId = osp.superPersonId "
                + "JOIN organization o ON o.organizationId = osp.organizationId "
                + "WHERE sp.superPersonId = ?";
        List<Organization> organizations = jdbc.query(GET_HERO_ORGANIZATIONS, new OrganizationPersonMapper(), superPerson.getSuperPersonId());
        superPerson.setOrganizations(organizations);
        return superPerson;    
    }

    @Override
    public SuperPerson getSuperPersonByName(String superPersonName) {
        try {
            final String GET_SUPER_BY_NAME = "SELECT * FROM superPerson WHERE sName = ?";
            SuperPerson superPerson = jdbc.queryForObject(GET_SUPER_BY_NAME, new SuperPersonMapper(), superPersonName);   
            superPerson = assosciatePowers(superPerson);
            superPerson = assosciateOrganizations(superPerson);
            return superPerson;
        } 
        catch(DataAccessException ex) {
            return null;
        }    

       
    }

    public static final class SuperPersonMapper implements RowMapper<SuperPerson> {

        @Override
        public SuperPerson mapRow(ResultSet rs, int index) throws SQLException {
            SuperPerson superPerson = new SuperPerson();
            superPerson.setSuperPersonId(rs.getInt("superPersonId"));
            superPerson.setName(rs.getString("sName"));
            superPerson.setDescription(rs.getString("sDescription"));
            superPerson.setVillain(rs.getBoolean("isVillain"));
            return superPerson;
        }
    }
    
    private static final class PowerPersonMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power power = new Power();
            power.setPowerId(rs.getInt("powerId"));
            power.setName(rs.getString("powerName"));
            power.setDescription(rs.getString("powerDesc"));
            return power;
        }
    }
    
    private static final class OrganizationPersonMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization = new Organization();
            organization.setOrganizationId(rs.getInt("organizationId"));
            organization.setLocationId(rs.getInt("locationId"));
            organization.setType(rs.getString("organizationType"));
            organization.setName(rs.getString("organizationName"));
            organization.setDescription(rs.getString("organizationDesc"));
            organization.setPhone(rs.getString("organizationPhone"));
            return organization;
        }
    }
    
}
