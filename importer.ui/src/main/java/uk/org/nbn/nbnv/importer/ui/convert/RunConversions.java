/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import uk.org.nbn.nbnv.importer.ui.util.UnsatisfiableDependencyError;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;
import uk.org.nbn.nbnv.importer.ui.meta.MetaWriter;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.NXFParser;
import uk.org.nbn.nbnv.importer.ui.util.OrganisationGroupXMLParser;

/**
 *
 * @author Paul Gilbertson
 */
public class RunConversions {
    private List<ConverterStep> steps;
    private List<ColumnMapping> mappings;
    private NXFParser nxfParser;
    private int organisation;

    public RunConversions(File in, int organisation) throws IOException {
        this.nxfParser = new NXFParser(in);
        this.organisation = organisation;
    }

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

    private List<ConverterStep> getDepSteps(List<ColumnMapping> mappings, Reflections reflections) throws UnsatisfiableDependencyError {
        ArrayList<DependentStep> depSteps = new ArrayList<DependentStep>();
        ArrayList<DependentStep> preSteps = new ArrayList<DependentStep>();
        ArrayList<DependentStep> postSteps = new ArrayList<DependentStep>();
        
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
        
        try {
            
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxFactory.newSAXParser();
            
            OrganisationGroupXMLParser handler = new OrganisationGroupXMLParser();
            
            saxParser.parse(getClass().getClassLoader().getResourceAsStream("OrganisationStepProvider.xml"), handler);
            
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
            
        } catch (ParserConfigurationException ex) {
            
        } catch (SAXException ex) {
            
        }
        

        
        for (DependentStep preStep : preSteps) {
            getSteps().add(0, preStep);
        }
        
        boolean actionPerformed;
        
        // TODO Need to fix infinite loop case!
        while (!depSteps.isEmpty()) {
            
            actionPerformed = false;
            ArrayList<ConverterStep> stepsToAdd = new ArrayList<ConverterStep>();
            
            for (ConverterStep cStep : getSteps()) {
                
                ListIterator<DependentStep> dStepsIterator = depSteps.listIterator();
                
                while (dStepsIterator.hasNext()) {
                    DependentStep dStep = dStepsIterator.next();
                    
                    if ((dStep.satisfyDependency(cStep.getClass())) || dStep.getDependency().isEmpty()) {
                        stepsToAdd.add(dStep);
                        dStepsIterator.remove();
                        actionPerformed = true;
                    }
                }
            }
            
            if (!actionPerformed) {
                
                boolean dStepChecked;
                
                ListIterator<DependentStep> dStepIterator = depSteps.listIterator();
                
                while(dStepIterator.hasNext()) {
                    DependentStep dStep = dStepIterator.next();
                    
                    dStepChecked = false;
                    // Check if dependency is in depList
                    for (DependentStep checkDep : depSteps) {
                        if (dStep.checkDependency(checkDep.getClass())) {
                            dStepChecked = true;
                        }
                    }

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
                        
                        if (!dStepChecked) {
                            getSteps().add(dStep);
                            actionPerformed = true;
                        }
                    }
                } 
                
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
                getSteps().addAll(stepsToAdd);
            }
        }
        
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

    private void getMappings(Map<String, String> args) throws IOException, FileNotFoundException {
        mappings = nxfParser.parseHeaders();
        for (ColumnMapping cm : mappings) {
            if (args.containsKey(Integer.toString(cm.getColumnNumber()))) {
                cm.setField(DarwinCoreField.valueOf(args.get(Integer.toString(cm.getColumnNumber()))));
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
