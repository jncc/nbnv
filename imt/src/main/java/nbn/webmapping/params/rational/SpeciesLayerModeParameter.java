package nbn.webmapping.params.rational;

public abstract class SpeciesLayerModeParameter<T> {
    private String queryStringParameter,jsonRepresentation;
    private boolean isOptional;

    public SpeciesLayerModeParameter(String parameterName) {
        this(parameterName, parameterName, false);
    }

    public SpeciesLayerModeParameter(String parameterName, boolean isOptional) {
        this(parameterName, parameterName, isOptional);
    }

    public SpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation) {
        this(queryStringParameter,jsonRepresentation, false);
    }

    public SpeciesLayerModeParameter(String queryStringParameter, String jsonRepresentation, boolean isOptional) {
        this.queryStringParameter = queryStringParameter;
        this.jsonRepresentation = jsonRepresentation;
        this.isOptional = isOptional;
    }

    public String getQueryStringParameterName() {
        return queryStringParameter;
    }

    public String getJSONRepresentationParameterName() {
        return jsonRepresentation;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public abstract T getParameterInCorrectForm(String toPutInCorrectForm);
}
