<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Records for a Ten Km Square PHP</h1>
        <a href="Source_Code" target="_blank"><h2>Page Source Code</h2></a>

        <p>This page simply logs the user in (note you need to replace the placeholders with a real username and password) and then calls the NBNClient method PostRecordsForDatasetAndGridRef. The data returned is then output to a table.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: true">
        <?php
        require_once "NBNClient.php"; 

        $nbnClient = new NBNClient();
        $nbnClient->login("#USERNAME#", "#PASSWORD#");
        $records = $nbnClient->PostRecordsForDatasetAndGridRef("GA000466", "TL28");
        ?>
        </script>
        <p>For this example, the request is POSTed to the server (see thie discussion of this in the NBNClient description) in anticipation of the next examples that require this method to work properly with a long WKT string.</p>
        <script type="syntaxhighlighter" class="brush: php; html-script: false">
        function PostRecordsForDatasetAndGridRef($datasetKey, $gridReference)
        {
            $url = "https://data.nbn.org.uk/api/taxonObservations";
            $postData = array();
            $postData["datasetKey"] = $datasetKey;
            $postData["gridRef"] = $gridReference;
            $json = $this->CurlPostData($url, $postData);
            return json_decode($json);
        }
        </script>
        <p>Instead of attaching the filters directly as a querystring to the URI, they are passed in as an array to the CurlPostData method of the NBNClient, which creates the form data for the request.</p>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
