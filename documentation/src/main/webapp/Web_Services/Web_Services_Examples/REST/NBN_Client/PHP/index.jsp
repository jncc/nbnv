<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>NBNClient PHP</h1>
        <a href="Source_Code" target="_blank"><h2>NBN Client Source Code</h2></a>

        <p>The NBN Client makes requests to the NBN API and converts the JSON into arrays of objects that can be displayed in JSP pages.</p>
        <p>This page describes all the methods in the NBNClient, including those for the more-advanced examples. The first section of this page describes the basic functionality of the NBNClient and should be read first. The second section describes functionality needed for later examples and can be read when needed.</p>
        <h2>Section 1: Basic Requests and Processing the JSON</h2>
        <h2>Making a request</h2>
        <p>To make requests to the NBN API, we will be using the libcurl package, which should be part of your PHP distribution. This is a library for connecting and communicating to many different types of servers.</p>
        <p>For requests, we need to specify the location of the resource. Then we initialize a cURL session, and set options for the URL and for cURL to return a string as a when curl_exec() is called. Finally, the request is executed and the cURL session closed.</p>
        <p>So for the blackbird example in the <a href="../../Overview.html">Overview</a>, the code to get the JSON string from the NBN API would look like this:</p>

        <script type="syntaxhighlighter" class="brush: php; html-script: false">
                $url = "https://data.nbn.org.uk/api/taxa/NHMSYS0000530674";
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                $json = curl_exec($ch);
                curl_close($ch);
        </script>

        <p>JSON can be converted into PHP objects of the same structure using json_decode. When the json has been converted into an object, it's properties can be easily accessed and used in a web page. Converting the JSON created above to a 'taxon' object is very simple: </p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
            $taxon = json_decode($json);
            //get the taxon name
            $taxonName = $taxon->name;
        </script>

        <p>For most of the examples, this is all we need to know to request resources from the NBN API and convert them to PHP objects. In most cases, the only thing that varies for each request is the URL.</p>
        <p>So, to avoid duplicating code, the NBN Client contains a generic method CurlGetString that takes the URL as parameter and, from this, it can create the request, read the response and return the JSON string.</p>
        <p>Here is the method:</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
            function CurlGetString($url)
            {
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                $output = curl_exec($ch);
                curl_close($ch);

                return $output;
            }
        </script>
        <p>This method simplifies the methods called by the web pages. For example GetViceCounties simply needs to call CurlGetString with the appropriate URL and then deserialize this into a PHP object like this:</p>
        <script type="syntaxhighlighter" class="brush: java; html-script: false">
            function GetViceCounties()
            {
                $url = "https://data.nbn.org.uk/api/siteBoundaryDatasets/GA000344/siteBoundaries";
                $json = $this->CurlGetString($url);
                return json_decode($json);
            }
        </script>

        <h2>Section 2: Logging in and POSTing data</h2>
        <h2>POST requests</h2>
        <p>For many of the examples we just need to create GET requests to the NBN API, which means that the request parameters make up part of the URI of the request in the form of a querystring.</p>
        <p>In later examples, we will need to use HTTP POST requests, which can supply their querystrings as part of the request body without any restrictions on length. This can be useful when supplying a lot of data as part of the request (for example, Well Known Text representing a complex geometry). Or when you don't want the parameters being part of the URI (for example, your username and password when logging in).</p>
        <p>Making a POST request is very similar to making a GET request. The differences are that you set the post option in cURL and supply the name-value pairs of the querystring as an additional associative array parameter to it. In our examples, the POST requests will also include an authentication cookie, from logging in, to retrieve species records.</p>
        <h2>Logging In</h2>
        <p>In later examples, where we request species records, we will need to log in to the API. If the login is successful, the NBN API returns an authentication code (and cookie) in its response. This authenticaiton code needs to be supplied in a cookie for subsequent requests. Here is the relevant part of the NBNClient class:</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
            private $token;

            function Login($username, $password)
            {
                $url = "https://data.nbn.org.uk/api/user/login";
                $fields_string = "username=".$username."&password=".$password;

                $ch = curl_init();

                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                curl_setopt($ch, CURLOPT_POST, 2);
                curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);

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
        <p>Note the post option is set and the number of fields that will be supplied (two: username and password). An authentication cookie comes back with the response but reading cookies can be a little awkward in PHP cURL. Fortunately, the authentication token is also supplied in the body of the response as JSON (as well as your user details). So this JSON can be decoded and the token assigned to our private member variable $token. This token can then be used to create an authentication cookie in subsequent requests. Note that trailing '=' signs are removed from the token in the response (I'm not sure why they are there).</p>
        <p>Use of the cookie in a POST request is illustrated in the CurlPostData method below:</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
            function CurlPostData($url, $postData)
            {
                $fields_string = "";
                foreach($postData as $key=>$value)
                {
                    $fields_string .= $key . "=" . $value . "&";
                }
                $fields_string = rtrim($fields_string, "&");

                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                curl_setopt($ch, CURLOPT_COOKIE, "nbn.token_key=".$this->token);

                curl_setopt($ch, CURLOPT_POST, count($postData));
                curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);

                $output = curl_exec($ch);
                curl_close($ch);

                return $output;
            }
        </script>
        <p>This works on similar principles to the CurlGetString method, but with a little more work to create the request. This method can only be used after a successful login because it requires the authentication cookie.</p>
        <p>The method takes an object as a parameter that, as we will see in later examples, can store filters for the web services. For example, a particular designation, geometry or date range.</p>
        <p>The response JSON String is returned to the calling public method and deserialized to a PHP object.</p>

        <h2>Further Development of the NBN Client</h2>
        <p>This version of the NBNClient illustrates the principles behind using the NBN API but it does not contain many safeguards that would be appropriate in a production environment. For example, it does not check that a user has logged in before making a POST request. It might also be appropriate to store the authentication token between page requests in a Session variable to avoid multiple logins. These would all be simple additions to make to the class but have not been included here to keep the examples relatively simple.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
