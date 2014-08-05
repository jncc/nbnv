/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.convert.converters;

import java.util.List;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class GridRefCleanup extends ConverterStep {
    private int gridRefColumn;    
    
    public GridRefCleanup() {
        super(ConverterStep.MODIFY);
    }
    
    @Override
    public String getName() {
        return "Removed Spaces and -'s from Grid Reference if they exist";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.GRIDREFERENCE) {
                gridRefColumn = cm.getColumnNumber();
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {        

    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        row.set(gridRefColumn, row.get(gridRefColumn).replaceAll(" ", "").replaceAll("-", ""));
    }
}
