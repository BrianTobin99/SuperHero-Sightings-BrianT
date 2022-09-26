/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.briant.dao;

import com.briant.entities.Organization;
import java.util.List;

/**
 *
 * @author Joe
 */
public interface OrganizationDao {
    Organization getOrganizationById(int organizationId);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void deleteOrganizationById(int organizationId); //boolean for verification?
    void editOrganization(Organization organization);

    public Organization getOrganizationByName(String orgName);
}
