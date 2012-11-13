<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Overview
The secure NBN Web services allow you to log into the NBN Gateway web services
using your normal account details. This may give you greater access to datasets
on the Gateway.

#Example with the NBN Web Services java client API library
Here you can find the source code for a grid map request with digest authentication:
View JSP [source code](resources/ws-gridmapDigest.txt). This example works only with the java library
nbn-ws-client-3.0.0.jar and above.

#Example with the NBN Web Services .Net dll

Here is example [source code](resources/DigestExample.txt) showing how to use the NBN Gateway Webservices dll
with digest authentication in c#.
        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
