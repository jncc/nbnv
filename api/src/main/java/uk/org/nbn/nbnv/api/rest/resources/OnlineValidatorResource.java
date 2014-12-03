package uk.org.nbn.nbnv.api.rest.resources;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.validator.ValidatorColumnModel;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DefaultTypes;
import uk.org.nbn.nbnv.importer.s1.utils.errors.POIImportError;
import uk.org.nbn.nbnv.importer.s1.utils.metadata.harvester.MetadataHarvester;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.s1.utils.parser.NXFParser;
import uk.org.nbn.nbnv.importer.s1.utils.wordImporter.WordImporter;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.importer.s1.utils.tools.TextTools;

@Component
@Path("/validator")
public class OnlineValidatorResource extends AbstractResource {

    private Pattern pattern;
    private static final String UPLOAD = "upload.tab";

    @Autowired OrganisationMapper organisationMapper;

    public OnlineValidatorResource() {
        pattern = Pattern.compile("^\\d{8}_\\d{6}_(\\d+)_[A-Za-z0-9]{10}$");
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(DefaultTypes.JSON)
    public Response uploadDataFile(
            @TokenUser(allowPublic = false) User user,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");

            String jobName = formatter.format(new Date()) + "_"
                    + user.getId() + "_" + RandomStringUtils.randomAlphanumeric(10);

            File tmpDir = new File(properties.getProperty("validatorRoot") + File.separator + "tmp" + File.separator + jobName);
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }
            File upload = new File(tmpDir.getAbsoluteFile() + File.separator + UPLOAD);

            OutputStream output = new FileOutputStream(upload);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = fileInputStream.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            output.flush();
            output.close();

            NXFParser parser = new NXFParser(upload);

            ValidatorColumnModel model = new ValidatorColumnModel(
                    jobName,
                    parser.parseHeaders(),
                    Arrays.asList(DarwinCoreField.values()));

            parser.closeFile();

            return Response.ok(model).build();
        } catch (IOException ex) {
            return Response.serverError().entity("There was an error while reading the uploaded file: " + ex.getLocalizedMessage()).build();
        }
    }

    /**
     *
     * @param user
     * @param path
     * @param fileInputStream
     * @param contentDispositionHeader
     * @return
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(DefaultTypes.JSON)
    @Path("/{path}/processMetadata")
    public Response processMetadataForm(
            @TokenUser(allowPublic = false) User user,
            @PathParam("path") String path,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            if (Integer.parseInt(matcher.group(1)) == user.getId()) {
                File tmpDir = new File(properties.getProperty("validatorRoot") + File.separator + "tmp" + File.separator + path);

                if (tmpDir.exists()) {
                    try {
                        if (!tmpDir.exists()) {
                            tmpDir.mkdirs();
                        }
                        File metadata = new File(tmpDir.getAbsoluteFile() + File.separator + "metadata.doc");

                        OutputStream output = new FileOutputStream(metadata);

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = fileInputStream.read(bytes)) != -1) {
                            output.write(bytes, 0, read);
                        }
                        output.flush();
                        output.close();
                        
                        Response resp = Response.ok(harvestMetadata(metadata)).build();

                        metadata.delete();                                
                                
                        return resp;
                    } catch (IOException ex) {
                        return Response.serverError().entity("There was an error while reading the uploaded metadata file: " + ex.getLocalizedMessage()).build();
                    } catch (POIImportError ex) {
                        return Response.serverError().entity("There was an error while reading the uploaded metadata file: " + ex.getLocalizedMessage()).build();
                    }
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity(path + " does not exist").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity(path + " does not belong to your user").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(path + " is not valid").build();
        }
    }

    private Map<String, String> harvestMetadata(File metadataFile) throws IOException, POIImportError {
        MetadataHarvester harvester = new MetadataHarvester();
        List<String> messages = harvester.harvest(new FileInputStream(metadataFile), null);
        Metadata metadata = TextTools.cleanMetadataTextInputs(harvester.getMetadata());
        Map<String, String> mappings = harvester.getMappings();
        WordImporter importer = harvester.getImporter();

        if (!((mappings.get(WordImporter.ORG_NAME) == null || mappings.get(WordImporter.ORG_NAME).trim().isEmpty()))) {
            List<Organisation> org = organisationMapper.searchForOrganisation(mappings.get(WordImporter.ORG_NAME));
            if (!org.isEmpty()) {
                metadata.setOrganisationID(org.get(0).getId());
            } else {
                messages.add("Could not detect Organisation, please select it from the list below or add manually");
            }
        } else {
            messages.add("Could not detect Organisation, please select it from the list below or add manually");
        }

        Map<String, String> retVal = new HashMap<String, String>();

        ObjectMapper m = new ObjectMapper();
        Map<String, Object> map = m.convertValue(metadata, Map.class);
        for (String key : map.keySet()) {
            retVal.put(key, map.get(key) != null ? map.get(key).toString() : "");
        }

        retVal.put("messages", StringUtils.join(messages.toArray(), ";"));

        return retVal;
    }

    @POST
    @Produces(DefaultTypes.JSON)
    @Path("/{path}/process")
    public Response processUpload(
            @TokenUser(allowPublic = false) User user,
            @PathParam("path") String path,
            @FormParam("mappings") String mappings,
            @FormParam("metadata") String metadata,
            @FormParam("friendlyName") String friendlyName) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            if (Integer.parseInt(matcher.group(1)) == user.getId()) {
                File jobDir = new File(properties.getProperty("validatorRoot") + File.separator + "tmp" + File.separator + path);
                File upload = new File(jobDir.getAbsoluteFile() + File.separator + "upload.tab");

                if (upload.exists()) {
                    try {
                        File queueDir = new File(properties.getProperty("validatorRoot") + File.separator + "queue" + File.separator + path);
                        File queueFile = new File(queueDir.getAbsoluteFile() + File.separator + "upload.tab");
                        queueDir.mkdirs();

                        File mappingsFile = new File(queueDir.getAbsoluteFile() + File.separator + "mappings.out");
                        PrintWriter writer = new PrintWriter(mappingsFile);

                        writer.write(mappings);
                        writer.flush();
                        writer.close();
                        
                        if (!StringUtils.isEmpty(metadata)) {
                            File metadataOut = new File(queueDir.getAbsoluteFile() + File.separator + "metadata.out");
                            writer = new PrintWriter(metadataOut);
                            writer.write(metadata);
                            writer.flush();
                            writer.close();
                        }

                        File infoFile = new File(queueDir.getAbsoluteFile() + File.separator + "info.out");
                        writer = new PrintWriter(infoFile);

                        String infoString = "{\"user\":\"" + user.getForename() + "\",\"email\":\"" + user.getEmail() + "\"" +
                                (!StringUtils.isEmpty(friendlyName) ? ",\"friendlyName\":\"" + friendlyName + "\"" : "") +
                                (!StringUtils.isEmpty(metadata) ? ",\"metadataIncluded\":\"true\"" : "")
                                + "}";
                        
                        writer.write(infoString);
                        writer.flush();
                        writer.close();

                        FileUtils.copyFile(upload, queueFile);
                        FileUtils.deleteDirectory(jobDir);
                    } catch (IOException ex) {

                    }
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity(path + " does not exist").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity(path + " does not belong to your user").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(path + " is not valid").build();
        }

        return null;
    }

    @POST
    @Produces(DefaultTypes.JSON)
    @Path("/{path}/remove")
    public Response removeUpload(
            @TokenUser(allowPublic = false) User user,
            @PathParam("user") int userID,
            @PathParam("path") String path) {
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            if (Integer.parseInt(matcher.group(1)) == userID) {
                File dir = new File(properties.getProperty("validatorRoot") + File.separator + "tmp" + File.separator + path);
                File upload = new File(properties.getProperty("validatorRoot") + File.separator + "tmp" + File.separator + path + File.separator + "upload.tab");

                if (dir.exists() && upload.exists()) {
                    if (dir.delete()) {
                        return Response.ok("Upload was removed from the validator service").build();
                    } else {
                        return Response.serverError().entity("Upload could not be removed").build();
                    }
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity(path + " does not exist").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity(path + " does not belong to your user").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(path + " is not valid").build();
        }
    }
}
