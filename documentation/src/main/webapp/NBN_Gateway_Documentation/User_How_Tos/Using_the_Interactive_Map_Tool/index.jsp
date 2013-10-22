<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Using the Interactive Map Tool

The Interactive Map Tool allows the exploration of species records, habitats, datasets and site boundaries.

### To use the Interactive Map Tool

1. Enter the name of the species, habitat, designated list, dataset or site in the text box at the top of the page. Species names can either be the currently used scientific name, a common name, other previously used scientific names, a higher taxon, for example genus, or the species code (first 2 letters of the genus and first 3 letters of the species name).
2. On entering a species the 10KM grid squares and legend will be displayed. Further changes can be made to this map
 1. Click on the spanner icon to edit the colour and opacity of the grid squares, apply a year filter and to turn on or off datasets contributing to the map. Absence records are displayed as cross hatched grid squares.
 2. Change the background map by hovering over and selecting the background from the small map image in the bottom left hand corner.
 3. Add additional species, habitats, designated list, dataset or site boundaries. These will be added as a separate layer on the map with a new entry in the legend.
 4. Zoom in using the control at the top left of the page. Alternatively you can draw a bounding box whilst holding down the shift key, enter a site name or 10KM grid square into the text box to zoom directly to a defined area.
 5. On zooming in on the map the resolution of the grid square will automatically increase to an appropriate resolution level for the map displayed. This can be changed to a fixed resolution display by editing the layer using the spanner tool and changing the grid the species should be mapped at. If the displayed resolution is higher than the actual record resolution or your access to the record then the grid square will not appear on the map. To display the grid square change to a lower fixed resolution using the edit control for the layer.
 6. The order that layers are displayed on the map can be changed by moving layers up or down on the legend. This can be done by dragging and dropping the layer with the topmost layer in the list appearing above the lower listed layers.
 7. The layer can be removed from the map by clicking the bin icon.
3. On entering a site or 10KM grid square the map will automatically zoom into that area. The site boundary can be added by entering the corresponding site layer, for example National Nature Reserves in England. Further changes can be made to the site boundary using the spanner icon next to the site layer in the legend.
4. Habitat layers can be added to the map by entering the name of the layer in the text box. Further changes can be made to the habitat boundary using the spanner icon next to the habitat layer in the legend.
5. Dataset and designated list species richness maps can be produced by entering the dataset or designated list name in the text box. Click on the spanner icon to apply a year filter, turn on or off datasets contributing to the map and to change the resolution of the grid squares.
6. Datasets contributing to the map are listed under each organisation in the legend Datasets tab. Datasets selection may be changed for each layer through their edit control by clicking on the spanner icon.
7. To view records contributing to the map of species, designated list or dataset you will need to be registered and logged in. Click on the Login link on the top right hand corner to log in and then select the Records tab in the legend. Records within a drawn bounding box will appear in the legend under the specific layer. The records are searchable using the search box provided on the Records tab. Click 'Redraw Selection'  to draw a new bounding box for record selection.
8. The web address at the top of the page contains the information required to recreate the map including your added layers and zoomed in area. Use this address for future visits to go directly to your customised map.

        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>