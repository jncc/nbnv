/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.parser;

/**
 *
 * @author Administrator
 */
public class ColumnMapping {
    private int columnNumber;
    private String columnLabel;
    private DarwinCoreField field;

    public ColumnMapping() { };
    
    public ColumnMapping(int columnNumber, String columnLabel, DarwinCoreField field) {
        this.columnNumber = columnNumber;
        this.columnLabel = columnLabel;
        this.field = field;
    }
    
    /**
     * @return the columnNumber
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * @param columnNumber the columnNumber to set
     */
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    /**
     * @return the columnLabel
     */
    public String getColumnLabel() {
        return columnLabel;
    }

    /**
     * @param columnLabel the columnLabel to set
     */
    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    /**
     * @return the field
     */
    public DarwinCoreField getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(DarwinCoreField field) {
        this.field = field;
    }
    
    @Override
    public String toString() {
        return "{columnNumber:" + columnNumber + ",columnLabel:" + columnLabel +
                ",term:" + field.getTerm() + ",type" + field.getType().getRowType() +
                "}";
    }
}
