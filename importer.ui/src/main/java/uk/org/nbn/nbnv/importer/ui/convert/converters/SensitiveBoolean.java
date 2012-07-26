/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.List;
import java.util.Map;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class SensitiveBoolean implements ConverterStep {

    @Override
    public String getName() {
        return "Convert Sensitive T-F flag to boolean";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.SENSITIVEOCCURRENCE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyRow(Map<DarwinCoreField, String> row) throws BadDataException {
        String data = row.get(DarwinCoreField.SENSITIVEOCCURRENCE);

        if (data.equalsIgnoreCase("T")) {
            row.put(DarwinCoreField.SENSITIVEOCCURRENCE, "true");
        } else if (data.equalsIgnoreCase("F")) {
            row.put(DarwinCoreField.SENSITIVEOCCURRENCE, "false");
        } else {
            throw new BadDataException("Bad sensitive entry: " + data);
        }
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        // Nothing to do
    }
}

