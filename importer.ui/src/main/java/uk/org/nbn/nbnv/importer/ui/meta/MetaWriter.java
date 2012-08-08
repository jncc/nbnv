/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.meta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreFieldType;

/**
 *
 * @author Paul Gilbertson
 */
public class MetaWriter {

    public List<String> createMetaFile(List<ColumnMapping> mapping, File metaFile) throws IOException {
        List<String> errors = new ArrayList<String>();
        BufferedWriter w = null;

        try {
            w = new BufferedWriter(new FileWriter(metaFile));
            w.append(createMeta(mapping));
        } catch (IOException ex) {
            errors.add(ex.getMessage());
        } finally {
            if (w != null) {
                w.close();
            }
        }
        
        return errors;
    }

    public String createMeta(List<ColumnMapping> columns) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("archive");
            root.setAttribute("xmlns", "http://rs.tdwg.org/dwc/text/");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://rs.tdwg.org/dwc/text/ http://rs.tdwg.org/dwc/text/tdwg_dwc_text.xsd");
            doc.appendChild(root);
            Element core = createCore(doc, root);
            Element extension = createExtension(doc, root);

            addCoreFields(doc, core, columns);
            addExtensionFields(doc, extension, columns);

            return convertToString(doc);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(MetaWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MetaWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MetaWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void addCoreFields(Document doc, Element core, List<ColumnMapping> mapping) {
        for (ColumnMapping cm : mapping) {
            if (cm.getField().getType() == DarwinCoreFieldType.DWCA_OCCURRENCE) {
                Element field = doc.createElement("field");
                field.setAttribute("index", Integer.toString(cm.getColumnNumber()));
                field.setAttribute("term", cm.getField().toString());
                core.appendChild(field);
            }
        }
    }

    private void addExtensionFields(Document doc, Element extension, List<ColumnMapping> mapping) {
        for (ColumnMapping cm : mapping) {
            if (cm.getField().getType() == DarwinCoreFieldType.NBNEXCHANGE) {
                Element field = doc.createElement("field");
                field.setAttribute("index", Integer.toString(cm.getColumnNumber()));
                field.setAttribute("term", cm.getField().toString());
                extension.appendChild(field);
            }
        }
    }

    private Element createCore(Document doc, Element root) throws DOMException {
        Element core = doc.createElement("core");
        core.setAttribute("encoding", "UTF-8");
        core.setAttribute("fieldsTerminatedBy", "\\t");
        core.setAttribute("linesTerminatedBy", "\\r\\n");
        core.setAttribute("fieldsEnclosedBy", "");
        core.setAttribute("ignoreHeaderLines", "1");
        core.setAttribute("rowType", DarwinCoreFieldType.DWCA_OCCURRENCE.getRowType());
        root.appendChild(core);
        Element files = doc.createElement("files");
        Element location = doc.createElement("location");
        location.setTextContent("data.tab");
        files.appendChild(location);
        core.appendChild(files);
        Element id = doc.createElement("id");
        id.setAttribute("index", "0");
        core.appendChild(id);
        return core;
    }

    private Element createExtension(Document doc, Element root) throws DOMException {
        Element core = doc.createElement("extension");
        core.setAttribute("encoding", "UTF-8");
        core.setAttribute("fieldsTerminatedBy", "\\t");
        core.setAttribute("linesTerminatedBy", "\\r\\n");
        core.setAttribute("fieldsEnclosedBy", "");
        core.setAttribute("ignoreHeaderLines", "1");
        core.setAttribute("rowType", DarwinCoreFieldType.NBNEXCHANGE.getRowType());
        root.appendChild(core);
        Element files = doc.createElement("files");
        Element location = doc.createElement("location");
        location.setTextContent("data.tab");
        files.appendChild(location);
        core.appendChild(files);
        Element id = doc.createElement("coreid");
        id.setAttribute("index", "0");
        core.appendChild(id);
        return core;
    }

    private String convertToString(Document doc) throws TransformerFactoryConfigurationError, TransformerConfigurationException, IllegalArgumentException, TransformerException {
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        return sw.toString();
    }
}
