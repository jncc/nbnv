/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.List;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class OccurrenceStatus extends ConverterStep {
    private int column;
    
    public OccurrenceStatus() {
        super(ConverterStep.MODIFY);
    }

    @Override
    public String getName() {
        return "Convert ZeroAbundance T-F flag to controlled vocabulary";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.OCCURRENCESTATUS) {
                column = cm.getColumnNumber();
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        String data = row.get(column);

        if (data.equalsIgnoreCase("T") || data.equalsIgnoreCase("true") || data.equalsIgnoreCase("yes") || data.equalsIgnoreCase("absent")) {
            row.set(column, "absent");
        } else if (data.equalsIgnoreCase("F") || data.equalsIgnoreCase("false") || data.equalsIgnoreCase("no") || data.equalsIgnoreCase("present")) {
            row.set(column, "present");
        } else {
            throw new BadDataException("Bad zero abundance entry: " + data);
        }
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        // Nothing to do
    }
}

