package com.briant.dto;

import java.util.List;
import java.util.Objects;

public class SuperPerson {
    private int superPersonID;
    private String name, description;
    private boolean villain;
    private List<Organisation> organisations;
    private List<Power> powers;
    String organisationNames, powerNames;
    
    public SuperPerson(){
        organisationNames = "";
        powerNames = "";
    }

    public SuperPerson(String name, String description, boolean villain) {
        this.name = name;
        this.description = description;
        this.villain = villain;
    }
    public int getSuperPersonID() {
        return superPersonID;
    }

    public void setSuperPersonID(int superPersonID) {
        this.superPersonID = superPersonID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVillain() {
        return villain;
    }

    public void setVillain(boolean villain) {
        this.villain = villain;
    }

    public List<Organisation> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(List<Organisation> organisations) {
        this.organisations = organisations;
    }

    public List<Power> getPowers() {
        return powers;
    }

    public void setPowers(List<Power> powers) {
        this.powers = powers;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.superPersonID;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.description);
        hash = 71 * hash + (this.villain ? 1 : 0);
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
        final SuperPerson other = (SuperPerson) obj;
        if (this.superPersonID != other.superPersonID) {
            return false;
        }
        if (this.villain != other.villain) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    public void appendOrgNames(String name) {
        this.organisationNames += name + "<br>";
    }
    
    public void appendPowerNames(String name) {
        this.powerNames += name + "<br>";
    }
    
    public String getOrganisationNames(){
        return organisationNames;
    }
    
    public String getPowerNames(){
        return powerNames;
    }

    
}
