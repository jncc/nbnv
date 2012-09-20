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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.importer.ui.meta.MetaWriter;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.NXFParser;

/**
 *
 * @author Paul Gilbertson
 */
public class RunConversions {
    private List<ConverterStep> steps;
    private List<ColumnMapping> mappings;
    private NXFParser nxfParser;
    
    private static final Set<Class> classes = new HashSet<Class>(Arrays.asList(new Class[] {ConverterStep.class, DependentStep.class, OrganisationStep.class, PreStep.class}));

    public RunConversions(File in) throws IOException {
        this.nxfParser = new NXFParser(in);
    }

    private List<ConverterStep> getSteps(List<ColumnMapping> mappings) {
        setSteps(new ArrayList<ConverterStep>());

        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.ui.convert.converters");
        
        setSteps(getPreSteps(mappings, reflections));
        
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
        
        setSteps(getPostSteps(mappings, reflections));

        return getSteps();
    }
    
    private List<ConverterStep> getPostSteps(List<ColumnMapping> mappings, Reflections reflections) {
        
        Set<Class<? extends PostStep>> postConverters = reflections.getSubTypesOf(PostStep.class);
        
        for (Class<? extends PostStep> stepClass: postConverters) {
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
        
        return getSteps();
    }
    
    private List<ConverterStep> getPreSteps(List<ColumnMapping> mappings, Reflections reflections) {
        
        Set<Class<? extends PreStep>> preConverters = reflections.getSubTypesOf(PreStep.class);
        
        for (Class<? extends PreStep> stepClass: preConverters) {
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
        
        return getSteps();
    }

    private List<ConverterStep> getDepSteps(List<ColumnMapping> mappings, Reflections reflections) {
        ArrayList<DependentStep> depSteps = new ArrayList<DependentStep>();
        
        Set<Class<? extends DependentStep>> depConverters = reflections.getSubTypesOf(DependentStep.class);
        for (Class<? extends DependentStep> stepClass: depConverters) {
            try {
                DependentStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    depSteps.add(step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Set<Class<? extends OrganisationStep>> orgConverters = reflections.getSubTypesOf(OrganisationStep.class);
        
        for (Class<? extends OrganisationStep> stepClass: orgConverters) {
            try {
                DependentStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    depSteps.add(step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        boolean actionPerformed;
        
        // TODO Need to fix infinite loop case!
        while (!depSteps.isEmpty()) {
            
            actionPerformed = false;
            ArrayList<ConverterStep> stepsToAdd = new ArrayList<ConverterStep>();
            
            for (ConverterStep step : getSteps()) {
                ListIterator<DependentStep> dSteps = depSteps.listIterator();
                while(dSteps.hasNext()) {
                    DependentStep propStep = dSteps.next();
                    propStep.satisfyDependency(step.getClass());
                    
                    if (propStep.getDependency().isEmpty()) {
                        stepsToAdd.add(propStep);
                        dSteps.remove();
                        actionPerformed = true;
                    }
                }
            }
            
            getSteps().addAll(stepsToAdd);
            
            if (!actionPerformed) {
                System.err.println("Remaining Dependent Steps to be allocated include;");
                for (DependentStep errSteps : depSteps) {
                    System.err.println(errSteps.getClass().toString());
                }
                throw new UnsatisfiedLinkError("Dependency Structure for Converter Steps is not satisfiable, please check your dependencies!");
            }
        }
        
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
