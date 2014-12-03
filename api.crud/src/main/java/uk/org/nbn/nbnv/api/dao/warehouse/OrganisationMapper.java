/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
public interface OrganisationMapper {
    final String SELECT_ALL = "SELECT " +
            "CASE WHEN logo IS NULL THEN 0 ELSE 1 END as hasLogo," +
            "CASE WHEN logoSmall IS NULL THEN 0 ELSE 1 END as hasSmallLogo,"  +
            "id, name, abbreviation, summary, address, postcode, contactName,"  +
            "contactEmail, website, allowPublicRegistration "  +
        "FROM OrganisationData";
    
    @Select(SELECT_ALL)
    List<Organisation> selectAll();
    
    @Select(SELECT_ALL + " WHERE id = #{id}")
    Organisation selectByID(int id);
    
    @Select("SELECT org.* FROM OrganisationData org WHERE name LIKE #{term} OR abbreviation LIKE #{term}")
    List<Organisation> searchForOrganisation(@Param("term") String term);        
    
    @Select(SELECT_ALL + " WHERE ((SELECT COUNT(*) FROM DatasetData dd WHERE organisationID = id) > 0)")
    List<Organisation> selectAllContributing();
    
    @Select("SELECT logo FROM OrganisationData WHERE id = #{id}")
    Object selectLogoByOrganisationID(int id);
    
    @Select("SELECT logoSmall FROM OrganisationData WHERE id = #{id}")
    Object selectLogoSmallByOrganisationID(int id);

    @Select("SELECT org.* FROM OrganisationData org INNER JOIN OrganisationMembershipData omd ON omd.organisationID = org.id WHERE omd.userID = #{id}")
    List<Organisation> selectByUser(int id);

    @Select("SELECT org.* FROM OrganisationData org INNER JOIN OrganisationMembershipData omd ON omd.organisationID = org.id WHERE omd.userID = #{id} AND (omd.role = 'administrator' OR omd.role = 'lead')")
    List<Organisation> selectByAdminUser(int id);
    
    @Select("SELECT DISTINCT orgd.* FROM TaxonDatasetTaxonData tdtd INNER JOIN DatasetData dd ON dd.[key] = tdtd.datasetKey INNER JOIN OrganisationData orgd ON orgd.id = dd.organisationID WHERE tdtd.pTaxonVersionKey = #{id}")
    List<Organisation> selectOrganisationsContributingToTaxon(String id);
}
