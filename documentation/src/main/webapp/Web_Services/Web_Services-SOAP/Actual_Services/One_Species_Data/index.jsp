<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway species data web service returns all available data for a single species. This web service does not let you apply a spatial filter, to do that you should use the one site data web service, but you can apply year and dataset filters.</p>
        <div class="warning">
            <p>When using the one species data web service there are two things you should be aware of: </p>
            <ol>
                <li>The web service returns actual record data, therefore only datasets to which you have download access can be used. </li>
                <li>The web service can generate very large responses. Large responses may be difficult to handle or unreliable. For example, if your client tool loads the XML into memory, you may have insufficient space to handle the data. Therefore we recommend you only query the data you need, generally it is safer to make several small requests rather than one very large one. </li>
            </ol>
        </div>
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to be able to use the One Species Data web service, a OneSpeciesDataRequest element has to be created. The details on how to do this are provided on this page along with some a constructed example.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the OneSpeciesDataRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">OneSpeciesDataRequest</div>
                <img src="../images/OneSpeciesDataRequest.png" /></div>
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
                    <td>This is the key used to identify the species.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>No</td>
                    <td>Lets you specify the datasets you want to query data from. If this element is omitted all datasets you have appropriate access to are queried. For a full description refer to the <a href="../../Schema_Elements/#DatasetList">DatasetList documentation</a>.</td>
                </tr>
                <tr>
                    <th>DateRange</th>
                    <td>No</td>
                    <td>Lets you define a date range filter using a start and end year. For a full description refer to the <a href="../../Schema_Elements/#DateRange-query">DateRange documentation</a>.</td>
                </tr>
                <tr>
                    <th>Resolution</th>
                    <td>No</td>
                    <td>
                        <p>Specifies the minimum resolution of records to be returned in the response. In otherwords, it is used to filter out low resolution records. For example, if you just want 2km records or better and not the 10km records, use this filter to specify 2km or better. The legal query parameters and their meanings are:</p>
                        <ul>
                            <li>_100m : only 100m records</li>
                            <li>_1km : 1km and 100m records</li>
                            <li>_2km : 2km, 1km and 100m records</li>
                            <li>_10km : all records</li>
                            <li>Any : all records</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>ReturnRecordAttributes</th>
                    <td>No</td>
                    <td>If set to true, the web service will return record attributes where these are available.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example of a constructed OneSpeciesDataRequest element. This particular example will request species records for Chiroptera that were recorded between the years 1955 and 1985.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:dat="http://www.nbnws.net/Dataset">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:OneSpeciesDataRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
         <tax:TaxonVersionKey>NHMSYS0000376160</tax:TaxonVersionKey>
         <tax:DateRange>
            <tax:Start>1955</tax:Start>
            <tax:End>1985</tax:End>
         </tax:DateRange>
       </tax:OneSpeciesDataRequest>
   </soapenv:Body>
</soapenv:Envelope>
        </nbn:prettyprint-code>
        </jsp:attribute>
        <jsp:attribute name="response">
        <p>The response to the One Species Data web service comes in the form of a GatewayData element. This element is capable of returning a Map, SpeciesList, SiteList and List of Dataset elements. The contents of the response is dependant on the request.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the GatewayData element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">GatewayData</div>
                <img src="../images/GatewayData.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Header</div>
                <img src="../images/Header.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Summary</div>
                <img src="../images/Summary.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Data</div>
                <img src="../images/Data.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Map</div>
                <img src="../images/Map.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SpeciesList</div>
                <img src="../images/SpeciesList.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Species</div>
                <img src="../images/Species.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteList</div>
                <img src="../images/SiteList.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Site</div>
                <img src="../images/Site.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Dataset</div>
                <img src="../images/Dataset.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Privileges</div>
                <img src="../images/Privileges.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Location</div>
                <img src="../images/Location.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SpeciesRecord</div>
                <img src="../images/SpeciesRecord.png" /></div>
        </div>
        <h4>Element Descriptions</h4>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>GatewayData</th>
                    <td>
                        <p>This is the root element for the response. It contains these attributes:</p>
                        <ul>
                            <li>NBNLogo: a link to the NBN logo, which must be displayed on products using this web service.</li>
                            <li>termsAndConditions: a link to the NBN terms and conditions, which must be added to products using this web service</li>
                            <li>RecordsFound: a boolean flag indicating whether records were found for this query</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>Header</th>
                    <td>Contains summary information on the response including status and number of records. For a full description refer to the <a href="../../Schema_Elements/#Header">Header documentation</a>.</td>
                </tr>
                <tr>
                    <th>Data</th>
                    <td>This element contains a sequence of elements which describe all the data for a GatewayData response. This includes a map of the queried area, a list of species, sites and habitats and the record data these refer to. The actual record data is grouped by dataset using the Dataset element.</td>
                </tr>
                <tr>
                    <th>Map</th>
                    <td>
                        <p>Contains information for a requested map image. The image will be of the area searched and displaying the area boundary over and OS backdrop. A map is only included if the request specified MapSettings.</p>
                        <p>For a full description of the Map element please refer to the <a href="../../Schema_Elements/#map">Map documentation</a>.</p>
                    </td>
                </tr>
                <tr>
                    <th>SpeciesList</th>
                    <td>
                        <p>This is a list of Species elements. Each Species refers to a species recorded in the Data section. It contains all the information about a species such as its name and authority. This saves duplicating this information for every record for the same species in the response.</p>
                        <p>For a full description refer to the <a href="../../Schema_Elements/#Species">Species documentation</a>.</p>
                    </td>
                </tr>
                <tr>
                    <th>SiteList</th>
                    <td>This is a list of Site elements. In the current web service implementation it is a list of all the unique 10km, 2k, 1km and 100m gridsquares in which species have been recorded. For a full description of this tag please refer to the <a href="../../Schema_Elements/#Site">Site documentation</a>.</td>
                </tr>
                <tr>
                    <th>Dataset</th>
                    <td>The Dataset element contains species records from a dataset plus metadata. The Metadata is stored in the ProviderMetadata element. The records are grouped into a sequence of Location elements. Each Location describes a unique site with a name and grid reference from which species have been recorded.</td>
                </tr>
                <tr>
                    <th>Location</th>
                    <td>
                        <p>A Location groups together all records from the same place. A location is defined as having a unique grid reference and name. The grid reference information can be looked up from the SiteList. Note, it is possible for two or more locations to refer to the same Site. The locations differ either because they have a different LocationName or are from a different Dataset. The location element has the following attributes :</p>
                        <ul>
                            <li>SiteKey - This identfies the Site in the SiteList this location refers to. Use the siteKey attribute of Site to make the lookup.</li>
                            <li>ID - This is the NBN Gateway ID for the location.</li>
                            <li>Pid - This is the dataset providers ID for the location. Although the attribute is defined as mandatory, a provider ID may not exist in which case it is given as a zero-length string.</li>
                            <li>
                                <p>Blurred - This is a boolean field indicating whether or not the location information has been blurred. If a location has been blurred then:</p>
                                <ul>
                                    <li>The location name will not be available</li>
                                    <li>A lower resolution grid reference than that aginst which the location is actually recorded is provided. The grid reference provided will be the best available based on your access to the dataset. For example, the location is to 100m resolution, but you only have 2km access. The location will be provided with a blurred 2km grid reference.</li>
                                </ul>
                            </li>
                        </ul>
                        <p>The location element contains the LocationName element. If the user has sufficient access then the value of the LocationName element will be the name of the Location, if the user does not have sufficient access then the element will be emitted.</p>
                    </td>
                </tr>
                <tr>
                    <th>SpeciesRecord</th>
                    <td>
                        <p>The SpeciesRecord is a sequence of elements listing the species records for the location. It contains the following attributes :</p>
                        <ul>
                            <li>pid - This is the dataset providers ID for the location. Although the attribute is defined as mandatory, a provider ID may not exist in which case it is given as a zero-length string.</li>
                            <li>isSensitive - Indicates whether or not the record is classed as sensitive</li>
                            <li>ID - This is the NBN Gateway ID for the location.</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>taxonVersionKey</th>
                    <td>This is the NHM taxon version key identifying the species the record is for. You can look up the species details from the SpeciesList element using the taxonVersionKey attribute of the Species elements.</td>
                </tr>
                <tr>
                    <th>RecordDate</th>
                    <td>The RecordDate is the date for when the record was recorded. It is defined by the following three attributes:
                        <ul>
                            <li>Accuracy - This describes the precision of the record, for example Year, exact date or month range.</li>
                            <li>From - This is the start date, represented as a year.</li>
                            <li>To - This is the end date, represented as a year.</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>Recorder</th>
                    <td>The recorder name.</td>
                </tr>
                <tr>
                    <th>Determiner</th>
                    <td>The determiner name.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Provided below is an example response of what could be expected from the One Species Data web service.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns12:GatewayData TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/Spatial" xmlns:ns2="http://www.nbnws.net/SiteBoundary" xmlns:ns3="http://www.nbnws.net/Map" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/Dataset" xmlns:ns6="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns7="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns8="http://www.nbnws.net/Habitat" xmlns:ns9="http://www.nbnws.net/Designation" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Metadata" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns12:Header>
            <ns12:Status isError="false"/>
            <ns12:Summary>
               <ns12:DatasetCount>4</ns12:DatasetCount>
               <ns12:LocationCount>4</ns12:LocationCount>
               <ns12:RecordCount>4</ns12:RecordCount>
            </ns12:Summary>
         </ns12:Header>
         <ns12:Data>
            <ns4:SpeciesList>
               <ns4:Species taxonVersionKey="NBNSYS0000005645">
                  <ns4:ScientificName>Sympetrum striolatum</ns4:ScientificName>
                  <ns4:CommonName>Common Darter</ns4:CommonName>
                  <ns4:Authority>(Charpentier, 1840)</ns4:Authority>
                  <ns6:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080071">insect - dragonfly (Odonata)</ns6:TaxonReportingCategory>
               </ns4:Species>
               <ns4:Species taxonVersionKey="NHMSYS0000080156">
                  <ns4:ScientificName>Triturus cristatus</ns4:ScientificName>
                  <ns4:CommonName>Great Crested Newt</ns4:CommonName>
                  <ns4:Authority>(Laurenti, 1768)</ns4:Authority>
                  <ns6:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080033">amphibian</ns6:TaxonReportingCategory>
               </ns4:Species>
            </ns4:SpeciesList>
            <ns12:SiteList>
               <ns12:Site siteKey="loc10031505">
                  <GridSquare key="NC26" minx="220000.0" miny="960000.0" maxx="230000.0" maxy="970000.0" srs="EPSG_27700"/>
               </ns12:Site>
               <ns12:Site siteKey="loc849119">
                  <GridSquare key="NC36" minx="230000.0" miny="960000.0" maxx="240000.0" maxy="970000.0" srs="EPSG_27700"/>
               </ns12:Site>
               <ns12:Site siteKey="loc10062044">
                  <GridSquare key="NC27" minx="220000.0" miny="970000.0" maxx="230000.0" maxy="980000.0" srs="EPSG_27700"/>
               </ns12:Site>
               <ns12:Site siteKey="loc9532940">
                  <GridSquare key="NC27" minx="220000.0" miny="970000.0" maxx="230000.0" maxy="980000.0" srs="EPSG_27700"/>
               </ns12:Site>
            </ns12:SiteList>
            <ns12:Dataset id="BRCODON0">
               <ns11:ProviderMetadata exchangeFormatVersion="1" datestamp="28/03/2001" gatewayId="BRCODON0">
                  <ns11:DatasetTitle>Dragonfly and Damselfly (Odonata) records for Britain to 1992, from the former Dragonfly Recording Scheme</ns11:DatasetTitle>
                  <ns11:DatasetProvider>Biological Records Centre</ns11:DatasetProvider>
                  <ns11:Abstract>
                     <ns11:Description>This dataset consists of the records collected and collated for the Odonata Recording Scheme (now the Dragonfly Recording Network), which led to the publication of the Atlas of the Dragonflies of Britain and Ireland (1996) by Merritt, Moore and Eversham.  The scheme was run in association with the Biological Records Centre, where the dataset is now held.  It contains not only the records made and submitted by the volunteer community, but also some from historical literature and museum sources.  The records span the period 1807 (one from literature) to 1992, with 82% from 1975 to 1988.</ns11:Description>
                     <ns11:DataCaptureMethod/>
                     <ns11:DatasetPurpose/>
                     <ns11:GeographicalCoverage>Great Britain, but not Ireland.</ns11:GeographicalCoverage>
                     <ns11:TemporalCoverage>The majority of records span 1888-1992 although there are a few records before these dates.</ns11:TemporalCoverage>
                     <ns11:DataQuality>All records have been checked and verified by the national scheme organiser and validated as part of BRC&amp;apos;s routine data acquisition process.</ns11:DataQuality>
                     <ns11:AdditionalInformation><![CDATA[Merritt. R., Moore, N.W. &amp; Eversham, B.C. 1996. Atlas of The Dragonflies of Britain and Ireland. London: HMSO &lt;br&gt;
&lt;a href=&quot;http://www.brc.ac.uk/publications.htm&quot;target=&quot;_blank&quot;&gt;http://www.brc.ac.uk/publications.htm&lt;/a&gt;&lt;br&gt;
&lt;a href=&quot;http://www.dragonflysoc.org.uk/&quot;target=&quot;_blank&quot;&gt;http://www.dragonflysoc.org.uk/&lt;/a&gt;]]></ns11:AdditionalInformation>
                  </ns11:Abstract>
                  <ns11:AccessConstraints>There are no access constraints</ns11:AccessConstraints>
                  <ns11:UseConstraints/>
               </ns11:ProviderMetadata>
               <ns5:Privileges>
                  <ns5:DownloadRawData>true</ns5:DownloadRawData>
                  <ns5:SensitiveRecordsAccess>true</ns5:SensitiveRecordsAccess>
                  <ns5:ViewAttributes>true</ns5:ViewAttributes>
                  <ns5:ViewRecorder>true</ns5:ViewRecorder>
                  <ns5:AccessResolution>_100m</ns5:AccessResolution>
               </ns5:Privileges>
               <ns12:Location siteKey="loc849119" id="849119" pid="">
                  <ns12:LocationName>Durness</ns12:LocationName>
                  <ns12:SpeciesRecord id="10289357" taxonVersionKey="NBNSYS0000005645" pid="" isSensitive="false">
                     <ns12:RecordDate accuracy="Day" from="1914-08-18Z" to="1914-08-18Z"/>
                     <ns12:Determiner/>
                     <ns12:Recorder/>
                  </ns12:SpeciesRecord>
               </ns12:Location>
            </ns12:Dataset>
            <ns12:Dataset id="GA000497">
               <ns11:ProviderMetadata exchangeFormatVersion="1" datestamp="09/03/2010" gatewayId="GA000497">
                  <ns11:DatasetTitle>HBRG Vertebrates (not Badger) Dataset</ns11:DatasetTitle>
                  <ns11:DatasetProvider>Highland Biological Recording Group</ns11:DatasetProvider>
                  <ns11:Abstract>
                     <ns11:Description>All records of vertebrates, other than Badger, available to HBRG at March 2010, including all those previously held on the database at the Inverness Museum &amp;amp; Art Gallery Records Centre (IMAG). Assembly of this dataset was funded by grants from Scottish Natural Heritage.</ns11:Description>
                     <ns11:DataCaptureMethod>Field records submitted to HBRG and/or IMAG, with some records extracted from the literature.</ns11:DataCaptureMethod>
                     <ns11:DatasetPurpose>Casual recording, but HBRG interests have included targeted recording of mammals, amphibians and reptiles.</ns11:DatasetPurpose>
                     <ns11:GeographicalCoverage>Concentrated in Highland, but any records submitted from within UK are included. Coverage will reflect observer location and activity. The majority of records in this dataset are shown at six figure grid-references.</ns11:GeographicalCoverage>
                     <ns11:TemporalCoverage/>
                     <ns11:DataQuality>Data do not have comprehensive geographic or taxonomic coverage. They have not been reassessed in light of recent taxonomic splits or aggregations, and should be checked against original sources if there is doubt.  Records of Triturus helveticus and T. vulgaris are currently being reassessed in the light of recent finds.  Records of these which cannot be confidently assigned to species are listed under the genus, and a note made in the Comment of the original reported determination.

Records of Pipistrellus pipistrellus from before the split are listed under the genus only, and subsequent records are only assigned to species if confirmed by a specimen or bat detector.</ns11:DataQuality>
                     <ns11:AdditionalInformation>Website www.hbrg.org.uk. HBRG can provide limited interpretation and analysis of data, for which a fee may be charged.</ns11:AdditionalInformation>
                  </ns11:Abstract>
                  <ns11:AccessConstraints>No constraints.</ns11:AccessConstraints>
                  <ns11:UseConstraints>Default conditions apply.</ns11:UseConstraints>
               </ns11:ProviderMetadata>
               <ns5:Privileges>
                  <ns5:DownloadRawData>true</ns5:DownloadRawData>
                  <ns5:SensitiveRecordsAccess>true</ns5:SensitiveRecordsAccess>
                  <ns5:ViewAttributes>true</ns5:ViewAttributes>
                  <ns5:ViewRecorder>true</ns5:ViewRecorder>
                  <ns5:AccessResolution>_100m</ns5:AccessResolution>
               </ns5:Privileges>
               <ns12:Location siteKey="loc10062044" id="10062044" pid="">
                  <ns12:LocationName>NGR 10km square NC27</ns12:LocationName>
                  <ns12:SpeciesRecord id="117751759" taxonVersionKey="NHMSYS0000080156" pid="CI00016600003U1U" isSensitive="false">
                     <ns12:RecordDate accuracy="Year Range" from="1900-01-01Z" to="1960-12-31Z"/>
                     <ns12:Determiner>Unknown-to-HBRG</ns12:Determiner>
                     <ns12:Recorder>Unknown-to-HBRG</ns12:Recorder>
                  </ns12:SpeciesRecord>
               </ns12:Location>
            </ns12:Dataset>
            <ns12:Dataset id="GA000145">
               <ns11:ProviderMetadata exchangeFormatVersion="1" datestamp="30/11/2009" gatewayId="GA000145">
                  <ns11:DatasetTitle>Reptiles and Amphibians Dataset</ns11:DatasetTitle>
                  <ns11:DatasetProvider>Biological Records Centre</ns11:DatasetProvider>
                  <ns11:Abstract>
                     <ns11:Description>Reptile and Amphibian records extracted from the BRC herptiles database. The dataset includes records from RHR Taylor, Frank Perrin and various other volunteer recorders. There is no official recording scheme for herptiles although some recording is ongoing within BRC. These data were input to electronic format in BRC from recording cards.</ns11:Description>
                     <ns11:DataCaptureMethod/>
                     <ns11:DatasetPurpose/>
                     <ns11:GeographicalCoverage>The UK and Ireland. Good coverage of England, Scotland and Wales and reasonable coverage of Ireland.</ns11:GeographicalCoverage>
                     <ns11:TemporalCoverage>There is a peak of recording from 1958-1996 although the last records were collected in 2001 and there are many earlier records in the dataset.</ns11:TemporalCoverage>
                     <ns11:DataQuality>Records have been subject to routine validation by BRC.</ns11:DataQuality>
                     <ns11:AdditionalInformation><![CDATA[Data published in: Arnold, H.R. 1995. Atlas of amphibians and reptiles in Britain. London: HMSO. Details
&lt;a href=&quot;hhttp://www.brc.ac.uk/publications.htm&quot; target=&quot;_blank&quot;&gt; here
&lt;/a&gt;
&lt;br&gt;
&lt;a href=&quot;http://www.brc.ac.uk/scheme_details.asp?schemeChoice=75
&quot; target=&quot;_blank&quot;&gt; More information
&lt;/a&gt;]]></ns11:AdditionalInformation>
                  </ns11:Abstract>
                  <ns11:AccessConstraints>There are access constraints to protect vulnerable species. Detailed data may be made available to the country agencies and other conservation organisations to assist their conservation work.</ns11:AccessConstraints>
                  <ns11:UseConstraints/>
               </ns11:ProviderMetadata>
               <ns5:Privileges>
                  <ns5:DownloadRawData>true</ns5:DownloadRawData>
                  <ns5:SensitiveRecordsAccess>false</ns5:SensitiveRecordsAccess>
                  <ns5:ViewAttributes>true</ns5:ViewAttributes>
                  <ns5:ViewRecorder>true</ns5:ViewRecorder>
                  <ns5:AccessResolution>_100m</ns5:AccessResolution>
               </ns5:Privileges>
               <ns12:Location siteKey="loc9532940" id="9532940" pid="">
                  <ns12:LocationName>Cape Wrath,E</ns12:LocationName>
                  <ns12:SpeciesRecord id="105021284" taxonVersionKey="NHMSYS0000080156" pid="4810" isSensitive="false">
                     <ns12:RecordDate accuracy="Before Year" from="1900-01-01Z" to="1904-12-31Z"/>
                     <ns12:Determiner/>
                     <ns12:Recorder/>
                  </ns12:SpeciesRecord>
               </ns12:Location>
            </ns12:Dataset>
            <ns12:Dataset id="GA000495">
               <ns11:ProviderMetadata exchangeFormatVersion="1" datestamp="09/03/2010" gatewayId="GA000495">
                  <ns11:DatasetTitle>HBRG Insects Dataset</ns11:DatasetTitle>
                  <ns11:DatasetProvider>Highland Biological Recording Group</ns11:DatasetProvider>
                  <ns11:Abstract>
                     <ns11:Description>All records of insects available to HBRG at March 2010, including all those previously held on the database at the Inverness Museum &amp;amp; Art Gallery Records Centre (IMAG). Assembly of this dataset was funded by grants from Scottish Natural Heritage.</ns11:Description>
                     <ns11:DataCaptureMethod>Field records submitted to HBRG and/or IMAG, with some records extracted from the literature.</ns11:DataCaptureMethod>
                     <ns11:DatasetPurpose>Casual recording, but HBRG interests have included targeted recording of particular taxa.</ns11:DatasetPurpose>
                     <ns11:GeographicalCoverage>Concentrated in Highland, but any records submitted from within UK are included. Coverage will reflect observer location and activity. The majority of records in this dataset are shown at six figure grid-references.</ns11:GeographicalCoverage>
                     <ns11:TemporalCoverage/>
                     <ns11:DataQuality>See corrections below (scroll down).

Data do not have comprehensive geographic or taxonomic coverage. They may not have been assessed in light of recent taxonomic splits or aggregations, and should be checked against original sources if there is doubt.

Three errors have come to light and will be corrected at the next update:
Phytomyza obscurella Cromarty 26-27 June 2003 given as in Galium aparine should be ignored as the stated host is incorrect.
Creophilus maxillosus Strathpeffer 9 June and 9 August 2009 should refer to Ontholestes tesselatus.</ns11:DataQuality>
                     <ns11:AdditionalInformation>All the taxon records used in the HBRG bumblebee atlas are included.  Details on the website www.hbrg.org.uk. HBRG can provide limited interpretation and analysis of data, for which a fee may be charged.</ns11:AdditionalInformation>
                  </ns11:Abstract>
                  <ns11:AccessConstraints>No constraints.</ns11:AccessConstraints>
                  <ns11:UseConstraints>Default conditions apply.</ns11:UseConstraints>
               </ns11:ProviderMetadata>
               <ns5:Privileges>
                  <ns5:DownloadRawData>true</ns5:DownloadRawData>
                  <ns5:SensitiveRecordsAccess>true</ns5:SensitiveRecordsAccess>
                  <ns5:ViewAttributes>true</ns5:ViewAttributes>
                  <ns5:ViewRecorder>true</ns5:ViewRecorder>
                  <ns5:AccessResolution>_100m</ns5:AccessResolution>
               </ns5:Privileges>
               <ns12:Location siteKey="loc10031505" id="10031505" pid="">
                  <ns12:LocationName>10km square NC26</ns12:LocationName>
                  <ns12:SpeciesRecord id="117679511" taxonVersionKey="NBNSYS0000005645" pid="CI00016600006LUD" isSensitive="false">
                     <ns12:RecordDate accuracy="Year" from="1984-01-01Z" to="1984-12-31Z"/>
                     <ns12:Determiner>Unknown-to-HBRG</ns12:Determiner>
                     <ns12:Recorder>Unknown-to-HBRG</ns12:Recorder>
                  </ns12:SpeciesRecord>
               </ns12:Location>
            </ns12:Dataset>
         </ns12:Data>
      </ns12:GatewayData>
   </S:Body>
</S:Envelope>
        </nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>