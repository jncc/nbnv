/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.arcgis;

import nbn.common.logging.RequestParameters;

/**
 *
 * @author Administrator
 */
public final class SingleSpeciesRequest extends SpeciesDataRequest {
    private String _taxon;

    public SingleSpeciesRequest(String taxon) {
        super();

        _taxon = taxon;
    }

    public String getTaxonKey() {
        return _taxon;
    }

    @Override
    protected void appendExtraRequestParameters(RequestParameters out) {
	out.addParameter("TaxonVersionKey", getTaxonKey());
    }

}
