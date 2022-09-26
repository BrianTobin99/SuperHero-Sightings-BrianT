
package com.briant.dao;

import java.util.List;

import com.briant.dto.Organization;


public interface OrganizationDao {
    Organization getOrganizationById(int organizationId);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void deleteOrganizationById(int organizationId);
    void editOrganization(Organization organization);

    public Organization getOrganizationByName(String orgName);
}
