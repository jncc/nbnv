/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;

/**
 *
 * @author Paul Gilbertson
 */
public class RunConversions {
    
    public List<ConverterStep> getSteps(List<ColumnMapping> mappings) {
        List<ConverterStep> steps = new ArrayList<ConverterStep>();
        
        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.ui.convert.converters");
        Set<Class<? extends ConverterStep>> converters = reflections.getSubTypesOf(ConverterStep.class);
        
        for (Class<? extends ConverterStep> stepClass : converters) {
            try {
                ConverterStep step = stepClass.newInstance();
                if (step.isStepNeeded(mappings)) {
                    steps.add(step);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(RunConversions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return steps;
    }
    
    public void modifyColumns(List<ConverterStep> steps, List<ColumnMapping> mappings) {
        for (ConverterStep step : steps) {
            step.modifyHeader(mappings);
        }
    }
    
    public void modifyRow(List<ConverterStep> steps, List<String> row) throws BadDataException {
        for (ConverterStep step : steps) {
            step.modifyRow(row);
        }
    }
    
    
}
