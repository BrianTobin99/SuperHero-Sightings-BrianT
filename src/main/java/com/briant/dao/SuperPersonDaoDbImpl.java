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
import com.briant.dto.Power;
import com.briant.dto.SuperPerson;

@Repository
public class SuperPersonDaoDbImpl implements SuperPersonDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public SuperPerson getSuperByID(int superPersonID) {
        try {
            final String SELECT_SUPER_PERSON_BY_ID = "SELECT * FROM SuperPerson WHERE SuperPersonID = ?";
            SuperPerson superPerson = jdbc.queryForObject(SELECT_SUPER_PERSON_BY_ID, new SuperPersonMapper(), superPersonID);
            superPerson = assosciatePowers(superPerson);
            superPerson = assosciateOrganisations(superPerson);
            return superPerson;
        } 
        catch(DataAccessException ex) {
            return null;
        }    
    }
    
    @Override
    public List<SuperPerson> getAllHeroes() {
        final String GET_ALL_HEROES = "SELECT * FROM SuperPerson WHERE IsVillain = ?";
        List<SuperPerson> heroes = jdbc.query(GET_ALL_HEROES, new SuperPersonMapper(), false);
        heroes.forEach(hero -> {
            hero = assosciatePowers(hero);
            hero = assosciateOrganisations(hero);
        });
        return heroes;
    }
    
    @Override
    public List<SuperPerson> getAllVillains() {
        final String GET_ALL_HEROES = "SELECT * FROM SuperPerson WHERE IsVillain = ?";
        List<SuperPerson> villains = jdbc.query(GET_ALL_HEROES, new SuperPersonMapper(), true);
        villains.forEach(villain -> {
            villain = assosciatePowers(villain);
            villain = assosciateOrganisations(villain);
        });
        return villains;
    }

    @Override
    @Transactional
    public SuperPerson addSuper(SuperPerson superPerson) {
        final String INSERT_SUPER_PERSON = "INSERT INTO SuperPerson(PersonName, IsVillain, Description) VALUES(?,?,?)";
        jdbc.update(INSERT_SUPER_PERSON, superPerson.getName(), superPerson.getDescription(), superPerson.isVillain());
        int newID = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superPerson.setSuperPersonID(newID);
        final String INSERT_SUPER_PERSON_POWERS = "INSERT INTO Powers(PowerID, SuperPersonID) VALUES (?,?)";
        final String INSERT_SUPER_PERSON_Organisations = "INSERT INTO Organisation(OrganisationID, SuperPersonID) VALUES (?,?)";
        if(superPerson.getPowers() != null){
            superPerson.getPowers().forEach(power -> {
                jdbc.update(INSERT_SUPER_PERSON_POWERS, power.getPowerID(), superPerson.getSuperPersonID());
            });
        }
        if(superPerson.getOrganisations() != null){
            superPerson.getOrganisations().forEach(org -> {
                jdbc.update(INSERT_SUPER_PERSON_Organisations, org.getOrganisationID(), superPerson.getSuperPersonID());
            });
        }
        
        return superPerson;    
    }

    @Override
    @Transactional
    public void deleteSuperByID(int superPersonID) {
        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE SuperPersonID = ?";
        jdbc.update(DELETE_SIGHTING, superPersonID);
        
        final String DELETE_ORGSUPERPERSON = "DELETE FROM Organisation WHERE superPersonID = ?";
        jdbc.update(DELETE_ORGSUPERPERSON, superPersonID);
        
        final String DELETE_SUPERPERSONPOWER = "DELETE FROM Power WHERE superPersonID = ?";
        jdbc.update(DELETE_SUPERPERSONPOWER, superPersonID);
        
        final String DELETE_SUPERPERSON = "DELETE FROM SuperPerson WHERE superPersonID = ?";
        jdbc.update(DELETE_SUPERPERSON, superPersonID);
    }

    @Override
    public void editSuper(SuperPerson superperson) {
        final String UPDATE_SUPERPERSON = "UPDATE SuperPerson SET PersonName = ?, IsVillain = ?, Description = ? WHERE superPersonID = ?";
        jdbc.update(UPDATE_SUPERPERSON, superperson.getName(), superperson.getDescription(), superperson.isVillain(), superperson.getSuperPersonID());    
    }

    private SuperPerson assosciatePowers(SuperPerson superPerson) {
        final String GET_HERO_POWERS = "SELECT * FROM SuperPerson sp "
                + "JOIN Power p ON sp.superPersonID = p.superPersonID "
                + "WHERE sp.SuperPersonID = ?";
        List<Power> powers = jdbc.query(GET_HERO_POWERS, new PowerPersonMapper(), superPerson.getSuperPersonID());
        superPerson.setPowers(powers);
        return superPerson;
    }

    private SuperPerson assosciateOrganisations(SuperPerson superPerson) {
       final String GET_HERO_OrganisationS = "SELECT * FROM SuperPerson sp "
                + "JOIN Organisation o ON sp.SuperPersonID = o.SuperPersonID "
                + "WHERE sp.SuperPersonID = ?";
        List<Organisation> Organisations = jdbc.query(GET_HERO_OrganisationS, new OrganisationPersonMapper(), superPerson.getSuperPersonID());
        superPerson.setOrganisations(Organisations);
        return superPerson;    
    }

    @Override
    public SuperPerson getSuperPersonByName(String superPersonName) {
        try {
            final String GET_SUPER_BY_NAME = "SELECT * FROM SuperPerson WHERE PersonName = ?";
            SuperPerson superPerson = jdbc.queryForObject(GET_SUPER_BY_NAME, new SuperPersonMapper(), superPersonName);   
            superPerson = assosciatePowers(superPerson);
            superPerson = assosciateOrganisations(superPerson);
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
            superPerson.setSuperPersonID(rs.getInt("SuperPersonID"));
            superPerson.setName(rs.getString("PersonName"));
            superPerson.setVillain(rs.getBoolean("IsVillain"));
            superPerson.setDescription(rs.getString("Description"));
            return superPerson;
        }
    }
    
    private static final class PowerPersonMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power power = new Power();
            power.setPowerID(rs.getInt("PowerID"));
            power.setName(rs.getString("PowerName"));
            power.setDescription(rs.getString("Description"));
            return power;
        }
    }
    
    private static final class OrganisationPersonMapper implements RowMapper<Organisation> {

        @Override
        public Organisation mapRow(ResultSet rs, int index) throws SQLException {
            Organisation Organisation = new Organisation();
            Organisation.setOrganisationID(rs.getInt("OrganisationID"));
            Organisation.setName(rs.getString("OrganisationName"));
            Organisation.setAlignment(rs.getString("Alignment"));
            Organisation.setDescription(rs.getString("Description"));
            Organisation.setLocationID(rs.getInt("LocationID"));
            Organisation.setPhone(rs.getString("PhoneNumber"));
            return Organisation;
        }
    }
    
}
