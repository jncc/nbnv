/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

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
import uk.org.nbn.nbnv.api.model.meta.DownloadFilterJSON;

/**
 *
 * @author Administrator
 */
@Component
public class DownloadJSONToText {
    @Autowired TaxonMapper taxonMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonOutputGroupMapper outputGroupMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    @Autowired OrganisationSuppliedListMapper organisationSuppliedListMapper;

    public String convert(DownloadFilterJSON filter) {
        String text = null;

        if ("ns".equals(filter.getSensitive())) {
            text = "All non-sensitive records";
        } else if ("sans".equals(filter.getSensitive())) {
            text = "All sensitive and non-sensitive records";
        }

        if (!filter.getYear().isAll()) {
            text += " between " + Integer.toString(filter.getYear().getStartYear()) + " and " + Integer.toString(filter.getYear().getEndYear());
        }

        if (!filter.getTaxon().isAll() && !filter.getTaxon().getTvk().isEmpty()) {
            Taxon t = taxonMapper.getTaxon(filter.getTaxon().getTvk());
            text += " for " + t.getName() + " " + t.getAuthority();
        } else if (!filter.getTaxon().isAll() && !filter.getTaxon().getDesignation().isEmpty()) {
            Designation d = designationMapper.selectByID(filter.getTaxon().getDesignation());
            text += " for " + d.getName() + " species";
        } else if (!filter.getTaxon().isAll() && !filter.getTaxon().getOutput().isEmpty()) {
            TaxonOutputGroup o = outputGroupMapper.getById(filter.getTaxon().getOutput());
            text += " for " + o.getName() + " species";
        } else if (!filter.getTaxon().isAll() && filter.getTaxon().getOrgSuppliedList() > 0) {
            OrganisationSuppliedList l = organisationSuppliedListMapper.selectByID(filter.getTaxon().getOrgSuppliedList());
            text += " for species in the list \"" + l.getName() + "\"  provided by " + l.getOrganisationName();
        }
        
        if (!filter.getSpatial().isAll()) {
            if (StringUtils.hasText(filter.getSpatial().getGridRef())) {
                text += " " + filter.getSpatial().getMatch() + " the grid square " + filter.getSpatial().getGridRef();
            } else {
                SiteBoundary sb = siteBoundaryMapper.getById(filter.getSpatial().getFeature());
                text += " " + filter.getSpatial().getMatch() + " the boundary of " + sb.getName();
            }
        }

        return text;
    }
}
