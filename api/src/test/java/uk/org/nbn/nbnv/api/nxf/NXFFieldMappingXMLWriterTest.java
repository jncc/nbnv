package uk.org.nbn.nbnv.api.nxf;

import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author cjohn
 */
public class NXFFieldMappingXMLWriterTest {
    @Test
    public void checkThatCanMapNXFHeading() throws IOException, TemplateException {
        //Given
        URL url = getClass().getResource("/test-data/expected-mapping-meta.xml");
        String metaXml = FileUtils.readFileToString(new File(url.getFile()));
        NXFLine header = new NXFLine(
            "RECORDKEY\tSURVEYKEY\tSTARTDATE\tENDDATE\tDATETYPE\tSENSITIVE\t" +
            "TAXONVERSIONKEY\tGRIDREFERENCE\tPRECISION\tPROJECTION\tSITENAME\t" +
            "RECORDER\tDETERMINER\tABUNDANCE\tSITEKEY\tDYNAMICPROPERTIES");
        
        StringWriter writer = new StringWriter();
        NXFFieldMappingXMLWriter xmlWriter = new NXFFieldMappingXMLWriter(writer);
        
        //When
        xmlWriter.write(header);
        
        //Then
        assertEquals("Expected meta.xml to match", writer.toString(), metaXml);
    }
}
