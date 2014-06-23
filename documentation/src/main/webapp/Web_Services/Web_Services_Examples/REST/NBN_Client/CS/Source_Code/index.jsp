<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBN Client Source Code C#</h1>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        using System;
        using System.Collections.Generic;
        using System.IO;
        using System.Net;
        using System.Web.Script.Serialization;
        using System.Text;

        namespace NBNAPI.REST
        {
            class NBNClient
            {
                private const string _cookieName = "nbn.token_key";
                private const string _cookieDomain = "nbn.org.uk";
                private string _authenticationToken;

                public bool Login(string username, string password)
                {
                    Uri uri = new Uri("https://data.nbn.org.uk/api/user/login");
                    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
                    request.Method = "POST";
                    //cookies are disabled by default so you have to create a cookie container
                    request.CookieContainer = new CookieContainer();

                    //add username and password to formdata of request
                    string formData = "username=" + username + "&password=" + password;
                    StreamWriter requestWriter = new StreamWriter(request.GetRequestStream());
                    requestWriter.Write(formData);
                    requestWriter.Close();

                    //check response and read the authentication token from the cookie
                    HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                    if (response.StatusCode == HttpStatusCode.OK && response.Cookies.Count > 0)
                    {
                        Cookie cookie = response.Cookies[_cookieName];
                        if (cookie != null)
                        {
                            _authenticationToken = cookie.Value;
                            return true;
                        }
                    }
                    return false;
                }

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

                public SiteBoundary[] GetViceCounties()
                {
                    Uri uri = new Uri("https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries");
                    return GetData<SiteBoundary>(uri);
                }

                public TaxonOutputGroup[] GetTaxonOutputGroups()
                {
                    Uri uri = new Uri("https://data.nbn.org.uk/api/taxonOutputGroups");
                    return GetData<TaxonOutputGroup>(uri);
                }

                public Designation[] GetDesignations()
                {
                    Uri uri = new Uri("https://data.nbn.org.uk/api/designations");
                    return GetData<Designation>(uri);
                }

                // identifier is the featureID for the query (not the featureID of the boundary)
                public TaxonWithQueryStats[] GetSiteSpecies(string featureID, string taxonOutputGroup, string designation)
                {
                    string uriString = String.Format("https://data.nbn.org.uk/api/taxonObservations/species?featureID={0}&taxonOutputGroup={1}", featureID, taxonOutputGroup);
                    if (!String.IsNullOrWhiteSpace(designation))
                    {
                        uriString = uriString + String.Format("&designation={0}", designation);
                    }
                    Uri uri = new Uri(uriString);
                    return GetData<TaxonWithQueryStats>(uri);
                }

                public TaxonDatasetWithQueryStats[] GetSiteDatasets(string featureID, string taxonOutputGroup, string designation)
                {
                    string uriString = String.Format("https://data.nbn.org.uk/api/taxonObservations/datasets?featureID={0}&taxonOutputGroup={1}", featureID, taxonOutputGroup);
                    if (!String.IsNullOrWhiteSpace(designation))
                    {
                        uriString = uriString + String.Format("&designation={0}", designation);
                    }
                    Uri uri = new Uri(uriString);
                    return GetData<TaxonDatasetWithQueryStats>(uri);
                }

                public TaxonWithQueryStats[] GetTenKmSpecies(string gridReference)
                {
                    Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxonObservations/species?gridRef={0}", gridReference));
                    return GetData<TaxonWithQueryStats>(uri);
                }

                public TaxonDatasetWithQueryStats[] GetTenKmDatasets(string gridReference)
                {
                    Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxonObservations/datasets?gridRef={0}", gridReference));
                    return GetData<TaxonDatasetWithQueryStats>(uri);
                }

                public SearchResult GetTaxonSearchResult(string query)
                {
                    Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa?q={0}", query));
                    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
                    HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    string json = reader.ReadToEnd();
                    JavaScriptSerializer serializer = new JavaScriptSerializer();
                    SearchResult data = serializer.Deserialize<SearchResult>(json);
                    return data;
                }

                public Taxon GetTaxonomy(string taxonVersionKey)
                {
                    Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa/{0}", taxonVersionKey));
                    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
                    HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    string json = reader.ReadToEnd();
                    JavaScriptSerializer serializer = new JavaScriptSerializer();
                    Taxon data = serializer.Deserialize<Taxon>(json);
                    return data;
                }

                public Taxon GetTaxonParent(string taxonVersionKey)
                {
                    Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa/{0}/parent", taxonVersionKey));
                    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
                    HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    string json = reader.ReadToEnd();
                    JavaScriptSerializer serializer = new JavaScriptSerializer();
                    Taxon data = serializer.Deserialize<Taxon>(json);
                    return data;
                }

                public Taxon[] GetTaxonParents(string taxonVersionKey)
                {
                    string tvk = taxonVersionKey;
                    List<Taxon> parents = new List<Taxon>();
                    Taxon parent;
                    do
                    {
                        parent = GetTaxonParent(tvk);
                        if (parent != null)
                        {
                            tvk = parent.taxonVersionKey;
                            parents.Add(parent);
                        }
                    } while (parent != null);
                    parents.Reverse();
                    return parents.ToArray();
                }

                public Taxon[] GetTaxonChildren(string taxonVersionKey)
                {
                    Uri uri = new Uri(String.Format("https://data.nbn.org.uk/api/taxa/{0}/children", taxonVersionKey));
                    return GetData<Taxon>(uri);
                }

                public TaxonObservation[] PostRecordsForDatasetAndGridRef(string datasetKey, string gridReference)
                {
                    Uri uri = new Uri("https://data.nbn.org.uk/api/taxonObservations");
                    Dictionary<string, string> filters = new Dictionary<string, string>();
                    filters.Add("datasetKey", datasetKey);
                    filters.Add("gridRef", gridReference);
                    return PostData<TaxonObservation>(uri, filters);
                }

                public TaxonObservation[] PostRecordsForWKTDesignationDatasetDates(string wkt, string designation, string datasetKey, int startYear, int endYear)
                {
                    Uri uri = new Uri("https://data.nbn.org.uk/api/taxonObservations");
                    Dictionary<string, string> filters = new Dictionary<string, string>();
                    filters.Add("polygon", wkt);
                    filters.Add("designation", designation);
                    filters.Add("datasetKey", datasetKey);
                    filters.Add("startYear", startYear.ToString());
                    filters.Add("endYear", endYear.ToString());
                    return PostData<TaxonObservation>(uri, filters);
                }
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
