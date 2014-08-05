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
public class SensitiveBoolean extends ConverterStep {
    private int column;
    
    public SensitiveBoolean() {
        super(ConverterStep.MODIFY);
    }

    @Override
    public String getName() {
        return "Convert Sensitive T-F flag to boolean";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.SENSITIVEOCCURRENCE) {
                column = cm.getColumnNumber();
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        String data = row.get(column);

        if (data.equalsIgnoreCase("T") || data.equalsIgnoreCase("true") || data.equalsIgnoreCase("yes")) {
            row.set(column, "true");
        } else if (data.equalsIgnoreCase("F") || data.equalsIgnoreCase("false") || data.equalsIgnoreCase("no")) {
            row.set(column, "false");
        } else {
            throw new BadDataException("Sensitive, '"  + data + "' is not an accepted value in the NBN Exchange Format");
        }
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        // Nothing to do
    }
}

