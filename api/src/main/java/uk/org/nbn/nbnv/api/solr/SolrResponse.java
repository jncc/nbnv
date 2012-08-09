/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.solr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author Administrator
 */
public class SolrResponse {
    private final QueryResponse toWrap;
    public SolrResponse(QueryResponse toWrap) {
        this.toWrap = toWrap;
    }
    
    public List<SolrDocument> getResults() {
        return toWrap.getResults();
    }
    
    public Map getFacetFields() {
        return processFacetFields(toWrap.getFacetFields());
    }
    
    private static Map processFacetFields(List<FacetField> facetFields) {
        Map toReturn = new HashMap();
        for(FacetField currField : facetFields) {
            toReturn.put(currField.getName(), 
                (currField.getName().startsWith("category"))
                    ? processCategoriesdFacet(currField.getValues())
                    : processFacetCounts(currField.getValues()));
        }
        return toReturn;
    }
    
    private static Map processFacetCounts(List<Count> facetCount) {
        Map toReturn = new HashMap();
        for(Count currCount: facetCount) {
            toReturn.put(currCount.getName(), currCount.getCount());
        }
        return toReturn;
    }
    
    private static Map processCategoriesdFacet(List<Count> facetCount) {
        SubCategoryFacet toReturn = new SubCategoryFacet();
        
        for(Count currCatFacet: facetCount) {
            toReturn.getSubCategory(currCatFacet.getName()).setTotalCount(currCatFacet.getCount());
        }
        return toReturn.getSubCategories();
    }
    
    private static class SubCategoryFacet {
        private long totalCount;
        private Map<String, SubCategoryFacet> subCategories = new HashMap<String, SubCategoryFacet>();

        public Map<String, SubCategoryFacet> getSubCategories() {
            return (subCategories.isEmpty()) ? null :subCategories;
        }
        
        public SubCategoryFacet getSubCategory(String path) {
            System.out.println(path);
            String currSubCategoryName = path.split(">")[0];
            if(!subCategories.containsKey(currSubCategoryName)) {
                subCategories.put(currSubCategoryName, new SubCategoryFacet());
            }
            SubCategoryFacet nextCategory = subCategories.get(currSubCategoryName);
            if(path.contains(">")) 
                return nextCategory.getSubCategory(path.substring(currSubCategoryName.length()+1));
            else 
                return nextCategory;
        }

        public long getTotalCount() {
            return totalCount;
        }

        void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }
    }
    
    public SolrResponseHeader getHeader() {
        return new SolrResponseHeader();
    }
    
    public class SolrResponseHeader {
        public long getNumFound() {
            return toWrap.getResults().getNumFound();
        }  
        
        public long getStart() {    
            return toWrap.getResults().getStart();
        }
        
        public Long getRows() {
            NamedList requestParameters = (NamedList)toWrap.getResponseHeader().get("params");
            String rowsStr = (String)requestParameters.get("rows");
            if(rowsStr!=null) {
                return Long.parseLong(rowsStr);
            }
            else {
                return null;
            }
        }
    }
}
