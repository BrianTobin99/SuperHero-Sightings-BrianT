package com.briant.dao;

import java.util.List;

import com.briant.dto.Location;

public interface LocationDao {
    Location getLocationById(int locationId);
    List<Location> getAllLocations();
    Location addLocation(Location location);
    void deleteLocationById(int locationId);
    void editLocation(Location location);

    public Location getLocationByName(String locationName);
}
