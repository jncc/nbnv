package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
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
import uk.org.nbn.nbnv.api.utils.Status;

@Component
@Path("/gridMapSquares")
public class GridMapSquareResource extends AbstractResource {

    @Autowired GridMapSquareMapper gridMapSquareMapper;
    @Autowired TaxonMapper taxonMapper;
    @Autowired DownloadHelper downloadHelper;

    /**
     * Returns a list of grid squares matching a name and / or resolution 
     * parameter
     * 
     * @param term The partial term to search for
     * @param resolution The resolution to search for (10km, 2km, 1km or 100m)
     * 
     * @return A list of matching grid squares
     * 
     * @response.representation.200.qname GridMapSquare
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/search")
    @TypeHint(GridMapSquare.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Succesfully returned a list of matching grid squares")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<GridMapSquare> searchForGridSquares(@QueryParam("term") String term, @QueryParam("resolution") String resolution) {
        return gridMapSquareMapper.searchForMatchingResolutions(term, resolution);
    }
    
    /**
     * Returns a zip file containing a list of grid map squares filtered by the
     * given parameters, specifically the Taxon Version Key supplied
     * 
     * @param user The current user (determines what records are available)
     * (Injected Token no need to pass)
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
            @QueryParam("feature") @DefaultValue(ObservationResourceDefaults.defaultFeatureID) final String viceCountyIdentifier,
            @QueryParam("verification") @DefaultValue("") final List<String> verifications)
            throws IOException {
	
	// Set the filename to get around a bug with Firefox not adding the extension properly
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s_grid_squares.zip\"", ptvk));
        
        return new StreamingOutput() {
            
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                ZipOutputStream zip = new ZipOutputStream(out);
                try{
                    addReadMe(zip, user, ptvk, resolution, bands, verifications);
                    addGridRefs(zip, user, ptvk, resolution, bands, datasets, viceCountyIdentifier, verifications);
                    addDatasetMetadata(zip, user, ptvk, resolution, bands, datasets, viceCountyIdentifier, verifications);
                }finally{
                    zip.flush();
                    zip.close();
                }
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
    private void addReadMe(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> verifications) throws IOException {
        Taxon taxon = taxonMapper.getTaxon(ptvk);
        String title = "Grid map square download from the NBN Gateway";
        HashMap<String, String> filters = new HashMap<String, String>();
        filters.put("Taxon", taxon.getName() + " " + taxon.getAuthority());
        filters.put("Resolution ", resolution);
        int i = 1;
        for(String band: bands){
            filters.put("Year range " + i++, band.substring(0,band.indexOf(",")));
        }
        i=1;
        for(Integer verificationKey : getVerificationKeys(verifications)){
            filters.put("Verification status " + i++, Status.get(verificationKey).name());
        }
        downloadHelper.addReadMe(zip, user, title, filters);
    }
    
    private void addGridRefs(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKeys, String viceCountyIdentifier, List<String> verifications) throws IOException {
	List<Integer> verificationKeys = getVerificationKeys(verifications);
	boolean isGroupByDate = isGroupByDate(verifications);
	if(isGroupByDate){
	    for (String band : bands) {
		if (!"".equals(band)) {
		    String title = "GridSquares_" + band.substring(0,band.indexOf(","));
		    fetchAndAppendGridRefs(zip, user, ptvk, resolution, Arrays.asList(band), datasetKeys, viceCountyIdentifier, verificationKeys, title, isGroupByDate);
		} else {
		    throw new IllegalArgumentException("No year band arguments supplied, at least one 'band' argument is required (eg band=2000-2012,ff0000,000000)");
		}
	    }
	}else{
	    for (Integer verificationKey : verificationKeys){
		String title = "GridSquares_" + Status.get(verificationKey);
		fetchAndAppendGridRefs(zip, user, ptvk, resolution, bands, datasetKeys, viceCountyIdentifier, Arrays.asList(verificationKey), title, isGroupByDate);
	    }
	}
    }

    /**
    * @param bands list of Strings with year range and colours, eg: 2000-2012,ff0000,000000
    * @param list of verification statuses either with colours (eg 1,ff0000,00ff00), or without (eg 1)
    */
    private void fetchAndAppendGridRefs(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKeys, String viceCountyIdentifier, List<Integer> verificationKeys, String title, boolean isGroupByDate) throws IOException {
        zip.putNextEntry(new ZipEntry(title + ".csv"));
        List<GridMapSquare> gridMapSquares = gridMapSquareMapper.getGridMapSquares(user, ptvk, resolution, bands, datasetKeys, viceCountyIdentifier, 0, verificationKeys, isGroupByDate);
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
    private void addDatasetMetadata(ZipOutputStream zip, User user, String ptvk, String resolution, List<String> bands, List<String> datasetKeys, String viceCountyIdentifier, List<String> verifications) throws IOException {
	List<Integer> verificationKeys = getVerificationKeys(verifications);
	boolean isGroupByDate = isGroupByDate(verifications);
        List<TaxonDataset> taxonDatasets = gridMapSquareMapper.getGridMapDatasets(user, ptvk, resolution, bands, datasetKeys, viceCountyIdentifier, verificationKeys, isGroupByDate);
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
    
    /*
     * Returns true if the output should contain one file per date band, 
     * otherwise false (which indicates one file per verification status).
     * The test relies on a colour being found in the Verification argument (eg 1,ff0000,00ff00),
     * which indicates the user was looking at verifications on the map
     * rather than date bands.
     */
    private boolean isGroupByDate(List<String> verifications){
	if(verifications == null || verifications.isEmpty()){
	    return true;
	}else{
	    String verificationPattern = "[1-4],[0-9a-fA-F]{6},[0-9a-fA-F]{6}";
	    return !verifications.get(0).matches(verificationPattern);
	}
    }

    private List<Integer> getVerificationKeys(List<String> verifications){
	List<Integer> defaultVerificationKeys = Arrays.asList(1,3,4);
	if(isValidVerifications(verifications)){
	    if(verifications != null && !verifications.isEmpty()){
		List<Integer> toReturn = new ArrayList<Integer>();
		for(String verification : verifications){
		    toReturn.add(Integer.parseInt(verification.substring(0,1)));
		}
		return toReturn;
	    }else{
		return defaultVerificationKeys;
	    }
	}else{
	    throw new IllegalArgumentException("At least one  Validation argument is incorrect, it should be either a  verification key in the range 1-4, or a verification key with fill and outline rgb hex colours separated by commas (eg 2,0000ff,00ff00)");
	}
    }
    
    /*
     * Tests for valid verifications, the allowed values are:
     * - null or empty
     * - Integers in range 1-4
     * - Integers in range 1-4 followed fill and outline rgb hex colour comma separated (eg 2,ff0000,00ff00)
     */
    private boolean isValidVerifications(List<String> verifications){
	boolean toReturn = false;
	if(verifications != null && !verifications.isEmpty()){
	    String validVerification = "[1-4](,[0-9a-fA-F]{6},[0-9a-fA-F]{6})?";
	    for(String verification : verifications){
		toReturn = toReturn || verification.matches(validVerification);
	    }
	}else{
	    toReturn = true;
	}
	return toReturn;
    }
    
    
}
