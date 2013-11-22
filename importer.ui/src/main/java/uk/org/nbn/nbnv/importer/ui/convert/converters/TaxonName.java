package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.RecordingEntity;

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
            } else if (cm.getField() == DarwinCoreField.TAXONNAME) {
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
            Query q =  em.createNamedQuery("RecordingEntity.findByRecordedName");
            q.setParameter("name", origVal);


            List<RecordingEntity> res = q.getResultList();

            if (res.isEmpty()) {
                throw new BadDataException("No matching names found for: " + origVal);
            }
            
            if (res.size() > 1) {
                throw new BadDataException("Possible Ambiguous or Dangerous name found for: " + origVal);
            }
            
            if (res.size() == 1) {
                if (res.get(0).getDangerousName().equals("A")) {
                    throw new BadDataException("Ambiguous name found for: " + origVal);
                }
                if (res.get(0).getDangerousName().equals("D")) {
                    throw new BadDataException("Dangerous name found for: " + origVal);
                }
                if (res.get(0).getDangerousName().equals("M")) {
                    lookup.put(origVal, res.get(0).getTaxon().getTaxonVersionKey());
                } else {
                    throw new BadDataException("Name '" + origVal + "' tagged with unkown nameType of " + res.get(0).getDangerousName());
                }
            }
        }
        
        row.add(lookup.get(origVal));
    }
}