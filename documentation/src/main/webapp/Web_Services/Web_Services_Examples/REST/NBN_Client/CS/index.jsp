<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBNClient C#</h1>
        <a href="Source_Code" target="_blank"><h2>NBN Client Source Code</h2></a>
        <p>Source code for the data classes:</p>
        <ul>
            <li><a href="../Source/CS/TaxonWithQueryStats" target="_blank">TaxonWithQueryStats and Taxon</a></li>
            <li><a href="../Source/CS/TaxonDatasetWithQueryStats" target="_blank">TaxonDatasetWithQueryStats and TaxonDataset</a></li>
            <li><a href="../Source/CS/TaxonOutputGroup" target="_blank">TaxonOutputGroup</a></li>
            <li><a href="../Source/CS/SiteBoundary" target="_blank">SiteBoundary</a></li>
            <li><a href="../Source/CS/SearchResult" target="_blank">SearchResult and Result</a></li>
            <li><a href="../Source/CS/TaxonObservation" target="_blank">TaxonObservation</a></li>
            <li><a href="../Source/CS/Designation" target="_blank">Designation</a></li>
        </ul>

        <p>The NBN Client makes requests to the NBN API and converts the JSON into simple data classes that can then be displayed easily in an ASP.NET Web Forms page.</p>
        <p>This page describes all the methods in the NBNClient, including those for the more-advanced examples. The first section of this page describes the basic functionality of the NBNClient and should be read first. The second section describes functionality needed for later examples and can be read when needed.</p>
        <h2>Section 1: Basic Requests and Processing the JSON</h2>
        <h2>Making a request</h2>   
        <p>To make a request, we need to specify the location of the resource as a URI. Then we create a .NET HttpWebRequest object with this URI. When created, the GetResponse method can be called. This sends the request to the server and returns the reponse. From the response, we can get a stream that can be used to read the body of the response, which is JSON in our examples.</p>
        <p>So for the blackbird example in the <a href="../../Overview">Overview</a>, the code to get the JSON string from the NBN API would look like this:</p>

        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
            Uri uri = new Uri("https://data.nbn.org.uk/api/taxa/NHMSYS0000530674");
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            StreamReader reader = new StreamReader(response.GetResponseStream());
            string json = reader.ReadToEnd();
        </script> 

        <h2>Converting the JSON string into a class</h2>
        <p>In the <a href="../Overview">Overview</a> the JSON representing a Taxon was shown. Below is a Taxon class in C#. Note that this class has properties with the same names as the keys in the JSON for a taxon.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
        namespace NBNAPI.REST
        {
                public class Taxon
                {
                        public string taxonVersionKey { get; set; }
                public string organismKey { get; set; }
                        public string name { get; set; }
                        public string authority { get; set; }
                public string rank { get; set; }
                public string taxonOutputGroupKey { get; set; }
                public string taxonOutputGroupName  { get; set; }
                public string ptaxonVersionKey { get; set; }
                public string commonNameTaxonVersionKey { get; set; }
                public string commonName { get; set; }
                public string href { get; set; }
                        public string languageKey { get; set; }
                public string nameStatus { get; set; }
                        public string versionForm { get; set; }
                        public int gatewayRecordCount { get; set; }
                }
        }
        </script>
        <p>In .NET, JSON can be converted into objects of a particular class using the JavaScriptSerializer, which is available in the System.Web.Script.Serialization namespace. JavaScriptSerializer has a generic Deserialize method that takes JSON as a string parameter and returns an instance of the type specified. It does this by mapping the property names of the class to the key names of the JSON.</p>
        <p>For example, if we had a JSON string representing a single Taxon as in the overivew, it could be converted to an instance of the Taxon class like this:</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
            JavaScriptSerializer serializer = new JavaScriptSerializer();
            Taxon taxon = serializer.Deserialize<Taxon>(json);
        </script>

        <p>For most of the examples, this is all we need to know to request resources from the NBN API and convert them to .NET types. In many cases, the only things that vary between each request are the URI and the class that the returned JSON needs to converted into.</p>
        <p>So, to avoid duplicating code, the NBN Client contains a generic method GetData that takes the URI as parameter and the class as generic parameter. From this, it can create the request, read the response and deserialize it into an array of the type we specify.</p>
        <p>Here is the method:</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
            // Generic method to GET and deserialize data at uri.
            private T[] GetData<T>(Uri uri)
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                StreamReader reader = new StreamReader(response.GetResponseStream());
                string json = reader.ReadToEnd();
                JavaScriptSerializer serializer = new JavaScriptSerializer();
                if (json.Length > serializer.MaxJsonLength) serializer.MaxJsonLength = json.Length;
                T[] data = serializer.Deserialize<T[]>(json);
                return data;
            }
        </script>
        <p>Note that this method returns an array of the class we specify. Since we will mainly be requesting lists from the NBN API (for example all vice counties, a list of taxa for a site etc.). Also, the JSON response can be longer than the default maximum length allowed by the JavaScriptSerializer. So the length is tested, and if it exceeds the default maximum, the maximum length is updated to the length of the JSON string.</p>
        <p>This method greatly simplifies the public methods called by the web pages. For example GetViceCounties simply needs to call GetData specifying that the class is a SiteBoundary and the URI.</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
            public SiteBoundary[] GetViceCounties()
            {
                Uri uri = new Uri("https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries");
                return GetData<SiteBoundary>(uri);
            }
        </script>

        <h2>Section 2: Logging in and POSTing data</h2>
        <h2>POST requests</h2>
        <p>By default, the HttpWebRequest creates an HTTP GET request, which means that the request parameters make up part of the URI of the request in the form of a querystring.</p>
        <p>In later examples, we will need to use HTTP POST requests, which can supply their querystrings as part of the request body without any restrictions on length. This can be useful when supplying a lot of data as part of the request (for example, Well Known Text representing a complex geometry). Or when you don't want the parameters being part of the URI (for example, your username and password when logging in).</p>
        <p>Making a POST request is very similar to the default GET request. The differences are that the request Method property is set to "POST" and the querystring needs to be written to the body of the request instead of making up part of the URI. In our examples, the POST requests will also include the authentication cookie, from logging in, to retrieve species records. So there's a bit more work making these requests.</p>
        <h2>Logging In</h2>
        <p>In later examples, where we request species records, we will need to log in to the API. If the login is successful, the NBN API returns a cookie that needs to be supplied with subsequent requests. Here is the relevant part of the NBNClient class:</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
            private $token;

            function Login($username, $password)
            {
                $url = "https://data.nbn.org.uk/api/user/login?username=".$username."&password=".$password."&remember=true";

                $ch = curl_init();

                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                $json = curl_exec($ch);
                if($json === FALSE)
                {
                    echo "<p>Error: ".curl_error($ch)."</p>";
                }
                curl_close($ch);

                $response = json_decode($json);
                $this->token = rtrim($response->token, "=");
            }
        </script>
        <p>When creating the request, the request method is set to "POST", A CookieContainer is added to store the cookie we should receive from the NBN API, and the username and password are written to the request body (instead of being part of the URI).</p>
        <p>If the response indicates that the request was successful, the authentication token received from the NBN API is stored in a private member variable of the NBNClient class, so it can be used for subsequent requests that require authentication.</p>
        <p>Use of the token in a POST request is illustrated in the generic method below:</p>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false">
            // Generic method to POST and deserialize data at uri, with optional filters.
            private T[] PostData<T>(Uri uri, Dictionary<string, string> filters)
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
                request.Method = "POST";
                request.CookieContainer = new CookieContainer();
                request.CookieContainer.Add(new Cookie(_cookieName, _authenticationToken, "/", _cookieDomain));
                request.ContentType = "application/x-www-form-urlencoded";

                if (filters != null && filters.Count > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    foreach (KeyValuePair<string, string> kvp in filters)
                    {
                        sb.Append(kvp.Key + "=" + kvp.Value + "&");
                    }
                    //remove trailing &
                    sb.Length = sb.Length - 1;
                    string postString = sb.ToString();

                    StreamWriter requestWriter = new StreamWriter(request.GetRequestStream());
                    requestWriter.Write(postString);
                    requestWriter.Close();
                }

                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                StreamReader responseReader = new StreamReader(response.GetResponseStream());
                string json = responseReader.ReadToEnd();
                responseReader.Close();
                request.GetResponse().Close();
                JavaScriptSerializer serializer = new JavaScriptSerializer();
                if (json.Length > serializer.MaxJsonLength) serializer.MaxJsonLength = json.Length;
                T[] data = serializer.Deserialize<T[]>(json);
                return data;
            }
        </script>
        <p>This works on similar principles to the GetData method, but with a little more work to create the request. This method can only be used after a successful login because a Cookie is created from the authentication token. Note that the content type of the request is set to the MIME type "application/x-www-form-urlencoded". This indicates that the request parameters will be in the form of a querystring in the request body.</p>
        <p>The method takes a Dictionary of filters as a parameter that, as we will see in later examples, may represents particular designations, geometry or date ranges. This dictionary is written to the request body in the querystring format.</p>
        <p>When all this is done, the rest of the method is the same as GetData. The response JSON is written to a string and deserialized to an array of the appropriate class.</p>

        <h2>Further Development of the NBN Client</h2>
        <p>This version of the NBNClient illustrates the principles behind using the NBN API but it does not contain many safeguards that would be appropriate in a production environment. For example, it does not check that requests have been successful or whether a user has logged in before making a POST request. It might also be appropriate to store the authentication cookie between page requests in a Session variable to avoid multiple logins. These would all be simple additions to make to the class but have not been included here to keep the examples relatively simple.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
