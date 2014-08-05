<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBNClient Java</h1>
        <a href="Source_Code" target="_blank"><h2>NBN Client Source Code</h2></a>
        <p>Source code for the data classes:</p>
        <ul>
            <li><a href="../Source/Java/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats and Taxon</a></li>
            <li><a href="../Source/Java/TaxonDatasetWithQueryStats" target="_blank">TaxonDatasetWithQueryStats and TaxonDataset</a></li>
            <li><a href="../Source/Java/TaxonOutputGroup" target="_blank">TaxonOutputGroup</a></li>
            <li><a href="../Source/Java/SiteBoundary" target="_blank">SiteBoundary</a></li>
            <li><a href="../Source/Java/SearchResult" target="_blank">SearchResult and Result</a></li>
            <li><a href="../Source/Java/TaxonObservation" target="_blank">TaxonObservation</a></li>
            <li><a href="../Source/Java/Designation" target="_blank">Designation</a></li>
        </ul>
        <p>External libraries used:</p>
        <ul>
            <li><a href="https://jersey.java.net/download.html" target="_blank">Jersey 1.18 bundle</a></li>
            <li><a href="http://wiki.fasterxml.com/JacksonDownload" target="_blank">Jackson Java JSON-processor 2.2.3</a></li>
        </ul>

        <p>The NBN Client makes requests to the NBN API and converts the JSON into simple data classes that can be displayed in JSP pages.</p>
        <p>This page describes all the methods in the NBNClient, including those for the more-advanced examples. The first section of this page describes the basic functionality of the NBNClient and should be read first. The second section describes functionality needed for later examples and can be read when needed.</p>
        <h2>Section 1: Basic Requests and Processing the JSON</h2>
        <h2>Making a request</h2>
        <p>To make requests to the NBN API, we will be using the <a href="https://jersey.java.net/" target="_blank">Jersey RESTful Web Services framework</a>. This is a comprehensive library for creating RESTful web services but we will just use the client APIs. The examples use version 1.18 of the bundle, which can be downloaded <a href="https://jersey.java.net/download.html" target="_blank">here</a>. Download this and include it in your Java project.</p>
        <p>For requests, we need to specify the location of the resource as a URI. Then we create a client to the web services and pass in our URI. Next, we build the request object, specifying that it expects a JSON response, and make the GET request, specifying the type of the returned response. From the response, we get the content as a String using getEntity(), which is the JSON for our examples.</p>
        <p>So for the blackbird example in the <a href="../../Overview">Overview</a>, the code to get the JSON string from the NBN API would look like this:</p>

        <script type="syntaxhighlighter" class="brush: java; html-script: false">
            URI uri = new URI("https://data.nbn.org.uk/api/taxa/NHMSYS0000530674");
            Client client = Client.create();
            WebResource webResource = client.resource(uri);
            WebResource.Builder builder = webResource.getRequestBuilder();
            ClientResponse response = builder.accept("application/json").get(ClientResponse.class);
            String json = response.getEntity(String.class);
        </script>


        <p>In the <a href="../../Overview">Overview</a> the JSON representing a Taxon was shown. Below is the Taxon class in Java. Note that this class is a JavaBean with private properties, getter/setter methods and serializable. It has the same data fields as the JSON.</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
        package nbnapi;

        public class Taxon implements java.io.Serializable {
                private static final long serialVersionUID = 1L;

                private String taxonVersionKey;
                private String organismKey;
                private String name;
                private String authority;
                private String rank;
                private String taxonOutputGroupKey;
                private String taxonOutputGroupName;
                private String ptaxonVersionKey;
                private String commonNameTaxonVersionKey;
                private String commonName;
                private String href;
                private String languageKey;
                private String nameStatus;
                private String versionForm;
                private int gatewayRecordCount;

                public String getTaxonVersionKey() {
                        return taxonVersionKey;
                }
                public void setTaxonVersionKey(String taxonVersionKey) {
                        this.taxonVersionKey = taxonVersionKey;
                }

                public String getOrganismKey() {
                        return organismKey;
                }
                public void setOrgansimKey(String organismKey) {
                        this.organismKey = organismKey;
                }

                public String getName() {
                        return name;
                }
                public void setName(String name) {
                        this.name = name;
                }

                public String getAuthority() {
                        return authority;
                }
                public void setAuthority(String authority) {
                        this.authority = authority;
                }

                public String getRank() {
                        return rank;
                }
                public void setRank(String rank) {
                        this.rank = rank;
                }

                public String getTaxonOutputGroupKey() {
                        return taxonOutputGroupKey;
                }
                public void setTaxonOutputGroupKey(String taxonOutputGroupKey) {
                        this.taxonOutputGroupKey = taxonOutputGroupKey;
                }

                public String getTaxonOutputGroupName() {
                        return taxonOutputGroupName;
                }
                public void setTaxonOutputGroupName(String taxonOutputGroupName) {
                        this.taxonOutputGroupName = taxonOutputGroupName;
                }

                public String getPtaxonVersionKey() {
                        return ptaxonVersionKey;
                }
                public void setPtaxonVersionKey(String PtaxonVersionKey) {
                        this.ptaxonVersionKey = PtaxonVersionKey;
                }

                public String getCommonNameVersionKey() {
                        return commonNameTaxonVersionKey;
                }
                public void setCommonNameTaxonVersionKey(String commonNameTaxonVersionKey) {
                        this.commonNameTaxonVersionKey = commonNameTaxonVersionKey;
                }

                public String getCommonName() {
                        return commonName;
                }
                public void setCommonName(String commonName) {
                        this.commonName = commonName;
                }

                public String getHref() {
                        return href;
                }
                public void setHref(String href) {
                        this.href = href;
                }

                public String languageKey() {
                        return languageKey;
                }
                public void setLanguageKey(String languageKey) {
                        this.languageKey = languageKey;
                }

                public String getNameStatus() {
                        return nameStatus;
                }
                public void setNameStatus(String nameStatus) {
                        this.nameStatus = nameStatus;
                }

                public String getVersionForm() {
                        return versionForm;
                }
                public void setVersionForm(String versionForm) {
                        this.versionForm = versionForm;
                }

                public int getGatewayRecordCount() {
                        return gatewayRecordCount;
                }
                public void setGatewayRecordCount(int gatewayRecordCount) {
                        this.gatewayRecordCount = gatewayRecordCount;
                }
        }
        </script>
        <p>JSON can be converted into objects of a particular class using the ObjectMapper class in the Jackson JSON processer, which is available <a href="http://wiki.fasterxml.com/JacksonDownload" target="_blank">here</a>. We're using version 2.2.3. The ObjectMapper readValue method takes a json string and the class object of the type you want to convert to. It uses the class getter/setter methods to map to the JSON properties.</p>
        <p>For example, if we had a JSON string representing a single Taxon as in the overivew, it could be converted to an instance of the Taxon class like this:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
            Taxon taxon = new ObjectMapper().readValue(json, Taxon.class);
        </script>

        <p>For most of the examples, this is all we need to know to request resources from the NBN API and convert them to Java objects. In many cases, the only things that vary between each request are the URI and the class that the returned JSON needs to converted into.</p>
        <p>So, to avoid duplicating code, the NBN Client contains a generic method getJSON that takes the URI as parameter and, from this, it can create the request, read the response and return the JSON string.</p>
        <p>Here is the method:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
            private String getJSON(URI uri) {
                        Client client = Client.create();
                        WebResource webResource = client.resource(uri);
                        WebResource.Builder builder = webResource.getRequestBuilder();
                        ClientResponse response = builder.accept("application/json").get(ClientResponse.class);
                        String json = response.getEntity(String.class);
                        return json;
                }
        </script>
        <p>This method simplifies the public methods called by the web pages. For example GetViceCounties simply needs to call getJSON with the appropriate URI and then deserialize this into an array of SiteBoundary like this:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
            URI uri = new URI("https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries");
                String json = getJSON(uri);
                SiteBoundary[] data = new ObjectMapper().readValue(json, SiteBoundary[].class);
        </script>

        <h2>Section 2: Logging in and POSTing data</h2>
        <h2>POST requests</h2>
        <p>For many of the examples we just need to create GET requests to the NBN API, which means that the request parameters make up part of the URI of the request in the form of a querystring.</p>
        <p>In later examples, we will need to use HTTP POST requests, which can supply their querystrings as part of the request body without any restrictions on length. This can be useful when supplying a lot of data as part of the request (for example, Well Known Text representing a complex geometry). Or when you don't want the parameters being part of the URI (for example, your username and password when logging in).</p>
        <p>Making a POST request is very similar to making a GET request. The differences are that you call the post method and supply the name-value pairs of the querystring as an additional MultivaluedMap parameter to it. In our examples, the POST requests will also include the authentication cookie, from logging in, to retrieve species records.</p>
        <h2>Logging In</h2>
        <p>In later examples, where we request species records, we will need to log in to the API. If the login is successful, the NBN API returns a cookie that needs to be supplied with subsequent requests. Here is the relevant part of the NBNClient class:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
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
        </script>
        <p>Note the post method is used, which includes the username and password as a MultivaluedMap. If the response indicates that the request was successful, the authentication cookie received from the NBN API is stored in a private member variable of the NBNClient class, so it can be used for subsequent requests that require authentication.</p>
        <p>Use of the cookie in a POST request is illustrated in the postJSON method below:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
            private String postJSON(URI uri, MultivaluedMap<String, String> filters) {
                        Client client = Client.create();
                        WebResource webResource = client.resource(uri);
                        WebResource.Builder builder = webResource.getRequestBuilder();
                        builder = builder.cookie(authCookie);
                        ClientResponse response = builder.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, filters);
                        String json = response.getEntity(String.class);
                        return json;
                }
        </script>
        <p>This works on similar principles to the getJSON method, but with a little more work to create the request. This method can only be used after a successful login because it requires the authentication cookie. Note that the content type of the request is set to the MIME type "application/x-www-form-urlencoded". This indicates that the request parameters will be in the form of a querystring in the request body.</p>
        <p>The method takes a MultivaluedMap of filters as a parameter that, as we will see in later examples, may represents particular designations, geometry or date ranges.</p>
        <p>The response JSON String is returned to the calling public method and deserialized to an array of the appropriate class.</p>

        <h2>Further Development of the NBN Client</h2>
        <p>This version of the NBNClient illustrates the principles behind using the NBN API but it does not contain many safeguards that would be appropriate in a production environment. For example, it does not check that requests have been successful or whether a user has logged in before making a POST request. It might also be appropriate to store the authentication cookie between page requests in a Session variable to avoid multiple logins. These would all be simple additions to make to the class but have not been included here to keep the examples relatively simple.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
