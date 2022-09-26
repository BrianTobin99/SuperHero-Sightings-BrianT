package com.briant.dao;

import com.briant.entities.Power;
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
public class PowerDaoDbImpl implements PowerDao {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Power getPowerById(int powerId) {
        try {
            final String SELECT_POWER_BY_ID = "SELECT * FROM power WHERE powerId = ?";
            Power power = jdbc.queryForObject(SELECT_POWER_BY_ID, new PowerMapper(), powerId);
            return power;
        } 
        catch(DataAccessException ex) {
            return null;
        }   
    }

    @Override
    public List<Power> getAllPowers() {
        final String GET_ALL_POWERS = "SELECT * FROM power";
        return jdbc.query(GET_ALL_POWERS, new PowerMapper());
    }

    @Override
    @Transactional
    public Power addPower(Power power) {
        final String INSERT_POWER = "INSERT INTO power(powerName, powerDesc) VALUES (?,?)";
        jdbc.update(INSERT_POWER, power.getName(), power.getDescription());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        power.setPowerId(newId);
        return power;
    }

    @Override
    @Transactional
    public void deletePowerById(int powerId) {
        final String DELETE_HEROVILLAINPOWER = "DELETE hvp.* FROM superpersonpower hvp WHERE powerId = ?";
        jdbc.update(DELETE_HEROVILLAINPOWER, powerId);
        
        final String DELETE_POWER = "DELETE p.* FROM power p WHERE powerId = ?";
        jdbc.update(DELETE_POWER, powerId);
    }

    @Override
    public void editPower(Power power) {
        final String UPDATE_POWER = "UPDATE power SET powerName = ?, powerDesc = ? WHERE powerId = ?";
        jdbc.update(UPDATE_POWER, power.getName(), power.getDescription(), power.getPowerId());
    }

    @Override
    public Power getPowerByName(String powerName) {
        final String GET_POWER_BY_NAME = "SELECT * FROM power WHERE powerName = ?";
        Power power = jdbc.queryForObject(GET_POWER_BY_NAME, new PowerMapper(), powerName);
        return power;    
    }
    
    private static final class PowerMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power power = new Power();
            power.setPowerId(rs.getInt("powerId"));
            power.setName(rs.getString("powerName"));
            power.setDescription(rs.getString("powerDesc"));
            return power;
        }
    }
}
