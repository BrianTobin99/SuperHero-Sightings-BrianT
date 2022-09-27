
package com.briant.dao;

import java.util.List;

import com.briant.dto.Organisation;


public interface OrganisationDao {
    Organisation getOrganisationByID(int organisationId);
    List<Organisation> getAllOrganisations();
    Organisation addOrganisation(Organisation organisation);
    void deleteOrganisationByID(int organisationId);
    void editOrganisation(Organisation organisation);

    public Organisation getOrganisationByName(String orgName);
}
