<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Overview
Below shows the realisation of the WSDL in a few different languages.

#.net
##NBN Web Services .Net dll
Download the most recent version here: Download [GatewayWebservice 3 5.dll](resources/GatewayWebservice_3_5.dll).

This dll was built using .Net 2.0. It connects to the nbn data web services at http://www.nbnws.net/ws_3_5/ws/webservice/.

##Getting Started - Installing the DLL

* [Download the DLL](resources/GatewayWebservice_3_5.dll)

* Place the DLL in the Bin directory of your ASP.NET project (if this is not
    visible right click on the website name in Solution Explorer in Visual Studio
    & Add ASP.NET folder)

* Once the DLL is in the Bin directory, you can import it in the code behind.
    The namespace is: net.searchnbn.webservice

    * You may find it makes your code more readable if you give the import a useful
        name e.g. Imports NBN = net.searchnbn.webservice which will then allow you
        to refer to NBN classes and objects using

        Dim areaType As New NBN.AreaType()  
        areaType.units = NBN.AreaUnit.SquareMetres

Alternatively you may save the DLL elsewhere and add a reference to it in your project.
To do this in Visual Studio right click on website name, Add reference, Browse to where
you saved the DLL and click OK. You will then be able import as described in point 3 above.

##FAQ for Versions 3.3 and 3.4

##Why isn't the Gateway applying the filters I put on the request?

There could be several reasons for this, the most common are listed below. If you
have tried all the suggestions, we recommend that you write out the request object
and examine the XML that it is actually sending to the NBN Gateway. A method of
doing this is shown in the .Net Example project, available for download.

Possible reasons for not applying filter:

###The matching boolean parameter not set

Many parameters have a paired boolean parameter.
This boolean must be set to True if the parameter is to be applied E.g. a GeographicalFilter
has a MinimumResolution, if this is to be applied MinimumResolutionSpecified must be set to true.

myGeographicalFilter.MinimumResolution = NBN.Resolution.\_1km  
myGeographicalFilter.MinimumResolutionSpecified = True

###YearFilter
If you are using a YearFilter, it may not apply in the way you expect. Click [here](../Resources/resources/DateFiltering.pdf) for details

###You do not have access to the level of detail you are requesting
Some datasets are restricted, some completely and some are only publicly
available at some resolutions. For example: If you request a dataset at 100m
resolution, but it is not publicly available at this level you will get the
resolution that is publicly available or the resolution your account has access to.

If you have a reason for requiring restricted data please contact the NBN.

###The projection system isn't known, you provided

You get error: The projection system isn't known, you provided '', the only values
allowed are 'EPSG\_27700', 'EPSG\_29903', 'osgb\_BNG and osni\_ING when applying a
GeographicalFilter

An error in the .Net dll (version 3.3) causes the projection to be set to an
empty string when the value is EPSG:27700. As a workaround set the projection
to osgb\_BNG, which is an alternative value for the EPSG:27700 projection.

##How do I create a PolygonBoundary Ring?
A PolygonBoundary Ring is an array of Coordinates. As Coordinate is an abstract
class, you must create an array of a class which inherits from Coordinates, such
as Point.

#Java /JSP
##NBN Web Services java client API
The NBN Web Services java client provides access to all the NBN web services.
It was built using JAX-WS. Download the most recent version here:
[nbn-ws-client3-5-0.jar](resources/nbn-ws-client3-5-0.jar). Version 3.5 and beyond use registration keys to track
your usage and give you access to data, more information about this can be found [here](../Registration).
        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
