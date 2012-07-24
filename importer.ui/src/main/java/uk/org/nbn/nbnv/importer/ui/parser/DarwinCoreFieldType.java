/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.parser;

/**
 *
 * @author Administrator
 */
public enum DarwinCoreFieldType {
    DWCA_OCCURRENCE ("http://rs.tdwg.org/dwc/terms/Occurrence"),
    NBNEXCHANGE ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence"),
    INTERNAL ("Internal mapping");
    
    private String rowType;

    private DarwinCoreFieldType(String rowType) {
        this.rowType = rowType;
    }
    
    public String getRowType() {
        return this.rowType;
    }
    
    @Override
    public String toString(){
        return this.rowType;
    }
}
