package uk.org.nbn.nbnv.api.nxf;

import java.util.ArrayList;
import java.util.List;

public enum NXFHeading {
    RECORDKEY, TAXONVERSIONKEY, SITENAME, NORTH, EAST, NORTHINGS, 
    EASTINGS, LAT, LONG, LATITUDE, LONGITUDE, SRS, DATE, RECORDER, DETERMINER, 
    ZEROABUNDANCE, SURVEYKEY, SAMPLEKEY, DYNAMICPROPERTIES, PROJECTION, SENSITIVE, 
    GRIDREFERENCE, PRECISION, DATETYPE, STARTDATE, ENDDATE, FEATUREKEY;
    
    /**
     * Generates a list of the values of this enumeration as a list of strings
     * @return a list of strings
     */
    public static List<String> stringValues() {
        List<String> toReturn = new ArrayList<>();
        for(NXFHeading heading: values()){
            toReturn.add(heading.name());
        }
        return toReturn;
    }
}
