package com.briant.entities;

import java.sql.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

public class Sighting {
    private int sightingId, superPersonId, locationId;
   
    @NotNull(message = "Invalid super person")
    private SuperPerson superPerson;
   
    @NotNull(message = "Invalid location")
    private Location location;
    
    @NotNull(message = "Must enter a value for date")
    private Date sightingDate;
    
    private List<Organization> organizations;
    private List<Power> powers;
    private String organizationNames, powerNames;

    public List<Power> getPowers() {
        return powers;
    }

    public void setPowers(List<Power> powers) {
        this.powers = powers;
    }
    
    public Sighting(){
        organizationNames = "";
        powerNames = "";
    }
    
    public int getSightingId() {
        return sightingId;
    }

    public void setSightingId(int sightingId) {
        this.sightingId = sightingId;
    }

    public int getSuperPersonId() {
        return superPersonId;
    }

    public void setSuperPersonId(int superPersonId) {
        this.superPersonId = superPersonId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public SuperPerson getSuperPerson() {
        return superPerson;
    }

    public void setSuperPerson(SuperPerson superPerson) {
        this.superPerson = superPerson;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getSightingDate() {
        return sightingDate;
    }

    public void setSightingDate(Date sightingDate) {
        this.sightingDate = sightingDate;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.sightingId;
        hash = 83 * hash + this.superPersonId;
        hash = 83 * hash + this.locationId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sighting other = (Sighting) obj;
        if (this.sightingId != other.sightingId) {
            return false;
        }
        if (this.superPersonId != other.superPersonId) {
            return false;
        }
        if (this.locationId != other.locationId) {
            return false;
        }
        return true;
    }

    public void appendOrgNames(String name) {
        this.organizationNames += name + "<br>";
    }
    
    public void appendPowerNames(String name) {
        this.powerNames += name + "<br>";
    }
    
    public String getOrganizationNames(){
        return organizationNames;
    }
    
    public String getPowerNames(){
        return powerNames;
    }

 



    
    
}
