/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.params.rational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
public enum SpeciesLayerMode {
    SINGLE_DATASET("SINGLE_DATASET", true, new SpeciesLayerModeParameter<?>[]{new StringValidatingSpeciesLayerModeParameter("dataset"){
        public boolean isValidFormOfParameter(String toCheck) {
            return true;
        }
    }}),
    DESIGNATION("DESIGNATION", true, new SpeciesLayerModeParameter<?>[]{new StringValidatingSpeciesLayerModeParameter("designation"){
            public boolean isValidFormOfParameter(String toCheck) {
                return true;
            }
        },new StringValidatingSpeciesLayerModeParameter("datasets", true){
            public boolean isValidFormOfParameter(String toCheck) {
                return true;
            }
    }}),
    SPECIES("SPECIES", true, new SpeciesLayerModeParameter<?>[]{
        new StringValidatingSpeciesLayerModeParameter("species"){
            public boolean isValidFormOfParameter(String toCheck) {
                return true;
            }
        },new StringValidatingSpeciesLayerModeParameter("datasets", true){
            public boolean isValidFormOfParameter(String toCheck) {
                return true;
            }
        }
    }),
    ALL_NBN_GATEWAY_DATA("ALL_NBN_GATEWAY_DATA");


    private String mode;
    private List<SpeciesLayerModeParameter<?>> constructionParameters;

    private SpeciesLayerMode(String mode) {
        this(mode, false, new SpeciesLayerModeParameter<?>[0]);
    }
    
    private SpeciesLayerMode(String mode, SpeciesLayerModeParameter<?>[] constructionParameters) {
        this(mode, true, constructionParameters);
    }
    
    private SpeciesLayerMode(String mode, boolean allowYearFiltering, SpeciesLayerModeParameter<?>[] constructionParameters) {
        this.mode = mode;
        this.constructionParameters = new ArrayList<SpeciesLayerModeParameter<?>>(Arrays.asList(constructionParameters));
        if(allowYearFiltering) {
            this.constructionParameters.add(new FilterableYearSpeciesLayerModeParameter("startyear",true));
            this.constructionParameters.add(new FilterableYearSpeciesLayerModeParameter("endyear",true));
        }
    }

    public String getModeAsString() {
        return mode;
    }

    public List<SpeciesLayerModeParameter<?>> getRequiredParameterNames() {
        return constructionParameters;
    }

    public static SpeciesLayerMode getSpeciesLayerMode(String representation) {
        for(SpeciesLayerMode currMode : SpeciesLayerMode.values())
            if(representation.equals(currMode.getModeAsString()))
                return currMode;
        throw new IllegalArgumentException("The specified mode is not known");
    }
}
