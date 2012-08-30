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
public class DesignationSpeciesDensityRequest extends SpeciesDataRequest {
    private String _designation;

    public DesignationSpeciesDensityRequest(String desig) {
        super();

        _designation = desig;
    }

    public String getDesignationKey() {
        return _designation;
    }

    @Override
    protected void appendExtraRequestParameters(RequestParameters out) {
	out.addParameter("DesignationKey", getDesignationKey());
    }

}
