package com.briant.dao;

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

import com.briant.dto.Location;
import com.briant.dto.Sighting;
import com.briant.dto.SuperPerson;

@Repository
public class SightingDaoDbImpl implements SightingDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sighting getSightingByID(int sightingId) {
        try {
            final String SELECT_SIGHTING_BY_ID = "SELECT * FROM Sighting WHERE SightingID = ?";
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
        final String GET_ALL_SIGHTINGS = "SELECT * FROM Sighting s JOIN SuperPerson sp ON s.SuperPersonID = sp.SuperPersonID WHERE sp.IsVillain = ?";
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
    public List<Sighting> getAllSightingsAtLocationDate(int locationId, LocalDate date) {
        final String GET_SIGHTINGS_LOCATIONDATE = "SELECT s.SightingID, s.SuperPersonID, s.LocationID, s.SightingTime FROM Sighting s "
                + "JOIN SuperPerson sp ON s.SuperPersonID = sp.SuperPersonID "
                + "JOIN Location l ON l.LocationID = s.LocationID "
                + "WHERE l.LocationID = ? AND CAST(s.SightingTime AS DATE) = ?";
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
        final String INSERT_SIGHTING = "INSERT INTO Sighting(SuperPersonId, LocationID, SightingTime) VALUES (?,?,?)";
        jdbc.update(INSERT_SIGHTING, sighting.getSuperPersonID(), sighting.getLocationID(), sighting.getSightingTime());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setSightingID(newId);
        return sighting;
    }

    @Override
    @Transactional
    public void deleteSightingByID(int sighting) {
        final String DELETE_SIGHTING = "DELETE s.* FROM Sighting s WHERE SightingID = ?";
        jdbc.update(DELETE_SIGHTING, sighting);
    }

    @Override
    public void editSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE Sighting SET SuperPersonID = ?, LocationID = ?, SightingTime = ? WHERE SightingID = ?";
        jdbc.update(UPDATE_SIGHTING, sighting.getSuperPersonID(), sighting.getLocationID(), sighting.getSightingTime(), sighting.getSightingID());
    }

    private Sighting assosciateSuperPerson(Sighting sighting) {
        final String GET_SIGHTING_SUPERPERSON = "SELECT * FROM SuperPerson sp WHERE sp.SuperPersonID = ?";
        SuperPerson superPerson = jdbc.queryForObject(GET_SIGHTING_SUPERPERSON, new SuperPersonDaoDbImpl.SuperPersonMapper(), sighting.getSuperPersonID());
        sighting.setSuperPerson(superPerson);
        return sighting;
    }

    private Sighting assosciateLocation(Sighting sighting) {
        final String GET_SIGHTING_LOCATION = "SELECT * FROM Location l WHERE l.LocationID = ?";
        Location location = jdbc.queryForObject(GET_SIGHTING_LOCATION, new LocationDaoDbImpl.LocationMapper(), sighting.getLocationID());
        sighting.setLocation(location);
        return sighting;
    }

    @Override
    public List<Sighting> getSightingsPreview() {
        final String GET_SIGHTING_PREVIEW = "SELECT * FROM Sighting ORDER BY SightingTime limit 10";
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
            sighting.setSightingID(rs.getInt("sightingId"));
            sighting.setSuperPersonID(rs.getInt("superPersonId"));
            sighting.setLocationID(rs.getInt("locationId"));
            sighting.setSightingTime(rs.getDate("sightingTime"));
            
            return sighting;
        }
    }

}
