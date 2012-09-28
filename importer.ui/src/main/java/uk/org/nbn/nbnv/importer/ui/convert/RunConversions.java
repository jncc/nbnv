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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
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
    public RunConversions(File in, int organisation) throws IOException {
        this.nxfParser = new NXFParser(in);
        this.organisation = organisation;
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

        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.ui.convert.converters");
        
        Set<Class<? extends ConverterStep>> converters = reflections.getSubTypesOf(ConverterStep.class);

        for (Class<? extends ConverterStep> stepClass : converters) {
            try {
                ConverterStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    getSteps().add(step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        setSteps(getDepSteps(mappings, reflections));

        return getSteps();
    }

    /**
     * Grab the steps with some sort of dependency, restriction or similar may
     * be involved
     * 
     * @param mappings
     * @param reflections
     * @return
     * @throws UnsatisfiableDependencyError 
     */
    private List<ConverterStep> getDepSteps(List<ColumnMapping> mappings, Reflections reflections) throws UnsatisfiableDependencyError {
        // Stores regular steps with some form of dependency or restrictions
        ArrayList<DependentStep> depSteps = new ArrayList<DependentStep>();
        // Stores steps which we want to run first
        ArrayList<ConverterStep> preSteps = new ArrayList<ConverterStep>();
        // Stores steps which we should run after all others
        ArrayList<DependentStep> postSteps = new ArrayList<DependentStep>();
        
        // Get all dependent steps and coarsely sort them into the arrays above
        Set<Class<? extends DependentStep>> depConverters = reflections.getSubTypesOf(DependentStep.class);
        for (Class<? extends DependentStep> stepClass: depConverters) {
            try {
                DependentStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    if ((step.getModifier() & DependentStep.RUN_FIRST) != 0) {
                        preSteps.add(step);
                    } else if ((step.getModifier() & DependentStep.INSERT_COLUMN) != 0) {
                        postSteps.add(step);
                    } else {
                        depSteps.add(step);
                    }
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        
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
                        depSteps.add(step);
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

        // Add all Pre-Steps to the beginning of the 
        preSteps.addAll(getSteps());
        setSteps(preSteps);
        
        // Infinite loop preventer flag
        boolean actionPerformed;
        
        // While we have dependent steps still waiting to be queued
        while (!depSteps.isEmpty()) {
            
            // Infinite loop preventer flag set to false to mark new stage of
            // actions
            actionPerformed = false;
            // Storage list for steps to be added as we are iterating
            List<ConverterStep> stepsToAdd = new ArrayList<ConverterStep>();
            
            // For each step we have already in the queue
            for (ConverterStep cStep : getSteps()) {
                
                // See if this step satisfies a dependency, if so check if any
                // remain for this dependent step adding it to the stepsToAdd List
                // if none do
                ListIterator<DependentStep> dStepsIterator = depSteps.listIterator();
                while (dStepsIterator.hasNext()) {
                    DependentStep dStep = dStepsIterator.next();
                    
                    if ((dStep.satisfyDependency(cStep.getClass())) || dStep.getDependency().isEmpty()) {
                        stepsToAdd.add(dStep);
                        dStepsIterator.remove();
                        // We did an action in this round \o/
                        actionPerformed = true;
                    }
                }
            }
            
            // If no actions where performed
            if (!actionPerformed) {
                
                boolean dStepChecked;
                
                // Attempt to queue this dependency by checking if its dependency
                // is even being run / exists in the dependency list / exists in
                // the post steps
                ListIterator<DependentStep> dStepIterator = depSteps.listIterator();
                while(dStepIterator.hasNext()) {
                    DependentStep dStep = dStepIterator.next();
                    
                    dStepChecked = false;
                    // Check if dependency is in depList, action stalls until no
                    // further action is possible
                    for (DependentStep checkDep : depSteps) {
                        if (dStep.checkDependency(checkDep.getClass())) {
                            dStepChecked = true;
                        }
                    }

                    // If dependency is not in the current dependency list, check
                    // other places for it adding it if no obstruction
                    if (!dStepChecked) {
                        // Check if dependency is in postList
                        for (int i = 0; i < postSteps.size(); i++) {
                            DependentStep postStep = postSteps.get(i);
                            if (dStep.satisfyDependency(postStep.getClass()) && dStep.getDependency().isEmpty()) {
                                postSteps.add(i + 1, dStep);
                                dStepIterator.remove();
                                dStepChecked = true;
                                actionPerformed = true;
                                break;
                            }
                        }
                        
                        // If its not anywhere then we can probably add it here
                        // if you want a hard dependency then add the dependency
                        // to the steps needed!
                        if (!dStepChecked) {
                            getSteps().add(dStep);
                            actionPerformed = true;
                        }
                    }
                } 
                
                // If we still haven't done something by the end of the loop something
                // is horribly wrong and we need to re-think our dependencies again!
                if (!actionPerformed) {
                    // Locked up, dependencies are seriously screwed up for some 
                    // reason
                    if (depSteps.size() > 1) {
                        String depFails = " - ";
                        for(DependentStep step : depSteps) {
                            depFails = depFails + step.getClass().getCanonicalName() + " - ";
                        }
                        throw new UnsatisfiableDependencyError("Could not satisfy a dependency on multiple steps " + 
                                depFails);
                    } else {
                        throw new UnsatisfiableDependencyError("Could not satsify a dependency on step " + depSteps.get(0).getName());
                    }
                }
            } else {
                // Add all currently satisfied dependencies in this round
                getSteps().addAll(stepsToAdd);
            }
        }
        
        // Add our poststeps to the mix 
        getSteps().addAll(postSteps);
        
        return getSteps();
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
                    modifyRow(getSteps(), row);
                    updateStartEndDates(row);
                } catch (BadDataException ex) {
                    errors.add("Bad Data: " + ex.getMessage());
                } catch (ParseException ex) {
                    errors.add("Errors Parsing Date fields: " + ex.getMessage());
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
    
    private void updateStartEndDates(List<String> row) throws ParseException {
        if (startDateCol >= 0 && endDateCol >= 0) {
            Date currStart = DateFormat.getDateInstance().parse(row.get(startDateCol));
            if (startDate.after(currStart)) {
                startDate = currStart;
            }
            
            Date currEnd = DateFormat.getDateInstance().parse(row.get(endDateCol));
            if (endDate.before(currEnd)) {
                endDate = currEnd;
            }
        }
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
