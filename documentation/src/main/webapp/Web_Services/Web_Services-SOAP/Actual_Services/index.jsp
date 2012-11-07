<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Web Services

## A guidebook for those interested in the technical side of the NBN and the Gateway

![NBN web services graphic](images/NBN-webservices-graphic.png)

As well as accessing the data available through the NBN via the website
the NBN also provides a set of ‘web services’ to access and use the data.
These essentially allow you to integrate the data directly into other websites or
applications. So, for example, you could embed a distribution map of a species
directly within your website perhaps with some text or images of the species.
Find out more about web services and how they work.

### The Services

There are currently 14 web services available. These broadly break down into data services and lookup services.

* [Dataset Summary List](Dataset_Summary_List)	This web service provides metadata and access information for one or more datasets on the NBN Gateway. It does not provide the actual data. This can be used, for example, to find out which datasets have data for a species or the metadata of a particular dataset.
* [Designation List](Designation_List)	This web service lists all the Designations. It provides their name and a key that can be used in other webservices that allow species designation filters eg One site data request.
* [Grid Map](Grid_Map)	A grid map image showing the distribution of species records. It can be customised to show either all Great Britain and Ireland or specific counties. Different overlays and backgrounds can be specified. It allows date classes to be applied to show recent and older records.
* [Habitat Discovery](Habitat_Discovery)	Enables you to look up a list of habitat layers available on the NBN Gateway with their dataset details.
* [Habitat Query](Habitat_Query)	Intersects the chosen habitat layers with the user input polygon and returns habitat and dataset details as well as the raw polygon data clipped to the user input polygon.
* [One Site Data](One_Site_Data)	Gets all the raw data for a single site. A site can be a custom polygon or a known site (10km square or boundary (e.g. SSSI)). A number of different filters can be set including year range, dataset list, species designations, species keys and species group keys. A map can optionally be requested.
* [One Species Data](One_Species_Data)	Gets all the raw data for a single species. It doesn't allow you to specify an area of interest, but does allow year ranges and dataset lists to be applied.
* [One Species Location Data](One_Species_Location_Data)	This web service is intended to allow you to retrieve the locations in which records occur, at a specified resolution, rather than the detailed records themselves. This is useful for mapping and analysis purposes.
* [Site Boundary Discovery](Site_Boundary_Discovery)	Enables you to look up a list of Site Boundary layers available on the NBN Gateway with their dataset details.
* [Site Boundary List](Site_Boundary_List)	Enables you to look up a list of site names and IDs for a site type, such as a SSSI or Vice County, using a site type code.
* [Site Boundary Name](Site_Boundary_Name)	Enables you to look up the name for a site, such as a SSSI or Vice County, using the site ID and dataset provider key.
* [Site Boundary Query](Site_Boundary_Query)	Intersects the chosen Site Boundary layers with the user input polygon and returns Site Boundary and dataset details as well as the raw polygon data clipped to the user input polygon.
* [Species Density Data](Species_Density_Data)	This web services enables you to look at data on the NBN Gateway in terms of species richness and record density.
* [Species List](Species_List)	Gets a list of species meeting some conditions. The conditions are designation (e.g. BAP), area of interest, year range and dataset list.
* [Taxon Reporting Category List](Taxon_Reporting_Category_List)	Gets a list of species groups meeting some conditions. The conditions are designation (e.g. BAP), area of interest, year range and dataset list. A map image of your area of interest can also be requested.
* [Taxon Reporting Category Name](Taxon_Reporting_Category_Name)	Enables you to look up the name for a taxon reporting category using its ID.
* [Taxonomy & Species Search](Taxonomy_and_Species_Search)	Lets you search for species by scientific or common name or by NHM taxon version key. The web service returns a list of matching species and for each gives information on synonymy, lower taxa, segretates and preferred taxon version key.


### Getting started and help

Each of the web services is fully documented through the links above (including
a range of examples). There are also some more general examples of .NET and Java
implementations. If you encounter problems please check the NBN Forum for potential
solutions or to raise new issues.
        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
