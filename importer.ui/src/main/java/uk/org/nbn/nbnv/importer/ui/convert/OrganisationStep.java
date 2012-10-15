/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.util.List;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;

/**
 *
 * @author Matt Debont
 */
public abstract class OrganisationStep extends ConverterStep {
    private int orgGroup;
    
    public OrganisationStep (int orgGroup, int modifier) {
        super(modifier);
        this.orgGroup = orgGroup;
    }
    
    public int getOrganisationName() {
        return orgGroup;
    }
    
    public boolean isStepNeeded(List<ColumnMapping> columns, List<Integer> groups) {
        for(int groupID : groups) {
            if (groupID == orgGroup) {
                return isStepNeeded(columns);
            }
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
