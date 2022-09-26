package com.briant.dao;

import com.briant.entities.SuperPerson;
import java.util.List;

public interface SuperPersonDao {
    SuperPerson getSuperById(int heroId);
    List<SuperPerson> getAllVillains();
    List<SuperPerson> getAllHeroes();
    SuperPerson addSuper(SuperPerson hero);
    void deleteSuperById(int heroId);
    void editSuper(SuperPerson hero);

    public SuperPerson getSuperPersonByName(String superPersonName);
}
