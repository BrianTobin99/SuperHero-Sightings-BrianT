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
import com.briant.entities.Sighting;
import com.briant.service.ServiceLayer;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SightingController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    SightingDao sightingDao;
    
    Set<ConstraintViolation<Sighting>> violations = new HashSet<>();
    
    @GetMapping("sightings")
    public String displaySightings(Model model){
        model.addAttribute("errors", violations);
        List<Sighting> sightings = service.getSightings();
        model.addAttribute("sightings", sightings);
        return "sightings";
    }
    
    @GetMapping("deleteSighting")
    public String deleteSighting(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteSightingById(id);
        return "redirect:/sightings";
    }
    
    @PostMapping("addSighting")
    public String addSighting(HttpServletRequest request) {
        violations.clear();
        Sighting sighting = service.createSighting(request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);

        if(violations.isEmpty()) {
            sightingDao.addSighting(sighting);
        }
        return "redirect:/sightings";
    }
    
    @GetMapping("editSighting")
    public String editSighting(HttpServletRequest request, Model model) {
        violations.clear();
        model.addAttribute("errors", violations);
        int id = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = service.getSightingById(id);
        model.addAttribute("sighting", sighting);
        
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = service.getSightingById(id);
        sighting = service.editSighting(sighting, request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);
        
        if(violations.isEmpty()){
            service.updateSighting(sighting);
            
        }
        
        return "redirect:/sightings";
    }
}
