package com.briant.dao;

import com.briant.entities.Power;
import java.util.List;

public interface PowerDao {
    Power getPowerById(int powerId);
    List<Power> getAllPowers();
    Power addPower(Power power);
    void deletePowerById(int powerId); 
    void editPower(Power power);

    public Power getPowerByName(String powerName);
}
