<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Documentation under review

Please note that the documentation is still being reviewed at this time and as such some documentation may be out of date or missing, we apologise for this and are working to update the documentation right now!
            
#Introduction

The NBN Gateway web services have been built to allow biological data from the NBN Gateway to be used in websites and applications.

Prior to 2006 the NBN Gateway website was the only way you could interact on-line with the huge pot of biological data contained in its central database. However, NBN Gateway web services now expose these data in a very flexible way. This allows the data to not only be used in web sites, but also in the software you use on your desktop.

An important point is that the policies governing what access you have to different datasets on the NBN Gateway website apply equally to the web services.

Here are some examples of what these web services make possible:
* get a list and map of species recorded in your area of interest (defined by a custom polygon, 10km square or known boundary, e.g. an SSSI).
* get a list of BAP species with data on the NBN Gateway.
* create a grid map displaying the records for a species.
* get species records including date and location.
* review the taxonomy for a species or sub-species.

##REST Service API
You can view the current version of the API [here](resources/restapi/index.html)

You can download a copy of the API for the NBN Rest service [here](resources/restapi.zip)


##WADL - working with the web services
You can find the WADL description of the REST services [here](/api/application.wadl)



If you have any difficulties please visit the [NBN Web services forum](http://forums.nbn.org.uk/viewforum.php?id=15).
        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
