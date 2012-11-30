package uk.org.nbn.nbnv.api.rest.resources;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.GridMapSquareMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.GridMapSquare;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;

@Component
@Path("/gridMapSquares")
public class GridMapSquareResource extends AbstractResource {

    @Autowired
    GridMapSquareMapper gridMapSquareMapper;
    @Autowired
    TaxonMapper taxonMapper;

    @GET
    @Produces("application/x-zip-compressed")
    @Path("{ptvk : [A-Z]{3}SYS[0-9]{10}}")
    public StreamingOutput getGridMapSquares(
            @TokenUser() final User user,
            @PathParam("ptvk") final String ptvk,
            @QueryParam("resolution") @DefaultValue("") final String resolution,
            @QueryParam("band") @DefaultValue("") final List<String> bands,
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) final List<String> datasetKey) throws IOException {
        return new StreamingOutput() {
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                addReadMe(zip, ptvk);
                addGridRefs(zip, user, ptvk, resolution, bands, datasetKey);
                zip.flush();
                zip.close();
            }
        };
    }

    private void addReadMe(ZipOutputStream zip, String ptvk) throws IOException {
        Taxon taxon = taxonMapper.getTaxon(ptvk);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mmm/dd HH:mm:ss");
        zip.putNextEntry(new ZipEntry("readme.txt"));
        zip.write("Grid map square download from the NBN Gateway\r\n".getBytes());
        zip.write((taxon.getName() + " (authority: " + taxon.getAuthority() + ")\r\n").getBytes());
        zip.write((dateFormat.format(new Date()) + "\r\n").getBytes());
        zip.flush();
    }

    private void addGridRefs(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKey) throws IOException {
        for (String band : bands) {
            if (!"".equals(band)) {
                addGridRefsForYearBand(zip, user, ptvk, resolution, band, datasetKey);
            } else {
                throw new IllegalArgumentException("No year band arguments supplied, at least one 'band' argument is required (eg band=2000-2012,ff0000,000000)");
            }
        }
    }

    private void addGridRefsForYearBand(ZipOutputStream zip, User user, String ptvk, String resolution, String band, List<String> datasetKey) throws IOException {
        //Example year band: 2000-2012,ff0000,000000
        String yearRange = band.substring(0,band.indexOf(","));
        zip.putNextEntry(new ZipEntry("gridrefs_" + yearRange + ".csv"));
        List<GridMapSquare> gridMapSquares = gridMapSquareMapper.getGridMapSquares(user, ptvk, resolution, band, datasetKey);
        for (GridMapSquare gridMapSquare : gridMapSquares) {
            zip.write((gridMapSquare.getGridRef() + "\r\n").getBytes());
        }
        zip.flush();
    }

    private void addDatasetMetadata(ZipOutputStream zip, String ptvk) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mmm/dd HH:mm:ss");
        zip.putNextEntry(new ZipEntry("datasetmetadata.txt"));
        zip.flush();
    }
}
