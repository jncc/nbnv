<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Species List web service provides a list of species for your specified criteria. It is commonly used to support&nbsp;web pages that display a list of species for an area of interest. Here is an overview of the full list of filters you can use to restrict the species returned:</p>
        <ul>
            <li>Species designation (e.g. Biodiversity Action Plan 2007)</li>
            <li>Area of interest (e.g. polygon, bounding box, point-buffer, SSSI, etc)</li>
            <li>Year range</li>
            <li>List of datasets</li>
            <li>One taxonomic group (e.g. butterflies)</li>
            <li>List of species&nbsp;</li>
        </ul>
        <p>&nbsp;</p>
    </jsp:attribute>
    <jsp:attribute name="request">
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the SpeciesListRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SpeciesListRequest</div>
                <img src="../images/SpeciesListRequest.png" /></div>
        </div>
        <h4>Element Descriptions</h4>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Required?</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>sort</th>
                    <td>O</td>
                    <td>This sets the sort order of the response to Scientiifc or Common name. It accepts the values 'Scientific' or 'Common'. If Common is specified then all species without a common name will follow those that do.</td>
                </tr>
                <tr>
                    <th>designation</th>
                    <td>O</td>
                    <td>This is an attribute of <code>SpeciesListRequest</code>. It restricts the species in the list to just those with the designation requested. For more information about what species designations are see <a href="http://www.jncc.gov.uk">JNCC's website</a>. Full details of designations in use on the gateway are available from the <a href="../Designation_List/"><code>designation</code> web service</a>.</td>
                </tr>
                <tr>
                    <th>GeographicalFilter</th>
                    <td>O</td>
                    <td>Lets you define a spatial area and the behaviour of records over the area for a request. You can also specify whether or a not a map image of the query area is returned and style options for the map. A spatial area can be defined as:
                        <ul style="list-style-type: circle">
                            <li>Bounding box</li>
                            <li>Point or Point and buffer</li>
                            <li>Simple (singlepart) or complex (multipart) polygon</li>
                            <li>10km Grid square, e.g. 'TL18'</li>
                            <li>Site known to the NBN Gateway, e.g. SSSI, Vice County</li>
                        </ul>
                        For a full description refer to the <a href="../../Schema_Elements/#GeographicalFilter"><code>GeographicalFilter</code> documentation</a>.</td>
                </tr>
                <tr>
                    <th>Date Range</th>
                    <td>O</td>
                    <td>Lets you define a date range filter using a start and end year. For a full description refer to the <a href="../../Schema_Elements/#DateRange-query"><code>DateRange</code> an&gt;documentation</a>.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>O</td>
                    <td>Lets you specify the datasets you want to query data from. If this element is omitted all datasets you have appropriate access to are queried. For a full description refer to the <a href="../../Schema_Elements#DatasetList"><code>DatasetList</code>documentation</a>.</td>
                </tr>
                <tr>
                    <th>TaxonReportingCategoryKey</th>
                    <td>O</td>
                    <td>Lets you filter the species list to an NBN Taxon Reporting Category (e.g. amphibians) using its key. You can only filter by one category.
                        <p><a href="../../Resources">Download</a> the list of NBN Taxon Reporting Category names and keys.</p>
                    </td>
                </tr>
                <tr>
                    <th>TaxonVersionKeys</th>
                    <td>O</td>
                    <td>Lets you filter the species list against a list of species you are interested in.</td>
                </tr>
            </tbody>
        </table>
        <p>&nbsp;</p>

        <h3>Example 1: taxon group species list</h3>
        <p>Creates a BAP species list for butterflies. There is no geographical filtering.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:SpeciesListRequest registrationKey="YOUR_REGISTRATION_KEY" designation="BIODIVERSITY_ACTION_PLAN">
         <tax1:TaxonReportingCategoryKey>NHMSYS0000080067</tax1:TaxonReportingCategoryKey>
      </tax:SpeciesListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>

        <h3>Example 2: 10km square species list</h3>
        <p>Creates a species list and map for a 10km grid square square where species have been recorded between 1950 and 2000.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
  <soapenv:Header />
  <soapenv:Body>
    <tax:SpeciesListRequest registrationKey="YOUR_REGISTRATION_KEY" sort="Scientific">
      <spat:GeographicalFilter>
        <spat:GridSquare srs="EPSG_27700" key="TL18" />
        <map:MapSettings height="300" width="300" outlineWidth="1" outlineColour="#000000" fillColour="#FF0000" fillTransparency="0.5" mapOnly="false" />
      </spat:GeographicalFilter>
      <tax:DateRange>
        <tax:Start>1950</tax:Start>
        <tax:End>2000</tax:End>
      </tax:DateRange>
    </tax:SpeciesListRequest>
  </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>

        <h3>Example 3: site boundary</h3>
        <p>Creates a species list for the SSSI 'Monks wood &amp; the odd quarter'. It restricts the results to just records contained completely by Monks Wood and that are no larger than 1km. It is also restricted to the two datasets GA000144 and GA000145.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:SpeciesListRequest registrationKey="YOUR_REGISTRATION_KEY">
         <spat:GeographicalFilter>
            <sit:SiteBoundary siteKey="1002775" providerKey="GA000339" />
            <spat:OverlayRule>within</spat:OverlayRule>
            <spat:MinimumResolution>_1km</spat:MinimumResolution>
         </spat:GeographicalFilter>
         <dat:DatasetList>
            <dat:DatasetKey>GA000144</dat:DatasetKey>
            <dat:DatasetKey>GA000145</dat:DatasetKey>
         </dat:DatasetList>
      </tax:SpeciesListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>

        <h3>Example 4: user defined polygon species list</h3>
        <p>This query returns a list of species recorded in a user defined polygon area.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:SpeciesListRequest registrationKey="1e279d92fc31f267d185d4a726930bcf280c907d">
         <spat:GeographicalFilter>
            <spat:Polygon srs="EPSG_27700">
               <spat:Boundary>
                  <spat:Ring>
            <spat:v srs="EPSG_27700" x="519222.0" y="279259.0"/>
            <spat:v srs="EPSG_27700" x="518988.0" y="279651.0"/>
            <spat:v srs="EPSG_27700" x="519388.0" y="280590.0"/>
            <spat:v srs="EPSG_27700" x="520173.0" y="280847.0"/>
            <spat:v srs="EPSG_27700" x="520690.0" y="279727.0"/>
            <spat:v srs="EPSG_27700" x="520467.0" y="279745.0"/>
            <spat:v srs="EPSG_27700" x="520363.0" y="279911.0"/>
            <spat:v srs="EPSG_27700" x="519222.0" y="279259.0"/>
                  </spat:Ring>
               </spat:Boundary>
            </spat:Polygon>
            <map:MapSettings height="400" width="400" outlineWidth="2" outlineColour="#000000" fillColour="#00ff00" fillTransparency="0.7" />
            <spat:MinimumResolution>_1km</spat:MinimumResolution>
         </spat:GeographicalFilter>
      </tax:SpeciesListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>

        <h3>Example 5:  species list for a predefined list of species</h3>
        <p>This query requests a species list for the River Barle SSSI.  The species list is restricted to <em>Agapetus delicatulus</em> (NBNSYS0000008346), <em>Aeshna cyanea</em> (NBNSYS0000005626) and <em>Bombylius canescens</em>(NBNSYS0000007941)</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:SpeciesListRequest registrationKey="YOUR_REGISTRATION_KEY">
         <spat:GeographicalFilter>
            <sit:SiteBoundary siteKey="1006540" providerKey="GA000339">
            </sit:SiteBoundary>
         </spat:GeographicalFilter>
         <tax:TaxonVersionKeys>NBNSYS0000008346,NBNSYS0000005626,NBNSYS0000007941</tax:TaxonVersionKeys>
      </tax:SpeciesListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The XML Elements for a response from the species list web service are described below. An example of response XML data is given after.</p>
        <h3>Graphical Representation</h3>
		<p>The following is a graphical representation of the structure of the SpeciesListResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
		<div class="graphicalRepresentation">
			<div class="graphicalRepresentationElement">
				<div class="caption">SpeciesListResponse</div>
				<img src="../images/SpeciesListResponse.png" /></div>
		</div>
        <table border="0" cellspacing="1" cellpadding="3" bgcolor="#c0c0c0">
            <tbody>
                <tr>
                    <td bgcolor="#f0f0f0" colspan="2" align="left" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Element</strong></td>
                    <td bgcolor="#f0f0f0" align="left" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Comments</strong></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" colspan="2" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>SpeciesListResponse</strong></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">Root element for the species list response XML. It contains these attributes:
                        <ul style="list-style-type: circle">
                            <li><code>NBNLogo</code>: a link to the NBN logo, which must be displayed on products using this web service</li>
                            <li><code>termsAndConditions</code>: a link to the NBN terms and conditions, which must be added to products using this web service</li>
                            <li><code>RecordsFound</code>: a boolean flag indicating whether records were found for this query</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" colspan="2" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Map</strong></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">Contains information for a requested map image. The image will be of the area searched and displaying the area boundary, optionally over an OS backdrop. A map is only included if the request specified<span class="Apple-converted-space">&nbsp;</span><code>MapSettings</code>.
                        <p style="color: rgb(51,51,51); font-size: 1em">For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../../Schema_Elements/#map"><code>Map</code><span class="Apple-converted-space">&nbsp;</span>documentation</a></p>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" colspan="2" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>SpeciesList</strong></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">Contains a list of<span class="Apple-converted-space">&nbsp;</span><code>Species</code><span class="Apple-converted-space">&nbsp;</span>elements.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" colspan="2" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Species</strong></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">Element containing information about a species including its name, taxon version key and authority. In version 3.4 the SpeciesAttributes tag now returns number of records, temporal coverage and if a dataset with download privilege exists.
                        <p style="color: rgb(51,51,51); font-size: 1em">For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../../Schema_Elements/#Species"><code>Species</code><span class="Apple-converted-space">&nbsp;</span>documentation</a></p>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" colspan="2" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>DatasetSummaryList</strong></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">The list of datasets queried to generate the taxon group list.
                        <p style="color: rgb(51,51,51); font-size: 1em">For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../Dataset_Summary_List/"><code>DatasetSummaryList</code><span class="Apple-converted-space">&nbsp;</span>documentation</a></p>
                    </td>
                </tr>
            </tbody>
        </table>
        <p>&nbsp;</p>
        <h3>Example response XML</h3>
        <nbn:prettyprint-code lang="xml">
<SpeciesListResponse targetNamespace="http://webservices.searchnbn.net/data" version="1.1" xmlns="http://webservices.searchnbn.net/data" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsd="http://www.w3.org/2001/XMLSchema" termsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" RecordsFound="true" >
	<Map>
		<Url>http://www.nbnws.net/output/28444134.gif</Url>
		<Width>300</Width>
		<Height>300</Height>
		<Format>image/gif</Format>
		<BoundingBox maxx="519999.999296" maxy="289999.999534" minx="509999.999296" miny="279999.999534" srs="osgb_BNG"/>
	</Map>
	<SpeciesList>
	<Species taxonVersionKey="NBNSYS0000002445">
			<ScientificName>Carex ericetorum<ScientificName>
			<CommonName>Rare Spring-sedge<CommonName>
			<Authority>Pollich<Authority>
			<TaxonReportingCategory
				taxonReportingCategoryKey="NHMSYS0000080054">
				flowering plant
			<TaxonReportingCategory>
		</Species>
	...
	<Species taxonVersionKey="NBNSYS0000002443">
			<ScientificName>Carex lasiocarpa<ScientificName>
			<CommonName>Slender Sedge<CommonName>
			<Authority>Ehrh.<Authority>
			<TaxonReportingCategory
				taxonReportingCategoryKey="NHMSYS0000080054">
				flowering plant
			<TaxonReportingCategory>
		</Species>
	</SpeciesList>
	<DatasetSummaryList>
	...
	</DatasetSummaryList>
</SpeciesListResponse>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>