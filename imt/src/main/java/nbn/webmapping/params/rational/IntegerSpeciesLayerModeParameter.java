package nbn.webmapping.params.rational;

public class IntegerSpeciesLayerModeParameter extends SpeciesLayerModeParameter<Integer>{
    public IntegerSpeciesLayerModeParameter(String parameterName) {
        super(parameterName);
    }

    public IntegerSpeciesLayerModeParameter(String parameterName, boolean isOptional) {
        super(parameterName, isOptional);
    }

    public IntegerSpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation) {
        super(queryStringParameter,jsonRepresentation);
    }

    public IntegerSpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation, boolean isOptional) {
        super(queryStringParameter, jsonRepresentation, isOptional);
    }

    @Override
    public Integer getParameterInCorrectForm(String toPutInCorrectForm) {
        try {
            return Integer.parseInt(toPutInCorrectForm);
        }
        catch(NumberFormatException nfe) {
            throw new IllegalArgumentException("The passed in string " + toPutInCorrectForm + " is not a valid integer",nfe);
        }
    }
}
