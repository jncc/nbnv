/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon.taxonomy;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import nbn.common.taxonreportingcategory.TaxonReportingCategoryDAO;
import nbn.common.database.DataAccessObject;

/**
 *
 * @author Administrator
 */
public class TaxonomyDAO  extends DataAccessObject {
    public TaxonomyDAO() throws SQLException {
        super();
    }

    /**
     * This is the default operation for getting a taxonomy, it will not request for designations to be added
     * @param nbnKey The taxon key
     * @return A Taxonomy without designations appended
     * @throws SQLException
     */
    public Taxonomy getTaxonomy(String nbnKey) throws SQLException {
	return getTaxonomy(nbnKey, false, false);
    }

    public Taxonomy getTaxonomy(String nbnKey, boolean includeDesignations, boolean includeFormerDesignations) throws SQLException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesTaxonomy2(?,?)}");
        cs.setString(1, nbnKey);
        cs.setInt(2, 0); // searching by nbn key not internal key

        ResultSet rs = cs.executeQuery();   // Result set should be order by species
        Taxonomy root = null;

        LinkedList<Taxonomy> tempSyn = new LinkedList<Taxonomy>();
        LinkedList<Taxonomy> tempAgg = new LinkedList<Taxonomy>();
        LinkedList<Taxonomy> tempCh = new LinkedList<Taxonomy>();

        TaxonReportingCategoryDAO trcDAO = null;
	TaxonDesignationDAO taxonDesignationDAO = null;
        try {
            trcDAO = new TaxonReportingCategoryDAO();
	    taxonDesignationDAO = new TaxonDesignationDAO();

            while (rs.next()) {
                Taxonomy result = new Taxonomy(rs.getString("taxonName"), rs.getString("nbnTaxonVersionKey"));
                result.setTaxonVersionKey(rs.getInt("childTVK"));

                if (!"&nbsp;".equals(rs.getString("taxonAuthority")))
                    result.setAuthority(rs.getString("taxonAuthority"));
                result.setCategory(trcDAO.getTaxonReportingCategoryByName(rs.getString("taxonGroupName")));
                result.setAggregate(rs.getBoolean("isAggregate"));
                result.setNameWellFormed("W".equals(rs.getString("taxonVersionForm")));
                result.setNameScientific("la".equals(rs.getString("lang")));
                result.setNamePrefered(false);
		
                String source = rs.getString("source");

                if ("PREFERRED NAME".equals(source)) {
		    //I only want to add the taxon designations to the root. That is why this method is performed here
		    if(includeDesignations)
			result.addAllTaxonDesignations(taxonDesignationDAO.getAllTaxonDesignations(rs.getInt("internalTaxonVersionKey"),includeFormerDesignations));
                    root = result;
                    root.setNamePrefered(true);
                } else if ("NHM NAME SERVER".equals(source) || "SYNONYMY".equals(source)) {
                    if (result.getName() != null || result.getName().length() > 0) {
                        tempSyn.add(result);
                    }
                } else if ("BRC - VP".equals(source) || "BRC-BRYO".equals(source)) {
                    tempAgg.add(result);
                } else if ("TAXON HIERARCHY".equals(source)) {
                    tempCh.add(result);
                }
            }
        }
	finally {
	    rs.close();
	    cs.close();
            if (trcDAO != null)
                trcDAO.dispose();
	    if(taxonDesignationDAO!=null)
		taxonDesignationDAO.dispose();
        }


        if (root == null)
            throw new IllegalArgumentException("Taxon Version Key returned no prefered taxonomy {key = " + nbnKey + "}");

        if (!tempSyn.isEmpty())
            root.getModifiableSynonymList().addAll(tempSyn);

        if (!tempAgg.isEmpty())
            root.getModifiableAggregateList().addAll(tempAgg);

        if (!tempCh.isEmpty())
            root.getModifiableChildrenList().addAll(tempCh);

        return root;
    }

    public List<Taxonomy> getTaxonomyListByNBNKey(String nbnKey, boolean includeDesignations, boolean includeFormerDesignations) throws SQLException {
        List<Taxonomy> results = new LinkedList<Taxonomy>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesTaxonomyPreferedNameTVK(?)}");
        cs.setString(1, nbnKey);

        ResultSet rs = cs.executeQuery();
	try {
	    while (rs.next())
		results.add(getTaxonomy(rs.getString("prefnameTaxonVersionKey"),includeDesignations,includeFormerDesignations));
	    return results;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<Taxonomy> getTaxonomyListBySearchTerm(String term, boolean includeDesignations, boolean includeFormerDesignations) throws SQLException {
        List<Taxonomy> results = new LinkedList<Taxonomy>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_searchSpeciesTaxonomyXML(?)}");
        cs.setString(1, term);

        ResultSet rs = cs.executeQuery();
	try {
	    while (rs.next())
		results.add(getTaxonomy(rs.getString("prefnameTaxonVersionKey"),includeDesignations,includeFormerDesignations));
		return results;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }
}
