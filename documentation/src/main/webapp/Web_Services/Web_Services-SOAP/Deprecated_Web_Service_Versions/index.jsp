<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Overview
To make new functionality available to web service users and still be able to
support applications based on old versions of the NBN Gateway web service, the
NBN Gateway provides different versions of the web services.

Older versions of the web service will still be available for 12 months after
the release of a newer version, but we advise users to move to a new version as
soon as possible to make use of new functionality and bug fixes.

#Deprecated Versions

##NBN Gateway Web Services 3.4 - (Supported until 1st November 2011)
Url to WSDL: <http://www.nbnws.net/ws_3_4/ws/WSDL>  
Url to WSDL with digest authentication: <http://www.nbnws.net/ws_3_3/ws/secure/WSDL>  
Url to WSDL with digest authentication: <http://www.nbnws.net/ws_3_4/ws/secure/WSDL>    
Url to designation discovery service WSDL: <http://www.nbnws.net/ws_3_4/ws/resources/Resources?WSDL>  

##NBN Gateway Web Services 3.3 - (Supported until 1st April 2011)
Url to WSDL: <http://www.nbnws.net/ws_3_3/ws/WSDL>  
Url to WSDL with digest authentication: <http://www.nbnws.net/ws_3_3/ws/secure/WSDL>

##NBN Gateway Web Services 3.2 - (Supported until 1st September 2010)
Url to WSDL: <http://www.nbnws.net/ws/WSDL>
Url to WSDL with digest authentication: <http://www.nbnws.net/ws/secure/WSDL>

#Archive
Below are the realisation of the WSDLs of the deprecated web services in a few different languages.

##.Net DLL

* NBN Webservices .Net dll V.3.4: [Download GatewayWebservice 3 4 0.dll](resources/GatewayWebservice_3_4_0.dll)
* NBN Webservices .Net dll V.3.3: [Download GatewayWebservice 3 3.dll](resources/GatewayWebservice_3_3.dll)
* NBN Webservices .Net dll V.3.2.0: [Download GatewayWebservice 3 2 0.dll](resources/GatewayWebservice_3_2_0.dll)
* NBN Webservices .Net dll V.3.1.0: [Download GatewayWebservice 3 1 0.dll](resources/GatewayWebservice_3_1_0.dll)
* NBN Webservices .Net dll V.3.0.0: [Download GatewayWebServices.dll](resources/GatewayWebService.dll)

##Java

Here is an archive of client jars for accessing deprecated versions of the main
NBN web services. Unlike v3.5 and beyond, they use digest authentication rather
than registration keys, as documented [here](Secure_Web_Services):

The Taxonomy and Designation Web Services client are now bundled into the main NBN client
jar and not distributed separately.

* Java client api v3.4.0 [nbn-ws-client3-4-0.jar](resources/nbn-ws-client3-4-0.jar)
* Java client api v3.3.0 [nbn-ws-client3-3-0.jar](resources/nbn-ws-client3-3-0.jar)
* Java client api v3.2.0 [nbn-ws-client3-2-0.jar](resources/nbn-ws-client3-2-0.jar)
* Java client api v3.1.0 [nbn-ws-client3-1-0.jar](resources/nbn-ws-client3-1-0.jar)
* Java client api v3.0.0 [nbn-ws-client3-0-0.jar](resources/nbn-ws-client3-0-0.jar)

        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
