/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.metadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.org.nbn.nbnv.importer.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

public class MetadataWriter {
    private File metadata;
    
    public MetadataWriter(File metadata) {
        this.metadata = metadata;
    }
    
    public String datasetToEML(Metadata ds, Organisation org, Date startDate, Date endDate, boolean isUpdate) throws Exception {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(metadata), "UTF8"));

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element root = doc.createElement("eml:eml");
        root.setAttribute("xsi:schemaLocation", "eml://ecoinformatics.org/eml-2.1.1 eml.xsd");
        root.setAttribute("xmlns", "eml://ecoinformatics.org/eml-2.1.1");
        root.setAttribute("xmlns:eml", "eml://ecoinformatics.org/eml-2.1.1");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xmlns:dc", "http://purl.org/dc/terms/");
        root.setAttribute("xml:lang", "en");
        root.setAttribute("packageId", "NBN//eml-1.xml");
        root.setAttribute("system", "GBIF");
        root.setAttribute("scope", "system");
        doc.appendChild(root);

        Element dataset = doc.createElement("dataset");
        root.appendChild(dataset);

        Element altID = doc.createElement("alternateIdentifier");
        altID.setTextContent(ds.getDatasetID());
        dataset.appendChild(altID);
        
        Element title = doc.createElement("title");
        title.setAttribute("xml:lang", "en");
        title.setTextContent(ds.getTitle());
        dataset.appendChild(title);

        dataset.appendChild(createCreatorNode(doc, ds));
        dataset.appendChild(createMetadataProviderNode(doc, ds, org));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Element pubDate = doc.createElement("pubDate");
        pubDate.setTextContent(df.format(Calendar.getInstance().getTime()));
        dataset.appendChild(pubDate);

        Element lang = doc.createElement("language");
        lang.setTextContent("en");
        dataset.appendChild(lang);

        dataset.appendChild(createAbstractNode(doc, ds));
        //dataset.appendChild(createKeywordNode(doc));
        dataset.appendChild(createIntRightsNode(doc, ds));
        dataset.appendChild(createCoverageNode(doc, ds, startDate, endDate));
        dataset.appendChild(createPurposeNode(doc, ds));
        dataset.appendChild(createMethodsNode(doc, ds));
        dataset.appendChild(createTemporalNode(doc, ds));
        dataset.appendChild(createInfoNode(doc, ds));
        dataset.appendChild(createPublicAccessResNode(doc, ds));
        dataset.appendChild(createRecorderNameNode(doc, ds));
        dataset.appendChild(createUpdateNode(doc, isUpdate));
        dataset.appendChild(createRecordAttsNode(doc, ds));

        TransformerFactory tfac = TransformerFactory.newInstance();
        Transformer trans = tfac.newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        DOMSource src = new DOMSource(doc);
        trans.transform(src, sr);
        out.write(sw.toString());
        out.flush();
        out.close();
        return sw.toString();
    }

    private Element createPublicAccessResNode(Document doc, Metadata ds) {
        Element ir = doc.createElement("additionalInfo");
        ir.appendChild(formatParaTag(doc, "publicPrecision: " + ds.getGeographicalRes()));
        return ir;
    }
    
    private Element createRecorderNameNode(Document doc, Metadata ds) {
        Element ir = doc.createElement("additionalInfo");
        if (ds.getRecorderNames().equals("null")) {
            ds.setRecorderNames("false");
        }
        ir.appendChild(formatParaTag(doc, "recorderAndDeterminerNamesArePublic: " + ds.getRecorderNames()));
        return ir;
    }    
    
    private Element createRecordAttsNode(Document doc, Metadata ds) {
        Element ir = doc.createElement("additionalInfo");
        if (ds.getRecordAtts().equals("null")) {
            ds.setRecordAtts("false");
        }
        ir.appendChild(formatParaTag(doc, "recordAttributesArePublic: " + ds.getRecordAtts()));
        return ir;
    }      
    
    private Element createUpdateNode(Document doc, boolean isUpdate) {
        Element ir = doc.createElement("additionalInfo");
        ir.appendChild(formatParaTag(doc, "importType: " + (isUpdate ? "append" : "upsert")));
        return ir;
    }

    private Element formatParaTag(Document doc, String text) {
        Element para = doc.createElement("para");
        para.setTextContent(text);
        return para;
    }

    
    private Element createCreatorNode(Document doc, Metadata ds) {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Organisation org = em.find(Organisation.class, ds.getOrganisationID());
        
        Element creator = doc.createElement("creator");
        Element organisation = doc.createElement("organizationName");
        organisation.setTextContent(org.getName());
        creator.appendChild(organisation);
        Element email = doc.createElement("electronicMailAddress");
        email.setTextContent(org.getContactEmail());
        creator.appendChild(email);

        return creator;
    }
    
    private Element createMetadataProviderNode(Document doc, Metadata ds, Organisation org) {
        Element creator = doc.createElement("metadataProvider");

        String forename, surname;
        if (ds.getDatasetAdminName().indexOf(" ") != -1) {
            forename = ds.getDatasetAdminName().substring(0, ds.getDatasetAdminName().lastIndexOf(" "));
            surname = ds.getDatasetAdminName().substring(ds.getDatasetAdminName().lastIndexOf(" ") + 1, ds.getDatasetAdminName().length());
        } else {
            forename = "";
            surname = ds.getDatasetAdminName();
        }
        
        creator.appendChild(createNameNode(doc, forename, surname));

        Element organisation = doc.createElement("organizationName");
        organisation.setTextContent(org.getName()); 
        creator.appendChild(organisation);

        Element email = doc.createElement("electronicMailAddress");
        email.setTextContent(ds.getDatasetAdminEmail());
        creator.appendChild(email);

        Element url = doc.createElement("onlineUrl");
        url.setTextContent(org.getWebsite());
        creator.appendChild(url);
        
        /**
        Element id = doc.createElement("userId");
        id.setTextContent(Integer.toString(ds.getDatasetAdminID()));
        creator.appendChild(id);
        */

        return creator;
    }
    
    private Element createNameNode(Document doc, String forename, String surname) {
        Element name = doc.createElement("individualName");

        Element given = doc.createElement("givenName");
        given.setTextContent(forename);
        name.appendChild(given);

        Element sur = doc.createElement("surName");
        sur.setTextContent(surname);
        name.appendChild(sur);

        return name;
    }

    private Element createAbstractNode(Document doc, Metadata ds) {
        Element abs = doc.createElement("abstract");
        abs.appendChild(formatParaTag(doc, ds.getDescription()));

        return abs;
    }

     private Element createPurposeNode(Document doc, Metadata ds) {
        Element abs = doc.createElement("purpose");
        abs.appendChild(formatParaTag(doc, ds.getPurpose()));

        return abs;
    }

    private Element createIntRightsNode(Document doc, Metadata ds) {
        Element ir = doc.createElement("intellectualRights");
        
        if (ds.getAccess() == null || ds.getAccess().isEmpty()) {
            ds.setAccess("None");
        }
        
        if (ds.getUse() == null || ds.getUse().isEmpty()) {
            ds.setUse("None");
        }
        
        ir.appendChild(formatParaTag(doc, "accessConstraints: " + ds.getAccess() + " useConstraints: " + ds.getUse()));
        
        return ir;
    }

    private Element createCoverageNode(Document doc, Metadata ds, Date startDate, Date endDate) {
        Element coverage = doc.createElement("coverage");
        coverage.appendChild(createGeographicCoverageNode(doc, ds));
        coverage.appendChild(createTemporalCoverageNode(doc, startDate, endDate));

        return coverage;
    }

    private Element createGeographicCoverageNode(Document doc, Metadata ds) {
        Element coverage = doc.createElement("geographicCoverage");
        Element desc = doc.createElement("geographicDescription");
        desc.setTextContent(ds.getGeographic());
        coverage.appendChild(desc);

        Element bound = doc.createElement("boundingCoordinates");
        Element wbound = doc.createElement("westBoundingCoordinate");
        wbound.setTextContent(String.valueOf(-10));
        Element ebound = doc.createElement("eastBoundingCoordinate");
        ebound.setTextContent(String.valueOf(10));
        Element nbound = doc.createElement("northBoundingCoordinate");
        nbound.setTextContent(String.valueOf(75));
        Element sbound = doc.createElement("southBoundingCoordinate");
        sbound.setTextContent(String.valueOf(45));
        bound.appendChild(wbound);
        bound.appendChild(ebound);
        bound.appendChild(nbound);
        bound.appendChild(sbound);
        coverage.appendChild(bound);

        return coverage;
    }

    private Element createTemporalCoverageNode(Document doc, Date startDate, Date endDate) {        
        Element tc = doc.createElement("temporalCoverage");
        Element rod = doc.createElement("rangeOfDates");
        Element begin = doc.createElement("beginDate");
        begin.appendChild(formatCalendarDate(doc, startDate));
        rod.appendChild(begin);
        Element end = doc.createElement("endDate");
        end.appendChild(formatCalendarDate(doc, endDate));
        rod.appendChild(end);
        tc.appendChild(rod);
        return tc;
    }

    private Element formatCalendarDate(Document doc, Date date) {
        Element cd = doc.createElement("calendarDate");
        DateFormat dayf = new SimpleDateFormat("yyyy-MM-dd");
        cd.setTextContent(dayf.format(date));
        return cd;
    }

    /*private static Element createTaxonomicCoverageNode(Document doc, Dataset ds, RecordSet rs) {
        Element coverage = doc.createElement("taxonomicCoverage");


        Iterator<String> sIt = rs.getSpecies().iterator();

        while (sIt.hasNext()) {
            Element tclass = doc.createElement("taxonomicClassification");
            Element trv = doc.createElement("taxonRankValue");
            trv.setTextContent(sIt.next());
            tclass.appendChild(trv);
            coverage.appendChild(tclass);
        }

        return coverage;
    }
    */

    private Element createMethodsNode(Document doc, Metadata ds) {
        Element methods = doc.createElement("methods");
        Element methodStep = doc.createElement("methodStep");
        Element methodDesc = doc.createElement("description");
        methodDesc.appendChild(formatParaTag(doc, ds.getMethods()));
        methodStep.appendChild(methodDesc);
        methods.appendChild(methodStep);
        Element qcStep = doc.createElement("qualityControl");
        Element qcDesc = doc.createElement("description");
        qcDesc.appendChild(formatParaTag(doc, ds.getQuality()));
        qcStep.appendChild(qcDesc);
        methods.appendChild(qcStep);

        return methods;
    }

    private Element createTemporalNode(Document doc, Metadata ds) {
        Element ir = doc.createElement("additionalInfo");
        ir.appendChild(formatParaTag(doc, "temporalCoverage: " + ds.getTemporal()));
        return ir;
    }

    private Element createInfoNode(Document doc, Metadata ds) {
        Element ir = doc.createElement("additionalInfo");
        ir.appendChild(formatParaTag(doc, "additionalInformation: " + ds.getInfo()));
        return ir;
    }
}

