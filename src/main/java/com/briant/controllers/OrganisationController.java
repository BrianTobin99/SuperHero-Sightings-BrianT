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

import com.briant.dao.OrganisationDao;
import com.briant.dto.Organisation;
import com.briant.service.ServiceLayer;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class OrganisationController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    OrganisationDao organizationDao;
    
    Set<ConstraintViolation<Organisation>> violations = new HashSet<>();
    
    @GetMapping("organisations")
    public String displayOrganisations(Model model){
        model.addAttribute("errors", violations);
        List<Organisation> organisations = service.getOrganisations();
        model.addAttribute("organisations", organisations);
        return "organisations";
    }
    
    @PostMapping("addOrganisation")
    public String addOrganization(HttpServletRequest request){
        Organisation organisation = service.createOrganisation(request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organisation);

        if(violations.isEmpty()) {
            service.addOrganisation(organisation);
        }
        return "redirect:/organisations";
    }
    
    @GetMapping("deleteOrganisation")
    public String deleteOrganisation(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteOrganisationById(id);
        return "redirect:/organisations";
    }
    
    @GetMapping("editOrganisation")
    public String editOrganisation(HttpServletRequest request, Model model) {
        model.addAttribute("errors", violations);
        int id = Integer.parseInt(request.getParameter("id"));
        Organisation organisation = service.getOrganisationById(id);

        model.addAttribute("organisation", organisation);
        return "editOrganisation";
    }

    @PostMapping("editOrganisation")
    public String performEditOrganisation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organisation organisation = service.getOrganisationById(id);
        organisation = service.editOrganisation(organisation, request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organisation);
        
        if(violations.isEmpty()){
            service.updateOrganisation(organisation);
        }
        return "redirect:/organisations";
    }
    
}
