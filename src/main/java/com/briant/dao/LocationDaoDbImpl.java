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

import com.briant.dto.Location;


@Repository
public class LocationDaoDbImpl implements LocationDao {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Location getLocationByID(int locationId) {
        try {
            final String SELECT_ORGANISATION_BY_ID = "SELECT * FROM Location WHERE LocationID = ?";
            Location location = jdbc.queryForObject(SELECT_ORGANISATION_BY_ID, new LocationMapper(), locationId);
            return location;
        } 
        catch(DataAccessException ex) {
            return null;
        }           
    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCATIONS = "SELECT * FROM Location";
        return jdbc.query(GET_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO Location(LocationName, City, State, Address,"
                + " Coordinates, Description) VALUES (?,?,?,?,?,?)";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getCity(),
                location.getState(),
                location.getAddress(),
                location.getCoordinates(),
                location.getDescription());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setLocationID(newId);
        return location;
    }

    @Override
    @Transactional
    public void deleteLocationByID(int locationId) {
        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE LocationID = ?";
        jdbc.update(DELETE_SIGHTING, locationId);
        
        final String DELETE_ORGANIZATION = "DELETE FROM Organization WHERE LocationID = ?";
        jdbc.update(DELETE_ORGANIZATION, locationId);
        
        final String DELETE_LOCATION = "DELETE FROM Location l WHERE LocationID = ?";
        jdbc.update(DELETE_LOCATION, locationId);
    }

    @Override
    public void editLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE Location SET LocationName = ?, City = ?, "
                + " State = ?, Address = ?, Coordinates = ?, Description = ? WHERE LocationID = ?";
        jdbc.update(UPDATE_LOCATION, location.getName(), location.getCity(), 
                location.getState(), location.getAddress(), location.getCoordinates(), 
                location.getDescription(), location.getLocationID());    
    }

    @Override
    public Location getLocationByName(String locationName) {
        try {
            final String GET_LOCATION_BY_NAME = "SELECT  * FROM Location WHERE LocationName = ?";
            return jdbc.queryForObject(GET_LOCATION_BY_NAME, new LocationMapper(), locationName);
        } 
        catch(DataAccessException ex) {
            return null;
        }   

    }
    
    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setLocationID(rs.getInt("locationId"));
            location.setName(rs.getString("locationName"));
            location.setCity(rs.getString("locationCity"));
            location.setState(rs.getString("locationState"));
            location.setAddress(rs.getString("locationAddress"));
            location.setCoordinates(rs.getString("locationCoord"));
            location.setDescription(rs.getString("locationDesc"));

            return location;
        }
    }

}
