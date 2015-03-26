package uk.org.nbn.nbnv.api.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.reflections.Reflections;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.nxf.metadata.WordImporter;

public class TaxonDatasetMetadataImportService {
    private final Set<Class<? extends WordImporter>> importers;
    private List<String> messages = new ArrayList<>();
    

    public TaxonDatasetMetadataImportService() {
        Reflections reflections = new Reflections("uk.org.nbn.nbnv.api.nxf.metadata");
        importers = reflections.getSubTypesOf(WordImporter.class);
    }
    
    public TaxonDataset getTaxonDataset(InputStream input) throws IOException, InstantiationException, IllegalAccessException{
        HWPFDocument doc = new HWPFDocument(input);
        WordExtractor ext = new WordExtractor(doc);
        List<String> paragraphs = Arrays.asList(ext.getParagraphText());
        ListIterator<String> paragraphsIt = paragraphs.listIterator();
        WordImporter importer = getImporter(paragraphsIt);
        return getTaxonDataset(importer.parseDocument(paragraphs, paragraphsIt, new HashMap<String,String>(), messages));
    }
    
    TaxonDataset getTaxonDataset(Map<String,String> metadata){
        TaxonDataset toReturn = new TaxonDataset();
        //TODO create TaxonDataset from metadata
        
        //No solution has been found to extract checkboxes from a Word document
        //into Java.  Poi is the library used to work with Word documents in Java
        //and it does not have an implementation for this.  Therefore, the best
        //that can be done is to set the most conservative defaults for the public's
        //view of resolution, attributes and recorder
        toReturn.setPublicResolution("10000");
        toReturn.setPublicAttribute(false);
        toReturn.setPublicRecorder(false);
        
        toReturn.setTitle(metadata.get(WordImporter.META_TITLE));
        toReturn.setDescription(metadata.get(WordImporter.META_DESC));
        toReturn.setCaptureMethod(metadata.get(WordImporter.META_CAPTURE_METHOD));
        toReturn.setPurpose(metadata.get(WordImporter.META_PURPOSE));
        toReturn.setGeographicalCoverage(metadata.get(WordImporter.META_GEOCOVER));
        toReturn.setTemporalCoverage(metadata.get(WordImporter.META_TEMPORAL));
        toReturn.setQuality(metadata.get(WordImporter.META_DATA_CONFIDENCE));
        toReturn.setAdditionalInformation(metadata.get(WordImporter.META_ADDITIONAL_INFO));
        toReturn.setAccessConstraints(metadata.get(WordImporter.META_ACCESS_CONSTRAINT));
        toReturn.setUseConstraints(metadata.get(WordImporter.META_USE_CONSTRAINT));
        
        return toReturn;
    }
    
    public List<String> getMessages(){
        return messages;
    }
    
    WordImporter getImporter(ListIterator<String> paragraphsIt) throws InstantiationException, IllegalAccessException{
        Pattern pat = Pattern.compile("([0-9]+)(\\.([0-9]+))?");
        while(paragraphsIt.hasNext()){
            String paragraph = paragraphsIt.next();
            if(paragraph.startsWith("Version ")){
                Matcher matcher = pat.matcher(paragraph);
                if(matcher.find()){
                    int major;
                    int minor;
                    try {
                        major = Integer.parseInt(matcher.group(1));
                        minor = Integer.parseInt(matcher.group(3));
                        return getImporterFromVersion(major, minor);
                    } catch(NumberFormatException ex){
                        messages.add("Could not find a properly formated document version number (eg Version 3.2), this is what was found: " + paragraph);
                        return null;
                    }
                } else {
                    messages.add("Could not find a properly formated document version number (eg Version 3.2), this is what was found: " + paragraph);
                    return null;
                }
            }
        }
        messages.add("Could not find a version number in this document");
        return null;
    }
    
    WordImporter getImporterFromVersion(int major, int minor) throws InstantiationException, IllegalAccessException{
        for (Class<? extends WordImporter> newImporter : importers) {
            WordImporter instance = newImporter.newInstance();
            if (instance.supports(major, minor)) {
                return instance;
            }
        }
        messages.add(String.format("The version number found in this document (%s.%s) is not supported.", major, minor));
        return null;
    }
    
    static String normalizeAndTrim(String input) {
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFKD).replaceAll("âââââ", "").replaceAll("     ", "");
    }
}
