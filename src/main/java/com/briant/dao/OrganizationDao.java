
package com.briant.dao;

import com.briant.entities.Organization;
import java.util.List;


public interface OrganizationDao {
    Organization getOrganizationById(int organizationId);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void deleteOrganizationById(int organizationId);
    void editOrganization(Organization organization);

    public Organization getOrganizationByName(String orgName);
}
