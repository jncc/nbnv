package uk.org.nbn.nbnv.api.rest.resources;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.validator.ValidatorColumnModel;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DefaultTypes;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.s1.utils.parser.NXFParser;

@Component
@Path("/validator")
public class OnlineValidatorResource extends AbstractResource {
    
    private Pattern pattern;
    private static final String UPLOAD = "upload.tab";
    
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

            String jobName = formatter.format(new Date())+ "_" 
                    + user.getId() + "_" + RandomStringUtils.randomAlphanumeric(10);
            
            File tmpDir = new File(properties.getProperty("validatorRoot") + File.separator + "tmp" + File.separator + jobName);
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }
            File upload = new File(tmpDir.getAbsoluteFile() + File.separator + UPLOAD);
            
            OutputStream output = new FileOutputStream(upload);
            
            int read=0;
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
    
    @POST
    @Produces(DefaultTypes.JSON)
    @Path("/{path}/process")
    public Response processUpload(
            @TokenUser(allowPublic = false) User user,
            @PathParam("path") String path,
            @FormParam("mappings") String mappings) {
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
                        
                        File infoFile = new File(queueDir.getAbsoluteFile() + File.separator + "info.out");
                        writer = new PrintWriter(infoFile);
                        
                        writer.write("{\"user\":\"" + user.getForename() + "\",\"email\":\"" + user.getEmail() + "\"}");
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