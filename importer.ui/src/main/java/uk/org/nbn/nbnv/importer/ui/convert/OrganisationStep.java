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
public abstract class OrganisationStep extends DependentStep {
    private String orgGroup;
    
    public OrganisationStep (String orgGroup, int modifier) {
        super(modifier);
        this.orgGroup = orgGroup;
    }
    
    public String getOrganisationName() {
        return orgGroup;
    }
    
    public boolean isStepNeeded(List<ColumnMapping> columns, String orgGroup) {
        if (orgGroup.equalsIgnoreCase(this.orgGroup)) {
            return isStepNeeded(columns);
        }
        return false;
    }
}
