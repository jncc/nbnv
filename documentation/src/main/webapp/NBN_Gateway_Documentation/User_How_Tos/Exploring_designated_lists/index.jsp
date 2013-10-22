<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Exploring designated lists

The NBN Gateway incorporates the current designated lists collated by Joint Nature Conservation Committee ([JNCC](http://jncc.defra.gov.uk/page-5546)). The information on these lists along with the species they include can be viewed and mapped on the NBN Gateway. In addition these lists be used within site reports (see [Exploring a site or 10KM grid square](../Exploring_a_site_or_10KM_grid_square)), to request enhanced access (see [Filling in the Request Enhanced Access Form](../Filling_in_the_Request_Enhanced_Access_form)) and download records (see [Downloading records](../Downloading_records)).

### To explore designated lists

1. Click on 'Browse Designations' and expand the appropriate designation category to select the required designated list. Click on the designated list to go to its information page.
2. Alternatively, enter the name or part of the name of the designated list in the 'Search the NBN Gateway' general search box and click on the link for your required designated list in the search results table.
3. On the designated list information page you can 
 1. View the information for the designated list.
 2. Map the species richness map for the designated list on the Interactive Map Tool (see [Using the Interactive Map Tool](../Using_the_Interactive_Map_Tool))
 3. View the species groups and species that make up the designated list. Click on the species to see it's species information page. The Designations section in this page lists all the current designations for that species.
 4. Click the download link to download records for species within the designated list (see Downloading records](#downloading_records)).
 5. Click on the Joint Nature Conservation Committee link to see further information on the collation of these designated lists. The JNCC web page also includes a link to download the master Conservation Designations Spreadsheet.

        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>