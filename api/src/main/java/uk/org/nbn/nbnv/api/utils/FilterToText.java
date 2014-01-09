/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationSuppliedListMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.OrganisationSuppliedList;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

/**
 *
 * @author Matt Debont
 */
@Component
public class FilterToText {
    
    @Autowired TaxonMapper taxonMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonOutputGroupMapper outputGroupMapper;
    @Autowired OrganisationSuppliedListMapper organisationSuppliedListMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    
    public String convert(
            int startYear,
            int endYear,
            List<String> datasetKeys,
            List<String> taxa,
            String spatialRelationship,
            String featureID,
            Boolean sensitive,
            String designation,
            String taxonOutputGroup,
            int orgSuppliedList,
            String gridRef,
            String polygon,
            boolean absence) {
        String text;

        if (!sensitive) {
            text = "All non-sensitive ";
        } else {
            text = "All sensitive and non-sensitive ";
        }
        
        if (absence) {
            text += "absence records";
        } else {
            text += "presence records";
        }

        if (startYear > 0 && endYear > 0) {
            text += " between " + startYear + " and " + endYear;
        }

        if (taxa != null && !taxa.isEmpty()) {
            List<String> taxaList = new ArrayList<String>();
            for (String tvk : taxa) {
                Taxon t = taxonMapper.getTaxon(tvk);
                taxaList.add(t.getName() + " " + t.getAuthority() + "(" + tvk + ")");
            }
            text += " for " + StringUtils.collectionToDelimitedString(taxaList, " and ");
        } else if (StringUtils.hasText(designation)) {
            Designation d = designationMapper.selectByID(designation);
            text += " for " + d.getName() + " species";
        } else if (StringUtils.hasText(taxonOutputGroup)) {
            TaxonOutputGroup o = outputGroupMapper.getById(taxonOutputGroup);
            text += " for " + o.getName() + " species";
        }  else if (orgSuppliedList > 0) {
            OrganisationSuppliedList l = organisationSuppliedListMapper.selectByID(orgSuppliedList);
            text += " for species in the list \"" + l.getName() + "\"  provided by " + l.getOrganisationName();
        }

        if (StringUtils.hasText(gridRef)) {
            text += " " + spatialRelationship + " the grid square " + gridRef;
        } else if (StringUtils.hasText(featureID)) {
            SiteBoundary sb = siteBoundaryMapper.getById(featureID);
            text += " " + spatialRelationship + " the boundary of " + sb.getName();
        }
        
        if (datasetKeys != null && !datasetKeys.isEmpty()) {
            text += " for the following datasets; " + StringUtils.collectionToCommaDelimitedString(datasetKeys);
        }

        return text;
    }
}
