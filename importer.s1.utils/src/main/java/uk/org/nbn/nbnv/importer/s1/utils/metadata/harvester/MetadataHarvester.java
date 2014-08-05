/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.metadata.harvester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.reflections.Reflections;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import uk.org.nbn.nbnv.importer.s1.utils.errors.NotHarvestedError;
import uk.org.nbn.nbnv.importer.s1.utils.errors.POIImportError;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.tools.TextTools;
import uk.org.nbn.nbnv.importer.s1.utils.wordImporter.WordImporter;

/**
 *
 * @author Matt Debont
 */
public class MetadataHarvester {

    private Map<String, String> mappings;
    private Metadata metadata;
    private WordImporter importer;

    public List<String> harvest(CommonsMultipartFile input, String datasetID) throws IOException, POIImportError {
        List<String> messages = new ArrayList<String>();

        HWPFDocument doc = new HWPFDocument(input.getInputStream());
        WordExtractor ext = new WordExtractor(doc);
        String[] strs = ext.getParagraphText();

        // Catch some of the annoying cases where inputs are followed by
        // paragraphs of text, as we are using \r\n to seperate out the 
        // inputs

        List<String> strList = Arrays.asList(strs);
        ListIterator<String> strIt = strList.listIterator();

        // Check the version number of the document to make sure we 
        // can read it
        Float version = null;

        while (strIt.hasNext() && version == null) {
            try {
                String str = strIt.next().trim();
                if (str.startsWith("Version ")) {
                    Pattern pat;
                    pat = Pattern.compile("([0-9]+)(\\.([0-9]+))?");
                    Matcher matcher = pat.matcher(str);
                    if (matcher.find()) {
                        int major = Integer.parseInt(matcher.group(1));
                        int minor = 0;
                        try {
                            minor = Integer.parseInt(matcher.group(3));
                        } catch (NumberFormatException ex) {
                            // No number available or an unknown number
                        }

                        version = Float.parseFloat(Integer.toString(major) + "." + Integer.toString(minor));

                        importer = getDocumentImporter(major, minor);

                        if (importer == null) {
                            if (minor > 0) {
                                messages.add("Version " + version + " of Metadata form not supported");
                                minor = 0;
                                importer = getDocumentImporter(major, minor);
                            }
                            if (importer == null) {
                                throw new POIImportError("Unable to read Metadata form, version " + version + " of form not supported");
                            }
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                throw new POIImportError("Unable to read Metadata form, version number of form not found");
            }
        }

        if (version == null && importer == null) {
            throw new POIImportError("Unable to read Metadata form, version number of form not found");
        }

        List<String> errors = new ArrayList<String>();

        mappings = importer.parseDocument(strList, strIt, new HashMap<String, String>(), errors);
        messages.addAll(importer.getDefaultMessages());
        messages.addAll(errors);
        metadata = new Metadata();

        metadata.setAccess(TextTools.normalizeAndTrim(mappings.get(importer.META_ACCESS_CONSTRAINT)));
        metadata.setDescription(TextTools.normalizeAndTrim(mappings.get(importer.META_DESC)));
        metadata.setGeographic(TextTools.normalizeAndTrim(mappings.get(importer.META_GEOCOVER)));
        metadata.setInfo(TextTools.normalizeAndTrim(mappings.get(importer.META_ADDITIONAL_INFO)));
        metadata.setMethods(TextTools.normalizeAndTrim(mappings.get(importer.META_CAPTURE_METHOD)));
        metadata.setPurpose(TextTools.normalizeAndTrim(mappings.get(importer.META_PURPOSE)));
        metadata.setQuality(TextTools.normalizeAndTrim(mappings.get(importer.META_DATA_CONFIDENCE)));
        metadata.setTemporal(TextTools.normalizeAndTrim(mappings.get(importer.META_TEMPORAL)));
        metadata.setTitle(TextTools.normalizeAndTrim(mappings.get(importer.META_TITLE)));
        metadata.setUse(TextTools.normalizeAndTrim(mappings.get(importer.META_USE_CONSTRAINT)));
        metadata.setDatasetAdminName(TextTools.normalizeAndTrim(mappings.get(importer.META_NAME)));
        metadata.setDatasetAdminPhone(TextTools.normalizeAndTrim(mappings.get(importer.META_CONTACT_PHONE)));
        metadata.setDatasetAdminEmail(TextTools.normalizeAndTrim(mappings.get(importer.META_EMAIL)));
        metadata.setDatasetID(TextTools.normalizeAndTrim(datasetID));

        return messages;
    }

    private WordImporter getDocumentImporter(int major, int minor) {
        Reflections reflections = new Reflections("uk.org.nbn.nbnv.importer.s1.utils.wordImporter");

        Set<Class<? extends WordImporter>> importers = reflections.getSubTypesOf(WordImporter.class);

        for (Class<? extends WordImporter> newImporter : importers) {
            try {
                WordImporter instance = newImporter.newInstance();
                if (instance.supports(major, minor)) {
                    return instance;
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(MetadataHarvester.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(MetadataHarvester.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Map<String, String> getMappings() throws NotHarvestedError {
        if (mappings == null) {
            throw new NotHarvestedError();
        }
        return mappings;
    }

    public Metadata getMetadata() throws NotHarvestedError {
        if (metadata == null) {
            throw new NotHarvestedError();
        }
        return metadata;
    }

    public WordImporter getImporter() throws NotHarvestedError {
        if (importer == null) {
            throw new NotHarvestedError();
        }
        return importer;
    }
}
