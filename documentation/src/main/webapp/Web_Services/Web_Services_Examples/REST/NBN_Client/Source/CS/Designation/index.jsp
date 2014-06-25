<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage showContentDiv="false">
    <jsp:attribute name="body">
        <h1>Designation Source Code C#</h1>
        <script type="syntaxhighlighter" class="brush: csharp; html-script: false"><![CDATA[
        namespace NBNAPI.REST
        {
            public class Designation
            {
                public int id { get; set; }
                public string name { get; set; }
                public string label { get; set; }
                public string code { get; set; }
                public int designationCategoryID { get; set; }
                public string description { get; set; }
                public string href { get; set; }
            }
        }
        ]]></script>

        <script>SyntaxHighlighter.defaults.toolbar = false; SyntaxHighlighter.all();</script>
    </jsp:attribute>
</t:webserviceDocumentationPage>
