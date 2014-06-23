<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>SearchResult and Result Source Code C#</h1>
        <h2>SearchResult</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        namespace NBNAPI.REST
        {
            public class SearchResult
            {
                public Result[] results { get; set; }
                public Header header { get; set; }
            }
        }
        ]]></script>
        <h2>Result</h2>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        namespace NBNAPI.REST
        {
            public class Result
            {
                public string entityType { get; set; }
                public string searchMatchTitle { get; set; }
                public string descript { get; set; }
                public string pExtendedName { get; set; }
                public string taxonVersionKey { get; set; }
                public string name { get; set; }
                public string authority { get; set; }
                public string languageKey { get; set; }
                public string taxonOutputGroupKey { get; set; }
                public string taxonOutputGroupName { get; set; }
                public string commonNameTaxonVersionKey { get; set; }
                public string commonName { get; set; }
                public string organismKey { get; set; }
                public string rank { get; set; }
                public string nameStatus { get; set; }
                public string versionForm { get; set; }
                public int gatewayRecordCount { get; set; }
                public string href { get; set; }
                public string ptaxonVersionKey { get; set; }
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
