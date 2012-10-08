package uk.org.nbn.nbnv.importer.ui.convert;

import java.util.ArrayList;
import java.util.List;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;

/**
 *
 * @author Paul Gilbertson
 */
public abstract class ConverterStep {
    public static final int ADD_COLUMN = 1;
    public static final int ADD_COLUMNS = 2;
    public static final int INSERT_COLUMN = 4;
    public static final int PERSIST = 8;
    public static final int MODIFY = 16;
    public static final int RUN_FIRST = 32;
    public static final int RUN_LAST = 64;
    protected int modifier = 0;
    protected List<Class> dependsOn;
    protected boolean soft = true;
    protected int minimumPos = -1;
    
    public ConverterStep(int modifier) {
        this.modifier = modifier;
        this.dependsOn = new ArrayList<Class>();
    }
    
    public void addDependency(Class dependency) {
        this.dependsOn.add(dependency);
    }
    
    public List<Class> getDependency() {
        return this.dependsOn;
    }
    
    public boolean hasDependency() {
        return !this.dependsOn.isEmpty();
    }
    
    public boolean satisfyDependency(Class dependency) {
        return this.dependsOn.remove(dependency);
    }    
    
    public boolean checkDependency(Class dependency) {
        return this.dependsOn.contains(dependency);
    }
    
    public int getModifier() {
        return this.modifier;
    }
    
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }    
    
    public boolean getSoft() {
        return this.soft;
    }
    
    public void setSoft(boolean soft) {
        this.soft = soft;
    }

    public int getMinimumPos() {
        return minimumPos;
    }

    public void setMinimumPos(int minimumPos) {
        this.minimumPos = minimumPos;
    }
    
    public abstract String getName();
    public abstract boolean isStepNeeded(List<ColumnMapping> columns);
    public abstract void modifyHeader(List<ColumnMapping> columns);
    public abstract void modifyRow(List<String> row) throws BadDataException;
    public abstract void checkMappings(List<ColumnMapping> columns) throws MappingException;
}
