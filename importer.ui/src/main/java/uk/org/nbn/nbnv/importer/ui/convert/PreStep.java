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
public class PreStep extends DependentStep {
    protected boolean persist = false;
    protected boolean firstCheck = true;
    
    public PreStep() {
        super();
    }

    protected boolean isFirstCheck() {
        return firstCheck;
    }

    protected void setFirstCheck(boolean firstCheck) {
        this.firstCheck = firstCheck;
    }

    protected boolean isPersist() {
        return persist;
    }

    protected void setPersist(boolean persist) {
        this.persist = persist;
    }
    
    public boolean isStepNeeded(boolean check) {
        if (isFirstCheck()) {
            setPersist(check);
            setFirstCheck(false);
        }
        return isPersist();
    }
    
}
