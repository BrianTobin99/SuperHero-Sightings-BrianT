package com.briant.dao;

import com.briant.entities.Location;
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
public class LocationDaoDbImpl implements LocationDao {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Location getLocationById(int locationId) {
        try {
            final String SELECT_ORGANIZATION_BY_ID = "SELECT * FROM location WHERE locationId = ?";
            Location location = jdbc.queryForObject(SELECT_ORGANIZATION_BY_ID, new LocationMapper(), locationId);
            return location;
        } 
        catch(DataAccessException ex) {
            return null;
        }           
    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCATIONS = "SELECT * FROM location";
        return jdbc.query(GET_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO location(locationName, locationCity, locationState, "
                + "locationAddress, locationCoord, locationDesc) VALUES (?,?,?,?,?,?)";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getCity(),
                location.getState(),
                location.getAddress(),
                location.getCoordinates(),
                location.getDescription());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setLocationId(newId);
        return location;
    }

    @Override
    @Transactional
    public void deleteLocationById(int locationId) {
        final String DELETE_SIGHTING = "DELETE s.* FROM sighting s WHERE locationId = ?";
        jdbc.update(DELETE_SIGHTING, locationId);
        
        final String DELETE_ORGHEROVILLAIN = "DELETE ohv.* FROM organizationsuperperson ohv "
                + "JOIN organization o ON ohv.organizationId = o.organizationId WHERE o.locationId = ?";
        jdbc.update(DELETE_ORGHEROVILLAIN, locationId);
        
        final String DELETE_ORGANIZATION = "DELETE o.* FROM organization o WHERE locationId = ?";
        jdbc.update(DELETE_ORGANIZATION, locationId);
        
        final String DELETE_LOCATION = "DELETE l.* FROM location l WHERE locationId = ?";
        jdbc.update(DELETE_LOCATION, locationId);
    }

    @Override
    public void editLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE location SET locationName = ?, locationCity = ?, "
                + " locationState = ?, locationAddress = ?, locationCoord = ?, locationDesc = ? WHERE locationId = ?";
        jdbc.update(UPDATE_LOCATION, location.getName(), location.getCity(), 
                location.getState(), location.getAddress(), location.getCoordinates(), 
                location.getDescription(), location.getLocationId());    
    }

    @Override
    public Location getLocationByName(String locationName) {
        try {
            final String GET_LOCATION_BY_NAME = "SELECT  * FROM location WHERE locationName = ?";
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
            location.setLocationId(rs.getInt("locationId"));
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
