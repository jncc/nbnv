/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.convert.MappingException;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Taxon;

/**
 *
 * @author Matt Debont
 */
public class PreferredTaxonVersionKey extends ConverterStep {
    private int columnNumber;
    private Map<String, String> lookup;

    public PreferredTaxonVersionKey() {
        super(ConverterStep.MODIFY);
    }
    
    @Override
    public String getName() {
        return "Ensure that the Preferred Taxon Version Key is used";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getColumnLabel().equals("TaxonVersionKey")) {
                columnNumber = cm.getColumnNumber();
                lookup = new HashMap<String, String>();
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        // No modification necessary
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        // If we haven't looked for this TVK before then look for it otherwise
        // use stored value, so only throw error for first instance of an issue
        if (!lookup.containsKey(row.get(columnNumber))) {
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Taxon.findByTaxonVersionKey");
            q.setParameter("taxonVersionKey", row.get(columnNumber));

            List<Taxon> results = q.getResultList();

            if (!results.isEmpty()) {
                Taxon current = results.get(0);
                while (!current.getPTaxonVersionKey().getTaxonVersionKey().equals(current.getTaxonVersionKey())) {
                    current = current.getPTaxonVersionKey();
                }

                row.set(columnNumber, current.getTaxonVersionKey());
            } else {
                lookup.put(row.get(columnNumber), "");
                throw new BadDataException("Could not find an entry for the TaxonVersionKey: " + row.get(columnNumber));
            }
        } else {
            if (!lookup.get(row.get(columnNumber)).isEmpty()) {
                row.set(columnNumber, lookup.get(row.get(columnNumber)));
            }
        }
    }
    
    @Override
    public void checkMappings(List<ColumnMapping> mappings) throws MappingException {
        if (!isStepNeeded(mappings)) {
            throw new MappingException("Could not find necessary columns again for step: " + this.getClass().getName() + " - " + getName());
        }
    }
}
