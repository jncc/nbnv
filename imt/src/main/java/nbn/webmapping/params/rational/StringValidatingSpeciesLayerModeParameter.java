package nbn.webmapping.params.rational;

public abstract class StringValidatingSpeciesLayerModeParameter extends SpeciesLayerModeParameter<String>{
    public StringValidatingSpeciesLayerModeParameter(String parameterName) {
        super(parameterName);
    }

    public StringValidatingSpeciesLayerModeParameter(String parameterName, boolean isOptional) {
        super(parameterName, isOptional);
    }

    public StringValidatingSpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation) {
        super(queryStringParameter,jsonRepresentation);
    }

    public StringValidatingSpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation, boolean isOptional) {
        super(queryStringParameter, jsonRepresentation, isOptional);
    }

    @Override
    public String getParameterInCorrectForm(String toPutInCorrectForm) {
        if(!isValidFormOfParameter(toPutInCorrectForm))
            throw new IllegalArgumentException("The paramter " + getJSONRepresentationParameterName() + " is not in the correct form");
        return toPutInCorrectForm;
    }

    public abstract boolean isValidFormOfParameter(String toPutInCorrectForm);
}
