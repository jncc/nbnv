<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h2>The NBN Client Class</h2>
        <p>Each example will involve making requests to the NBN web services, translating the data from the response into appropriate objects, and displaying the results. For examples that display individual observations, we will also need to log in to the NBN and store the authentication details for future requests. The code for many of these tasks will be very similar between examples. So to minimize duplication we will create a class that the web pages can call, which manages all the routine tasks. The NBN Client acts as a go-between between the NBN web services and the web page and hides all of the details of making requests to the API from the page.
        The diagram below shows how this works There is the NBN API that acts as the source of the data, the NBN client, which we will write, that queries and processes the data, and a web page that displays controls and presents the results.</p>
        <img src="../img/NBNClient.png" alt="NBNClient" title="NBNClient" />
        <p>The public interface of the NBN Client is almost the same for each language. Most of the methods are of the form GetX or PostX where X is the kind of data requested. For example, GetViceCounties returns a list of all vice counties on the NBN in an appropriate format for the page to display in a control or table. It does this by creating the correct URI for the resource, making a request with this URI and processing the response returned.</p>
        <p>In the C# and Java versions of the NBN Client, the JSON is converted into instances of simple, pre-defined data classes, for example Taxon, Designation or SiteBoundary. The PHP examples simply convert the JSON into a PHP object, which has properties corresponding to the keys in the JSON.</p>
        <p>The NBN Client also contains a login method where you pass in your username and password as parameters. Authentication with the NBN API is managed with cookies. So when the login method is called, the NBN client attempts to log in with the supplied username and password and stores the returned cookie if that request was successful. This cookie is used in subsequent requests where authentication is required e.g. to download species records.</p>
        <p>Note that in the supplied source code, username and password is replaced with placeholders. You will need to supply valid credentials for the examples to work. In a production environment, these would probably be supplied by the user or settings in a configuration or properties file.</p>
        <p>The implementation of the NBN Client is described in full for each language below.</p>
        <a href="CS"><h2>C# Example</h2></a>
        <a href="Java"><h2>Java Example</h2></a>
        <a href="PHP"><h2>PHP Example</h2></a>
    </jsp:attribute>
</t:webserviceDocumentationPage>
