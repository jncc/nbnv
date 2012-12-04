package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.*;
import javax.ws.rs.core.StreamingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.providers.ProviderHelper;
import uk.org.nbn.nbnv.api.dao.warehouse.GridMapSquareMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
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
            @QueryParam("datasetKey") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) final List<String> datasetKeys) throws IOException {
        return new StreamingOutput() {
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                addReadMe(zip, ptvk, resolution);
                addGridRefs(zip, user, ptvk, resolution, bands, datasetKeys);
                addDatasetMetadata(zip, user, ptvk, resolution, bands, datasetKeys);
                zip.flush();
                zip.close();
            }
        };
    }

    private void addReadMe(ZipOutputStream zip, String ptvk, String resolution) throws IOException {
        Taxon taxon = taxonMapper.getTaxon(ptvk);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        zip.putNextEntry(new ZipEntry("readme.txt"));
        writeln(zip, "Grid map square download from the NBN Gateway");
        writeln(zip, "---------------------------------------------");
        writeln(zip, "Taxon: " + taxon.getName() + " (authority: " + taxon.getAuthority() + ")");
        writeln(zip, "Date and time of download: " + dateFormat.format(new Date()));
        writeln(zip, "Resolution: " + resolution);
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

    private void addGridRefsForYearBand(ZipOutputStream zip, User user, String ptvk, String resolution, String band, List<String> datasetKeys) throws IOException {
        //Example year band: 2000-2012,ff0000,000000
        String yearRange = band.substring(0,band.indexOf(","));
        zip.putNextEntry(new ZipEntry("gridrefs_" + yearRange + ".csv"));
        List<GridMapSquare> gridMapSquares = gridMapSquareMapper.getGridMapSquares(user, ptvk, resolution, band, datasetKeys);
        for (GridMapSquare gridMapSquare : gridMapSquares) {
            writeln(zip, gridMapSquare.getGridRef());
        }
        zip.flush();
    }

    private void addDatasetMetadata(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKeys) throws IOException {
        List<Dataset> datasets = gridMapSquareMapper.getGridMapDatasets(user, ptvk, resolution, getStartYear(bands), getEndYear(bands), datasetKeys);
        zip.putNextEntry(new ZipEntry("datasetmetadata.txt"));
        writeln(zip, "Datasets that contributed to this download");
        for(Dataset dataset : datasets){
        writeln(zip, "------------------------------------------");
        writeln(zip, "");
            writeln(zip, "Title: " + dataset.getTitle());
            writeln(zip, "");
            writeln(zip, "Dataset key: " + dataset.getKey());
            writeln(zip, "");
            writeln(zip, "Description: " + dataset.getDescription());
            writeln(zip, "");
            writeln(zip, "Dataset owner: " + dataset.getOrganisationName());
            writeln(zip, "");
            if(dataset.getUseConstraints() != null && !"".equals(dataset.getUseConstraints().trim())){
                writeln(zip, "Use constraints: " + dataset.getUseConstraints());
                writeln(zip, "");
            }
            if(dataset.getAccessConstraints() != null && !"".equals(dataset.getAccessConstraints().trim())){
                writeln(zip, "Access constraints: " + dataset.getAccessConstraints());
                writeln(zip, "");
            }
        }
        
        zip.flush();
    }
    
    private Integer getStartYear(List<String> bands){
        Integer toReturn = Calendar.getInstance().get(Calendar.YEAR);
        if(bands.size() < 1){
            throw new IllegalArgumentException("No year bands have been specified, there should be at least one (eg 2000-2012,ff0000,000000)");
        }else{
            for(String band : bands){
                Integer currentStartYear = ProviderHelper.getStartYear(band);
                if (toReturn > currentStartYear){
                    toReturn = currentStartYear;
                }
            }
        }
        return toReturn;
    }
    
    private Integer getEndYear(List<String> bands){
        Integer toReturn = 0;
        if(bands.size() < 1){
            throw new IllegalArgumentException("No year bands have been specified, there should be at least one (eg 2000-2012,ff0000,000000)");
        }else{
            for(String band : bands){
                Integer currentEndYear = ProviderHelper.getEndYear(band);
                if (toReturn < currentEndYear){
                    toReturn = currentEndYear;
                }
            }
        }
        return toReturn;
    }
    
    private void writeln(ZipOutputStream zip, String output) throws IOException{
        zip.write((output + "\r\n").getBytes());
    }
    
}
