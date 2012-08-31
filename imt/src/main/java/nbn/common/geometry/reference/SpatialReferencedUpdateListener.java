package nbn.common.geometry.reference;

import nbn.common.geometry.util.conversion.GeodeticSystemConvertor;
/**
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :-
*/
public interface SpatialReferencedUpdateListener {
    public void changedSpatialReferenceSystem(GeodeticSystemConvertor toUseForConversion);
}