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
    private int column = -1;
    private int outColumn = -1;
    private Pattern pattern = Pattern.compile(matchString, Pattern.CASE_INSENSITIVE);
    private Map<String, String> lookup;
    
    public TaxonName() {
        super(ConverterStep.ADD_COLUMN);
        this.lookup = new HashMap<String, String>();
    }
    
    @Override
    public String getName() {
        return "Find Taxon Version Key from a valid well formed Taxon Name";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        boolean found = false;
        
        for (ColumnMapping cm : columns) {
            // If we find a taxon_id column we dont have to do anything in this 
            // case, otherwise we need to transform the name (scientific or 
            // otherwise) into a taxon version key
            if (cm.getField() == DarwinCoreField.TAXONID) {
                return false;
            } else if (cm.getField() == DarwinCoreField.TAXONNAME 
                    || pattern.matcher(cm.getColumnLabel()).matches()) {
                this.column = cm.getColumnNumber();
                found = true;
            }
        }
        
        return found;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            outColumn = cm.getColumnNumber() > outColumn ? cm.getColumnNumber() : outColumn;
        }
        
        outColumn++;
        
        ColumnMapping col = new ColumnMapping(outColumn, "TaxonVersionKey", DarwinCoreField.TAXONID);
        columns.add(col);
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
                q.setParameter("recordedName", origVal);
                
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
                        lookup.put(origVal, pTVK);
                    }
                } else {    
                    lookup.put(origVal, origVal);
                    throw new BadDataException("Could not find a valid tansformation as a valid scientific name, common name or other recording entitiy - " + origVal);
                }
            }
        }
        
        row.add(lookup.get(origVal));
    }
}