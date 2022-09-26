package com.briant.dao;

import java.util.List;

import com.briant.dto.Power;

public interface PowerDao {
    Power getPowerById(int powerId);
    List<Power> getAllPowers();
    Power addPower(Power power);
    void deletePowerById(int powerId); 
    void editPower(Power power);

    public Power getPowerByName(String powerName);
}
