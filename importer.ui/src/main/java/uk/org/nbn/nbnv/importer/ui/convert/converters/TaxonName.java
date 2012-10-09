package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.ui.convert.AmbiguousDataException;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.convert.MappingException;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.RecordingEntity;
import uk.org.nbn.nbnv.jpa.nbncore.Taxon;

/**
 *
 * @author Matt Debont
 */
public class TaxonName extends ConverterStep {

    private static final String matchString = "(species|taxon(?!version)|scientific)([a-z]+)?((\\s|_)?(name|id)?)";
    private int column;
    private Pattern pattern;
    private Map<String, String> lookup;
    
    public TaxonName() {
        super(ConverterStep.MODIFY);
        
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
                this.column = cm.getColumnNumber();
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {    
        columns.set(column, new ColumnMapping(column, "TaxonVersionKey", DarwinCoreField.TAXONID));
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        // Add original values to attributes column
        
        String origVal = row.get(this.column);
        
        // If we haven't already had a look for this value then do so otherwise
        // use the stored value in the lookup tables
        if (!lookup.containsKey(origVal)) {
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Taxon.findByName");
            q.setParameter("name", origVal);            
            
            List<Taxon> results = q.getResultList();
            
            if (results.size() == 1) {
                lookup.put(origVal, results.get(0).getPTaxonVersionKey().getTaxonVersionKey());
            } else if (results.size() > 1) {
                
                String pTVK = results.get(0).getPTaxonVersionKey().getTaxonVersionKey();
                for (Taxon taxon : results) {
                    if (!taxon.getPTaxonVersionKey().getTaxonVersionKey().equals(pTVK)) {
                        throw new AmbiguousDataException("Found Multiple Prefered TaxonVersionKeys in result set, possible dangerous name?");
                    }
                }
                
                lookup.put(origVal, pTVK);
            } else {
                // Check RecordingEntity Tables for a translation
                q = em.createNamedQuery("RecordingEntity.findByRecordedName");
                q.setParameter("name", origVal);
                
                List<RecordingEntity> res = q.getResultList();
                
                if (res.size() == 1) {
                    lookup.put(origVal, res.get(0).getTaxon().getPTaxonVersionKey().getTaxonVersionKey());
                } else if (results.size() > 1) {               
                    String pTVK = res.get(0).getTaxon().getPTaxonVersionKey().getTaxonVersionKey();
                    for (RecordingEntity recordingEntity : res) {
                        if (!recordingEntity.getTaxon().getPTaxonVersionKey().getTaxonVersionKey().equals(pTVK)) {
                            throw new AmbiguousDataException("Found Multiple Prefered TaxonVersionKeys in result set, possible dangerous name? - " + recordingEntity.getRecordedName());
                        }
                        if (recordingEntity.getDangerous()) {
                            throw new BadDataException("Found record, but it is marked as dangerous: " + recordingEntity.getRecordedName());
                        }
                    }
                    
                    lookup.put(origVal, pTVK);
                    
                } else if (origVal.length() == 16) {
                    // Possible Taxon Version Key?
                    q = em.createNamedQuery("Taxon.findByTaxonVersionKey");
                    results = q.setParameter("taxonVersionKey", origVal).getResultList();
                    
                    if (res.size() == 1) {
                        lookup.put(origVal, results.get(0).getPTaxonVersionKey().getTaxonVersionKey());
                    } else if (res.size() > 1) {
                        String pTVK = results.get(0).getPTaxonVersionKey().getTaxonVersionKey();
                        for (Taxon taxon : results) {
                            if (!pTVK.equals(taxon.getPTaxonVersionKey().getTaxonVersionKey())) {
                                throw new AmbiguousDataException("Found more than one Preferred Taxon Version Key from an input which cannot be matched to a valid scientific name, common name, taxon verion key or any other recording entity - " + origVal);
                            }
                        }
                    }
                } else {    
                    lookup.put(origVal, origVal);
                    throw new BadDataException("Could not find a valid tansformation as a valid scientific name, common name or other recording entitiy - " + origVal);
                }
            }
        }
        
        row.set(column, lookup.get(origVal));
    }
    
    @Override
    public void checkMappings(List<ColumnMapping> mappings) throws MappingException {
        boolean foundCol = false;
        
        for (ColumnMapping cm : mappings) {
            if (cm.getColumnLabel().equals("TaxonVersionKey")) {
                this.column = cm.getColumnNumber();
                foundCol = true;
            }
        }
        
        if (!foundCol) {
            throw new MappingException("Could not find necessary columns again for step: " + this.getClass().getName() + " - " + getName());
        }
    }    
}