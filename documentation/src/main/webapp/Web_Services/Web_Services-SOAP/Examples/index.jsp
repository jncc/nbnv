<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Introduction

The NBN Gateway has provided a variety of sample source code and tutorial workshop
literature which can be used to help you develop applications which use data from
the NBN Gateways web services.

#Applications of the NBN Web services
Below is a list of applications which are based upon the NBN Gateway web service.
Please note that the NBN Gateway is not responsible for the content of these pages.

* [National Grid map: National Moth Recording Scheme](http://www.mothscount.org/maps/94/NBN_Maps.html)

    This returns a distribution map of moth species from records taken from Butterfly Conservation’s
    Macro-moth provisional distribution for the British Isles (excluding Republic of Ireland)
    from the National Moth Recording Scheme dataset on the NBN Gateway

* [Local Grid map: Bedfordshire Natural History Society](http://www.bnhs.org.uk/)

    This returns metadata and local distribution map of species from records taken from
    datasets on NBN Gateway submitted by Bedfordshire and Luton Biodiversity Recording
    and Monitoring Centre.

* [One Site data (national): SNHi](http://www.snh.gov.uk/publications-data-and-research/snhi-information-service/map/)

    This uses the one site web service to return records for species sightings in the Scotland.

* [One site data (local): Natural Shropshire](http://www.naturalshropshire.org.uk/SpeciesGroups/SPECIESBIODIVERSITYMAP/tabid/44/Default.aspx)

    Return species list for 2km square within Shropshire

* [Taxonomy and species list web service: North East Scotland Biological Records Centre](http://www.nesbrec.org.uk/modules/biomaps/index.php)

    Uses web services to return species information from the NBN dictionary as used on the NBN Gateway


        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>