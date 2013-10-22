<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Exploring species
 
The NBN Gateway provides a number of reporting pages to explore species' records and information supplied by [organisations](/Organisations). These pages provide information on the species taxonomy, current designation status, list of sites, national and local maps of species records, species reports for sites and 10KM grid squares.

Each reporting page also provides a link to [request better access](/AccessRequest/Create) (see [Getting better access to records](../Getting_better_access_to_records) and [download](/Download) the species records (see [Downloading records](../Downloading_records))

### To explore species

1. From the main menu click on 'Browse Species' and enter the name of the species or higher taxon, for example genus, in the search box. Names can either be the currently used scientific name, a common name, other previously used scientific names, a higher taxon, for example genus, or the species code (first 2 letters of the genus and first 3 letters of the species name).
2. The name should appear towards the top of the Browse species table. The preferred name, as used in the NBN Gateway, and the number of records are also displayed. Click on the link of the species or taxon name to go to its information page.
3. The information page summarises the taxonomic information and current designation status for the species, as well as listing the organisations and datasets that have contributed records for that species. A map of these species records is displayed and relevant links to external sites for further information may be available. The 'Explore and Download Records' section provides access to the reporting pages and download wizard for that species or taxon. For a higher level taxon these reports and download contain the corresponding lower level taxa, for example records for all the species within a genus.
4. Click on '**Grid Map**' to go to the report displaying the grid squares for the species or taxon on a map, along with list of the contributing datasets and your level of access. On this page you can
 1. Select or deselect datasets contributing to the report using the datasets tick boxes. Organisations can be sorted alphabetically by selecting 'Organisation Name' in the Sort by box above the list of datasets. Click on the title of the dataset to view the metadata for that particular dataset (see [Exploring datasets](../Exploring_datasets)).
 2. Display up to three date groups by providing the year ranges for these groups in the Date ranges and colours control. Click on the colour square for that particular date group to change colour of the grid squares displayed on the map. Tick the date group to display the grid squares for that your range on the map. 
 3. Add a 100KM or 10KM grid, vice-countries boundaries or Ordnance Survey map background within the Overlays and backgrounds controls.
 4. Zoom to Great Britain, Ireland or a vice-county in the Zoom to area control. The displayed grid square resolution can be increased up to 1KM resolution when zoomed in to a vice-county using the Resolution Control.
 5. Click on 'Request Better Access' in the Other Options to go to the access request form (see [Filling in the Request Enhanced Access Form](../Filling_in_the_Request_Enhanced_Access_form))
 6. Click Download button to download either the list of grid squares displayed on the map into a csv file, along with the datasets' metadata, or the records contributing to the map (see [Downloading records](../Downloading_records)). 
 7. Click 'View on Interactive Map' to display the species directly in the Interactive Map Tool (see [Using the Interactive Map Tool](../Using_the_Interactive_Map_Tool))
5. Click on '**Interactive Map**' to display and explore the species records using the Interactive Map Tool  (see [Using the Interactive Map Tool](../Using_the_Interactive_Map_Tool)).
6. Click on '**List of sites**' to go to the report listing the sites for the species. The sites, ranging from small local nature reserves up to the much larger vice-counties, have been supplied by organisations. In addition to including records that are wholly within the site the list of sites may also include sites where all the records overlap but are not necessary within the sites. On this page you can
 1. Select or deselect datasets contributing to the report using the datasets tick boxes. Organisations can be sorted alphabetically by selecting 'Organisation Name' in the Sort by box above the list of datasets. Click on the title of the dataset to view the metadata for that particular dataset (see [Exploring datasets](../Exploring_datasets)).
 2. Select 'Records completely within site' in the Spatial relationship option to restrict the list of sites to sites where at least one record of the species occurs wholly within the site.
 3. Provide a year range (start and end year in YYYY format) to restrict the list of sites for species records occurring within the defined year range.
 4. Search for specific site or site type by typing in the name in the search list box.
 5. Click on 'Request Better Access' in the Other Options to go to the access request form (see [Filling in the Request Enhanced Access Form](../Filling_in_the_Request_Enhanced_Access_form))
 6. Click the Download button to download the list sites for the species into a csv file, along with the datasets' metadata.
 7. Click on the name of a site to view the records for the species within that site's site reporting page (see [Exploring datasets](../Exploring_datasets)).
7. Click on '**Download Records**' to download the species records using the download wizard form (see [Downloading records](../Downloading_records))

        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>