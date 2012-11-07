<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Web Services Documentation

Welcome to the NBN Gateways Web Services Documentation, here you will be able to find
information regarding the various web services which the NBN Gateway provides.

Please use the navigation bar on the left to view the services of interest.
        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>