package com.briant.entities;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class Power {
    private int powerId;
    
    @NotBlank(message = "Must enter a value for name")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;
    
    @NotBlank(message = "Must enter a value for description")
    @Size(max = 100, message = "Description must be less than 100 characters")
    private String description;

    public int getPowerId() {
        return powerId;
    }

    public void setPowerId(int powerId) {
        this.powerId = powerId;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.powerId;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.description);
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
        final Power other = (Power) obj;
        if (this.powerId != other.powerId) {
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
    
    
}
