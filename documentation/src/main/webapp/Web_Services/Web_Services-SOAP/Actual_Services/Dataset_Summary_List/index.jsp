<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway dataset summary list web service provides metadata and access information for one or more datasets on the NBN Gateway. It does not provide any data for a dataset. Using this service you could, for example, ask for:</p>
        <ul>
            <li>which datasets have data for a species</li>
            <li>metadata for a single dataset of interest</li>
            <li>a list of datasets for which you have view recorder privilege</li>
        </ul>
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to obtain a summary for a single dataset or a group of datasets then you need to send a DatasetListRequest to the web services. An example of this is provided below.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the DatasetListRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetListRequest</div>
                <img src="../images/DatasetListRequest.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">PrivilegesFilter</div>
                <img src="../images/PrivilegesFilter.png" />
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
                    <th>DatasetListRequest</th>
                    <td>No</td>
                    <td>This is the root element. It provides a choice between one of three filter elements. Only one filter element can be applied.</td>
                </tr>
                <tr>
                    <th>TaxonVersionKey</th>
                    <td>No</td>
                    <td>Lets you select all datasets which have records for the taxon version key.</td>
                </tr>
                <tr>
                    <th>TaxonReportingCategoryKey</th>
                    <td>No</td>
                    <td>Lets you select all datasets which have records for the taxon reporting category key.</td>
                </tr>
                <tr>
                    <th>DatasetList</th>
                    <td>No</td>
                    <td>Lets you specify one or more datasets you want to query metadata for. For a full description refer to the <a href="../../Schema_Elements/#DatasetList">DatasetList documentation</a></td>
                </tr>
                <tr>
                    <th>PrivilegesFilter</th>
                    <td>No</td>
                    <td>Lets you specify a minimum access filter. This will filter out datasets to which your access level is less than the required access.</td>
                </tr>
                <tr>
                    <th>HasDownloadRawData</th>
                    <td>No</td>
                    <td>Filters out datasets that do not allow access to record level data.</td>
                </tr>
                <tr>
                    <th>HasSensitiveRecords Access</th>
                    <td>No</td>
                    <td>Filter out datasets that do not allow access to sensitive records.</td>
                </tr>
                <tr>
                    <th>HasViewAttributes</th>
                    <td>No</td>
                    <td>Filter out datasets that do not allow access to record attribute information.</td>
                </tr>
                <tr>
                    <th>HasViewRecorder</th>
                    <td>No</td>
                    <td>Filter out datasets that do not allow access to recorder and determiner information.</td>
                </tr>
                <tr>
                    <th>MinimumAccessResolution</th>
                    <td>No</td>
                    <td>Filter out datasets whose records are recorded at, or are blurred to a worse resolution to the one specified.</td>
                </tr>
            </tbody>
        </table>

        <h3>Example</h3>
        <p>The following is an example of the DatasetListRequest XML structure which is used to prompt the web service in returning a summary of the dataset which is identified with the code "GA000091"</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:dat="http://www.nbnws.net/Dataset" xmlns:tax="http://www.nbnws.net/Taxon" xmlns:tax1="http://www.nbnws.net/TaxonReportingCategory">
    <soapenv:Header/>
    <soapenv:Body>
        <dat:DatasetListRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
            <dat:DatasetList>
                <dat:DatasetKey>GA000091</dat:DatasetKey>
            </dat:DatasetList>
        </dat:DatasetListRequest>
    </soapenv:Body>
</soapenv:Envelope>
        </nbn:prettyprint-code>
            
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The summary response of the web service will be in the form of a DatsetListResponse XML structure.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the DatasetListResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetListResponse</div>
                <img src="../images/DatasetListResponse.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetSummaryList</div>
                <img src="../images/DatasetSummaryList.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DatasetSummary</div>
                <img src="../images/DatasetSummary.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Privileges</div>
                <img src="../images/Privileges.png" />
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
                    <th>DatasetSummaryList</th>
                    <td>This is the root element. It has a list of one or more DatasetSummary elements.</td>
                </tr>
                <tr>
                    <th>DatasetSummary</th>
                    <td>Contains information about a dataset.</td>
                </tr>
                <tr>
                    <th>Id</th>
                    <td>Attribute of DatasetSummary, it is the NBN Gateway dataset ID.</td>
                </tr>
                <tr>
                    <th>ProviderMetadata</th>
                    <td>Contains metadata for the dataset. For a full description refer to the <a href="../../Schema_Elements/#ProviderMetadata">ProviderMetadata documentation</a></td>
                </tr>
                <tr>
                    <th>Privileges</th>
                    <td>Contains the current access level to the dataset.</td>
                </tr>
                <tr>
                    <th>DownloadRawData</th>
                    <td>Indicates if you are allowed to download raw records. Most webservices require this privilege to process data from a dataset.</td>
                </tr>
                <tr>
                    <th>SensitiveRecordsAccess</th>
                    <td>Indicates if you are allowed access to records marked as sensitive.</td>
                </tr>
                <tr>
                    <th>ViewAttributes</th>
                    <td>Indicates if you are allowed access to record attributes.</td>
                </tr>
                <tr>
                    <th>ViewRecorder</th>
                    <td>Indicates if you are allowed access to recorder and determiner information.</td>
                </tr>
                <tr>
                    <th>AccessResolution</th>
                    <td>Indicates the best resolution that you have access to. Records recorded at a better resolution are blurred to this resolution.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example of a response which could be expected from this web service. This particular response has been produced in response to a Dataset Summary query requesting a summary of the dataset which is identified by "GA000091".</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns11:DatasetListResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns11:DatasetSummaryList>
            <ns11:DatasetSummary id="GA000091">
               <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="24/04/2008" gatewayId="GA000091">
                  <ns8:DatasetTitle>Vascular Plants Database</ns8:DatasetTitle>
                  <ns8:DatasetProvider>Botanical Society of the British Isles</ns8:DatasetProvider>
                  <ns8:Abstract>
                     <ns8:Description>This database of 9.8 million records contains the distributions of 6669 taxa of flowering plants and ferns. Most of these records have been collected by volunteer members of the Botanical Society of the British Isles (BSBI) and include those gathered for the Atlas of the British Flora (Perring &amp;amp; Walters, 1962) and the New Atlas of the British and Irish flora (Preston, Pearman &amp;amp; Dines, 2002). The latter is a collation of many datasets (including those held by BSBI vice-county recorders), some included in this database in full and others as summary data; the major sources of data are outlined in the New Atlas text. The availability of data at resolutions higher than 10km square varies between sources. The collated data have been validated at the 10km square level by BSBI Vice-County recorders and the editors for those taxa mapped in the New Atlas, and were believed to provide a good representation of vascular plant distributions in Britain and Ireland at the time of publication. Records of infraspecific taxa and hybrids which were not mapped in the New Atlas have been scrutinised less closely. An update of around 900,000 records was made to the dataset in January 2007, to include the data from the BSBI Local Change survey (Braithwaite et al., 2006), data from the Flora of Oxfordshire (Killick et al., 1998) and the Isle of Wight Flora (Pope et al., 2003), records of hybrid plants and a variety of other records. Some corrections to the New Atlas dataset have also been made.</ns8:Description>
                     <ns8:DataCaptureMethod>The records are compiled from many sources and do not conform to any one method. Nearly all will include a 10km square but quite often other information is missing.</ns8:DataCaptureMethod>
                     <ns8:DatasetPurpose>To produce Atlases at 10km resolution.</ns8:DatasetPurpose>
                     <ns8:GeographicalCoverage>Britain &amp;amp; Ireland, the Channel Isles and Isle of Man.</ns8:GeographicalCoverage>
                     <ns8:TemporalCoverage>1629-2006</ns8:TemporalCoverage>
                     <ns8:DataQuality>Records are variable in quality. Many records are detailed, with six-figure grid references and full dates, but many are not. A certain level of duplication can be expected. Experience is needed in interpretation.</ns8:DataQuality>
                     <ns8:AdditionalInformation>
                        <![CDATA[&lt;br&gt;&lt;br&gt;
                        References
                        &lt;br&gt;&lt;br&gt;
                        BRAITHWAITE, M. E., ELLIS, R. W. &amp; PRESTON, C. D. (2006) Change in the British Flora 1987-2004. Botanical Society of the British Isles, London. &lt;br&gt;
                        KILLICK, J., PERRY, R. &amp; WOODELL, S. (1998) The Flora of Oxfordshire. Nature Conservation Bureau, Newbury. &lt;br&gt;
                        PERRING, F. H. &amp; WALTERS, S. M. (Eds.) (1962) Atlas of the British Flora, London, Thomas Nelson &amp; Sons. &lt;br&gt;
                        POPE, C., SNOW, L. &amp; ALLEN, D. E. (2003) The Isle of Wight Flora. Dovecote  Press, Wimborne. &lt;br&gt;
                        PRESTON, C. D., PEARMAN, D. A. &amp; DINES, T. D. (2002) New atlas of the British and Irish flora. Oxford University Press, Oxford.]]>
                     </ns8:AdditionalInformation>
                  </ns8:Abstract>
                  <ns8:AccessConstraints>Access is normally set to 10km resolution, which is how the information was collected and displayed in the New Atlas. Finer levels of resolution are not normally allowed, in order to prevent users creating their own databases, adversely affecting the BSBI&amp;apos;s ability to produce definitive maps. Legitimate researchers and those with comprehensive data exchange agreements with the BSBI are permitted full access.</ns8:AccessConstraints>
                  <ns8:UseConstraints/>
               </ns8:ProviderMetadata>
               <ns11:Privileges>
                  <ns11:DownloadRawData>true</ns11:DownloadRawData>
                  <ns11:SensitiveRecordsAccess>false</ns11:SensitiveRecordsAccess>
                  <ns11:ViewAttributes>false</ns11:ViewAttributes>
                  <ns11:ViewRecorder>false</ns11:ViewRecorder>
                  <ns11:AccessResolution>_10km</ns11:AccessResolution>
               </ns11:Privileges>
            </ns11:DatasetSummary>
         </ns11:DatasetSummaryList>
      </ns11:DatasetListResponse>
   </S:Body>
</S:Envelope>
        </nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>