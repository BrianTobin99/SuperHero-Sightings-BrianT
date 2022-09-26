package com.briant.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.briant.dao.SightingDao;
import com.briant.dto.Location;
import com.briant.service.ServiceLayer;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class LocationController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    SightingDao sightingDao;
    
    Set<ConstraintViolation<Location>> violations = new HashSet<>();
    
    @GetMapping("locations")
    public String displayLocations(Model model){
        model.addAttribute("errors", violations);
        List<Location> locations = service.getLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }
    
    @PostMapping("addLocation")
    public String addLocation(HttpServletRequest request){
        violations.clear();
        Location location = service.createLocation(request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(location);

        if(violations.isEmpty()) {
            service.addLocation(location);
        }
        return "redirect:/locations";
    }
    
    @GetMapping("deleteLocation")
    public String deleteLocation(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteLocationById(id);
        return "redirect:/locations";
    }
    
    @GetMapping("editLocation")
    public String editLocation(HttpServletRequest request, Model model) {
        violations.clear();
        model.addAttribute("errors", violations);
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = service.getLocationById(id);

        model.addAttribute("location", location);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = service.getLocationById(id);
        location = service.editLocation(location, request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(location);
        
        if(violations.isEmpty()){
            service.updateLocation(location);
        }
        
        return "redirect:/locations";
        
    }    
}
