package com.briant.controllers;

import com.briant.dao.SightingDao;
import com.briant.entities.Sighting;
import com.briant.service.ServiceLayer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    SightingDao sightingDao;
    
    Set<ConstraintViolation<Sighting>> violations = new HashSet<>();
    
    @GetMapping("home")
    public String displayHome(Model model){
        model.addAttribute("errors", violations);
        List<Sighting> sightings = service.getSightingsPreview();
        model.addAttribute("sightings", sightings);
        return "home";
    }
}
