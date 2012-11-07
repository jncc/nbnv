<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Introduction
This section provides a guide to the XML elements defined by the gateway_data and
gateway_query XML schemas. It is not intended to provide an exhaustive commentary
of every element and attribute. Click on an element or attribute name for information.

#AggregateSite

The AggregateSite element provides aggregated record and species data for a grid square.
It only appears in web service responses.  At the time of writing the web services that
use it in their response are:
* OneSpeciesLocationData
* SpeciesDensityData

The records that are aggregated to produce the results found in the AggregageSite
element will depend on the filters applied in the original request (e.g. it might
be restricted to just butterflies, or a date range, etc.).


##Graphical Representation
The diagram below shows the AggregateSite element.  The Site element has also been expanded too to help.

![AggregateSite element](images/AggregateSite.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Element</th>
            <th>Comments</th>
        </tr>
        <tr>
            <th>RecordCount</th>
            <td>The number of records within this square.</td>
        </tr>
        <tr>
            <th>SpeciesCount</th>
            <td>The number of distinct species within the square.</td>
        </tr>
        <tr>
            <th>HasSensitiveData</th>
            <td>This element is set to <strong>true</strong> if any records within this location are flagged as sensitive.</td>
        </tr>
        <tr>
            <th>HasNonSensitiveData</th>
            <td>This element is set to <strong>true</strong> if any records within this location are not flagged as sensitive.</td>
        </tr>
        <tr>
            <th>EarliestRecord</th>
            <td>The earliest recorded year for records in this site.</td>
        </tr>
        <tr>
            <th>LatestRecord</th>
            <td>The latest recorded year for records in this site.</td>
        </tr>
        <tr>
            <th>Site</th>
            <td>The square that has been used to aggregate records.</td>
        </tr>
    </tbody>
</table>
#BoundingBox
The BoundingBox has dual role being used to:

* let you define a rectangular spatial extent for a query
* define the geographic extent of a map image in response data
* The element is defined by the gateway\_data.xsd and imported by the gateway\_query.xsd.
It has no child elements and 5 attributes. The BoundingBox element attributes are
described in the table below.

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Element</th>
            <th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <th>srs</th>
            <td>No</td>
            <td>
                The spatial reference system the box coordinates refer to. The schema defines the following enumeration values for this attribute:
                <ul>
                    <li>EPSG_27700 (British National Grid, Default)</li>
                    <li>EPSG_4277 (OSGB 1936)</li>
                    <li>EPSG_4326 (WGS 84/Latlong)</li>
                    <li>EPSG_29903 (TM65/Irish National Grid)</li>
                    <li>osgb_BNG (alternate code for British National Grid)</li>
                    <li>osni_ING (alternate code for Irish National Grid)</li>
                </ul>
            </td>
        </tr>
        <tr>
            <th>minx</th>
            <td>Yes</td>
            <td>The bottom left corner x-axis coordinate</td>
        </tr>
		<tr>
            <th>miny</th>
            <td>Yes</td>
            <td>The bottom left corner y-axis coordinate</td>
        </tr>
		<tr>
            <th>maxx</th>
            <td>Yes</td>
            <td>The top right corner x-axis coordinate</td>
        </tr>
		<tr>
            <th>maxy</th>
            <td>Yes</td>
            <td>The top right corner y-axis coordinate</td>
        </tr>
    </tbody>
</table>

#Buffer

The Buffer query element lets you define a geographical point with a distance
buffer around it.

##Graphical Representation
The diagram below shows the Buffer element.

![Graphical Representation](images/Buffer.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Element</th>
            <th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <th>Point</th>
            <td>Yes</td>
            <td>Defines the geographical point the buffer surrounds. It has the the following three attributes: srs, x and y.</td>
        </tr>
		<tr>
            <th>srs</th>
            <td>No</td>
            <td>Defines the geographical point the buffer surrounds. It has the the following three attributes: srs, x and y.</td>
        </tr>
		<tr>
            <th>x</th>
            <td>No</td>
            <td>The point x-axis coordinate</td>
        </tr>
		<tr>
            <th>y</th>
            <td>No</td>
            <td>The point y-axis coordinate.</td>
        </tr>
		<tr>
            <th>Distance</th>
            <td>No</td>
            <td>The Radius of the buffer in metres from the point.</td>
        </tr>
    </tbody>
</table>

#DateRange

The DateRange query element lets you filter records by a year range.
If omitted, then no year filtering is applied.

The Start and End must only be year integers, date values are illegal.

The Start and End elements are optional letting you can make the filter open ended.
For example specifying a Start year of 1900 will select records from 1900 to the present.

[This date range document](../Resources/resources/DateFiltering.pdf) (pdf) describes in detail how date filtering is applied on the NBN Gateway.

##Graphical Representation
The diagram below shows the DateRange element.

![Graphical Representation](images/DateRange.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Element</th>
            <th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <th>Start</th>
            <td>No</td>
            <td>Defines the start year (inclusive) to which to filter to.</td>
        </tr>
		<tr>
            <th>End</th>
            <td>No</td>
            <td>Defines the end year (inclusive) to which to filter to.</td>
        </tr>
    </tbody>
</table>

#DatasetList
The DatasetList query element is lets you specify which datasets you
want to use for a query. Datasets are identfied in the list by their
NBN 8 character dataset key.

If it is omitted all datasets that are available for you to use
and have data for your query are used.

Just becuase you specify a dataset to use does not mean it will
be used. The NBN database will compare the list of datasets you
have set against your access to use them. If you do not have
sufficient access to use a dataset it will be dropped from your query.

##Graphical Representation
The diagram below shows the DatasetList element.

![Graphical Representation](images/DatasetList.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Element</th>
            <th>Comments</th>
        </tr>
        <tr>
            <th>Start</th>
            <td>A Dataset represented by its NBN 8 character dataset key.</td>
        </tr>
    </tbody>
</table>

#Designation
The NBN Gateway database has a number of formal and informal species
lists defined. The designation attribute can be used to restrict species
queried using one of these lists.

A list designation available on the NBN Gateway can be obtained from the [Designation List](../Actual_Services/Designation_List/) web service.

#Filter
The Filter query element lets you define a spatial extent for a query.

If used the Filter must define the spatial extent by using one and only
one of the following (click on a filter type for a full description):

* [BoundingBox](#BoundingBox)
* [Buffer](#Buffer)
* [Point](#Point)
* [Polygon](#Polygon)
* [MultiPartPolygon](#MultiPartPolygon)

##Graphical Representation
The following is a graphical representation of the structure of the Filter element.
Also provided is a table which describes the meaning of the elements in
the graphical representation.

![Graphical Representation](images/Filter.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
            <th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>BoundingBox</td>
            <td>No</td>
            <td>A bounding box defined by four corner points. <a href="#BoundingBox">Full Details</a></td>
        </tr>
        <tr>
            <td>Buffer</td>
            <td>No</td>
            <td>A buffer around a geogrpahical point. <a href="#Buffer">Full Details</a></td>
        </tr>
        <tr>
            <td>Point</td>
            <td>No</td>
            <td>A geographical point. <a href="#Point">Full Details</a></td>
        </tr>
        <tr>
            <td>Polygon</td>
            <td>No</td>
            <td>A simple polygon with one boundary and optionally holes in it. <a href="#Polygon">Full Details</a></td>
        </tr>
        <tr>
            <td>MultiPartPolygon</td>
            <td>No</td>
            <td>A collection of single polygons which should be treated as one geographical feature. <a href="#MultiPartPolygon">Full Details</a></td>
        </tr>
    </tbody>
</table>

#GeographicalFilter

The GeographicalFilter query element lets you define a spatial extent for a query.

If used the GeographicalFilter must define the spatial extent by using one and
only one of the following (click on a filter type for a full description):

* [BoundingBox](#BoundingBox)
* [Buffer](#Buffer)
* [GridSquare](#GridSquare)
* [SiteBoundary](#SiteBoundary)
* [Point](#Point)
* [Polygon](#Polygon)
* [MultiPartPolygon](#MultiPartPolygon)

##Graphical Representation
The following is a graphical representation of the structure of the
GeographicalFilter element. Also provided is a table which
describes the meaning of the elements in the graphical representation.

![Graphical Representation](images/GeographicalFilter.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
            <th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>BoundingBox</td>
            <td>No</td>
            <td>A bounding box defined by four corner points. <a href="#BoundingBox">Full Details</a></td>
        </tr>
        <tr>
            <td>Buffer</td>
            <td>No</td>
            <td>A buffer around a geogrpahical point. <a href="#Buffer">Full Details</a></td>
        </tr>
        <tr>
			<td>GridSquare</td>
            <td>No</td>
            <td>A 10km Grid Square. <a href="#GridSquare">Full Details</a></td>
        </tr>
        <tr>
            <td>SiteBoundary</td>
            <td>No</td>
            <td>A polygon boundary known to the NBN Gateway, for example a SSSI. <a href="#SiteBoundary">Full Details</a></td>
        </tr>
        <tr>
            <td>Point</td>
            <td>No</td>
            <td>A geographical point. <a href="#Point">Full Details</a></td>
        </tr>
        <tr>
            <td>Polygon</td>
            <td>No</td>
            <td>A simple polygon with one boundary and optionally holes in it. <a href="#Polygon">Full Details</a></td>
        </tr>
        <tr>
            <td>MultiPartPolygon</td>
            <td>No</td>
            <td>A collection of single polygons which should be treated as one geographical feature. <a href="#MultiPartPolygon">Full Details</a></td>
        </tr>
        <tr>
            <td>MapSettings</td>
            <td>No</td>
            <td>Defines the display settings for a map. <a href="#MapSettings">Full Details</a></td>
        </tr>
        <tr>
            <td>OverlayRule</td>
            <td>No</td>
            <td>
            <p>Sets the rule for querying recorded grid squares intersecting a geographical filter boundary. The schema defines an enumeration with two values:</p>
            <ul>
                <li>overlaps - any recorded grid square that is either fully contained within the geographical filter boundary or overlaps the boundary can be queried</li>
                <li>within - only recorded grid squares fully contained within the geographical filter boundary can be queried</li>
            </ul>
            <p>If omitted the default 'overlaps' rule is applied.</p>
            <p>The overlap rule generally returns more records but is less precise then the within rule.</p>
            <p>If the geographical filter used is a GridSquare the OverlayRule is ignored as all recorded grid squares will be within the selected GridSquare boundary.</p>
            </td>
        </tr>
        <tr>
            <td>MinimumResolution</td>
			<td>No</td>
            <td>
            <p>Sets the minimum resolution for querying records at. For example setting the MimimumResolution to 1km means only records recorded to 1km or 100m accuracy will be queried. The schema defines the following enumeration values for this element:</p>
            <ul>
                <li>Any</li>
                <li>_100m</li>
                <li>_1km</li>
                <li>_2km</li>
                <li>_10km</li>
            </ul>
            <p>Any and _10km are functionally equivalent. If omitted the default minimum resolution is _10km</p>
            <p>Care should be exercised when setting a minimum resolution that it does not conflict with your access to use a dataset. For example, if you have 2km access to a dataset but set the MinimumResolution to 1km then the dataset will not be used.</p>
            </td>
        </tr>
    </tbody>
</table>

#GridSquare

The GridSquare has a dual role, being used to:

* let you define a 10km grid square for a query
* define a grid square for a recorded site in a GatewayData response

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
            <th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>srs</td>
            <td>The spatial reference system the box coordinates refer to. The schema defines the following enumeration values for this attribute:
				<ul>
					<li>EPSG_27700 (Default)</li>
					<li>EPSG_4277</li>
					<li>EPSG_4326</li>
					<li>EPSG_29903</li>
					<li>osgb_BNG</li>
					<li>osni_ING</li>
				</ul>
			</td>
            <td>A bounding box defined by four corner points. <a href="#BoundingBox">Full Details</a></td>
        </tr>
        <tr>
            <td>minx</td>
            <td>Yes</td>
            <td>The bottom left corner x-axis coordinate (see important note below)</td>
        </tr>
        <tr>
            <td>miny</td>
            <td>Yes</td>
            <td>The bottom left corner y-axis coordinate (see important note below)</td>
        </tr>
        <tr>
            <td>maxx</td>
            <td>Yes</td>
            <td>The top right corner x-axis coordinate (see important note below)</td>
        </tr>
        <tr>
            <td>maxy</td>
            <td>Yes</td>
            <td>The top right corner y-axis coordinate (see important note below)</td>
        </tr>
        <tr>
            <td>key</td>
            <td>Yes</td>
            <td>TA grid reference value for a grid square, for example 'TL18'.</td>
        </tr>
    </tbody>
</table>

__Important note:__ when GridSquare is used in a request within the GeographicalFilter element the bounding box coordinates are not used (only the key identifies the square), so set them to zero (see example below).

#Header
The Header response element provides status information and statistics for a GatewayData response.

##Graphical Representation
The following is a graphical representation of the structure of the Header, Status and Summary element.
Also provided is a table which describes the meaning of the elements in the graphical representation.

![Header element](images/Header.png)

![Status element](images/Status.png)

![Summary element](images/Summary.png)

##Element Descriptions
<table class="attributeTable">
	<tbody>
		<tr class="head">
			<th>Parameter</th>
			<th>Required?</th>
			<th>Comments</th>
		</tr>
		<tr>
			<th>Header</th>
			<td>Yes</td>
			<td>Header has the two child elements Status and Summary. It does not have any attributes.</td>
		</tr>
		<tr>
			<th>Status</th>
			<td>Yes</td>
			<td>Status indicates whether or not the query was successful, this is indicated by the boolean attribute isError. It has three child elements:
				<ul>
					<li>ErrorCode</li>
					<li>ErrorDescription</li>
					<li>ErrorTime</li>
				</ul>
				The child error elements are only provided if the isError value is true, i.e. there has been an error.
			</td>
		</tr>
		<tr>
			<th>Summary</th>
			<td>Yes</td>
			<td>The Summary element groups together a series of elements which provide summary statistics for the response data.</td>
		</tr>
		<tr>
			<th>DatasetCount</th>
			<td>No</td>
			<td>Child element of Summary, it is a count of the number of datasets for which there is data.</td>
		</tr>
		<tr>
			<th>LocationCount</th>
			<td>No</td>
			<td>Child element of Summary, it is a count of the number of unique locations.</td>
		</tr>
		<tr>
			<th>RecordCount</th>
			<td>No</td>
			<td>Child element of Summary, it is a count of the total number of records.</td>
		</tr>
	</tbody>
</table>


#Map
The Map response element encapsulates a sequence of elements containing information about a
requested map image, including the images location on the NBN Server, dimensions and format.

##Graphical Representation

![Graphical Representation](images/Map.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>Map</td>
            <td>This element groups together the elements describing the map image.</td>
        </tr>
        <tr>
            <td>URL</td>
            <td>An URL to the map image on the NBN Servers. Child element of Map.</td>
        </tr>
        <tr>
            <td>Width</td>
            <td>The image width in pixels. Child element of Map.</td>
        </tr>
        <tr>
            <td>Height</td>
            <td>The image height in pixels. Child element of Map.</td>
        </tr>
        <tr>
            <td>Format</td>
            <td>The image format mime type. Child element of Map.</td>
        </tr>
        <tr>
            <td>BoundingBox</td>
            <td>Describes the spatial extent of the image so allowing it to be georeferenced. For a full description refer to the <a href="#BoundingBox">BoundingBox documentation</a></td>
        </tr>
    </tbody>
</table>


#MapSettings

The MapSettings query element lets you customise a map image returned by an NBN web service. It has no child elements and 7 attributes.

##Graphical Representation
The following diagram shows the element graphically, and the MapSettings element attributes are described in the table below.

![Graphical Representation](images/MapSettings.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
			<th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>height</td>
            <td>No</td>
            <td>The map image height in pixels. The default is 350.</td>
        </tr>
        <tr>
            <td>width</td>
            <td>No</td>
            <td>The map image width in pixels. The default is 350.</td>
        </tr>
        <tr>
            <td>outlineWidth</td>
            <td>No</td>
            <td>The Polygon boundary is drawn on the map. This sets the thickness for the boundary in pixels. The default is 1 pixel width.</td>
        </tr>
        <tr>
            <td>outlineColour</td>
            <td>No</td>
            <td>The boundary colour set as a hexadecimal string. The default is black (#000000).</td>
        </tr>
        <tr>
            <td>fillColour</td>
            <td>No</td>
            <td>Applies only to single species maps, it sets the colour for grid squares where the species has been recorded as a hexadecimal string. The default is red (#FF0000).</td>
        </tr>
        <tr>
            <td>fillTransparency</td>
            <td>No</td>
            <td>Sets the transparency of grid squares. Set values between 0.0 (transparent) and 1.0 (solid). The default is 1.0 (solid).</td>
        </tr>
        <tr>
            <td>mapOnly</td>
            <td>No</td>
            <td>If this value is true, the response of the web service does not contain species data anymore, it contains only the map image and information about the data providers. This option helps to reduce the network traffic and so improves the performance of the service, so if you only need the map in your client, we advise you to make use of this option.
				The value can be set to true or false. The default is false.
				This element is only supported for the one site data web service.</td>
        </tr>
    </tbody>
</table>

#MultiPartPolygon

Essentially this is the same as Polygon except multiple polygons can be defined which are treated as a single feature.

##Graphical Representation

![Graphical Representation](images/MultiPartPolygon.png)

#Point

The Point query element lets you define a geographical point and query records co-inciding with it. The Point element has no child elements and 3 attributes, these are defined in the table below:

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
			<th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>srs</td>
            <td>No</td>
            <td>Attribute of Point. It is the spatial reference system the point coordinates refer to. The schema defines the following enumeration values for this attribute:
				<ul>
					<li>EPSG_27700 (Default)</li>
					<li>EPSG_4277</li>
					<li>EPSG_4326</li>
					<li>EPSG_29903</li>
					<li>osgb_BNG</li>
					<li>osni_ING</li>
				</ul>
			</td>
        </tr>
        <tr>
            <td>x</td>
            <td>No</td>
            <td>The point x-axis coordinate</td>
        </tr>
        <tr>
            <td>y</td>
            <td>No</td>
            <td>The point y-axis coordinate</td>
        </tr>
    </tbody>
</table>

#Polygon

The Polygon query element is intended to let you define your own boundary for a geographical filter.
Optionally you can define holes inside the polygon, records inside the holes are ignore.

##Graphical Representation

![Graphical Representation](images/Polygon.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
			<th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>Polygon</td>
            <td>Yes</td>
            <td>Defines a simple polygon with one outer boundary and optionally one or more holes in it. It has one attribute, srs</td>
        </tr>
        <tr>
            <td>srs</td>
            <td>No</td>
            <td>Attribute of Polygon. It is the spatial reference system the point coordinates refer to. The schema defines the following enumeration values for this attribute:
				<ul>
					<li>EPSG_27700 (Default)</li>
					<li>EPSG_4277</li>
					<li>EPSG_4326</li>
					<li>EPSG_29903</li>
					<li>osgb_BNG</li>
					<li>osni_ING</li>
				</ul>
			</td>
        </tr>
        <tr>
            <td>Boundary</td>
            <td>Yes</td>
            <td>The polygon boundary. If the last vertex is not the same as the first vertex it is assumed the boundary is closed by a straight line connecting the two.</td>
        </tr>
        <tr>
            <td>Ring</td>
            <td>Yes</td>
            <td>The ring of vertices defining a polygon or hole boundary</td>
        </tr>
        <tr>
            <td>v</td>
            <td>Yes</td>
            <td>An individual boundary or hole vertex. At least three of these must be defined. The vertex is defined by the attributes srs, x and y, defined below</td>
        </tr>
        <tr>
            <td>srs</td>
            <td>No</td>
            <td>Same definition as the Polygon srs above.</td>
        </tr>
        <tr>
            <td>x</td>
            <td>Yes</td>
            <td>The vertex x-axis coordinate</td>
        </tr>
        <tr>
            <td>y</td>
            <td>Yes</td>
            <td>The vertex y-axis coordinate</td>
        </tr>
        <tr>
            <td>Hole</td>
            <td>No</td>
            <td>Any holes defined within the polygon. These must not cross the outer polygon defined by Boundary. If the last vertex is not the same as the first vertex it is assumed the boundary is closed by a straight line connecting the two.</td>
        </tr>
    </tbody>
</table>

#ProviderMetadata

The ProviderMetadata response element contains metadata for a dataset which has been used by a query.

##Graphical Representation

![Graphical Representation](images/ProviderMetadata.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
			<th>Required?</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>ProviderMetadata</td>
            <td>Yes</td>
            <td>Root element, it encapsulates a sequence of elements which define dataset metadata.</td>
        </tr>
        <tr>
            <td>exchangeFormatVersion</td>
            <td>Yes</td>
            <td>The version of the standard exchange format this metadata adheres to.</td>
        </tr>
        <tr>
            <td>dateStamp</td>
            <td>Yes</td>
            <td>This is the date the dataset was either first uploaded to the gateway or updated, whichever is the latest.</td>
        </tr>
        <tr>
            <td>gatewayID</td>
            <td>No</td>
            <td>The NBN Gateway Id for the dataset</td>
        </tr>
        <tr>
            <td>DatasetTitle</td>
            <td>Yes</td>
            <td>The title of the dataset</td>
        </tr>
        <tr>
            <td>DatasetProvider</td>
            <td>Yes</td>
            <td>The organisation providing the dataset</td>
        </tr>
        <tr>
            <td>Abstract</td>
            <td>Yes</td>
            <td>Groups together a sequence of elements which describe the dataset.</td>
        </tr>
        <tr>
            <td>Description</td>
            <td>Yes</td>
            <td>Child element of Abstract. It is a description of the dataset.</td>
        </tr>
        <tr>
            <td>DataCaptureMethod</td>
            <td>No</td>
            <td>Child element of Abstract. Describes the methods used to collect data.</td>
        </tr>
        <tr>
            <td>DatasetPurpose</td>
            <td>No</td>
            <td>Child element of Abstract. The motivation for collecting or collating the data.</td>
        </tr>
        <tr>
            <td>GeograhicalCoverage</td>
            <td>No</td>
            <td>Child element of Abstract. Describes the geographic spread of records</td>
        </tr>
        <tr>
            <td>TemporalCoverage</td>
            <td>No</td>
            <td>Child element of Abstract. Describes the spread of records through time.</td>
        </tr>
        <tr>
            <td>DataQuality</td>
            <td>No</td>
            <td>Child element of Abstract. Information on validation and verification the dataset has undergone.</td>
        </tr>
        <tr>
            <td>AdditionalInformation</td>
            <td>No</td>
            <td>Child element of Abstract. Any other information which does not fit into any of the other Abstractelements</td>
        </tr>
        <tr>
            <td>Survey</td>
            <td>No</td>
            <td>Survey level metadata, not currently provided.</td>
        </tr>
        <tr>
            <td>OccurrenceAttribute</td>
            <td>No</td>
            <td>Provides information for attribute data</td>
        </tr>
        <tr>
            <td>AccessConstraints</td>
            <td>No</td>
            <td>Information about constraints on accessing the data placed by the provider.</td>
        </tr>
        <tr>
            <td>UseConstraints</td>
            <td>No</td>
            <td>Information about constraints on using the data placed by the provider.</td>
        </tr>
    </tbody>
</table>

#Site
The Site response element describes a unique site by its geometry in a GatewayData response.

##Graphical Representation
The following is a graphical representation of the structure of the Site element.

![Header element](images/Site.png)

##Element Descriptions
<table class="attributeTable">
	<tbody>
		<tr class="head">
			<th>Parameter</th>
			<th>Required?</th>
			<th>Comments</th>
		</tr>
		<tr>
			<th>siteKey</th>
			<td>Yes</td>
			<td>This attribute contains the unique key which the represents this particular site</td>
		</tr>
		<tr>
			<th>Area</th>
			<td>No</td>
			<td>The area of the site in a defined set of units. Currently all data is returned in metres squared.</td>
		</tr>
		<tr>
			<th>GridSquare</th>
			<td>Yes</td>
			<td>TA grid square from either the British or Irish National Grids. Currently all data returned by the NBN Gateway web services is as a GridSquare. The grid square has attributes which define the squares bounding box, spatial reference system and grid reference.</td>
		</tr>
	</tbody>
</table>

#SiteBoundary

The SiteBoundary element represents a Site Boundary which exists on the NBN Gateway.
##Graphical Representation
The following is a graphical representation of the structure of the SiteBoundary element.
Also provided is a table which describes the meaning of the elements in the graphical representation.

![AggregateSite element](images/SiteBoundary.png)

##Element Descriptions
<table class="attributeTable">
	<tbody>
		<tr class="head">
			<th>Parameter</th>
			<th>Required?</th>
			<th>Comments</th>
		</tr>
		<tr>
			<th>siteKey</th>
			<td>Yes</td>
			<td>The Site key attribute represents the site which is to be looked up. The site key is the unique identifier for a particular site that exists in a particular Dataset Provider.</td>
		</tr>
		<tr>
			<th>providerKey</th>
			<td>Yes</td>
			<td>The provider key attribute is the Dataset provider key for which the &quot;siteKey&quot; has come from. This is important to define as site keys are not globally unique, they are only garenteed to be unique within the scope of a particular Dataset Provider.</td>
		</tr>
		<tr>
			<th>SiteBoundaryName</th>
			<td>No</td>
			<td>This element represents the name of the particular site boundary. This can be populated by sending this element to the <a href"../actual_services/site_boundary_name/">site boundary name</a> web service. </td>
		</tr>
		<tr>
			<th>SiteBoundaryType</th>
			<td>No</td>
			<td>This element represents the category of which the SiteBoundary exists in. This can be populated by sending this element to the <a href"../actual_services/site_boundary_name/">site boundary name</a> web service. </td>
		</tr>
	</tbody>
</table>

#Species

The Species response element encapsulates a sequence of elements containing
information about a species, including its name, taxon version key and authority.

##Graphical Representation

![Graphical Representation](images/Species.png)

##Element Descriptions
<table class="attributeTable">
    <tbody>
        <tr class="head">
            <th>Parameter</th>
            <th>Comments</th>
        </tr>
        <tr>
            <td>Species</td>
            <td>This element groups together the elements describing species. It has one attribute, taxonVersionKey.</td>
        </tr>
        <tr>
            <td>taxonVersionKey</td>
            <td>The 16 character NBN Taxon version key identifying the species</td>
        </tr>
        <tr>
            <td>ScientificName</td>
            <td>The species scientific name</td>
        </tr>
        <tr>
            <td>CommonName</td>
            <td>The species common name (if it has one).</td>
        </tr>
        <tr>
            <td>Authority</td>
            <td>The species naming authority.</td>
        </tr>
        <tr>
            <td>TaxonReportingCategory</td>
            <td>The NBN taxon reporting category the species belongs to.</td>
        </tr>
        <tr>
            <td>SpeciesAttributes</td>
            <td>This element contains attributes for the species. In 3.4 the following attributes are returned by the SpeciesList webservice:
				<ul>
					<li>Downloadable – If a dataset to which you have download access is available</li>
					<li>RecordCount – The number of records on the NBN Gateway for this species, based off dataset filter</li>
					<li>StartYear – Year of the earliest record based off dataset filter</li>
					<li>EndYear – Year of the latest record based off dataset filter</li>
				</ul>
			</td>
        </tr>
    </tbody>
</table>

        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>