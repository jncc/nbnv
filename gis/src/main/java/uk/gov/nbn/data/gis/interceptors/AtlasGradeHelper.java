package uk.gov.nbn.data.gis.interceptors;

import uk.gov.nbn.data.gis.processor.AtlasGrade;

/**
 * The following is a helper for obtaining the atlas grade layer which is enabled
 * @author Christopher Johnson
 */
public class AtlasGradeHelper {
    
    public static AtlasGrade.Layer getResolution(String resolution, AtlasGrade atlasGradeProperties) {
        //Work out which resolution to use. Either one requested or this AtlasGrade maps default
        AtlasGrade.Resolution resolutionToUse = (resolution != null) ? 
                                            AtlasGrade.Resolution.getResolutionFromParamValue(resolution) : 
                                            atlasGradeProperties.defaultResolution();
        //Find the layer which corresponds to this resolution
        for(AtlasGrade.Layer currLayer : atlasGradeProperties.layers()) {
            if(resolutionToUse.equals(currLayer.resolution())) {
                return currLayer;
            }
        }
        throw new IllegalArgumentException("This map service does not support the resolution " + resolutionToUse);
    }
}
