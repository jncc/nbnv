<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The Species Density Data web service provides counts of records and species grouped by grid square. You can choose a cell size of 100m, 1km, 2km or 10km for your grid. This allows you to look at data on the NBN Gateway in terms of species richness and record density.</p>
        <p>This web service has the usual set of filters to allow you to target your query to the data you are interested in. It is recommended that you use these filters wisely to prevent large volumes of data being returned and to ensure you get a meaningful response. Here is an overview of these filters:</p>
        <ul>
            <li>Species designation (e.g. Biodiversity Action Plan 2007)</li>
            <li>Area of interest (e.g. polygon, bounding box, point-buffer, SSSI, etc )</li>
            <li>Year range</li>
            <li>List of datasets</li>
            <li>One taxonomic group (e.g. butterflies)</li>
            <li>List of species</li>
        </ul>
        <p>The response contains overall summary statistics and data for each square. The summary statistics have a species list, dataset list and total counts. The data for each square has the number of records and species, and also dates for earliest and latest records.</p>
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>The request's elements are shown below, followed by a detailed description.</p>
        <h3>Grapical Representation</h3>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SpeciesDensityDataRequest</div>
                <img src="../images/SpeciesDensityDataRequest.png" /></div>
        </div>
        <h3>Element Descriptions</h3>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>designation</th>
                    <td>This is an attribute of <code>SpeciesDensityDataRequest</code>. It restricts the species in the list to just those with the designation requested.&nbsp; For more information about what species designations are see <a href="http://jncc.defra.gov.uk/">JNCC's website</a>.&nbsp; Full details of designations in use on the gateway are availabe&nbsp;from the&nbsp;<a href="../Designation_List/"><code>designation</code><span class="Apple-converted-space">&nbsp;web service</span></a>.</td>
                </tr>
                <tr>
                    <th>GeographicalFilter</th>
                    <td>Lets you define an area of interest.&nbsp; This can be one of the following:
                        <ul style="list-style-type: circle">
                            <li>Bounding box</li>
                            <li>Point or point-buffer</li>
                            <li>Polygon: simple (single-part) or complex (multi-part)</li>
                            <li>10km grid square e.g. TL18</li>
                            <li>Site known to the NBN Gateway, e.g. a specific SSSI</li>
                        </ul>
                        <p>In addition to these filters you can request whether to include records that overlap any part of your area of interest, or to restrict it to just those that are completely contained by it.</p>
                        <p>Unlike many other web services on the NBN Gateway, this one does not allow you to obtain a map image of your area of interest, so the <span style="font-family: Courier New">MapSettings</span> element of the <span style="font-family: Courier New">GeographicalFilter</span> is ignored.</p>
                        <p>For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../../Schema_Elements/#GeographicalFilter"><code>GeographicalFilter</code><span class="Apple-converted-space">&nbsp;</span>documentation</a>.</p>
                    </td>
                </tr>
                <tr>
                    <th>Date Range</th>
                    <td>Lets you define a date range using a start and/or end year. For a full description refer to the <a href="../../Resources/resources/DateFiltering.pdf"><code>DateRange</code><span class="Apple-converted-space">&nbsp;</span>documentation</a>.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>Lets you specify the datasets you want to query data from. If this element is omitted all datasets you have appropriate access to are queried. For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../../Schema_Elements#DatasetList"><code>DatasetList</code><span class="Apple-converted-space">&nbsp;</span>documentation</a>.</td>
                </tr>
                <tr>
                    <th>TaxonReportingCategoryKey</th>
                    <td>Lets you filter the species list to an NBN Taxon Reporting Category (e.g. amphibians) using its key. You can only filter by one category.
                        <p style="font-size: 1em; color: rgb(51,51,51)"><a href="../../Resources/">Download</a><span class="Apple-converted-space">&nbsp;</span>the list of NBN Taxon Reporting Category names and keys.</p>
                    </td>
                </tr>
                <tr>
                    <th>TaxonVersionKeys</th>
                    <td>
                        <p>Lets you restrict your request to a list of target species.&nbsp; These are identified by TaxonVersionKeys.</p>
                    </td>
                </tr>
                <tr>
                    <th>AtResolution</th>
                    <td>
                        <p>This defines the cell size of the grid used to aggregate records to.&nbsp; The values allowed are:</p>
                        <ul style="list-style-type: circle">
                            <li>_10km</li>
                            <li>_2km</li>
                            <li>_1km</li>
                            <li>_100m</li>
                        </ul>
                        For example, using a value of &quot;_10km&quot; would give you species and record counts for 10km squares.
                        <p>&nbsp;</p>
                    </td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following example shows a SpeciesDensityData request for Dragonfly records inside the 10km square TL27 with records grouped by 1km squares.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:tax="http://www.nbnws.net/Taxon"
    xmlns:spat="http://www.nbnws.net/Spatial"
    xmlns:sit="http://www.nbnws.net/SiteBoundary"
    xmlns:map="http://www.nbnws.net/Map"
    xmlns:dat="http://www.nbnws.net/Dataset"
    xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
    <soapenv:Header/>
    <soapenv:Body>
        <tax:SpeciesDensityDataRequest registrationKey="YOUR_REGISTRATION_KEY">
            <spat:GeographicalFilter>
                <spat:GridSquare srs="EPSG_27700" key="TL27"/>
            </spat:GeographicalFilter>
            <tax1:TaxonReportingCategoryKey>NHMSYS0000080071</tax1:TaxonReportingCategoryKey>
            <tax:AtResolution>_1km</tax:AtResolution>
        </tax:SpeciesDensityDataRequest>
    </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        </jsp:attribute>
        <jsp:attribute name="response">
        <p>The response elements are shown below, followed by a detailed description.</p>
        <h3>Graphical representation</h3><div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SpeciesListResponse</div>
                <img src="../images/SpeciesListResponse.png" /></div>
        </div>
        <h3>Element Descriptions</h3>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>SpeciesListResponse</th>
                    <td>Root element for the species list response XML. It contains these attributes:
                        <ul style="list-style-type: circle">
                            <li><code>NBNLogo</code>: a link to the NBN logo, which must be displayed on products using this web service</li>
                            <li><code>termsAndConditions</code>: a link to the NBN terms and conditions, which must be added to products using this web service</li>
                            <li><code>RecordsFound</code>: a boolean flag indicating whether records were found for this query</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th><strong>Header</strong></th>
                    <td>Contains summary information about the response including status and number of records. For a full description refer to the <a href="../../Schema_Elements/#Header"><span style="font-family: Courier New"><code>Header</code></span><span class="Apple-converted-space">&nbsp;</span>documentation</a>.</td>
                </tr>
                <tr>
                    <th>SpeciesList</th>
                    <td>A list of the unique species found by the request.</td>
                </tr>
                <tr>
                    <th>AggregateSiteList</th>
                    <td>
                        <p style="font-size: 1em; color: rgb(51,51,51)"><font color="#000000">This is a list of the grid squares that the records have been aggregated by.&nbsp; It contains a list of AggregateSite elements.&nbsp; For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../../Schema_Elements/#aggregateSite"><code>AggregateSite</code> documentation<span class="Apple-converted-space">&nbsp;</span></a>.</font></p>
                    </td>
                </tr>
                <tr>
                    <th>DatasetSummaryList</th>
                    <td>The list of datasets queried to generate the taxon group list.
                        <p style="font-size: 1em; color: rgb(51,51,51)">For a full description refer to the<span class="Apple-converted-space">&nbsp;</span><a href="../../Schema_Elements/#DatasetSummaryList"><code>DatasetSummaryList</code><span class="Apple-converted-space">&nbsp;</span>documentation</a></p>
                    </td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Here is an example of a slightly fictitious response.&nbsp; It was taken from a real response.&nbsp; But, since a lot of data is returned, it was shortened by removing some species, sites and datasets to make it easier for you to read.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns4:SpeciesDensityDataResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
            <ns12:Header>
                <ns12:Status isError="false"/>
                <ns12:Summary>
                    <ns12:DatasetCount>1</ns12:DatasetCount>
                    <ns12:LocationCount>2</ns12:LocationCount>
                    <ns12:RecordCount>27</ns12:RecordCount>
                </ns12:Summary>
            </ns12:Header>
            <ns4:SpeciesList>
                <ns4:Species taxonVersionKey="NBNSYS0000005650">
                    <ns4:ScientificName>Sympetrum sanguineum</ns4:ScientificName>
                    <ns4:CommonName>Ruddy Darter</ns4:CommonName>
                    <ns4:Authority>(Muller, 1764)</ns4:Authority>
                    <ns5:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080071">insect - dragonfly (Odonata)</ns5:TaxonReportingCategory>
                </ns4:Species>
                <ns4:Species taxonVersionKey="NBNSYS0000005605">
                    <ns4:ScientificName>Coenagrion pulchellum</ns4:ScientificName>
                    <ns4:CommonName>Variable Damselfly</ns4:CommonName>
                    <ns4:Authority>(Vander Linden, 1825)</ns4:Authority>
                    <ns5:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080071">insect - dragonfly (Odonata)</ns5:TaxonReportingCategory>
                </ns4:Species>
            </ns4:SpeciesList>
            <ns12:AggregateSiteList>
                <ns12:AggregateSite>
                    <ns12:Site siteKey="TL2771">
                        <ns2:GridSquare key="TL2771" minx="527000.0" miny="271000.0" maxx="528000.0" maxy="272000.0" srs="EPSG_27700"/>
                    </ns12:Site>
                    <ns12:RecordCount>16</ns12:RecordCount>
                    <ns12:SpeciesCount>2</ns12:SpeciesCount>
                    <ns12:HasSensitiveData>false</ns12:HasSensitiveData>
                    <ns12:HasNonSensitiveData>true</ns12:HasNonSensitiveData>
                    <ns12:EarliestRecord>1989</ns12:EarliestRecord>
                    <ns12:LatestRecord>1989</ns12:LatestRecord>
                </ns12:AggregateSite>
                <ns12:AggregateSite>
                    <ns12:Site siteKey="TL2270">
                        <ns2:GridSquare key="TL2270" minx="522000.0" miny="270000.0" maxx="523000.0" maxy="271000.0" srs="EPSG_27700"/>
                    </ns12:Site>
                    <ns12:RecordCount>11</ns12:RecordCount>
                    <ns12:SpeciesCount>1</ns12:SpeciesCount>
                    <ns12:HasSensitiveData>false</ns12:HasSensitiveData>
                    <ns12:HasNonSensitiveData>true</ns12:HasNonSensitiveData>
                    <ns12:EarliestRecord>1987</ns12:EarliestRecord>
                    <ns12:LatestRecord>1987</ns12:LatestRecord>
                </ns12:AggregateSite>
            </ns12:AggregateSiteList>
            <ns11:DatasetSummaryList>
                <ns11:DatasetSummary id="BRCODON0">
                    <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="28/03/2001" gatewayId="BRCODON0">
                        <ns8:DatasetTitle>Dragonfly and Damselfly (Odonata) records for Britain to 1992, from the former Dragonfly Recording Scheme</ns8:DatasetTitle>
                        <ns8:DatasetProvider>Biological Records Centre</ns8:DatasetProvider>
                        <ns8:Abstract>
                            <ns8:Description>This dataset consists of the records collected and collated for the Odonata Recording Scheme (now the Dragonfly Recording Network), which led to the publication of the Atlas of the Dragonflies of Britain and Ireland (1996) by Merritt, Moore and Eversham.  The scheme was run in association with the Biological Records Centre, where the dataset is now held.  It contains not only the records made and submitted by the volunteer community, but also some from historical literature and museum sources.  The records span the period 1807 (one from literature) to 1992, with 82% from 1975 to 1988.</ns8:Description>
                            <ns8:DataCaptureMethod/>
                            <ns8:DatasetPurpose/>
                            <ns8:GeographicalCoverage>Great Britain, but not Ireland.</ns8:GeographicalCoverage>
                            <ns8:TemporalCoverage>The majority of records span 1888-1992 although there are a few records before these dates.</ns8:TemporalCoverage>
                            <ns8:DataQuality>All records have been checked and verified by the national scheme organiser and validated as part of BRC&amp;apos;s routine data acquisition process.</ns8:DataQuality>
                            <ns8:AdditionalInformation><![CDATA[Merritt. R., Moore, N.W. &amp; Eversham, B.C. 1996. Atlas of The Dragonflies of Britain and Ireland. London: HMSO &lt;br&gt;
&lt;a href=&quot;http://www.brc.ac.uk/publications.htm&quot;target=&quot;_blank&quot;&gt;http://www.brc.ac.uk/publications.htm&lt;/a&gt;&lt;br&gt;
&lt;a href=&quot;http://www.dragonflysoc.org.uk/&quot;target=&quot;_blank&quot;&gt;http://www.dragonflysoc.org.uk/&lt;/a&gt;]]></ns8:AdditionalInformation>
                        </ns8:Abstract>
                        <ns8:AccessConstraints>There are no access constraints</ns8:AccessConstraints>
                        <ns8:UseConstraints/>
                    </ns8:ProviderMetadata>
                    <ns11:Privileges>
                        <ns11:DownloadRawData>true</ns11:DownloadRawData>
                        <ns11:SensitiveRecordsAccess>true</ns11:SensitiveRecordsAccess>
                        <ns11:ViewAttributes>true</ns11:ViewAttributes>
                        <ns11:ViewRecorder>true</ns11:ViewRecorder>
                        <ns11:AccessResolution>_100m</ns11:AccessResolution>
                    </ns11:Privileges>
                </ns11:DatasetSummary>
            </ns11:DatasetSummaryList>
        </ns4:SpeciesDensityDataResponse>
    </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>