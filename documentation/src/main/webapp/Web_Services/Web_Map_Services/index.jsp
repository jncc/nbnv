<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
    <jsp:attribute name="body">
        <nbn:markdown>
#Introduction

NBN Gateway supports a suite of Open Geospatial Consortium WMS 1.3.0 and 1.1.1 compliant
services. These services provide images of NBN data for species, single datasets and
designation species densities maps, enabling users to embed these NBN Gateway contextual
layers within their own tools and utilities, create mashups with other data, and easily
supporting NBN data on their websites.

#WMS Details

Each service has the following support matrix:

<table class="attributeTable">
    <tr class="head"><th>Request</th><th>Function/Argument</th><th>Supported?</th></tr>
    <tr><th>All</th><td>Version</td><td>1.3.0, 1.1.1</td></tr>
    <tr><th>GetCapabilities</th><td>FORMAT</td><td>XML, OGC WMS XML</td></tr>
    <tr><th rowspan="8">GetMap</th><td>LAYERS</td><td>Yes</td></tr>
    <tr><td>STYLES</td><td>Default only</td></tr>
    <tr><td>CRS</td><td>CRS:84 (1.3.0 only), EPSG:4236, EPSG:27700</td></tr>
    <tr><td>FORMAT</td><td>BMP, JPEG, TIFF, PNG (8/24/32), GIF, SVG+XML</td></tr>
    <tr><td>TRANSPARENT</td><td>Yes</td></tr>
    <tr><td>BGCOLOR</td><td>No</td></tr>
    <tr><td>TIME</td><td>No*</td></tr>
    <tr><td>ELEVATION</td><td>No</td></tr>

    <tr><th rowspan="2">GetFeatureInfo</th><td>FORMAT</td><td>XML, OGC WMS XML</td></tr>
    <tr><td>SRS</td><td>CRS:84, EPSG:4236, EPSG:27700</td></tr>

    <tr><th>GetLegendGraphic</th><td>FORMAT</td><td>PNG</td></tr>
</table>

\* Time based filtering is supported by custom arguments (see further below).

This compatibility matrix can be extended upon request. For example if any organisation
needs 1.0.0 support or different EPSG formats please relay your request to the development team.

##NBN Gateway Web Mapping Services

There are three sets of services. The general format of the service URL is:

<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/__SERVICETYPE__/__REQUIREDNBNKEY__/WMSServer?</nbn:prettyprint-code>

The following service types with their corresponding required NBN Key are

* __Single Species Mapping__ (NBN Key = TaxonVersionKey)  
    eg. WMS URL for single species layer for Aeshna isosceles  
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/SingleSpeciesMap/NBNSYS0000005629/WMSServer?</nbn:prettyprint-code>

* __Single Dataset Mapping__ (NBN Key = DatasetKey)
    eg. WMS URL for single dataset mapping for Terrestrial Heteroptera Recording
    Scheme's dataset "iSpot (2008-2010): Shieldbugs & allied species
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/DatasetSpeciesDensityMap/GA000858/WMSServer?</nbn:prettyprint-code>

* __Designation Mapping__ (NBN Key = Designation Abbreviation Code)
    eg. WMS URL for designation mapping for EC CITES Annex A
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/DesignationSpeciesDensityMap/ECCITES-A/WMSServer?</nbn:prettyprint-code>


This base URL can be augmented with a number of additional filters

* __username and userkey__ - Allows a user to login to the Gateway to gain better maps  
    eg. To use 'test' account  
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/DatasetSpeciesDensityMap/GA000858/WMSServer?username=test&userkey=HASHED_PASSWORD</nbn:prettyprint-code>

* __startyear__ - Filter out early records by year
    eg. To filter out all records before 2005
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/DatasetSpeciesDensityMap/GA000858/WMSServer?startyear=2005</nbn:prettyprint-code>

* __endyear__ - Filter out later records by year
    eg. To filter out all records after 1992
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/DatasetSpeciesDensityMap/GA000858/WMSServer?endyear=1992</nbn:prettyprint-code>

* __datasets__ - Limit results to specific datasets (not used in single dataset mapping)
    eg. To utilise only the Dragonfly Recording Network dataset in mapping Aeshna isosceles
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/SingleSpeciesMap/NBNSYS0000005629/WMSServer?datasets=GA000012</nbn:prettyprint-code>

* __abundance__ - Filters to show presence, absence or both types of record. (Only for Single Species Mapping)
    Valid values for this parameter are
    * __all__ - Shows both absence and presence records simultaneously
    * __presence__ - Only shows Presence records
    * __absence__ - Only shows Absence records

    eg. To only see absence records for *Tetrao tetrix*  
<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/SingleSpeciesMap/NHMSYS0000530658/WMSServer?abundance=absence</nbn:prettyprint-code>

##Using WMS Parameters to request maps

The URL constructed above, combined with other parameters (see below) form the basic
WMS GetMap request url (sent from your application, service or web page) to the NBN
service. This request url queries the WMS, which then returns the specific map layer,
or layers, requested.

The __required__ parameters for a basic __WMS GetMap__ v1.3.0 request are:

<table class="attributeTable">
    <tr class="head"><th>Parameter name</th><th>Parameter value</th><th>Notes</th></tr>
    <tr><th>REQUEST</th><td>GetMap</td><td>The required WMS request type.</td></tr>
    <tr><th>SERVICE</th><td>WMS</td><td>Specifies the type of service.</td></tr>
    <tr><th>VERSION</th><td>1.3.0</td></tr>
    <tr><th>CRS</th><td>EPSG:27700 | EPSG:4326 | CRS:84</td>
        <td>
            <p>Spatial Reference System.</p>
            <ul>
                <li>EPSG:27700 = British National Grid.</li>
                <li>EPSG:4326 = WGS84 (e.g. the SRS used by Google)</li>
                <li>CRS:84 = WGS84 in long/lat order</li>
            </ul>
        </td></tr>
    <tr><th>BBOX</th><td>A bounding box of the form minx, miny, maxx, maxy</td><td>Provide British National Grid or Lat/Long coordinates depending on SRS</td></tr>
    <tr><th>FORMAT</th><td>image/png | image/jpeg | image/gif</td><td>png is recommended for best quality image.</td></tr>
    <tr><th>LAYERS</th><td>0,1,2,3</td><td>
            <p>These are the map layers which can be requested in the WMS GetMap request. You can request just one, or any combination of them.</p>
            <ul>
                <li>0 = 100m squares</li>
                <li>1 = 1km squares</li>
                <li>2 = 2km squares</li>
                <li>3 = 10km squares</li>
            </ul>
        </td></tr>
    <tr><th>WIDTH</th><td>width of required map in pixels</td><td>Used to adjust the size of map you want in your application / website.</td></tr>
    <tr><th>HEIGHT</th><td>height of required map in pixels</td><td>Used to adjust the size of map you want in your application / website.</td></tr>
    <tr><th>TRANSPARENT</th><td>true | false</td><td>Specifies whether the image will have a transparent background or white</td></tr>
</table>

An example request url which queries the WMS directly for the Terrestrial Heteroptera
Recording Scheme's dataset "__iSpot (2008-2010): Shieldbugs & allied species__" 10km square layer

<nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/arcgis/rest/services/grids/DatasetSpeciesDensityMap/GA000858/WMSServer?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&BBOX=0,0,700000,900000&WIDTH=640&HEIGHT=640&CRS=EPSG:27700&FORMAT=image/png&LAYERS=3</nbn:prettyprint-code>

#Finding NBN Keys

All the NBN Web Mapping Services require NBN Keys to identify the information required.
Included below is information to allow you to locate the keys for the entities you require.
These keys are constant for the entities described, but may be removed if the dataset or
designation is changed significantly. Each replication new datasets are added, designation
information is updated in line with the JNCC collation, and new taxa are included at least
twice a year, so it is impossible for us to maintain a static list.

##Dataset Keys

The value needs to be set to NBN Dataset Key for the dataset. This can be found on the dataset metadata page:

![Dataset Keys](images/dataset.jpg)

To get to the metadata page, click on Browse Datasets in the menu at the top of the
main NBN Gateway page, and locate the dataset whose key you require.

##Taxon Keys

The value needs to be set to NHM Dictionary Taxon Version Key for a species. This can
be found on the taxonomic information page for the species:

To find the TVK for a species, use the search box on the main NBN Gateway page and click
on 'Taxonomic and designation information' for the species you require.

![Taxon Keys](images/taxon.png)

##Designation Keys

The value needs to be set to the abbreviation code for the designation. This can be found on the designation metadata page:

![Designation Keys](images/designation.png)

To get to the metadata page, click on Browse Designations in the menu at the top of the main NBN Gateway
page, and locate the designation whose key you require.

#Using WMS within ArcGIS

* Open ArcCatalog
* Scroll down the data list on the left to find "GIS Servers"
* Double click "GIS Servers"
* Click "add WMS Server"

In the pop up add the URL for the map service you wish to use:

![Using the WMS in ArcGIS](images/arcgis.png)

Hit get layers, you should now see the available list of layers.

That should set up the WMS for use within ArcMap. To access the data:

* Hit the add data button in ArcGIS
* Scroll down to the GIS Server section. You should now have your requested NBN WMS listed

Select this and click Add. The data will now display in ArcGIS.
        </nbn:markdown>

    </jsp:attribute>
</t:webserviceDocumentationPage>