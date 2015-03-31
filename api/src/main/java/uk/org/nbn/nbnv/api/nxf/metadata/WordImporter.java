package uk.org.nbn.nbnv.api.nxf.metadata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author Matt Debont
 */
public interface WordImporter {
    
    // Default Organisation Descriptors
    public static final String ORG_ABBREVIATION = "Abbreviation";
    public static final String ORG_EMAIL = "Contact email";
    public static final String ORG_CONTACT_NAME = "Contact name";
    public static final String ORG_NAME = "Data Provider Name";
    public static final String ORG_LOGO = "Organisation Logo";
    public static final String ORG_DESC = "Organisation Description";
    public static final String ORG_ADDRESS = "Postal Address";
    public static final String ORG_POSTCODE = "Postcode";
    public static final String ORG_PHONE = "Telephone* Number";
    public static final String ORG_WEBSITE = "Website";
    
    // Default Metadata Descriptors
    public static final String META_ACCESS_CONSTRAINT = "Access Constraint";
    public static final String META_ADDITIONAL_INFO = "Additional Information";
    public static final String META_DATA_CONFIDENCE = "Confidence in the Data";
    public static final String META_DESC = "Description";
    public static final String META_EMAIL = "Email Address";
    public static final String META_GEOCOVER = "Geographical Coverage";
    public static final String META_CAPTURE_METHOD = "Methods of Data Capture";
    public static final String META_NAME = "Name";
    public static final String META_PURPOSE = "Purpose of Data Capture";
    public static final String META_RECORD_ATT = "Record Attributes";
    public static final String META_RECORDERS = "Recorder Names";
    public static final String META_SET_GEORES = "Set Geographic Resolution";
    public static final String META_TITLE = "Title";
    public static final String META_CONTACT_PHONE = "Telephone Number";
    public static final String META_TEMPORAL = "Temporal Coverage";
    public static final String META_USE_CONSTRAINT = "Use Constraint";  
    
    // Default descriptors that we are searching for 
    public static final String[] stringsHWPF = {
        ORG_ABBREVIATION, ORG_ADDRESS, ORG_CONTACT_NAME, ORG_DESC, 
        ORG_EMAIL, ORG_LOGO, ORG_NAME, ORG_PHONE, ORG_POSTCODE, 
        ORG_WEBSITE, META_ACCESS_CONSTRAINT, META_ADDITIONAL_INFO, 
        META_CAPTURE_METHOD, META_CONTACT_PHONE, META_DATA_CONFIDENCE,
        META_DESC, META_EMAIL, META_GEOCOVER, META_NAME, META_PURPOSE,
        META_RECORDERS, META_RECORD_ATT, META_SET_GEORES, META_TEMPORAL,
        META_TITLE, META_USE_CONSTRAINT
    };  
    
    /**
     * An array of the mandatory metadata elements that should be found when
     * a Word metadata document is parsed
     */
    public static final String[] WORD_METADATA_MANDATORY_ELEMENTS = {
        ORG_NAME, ORG_DESC, ORG_CONTACT_NAME, ORG_EMAIL, ORG_ADDRESS, ORG_POSTCODE,
        ORG_PHONE, META_NAME, META_TITLE, META_DESC, META_CAPTURE_METHOD, META_PURPOSE, 
        META_DATA_CONFIDENCE, META_ACCESS_CONSTRAINT, META_CONTACT_PHONE, META_EMAIL
    };
    public static final HashSet<String> VALIDATION_KEYS = new HashSet<>(Arrays.asList(WORD_METADATA_MANDATORY_ELEMENTS));
    
    // Default Input form elements for Word doc
    public static final String INPUT_TEXT = "FORMTEXT";
    public static final String INPUT_CHECKBOX = "FORMCHECKBOX";    
    
    public Map<String, String> parseDocument(List<String> strList, ListIterator<String> strIt, Map<String, String> mappings, List<String> errors);
    public boolean supports(int major, int minor);
    public List<String> getDefaultMessages();
}
