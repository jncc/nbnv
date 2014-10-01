package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProviderWithQueryStats implements Comparable<ProviderWithQueryStats> {
    
    private int organisationID, querySpecificObservationCount;
    private Organisation organisation;
    private List<TaxonDatasetWithQueryStats> datasetsWithQueryStats;
    
    public ProviderWithQueryStats(){}
    
    public ProviderWithQueryStats(int organisationID, Organisation organisation, int querySpecificObservationCount, List<TaxonDatasetWithQueryStats> datasetsWithQueryStats){
        this.organisationID = organisationID;
        this.organisation = organisation;
        this.querySpecificObservationCount = querySpecificObservationCount;
        this.datasetsWithQueryStats = datasetsWithQueryStats;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public int getQuerySpecificObservationCount() {
        return querySpecificObservationCount;
    }

    public void setQuerySpecificObservationCount(int querySpecificObservationCount) {
        this.querySpecificObservationCount = querySpecificObservationCount;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public List<TaxonDatasetWithQueryStats> getDatasetsWithQueryStats() {
        return datasetsWithQueryStats;
    }

    public void setDatasetsWithQueryStats(List<TaxonDatasetWithQueryStats> datasetsWithQueryStats) {
        this.datasetsWithQueryStats = datasetsWithQueryStats;
    }

    @Override
    public int compareTo(ProviderWithQueryStats that) {
        return new Integer(this.querySpecificObservationCount).compareTo(new Integer(that.querySpecificObservationCount));
    }
    
}
