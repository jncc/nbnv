package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.nbn.data.portal.exceptions.ForbiddenException;
import uk.org.nbn.nbnv.api.model.ImportCleanup;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.SET_SENSITIVE_FALSE;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.SET_SENSITIVE_TRUE;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.STRIP_INVALID_RECORDS;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetAdditions;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithImportStatus;

@Controller
public class DatasetImporterController {
    @Autowired WebResource resource;
    
    @RequestMapping(value    = "/Import/Replace", 
                    consumes = "multipart/form-data",
                    method   = RequestMethod.POST)
    public ModelAndView replaceDataset(HttpServletRequest request, HttpServletResponse response) {
        return handleDatasetUpload("importReplacementDataset", request, response);
    }    
    
    @RequestMapping(value="/Import/New", method=RequestMethod.GET)
    public ModelAndView getNewDatasetForm(@RequestParam("datasetKey") String datasetKey) {
        return new ModelAndView("importNewDataset", "datasetKey", datasetKey);
    }
    
    @RequestMapping(value    = "/Import/New", 
                    consumes = "multipart/form-data",
                    method   = RequestMethod.POST)
    public ModelAndView newDataset(HttpServletRequest request, HttpServletResponse response) {
        return handleDatasetUpload("importNewDataset", request, response);
    }
    
    @RequestMapping(value="/Import/Replace", method = RequestMethod.GET)
    public ModelAndView getImportExistingPage() {
        return new ModelAndView("importReplacementDataset");
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.GET)
    public ModelAndView getImporterDashboard() {
        GenericType<List<TaxonDatasetWithImportStatus>> type = new GenericType<List<TaxonDatasetWithImportStatus>>() {};
        Map<String,Object> data = new HashMap<>();
        data.put("statuses", resource.path("/taxonDatasets/adminable/import").get(type));
        return new ModelAndView("importDashboard", data);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=unqueue")
    public ModelAndView deleteQueuedDataset(@RequestParam("datasetKey") String datasetKey) {
        ClientResponse response = resource
                .path(String.format("taxonDatasets/%s/import", datasetKey))
                .delete(ClientResponse.class);
        
        ModelAndView model = getImporterDashboard();
        switch(response.getStatus()) {
            case 204: return model.addObject("message", "Dataset: " + datasetKey + " has been removed from the queue");
            case 401: throw new UniformInterfaceException(response);
            case 404: return model.addObject("error", "Could not find " + datasetKey);
            default:  return model.addObject("error", "The NBN Gateway API failed. If this continues please contact us using the forums");
        }
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=remove")
    public ModelAndView removeImporterResult(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        ClientResponse response = resource
                .path(String.format("taxonDatasets/%s/import/%s", datasetKey, timestamp))
                .delete(ClientResponse.class);
        
        ModelAndView model = getImporterDashboard();
        switch(response.getStatus()) {
            case 200: return model.addObject("message", "Removed dataset importer result for dataset: " + datasetKey);
            case 401: throw new UniformInterfaceException(response);
            case 404: return model.addObject("error", "Could not find an existing import for " + datasetKey + " with the timestamp " + timestamp);
            default:  
                String status = (String)response.getEntity(Map.class).get("status");
                return model.addObject("error", "The NBN Gateway API failed. If this continues please contact us using the forums. " + status);
        }
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=importValid")
    public ModelAndView continueWithValidRecords(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        return performImportCleanup(datasetKey, timestamp, STRIP_INVALID_RECORDS);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=sensitiveTrue")
    public ModelAndView continueWithSensitiveSetToTrue(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        return performImportCleanup(datasetKey, timestamp, SET_SENSITIVE_TRUE);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=sensitiveFalse")
    public ModelAndView continueWithSensitiveSetToFalse(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        return performImportCleanup(datasetKey, timestamp, SET_SENSITIVE_FALSE);
    }
    
    @RequestMapping(value="/Import/NewMetadata", method = RequestMethod.GET)
    public ModelAndView getNewMetadatForm(){
        List<Organisation> organisations = getAdminableOrganisations();
        if(organisations.isEmpty()) {
            throw new ForbiddenException();
        }
        return new ModelAndView("importNewMetadata", "organisations", organisations);
    }
    
    @RequestMapping(value    = "/Import/NewMetadata", 
                    consumes = "multipart/form-data",
                    method   = RequestMethod.POST)
    public ModelAndView uploadMetadata(HttpServletRequest request, HttpServletResponse response){
        ServletFileUpload upload = new ServletFileUpload();
        Map<String, Object> data = new HashMap<>();
        try {
            FileItemIterator iter = upload.getItemIterator(request);
            
            TaxonDatasetAdditions additions = new TaxonDatasetAdditions();
            String organisationId = readNewDatasetOrganisation(iter, additions);

            InputStream wordDocumentInputStream = iter.next().openStream();

            WebResource importer = resource.path(String.format("organisations/%s/datasets", organisationId));
            
            ClientResponse restResponse = importer
                    .type("application/zip")
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, zip(additions, wordDocumentInputStream));
            
            switch(restResponse.getStatus()) {
                case 200:
                    // We have successfulyl created a new dataset redirect to 
                    // next page so that the user can upload a NBN Exchange 
                    // Format dataset
                    String key = restResponse.getEntity(TaxonDataset.class).getKey();
                    response.sendRedirect("/Import/New?datasetKey=" + key);
                    return null;
                case 401: throw new UniformInterfaceException(restResponse);
                default:
                    //Get the error message from the response
                    data.put("status", restResponse.getEntity(Map.class).get("status"));
            }
        } catch (IOException ex) {
            data.put("status", "Failed to communicate with the NBN Rest API");
        } catch (FileUploadException ex) {
            data.put("status", "There was an issue whilst uploading " + ex.getMessage());
        }
        return getNewMetadatForm().addAllObjects(data); //Base the result off of the original form
    }
    
    //Return the list of organisations that the current user is an administrator
    //of.
    private List<Organisation> getAdminableOrganisations() {
        GenericType<List<Organisation>> organisationsType = new GenericType<List<Organisation>>(){};
        return resource.path("/user/adminOrganisations").get(organisationsType);
    }
    
    private ModelAndView handleDatasetUpload(String view, HttpServletRequest request, HttpServletResponse response) {
        ServletFileUpload upload = new ServletFileUpload();
        Map<String, Object> data = new HashMap<>();
        try {
            FileItemIterator iter = upload.getItemIterator(request);

            String datasetKey = getFormField("datasetKey", iter);
            data.put("datasetKey", datasetKey);
            WebResource importer = resource.path(String.format("taxonDatasets/%s/import", datasetKey));
            
            InputStream nbnExchangeStream = iter.next().openStream();
            ClientResponse restResponse = importer.put(ClientResponse.class, nbnExchangeStream);
            
            switch(restResponse.getStatus()) {
                case 200: 
                    response.sendRedirect("/Import"); //Great stuff!! Jump back to the dashboard
                    return null;
                case 401: throw new UniformInterfaceException(restResponse);
                default:
                    //Get the error message from the response
                    data.put("status", restResponse.getEntity(Map.class).get("status"));
            }
        } catch (IOException ex) {
            data.put("status", "Failed to communicate with the NBN Rest API");
        } catch (FileUploadException ex) {
            data.put("status", "There was an issue whilst uploading " + ex.getMessage());
        }
        return new ModelAndView(view, data);
    }
    
    // Wrap the TaxonDatasetAdditions and the input stream of a word document
    // and produce an input stream which represents a zip with both contained
    // inside
    private InputStream zip(final TaxonDatasetAdditions additions, final InputStream wordDocument) throws IOException {
        final PipedOutputStream src = new PipedOutputStream();
        PipedInputStream toReturn = new PipedInputStream(src);
        
        new Thread() {
            @Override
            public void run() {
                try (ZipOutputStream out = new ZipOutputStream(src)) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
                    out.putNextEntry(new ZipEntry("form.doc"));
                    IOUtils.copyLarge(wordDocument, out);
                    out.putNextEntry(new ZipEntry("additions.json"));
                    mapper.writeValue(out, additions);
                    out.closeEntry();
                }
                catch(IOException io) {}
            }
        }.start();
        return toReturn;
    }
    
    private ModelAndView performImportCleanup(String datasetKey, String timestamp, ImportCleanup.Operation operation) {
        ClientResponse response = resource
                .path(String.format("taxonDatasets/%s/import/%s", datasetKey, timestamp))
                .post(ClientResponse.class, new ImportCleanup(operation));
        
        ModelAndView model = getImporterDashboard();
        switch(response.getStatus()) {
            case 200: return model.addObject("message", "Dataset: " + datasetKey + " has been queued for processing");
            case 401: throw new UniformInterfaceException(response);
            case 404: return model.addObject("error", "Could not find an existing import for " + datasetKey + " with the timestamp " + timestamp);
            case 409: return model.addObject("error", "The dataset " + datasetKey + " is already queued for import. Please delete and try again.");
            default:  
                String status = (String)response.getEntity(Map.class).get("status");
                return model.addObject("error", "The NBN Gateway API failed. If this continues please contact us using the forums. " + status);
        }
    }
    
    private String getFormField(String name, FileItemIterator iterator) throws FileUploadException, IOException {
        FileItemStream field = iterator.next();
        if(field.getFieldName().equals(name)) {
            return readFormField(field);
        }
        else {
            throw new IllegalArgumentException("Expected to find the form field: " + name);
        }
    }
    
    private String getFormFieldOrEmptyString(String name, FileItemIterator iterator) throws FileUploadException, IOException {
        FileItemStream field = iterator.next();
        if(field.isFormField() && field.getFieldName().equals(name)) {
            return Streams.asString(field.openStream());
        }
        else {
            return "";
        }
    }
    
    private String readFormField(FileItemStream field) throws IOException {
        if(field.isFormField()) {
            return Streams.asString(field.openStream());
        }
        else {
            throw new IllegalArgumentException("Expected to get a form field");
        }
    }
    
    // On the ImportNewMetadata form, read in all the TaxonDatasetAdditions 
    // values and populate the provided TaxonDatasetAdditions object. Do this
    // until we read the 'Organisation' field, then return this as a value.
    // The File Upload should be present after this
    private String readNewDatasetOrganisation(FileItemIterator iter, TaxonDatasetAdditions additions) throws FileUploadException, IOException {
        while (iter.hasNext()) {
            FileItemStream field = iter.next();
            String value = readFormField(field);
            switch (field.getFieldName()) {
                case "resolution":
                    additions.setResolution(value);                    
                break;
                case "recordAtts":
                    additions.setRecordAttributes(Boolean.parseBoolean(value));
                break;
                case "recorderNames":
                    additions.setRecorderNames(Boolean.parseBoolean(value));
                break;
                case "organisation": return value;
            }
        }
        throw new IllegalArgumentException("Reached the end of the iterator before reading an organisation");
    }
    
    // Handle any calls to the api which result in a 401 unauthorized exception
    // We simply redirect to the sign in page. This should really be in a 
    // controlleradvice class, however the version of spring we are using in 
    // this project is too old
    @ExceptionHandler(UniformInterfaceException.class)
    public void handleUnauthorized(UniformInterfaceException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(exception.getResponse().getStatus() == 401) {
            response.sendRedirect("/User/SSO/Unauthorized?redirect=" + URLEncoder.encode(request.getRequestURL().toString()));
        }
        else {
            throw exception;
        }
    }
}
