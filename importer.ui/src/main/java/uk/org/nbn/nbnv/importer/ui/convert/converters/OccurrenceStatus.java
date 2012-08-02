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
public class OccurrenceStatus implements ConverterStep {

    @Override
    public String getName() {
        return "Convert ZeroAbundance T-F flag to controlled vocabulary";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.OCCURRENCESTATUS) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyRow(Map<DarwinCoreField, String> row) throws BadDataException {
        String data = row.get(DarwinCoreField.OCCURRENCESTATUS);

        if (data.equalsIgnoreCase("T")) {
            row.put(DarwinCoreField.OCCURRENCESTATUS, "absent");
        } else if (data.equalsIgnoreCase("F")) {
            row.put(DarwinCoreField.OCCURRENCESTATUS, "present");
        } else {
            throw new BadDataException("Bad zero abundance entry: " + data);
        }
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        // Nothing to do
    }
}

