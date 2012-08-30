
package nbn.common.dataset;

import nbn.common.organisation.Organisation;


/**
*
* @author	    :- Christopher Johnson
* @date		    :- 08-Sep-2010
* @description	    :-
*/
public class SiteBoundaryDataset extends GISLayeredDataset {
    private String description;
    private String dateLoaded;
    private String layerName;

    public String getDateLoaded() {
	return dateLoaded;
    }

    public void setDateLoaded(String dateLoaded) {
	this.dateLoaded = dateLoaded;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    SiteBoundaryDataset(String key, String title, Organisation provider) {
	super(key,title,provider);
    }

    public String getLayerName() {
	return layerName;
    }

    public void setLayerName(String layerName) {
	this.layerName = layerName;
    }
}
