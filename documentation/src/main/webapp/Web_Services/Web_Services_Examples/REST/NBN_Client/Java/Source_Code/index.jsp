<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBN Client Source Code Java</h1>
        <script type="syntaxhighlighter" class="brush: java; html-script: false"><![CDATA[
        package nbnapi;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;

        import javax.ws.rs.core.Cookie;
        import javax.ws.rs.core.MediaType;
        import javax.ws.rs.core.MultivaluedMap;
        import javax.ws.rs.core.NewCookie;

        import com.sun.jersey.api.client.*;
        import com.sun.jersey.core.util.MultivaluedMapImpl;
        import com.fasterxml.jackson.databind.DeserializationFeature;
        import com.fasterxml.jackson.databind.ObjectMapper;

        import java.net.URI;

        public class NBNClient {

                private Cookie authCookie;

                public boolean login(String username, String password) {
                        try {
                                URI uri = new URI("https://data.nbn.org.uk/api/user/login");
                                Client client = Client.create();
                                WebResource webResource = client.resource(uri);
                        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
                        formData.add("username", username);
                        formData.add("password", password);
                        ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);

                        if (clientResponse.getStatus() == 200 && clientResponse.getCookies().size() > 0) {
                                List<NewCookie> cookies = clientResponse.getCookies();
                                for (NewCookie c : cookies) {
                                    // Cookie name we need is "nbn.token_key"
                                    if (c.getName().equals("nbn.token_key")) {
                                        authCookie = c;
                                    }
                                }
                                return true;
                        }
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                return false;
                }

                private String getJSON(URI uri) {
                        Client client = Client.create();
                        WebResource webResource = client.resource(uri);
                        WebResource.Builder builder = webResource.getRequestBuilder();
                        ClientResponse response = builder.accept("application/json").get(ClientResponse.class);
                        String json = response.getEntity(String.class);
                        return json;
                }

                private String postJSON(URI uri, MultivaluedMap<String, String> filters) {
                        Client client = Client.create();
                        WebResource webResource = client.resource(uri);
                        WebResource.Builder builder = webResource.getRequestBuilder();
                        builder = builder.cookie(authCookie);
                        ClientResponse response = builder.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, filters);
                        String json = response.getEntity(String.class);
                        return json;
                }

                public SiteBoundary[] getViceCounties()
                {
                        SiteBoundary[] data = null;
                        try {
                                URI uri = new URI("https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries");
                                String json = getJSON(uri);
                                data = new ObjectMapper().readValue(json, SiteBoundary[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                        return data;
                }

                public TaxonOutputGroup[] getTaxonOutputGroups()
            {
                TaxonOutputGroup[] data = null;
                        try {
                                URI uri = new URI("https://data.nbn.org.uk/api/taxonOutputGroups");
                                String json = getJSON(uri);
                                data = new ObjectMapper().readValue(json, TaxonOutputGroup[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                        return data;
            }

            public Designation[] getDesignations()
            {
                Designation[] data = null;
                try {
                        URI uri = new URI("https://data.nbn.org.uk/api/designations");
                        String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, Designation[].class);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
                return data;
            }

            // featureID is the identifier (not the featureID of the boundary)
            public TaxonWithQueryStats[] getSiteSpecies(String featureID, String taxonOutputGroup, String designation)
            {
                TaxonWithQueryStats[] data = null;
                try {
                        String uriString = String.format("https://data.nbn.org.uk/api/taxonObservations/species?featureID=%s&taxonOutputGroup=%s", featureID, taxonOutputGroup);
                        if (designation != null && designation.length() > 0)
                        {
                            uriString = uriString + String.format("&designation=%s", designation);
                        }
                        URI uri = new URI(uriString);
                        String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, TaxonWithQueryStats[].class);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
                return data;
            }

            public TaxonDatasetWithQueryStats[] getSiteDatasets(String featureID, String taxonOutputGroup, String designation)
            {
                TaxonDatasetWithQueryStats[] data = null;
                try {
                        String uriString = String.format("https://data.nbn.org.uk/api/taxonObservations/datasets?featureID={0}&taxonOutputGroup=%s", featureID, taxonOutputGroup);
                        if (designation != null && designation.length() > 0)
                        {
                            uriString = uriString + String.format("&designation=%s", designation);
                        }
                        URI uri = new URI(uriString);
                        String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, TaxonDatasetWithQueryStats[].class);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
                return data;
            }

                public TaxonWithQueryStats[] getTenKmSpecies(String gridReference)
            {
                        TaxonWithQueryStats[] data = null;
                        try {
                        URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxonObservations/species?gridRef=%s", gridReference));
                        String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, TaxonWithQueryStats[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                        return data;
            }

                public TaxonDatasetWithQueryStats[] getTenKmDatasets(String gridReference) {
                        TaxonDatasetWithQueryStats[] data = null;
                        try {
                                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxonObservations/datasets?gridRef=%s", gridReference));
                        String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, TaxonDatasetWithQueryStats[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                        return data;
                }

                public SearchResult getTaxonSearchResult(String query)
            {
                        SearchResult data = null;
                        try {
                                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa?q=%s", query));
                        String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, SearchResult.class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                        return data;
            }

                public Taxon getTaxonomy(String taxonVersionKey)
            {
                        Taxon data = null;
                        try {
                                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa/%s", taxonVersionKey));
                                String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, Taxon.class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                return data;
            }

                public Taxon getTaxonParent(String taxonVersionKey)
            {
                        Taxon data = null;
                        try {
                                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa/%s/parent", taxonVersionKey));
                                String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, Taxon.class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                return data;
            }

                public Taxon[] getTaxonParents(String taxonVersionKey)
            {
                String tvk = taxonVersionKey;
                List<Taxon> parents = new ArrayList<Taxon>();
                Taxon parent;
                do
                {
                    parent = getTaxonParent(tvk);
                    if (parent != null)
                    {
                        tvk = parent.getTaxonVersionKey();
                        parents.add(parent);
                    }
                } while (parent != null);
                Collections.reverse(parents);
                return parents.toArray(new Taxon[0]);
            }

                public Taxon[] getTaxonChildren(String taxonVersionKey)
            {
                        Taxon[] data = null;
                        try {
                                URI uri = new URI(String.format("https://data.nbn.org.uk/api/taxa/%s/children", taxonVersionKey));
                                String json = getJSON(uri);
                        data = new ObjectMapper().readValue(json, Taxon[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                return data;
            }

                public TaxonObservation[] postRecordsForDatasetAndGridRef(String datasetKey, String gridReference)
            {
                        TaxonObservation[] data = null;
                        try {
                                URI uri = new URI("https://data.nbn.org.uk/api/taxonObservations");
                                MultivaluedMap<String, String> filters = new MultivaluedMapImpl();
                        filters.add("datasetKey", datasetKey);
                        filters.add("gridRef", gridReference);
                        String json = postJSON(uri, filters);
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        data = mapper.readValue(json, TaxonObservation[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                return data;
            }

                public TaxonObservation[] postRecordsForWKTDesignationDatasetDates(String wkt, String designation, String datasetKey, int startYear, int endYear)
            {
                        TaxonObservation[] data = null;
                        try {
                URI uri = new URI("https://data.nbn.org.uk/api/taxonObservations");
                MultivaluedMap<String, String> filters = new MultivaluedMapImpl();
                filters.add("polygon", wkt);
                filters.add("designation", designation);
                filters.add("startYear", Integer.toString(startYear));
                filters.add("endYear", Integer.toString(endYear));
                filters.add("datasetKey", datasetKey);
                String json = postJSON(uri, filters);
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                data = mapper.readValue(json, TaxonObservation[].class);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                        return data;
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
