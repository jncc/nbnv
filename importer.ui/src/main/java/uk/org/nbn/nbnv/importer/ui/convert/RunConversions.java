/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;
import uk.org.nbn.nbnv.importer.ui.meta.MetaWriter;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.NXFParser;
import uk.org.nbn.nbnv.importer.ui.util.OrganisationGroupXMLParser;
import uk.org.nbn.nbnv.importer.ui.util.UnsatisfiableDependencyError;

/**
 *
 * @author Paul Gilbertson
 */
public class RunConversions {
    private List<ConverterStep> steps;
    private List<ColumnMapping> mappings;
    private NXFParser nxfParser;
    private int organisation;
    private MetadataForm metadataForm;
    
    private int startDateCol = -1;
    private Date startDate = new Date();
    private int endDateCol = -1;
    private Date endDate = new Date();

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
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            endDate = df.parse("01/01/1900");
        } catch(ParseException ex) {
            endDate = new Date(0);
        }
    }
    
    /**
     * File the provided step in the proper list according to its action and 
     * dependencies, for easier sorting later on
     * 
     * @param preSteps 
     * @param currSteps
     * @param postSteps
     * @param depSteps
     * @param step 
     */
    private void fileStep (List<ConverterStep> preSteps, 
            List<ConverterStep> currSteps, List<ConverterStep> postSteps, 
            List<ConverterStep> depSteps, ConverterStep step) {
        if (step.hasDependency()) {
            depSteps.add(step);
        } else if (((step.getModifier() & ConverterStep.RUN_FIRST) > 0)) {
            preSteps.add(step);
        } else if ((step.getModifier() & ConverterStep.INSERT_COLUMN) > 0) {
            currSteps.add(0, step);
        } else {
            currSteps.add(step);
        }
    }
    
    /**
     * Grab the steps we think are needed for a given input
     * 
     * @param mappings
     * @return
     * @throws UnsatisfiableDependencyError 
     */
    private List<ConverterStep> getSteps(List<ColumnMapping> mappings) throws UnsatisfiableDependencyError {
        setSteps(new ArrayList<ConverterStep>());
        
        // Pre Steps
        List<ConverterStep> preSteps = new ArrayList<ConverterStep>();
        // Regular Steps
        List<ConverterStep> currSteps = new ArrayList<ConverterStep>();
        // Post Steps
        List<ConverterStep> postSteps = new ArrayList<ConverterStep>();
        // Unsorted Dependencies
        List<ConverterStep> depSteps = new ArrayList<ConverterStep>();

        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.ui.convert.converters");
        
        Set<Class<? extends ConverterStep>> converters = reflections.getSubTypesOf(ConverterStep.class);

        for (Class<? extends ConverterStep> stepClass : converters) {
            try {
                ConverterStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    fileStep(preSteps, currSteps, postSteps, depSteps, step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Add any organisation required steps to the mix
        getOrgSteps(mappings, reflections, preSteps, currSteps, postSteps, depSteps);
        // Sort steps if necessary
        setSteps(sortSteps(preSteps, currSteps, postSteps, depSteps));
        
        return getSteps();
    }

    /**
     * Grab the steps with some sort of dependency, restriction or similar may
     * be involved
     * 
     * @param mappings
     * @param reflections
     * @param preSteps
     * @param currSteps
     * @param postSteps
     * @param depSteps 
     */
    private void getOrgSteps(List<ColumnMapping> mappings, 
            Reflections reflections, List<ConverterStep> preSteps, 
            List<ConverterStep> currSteps, List<ConverterStep> postSteps, 
            List<ConverterStep> depSteps) {
        
        // Grab the dependencies from the OrganisationStepProvider.xml if we 
        // haven't already then process any organisational step dependencies
        try {
            // Singleton which tells us this step has been run if it has. so 
            // we aren't continuously runnig the thing
            OrganisationGroupXMLParser handler = OrganisationGroupXMLParser.getInstance();
            
            // If we haven't run the parser yet then run it and mark it as run
            if (!handler.hasBeenRun()) {
                SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxFactory.newSAXParser();

                saxParser.parse(getClass().getClassLoader().getResourceAsStream("OrganisationStepProvider.xml"), handler);
                handler.parserRun();
            }
            
            // Get all Dependent Steps which are dependent on organisations as 
            // well
            Set<Class<? extends OrganisationStep>> orgConverters = reflections.getSubTypesOf(OrganisationStep.class);

            for (Class<? extends OrganisationStep> stepClass: orgConverters) {
                try {
                    OrganisationStep step = stepClass.newInstance();
                    if (step.isStepNeeded(mappings, handler.getGroups(organisation))) {   
                        fileStep(preSteps, currSteps, postSteps, depSteps, step);
                    }
                } catch (InstantiationException ex) {
                    Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            
        } catch (IOException ex) {
            Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sort the lists of steps into one list with satisfied dependencies if possible
     * if not throws an error to let you know something is very wrong here
     * 
     * @param preSteps Steps which should run before anything else
     * @param currSteps Steps which run order doesn't really matter
     * @param postSteps Steps which should be run after anything else
     * @param depSteps Steps which are directly dependent on other steps (hard or soft dependencies included)
     * @return An ordered list of steps which should work correctly
     * @throws UnsatisfiableDependencyError 
     */
    private List<ConverterStep> sortSteps(List<ConverterStep> preSteps, 
            List<ConverterStep> currSteps, List<ConverterStep> postSteps, 
            List<ConverterStep> depSteps) throws UnsatisfiableDependencyError {
        
        List<ConverterStep> orderedSteps = new ArrayList<ConverterStep>();
        
        // Add our pre-steps and normal steps to the mix so we can start 
        // processing dependencies
        orderedSteps.addAll(preSteps);
        orderedSteps.addAll(currSteps);
        
        // Everything after postStepIndex is a postStep
        int postStepIndex = orderedSteps.size() - 1;
        orderedSteps.addAll(postSteps);
        
        
        // While we haven't scheduled all dependent steps
        while (!depSteps.isEmpty()) {
            
            List<ConverterStep> copyOfOrderedSteps = new ArrayList<ConverterStep>();
            copyOfOrderedSteps.addAll(orderedSteps);
            
            boolean depStepAdded = false;
            
            for (ConverterStep step : copyOfOrderedSteps) {
                Iterator<ConverterStep> depStepIterator = depSteps.iterator();
                while (depStepIterator.hasNext()) {
                    ConverterStep depStep = depStepIterator.next();
                    depStep.satisfyDependency(step.getClass());
                    
                    if (!depStep.hasDependency()) {
                        orderedSteps.add(orderedSteps.indexOf(step) + 1,depStep);
                        depStepIterator.remove();
                        depStepAdded = true;
                        postStepIndex++;
                    }
                }
            }
            
            // Check if dependecy is soft 
            Iterator<ConverterStep> depStepIterator = depSteps.iterator();
            while (depStepIterator.hasNext()) {
                ConverterStep depStep = depStepIterator.next();
                if (depStep.soft) {
                    
                    boolean exists = false;
                    
                    for (ConverterStep cStep : depSteps) {
                        if (depStep.checkDependency(cStep.getClass())) {
                            exists = true;
                        }
                    }
                    
                    if (!exists) {
                        orderedSteps.add(postStepIndex + 1, depStep);
                        postStepIndex++;
                        depStepAdded = true;
                        depStepIterator.remove();
                    }
                }
            }
            
            // If we have been blocked then we need to bow out gracefully
            if (!depStepAdded) {
                if (depSteps.size() > 1) {
                    String depFails = " - ";
                    for (ConverterStep step : depSteps) {
                        depFails = depFails + step.getClass().getCanonicalName() + " - ";
                    }
                    throw new UnsatisfiableDependencyError("Could not satisfy a dependency on multiple steps "
                            + depFails);
                } else {
                    throw new UnsatisfiableDependencyError("Could not satsify a dependency on step " + depSteps.get(0).getName());
                }
            }
        }
        
        return orderedSteps;
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

    private void checkMappings(List<ConverterStep> steps, List<ColumnMapping> mappings) throws MappingException {
        for (ConverterStep step : steps) {
            step.checkMappings(mappings);
        }
    }
    
    public List<String> run(File out, File meta, Map<String, String> args) throws IOException {
        List<String> errors = new ArrayList<String>();
        BufferedWriter w = null;

        try {
            w = new BufferedWriter(new FileWriter(out));

            getMappings(args);
            getSteps(mappings);           
            modifyColumns(getSteps(), mappings);
            
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
                    modifyRow(getSteps(), row);
                } catch (AmbiguousDataException ex) { 
                    errors.add("Ambiguous Data: " + ex.getMessage());
                } catch (BadDataException ex) {
                    errors.add("Bad Data: " + ex.getMessage());
                } catch (ParseException ex) {
                    errors.add("Error Parsing Date fields: " + ex.getMessage());
                } 
                w.write(StringUtils.collectionToDelimitedString(row, "\t"));
                w.newLine();
            }

            MetaWriter mw = new MetaWriter();
            errors.addAll(mw.createMetaFile(mappings, meta));
//        } catch (MappingException ex) {
//            errors.add("MappingException: " + ex.getMessage());
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
    
    private void updateStartEndDates(List<String> row) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        if (startDateCol >= 0 && endDateCol >= 0) {
            Date currStart = df.parse(row.get(startDateCol));
            if (startDate.after(currStart)) {
                startDate = currStart;
            }
            
            Date currEnd = df.parse(row.get(endDateCol));
            if (endDate.before(currEnd)) {
                endDate = currEnd;
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

    /**
     * @return the steps
     */
    public List<ConverterStep> getSteps() {
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(List<ConverterStep> steps) {
        this.steps = steps;
    }
}
