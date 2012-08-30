package nbn.webmapping.json.entity;
import nbn.webmapping.json.entity.resolvers.*;

/**
 *
 * @author Administrator
 */
public enum ResolveableEntity {
    SPECIES("species", new TaxonResolveableEntityResolver()),
    DATASET("dataset", new DatasetResolveableEntityResolver(false)),
    DATASETS("datasets", new DatasetsResolveableEntityResolver(false)),
    DATASET_WITH_METADATA("datasetWithMetadata", new DatasetResolveableEntityResolver(true)),
    DATASETS_WITH_METADATA("datasetsWithMetadata", new DatasetsResolveableEntityResolver(true)),
    DESIGNATION("designation", new DesignationResolveableEntityResolver()),
    HABITAT("habitats", new HabitatsResolveableEntityResolver()),
    SITE_BOUNDARY("boundary", new SiteBoundaryResolveableEntityResolver());

    private String queryStringParameter;
    private ResolveableEntityResolver resolvableEntity;
    private ResolveableEntity(String queryStringParameter, ResolveableEntityResolver resolvableEntity) {
        this.queryStringParameter = queryStringParameter;
        this.resolvableEntity = resolvableEntity;
    }

    public String getQueryStringParameter() {
        return queryStringParameter;
    }

    public ResolveableEntityResolver getResolveableEntityResolver() {
        return resolvableEntity;
    }

    public static ResolveableEntity getResolveableEntityFromQueryStringParameter(String queryStringParameter) {
        for(ResolveableEntity currEntity : ResolveableEntity.values()) {
            if(queryStringParameter.equals(currEntity.getQueryStringParameter()))
                return currEntity;
        }
        throw new IllegalArgumentException("There is no ResolveableEntity which matches the passed in query string - " + queryStringParameter);
    }
}
