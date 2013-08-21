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
public class PointDataAttribute extends ConverterStep {

    private int eastColumn = -1;
    private int northColumn = -1;
    private int srsColumn = -1;
    
    public PointDataAttribute() {
        super(ConverterStep.ADD_COLUMNS);
    }

    @Override
    public String getName() {
        return "Convert East/North or Lat/Long to attributes";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
//        for (ColumnMapping cm : columns) {
//            if (cm.getField() == DarwinCoreField.VERBATIMLATITUDE) {
//                northColumn = cm.getColumnNumber();
//            } else if (cm.getField() == DarwinCoreField.VERBATIMLONGITUDE) {
//                eastColumn = cm.getColumnNumber();
//            } else if (cm.getField() == DarwinCoreField.VERBATIMSRS) {
//                srsColumn = cm.getColumnNumber();
//            }
//        }
//        
        if (eastColumn >= 0 && northColumn >= 0 && srsColumn >= 0) {
            return true;
        }
        
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
//        int highestColumn = -1;
//        
//        for (ColumnMapping cm : columns) {
//            highestColumn = cm.getColumnNumber() > highestColumn ? cm.getColumnNumber() : highestColumn;
//        }
//        
//        columns.add(new ColumnMapping(highestColumn + 1, "X", DarwinCoreField.ATTRIBUTE));
//        columns.add(new ColumnMapping(highestColumn + 2, "Y", DarwinCoreField.ATTRIBUTE));
//        columns.add(new ColumnMapping(highestColumn + 3, "SRS", DarwinCoreField.ATTRIBUTE));
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
//        row.add(row.get(eastColumn));
//        row.add(row.get(northColumn));
//        row.add(row.get(srsColumn));
    }
}
