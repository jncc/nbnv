
package nbn.common.feature;

import java.sql.SQLException;
import java.util.List;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.bridging.ListBridge;
import nbn.common.siteboundary.SiteBoundaryDAO;

public abstract class Feature {
    protected static final String TYPE_SEPERATOR = ":";
    
    public abstract String getName();
    public abstract String getUniqueIDForFeatureType();
    public abstract FeatureType getFeatureType();

    public static Feature getFeature(String featureID) {
        return new FeatureStringToFeatureBridge().convert(featureID);
    }

    public static List<Feature> getFeatures(List<String> featureIDs) {
        return new ListBridge<String,Feature>(new FeatureStringToFeatureBridge()).convert(featureIDs);
    }

    public final String getUniqueFeatureID() {
        StringBuilder toReturn = new StringBuilder(getFeatureType().getType());
        toReturn.append(TYPE_SEPERATOR);
        toReturn.append(getUniqueIDForFeatureType());
        return toReturn.toString();
    }

    private static class FeatureStringToFeatureBridge implements Bridge<String, Feature> {
        public Feature convert(String featureID) throws BridgingException {
            try {
                SiteBoundaryDAO dao = new SiteBoundaryDAO();
                try {
                    if(!featureID.contains(TYPE_SEPERATOR))
                        throw new IllegalArgumentException("The specified feature ID does not contain the type/id seperator");
                    FeatureType featureType = FeatureType.getFeatureType(featureID.split(TYPE_SEPERATOR)[0]);
                    String id = featureID.substring(featureID.indexOf(TYPE_SEPERATOR) + TYPE_SEPERATOR.length());
                    switch(featureType) {
                        case SITEBOUNDARY:
                            return dao.getSiteBoundary(Integer.parseInt(id));
                        case GRIDSQUARE:
                            return new GridSquare(id);
                        default:
                            throw new IllegalArgumentException("No construction method has been defined for featuretype " + featureType);
                    }
                }
                finally {
                    dao.dispose();
                }
            }
            catch(SQLException sqlEx) {
                throw new BridgingException("An SQL Exception has occurred", sqlEx);
            }
        }
    }
}
