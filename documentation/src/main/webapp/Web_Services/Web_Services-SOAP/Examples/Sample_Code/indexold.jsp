<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Introduction

The NBN Gateway has created some example usages of some of the web services.
The examples we provide are categorised by programming language. All the source code
for these examples are provided.

#Java/JSP

##[Dataset Summary List](../Actual_Services/Dataset_Summary_List)

* [Metadata for a dataset](java/ws-dataset.jsp?dsKey=GA000183). Uses a dataset key to lookup metadata for a dataset. View JSP [source code](java/ws-dataset.zip).


##[Grid Map](../Actual_Services/Grid_Map)

* Simple grid map for [White Beak-sedge](java/ws-gridmap.jsp?tvk=NBNSYS0000002407). View JSP [source code](java/ws-gridmap.zip).
* Grid map using data classifications for [Harlequin Ladybird](java/ws-harlequinGridmap.jsp?tvk=NHMSYS0000712592). View JSP [source code](java/ws-harlequinGridmap.zip).
* Grid map zoomed to Vice County with OS backdrop for [Pipistrelle Bat](java/ws-gridmapVC.jsp?tvk=NBNSYS0000005099). View JSP [source code](java/ws-gridmapVC.zip).

##[One Species Data](../Actual_Services/One_Species_Data)

* [10km square](java/ws-tenkmSpeciesData.jsp?tvk=NHMSYS0000080218&square=TL18). Returns data for a single species from a 10km square. View [JSP source](java/ws-tenkmSpeciesData.zip) code.
* [User defined point/buffer](java/ws-pointBufferData.jsp). Returns data for BAP species in a user defined point/buffer. View [JSP source code](java/ws-pointBufferData.zip).

##[Site Boundary List](../Actual_Services/Site_Boundary_List)

* [Known Site List Lookup](java/ws-knownSiteList.jsp). Looks up the list of sites for a site type. View [JSP source code](java/ws-knownSiteList.zip).

##[Site Boundary Name](../Actual_Services/Site_Boundary_Name)

* [Known Site name](java/ws-knownSiteLookup.jsp). Looks up the name of a site using the site key and provider key. View JSP [source code](java/ws-knownSiteLookup.zip).

##[Species List](../Actual_Services/Species_List)

* [10km square](java/ws-tenkmSpeciesList.jsp). Create a species group list for a 10km square. View JSP [source code](java/ws-tenkmSpeciesList.zip).
* [Known Site](java/ws-knownSiteSpeciesList.jsp?desig=NONE&tgk=NHMSYS0000080067&dsKey=GA000374&siteKey=101). Create a species group list for a known site. View JSP [source code](java/ws-knownSiteSpeciesList.zip).
* [User defined area](java/ws-speciesListUserPoly.jsp). Demonstrates the use of polygon, point, point buffer and rectangle user defined areas. View JSP [source code](java/ws-speciesListUserPoly.zip).

##[Taxon Reporting Category List](../Actual_Services/Taxon_Reporting_Category_List)

* [10km square](java/ws-tenkmGroupList.jsp). Lets you create a list for a 10km square. View JSP [source code](java/ws-tenkmGroupList.zip).
* [Known Site](java/ws-knownSiteGroupList.jsp). Lets you create a list for a. View JSP [source code](java/ws-knownSiteGroupList.zip).

##[Taxon Reporting Category Name](../Actual_Services/Taxon_Reporting_Category_Name)

* [Taxon Reporting Category Lookup](java/ws-taxCatLookup.jsp). Looks up the name of a Taxon Reporting Category using its key. View JSP [source code](java/ws-taxCatLookup.zip).

##[Taxonomy Species Search](../Actual_Services/Taxonomy_Species_Search)

* [Taxonomy Search](java/ws-taxonomySearch.jsp). Returns Taxonomic information for a specified search term or a taxon version key. View JSP [Source code](java/ws-taxonomySearch.zip).

#PHP / NuSoap

##[Dataset Summary List](../Actual_Services/Dataset_Summary_List)

*Metadata for a Dataset [example code](php/ws-dataset.php). Uses a dataset key to lookup metadata for a dataset.

##[Grid Map](../Actual_Services/Grid_Map)
The following are a set of php examples which use the Grid Map web service.

* [Species Group List](php/GetSpeciesList.php)
* [Site Data](php/GetSiteData.php)
* [Grid Map](php/GetGridMap.php)
* [Species List for Site](php/GetSpeciesListForSite.php)
* [Get Species Key](php/GetTaxonomy.php)

##[Site Boundary List](../Actual_Services/Site_Boundary_List)

* NBN Gateway Known Site List Web Service, [PHP Example](php/ws-knownSiteList.php)

##[Site Boundary Name](../Actual_Services/Site_Boundary_Name)

* NBN Gateway Known Site Name Web Service, [PHP Example](php/ws-knownSiteLookup.php)

##[Species List](../Actual_Services/Species_List)

* 10km square [example code](php/ws-tenkmSpeciesList.php). Lets you create a list for a 10km square.
* Known Site [example code](php/ws-siteSpeciesList.php). Lets you create a list for a known site.
* Point and buffer [example code](php/ws-bufferSpeciesList.php). Lets you create a list for user defined point and buffer.

##[Taxon Reporting Category List](../Actual_Services/Taxon_Reporting_Category_List)

* 10km square [example code](php/ws-tenkmGroupList.php). Lets you create a list for a 10km square.
* Known Site [example code](php/ws-siteGroupList.php). Lets you create a list for a known site.
* Point and buffer [example code](php/ws-bufferGroupList.php). Lets you create a list for user defined point and buffer.

##[Taxon Reporting Category Name](../Actual_Services/Taxon_Reporting_Category_Name)

* NBN Gateway Taxon Reporting Category Name Web Service, [PHP Example](php/ws-taxCatLookup.php)

        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>