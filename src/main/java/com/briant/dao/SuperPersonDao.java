package com.briant.dao;

import java.util.List;

import com.briant.dto.SuperPerson;

public interface SuperPersonDao {
    SuperPerson getSuperById(int heroId);
    List<SuperPerson> getAllVillains();
    List<SuperPerson> getAllHeroes();
    SuperPerson addSuper(SuperPerson hero);
    void deleteSuperById(int heroId);
    void editSuper(SuperPerson hero);

    public SuperPerson getSuperPersonByName(String superPersonName);
}
