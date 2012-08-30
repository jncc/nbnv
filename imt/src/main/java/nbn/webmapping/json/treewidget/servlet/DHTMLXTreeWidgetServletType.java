package nbn.webmapping.json.treewidget.servlet;

import nbn.webmapping.json.treewidget.impl.siteBoundary.SiteBoundaryDHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.habitat.HabitatDHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.designation.DesignationDHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.species.SpeciesDHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.dataset.DatasetDHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.designation.DatasetsForSingleDesignationDHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.species.DatasetsForSingleSpeciesDHTMLXTreeWidgetGenerator;

/**
 *
 * @author Administrator
 */
public enum DHTMLXTreeWidgetServletType {
    SITE_BOUNDARY("s", new SiteBoundaryDHTMLXTreeWidgetGenerator()),
    HABITAT("h",new HabitatDHTMLXTreeWidgetGenerator()),
    DESIGNATION("d", new DesignationDHTMLXTreeWidgetGenerator()),
    DESIGNATION_DATASETS("dd", new DatasetsForSingleDesignationDHTMLXTreeWidgetGenerator()),
    SINGLE_SPECIES("sp", new SpeciesDHTMLXTreeWidgetGenerator()),
    SINGLE_SPECIES_DATASETS("spd", new DatasetsForSingleSpeciesDHTMLXTreeWidgetGenerator()),
    SINGLE_DATASET("ds",new DatasetDHTMLXTreeWidgetGenerator());

    private String stringRepresentation;
    private DHTMLXTreeWidgetGenerator generator;

    private DHTMLXTreeWidgetServletType(String stringRepresentation,DHTMLXTreeWidgetGenerator generator) {
	this.stringRepresentation = stringRepresentation;
	this.generator = generator;
    }

    public DHTMLXTreeWidgetGenerator getServletGenerator() {
	return generator;
    }

    public static DHTMLXTreeWidgetServletType getDHTMLXTreeWidgetServletTypeRepresentedBy(String stringRepresentation) {
	for(DHTMLXTreeWidgetServletType currWidget : DHTMLXTreeWidgetServletType.values()) {
	    if(stringRepresentation.equals(currWidget.stringRepresentation))
		return currWidget;
	}
	throw new IllegalArgumentException("There is no DHTMLXTreeWidget which is represented by " + stringRepresentation);
    }
}
