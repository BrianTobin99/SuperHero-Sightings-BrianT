package com.briant.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.briant.dao.LocationDao;
import com.briant.dao.OrganisationDao;
import com.briant.dao.PowerDao;
import com.briant.dao.SightingDao;
import com.briant.dao.SuperPersonDao;
import com.briant.dto.Location;
import com.briant.dto.Organisation;
import com.briant.dto.Power;
import com.briant.dto.Sighting;
import com.briant.dto.SuperPerson;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ServiceLayer {
    @Autowired
    LocationDao locDao;
    
    @Autowired
    OrganisationDao orgDao;
    
    @Autowired
    PowerDao powerDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    SuperPersonDao superPersonDao;
    
    Set<ConstraintViolation<Sighting>> violations = new HashSet<>();
    public List<Sighting> getSightings() {
        List<Sighting> sightings = sightingDao.getAllVillainSightings();
        sightings.addAll(sightingDao.getAllHeroSightings());
        sightings.forEach(sighting ->{
            sighting.setSuperPerson(superPersonDao.getSuperByID(sighting.getSuperPersonID()));
            sighting.setLocation(locDao.getLocationByID(sighting.getLocationID()));
            sighting.setOrganisations(sighting.getSuperPerson().getOrganisations());
            sighting.setPowers(sighting.getSuperPerson().getPowers());
            sighting.getOrganisations().forEach(org -> {
                    sighting.appendOrgNames(org.getName());
            });
            sighting.getPowers().forEach(power -> {
                    sighting.appendPowerNames(power.getName());
            });
        });
        

        
        
        return sightings;
    }

    public List<SuperPerson> getVillains() {
        List<SuperPerson> villains = superPersonDao.getAllVillains();
        villains.forEach(villain -> {
            villain.getOrganisations().forEach(org -> {
                villain.appendOrgNames(org.getName());
            });
            villain.getPowers().forEach(power -> {
                villain.appendPowerNames(power.getName());
            });
        });
        
        
        return villains;
    }
    
    public List<SuperPerson> getHeroes() {
        List<SuperPerson> heroes = superPersonDao.getAllHeroes();
        heroes.forEach(hero -> {
            hero.getOrganisations().forEach(org -> {
                hero.appendOrgNames(org.getName());
            });
            hero.getPowers().forEach(power -> {
                hero.appendPowerNames(power.getName());
            });
        });
        
        
        return heroes;
    }

    public List<Power> getPowers() {
        return powerDao.getAllPowers();
    }

    public List<Organisation> getOrganisations() {
        return orgDao.getAllOrganisations();
    }

    public List<Location> getLocations() {
        return locDao.getAllLocations();
    }

    public void deleteSightingByID(int ID) {
        sightingDao.deleteSightingByID(ID);
    }

    public void addSighting(Sighting sighting) {
        sightingDao.addSighting(sighting);
    }

    public Sighting createSighting(HttpServletRequest request) {
        Sighting sighting = new Sighting();
        String superPersonName = request.getParameter("superPersonName");
        String locationName = request.getParameter("locationName");
        
        SuperPerson superPerson = superPersonDao.getSuperPersonByName(superPersonName);
        Location location = locDao.getLocationByName(locationName);
        
        if(location != null){
            sighting.setLocation(location);
            sighting.setLocationID(location.getLocationID());
        }
        if(superPerson != null){
            sighting.setSuperPerson(superPerson);
            sighting.setSuperPersonID(superPerson.getSuperPersonID());
        }
        
        sighting.setSightingTime(Date.valueOf(LocalDate.now()));
        
        return sighting;
    }

    public Sighting getSightingByID(int ID) {
        return sightingDao.getSightingByID(ID);
    }

    public Sighting editSighting(Sighting sighting, HttpServletRequest request) {
        
        SuperPerson superPerson = superPersonDao.getSuperPersonByName(request.getParameter("superPersonName"));
        Location location = locDao.getLocationByName(request.getParameter("locationName"));
        Date date = null;
        if(request.getParameter("dateTime").equals("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")){
            date = Date.valueOf(request.getParameter("dateTime"));
        }
        
        if(location != null){
            sighting.setLocation(location);
            sighting.setLocationID(location.getLocationID());
        }
        if(superPerson != null){
            sighting.setSuperPerson(superPerson);
            sighting.setSuperPersonID(superPerson.getSuperPersonID());
        }
        if(date != null){
            sighting.setSightingTime(date);
        }
        return sighting;
    }

    public void updateSighting(Sighting sighting) {
        sightingDao.editSighting(sighting);
    }

    public Organisation createOrganisation(HttpServletRequest request) {
        Organisation organisation = new Organisation();
        

        String locationName = request.getParameter("locationName");
        String organisationType = request.getParameter("organisationType");
        String organisationName = request.getParameter("organisationName");
        String organisationDesc = request.getParameter("organisationDesc");
        String organisationPhone = request.getParameter("organisationPhone");
        
        if(locDao.getLocationByName(locationName) != null){
            organisation.setLocation(locDao.getLocationByName(locationName));
            organisation.setLocationID(organisation.getLocation().getLocationID());
        }
        organisation.setAlignment(organisationType);
        organisation.setName(organisationName);
        organisation.setDescription(organisationDesc);
        organisation.setPhone(organisationPhone);
        
        return organisation;
    }

    public void addOrganisation(Organisation organisation) {
        orgDao.addOrganisation(organisation);
    }

    public void deleteOrganisationByID(int ID) {
        orgDao.deleteOrganisationByID(ID);
    }

    public Organisation getOrganisationByID(int ID) {
        return orgDao.getOrganisationByID(ID);
    }

    public Organisation editOrganisation(Organisation organisation, HttpServletRequest request) {
        String locationName = request.getParameter("locationName");
        String organisationType = request.getParameter("organisationAlignment");
        String organisationName = request.getParameter("organisationName");
        String organisationDesc = request.getParameter("organisationDesc");
        String organisationPhone = request.getParameter("organisationPhone");
        
         if(locDao.getLocationByName(locationName) != null){
            organisation.setLocation(locDao.getLocationByName(locationName));
            organisation.setLocationID(organisation.getLocation().getLocationID());
         }
        organisation.setAlignment(organisationType);
        organisation.setName(organisationName);
        organisation.setDescription(organisationDesc);
        organisation.setPhone(organisationPhone);
        return organisation;
    }

    public void updateOrganisation(Organisation organisation) {
        orgDao.editOrganisation(organisation);
    }

    public void addSuperPerson(SuperPerson superPerson) {
        superPersonDao.addSuper(superPerson);
    }

    public void deleteSuperPersonByID(int ID) {
        superPersonDao.deleteSuperByID(ID);
    }

    public SuperPerson getSuperPersonByID(int ID) {
        return superPersonDao.getSuperByID(ID);
    }

    public SuperPerson editSuperPerson(SuperPerson superPerson, HttpServletRequest request) {
        String name = request.getParameter("superPersonName");
        String description = request.getParameter("superPersonDesc");
        String organisationNames[] = request.getParameterValues("organisationNames");
        String powerNames[] = request.getParameterValues("powerNames");
        
        List<Organisation> organisations = new ArrayList<>();
        List<Power> powers = new ArrayList<>();
        
        for(String orgName : organisationNames){
            organisations.add(orgDao.getOrganisationByName(orgName));
        }
        
        for(String powerName : powerNames){
            powers.add(powerDao.getPowerByName(powerName));
        }
          
        superPerson.setName(name);
        superPerson.setDescription(description);
        superPerson.setOrganisations(organisations);
        superPerson.setPowers(powers);
        
        return superPerson;
    }

    public void updateSuperPerson(SuperPerson superPerson) {
        superPersonDao.editSuper(superPerson);
    }

    public Power createPower(HttpServletRequest request) {
        Power power = new Power();
        String name = request.getParameter("powerName");
        String description = request.getParameter("powerDesc");
        if(name != null){
            power.setName(name);
        }
        if(description != null){
            power.setDescription(description);
        }
        
        return power; 
    }

    public void addPower(Power power) {
        powerDao.addPower(power);
    }

    public void deletePowerByID(int ID) {
        powerDao.deletePowerByID(ID);
    }

    public Power getPowerByID(int ID) {
        return powerDao.getPowerByID(ID);
    }

    public Power editPower(Power power, HttpServletRequest request) {
        String name = request.getParameter("powerName");
        String description = request.getParameter("powerDesc");
        if(name != null){
            power.setName(name);
        }
        if(description != null){
            power.setDescription(description);
        }
        
        return power;     }

    public void updatePower(Power power) {
        powerDao.editPower(power);
    }

    public Location createLocation(HttpServletRequest request) {
        Location location = new Location();
        String name = request.getParameter("locationName");
        String city = request.getParameter("locationCity");
        String state = request.getParameter("locationState");
        String address = request.getParameter("locationAddress");
        String coord = request.getParameter("locationCoord");
        String description = request.getParameter("locationDesc");
        
        location.setName(name);
        location.setCity(city);
        location.setState(state);
        location.setAddress(address);
        location.setCoordinates(coord);
        location.setDescription(description);
        return location;
    }

    public void addLocation(Location location) {
        locDao.addLocation(location);
    }

    public void deleteLocationByID(int ID) {
        locDao.deleteLocationByID(ID);
    }

    public Location getLocationByID(int ID) {
        return locDao.getLocationByID(ID);
    }

    public Location editLocation(Location location, HttpServletRequest request) {
        String name = request.getParameter("locationName");
        String city = request.getParameter("locationCity");
        String state = request.getParameter("locationState");
        String address = request.getParameter("locationAddress");
        String coord = request.getParameter("locationCoord");
        String description = request.getParameter("locationDesc");
        
        location.setName(name);
        location.setCity(city);
        location.setState(state);
        location.setAddress(address);
        location.setCoordinates(coord);
        location.setDescription(description);    
        return location;
    }

    public void updateLocation(Location location) {
        locDao.editLocation(location);
    }

    public List<Sighting> getSightingsPreview() {
        
        List<Sighting> sightings = sightingDao.getAllVillainSightings();
        sightings.addAll(sightingDao.getAllHeroSightings());
        sightings.forEach(sighting ->{
            sighting.setSuperPerson(superPersonDao.getSuperByID(sighting.getSuperPersonID()));
            sighting.setLocation(locDao.getLocationByID(sighting.getLocationID()));
            sighting.setOrganisations(sighting.getSuperPerson().getOrganisations());
            sighting.setPowers(sighting.getSuperPerson().getPowers());
            sighting.getOrganisations().forEach(org -> {
                    sighting.appendOrgNames(org.getName());
            });
            sighting.getPowers().forEach(power -> {
                    sighting.appendPowerNames(power.getName());
            });
        });
        
        return sightings;
    }
}
