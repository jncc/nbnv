<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway taxon reporting category list web service lets you create list of taxon reporting categories. For example, using this web service you can:</p>
        <ul>
            <li>list all taxon reporting categories recorded on the NBN Gateway.</li>
            <li>list the taxon reporting categories containing BAP species in a SSSI.</li>
            <li>list the taxon reporting categories recorded in a polygon/area of interest after 2000.</li>
        </ul>
        <p>The names and keys returned by this web service are those defined by the NHM list of taxon reporting categories. View the <a href="http://data.nbn.org.uk/library/webservices/taxonGroups.csv">reporting taxon category reference list</a>.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to obtain a list of Taxon Reporting categories a TaxonReportingCategoryListRequest XML structure needs to be created. The TaxonReportingCategoryListRequest element allows various filters to be applied so that only a list of Taxon Reporting categories which are of interest are returned. It should be noted that all the filters are optional, and not providing any will return details of all the Taxon Reporting Categories which are stored on the NBN Gateway. The details of these filters are provided below.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the TaxonReportingCategoryListRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">TaxonReportingCategoryListRequest</div>
                <img src="../images/TaxonReportingCategoryListRequest.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">GeographicalFilter</div>
                <img src="../images/GeographicalFilter.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DateRange</div>
                <img src="../images/DateRange.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetList</div>
                <img src="../images/DatasetList.png" />
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
                    <th>designation</th>
                    <td>No</td>
                    <td>This is an attribute of TaxonReportingCategoryListRequest, the root element. It is a filter to create a list of taxon reporting categories where one or more species belongs to some list designation, for example Biodiversity Action Plan.</td>
                </tr>
                <tr>
                    <th>GeographicalFilter</th>
                    <td>No</td>
                    <td>
                        <p>Lets you set define a spatial area and the behaviour of records over the area for a request. You can also specify whether or a not a map image of the query area is returned and style options for the map. A spatial area can be defined as:</p>
                        <ul>
                            <li>Bounding box</li>
                            <li>Point or Point and buffer</li>
                            <li>Simple (singlepart) or complex (multipart) polygon</li>
                            <li>10km Grid square, e.g. 'TL18'</li>
                            <li>Site known to the NBN Gateway, e.g. SSSI, Vice County</li>
                        </ul>
                        <p>For a full description refer to the <a href="../../Schema_Elements/#GeographicalFilter">GeographicalFilter documentation</a>.</p>
                    </td>
                </tr>
                <tr>
                    <th>DateRange</th>
                    <td>No</td>
                    <td>Lets you define a date range filter using a start and end year. For a full description refer to the <a href="../../Schema_Elements/#DateRange-query">DateRange documentation</a>.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>No</td>
                    <td>Lets you specify the datasets you want to query data from. If this element is omiited all datasets you have appropriate access to and have data are queried. For a full description refer to the <a href="../../Schema_Elements/#DatasetList">DatasetList documentation</a>.</td>
                </tr>
            </tbody>
        </table>

        <h3>Examples</h3>
        <p>Provided below are three examples of constructed TaxonReportingCategoryListRequest elements which can be used to query the Taxon Reporting Category List web service.</p>
        <h4>10km square</h4>
        <p>This query requests a list and map image for a 10km square where taxon reporting categories have records for BAP species between 1950 and 2000.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/TaxonReportingCategory" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:tax1="http://www.nbnws.net/Taxon" xmlns:dat="http://www.nbnws.net/Dataset">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:TaxonReportingCategoryListRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
         <spat:GeographicalFilter>
            <spat:GridSquare srs="EPSG_27700" key="TL18" maxx="0.0" maxy="0.0" minx="0.0" miny="0.0" />
            <map:MapSettings fillColour="#FF0000" fillTransparency="0.0" height="300" outlineColour="#000000" outlineWidth="1" width="300" />
         </spat:GeographicalFilter>
         <tax1:DateRange>
            <tax1:Start>1950</tax1:Start>
            <tax1:End>2000</tax1:End>
         </tax1:DateRange>
      </tax:TaxonReportingCategoryListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        <h4>Site Boundary</h4>
        <p>This query requests a list for the Site Boundary Eccup Reservoir SSSI using the datasets 'GA000091', 'GA000144' and 'GA000360'.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/TaxonReportingCategory" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:tax1="http://www.nbnws.net/Taxon" xmlns:dat="http://www.nbnws.net/Dataset">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:TaxonReportingCategoryListRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
         <spat:GeographicalFilter>
             <sit:SiteBoundary siteKey="1004008" providerKey="GA000339" />
         </spat:GeographicalFilter>
         <dat:DatasetList>
            <dat:DatasetKey>GA000091</dat:DatasetKey>
            <dat:DatasetKey>GA000144</dat:DatasetKey>
            <dat:DatasetKey>GA000360</dat:DatasetKey>
         </dat:DatasetList>
      </tax:TaxonReportingCategoryListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        <h4>Bounded Buffer</h4>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/TaxonReportingCategory" xmlns:spat="http://www.nbnws.net/Spatial" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:map="http://www.nbnws.net/Map" xmlns:tax1="http://www.nbnws.net/Taxon" xmlns:dat="http://www.nbnws.net/Dataset">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:TaxonReportingCategoryListRequest registrationKey="1e279d92fc31f267d185d4a726930bcf280c907d">
         <spat:GeographicalFilter>
             <spat:Buffer>
               <spat:Point srs="EPSG_27700" x="519700.0" y="279800.0"/>
               <spat:Distance>500.0</spat:Distance>
            </spat:Buffer>
            <map:MapSettings fillColour="#00ff00" fillTransparency="0.7" height="400" outlineColour="#000000" outlineWidth="2" width="400"/>
            <spat:OverlayRule>within</spat:OverlayRule>
            <spat:MinimumResolution>_1km</spat:MinimumResolution>
         </spat:GeographicalFilter>
      </tax:TaxonReportingCategoryListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The response of the Taxon Reporting Category List web service is provided in the form of a  TaxonReportingCategoryListResponse XML structure. The TaxonReportingCategoryListResponse element is described below</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the TaxonReportingCategoryListResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">TaxonReportingCategoryListResponse</div>
                <img src="../images/TaxonReportingCategoryListResponse.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Map</div>
                <img src="../images/Map.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">TaxonReportingCategoryList</div>
                <img src="../images/TaxonReportingCategoryList.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">TaxonReportingCategory</div>
                <img src="../images/TaxonReportingCategory.png" />
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
                    <th>TaxonReportingCategoryListResponse</th>
                    <td>
                        <p>Root element for the Taxon Reporting Category List response XML. It contains these attributes:</p>
                        <ul>
                            <li>NBNLogo - a link to the NBN logo, which must be displayed on products using this web service</li>
                            <li>termsAndConditions - a link to the NBN terms and conditions, which must be added to products using this web service</li>
                            <li>RecordsFound - a boolean flag indicating whether records were found for this query</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>Map</th>
                    <td>
                        <p>Contains information for a requested map image of the area searched. A map is only included if the request specified MapSettings.</p>
                        <p>For a full description refer to the <a href="../../Schema_Elements/#map">Map documentation</a>.</p>
                    </td>
                </tr>
                <tr>
                    <th>TaxonReportingCategoryList</th>
                    <td>Contains a list of TaxonReportingCategory elements for each taxon reporting category found matching the request conditions.</td>
                </tr>
                <tr>
                    <th>TaxonReportingCategory</th>
                    <td>The name of an NBN taxon reporting category. It has one attribute,taxonReportingCategoryKey.</td>
                </tr>
                <tr>
                    <th>taxonReportingCategoryKey</th>
                    <td>The 16 character NBN taxon reporting category key.</td>
                </tr>
                <tr>
                    <th>DatasetSummaryList</th>
                    <td>
                        <p>The list of datasets queried to generate the list.</p>
                        <p>For a full description refer to the <a href="../Dataset_Summary_List/#Response">DatasetSummaryList</a> response documentation.</p>
                    </td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example response which could be expected from this web service. This particular example is a response to the Site Boundary request, described in the Examples section on the <a href="#Request">Request</a> page.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns4:TaxonReportingCategoryListResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/Designation" xmlns:ns2="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns3="http://www.nbnws.net/Taxon" xmlns:ns4="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns5="http://www.nbnws.net/Habitat" xmlns:ns6="http://www.nbnws.net/Metadata" xmlns:ns7="http://www.nbnws.net/Spatial" xmlns:ns8="http://www.nbnws.net/SiteBoundary" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns4:TaxonReportingCategoryList>
            <ns4:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080047">conifer</ns4:TaxonReportingCategory>
            <ns4:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080098">fern</ns4:TaxonReportingCategory>
            <ns4:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080054">flowering plant</ns4:TaxonReportingCategory>
            <ns4:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000629145">horsetail</ns4:TaxonReportingCategory>
            <ns4:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080083">liverwort</ns4:TaxonReportingCategory>
            <ns4:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080090">moss</ns4:TaxonReportingCategory>
         </ns4:TaxonReportingCategoryList>
         <ns11:DatasetSummaryList>
            <ns11:DatasetSummary id="GA000091">
               <ns6:ProviderMetadata exchangeFormatVersion="1" datestamp="24/04/2008" gatewayId="GA000091">
                  <ns6:DatasetTitle>Vascular Plants Database</ns6:DatasetTitle>
                  <ns6:DatasetProvider>Botanical Society of the British Isles</ns6:DatasetProvider>
                  <ns6:Abstract>
                     <ns6:Description>This database of 9.8 million records contains the distributions of 6669 taxa of flowering plants and ferns. Most of these records have been collected by volunteer members of the Botanical Society of the British Isles (BSBI) and include those gathered for the Atlas of the British Flora (Perring &amp;amp; Walters, 1962) and the New Atlas of the British and Irish flora (Preston, Pearman &amp;amp; Dines, 2002). The latter is a collation of many datasets (including those held by BSBI vice-county recorders), some included in this database in full and others as summary data; the major sources of data are outlined in the New Atlas text. The availability of data at resolutions higher than 10km square varies between sources. The collated data have been validated at the 10km square level by BSBI Vice-County recorders and the editors for those taxa mapped in the New Atlas, and were believed to provide a good representation of vascular plant distributions in Britain and Ireland at the time of publication. Records of infraspecific taxa and hybrids which were not mapped in the New Atlas have been scrutinised less closely. An update of around 900,000 records was made to the dataset in January 2007, to include the data from the BSBI Local Change survey (Braithwaite et al., 2006), data from the Flora of Oxfordshire (Killick et al., 1998) and the Isle of Wight Flora (Pope et al., 2003), records of hybrid plants and a variety of other records. Some corrections to the New Atlas dataset have also been made.</ns6:Description>
                     <ns6:DataCaptureMethod>The records are compiled from many sources and do not conform to any one method. Nearly all will include a 10km square but quite often other information is missing.</ns6:DataCaptureMethod>
                     <ns6:DatasetPurpose>To produce Atlases at 10km resolution.</ns6:DatasetPurpose>
                     <ns6:GeographicalCoverage>Britain &amp;amp; Ireland, the Channel Isles and Isle of Man.</ns6:GeographicalCoverage>
                     <ns6:TemporalCoverage>1629-2006</ns6:TemporalCoverage>
                     <ns6:DataQuality>Records are variable in quality. Many records are detailed, with six-figure grid references and full dates, but many are not. A certain level of duplication can be expected. Experience is needed in interpretation.</ns6:DataQuality>
                     <ns6:AdditionalInformation><![CDATA[&lt;br&gt;&lt;br&gt;
References
&lt;br&gt;&lt;br&gt;
BRAITHWAITE, M. E., ELLIS, R. W. &amp; PRESTON, C. D. (2006) Change in the British Flora 1987-2004. Botanical Society of the British Isles, London. &lt;br&gt;
KILLICK, J., PERRY, R. &amp; WOODELL, S. (1998) The Flora of Oxfordshire. Nature Conservation Bureau, Newbury. &lt;br&gt;
PERRING, F. H. &amp; WALTERS, S. M. (Eds.) (1962) Atlas of the British Flora, London, Thomas Nelson &amp; Sons. &lt;br&gt;
POPE, C., SNOW, L. &amp; ALLEN, D. E. (2003) The Isle of Wight Flora. Dovecote  Press, Wimborne. &lt;br&gt;
PRESTON, C. D., PEARMAN, D. A. &amp; DINES, T. D. (2002) New atlas of the British and Irish flora. Oxford University Press, Oxford.]]></ns6:AdditionalInformation>
                  </ns6:Abstract>
                  <ns6:AccessConstraints>Access is normally set to 10km resolution, which is how the information was collected and displayed in the New Atlas. Finer levels of resolution are not normally allowed, in order to prevent users creating their own databases, adversely affecting the BSBI&amp;apos;s ability to produce definitive maps. Legitimate researchers and those with comprehensive data exchange agreements with the BSBI are permitted full access.</ns6:AccessConstraints>
                  <ns6:UseConstraints/>
               </ns6:ProviderMetadata>
               <ns11:Privileges>
                  <ns11:DownloadRawData>true</ns11:DownloadRawData>
                  <ns11:SensitiveRecordsAccess>false</ns11:SensitiveRecordsAccess>
                  <ns11:ViewAttributes>false</ns11:ViewAttributes>
                  <ns11:ViewRecorder>false</ns11:ViewRecorder>
                  <ns11:AccessResolution>_10km</ns11:AccessResolution>
               </ns11:Privileges>
            </ns11:DatasetSummary>
            <ns11:DatasetSummary id="GA000144">
               <ns6:ProviderMetadata exchangeFormatVersion="1" datestamp="20/07/2010" gatewayId="GA000144">
                  <ns6:DatasetTitle>Bryophyte data for Great Britain from the British Bryological Society held by BRC</ns6:DatasetTitle>
                  <ns6:DatasetProvider>British Bryological Society</ns6:DatasetProvider>
                  <ns6:Abstract>
                     <ns6:Description>Bryophyte records submitted to the Biological Records Centre for England, Ireland, Scotland and Wales. The dataset comprises records collected for the Bryophyte Recording Scheme, as well as those extracted from literature and museum sources. This dataset was updated with two new surveys in February 2006 which contain around 50000 records for Carmarthenshire and Pembrokeshire. Details of all 90 surveys in this dataset can be obtained by clicking the Survey tab. An update of 130,000 records was made to the dataset in September 2006. This includes ten new surveys covering various taxa and localities including Coll &amp;amp; Tiree, Suffolk, Isle of Wight, Northamptonshire and Pembrokeshire. The record span of these new surveys is from 1800-2006. An update of over 200,000 records was made to the dataset in April 2007, including ten new surveys spanning 1744-2007.</ns6:Description>
                     <ns6:DataCaptureMethod>Mainly from record cards, which were typed up at the Biological Records Centre.  More recent data (since 2000) have mostly been sent electronically.</ns6:DataCaptureMethod>
                     <ns6:DatasetPurpose>For atlas production and inclusion in BRC database.</ns6:DatasetPurpose>
                     <ns6:GeographicalCoverage>Great Britain, Ireland, Isle of Man, Channel Islands</ns6:GeographicalCoverage>
                     <ns6:TemporalCoverage>The records span the period 1663 (extracted from literature) to 2006. A new atlas and complete update of data are planned for about 2010.</ns6:TemporalCoverage>
                     <ns6:DataQuality>The data were validated against known vice-county distributions of the species, and (for critical species) by expert examination of specimens.</ns6:DataQuality>
                     <ns6:AdditionalInformation>Hill MO, Preston CD, Smith AJE. 1991-1994. Atlas of the bryophytes of Britain and Ireland, Vol. 1 (1991), Vol. 2 (1992), Vol. 3 (1994). Colchester: Harley Books.</ns6:AdditionalInformation>
                  </ns6:Abstract>
                  <ns6:AccessConstraints>Eleven species Adelanthus lindenbergianus, Athalamia hyalina,Bruchia vogesiaca, Bryum schleicheri, Cyclodictyon laetevirens, Ditrichum cornubicum, Jamesoniella undulifolia, Leiocolea rutheana, Paludella squarrosa, Thamnobryum angustifolium, Weissia multicapsularis are thought to be vulnerable to collecting.  These are marked as confidential, so that the records are not visible to the public.  This sensitive information may be made available to specific organisations and individuals that need to know to avoid harm to the environment.</ns6:AccessConstraints>
                  <ns6:UseConstraints>When these data are used in publications and lectures, the British Bryological Society and the Biological Records Centre should be acknowledged.</ns6:UseConstraints>
               </ns6:ProviderMetadata>
               <ns11:Privileges>
                  <ns11:DownloadRawData>true</ns11:DownloadRawData>
                  <ns11:SensitiveRecordsAccess>false</ns11:SensitiveRecordsAccess>
                  <ns11:ViewAttributes>true</ns11:ViewAttributes>
                  <ns11:ViewRecorder>true</ns11:ViewRecorder>
                  <ns11:AccessResolution>_100m</ns11:AccessResolution>
               </ns11:Privileges>
            </ns11:DatasetSummary>
         </ns11:DatasetSummaryList>
      </ns4:TaxonReportingCategoryListResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>