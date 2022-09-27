package com.briant.controllers;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.briant.dao.SightingDao;
import com.briant.dto.Organisation;
import com.briant.dto.Power;
import com.briant.dto.SuperPerson;
import com.briant.service.ServiceLayer;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class VillainController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    SightingDao sightingDao;
    
    Set<ConstraintViolation<SuperPerson>> violations = new HashSet<>();
    
    @GetMapping("villains")
    public String displayVillains(Model model){
        model.addAttribute("errors", violations);
        List<SuperPerson> villains = service.getVillains();
        model.addAttribute("villains", villains);
        model.addAttribute("powers", service.getPowers());
        model.addAttribute("organisations", service.getOrganisations());
        return "villains";
    }

    @PostMapping("addVillain")
    public String addVillain(@RequestParam(value = "superPersonName", required = true) String name,
                             @RequestParam(value = "superPersonDesc", required = false) String description,
                             @RequestParam(value = "superPersonPowers", required = false) int[] powerIds,
                             @RequestParam(value = "superPersonOrgs", required = false) int[] organizationIds
                             ){
        violations.clear();
        SuperPerson villain = new SuperPerson(name, description, true);
        List<Power> powers = new ArrayList<>();
        if(powerIds != null){
            for(int powerId: powerIds){
                powers.add(service.getPowerByID(powerId));
            }
            villain.setPowers(powers);
        }
        if(organizationIds != null){
            List<Organisation> organizations = new ArrayList<>();
            for(int orgId: organizationIds){
                organizations.add(service.getOrganisationByID(orgId));
            }
            villain.setOrganisations(organizations);
        }

        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(villain);

        if(violations.isEmpty()) {
            service.addSuperPerson(villain);
        }
        return "redirect:/villains";
    }
    
    @GetMapping("deleteVillain")
    public String deleteVillain(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteSuperPersonByID(id);
        return "redirect:/villains";
    }
    
    @GetMapping("editVillain")
    public String editVillain(HttpServletRequest request, Model model) {
        violations.clear();
        model.addAttribute("errors", violations);
        int id = Integer.parseInt(request.getParameter("id"));
        SuperPerson villain = service.getSuperPersonByID(id);

        model.addAttribute("villain", villain);
        return "editVillain";
    }

    @PostMapping("editVillain")
    public String performEditVillain(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        SuperPerson villain = service.getSuperPersonByID(id);
        villain = service.editSuperPerson(villain, request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(villain);
        
        if(violations.isEmpty()){
            service.updateSuperPerson(villain);
        }
        return "redirect:/villains";
    }    
    
}
