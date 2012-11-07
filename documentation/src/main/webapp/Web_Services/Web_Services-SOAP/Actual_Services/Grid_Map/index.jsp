<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Grid Map web service lets you incorporate NBN style grid maps, for example <a href="http://data.nbn.org.uk/gridMap/gridMap.jsp?allDs=1&amp;srchSpKey=NHMSYS0000461871">Polygonum oxyspermum grid map</a> into your websites. The web service lets you customise various features such as date classes, size and colours. You can obtain a British Isles map or zoom to a Vice County. The <a href="#Request">request</a> section describes the full list of parameters available.</p>
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to obtain a Grid map from the web services, a Grid map request element must be formed and sent to the web services. The following page will describe the constructuon of the GridMapRequest element.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the GridMapRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">GridMapRequest</div>
                <img src="../images/GridMapRequest.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetList</div>
                <img src="../images/DatasetList.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">GridMapSettings</div>
                <img src="../images/GridMapSettings.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Classification</div>
                <img src="../images/Classification.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Band</div>
                <img src="../images/Band.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">GeographicalFilter</div>
                <img src="../images/GeographicalFilter.png" />
			</div>
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
                    <th>TaxonVersionKey</th>
                    <td>Yes</td>
                    <td>A taxon version key identifying the species which is to be mapped. This is the only mandatory parameter for the Grid map query.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>No</td>
                    <td>Specifies a list of datasets to query against. If omitted, all datasets which are available to the current user and have data for the requested species will be queried.</td>
                </tr>
                <tr>
                    <th>Resolution</th>
                    <td>No</td>
                    <td>
                        <p>You can specify the resolution at which you want to view records, for example 10km, 2km, 1km or 100m square. You should be aware that for higher resolution squares:</p>
                        <ul>
                            <li>Less data may be available as fewer records are recorded to higher resolution.</li>
                            <li>100m and 1km squares will look very small and be difficult to view for national maps. Their use is recommended for vice county maps only.</li>
                        </ul>
                        <p>The legal values for this parameter are :</p>
                        <ul>
                            <li>_100m</li>
                            <li>_1km</li>
                            <li>_2km</li>
                            <li>_10km</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>Width</th>
                    <td>No</td>
                    <td>Specify the image width in pixels. Depending on connection speed, larger maps may be slower to load. Not specifying a GridMapSettings element will provide a map with the default width of 350px.</td>
                </tr>
                <tr>
                    <th>Height</th>
                    <td>No</td>
                    <td>Specify the image height in pixels. Depending on connection speed, larger maps may be slower to load. Not specifying a GridMapSettings element will provide a map with the default height of 350px.</td>
                </tr>
                <tr>
                    <th>Grid</th>
                    <td>No</td>
                    <td>
                        <p>You can specify OSGB/OSNI 10km or 100km grid squares as an overlay to the map. The legal values for this element are:</p>
                        <ul>
                            <li>None (default) - no grids are shown</li>
                            <li>Grid_10km - display 10km grid squares</li>
                            <li>Grid_100km - display 100km grid squares</li>
                            <li>Grid_10km_100km - display 10km and 100km grid squares</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>Background</th>
                    <td>No</td>
                    <td>
                        <p>You can optionally set either an Ordnace Survey map or Vice County boundary as a background for your map. The legal values for this element are:</p>
                        <ul>
                            <li>None - This is the default value</li>
                            <li>OSMap</li>
                            <li>ViceCounty</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>ViceCounty</th>
                    <td>No</td>
                    <td>
                        <p>You can zoom the map to a Vice County extent. To filter by vice county use its ID. Note, using this parameter only zooms the map into the vice counties rectangular extent, it does not filter records, so you may see records which fall outside the Vice County boundary.</p>
                    </td>
                </tr>
                <tr>
                    <th>Region</th>
                    <td>No</td>
                    <td>
                        <p>Sets the geographical extent of the map; Great Britain only, Ireland only or Great Britain and Ireland together. Note, if you map just Britain the web service only removes the Irish coastline from the map, any Irish Records will still be displayed. The legal query parameters are:</p>
                        <ul>
                            <li>GBIreland (default) - displays Britain and Irish coastline and records</li>
                            <li>Ireland - zooms the map to Ireland, only Irish records display</li>
                            <li>GB - display the British coastline only. Any Irish records <em>will</em> still display.</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>FillColour</th>
                    <td>No</td>
                    <td>Sets the colour for squares using hexidecimal format. Note, if Classification bands are set the FillColour is ignored.</td>
                </tr>
                <tr>
                    <th>Classification / Band</th>
                    <td>No</td>
                    <td>
                        <p>Defines one to three date classes for the map, specified by a 'from' and 'to' year. The data falling within each class is plotted using the colour set for it. Date classes are defined using the Band element. The first Banddefined in the XML is plotted last and therefore masks anything plotted underneath it. If you complete only one or other of the years it will define all data before that date (if 'to' is set) or all data after that date (if a 'from' is set).</p>
                        <p>The attributes of the Band element are:</p>
                        <ul>
                            <li>from - the start year for the date class (inclusive)</li>
                            <li>to - the end year for the date class (inclusive)</li>
                            <li>fill - the fill colour for squares, hexidecimal format</li>
                            <li>border - the outline colour for squares, hexidecimal format (black is the default colour)</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>GeographicalFilter</th>
                    <td>No</td>
                    <td>
                        <p>Lets you define a spatial area of the UK or Ireland for to force the GridMap Webservice to zoom to this area. The GridMap Webservice will deliver an image with the extent you have defined in your geographical filter, the standard being to show the full extent of the UK and Ireland. A spatial area can only be defined using a bounding box, the other types are not implemented.</p>
                        <p>For a full description refer to the <a href="../../Schema_Elements/#GeographicalFilter">GeographicalFilter documentation</a>.</p>
                    </td>
                </tr>
            </tbody>
        </table>

        <h3>Example</h3>
        <p>The following is an example of a constructed GridMapRequest XML element which could be sent to the GridMap web service.</p>
        <p>This particular example will classify the records of the taxon which has the ID "NHMSYS0000376160" into three bands, 1900-1970, 1971-1995, 1996-2006. The results of this will be visualised on a 500px by 500px gridded OSMap focused on the Great Britain and Ireland.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:map="http://www.nbnws.net/Map" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary">
   <soapenv:Header/>
   <soapenv:Body>
      <map:GridMapRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
         <tax:TaxonVersionKey>NHMSYS0000376160</tax:TaxonVersionKey>
         <map:Resolution>_2km</map:Resolution>
         <map:GridMapSettings>
            <map:Width>500</map:Width>
            <map:Height>500</map:Height>
            <map:Grid>Grid_100km</map:Grid>
            <map:Background>OSMap</map:Background>
            <map:Region>GBIreland</map:Region>
         </map:GridMapSettings>
         <map:Classification>
            <map:Band border="#FF0000" fill="#FF0000" from="1900" to="1970"/>
            <map:Band border="#00FF00" fill="#00FF00" from="1971" to="1995"/>
            <map:Band border="#0000FF" fill="#0000FF" from="1996" to="2006"/>
         </map:Classification>
      </map:GridMapRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The response from the Grid map web services will provide a URL which can be used to obtain the map which was generated to fulfil this request. Along with the map, the response can provide a summary of the datasets which were used to complete the request and also details on the Taxon which was queried.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the GridMap element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">GridMap</div>
                <img src="../images/GridMap.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Map</div>
                <img src="../images/Map.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Species</div>
                <img src="../images/Species.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetSummaryList</div>
                <img src="../images/DatasetSummaryList.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetSummary</div>
                <img src="../images/DatasetSummary.png" />
			</div>
        </div>
        <h4>Element Descriptions</h4>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>GridMap</th>
                    <td>Root element for the grid map response XML. Note this has the attributes NBNLogo and TermsAndConditions. These should be displayed for any product using this web service.</td>
                </tr>
                <tr>
                    <th>Map</th>
                    <td>This element groups together the elements describing the map image.</td>
                </tr>
                <tr>
                    <th>URL</th>
                    <td>An URL to the grid map image. child element of Map.</td>
                </tr>
                <tr>
                    <th>Width</th>
                    <td>The image width in pixels. Child element of Map.</td>
                </tr>
                <tr>
                    <th>Height</th>
                    <td>The image height in pixels. Child element of Map.</td>
                </tr>
                <tr>
                    <th>Format</th>
                    <td>The image format mime type. Child element of Map.</td>
                </tr>
                <tr>
                    <th>BoundingBox</th>
                    <td>Defines the extent of the image as coordinates and the spatial reference system used. The bounding box spatially enables the map image. Child element of Map.</td>
                </tr>
                <tr>
                    <th>Species</th>
                    <td>Groups together naming information for the species being mapped.</td>
                </tr>
                <tr>
                    <th>ScientificName</th>
                    <td>The species scientific name. Child element of Species.</td>
                </tr>
                <tr>
                    <th>CommonName</th>
                    <td>The species common name, if it exists. Child element of Species.</td>
                </tr>
                <tr>
                    <th>Authority</th>
                    <td>The species naming authority. Child element of Species.</td>
                </tr>
                <tr>
                    <th>TaxonGroupName</th>
                    <td>The taxon group reporting name the species belongs to. Child element of Species.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>
                        <p>The list of datasets queried to generate the map image.</p>
                        <p>For a full description refer to the <a href="../Dataset_Summary_List/">DatasetSummaryList documentation</a>.</p>
                    </td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following example is what could be expected by querying the web services with the example request described in the <a href="#Request">request</a> page.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns9:GridMap TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns9:Map>
            <ns9:Url>http://www.testnbn.net/output/gridGBv4_la-nbntest11336244072.gif</ns9:Url>
            <ns9:Width>500</ns9:Width>
            <ns9:Height>500</ns9:Height>
            <ns9:Format>image/gif</ns9:Format>
            <ns2:BoundingBox minx="-405000.0" miny="-10000.0" maxx="915000.0" maxy="1310000.0" srs="EPSG_29903"/>
         </ns9:Map>
         <ns4:Species taxonVersionKey="NHMSYS0000376160">
            <ns4:ScientificName>Chiroptera</ns4:ScientificName>
            <ns4:Authority>Blumenbach, 1779</ns4:Authority>
            <ns5:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080085">terrestrial mammal</ns5:TaxonReportingCategory>
         </ns4:Species>
         <ns11:DatasetSummaryList>
            <ns11:DatasetSummary id="GA000690">
               <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="2010-03-09" gatewayId="GA000690">
                  <ns8:DatasetTitle>Worcestershire Wildlife Trust species data for owned and managed Reserves  within Worcestershire from date of first acquisition to present.</ns8:DatasetTitle>
                  <ns8:DatasetProvider>Worcestershire Biological Records Centre</ns8:DatasetProvider>
                  <ns8:Abstract>
                     <ns8:Description>Species records for WWT Reserves extracted from WWT files, all and any species records extracted.</ns8:Description>
                     <ns8:DataCaptureMethod>Species records in paper form held in the Reserves files from a variety of sources were entered directly into a copy of Recorder 2002 from 2003 to present. The source of the majority of the records WWT staff and volunteers as part of ongoing monitoring or simple field observations from local naturalists were entered.</ns8:DataCaptureMethod>
                     <ns8:DatasetPurpose>Adding records from WWT Reserves was a WWT priority.  Many are also SSSI&amp;#8217;s so making available data on these nationally important sites is vital to both the understanding of their biodiversity and of filling gaps across all taxonomic groups in Worcestershire.</ns8:DatasetPurpose>
                     <ns8:GeographicalCoverage>Not all WWT Reserves had large amounts of species records in the files. The majority of the records were assigned to a 100m site centroid for the site unless specific grid reference was provided or appropriate compartment mentioned.</ns8:GeographicalCoverage>
                     <ns8:TemporalCoverage>Some records recorded by year and others by exact date, all data extracted as presented in original WWT files.</ns8:TemporalCoverage>
                     <ns8:DataQuality>Following data entry, the plant records (which form the majority) are being checked by a local expert from the Worcestershire Flora Project together with all other botanical records submitted to WBRC.
All the records are checked against the current WWT Reserve boundaries to ensure grids fall within/close to boundaries.</ns8:DataQuality>
                     <ns8:AdditionalInformation>Worcestershire Biological Records Centre was established as an independent organisation to collate information on all of Worcestershire&amp;#8217;s wildlife. Data held by WBRC is accessible to everyone* from students and local residents to local authorities, conservation organisations and consultants. WBRC do not charge for data, but we do make a minimal charge to commercial enquirers to cover our administration costs in order to maintain the service.
&amp;lt;/br&amp;gt;
*Some information is confidential, either due to the sensitivity of the site, rarity of wildlife or because the recorder requested it.
&amp;lt;/br&amp;gt;
For more information please contact us records@wbrc.org.uk with a specific request.</ns8:AdditionalInformation>
                  </ns8:Abstract>
                  <ns8:AccessConstraints>Although the majority of the species records in this dataset were collected by staff or volunteers of Worcestershire Wildlife Trust, there are also records from local naturalists who have voluntarily provided their data to the organisation. Access to recorders names in this dataset is therefore restricted for the following reason: Disclosure of this information would adversely affect the interests of the person who provided the information. The provider is not under any legal obligation to supply the information to the public through the NBN Gateway and has not consented to its wider release.
a) Wider release would adversely affect the free flow of volunteered information from third parties for use by those that need to know for land use planning and conservation
b) Wider release would adversely affect financial support required to sustain the administration of volunteered recording activities, the collation of volunteered wildlife records and their continued management.
&amp;lt;/br&amp;gt;
The information may be made available under licence to approved individuals and organisations.  You may be required to either pay a one-off service charge or enter into a longer term funding arrangement with the data provider.</ns8:AccessConstraints>
                  <ns8:UseConstraints/>
               </ns8:ProviderMetadata>
               <ns11:Privileges>
                  <ns11:DownloadRawData>false</ns11:DownloadRawData>
                  <ns11:SensitiveRecordsAccess>false</ns11:SensitiveRecordsAccess>
                  <ns11:ViewAttributes>false</ns11:ViewAttributes>
                  <ns11:ViewRecorder>false</ns11:ViewRecorder>
                  <ns11:AccessResolution>_2km</ns11:AccessResolution>
               </ns11:Privileges>
            </ns11:DatasetSummary>
         </ns11:DatasetSummaryList>
      </ns9:GridMap>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>