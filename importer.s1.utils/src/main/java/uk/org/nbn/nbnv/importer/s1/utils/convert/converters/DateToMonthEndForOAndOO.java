/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.convert.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;

/**
 *
 * @author Matt Debont
 */
public class DateToMonthEndForOAndOO extends ConverterStep {

    private int idColumn = -1;
    private int dateTypeColumn;
    private int startDateColumn;
    private int endDateColumn;
    
    private List<SimpleDateFormat> types = new ArrayList<SimpleDateFormat>();

    public DateToMonthEndForOAndOO() {
        super(ConverterStep.MODIFY);
        
        // dd/mm/yyy, dd-mm-yyyy, yyyy/mm/dd, yyyy-mm-dd, dd mmm yyyy, mmm yyyy
        types.add(new SimpleDateFormat("dd/MM/yyyy"));
        types.add(new SimpleDateFormat("dd-MM-yyyy"));
        types.add(new SimpleDateFormat("yyyy/MM/dd"));
        types.add(new SimpleDateFormat("yyyy-MM-dd"));
        types.add(new SimpleDateFormat("dd MMM yyyy"));
        types.add(new SimpleDateFormat("MMM yyyy"));
        
        for (SimpleDateFormat type : types) {
            type.setLenient(false);
        }
    }

    @Override
    public String getName() {
        return "When Date Type is OO or O move the end date to the end of the month";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
//        for (ColumnMapping cm : columns) {
//            if (cm.getField() == DarwinCoreField.EVENTDATETYPECODE) {
//                dateTypeColumn = cm.getColumnNumber();
//            }
//            if (cm.getField() == DarwinCoreField.EVENTDATESTART) {
//                startDateColumn = cm.getColumnNumber();
//            }
//            if (cm.getField() == DarwinCoreField.EVENTDATEEND) {
//                endDateColumn = cm.getColumnNumber();
//            }
//            if (cm.getField() == DarwinCoreField.OCCURRENCEID) {
//                idColumn = cm.getColumnNumber();
//            }
//        }
//        
//        if (dateTypeColumn > 0 && startDateColumn > 0 && endDateColumn > 0) {
//            return true;
//        }
        
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        if (row.get(dateTypeColumn).trim().equals("OO") || row.get(dateTypeColumn).trim().equals("O")) {
            // TODO: Throw warnings!
            boolean matchedType = false;
            
            for (SimpleDateFormat type : types) {
                try {
                    Date start = type.parse(row.get(startDateColumn));
                    Calendar sCal = Calendar.getInstance();
                    sCal.setTime(start);
                    
                    Date end = type.parse(row.get(endDateColumn));
                    Calendar eCal = Calendar.getInstance();
                    eCal.setTime(end);
                    
                    if (eCal.get(Calendar.DAY_OF_MONTH) != eCal.getActualMaximum(Calendar.DAY_OF_MONTH)){
                        eCal.set(Calendar.DAY_OF_MONTH, eCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    }
                    
                    end = eCal.getTime();
                    
                    row.set(endDateColumn, type.format(end));
                    
                    matchedType = true;
                    
                    break;
                } catch (ParseException ex) {
                    // Don't do anything 
                }
            }
            
            if (!matchedType) {
                if (idColumn >= 0) {
                    throw new BadDataException("Dates were not valid - ID: " + row.get(idColumn) + "\tStart:" + row.get(startDateColumn) + " End:" + row.get(endDateColumn));
                } else {
                    throw new BadDataException("Dates were not valid - Start:" + row.get(startDateColumn) + " End:" + row.get(endDateColumn));
                }
            }            
        }
    }
}
