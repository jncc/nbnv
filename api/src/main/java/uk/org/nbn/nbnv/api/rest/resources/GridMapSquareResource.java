package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.providers.ProviderHelper;
import uk.org.nbn.nbnv.api.dao.warehouse.GridMapSquareMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.GridMapSquare;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.rest.resources.utils.DownloadHelper;

@Component
@Path("/gridMapSquares")
public class GridMapSquareResource extends AbstractResource {

    @Autowired GridMapSquareMapper gridMapSquareMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DownloadHelper downloadHelper;

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GridMapSquare> searchForGridSquares(@QueryParam("term") String term, @QueryParam("resolution") String resolution) {
        return gridMapSquareMapper.searchForMatchingResolutions(term, resolution);
    }
    
    /**
     * Returns a zip file containing a list of grid map squares filtered by the
     * given parameters, specifically the Taxon Version Key supplied
     * 
     * @param user The current user (determines what records are available)
     * @param ptvk The Taxon Version Key we are looking for
     * @param resolution What resolution we are looking for
     * @param bands A list of bands
     * @param datasets A list of datasets to restrict the search to
     * @param viceCountyIdentifier An identifier for a Vice County
     * 
     * @return A zip containing a list of grid squares filtered by the given
     * parameters
     * 
     * @throws IOException 
     * 
     * @response.representation.200.qname StreamingOutput
     * @response.representation.200.mediaType application/x-zip-compressed
     */
    @GET
    @Produces("application/x-zip-compressed")
    @Path("{ptvk : [A-Z]{3}SYS[0-9]{10}}")
    public StreamingOutput getGridMapSquares(
            @Context HttpServletResponse response,
            @TokenUser() final User user,
            @PathParam("ptvk") final String ptvk,
            @QueryParam("resolution") @DefaultValue("") final String resolution,
            @QueryParam("band") @DefaultValue("") final List<String> bands,
            @QueryParam("datasets") @DefaultValue(ObservationResourceDefaults.defaultDatasetKey) final List<String> datasets,
            @QueryParam("feature") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) final String viceCountyIdentifier)
            throws IOException {
        
        // Set the filename to get around a bug with Firefox not adding the extension properly
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s_grid_squares.zip\"", ptvk));
        
        return new StreamingOutput() {
            
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                addReadMe(zip, user, ptvk, resolution, bands);
                addGridRefs(zip, user, ptvk, resolution, bands, datasets, viceCountyIdentifier);
                addDatasetMetadata(zip, user, ptvk, resolution, bands, datasets, viceCountyIdentifier);
                zip.flush();
                zip.close();
            }
        };
    }

    /**
     * 
     * @param zip
     * @param user
     * @param ptvk
     * @param resolution
     * @param bands
     * @throws IOException 
     */
    private void addReadMe(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands) throws IOException {
        Taxon taxon = taxonMapper.getTaxon(ptvk);
        String title = "Grid map square download from the NBN Gateway";
        HashMap<String, String> filters = new HashMap<String, String>();
        filters.put("Taxon", taxon.getName() + " " + taxon.getAuthority());
        filters.put("Resolution: ", resolution);
        int i = 1;
        for(String band: bands){
            filters.put("Year range " + i++, band.substring(0,band.indexOf(",")));
        }
        downloadHelper.addReadMe(zip, user, title, filters);
    }

    /**
     * 
     * @param zip
     * @param user
     * @param ptvk
     * @param resolution
     * @param bands
     * @param datasetKey
     * @param viceCountyIdentifier
     * @throws IOException 
     */
    private void addGridRefs(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKey, String viceCountyIdentifier) throws IOException {
        for (String band : bands) {
            if (!"".equals(band)) {
                addGridRefsForYearBand(zip, user, ptvk, resolution, band, datasetKey, viceCountyIdentifier);
            } else {
                throw new IllegalArgumentException("No year band arguments supplied, at least one 'band' argument is required (eg band=2000-2012,ff0000,000000)");
            }
        }
    }

    /**
     * 
     * @param zip
     * @param user
     * @param ptvk
     * @param resolution
     * @param band
     * @param datasetKeys
     * @param viceCountyIdentifier
     * @throws IOException 
     */
    private void addGridRefsForYearBand(ZipOutputStream zip, User user, String ptvk, String resolution, String band, List<String> datasetKeys, String viceCountyIdentifier) throws IOException {
        //Example year band: 2000-2012,ff0000,000000
        String yearRange = band.substring(0,band.indexOf(","));
        zip.putNextEntry(new ZipEntry("GridSquares_" + yearRange + ".csv"));
        List<GridMapSquare> gridMapSquares = gridMapSquareMapper.getGridMapSquares(user, ptvk, resolution, band, datasetKeys, viceCountyIdentifier, 0);
        downloadHelper.writeln(zip, "GridSquares");
        for (GridMapSquare gridMapSquare : gridMapSquares) {
            downloadHelper.writeln(zip, gridMapSquare.getGridRef());
        }
        zip.flush();
    }

    /**
     * 
     * @param zip
     * @param user
     * @param ptvk
     * @param resolution
     * @param bands
     * @param datasetKeys
     * @param viceCountyIdentifier
     * @throws IOException 
     */
    private void addDatasetMetadata(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKeys, String viceCountyIdentifier) throws IOException {
        List<TaxonDataset> taxonDatasets = gridMapSquareMapper.getGridMapDatasets(user, ptvk, resolution, getStartYear(bands), getEndYear(bands), datasetKeys, viceCountyIdentifier);
        downloadHelper.addDatasetMetadata(zip, user.getId(), taxonDatasets);
    }
    
    /**
     * 
     * @param bands
     * @return 
     */
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
    
    /**
     * 
     * @param bands
     * @return 
     */
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
    
}
