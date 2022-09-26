package com.briant.dto;

import java.sql.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

public class Sighting {
    private int sightingID, superPersonID, locationID;
   
    @NotNull(message = "InvalID super person")
    private SuperPerson superPerson;
   
    @NotNull(message = "InvalID location")
    private Location location;
    
    @NotNull(message = "Must enter a value for date")
    private Date sightingDate;
    
    private List<Organisation> organizations;
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
    
    public int getSightingID() {
        return sightingID;
    }

    public void setSightingID(int sightingID) {
        this.sightingID = sightingID;
    }

    public int getSuperPersonID() {
        return superPersonID;
    }

    public void setSuperPersonID(int superPersonID) {
        this.superPersonID = superPersonID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
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

    public List<Organisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organisation> organizations) {
        this.organizations = organizations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.sightingID;
        hash = 83 * hash + this.superPersonID;
        hash = 83 * hash + this.locationID;
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
        if (this.sightingID != other.sightingID) {
            return false;
        }
        if (this.superPersonID != other.superPersonID) {
            return false;
        }
        if (this.locationID != other.locationID) {
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
