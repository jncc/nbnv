package uk.org.nbn.nbnv.api.nxf;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The following class will take an NXF Header line which can be used to 
 * generate a mapping file of the columns to darwin core and NBN extensions.
 * 
 * You will often find the content of this generator in importer archives (meta.xml)
 * @author cjohn
 */
public class NXFFieldMappingXMLWriter {
    private static final Map<String, String> DARWIN_CORE_FIELDS;
    private static final Map<String, String> NBN_EXTENSION_FIELDS;
    
    static {
        DARWIN_CORE_FIELDS = new HashMap<>();
        
        String VERBATIMLATITUDE = "http://rs.tdwg.org/dwc/terms/verbatimLatitude";
        String VERBATIMLONGITUDE = "http://rs.tdwg.org/dwc/terms/verbatimLongitude";

        DARWIN_CORE_FIELDS.put(NXFHeading.RECORDKEY.name(),"http://rs.tdwg.org/dwc/terms/occurrenceID");
        DARWIN_CORE_FIELDS.put(NXFHeading.TAXONVERSIONKEY.name(),"http://rs.tdwg.org/dwc/terms/taxonID");
        DARWIN_CORE_FIELDS.put(NXFHeading.SITENAME.name(),"http://rs.tdwg.org/dwc/terms/locality");
        DARWIN_CORE_FIELDS.put(NXFHeading.NORTH.name(),VERBATIMLATITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.EAST.name(),VERBATIMLONGITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.NORTHINGS.name(),VERBATIMLATITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.EASTINGS.name(),VERBATIMLONGITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.LAT.name(),VERBATIMLATITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.LONG.name(),VERBATIMLONGITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.LATITUDE.name(),VERBATIMLATITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.LONGITUDE.name(),VERBATIMLONGITUDE);
        DARWIN_CORE_FIELDS.put(NXFHeading.SRS.name(),"http://rs.tdwg.org/dwc/terms/verbatimSRS");
        DARWIN_CORE_FIELDS.put(NXFHeading.DATE.name(),"http://rs.tdwg.org/dwc/terms/eventDate");
        DARWIN_CORE_FIELDS.put(NXFHeading.RECORDER.name(),"http://rs.tdwg.org/dwc/terms/recordedBy");
        DARWIN_CORE_FIELDS.put(NXFHeading.DETERMINER.name(),"http://rs.tdwg.org/dwc/terms/identifiedBy");
        DARWIN_CORE_FIELDS.put(NXFHeading.ZEROABUNDANCE.name(),"http://rs.tdwg.org/dwc/terms/occurrenceStatus");
        DARWIN_CORE_FIELDS.put(NXFHeading.SURVEYKEY.name(),"http://rs.tdwg.org/dwc/terms/collectionCode");
        DARWIN_CORE_FIELDS.put(NXFHeading.SAMPLEKEY.name(),"http://rs.tdwg.org/dwc/terms/eventID");
        DARWIN_CORE_FIELDS.put(NXFHeading.DYNAMICPROPERTIES.name(),"http://rs.tdwg.org/dwc/terms/dynamicProperties");

        NBN_EXTENSION_FIELDS = new HashMap<>();
        NBN_EXTENSION_FIELDS.put(NXFHeading.PROJECTION.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType");
        NBN_EXTENSION_FIELDS.put(NXFHeading.SENSITIVE.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence");
        NBN_EXTENSION_FIELDS.put(NXFHeading.GRIDREFERENCE.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference");
        NBN_EXTENSION_FIELDS.put(NXFHeading.PRECISION.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecision");
        NBN_EXTENSION_FIELDS.put(NXFHeading.DATETYPE.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode");
        NBN_EXTENSION_FIELDS.put(NXFHeading.STARTDATE.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart");
        NBN_EXTENSION_FIELDS.put(NXFHeading.ENDDATE.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd");
        NBN_EXTENSION_FIELDS.put(NXFHeading.FEATUREKEY.name(),"http://rs.nbn.org.uk/dwc/nxf/0.1/terms/siteFeatureKey");    }
    
    private final Template template;
    private final Writer writer;
    
    public NXFFieldMappingXMLWriter(Writer writer) throws IOException {
        this.writer = writer;
        
        //Create a freemarker configuration for loading in the template
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(NXFFieldMappingXMLWriter.class, "");
        this.template = configuration.getTemplate("meta.xml.ftl");
    }
        
    public void write(NXFLine header) throws IOException, TemplateException {
        List<String> columns = header.getValues();
        Map<String, Object> data = new HashMap<>();
        data.put("darwinCoreFields", createFieldMappings(columns, DARWIN_CORE_FIELDS));
        data.put("nbnExtensionFields", createFieldMappings(columns, NBN_EXTENSION_FIELDS));
        data.put("recordKeyCol", columns.indexOf("RECORDKEY"));
        template.process(data, writer);
    }
    
    private List<FieldMapping> createFieldMappings(List<String> columns, Map<String,String> mappings) {
        List<FieldMapping> toReturn = new ArrayList<>();
        
        for(int i = 0; i<columns.size(); i++) {
            String column = columns.get(i);
            if(mappings.containsKey(column)) {
                toReturn.add(new FieldMapping(mappings.get(column), i));
            }
        }
        return toReturn;
    }
    
    public static class FieldMapping {
        private final String term;
        private final int index;
        
        public FieldMapping(String term, int index) {
            this.term = term;
            this.index = index;
        }
        
        public String getTerm() {
            return term;
        }
        
        public int getIndex() {
            return index;
        }
    }
}
