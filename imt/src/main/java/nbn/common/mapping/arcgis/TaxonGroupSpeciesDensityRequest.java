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
public final class TaxonGroupSpeciesDensityRequest extends SpeciesDataRequest {
    private String _taxonGroup;

    public TaxonGroupSpeciesDensityRequest(String taxonGroup) {
        super();

        _taxonGroup = taxonGroup;
    }

    public String getTaxonGroupKey() {
        return _taxonGroup;
    }

    @Override
    protected void appendExtraRequestParameters(RequestParameters out) {
	out.addParameter("TaxonGroupKey", getTaxonGroupKey());
    }
}
