package uk.org.nbn.nbnv.api.rest.resources;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DefaultTypes;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.NXFParser;

@Component
@Path("/validator")
public class OnlineValidatorResource extends AbstractResource {
    
    @PUT
    @Produces(DefaultTypes.JSON)
    public Response uploadDataFile(
            @TokenUser(allowPublic = false) User user,
            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
                
                String tmpFolderName = formatter.format(new Date())+ "_" 
                        + user.getId() + "_" + RandomStringUtils.random(10);
                
                String rootPath = properties.getProperty("validatorRoot") + File.separator + "tmp";
                File dir = new File(rootPath + File.separator + tmpFolderName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                File tmpFile = new File(dir.getAbsolutePath() + File.separator + "upload.tab");
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tmpFile));
                stream.write(bytes);
                stream.close();
                
                NXFParser parser = new NXFParser(tmpFile);
                List<ColumnMapping> headings = parser.parseHeaders();
                               
                return null;
            
            } catch (IOException ex) {
                return Response.serverError().entity("There was an error while reading the uploaded file: " + ex.getLocalizedMessage()).build();
            }
        } else {
            return Response.serverError().entity("No file was uploaded").build();
        }
    }
    
    @POST
    @Produces(DefaultTypes.JSON)
    @Path("/{user}/{path}/process")
    public String processUpload(
            @TokenUser(allowPublic = false) User user,
            @PathParam("user") int userID,
            @PathParam("path") String path,
            @FormParam("mappings") String mappings) {
        return null;
    }
    
    @POST
    @Produces(DefaultTypes.JSON)
    @Path("/{user}/{path}/remove")
    public String removeUpload(
            @TokenUser(allowPublic = false) User user,
            @PathParam("user") int userID,
            @PathParam("path") String path) {
        return null;
    }   
}