package com.briant.dao;

import java.util.List;

import com.briant.dto.SuperPerson;

public interface SuperPersonDao {
    SuperPerson getSuperByID(int heroID);
    List<SuperPerson> getAllVillains();
    List<SuperPerson> getAllHeroes();
    SuperPerson addSuper(SuperPerson hero);
    void deleteSuperByID(int heroID);
    void editSuper(SuperPerson hero);

    public SuperPerson getSuperPersonByName(String superPersonName);
}
