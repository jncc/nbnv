<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h2>NBN Gateway REST web services and JSON</h2>
        <p>
            The NBN Gateway web services allow applications to directly access data and maps held on the Gateway in a very straightforward way. Many of them follow a RESTful architecture which means that the resources on the Gateway all have their own unique locations, identified by their URL or web address (more generally, a URI). This simple idea is very powerful and this tutorial will explore many of the ways the resources can be used. Applications that use web services are called clients and one client that anyone can use for the Gateway web services is a web browser. For example, if you wanted to retrieve the taxonomic information for blackbird, you use the taxon resource of the web services and supply blackbird&#39;s taxon version key as part of the URL in your browser like this:</p>
        <p>
            <a href="https://data.nbn.org.uk/api/taxa/NHMSYS0000530674" target="_blank">https://data.nbn.org.uk/api/taxa/NHMSYS0000530674</a></p>
        <p>
            If you click on this link you will be able to see the data returned from the web service on the Gateway. It's in a format called JSON. This simple format is readable by people but it can be easily processed by computer applications too, as we'll demonstrate throughout the course of this tutorial. If we reformat the JSON a little, for example by pasting it into the JSON Formatter and Validator at <a href="http://jsonformatter.curiousconcept.com/" target="_blank">Curious Concept</a>, the structure should be more obvious:</p>

        <script type="syntaxhighlighter" class="brush: javascript; html-script: false"><![CDATA[
            {
               "taxonVersionKey":"NHMSYS0000530674",
               "name":"Turdus merula",
               "authority":"Linnaeus, 1758",
               "languageKey":"la",
               "taxonOutputGroupKey":"NHMSYS0000080039",
               "taxonOutputGroupName":"bird",
               "commonNameTaxonVersionKey":"NBNSYS0000189598",
               "commonName":"Common Blackbird",
               "organismKey":"NBNORG0000061050",
               "rank":"Species",
               "nameStatus":"Recommended",
               "versionForm":"Well-formed",
               "gatewayRecordCount":719219,
               "href":"https://data.nbn.org.uk/Taxa/NHMSYS0000530674",
               "ptaxonVersionKey":"NHMSYS0000530674"
            }
        ]]></script>
        <p>We can read from the web service resource the taxon version key, scientific name, and authority for blackbird as well as a range of other information including the link on the NBN Gateway for its taxonomy page.</p>
        <p>JSON consists of an object in the form of key-value pairs. Keys and values are surrounded by double quotes and separated by a colon. The pairs are separated by commas and the entire JSON object is surrounded by curly braces. The structure can be more complex than this, but properly formatted, it still remains relatively easy to read. If you have used the older, SOAP Gateway web services in the past, you will appreciate that JSON is a much more compact format than the XML that was returned by these services.</p>

        <h3>Summary</h3>
        <p>The RESTful web services on the NBN Gateway are represented as resources with fixed URIs and data is returned in the JSON format. The structure of the JSON reflects the type of data returned from that URI. For testing purposes, your web browser can be very useful to check the response from the web services for a resource location. For the rest of these tutorials we will not need to worry too much about the JSON format since most programming languages are able to process the JSON and present it in a way that it can be easily queried in code.</p>

        <h3>Examples</h3>
        <p>Each example of the Gateway web services is illustrated with a C# ASP.NET Web Form, a PHP page and Java JSP. The code is kept as simple as possible to highlight use of the RESTful API with minimal use of libraries and web development frameworks. They are not meant to be production code, which would include validation, exception handling and security considerations.</p>
        <p>It will soon become apparent that logging in to the Gateway, creating the request, getting the reponse and processing the JSON could result in a lot of duplicate code between examples. So a generalised NBNClient class will be created for each platform that manages these routine tasks. After a review of this class, the examples will concentrate on the new methods introduced to the NBNClient that affect the web page functionality.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
