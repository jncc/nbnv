<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Data Access Controls

##A guide to the controls and their effect upon the dataset records

Following extensive consultation with data providers and users, a new system of data access controls for the NBN Gateway will be released in October 2012. The current system has remained largely unchanged for over ten years, so this is a significant development which aims to increase the use of biodiversity data and give data providers greater flexibility in granting access to their data and better feedback on how their data are being used.

This [guidance](resources/NBN-Gateway-Access-Controls.pdf) provides an overview of the new access controls and helps you to visualise their effect upon individual records within the dataset to which they are applied.

Please visit the [NBN website](http://nbn.org.uk/) for more information about the changes to the data access controls.

        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
