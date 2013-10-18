<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Exploring a site or 10KM grid square

The NBN Gateway provides a reporting page for each site provided by [organisations](/Organisations) and each 10KM grid square. On this site report page you can view and download a list of species and records that occur within or overlap that site. Each page provides a link to [request better access](/AccessRequest/Create) to the records for the site. 

### To explore a sites or 10KM grid square

1. Click on 'Browse Sites' and select the appropriate site layer containing your chosen site. Zoom into the site displayed on the map using the control at the top left of the page, by drawing a bounding box whilst holding down the shift key or by double clicking on the site. Click on the name of the site appearing in in the table to go to its site report.
2. Alternatively if you know the name of the site, you can enter the name or 10KM grid square in the 'Search the NBN Gateway' general search box and click on the link for your required site in the search results table.
3. On the site report page you can
 1. View the boundary of the site overlaid on an Ordnance Survey map.
 2. Select or deselect datasets contributing to the report using the datasets tick boxes. Organisations can be sorted alphabetically by selecting 'Organisation Name' in the Sort by box above the list of datasets. Click on the title of the dataset to view the metadata for that particular dataset (see [Exploring datasets](../Exploring_datasets)).
 3. Provide a year range (start and end year in YYYY format) restricting the site report to species records occurring within that year range.
 4. Select 'Records completely within site' in the Spatial relationship option to restrict the site report to species records occurring wholly within and not overlapping the site boundary.
 5. Select a designation to restrict the site report to species occurring within chosen designated list.
 6. Click on a Species Group link to restrict the site report to that species group.
 7. Click on 'Request Better Access' in the Other Options to go to the access request form (see [Filling in the Request Enhanced Access Form](../Filling_in_the_Request_Enhanced_Access_form)) for the site.
 8. Click Download button to download either the list of species for that site into a csv file, along with the contributing dataset metadata, or to download the records for the site (see [Downloading records](../Downloading_records)). This download may contain contain species or records that do not occur within the site but overlap the site boundary if the spatial relationship 'Records within or overlapping site' option is used.
 9. Click on the species name to display the records for that species. Species may be displayed on the Interactive Map by clicking on the 'View on Interactive Map' link. (see [Using the Interactive Map Tool](#interative_map))

        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>