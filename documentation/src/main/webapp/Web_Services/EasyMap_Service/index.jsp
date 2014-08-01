<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
    <jsp:attribute name="body">
        <h1>EasyMap</h1>
        <p>EasyMap service provided by the NBN Gateway enables you to get a live NBN map on your website in one line of HTML.</p>
        <p>The service consists of </p>
        <ul>
            <li><b>Title</b> including species name and link to NBN terms and conditions</li>
            <li><b>Gridmap</b> which can be further customised using additional parameters described below</li>
            <li><b>Link to Interactive map</b> on the NBN Gateway</li>
            <li><b>Acknowledgement list</b> of data providers and datasets contributing to the map</li>
            <li><b>NBN Gateway logo</b></li>
        </ul>
        <p>The URL used for the EasyMap service consists of the address to the EasyMap service followed by a query string. This query string, starting with ?, contains one or more parameters used to determine how the map will be displayed. The basic format to display a species map is</p>
        <pre><code class="prettyPrint">https://data.nbn.org.uk/EasyMap?tvk=TAXONVERSIONKEY</code></pre>
        <p>TAXONVERSIONKEY is the TaxonVersionKey (TVK) used on the NBN Gateway for the species. To find the TVK search for the species on the NBN Gateway. The TVK will be displayed in the Taxon section of the species' information page on the NBN Gateway.</p>

        <h2>iFrames</h2>
        <p>The HTML tag &lt;iframe&gt; is used to include the map in your website. This creates a box, with a width and height that you can specify, with the map inside as specified using the src attribute.</p>
        <p>An example, producing a map of Small Red-eyed Damselfly looks like</p>
        <pre><code class="prettyPrint">&lt;iframe width="400" height="300" src="https://data.nbn.org.uk/EasyMap?tvk=NHMSYS0000344177"&gt;&lt;/iframe&gt;</code></pre>

        <h2>Customising the map</h2>
        <p>By specifying more parameters in the query string you can customise the appearance of the map. Each parameter is added to the query string separated from the previous one by an '&' and is written as <em>ParameterName=Value</em>. Note that the values are case sensitive and that there should be no spaces anywhere in the query string. It is possible to change the size of the map, zoom in on a vice county, select a particular dataset and much more.</p>
        <p>The format of the URL adding an additional parameter is</p>
        <pre><code class="prettyPrint">https://data.nbn.org.uk/EasyMap?tvk=TAXONVERSIONKEY&ParameterName=Value</code></pre>
        <p>The full list of parameters are described below.</p>

        <h2>Selecting what the map will display</h2>
        <p>The TaxonVersionKey, defining the particular species to map, is the only required parameter in the query string. By default all relevant datasets are included but you can restrict the map to specific datasets by including the datasetkey(s) in the query string. The datasetkey for a dataset can be found on the NBN Gateway by searching for the required dataset. The datasetkey is listed in the general metadata page for the dataset, as Permanent key.</p>
        <table class="attributeTable">
            <tr class="head">
                <th>Parameter Name</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
            <tr>
                <td><b>tvk</b></td>
                <td><b>TaxonVersionKey</b> obtainable from the NBN Gateway</td>
                <td>The TaxonVersionKey of the species for which you want to map. You must provide this in the query string.</td>
            </tr>
            <tr>
                <td><b>ds</b></td>
                <td><b>DatasetKey(s)</b> obtainable from the NBN Gateway</td>
                <td>A comma-separated list of datasetkeys of the datasets you want shown on the map. If none specified all available datasets are shown.</td>
            </tr>        
        </table>

        <h2>Changing the appearance of the map</h2>
        <table class="attributeTable">
            <tr class="head">
                <th>Parameter Name</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
            <tr>
                <td><b>w</b></td>
                <td>A number up to 800</td>
                <td>The width, in pixels, of the map. If neither height or width specified the width is 350. If only a height(h) is specified then that value is used here.</td>
            </tr>
            <tr>
                <td><b>h</b></td>
                <td>A number up to 800</td>
                <td>The height, in pixels, of the map. If neither height or width specified the width is 350. If only a width(w) is specified then that value is used here.</td>
            </tr>
            <tr>
                <td><b>gd</b></td>
                <td>Either <b>10km</b>, <b>100km</b> or <b>10km_100km</b></td>
                <td>Specifies a grid to overlay the map. If omitted, no grid is shown.</td>
            </tr>
            <tr>
                <td><b>res</b></td>
                <td>Either <b>100m</b>, <b>1km</b> or <b>2km</b></td>
                <td>The size of the grid squares to show on the map. If not specified the resolution is 10km. Remember not all datasets can be viewed at higher resolutions.</td>
            </tr>
            <tr>
                <td><b>bg</b></td>
                <td>Either <b>os</b> or <b>vc</b></td>
                <td>Specifics a background of either an Ordnance Survey map or Vice County boundaries. If omitted, no background is shown.</td>
            </tr>

        </table>

        <h2>Zooming in on the map</h2>

        <table class="attributeTable">
            <tr class="head">  
                <th>Parameter Name</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
            <tr>
                <td><b>zoom</b></td>
                <td>
                    One of the following
                    <b>england</b>
                    <b>scotland</b>
                    <b>walesbl=</b>
                    <b>highland</b>
                </td>
                <td>Zooms the map to the named area.</td>
            </tr>      
            <tr>
                <td><b>vc</b></td>
                <td>The number of the vice-county</td>
                <td>Zooms the map to a particular vice-county as specified.</td>
            </tr>
            <tr>
                <td><b>bl</b></td>
                <td><b>Grid reference</b> 10km, 2km or 1km resolution. Not Irish grid references</td>
                <td>Defines the bottom left corner of a bounding box. Use with <b>tr</b> parameter.</td>
            </tr>   
            <tr>
                <td><b>tr</b></td>
                <td><b>Grid reference</b> 10km, 2km or 1km resolution. Not Irish grid references</td>
                <td>Defines the top right hand corner of a bounding box. Use with <b>bl</b> parameter.</td>
            </tr>
            <tr>
                <td><b>blCoord</b></td>
                <td><b>Easting</b>,<b>Northing</b></td>
                <td>Defines the bottom left corner of a bounding box. Use with <b>trCoord</b> parameter.</td>
            </tr>   
            <tr>
                <td><b>trCoord</b></td>
                <td><b>Easting</b>,<b>Northing</b></td>
                <td>Defines the top right hand corner of a bounding box. Use with <b>blCoord</b> parameter</td>
            </tr>     
        </table>

        <h2>Displaying date bands</h2>
        <p>If you are interested in looking at a change in distribution over time you can show the records for one date range in one colour and overlay this with records for a different date range in a different colour. You can show up to three date ranges which are defined by the b0, b1 and b2 parameters.b0 will be the bottom layer, with b2 the top most layer.</p>
        <p>The colours of the grid squares are defined by their mix of red, green and blue. The amount of each colour is specified by a number from 0, meaning none, to a maximum of FF counting in hexadecimal (base 16). A leading 0 is added to ensure each number has 2 digits. Writing the 3 amounts together as one number, giving then in order red, green and blue, creates a 6-digit colour code. For example red is FF0000, magenta is FF00FF and white is FFFFFF. A hexadecimal colour selector is provided in atlas grade map documentation</p>
        <table class="attributeTable">
            <tr class="head">
                <th>Parameter Name</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
            <tr>
                <td><b>b0from</b></td>
                <td>A <b>year</b></td>
                <td>Defines the start year for the lower date layer (inclusive)</td>
            </tr>
            <tr>
                <td><b>b0to</b></td>
                <td>A <b>year</b></td>
                <td>Defines the end year for the lower date layer (inclusive)</td>
            </tr>
            <tr>
                <td><b>b0fill</b></td>
                <td><b>Hexadecimal</b> colour</td>
                <td>The fill colour for the lower date layer</td>
            </tr>
            <tr>
                <td><b>b0bord</b></td>
                <td><b>Hexadecimal</b> colour</td>
                <td>The border colour for the lower date layer</td>
            </tr>
            <tr>
                <td><b>b1from</b></td>
                <td>A <b>year</b></td>
                <td>Defines the start year for the middle date layer (inclusive)</td>
            </tr>
            <tr>
                <td><b>b1to</b></td>
                <td>A <b>year</b></td>
                <td>Defines the end year for the middle date layer (inclusive)</td>
            </tr>
            <tr>
                <td><b>b1fill</b></td>
                <td><b>Hexadecimal</b> colour</td>
                <td>The fill colour for the middle date layer</td>
            </tr>
            <tr>
                <td><b>b1bord</b></td>
                <td><b>Hexadecimal</b> colour</td>
                <td>The border colour for the middle date layer</td>
            </tr>    
            <tr>
                <td><b>b2from</b></td>
                <td>A <b>year</b></td>
                <td>Defines the start year for the top date layer (inclusive)</td>
            </tr>
            <tr>
                <td><b>b2to</b></td>
                <td>A <b>year</b></td>
                <td>Defines the end year for the top date layer (inclusive)</td>
            </tr>
            <tr>
                <td><b>b2fill</b></td>
                <td><b>Hexadecimal</b> colour</td>
                <td>The fill colour for the top date layer</td>
            </tr>
            <tr>
                <td><b>b2bord</b></td>
                <td><b>Hexadecimal</b> colour</td>
                <td>The border colour for the middle date layer</td>
            </tr>    
        </table>

        <h2>Selecting what parts of the service to be displayed</h2>
        <p>Along with the map the service returns a title containing the species name and link to terms and conditions, acknowledgement list of data providers, link to NBN Gateway interactive map and NBN Gateway logo. </p>
        <p>To comply with the terms of use of this service the link to the terms and conditions, acknowledgement list of data providers and NBN Gateway logo must be displayed on your site. However they can be turned off if you prefer to include them elsewhere on your site or are restricting the map to just your datasets and do not wish to include the acknowledgement list of data providers.</p>
        <table class="attributeTable">
            <tr class="head">
                <th>Parameter Name</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
            <tr>
                <td><b>title</b></td>
                <td><b>sci</b>, <b>com</b>, <b>0</b></td>
                <td>Displays either the scientific name or common name as a title or disables it. If omitted the scientific name is displayed</td>
            </tr>
            <tr>
                <td><b>link</b></td>
                <td><b>0</b></td>
                <td>Disables the link to the NBN Gateway interactive map. By default the link is displayed</td>
            </tr>
            <tr>
                <td><b>ref</b></td>
                <td><b>0</b></td>
                <td>Disables the list of datasets. By default the list of datasets are shown.</td>
            </tr>
            <tr>
                <td><b>logo</b></td>
                <td><b>0</b></td>
                <td>Disables the NBN Gateway logo. By default the logo is shown.</td>
            </tr>
            <tr>
                <td><b>maponly</b></td>
                <td><b>1</b></td>
                <td>Setting maponly=1 displays the map only.</td>
            </tr>    
        </table>

        <h2>Customising the text</h2>
        <p>To change the appearance and layout of the text returned by the service, the full path to your Cascading Style Sheet can be included in the query string. For example</p>
        <pre><code class="prettyPrint">https://data.nbn.org.uk/EasyMap?tvk=NHMSYS0000344177&css=http://www.mywebaddress.org.uk/mystylesheet.css</code></pre>

        <h2>Example</h2>
        <p>This example displays a map for Small Red-eyed Damselfly, restricted to a single dataset supplied by Dragonfly Recording Network. The map size has been increased and a 100km grid included.</p>
        <pre><code class="prettyPrint">https://data.nbn.org.uk/EasyMap?tvk=NHMSYS0000344177&w=600&h=600&gd=100km&ds=GA000012</code></pre>
    </jsp:attribute>
</t:webserviceDocumentationPage>
