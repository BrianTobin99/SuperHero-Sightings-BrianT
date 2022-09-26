package com.briant.dao;


import java.time.LocalDate;
import java.util.List;

import com.briant.dto.Sighting;

public interface SightingDao {
    Sighting getSightingById(int heroSightingId);
    List<Sighting> getAllHeroSightings();
    List<Sighting> getAllVillainSightings();
    Sighting addSighting(Sighting heroSighting);
    void deleteSightingById(int heroSightingId);
    void editSighting(Sighting heroSighting);
    public List<Sighting> getAllSightingsAtLocationDate(int locationId, LocalDate date);

    public List<Sighting> getSightingsPreview();
}
