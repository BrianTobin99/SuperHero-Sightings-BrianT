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
import com.briant.dao.OrganizationDao;
import com.briant.dao.PowerDao;
import com.briant.dao.SightingDao;
import com.briant.dao.SuperPersonDao;
import com.briant.dto.Location;
import com.briant.dto.Organization;
import com.briant.dto.Power;
import com.briant.dto.Sighting;
import com.briant.dto.SuperPerson;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ServiceLayer {
    @Autowired
    LocationDao locDao;
    
    @Autowired
    OrganizationDao orgDao;
    
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
            sighting.setSuperPerson(superPersonDao.getSuperById(sighting.getSuperPersonId()));
            sighting.setLocation(locDao.getLocationById(sighting.getLocationId()));
            sighting.setOrganizations(sighting.getSuperPerson().getOrganizations());
            sighting.setPowers(sighting.getSuperPerson().getPowers());
            sighting.getOrganizations().forEach(org -> {
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
            villain.getOrganizations().forEach(org -> {
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
            hero.getOrganizations().forEach(org -> {
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

    public List<Organization> getOrganizations() {
        return orgDao.getAllOrganizations();
    }

    public List<Location> getLocations() {
        return locDao.getAllLocations();
    }

    public void deleteSightingById(int id) {
        sightingDao.deleteSightingById(id);
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
            sighting.setLocationId(location.getLocationId());
        }
        if(superPerson != null){
            sighting.setSuperPerson(superPerson);
            sighting.setSuperPersonId(superPerson.getSuperPersonId());
        }
        
        sighting.setSightingDate(Date.valueOf(LocalDate.now()));
        
        return sighting;
    }

    public Sighting getSightingById(int id) {
        return sightingDao.getSightingById(id);
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
            sighting.setLocationId(location.getLocationId());
        }
        if(superPerson != null){
            sighting.setSuperPerson(superPerson);
            sighting.setSuperPersonId(superPerson.getSuperPersonId());
        }
        if(date != null){
            sighting.setSightingDate(date);
        }
        return sighting;
    }

    public void updateSighting(Sighting sighting) {
        sightingDao.editSighting(sighting);
    }

    public Organization createOrganization(HttpServletRequest request) {
        Organization organization = new Organization();
        

        String locationName = request.getParameter("locationName");
        String organizationType = request.getParameter("organizationType");
        String organizationName = request.getParameter("organizationName");
        String organizationDesc = request.getParameter("organizationDesc");
        String organizationPhone = request.getParameter("organizationPhone");
        
        if(locDao.getLocationByName(locationName) != null){
            organization.setLocation(locDao.getLocationByName(locationName));
            organization.setLocationId(organization.getLocation().getLocationId());
        }
        organization.setType(organizationType);
        organization.setName(organizationName);
        organization.setDescription(organizationDesc);
        organization.setPhone(organizationPhone);
        
        return organization;
    }

    public void addOrganization(Organization organization) {
        orgDao.addOrganization(organization);
    }

    public void deleteOrganizationById(int id) {
        orgDao.deleteOrganizationById(id);
    }

    public Organization getOrganizationById(int id) {
        return orgDao.getOrganizationById(id);
    }

    public Organization editOrganization(Organization organization, HttpServletRequest request) {
        String locationName = request.getParameter("locationName");
        String organizationType = request.getParameter("organizationType");
        String organizationName = request.getParameter("organizationName");
        String organizationDesc = request.getParameter("organizationDesc");
        String organizationPhone = request.getParameter("organizationPhone");
        
         if(locDao.getLocationByName(locationName) != null){
            organization.setLocation(locDao.getLocationByName(locationName));
            organization.setLocationId(organization.getLocation().getLocationId());
         }
        organization.setType(organizationType);
        organization.setName(organizationName);
        organization.setDescription(organizationDesc);
        organization.setPhone(organizationPhone);
        return organization;
    }

    public void updateOrganization(Organization organization) {
        orgDao.editOrganization(organization);
    }

    public void addSuperPerson(SuperPerson superPerson) {
        superPersonDao.addSuper(superPerson);
    }

    public void deleteSuperPersonById(int id) {
        superPersonDao.deleteSuperById(id);
    }

    public SuperPerson getSuperPersonById(int id) {
        return superPersonDao.getSuperById(id);
    }

    public SuperPerson editSuperPerson(SuperPerson superPerson, HttpServletRequest request) {
        String name = request.getParameter("superPersonName");
        String description = request.getParameter("superPersonDesc");
        String organizationNames[] = request.getParameterValues("organizationNames");
        String powerNames[] = request.getParameterValues("powerNames");
        
        List<Organization> organizations = new ArrayList<>();
        List<Power> powers = new ArrayList<>();
        
        for(String orgName : organizationNames){
            organizations.add(orgDao.getOrganizationByName(orgName));
        }
        
        for(String powerName : powerNames){
            powers.add(powerDao.getPowerByName(powerName));
        }
          
        superPerson.setName(name);
        superPerson.setDescription(description);
        superPerson.setOrganizations(organizations);
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

    public void deletePowerById(int id) {
        powerDao.deletePowerById(id);
    }

    public Power getPowerById(int id) {
        return powerDao.getPowerById(id);
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

    public void deleteLocationById(int id) {
        locDao.deleteLocationById(id);
    }

    public Location getLocationById(int id) {
        return locDao.getLocationById(id);
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
            sighting.setSuperPerson(superPersonDao.getSuperById(sighting.getSuperPersonId()));
            sighting.setLocation(locDao.getLocationById(sighting.getLocationId()));
            sighting.setOrganizations(sighting.getSuperPerson().getOrganizations());
            sighting.setPowers(sighting.getSuperPerson().getPowers());
            sighting.getOrganizations().forEach(org -> {
                    sighting.appendOrgNames(org.getName());
            });
            sighting.getPowers().forEach(power -> {
                    sighting.appendPowerNames(power.getName());
            });
        });
        
        return sightings;
    }
}
