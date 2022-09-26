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
import com.briant.dto.Organization;
import com.briant.dto.Power;
import com.briant.dto.SuperPerson;
import com.briant.service.ServiceLayer;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class HeroController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    SightingDao sightingDao;
    
    Set<ConstraintViolation<SuperPerson>> violations = new HashSet<>();
    
    
    @GetMapping("heroes")
    public String displayHeroes(Model model){
        model.addAttribute("errors", violations);
        List<SuperPerson> heroes = service.getHeroes();
        model.addAttribute("heroes", heroes);
        model.addAttribute("powers", service.getPowers());
        model.addAttribute("organizations", service.getOrganizations());
        return "heroes";
    }
    
    @PostMapping("addHero")
    public String addVillain(@RequestParam(value = "superPersonName", required = true) String name,
                             @RequestParam(value = "superPersonDesc", required = false) String description,
                             @RequestParam(value = "superPersonPowers", required = false) int[] powerIds,
                             @RequestParam(value = "superPersonOrgs", required = false) int[] organizationIds
                             ){
        violations.clear();
        SuperPerson hero = new SuperPerson(name, description, false);
        List<Power> powers = new ArrayList<>();
        if(powerIds != null){
            for(int powerId: powerIds){
                powers.add(service.getPowerById(powerId));
            }
            hero.setPowers(powers);
        }
        if(organizationIds != null){
            List<Organization> organizations = new ArrayList<>();
            for(int orgId: organizationIds){
                organizations.add(service.getOrganizationById(orgId));
            }
            hero.setOrganizations(organizations);
        }

        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(hero);

        if(violations.isEmpty()) {
            service.addSuperPerson(hero);
        }
        return "redirect:/heroes";
    }
    
    @GetMapping("deleteHero")
    public String deleteHero(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteSuperPersonById(id);
        return "redirect:/heroes";
    }
    
    @GetMapping("editHero")
    public String editHero(HttpServletRequest request, Model model) {
        violations.clear();
        model.addAttribute("errors", violations);
        int id = Integer.parseInt(request.getParameter("id"));
        SuperPerson hero = service.getSuperPersonById(id);

        model.addAttribute("hero", hero);
        return "editHero";
    }

    @PostMapping("editHero")
    public String performEditHero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        SuperPerson hero = service.getSuperPersonById(id);
        hero = service.editSuperPerson(hero, request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(hero);
        
        if(violations.isEmpty()){
            service.updateSuperPerson(hero);
        }
        return "redirect:/heroes";
    }    
    
}
