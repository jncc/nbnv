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
public abstract class DependentStep implements ConverterStep {
    private List<Class> dependsOn;
    private boolean inviable = false;
    private boolean isFirstCheck = true;
    private boolean stepPersists = false;
    private int modifier;
    
    public static final int ADD_COLUMN = 1;
    public static final int ADD_COLUMNS = 2;
    public static final int INSERT_COLUMN = 4;
    public static final int PERSIST = 8;
    public static final int RUN_FIRST = 16;
    public static final int RUN_LAST = 32;
    
    public DependentStep(int modifier) {
        this.modifier = modifier;
        this.dependsOn = new ArrayList<Class>();
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
    
    public boolean checkDependency(Class dependency) {
        return dependsOn.contains(dependency);
    }
    
    public int getModifier() {
        return modifier;
    }
    
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public boolean isInviable() {
        return inviable;
    }

    public void setInviable(boolean inviable) {
        this.inviable = inviable;
    }
    
    public boolean peristanceCheck(boolean check) {
        if ((modifier & PERSIST) > 0) {
            if (isFirstCheck) {
                isFirstCheck = false;
                stepPersists = check;
            }    
            return stepPersists;
        }
        return false;
    }
    
    @Override
    public abstract String getName();
    @Override
    public abstract boolean isStepNeeded(List<ColumnMapping> columns);
    @Override
    public abstract void modifyHeader(List<ColumnMapping> columns);
    @Override
    public abstract void modifyRow(List<String> row) throws BadDataException;    
}
