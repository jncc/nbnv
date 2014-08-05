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
public class PrecisionNormaliser extends ConverterStep {
    private int column;
    
    public PrecisionNormaliser() {
        super(ConverterStep.MODIFY);
    }
    
    @Override
    public String getName() {
        return "Normalise GridReferencePrecision field to supported resolutions";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.GRIDREFERENCEPRECISION) {
                column = cm.getColumnNumber();
                return true;
            }
        }
        return false;

    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        // Do nothing
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        if (!row.get(column).trim().isEmpty()) {
            int data = Integer.parseInt(row.get(column));

            if (data <= 100) {
                data = 100;
            } else if (data <= 1000) {
                data = 1000;
            } else if (data <= 2000) {
                data = 2000;
            } else if (data <= 10000) {
                data = 10000;
            } else {
                throw new BadDataException("Precision, '" + data + "' is lower than the maximum 10KM allowed on the NBN Gateway");
            }
            
            row.set(column, Integer.toString(data));
        } else {
            row.set(column, "");
        }
    }
}