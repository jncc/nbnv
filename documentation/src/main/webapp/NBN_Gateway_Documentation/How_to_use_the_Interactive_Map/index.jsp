<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#USING THE INTERACTIVE MAP

The Interactive Map allows you to explore species records in more depth with respect to different backgrounds and overlays. The page always starts with a map of Great Britain and Ireland with available records visible as 10km squares. Either side of the map are navigation, customisation and data interrogation tools. *It is important to remember that the Interactive Map only displays the species records that are available to you through the Gateway*.

##How to explore species data using the Interactive Map

###Selecting the Interactive Map

* Type the name of a species into the Searchbox on the Gateway homepage and click search. 
* Identify the correct species of interest on the species results page and click option
* You will now be presented with an Interactive Map of Britain and Ireland with available species records plotted as grey 10km squares. The datasets used to plot species records on the Interactive Map are named at the bottom of the page. You can add and remove datasets from the map by clearing the corresponding tick box and clicking refresh report. Datasets that are not available to you are listed separately at the bottom.

###Navigating the Map
There are four navigation tools provided which allow you to move around the map. These are the top set of tools to the left of the map

* You can zoom into an area of interest. Click the zoom in button and then use the mouse to click, hold and drag a box to define the area you wish to zoom in to. Once the yellow box contains your area of interest release the mouse. Within a few moments the map will refresh to a scale that fits your area of interest.

Notice how the scale of the backdrop map changes and becomes more detailed as you move in closer to a target area.

* Click the zoom out button to move back out from an area you have zoomed into. Each time you click the button the map will zoom out a set amount.
* Click the full view button to return to the full extent map displaying Great Britain and Ireland at one click.
* You can move across the surface of the map at any scale to a new area of interest. Click the move map button then hover the mouse cursor over roughly the centre of the map. Click and hold the mouse then drag it in the opposite direction to the area you wish to reveal. The map image will move with the cursor. When you release the mouse the map will refresh to reveal your new area of interest. Note that the map will automatically refresh if your cursor leaves the mapping area. 

As you use the navigation tools to identify and zoom in to an area of interest the species records will become clearer. The species records are presented on the map as grid squares. Four different resolutions of grid square can be displayed (10km, 2km, 1km and 100m). Only those records that you have access will be displayed at each resolution.  For example a species record held at 100m plus resolution, but only made available to you at 2km resolution will only appear on the Interactive Map as a 10km and 2km grid square.  For example a species record held at 100m plus resolution, but only made available to you at 2km resolution will only appear on the Interactive Map as a 10km and 2km grid square. Someone with full access would see those and the 1km and 100m squares.

As you zoom into an area of interest you may wish to turn the larger grid squares used to represent records off in favour of the more detailed squares revealed within them. The Resolution checklist tool to the right of the map allows you to do this. Simply uncheck the resolutions of square you would like to remove then click the refresh the map button at the bottom right of the map.

You can use the view controls to the bottom left of the map to make the map image on the page bigger or smaller. A smaller map takes less time to download and is useful if you have a slow internet connection. However, if you have a fast connection a large map will give you more detail. By default the image is 400 by 400 pixels.
* Click the Increase map size button to increase the map image size.
* Click the Decrease map size button to decrease the map image size.

###The Legend
The Interactive map can display a lot of visual information and keys are available to support their interpretation. To see the key to visual information on your map click the show legend button located to the left of the map. The legend will pop-up in a new window. The content of the legend is dependent upon what you have on your map. It does not include the symbols used on OS background maps.

###Background Maps
There are two backgrounds provided for you to view species records against. Ordnance Survey and Land Cover Map 2000 appear in a background checklist to the right of the map and are described further below. Tick the appropriate box to make them visible and then click the refresh the map button at the bottom right of the map.

* The Ordnance Survey map appears when over Great Britain and the Ordnance Survey of Northern Ireland appears if you are zoomed-in to just Northern Ireland. The Ordnance Survey background covering Great Britain is provided at three different resolutions. As you zoom in background changes from 1:1 million to 1:250,000 and then to 1:50,000. The Ordnance Survey of Northern Ireland is only provided as 1:50,000 and will not appear until you have zoomed-in to a sufficiently small area of Northern Ireland.
* The Land Cover Map 2000 of Broad Habitats has complete coverage for Great Britain and Northern Ireland. The Centre of Ecology and Hydrology have provided it to the Gateway. It allows you to view species records over broad habitats, which may be of ecological interest or help target areas that may be under recorded. The key to habitat classes is provided in the legend tool, but only when this background is in use. 

Tick the Make records transparent box under the background tools to the right of the map to see the detail of backgrounds and boundaries behind record grid squares. You will need to click the refresh the map button to apply this preference.

###Overlaying Boundaries
You can use the Boundary tool to the right of the map to overlay any of the Geographic Datasets held on the Gateway. Click the drop-down list and then select the Geographic boundary you would like to overlay. You will need to click the refresh the map button to apply this preference. Please note that some boundaries are scale dependent and will not draw if you are zoomed-out too far.

##Querying Records

Once you have identified records of interest using the Interactive Map you can use the Information tools to the left of the map to select and view data associated with them :

* First click the Select Records button and then use the mouse to click, hold and drag a box over the records you wish to query. Once the yellow box contains the records of interest release the mouse. Within a few moments the map will refresh. The records that your box overlapped will now appear with a yellow outline.
* Next click the Query records button to view the data associated with records you have just selected. This button is only active once records are selected. If you have sufficient access to the dataset underlying the records then their data will appear in a table on a new web page. If you do not have sufficient access then no data for these records will be displayed, but you can continue to view them on the map. 
* To clear your selection of records on the map click the Clear selection button. The map will refresh and the records that were previously selected will have lost their yellow border.

###Change the Species
To change the species being displayed on the map click the Change species button to the bottom left of the map. This opens a pop-up window containing a search box like the one on the homepage. Type the name of a species into the Searchbox and click search. The pop-up window will then present a list of species matching your search term. Identify and click the correct species of interest. Records available for this species will now be plotted.
		</nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
