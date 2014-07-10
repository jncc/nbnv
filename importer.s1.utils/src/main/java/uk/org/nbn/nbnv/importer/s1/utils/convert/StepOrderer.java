/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.convert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.reflections.Reflections;
import org.xml.sax.SAXException;
import uk.org.nbn.nbnv.importer.s1.utils.errors.UnsatisfiableDependencyError;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.tools.OrganisationGroupXMLParser;

/**
 *
 * @author Matt Debont
 */
public class StepOrderer {
    private List<ConverterStep> steps;
    private int organisation;
    
    public StepOrderer(int organisation) {
        this.organisation = organisation;
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
    public List<ConverterStep> getSteps(List<ColumnMapping> mappings) throws UnsatisfiableDependencyError {
        // Pre Steps
        List<ConverterStep> preSteps = new ArrayList<ConverterStep>();
        // Regular Steps
        List<ConverterStep> currSteps = new ArrayList<ConverterStep>();
        // Post Steps
        List<ConverterStep> postSteps = new ArrayList<ConverterStep>();
        // Unsorted Dependencies
        List<ConverterStep> depSteps = getSteps(); //new ArrayList<ConverterStep>();

        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.s1.utils.convert.converters");
        
        Set<Class<? extends ConverterStep>> converters = reflections.getSubTypesOf(ConverterStep.class);

        for (Class<? extends ConverterStep> stepClass : converters) {
            try {
                ConverterStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    fileStep(preSteps, currSteps, postSteps, depSteps, step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            
        } catch (IOException ex) {
            Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(StepOrderer.class.getName()).log(Level.SEVERE, null, ex);
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
                    // Get the minimum position that this step can exist at
                    depStep.setMinimumPos(orderedSteps.indexOf(step) + 1);
                    
                    if (!depStep.hasDependency()) {
                        orderedSteps.add(depStep.getMinimumPos(), depStep);
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
                    
                    if (!exists || !depStep.hasDependency()) {
                        // If dependencies dont exist and are soft then put step either at the 
                        // minimum position defined or the postStepIndex (so either at postStepIndex
                        // or after)
                        orderedSteps.add(Math.max(depStep.getMinimumPos(), postStepIndex + 1), depStep);
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
    
    /**
     * @return the steps
     */
    public List<ConverterStep> getSteps() {
        if (steps == null)
            return new ArrayList<ConverterStep>();
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(List<ConverterStep> steps) {
        this.steps = steps;
    }    
}
