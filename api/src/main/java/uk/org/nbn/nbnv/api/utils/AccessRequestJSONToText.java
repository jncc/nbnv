/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;

/**
 *
 * @author Administrator
 */
@Component
public class AccessRequestJSONToText {
    @Autowired TaxonMapper taxonMapper;
    @Autowired DesignationMapper designationMapper;
    @Autowired TaxonOutputGroupMapper outputGroupMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;

    public String convert(AccessRequestJSON ar) {
        String text = null;

        if ("ns".equals(ar.getSensitive())) {
            text = "All non-sensitive records";
        } else if ("sans".equals(ar.getSensitive())) {
            text = "All sensitive and non-sensitive records";
        }

        if (!ar.getYear().isAll()) {
            text += " between " + Integer.toString(ar.getYear().getStartYear()) + " and " + Integer.toString(ar.getYear().getEndYear());
        }

        if (!ar.getTaxon().isAll() && !ar.getTaxon().getTvk().isEmpty()) {
            Taxon t = taxonMapper.getTaxon(ar.getTaxon().getTvk());
            text += " for " + t.getName() + " " + t.getAuthority();
        } else if (!ar.getTaxon().isAll() && !ar.getTaxon().getDesignation().isEmpty()) {
            Designation d = designationMapper.selectByID(ar.getTaxon().getDesignation());
            text += " for " + d.getName() + " species";
        } else if (!ar.getTaxon().isAll() && !ar.getTaxon().getOutput().isEmpty()) {
            TaxonOutputGroup o = outputGroupMapper.getById(ar.getTaxon().getOutput());
            text += " for " + o.getName() + " species";
        }

        if (!ar.getSpatial().isAll()) {
            SiteBoundary sb = siteBoundaryMapper.getById(ar.getSpatial().getFeature());
            text += " " + ar.getSpatial().getMatch() + " the boundary of " + sb.getName();
        }

        if (!ar.getTime().isAll()) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            text += " until " + df.format(ar.getTime().getDate());
        }

        return text;
    }
}
