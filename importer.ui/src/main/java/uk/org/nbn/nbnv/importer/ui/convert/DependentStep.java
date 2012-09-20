/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.util.ArrayList;
import java.util.List;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;

/**
 *
 * @author Matt Debont
 */
public class DependentStep implements ConverterStep {
    private List<Class> dependsOn;
    private int modifier;
    
    public DependentStep() {
        dependsOn = new ArrayList<Class>();
    }
    
    public void addDependency(Class dependency) {
        dependsOn.add(dependency);
    }
    
    public List<Class> getDependency() {
        return dependsOn;
    }
    
    public boolean satisfyDependency(Class dependency) {
        return dependsOn.remove(dependency);
    }
    
    public int getModifier() {
        return modifier;
    }
    
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
