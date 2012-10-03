/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.DependentStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Taxon;

/**
 *
 * @author Matt Debont
 */
public class TaxonName extends DependentStep {

    private static final String matchString = "(species|taxon(?!version))([a-z]+)?((\\s|_)?(name|id)?)";
    private ColumnMapping column;
    private Pattern pattern;
    private Map<String, String> lookup;
    
    public TaxonName() {
        super(DependentStep.MODIFY & DependentStep.ADD_COLUMN);
        
        this.pattern = Pattern.compile(matchString, Pattern.CASE_INSENSITIVE);
        this.lookup = new HashMap<String, String>();
    }
    
    @Override
    public String getName() {
        return "Find Taxon Version Key from a valid well formed Taxon Name";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getColumnLabel().equals("TaxonVersionKey")) {
                return false;
            } else if (pattern.matcher(cm.getColumnLabel()).matches()) {
                this.column = cm;
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm == this.column) {
                columns.set(cm.getColumnNumber(), new ColumnMapping(cm.getColumnNumber(), "TaxonVersionKey", DarwinCoreField.TAXONID));
                return;
            }
        }
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        // Add original values to attributes column
        
        String origVal = row.get(this.column.getColumnNumber());
        
        // If we haven't already had a look for this value then do so otherwise
        // use the stored value in the lookup tables
        if (!lookup.containsKey(origVal)) {
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Taxon.findByName");
            q.setParameter("name", origVal);            
            
            List<Taxon> results = q.getResultList();
            
            if (results.size() == 1) {
                lookup.put(origVal, results.get(0).getTaxonVersionKey());
            } else {
                
                // More than one result came back try and get the Preferred TaxonVersionKey
                boolean foundParent = false;
                Taxon current = (Taxon) results.get(0);
                while (!foundParent) {
                    if (current.getPTaxonVersionKey() == current) {
                        foundParent = true;
                    } else {
                        current = current.getPTaxonVersionKey();
                    }
                }
                
                lookup.put(origVal, current.getTaxonVersionKey());
            }            
        }
        
        // Push Original Value to Attributes        
        
        // Put in lookupvalue, may be an error value we will handle this later on
        row.set(this.column.getColumnNumber(), lookup.get(origVal));
    }
}