<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#PRODUCING A GRID MAP

The Grid Map allows you to plot the distribution of records on the Gateway for the species you have selected. The page always starts with a map of Great Britain and Ireland with available records plotted as 10km squares. To the left of the map are several editing and filter tools that will help you customise your map. For example, you could use these to produce a Grid Map of a species, within a vice-county, mapped on a 2km square grid, with records made between 1900 and 1950 shown in light blue and post 1950 records in dark blue. It is important to remember that the Grid Map only shows the distribution of species records available to you through the Gateway.

##How to produce a Grid map for a species

1. Type the name of a species into the Searchbox in the top right of any page within the nbn.org.uk domain and click search.

    ![Search the NBN Gateway](images/accountsearch.png)
    
2. Identify the correct species of interest on the species results page and click option 1. Grid map of ...

    You will now be presented with a Grid Map of Britain and Ireland with available species records plotted as red 10km squares. The datasets used to plot species records on the Grid Map are named at the bottom of the page. You can add and remove datasets from the report by using the tick box and clicking refresh report. Datasets that are not available to you are listed separately at the bottom.
3. There are five tools available for you to customise your Grid Map. You need to refresh the map to implement your selection:
	* You can limit the geographical extent of the map by selecting an option from the Region drop down list. 
	* You can produce a map for a single vice-county by selecting it in the vice-county drop down list. This feature is not available for Ireland.
	* You use the resolution drop-down list to specify the scale at which you would like records to be plotted on your map. This feature is dependent upon your level of access to the datasets used to plot the species records.
	* You can use grid overlays drop-down list to apply a grid over your map at a 100km or 10km scale. The national grid systems are different for Great Britain & Isle of Man and Ireland.
	* You can use the other overlays drop-down list to display vice-county boundaries or an Ordnance Survey 1:1million background map.
4. There are four tools available for you to customise the way species records are displayed on your Grid Map. You need to refresh the map to implement your selection.
	* You can use the colour schemes drop-down list to change the colour of the species record squares on your map.
	* You can use the preset dates drop-down list to select preset time divisions already in use for some popular groups. This divides records into up to three date classes given in the date boxes below. These are illustrated on the map by different grades of colour from light to dark.
	* You can use the three boxes to enter your own date classes. You have to specify the year from and to for each date class. Date class one will be plotted on the top and be the darkest. You don't have to use all three.
	* You can use the outline drop-down list to change the outline colour of the records plotted on your map.  
	Once you are happy with your Grid Map there are several things you can do with the results.
5. Click large map to see a full screen version of your Grid Map. Once there if you wish to return to the customisation page click editable map.
6. Click printable map to open a printer friendly version of your Grid Map. 
7. Click download squares to receive a copy of the species records on your Grid Map. Note that this tool is only available if you have download access to one or more of the datasets used to plot your Grid Map. Each record is supplied at a 10km square resolution. 
8. Click download data to receive a copy of the actual records included in your Grid Map. Note that this tool is only available if you have download access to one or more of the datasets used to plot your Grid Map. Each record is supplied at the access resolution you have to the dataset they are contained within.
9. Click Use the interactive map to explore the data further using the NBN Gateway interactive map tool.	
        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
