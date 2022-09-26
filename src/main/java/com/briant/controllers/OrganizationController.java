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

import com.briant.dao.OrganizationDao;
import com.briant.dto.Organization;
import com.briant.service.ServiceLayer;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class OrganizationController {
    @Autowired
    ServiceLayer service;
    
    @Autowired 
    OrganizationDao organizationDao;
    
    Set<ConstraintViolation<Organization>> violations = new HashSet<>();
    
    @GetMapping("organizations")
    public String displayOrganizations(Model model){
        model.addAttribute("errors", violations);
        List<Organization> organizations = service.getOrganizations();
        model.addAttribute("organizations", organizations);
        return "organizations";
    }
    
    @PostMapping("addOrganization")
    public String addOrganization(HttpServletRequest request){
        Organization organization = service.createOrganization(request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organization);

        if(violations.isEmpty()) {
            service.addOrganization(organization);
        }
        return "redirect:/organizations";
    }
    
    @GetMapping("deleteOrganization")
    public String deleteOrganization(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteOrganizationById(id);
        return "redirect:/organizations";
    }
    
    @GetMapping("editOrganization")
    public String editOrganization(HttpServletRequest request, Model model) {
        model.addAttribute("errors", violations);
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = service.getOrganizationById(id);

        model.addAttribute("organization", organization);
        return "editOrganization";
    }

    @PostMapping("editOrganization")
    public String performEditOrganization(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = service.getOrganizationById(id);
        organization = service.editOrganization(organization, request);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organization);
        
        if(violations.isEmpty()){
            service.updateOrganization(organization);
        }
        return "redirect:/organizations";
    }
    
}
