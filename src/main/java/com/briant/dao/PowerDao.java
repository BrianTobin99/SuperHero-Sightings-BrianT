package com.briant.dao;

import java.util.List;

import com.briant.dto.Power;

public interface PowerDao {
    Power getPowerByID(int powerId);
    List<Power> getAllPowers();
    Power addPower(Power power);
    void deletePowerByID(int powerId); 
    void editPower(Power power);

    public Power getPowerByName(String powerName);
}
