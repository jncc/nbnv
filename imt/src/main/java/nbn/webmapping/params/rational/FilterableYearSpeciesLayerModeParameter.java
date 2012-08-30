/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.params.rational;

import java.util.Calendar;

/**
 *
 * @author Administrator
 */
public class FilterableYearSpeciesLayerModeParameter extends IntegerSpeciesLayerModeParameter {
    private static final int EARLIEST_YEAR = 1799;
    public FilterableYearSpeciesLayerModeParameter(String parameterName) {
        super(parameterName);
    }

    public FilterableYearSpeciesLayerModeParameter(String parameterName, boolean isOptional) {
        super(parameterName, isOptional);
    }

    public FilterableYearSpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation) {
        super(queryStringParameter,jsonRepresentation);
    }

    public FilterableYearSpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation, boolean isOptional) {
        super(queryStringParameter, jsonRepresentation, isOptional);
    }

    @Override
    public Integer getParameterInCorrectForm(String toPutInCorrectForm) {
        Integer yearToValidate = super.getParameterInCorrectForm(toPutInCorrectForm);
        if(yearToValidate >= EARLIEST_YEAR && yearToValidate <= Calendar.getInstance().get(Calendar.YEAR))
            return yearToValidate;
        else
            throw new IllegalArgumentException("The year needs to be specified as a value between 1799 and the current year");
    }
}
