package com.briant.dao;


import com.briant.entities.Sighting;
import java.time.LocalDate;
import java.util.List;

public interface SightingDao {
    Sighting getSightingById(int heroSightingId);
    List<Sighting> getAllHeroSightings();
    List<Sighting> getAllVillainSightings();
    Sighting addSighting(Sighting heroSighting);
    void deleteSightingById(int heroSightingId); //boolean for verification?
    void editSighting(Sighting heroSighting);
    public List<Sighting> getAllSightingsAtLocationDate(int locationId, LocalDate date);

    public List<Sighting> getSightingsPreview();
}
