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

/**
 *
 * @author Matt Debont
 */
public class TaxonName extends DependentStep {

    private static final String matchString = "(species|taxon(?!version))([a-z]+)?((\\s|_)?(name|id)?)";
    private ColumnMapping column;
    private Pattern pattern;
    private Map<String, List<String>> lookup;
    
    public TaxonName() {
        super(DependentStep.MODIFY & DependentStep.ADD_COLUMN);
        
        this.pattern = Pattern.compile(matchString, Pattern.CASE_INSENSITIVE);
        this.lookup = new HashMap<String, List<String>>();
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
                columns.set(cm.getColumnNumber(), new ColumnMapping(cm.getColumnNumber(), DarwinCoreField.TAXONID.getTerm(), DarwinCoreField.TAXONID));
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
            Query q = em.createNamedQuery("Taxon.findByTaxonName");
            q.setParameter("name", origVal);            
            
            List<String> results = q.getResultList();
            
            if (results.size() == 1) {
                lookup.put(origVal, results);
            } else {
                // TODO Do Fuzzy search 
                // If we find a return with more than one return then we have to return an ambiguous data exception
                //throw new AmbiguousDataException();
                
                List<String> res = new LinkedList<String>();
                res.add(origVal);
                lookup.put(origVal, res);
                
                throw new BadDataException("Could not find a Preferred Taxon Version Key for:" + origVal);
            }            
        }
        
        // Push Original Value to Attributes        
        
        // Put in lookupvalue, may be an error value we will handle this later on
        row.set(this.column.getColumnNumber(), lookup.get(origVal).get(0));
    }
}