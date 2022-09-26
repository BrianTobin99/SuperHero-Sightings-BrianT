package com.briant.dao;

import com.briant.entities.Location;
import java.util.List;

public interface LocationDao {
    Location getLocationById(int locationId);
    List<Location> getAllLocations();
    Location addLocation(Location location);
    void deleteLocationById(int locationId);
    void editLocation(Location location);

    public Location getLocationByName(String locationName);
}
