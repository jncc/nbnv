/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.importer.s1.utils.errors.AmbiguousDataException;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.convert.StepOrderer;
import uk.org.nbn.nbnv.importer.s1.utils.errors.UnsatisfiableDependencyError;
import uk.org.nbn.nbnv.importer.s1.utils.model.MetadataForm;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.s1.utils.parser.NXFParser;
import uk.org.nbn.nbnv.importer.s1.utils.xmlWriters.MetaWriter;

/**
 *
 * @author Paul Gilbertson
 */
public class RunConversions {
    private String[] validDates = {"dd/MM/yyyy", 
        "dd-MM-yyyy", 
        "yyyy/MM/dd", 
        "yyyy-MM-dd", 
        "dd MMM yyyy", 
        "MMM yyyy", 
        "yyyy"};
    private List<String> dateFormats = Arrays.asList(validDates);
    
    private List<ConverterStep> steps = new ArrayList<ConverterStep>();
    private List<ColumnMapping> mappings;
    private NXFParser nxfParser;
    private int organisation;
    private MetadataForm metadataForm;
    private StepOrderer stepOrderer;
    
    private int startDateCol = -1;
    private Date startDate = new Date();
    private int endDateCol = -1;
    private Date endDate = new Date(Long.MIN_VALUE);

    /**
     * Simple Constructor for RunConversions class
     * 
     * @param in Input file to the nxfParser, reads a data file
     * @param organisation Optional input to determine if organisational dependent steps need to run
     * @throws IOException 
     */
    public RunConversions(File in, int organisation, MetadataForm metadataForm) throws IOException {
        this.nxfParser = new NXFParser(in);
        this.organisation = organisation;
        this.metadataForm = metadataForm;
    }

    private void modifyColumns(List<ConverterStep> steps, List<ColumnMapping> mappings) {
        for (ConverterStep step : steps) {
            step.modifyHeader(mappings);
        }
    }

    private void modifyRow(List<ConverterStep> steps, List<String> row) throws BadDataException {
        for (ConverterStep step : steps) {
            step.modifyRow(row);
        }
    }
    
    public List<String> run(File out, File meta, Map<String, String> args) throws IOException {
        List<String> errors = new ArrayList<String>();
        BufferedWriter w = null;

        try {
            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF8"));

            getMappings(args);
            
            stepOrderer = new StepOrderer(organisation);
            stepOrderer.getSteps(mappings);
            modifyColumns(stepOrderer.getSteps(), mappings);
            
            List<String> columnNames = new ArrayList<String>();

            for (ColumnMapping cm : mappings) {
                columnNames.add(cm.getColumnLabel());
            }

            w.write(StringUtils.collectionToDelimitedString(columnNames, "\t"));
            w.newLine();
            
            List<String> row;
            while ((row = nxfParser.readDataLine()) != null) {
                try {
                    updateStartEndDates(row);
                    modifyRow(stepOrderer.getSteps(), row);
                } catch (AmbiguousDataException ex) { 
                    errors.add("Ambiguous Data: " + ex.getMessage());
                } catch (BadDataException ex) {
                    errors.add("Bad Data: " + ex.getMessage());
                }
                w.write(StringUtils.collectionToDelimitedString(row, "\t"));
                w.newLine();
            }

            MetaWriter mw = new MetaWriter();
            errors.addAll(mw.createMetaFile(mappings, meta));
        } catch (IOException ex) {
            errors.add("IOException: " + ex.getMessage());
        } catch(UnsatisfiableDependencyError ex) {
            errors.add("UnsatsifiableDependencyError: " + ex.getMessage());
        } finally {
            if (w != null) {
                w.close();
            }
        }
        return errors;
    }

    private void updateStartEndDates(List<String> row) {
        for (String format : dateFormats) { 
            try {
                DateFormat df = new SimpleDateFormat(format);
                if (startDateCol >= 0 && endDateCol >= 0) {
                    Date currStart = df.parse(row.get(startDateCol));
                    if (startDate.after(currStart)) {
                        startDate = currStart;
                    }

                    Date currEnd = df.parse(row.get(endDateCol));
                    if (endDate.before(currEnd)) {
                        endDate = currEnd;
                    }
                    
                    if (currStart != null && currEnd != null) {
                        return;
                    }
                }
            } catch (ParseException ex) { 
                
            }
        }
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }

    private void getMappings(Map<String, String> args) throws IOException, FileNotFoundException {
        mappings = nxfParser.parseHeaders();
        for (ColumnMapping cm : mappings) {
            if (args.containsKey(Integer.toString(cm.getColumnNumber()))) {
                cm.setField(DarwinCoreField.valueOf(args.get(Integer.toString(cm.getColumnNumber()))));
                if (cm.getField() == DarwinCoreField.EVENTDATESTART) {
                    startDateCol = cm.getColumnNumber();
                } else if (cm.getField() == DarwinCoreField.EVENTDATEEND) {
                    endDateCol = cm.getColumnNumber();
                }
            }
        }
    }
       
    public List<ConverterStep> getSteps() {
        if (stepOrderer == null)
            throw new NullPointerException("Have not run the converter yet");
        return stepOrderer.getSteps();
    }
}
