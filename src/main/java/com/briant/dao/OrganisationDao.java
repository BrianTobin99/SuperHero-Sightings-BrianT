
package com.briant.dao;

import java.util.List;


public interface OrganisationDao {
    Organisation getOrganisationById(int organisationId);
    List<Organisation> getAllOrganisations();
    Organisation addOrganisation(Organisation organisation);
    void deleteOrganisationById(int organisationId);
    void editOrganisation(Organisation organisation);

    public Organisation getOrganisationByName(String orgName);
}
