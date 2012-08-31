package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonMapper;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.YearStats;

@Component
@Path("/taxonDatasets")
public class TaxonDatasetResource {
    
    @Autowired DatasetMapper datasetMapper;
    @Autowired TaxonMapper taxonMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getDatasetList(){
        return datasetMapper.selectAllTaxonDatasets();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonDataset getTaxonDatasetByID(@PathParam("id") String id){
        TaxonDataset toReturn = datasetMapper.selectTaxonDatasetByID(id);
        toReturn.setTaxa(taxonMapper.selectByDatasetKey(id));
        toReturn.setRecordsPerYear(datasetMapper.selectRecordsPerYear(id));
        return toReturn;
    }
    
    @GET
    @Path("/{id}/taxa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getTaxaByDatasetKey(@PathParam("id") String id){
        return taxonMapper.selectByDatasetKey(id);
    }
    
    @GET
    @Path("/{id}/recordsPerYear")
    @Produces(MediaType.APPLICATION_JSON)
    public List<YearStats> getRecordsPerYearByDatasetKey(@PathParam("id") String id){
        return datasetMapper.selectRecordsPerYear(id);
    }
    
}
