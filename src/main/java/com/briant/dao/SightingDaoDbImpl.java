package com.briant.dao;

import com.briant.entities.Location;
import com.briant.entities.Sighting;
import com.briant.entities.SuperPerson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SightingDaoDbImpl implements SightingDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sighting getSightingById(int sightingId) {
        try {
            final String SELECT_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE sightingId = ?";
            Sighting sighting = jdbc.queryForObject(SELECT_SIGHTING_BY_ID, new SightingMapper(), sightingId);
            sighting = assosciateSuperPerson(sighting);
            sighting = assosciateLocation(sighting);
            return sighting;
        } 
        catch(DataAccessException ex) {
            return null;
        }   
    }

    @Override
    public List<Sighting> getAllHeroSightings() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM sighting s JOIN superperson sp ON s.superPersonId = sp.superPersonId WHERE sp.isVillain = ?";
        List<Sighting> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper(), false);
        sightings.forEach(sighting -> {
            sighting = assosciateSuperPerson(sighting);
            sighting = assosciateLocation(sighting);
        });
        return sightings;
    }

    @Override
    public List<Sighting> getAllVillainSightings() {  
        final String GET_ALL_SIGHTINGS = "SELECT * FROM sighting s JOIN superperson sp ON s.superPersonId = sp.superPersonId WHERE sp.isVillain = ?";
        List<Sighting> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper(), true); 
        sightings.forEach(sighting -> {
            sighting = assosciateSuperPerson(sighting);
            sighting = assosciateLocation(sighting);
        });
        return sightings;  
    }
    
    @Override
    public List<Sighting> getAllSightingsAtLocationDate(int locationId, LocalDate date) { //Broken
        final String GET_SIGHTINGS_LOCATIONDATE = "SELECT s.sightingId, s.superPersonId, s.locationId, s.sightingTime FROM sighting s "
                + "JOIN superperson sp ON s.superPersonId = sp.superPersonId "
                + "JOIN location l ON l.locationId = s.locationId "
                + "WHERE l.locationId = ? AND CAST(s.sightingTime AS DATE) = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_LOCATIONDATE, new SightingMapper(), locationId, date);
        sightings.forEach(sighting -> {
            sighting = assosciateSuperPerson(sighting);
            sighting = assosciateLocation(sighting);
        });
        return sightings;
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(superPersonId, locationId, sightingTime) VALUES (?,?,?)";
        jdbc.update(INSERT_SIGHTING, sighting.getSuperPersonId(), sighting.getLocationId(), sighting.getSightingDate());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setSightingId(newId);
        return sighting;
    }

    @Override
    @Transactional
    public void deleteSightingById(int sighting) {
        final String DELETE_SIGHTING = "DELETE s.* FROM sighting s WHERE sightingId = ?";
        jdbc.update(DELETE_SIGHTING, sighting);
    }

    @Override
    public void editSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET superPersonId = ?, locationId = ?, sightingTime = ? WHERE sightingId = ?";
        jdbc.update(UPDATE_SIGHTING, sighting.getSuperPersonId(), sighting.getLocationId(), sighting.getSightingDate(), sighting.getSightingId());
    }

    private Sighting assosciateSuperPerson(Sighting sighting) {
        final String GET_SIGHTING_SUPERPERSON = "SELECT * FROM superperson sp WHERE sp.superPersonId = ?";
        SuperPerson superPerson = jdbc.queryForObject(GET_SIGHTING_SUPERPERSON, new SuperPersonDaoDbImpl.SuperPersonMapper(), sighting.getSuperPersonId());
        sighting.setSuperPerson(superPerson);
        return sighting;
    }

    private Sighting assosciateLocation(Sighting sighting) {
        final String GET_SIGHTING_LOCATION = "SELECT * FROM location l WHERE l.locationId = ?";
        Location location = jdbc.queryForObject(GET_SIGHTING_LOCATION, new LocationDaoDbImpl.LocationMapper(), sighting.getLocationId());
        sighting.setLocation(location);
        return sighting;
    }

    @Override
    public List<Sighting> getSightingsPreview() {
        final String GET_SIGHTING_PREVIEW = "SELECT * FROM Sighting ORDER BY sightingTime limit 10";
        List<Sighting> sightings = jdbc.query(GET_SIGHTING_PREVIEW, new SightingMapper());
        
        sightings.forEach(sighting -> {
            sighting = assosciateSuperPerson(sighting);
            sighting = assosciateLocation(sighting);
        });    
        
        return sightings;
    }
    
    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setSightingId(rs.getInt("sightingId"));
            sighting.setSuperPersonId(rs.getInt("superPersonId"));
            sighting.setLocationId(rs.getInt("locationId"));
            sighting.setSightingDate(rs.getDate("sightingTime"));
            
            return sighting;
        }
    }

}
