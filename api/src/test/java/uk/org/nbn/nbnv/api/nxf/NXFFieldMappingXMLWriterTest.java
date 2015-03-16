package uk.org.nbn.nbnv.api.nxf;

import uk.org.nbn.nbnv.api.nxf.NXFFieldMappingXMLWriter;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import uk.org.nbn.nbnv.api.nxf.NXFReader.NXFLine;

/**
 *
 * @author cjohn
 */
public class NXFFieldMappingXMLWriterTest {
    @Test
    public void checkThatCanMapNXFHeading() throws IOException, TemplateException {
        //Given
        URL url = getClass().getResource("/test-data/GA000466-meta.xml");
        String metaXml = FileUtils.readFileToString(new File(url.getFile()));
        NXFLine header = new NXFLine(
            "RecordKey\tSurveykey\tStartDate\tEndDate\tDateType\tSensitive\t" +
            "TaxonVersionKey\tGridReference\tPrecision\tProjection\tSiteName\t" +
            "Recorder\tDeterminer\tAbundance\tSiteKey\tDynamicProperties");
        
        StringWriter writer = new StringWriter();
        NXFFieldMappingXMLWriter xmlWriter = new NXFFieldMappingXMLWriter(writer);
        
        //When
        xmlWriter.write(header);
        
        //Then
        assertEquals("Expected meta.xml to match", writer.toString(), metaXml);
    }
}
