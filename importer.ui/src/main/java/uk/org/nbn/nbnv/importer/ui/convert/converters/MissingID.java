/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.List;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class MissingID extends ConverterStep {
    private int count = 1;
    private List<Integer> lookup = new ArrayList<Integer>();
    private int outColumn = -1;
    
    public MissingID() {
        super(ConverterStep.ADD_COLUMN);
    }
    
    @Override
    public String getName() {
        return "Adding missing record key column with number sequence";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.OCCURRENCEID) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
//        for (ColumnMapping cm : columns) {
//            cm.setColumnNumber(cm.getColumnNumber() + 1); // move all columns up 1
//        }
        
        // Try and build up some meaningful data to make a UID
        for (ColumnMapping cm : columns) {
            if ((cm.getField() == DarwinCoreField.TAXONID || 
                    cm.getField() == DarwinCoreField.COLLECTIONCODE || 
                    cm.getField() == DarwinCoreField.LOCATIONID || 
                    cm.getField() == DarwinCoreField.GRIDREFERENCE ||
                    cm.getField() == DarwinCoreField.SITEFEATUREKEY ||
                    cm.getField() == DarwinCoreField.EVENTID || 
                    cm.getField() == DarwinCoreField.EVENTDATESTART || 
                    cm.getField() == DarwinCoreField.EVENTDATE) && lookup.size() <= 1) {
                lookup.add(cm.getColumnNumber());
            }
        }
        
        for (ColumnMapping cm : columns) {
            outColumn = cm.getColumnNumber() > outColumn ? cm.getColumnNumber() : outColumn;
        }
        
        outColumn++;
        
        ColumnMapping cm = new ColumnMapping(outColumn, "RecordKey", DarwinCoreField.OCCURRENCEID);
        columns.add(cm);
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        String uid = "uid:";
        for (int col : lookup) {
            uid = uid + row.get(col) + ":";
        }
        
        uid = uid + (count++);
        
        row.add(uid);
    }
}
