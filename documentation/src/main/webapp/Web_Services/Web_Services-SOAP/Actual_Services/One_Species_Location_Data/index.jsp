<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway species location data web service returns unique location data for a single species. This webservice is intended to allow you to retrieve the locations in which records occur, at a specified resolution, rather than the detailed records themselves. This is useful for mapping and analysis purposes. This web service does not let you apply a spatial filter, to do that you should use the <a href="../One_Site_Data">OneSiteData</a> web service, but you can apply year and dataset filters. If you need detailed records please use the <a href="../One_Species_Data">OneSpeciesData</a> webservice</p>
        <p>As this service returns raw data you must have download access to the datasets being used.</p>
        <p>The species data service uses the <code>OneSpeciesLocationDataRequest</code> document structure to send a request. The <code>OneSpeciesLocationDataResponse</code> document structure defines the response data sent back to the client.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <p>The XML elements for a request to the One Species Location Data web service are described below.</p>
        <p>The <code>OneSpeciesLocationDataRequest</code> element is defined by the gateway_query.xsd as:</p>
        <p><img src="../images/OneSpeciesLocationDataRequest.png" /></p>
        <p>&nbsp;</p>
        <table border="0" cellspacing="1" cellpadding="3" bgcolor="#c0c0c0">
            <tbody>
                <tr>
                    <td bgcolor="#f0f0f0" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Parameter</strong></td>
                    <td bgcolor="#f0f0f0" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Use</strong></td>
                    <td bgcolor="#f0f0f0" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Comments</strong></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>TaxonVersionKey</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">M</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">This is the key used to identify the species.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><strong>DatasetList</strong></span></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">Lets you specify the datasets you want to query data from. If this element is omitted all datasets you have appropriate access to are queried. For a full description refer to the<span class="Apple-converted-space">&nbsp;</span></span><a href="../../Schema_Elements/#DatasetList"><span style="font-family: Arial"><code>DatasetList</code> documentation</span></a><span style="font-family: Arial">.</span></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><strong>Date Range</strong></span></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">Lets you define a date range filter using a start and end year. For a full description refer to the<span class="Apple-converted-space">&nbsp;</span></span><a href="../../Schema_Elements/#DateRange-query"><span style="font-family: Arial"><code>DateRange </code>documentation</span></a><span style="font-family: Arial">.</span></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><strong>AtResolution</strong></span></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">
                        <p><span style="font-family: Arial">Specifies the&nbsp;resolution of locations&nbsp;to be returned in the response. In other words, it is used to filter out low resolution records and blur high resolution records. For example, if you just want 2km locations it will blur the 100m and 1km&nbsp;records to 2km and discard the 10km records. Use this filter to specify 2km. The legal query parameters and their meanings are:</span></p>
                        <ul>
                            <li><span style="font-family: Arial">_100m : only 100m records</span></li>
                            <li><span style="font-family: Arial">_1km :&nbsp;1km and 100m records</span></li>
                            <li><span style="font-family: Arial">_2km : 2km, 1km and 100m records</span></li>
                            <li><span style="font-family: Arial">_10km : all records</span></li>
                            <li><span style="font-family: Arial">Any : all records</span></li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong><span style="font-family: Arial">MinimumResolution</span></strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><span>
                                <p style="margin: 0cm 0cm 10pt"><span>Specifies the minimum resolution to be&nbsp;returned in the response. In other words, it is used to filter out low resolution records. For example if you just want 2km records or better and not 10km records, use this filter to specify 2km or better. The legal query parameters and their meanings are:</span></p>
                            </span></span><span style="font-family: Arial"><font color="#000000">
                                <ul>
                                    <li><span style="font-family: Arial">_100m : only 100m records</span></li>
                                    <li><span style="font-family: Arial">_1km :&nbsp;1km and 100m records</span></li>
                                    <li><span style="font-family: Arial">_2km : 2km, 1km and 100m records</span></li>
                                    <li><span style="font-family: Arial">_10km : all records</span></li>
                                    <li><span style="font-family: Arial">Any : all records</span></li>
                                </ul>
                            </font></span></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>MaximumResolution</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span><font color="#000000"><font size="+0"><font size="3">Specifies the maximum resolution to be&nbsp;returned in the response. In other words, it is used to filter out high resolution records. For example if you just want 2km records or worse and the 1km and 100m blurred to 2km, use this filter to specify 2km or worse. </font></font></font></span>The legal query parameters and their meanings are:<span style="font-family: Arial"><font color="#000000"><font face="Calibri"><font size="3">
                                        <ul>
                                            <li><span style="font-family: Arial">_100m : only 100m records</span></li>
                                            <li><span style="font-family: Arial">_1km :&nbsp;1km and 100m records</span></li>
                                            <li><span style="font-family: Arial">_2km : 2km, 1km and 100m records</span></li>
                                            <li><span style="font-family: Arial">_10km : all records</span></li>
                                            <li><span style="font-family: Arial">Any : all records</span></li>
                                        </ul>
                                    </font></font></font></span></td>
                </tr>
            </tbody>
        </table>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The XML Elements for a response from the one species location data request web service are described below.</p>
        <p>The <span style="font-family: Courier New"><code>OneSpeciesLocationData</code> </span>element is defined by the gateway_data.xsd as:</p>
        <p><img src="../images/OneSpeciesLocationData.png" /></p>
        <p>The <code>AggregateSite</code> element is defined as:</p>
        <p><img src="../images/AggregateSite.png" /></p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <table border="0" cellspacing="1" cellpadding="3" bgcolor="#c0c0c0">
            <tbody>
                <tr>
                    <td bgcolor="#f0f0f0" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Parameter</strong></td>
                    <td bgcolor="#f0f0f0" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Use</strong></td>
                    <td bgcolor="#f0f0f0" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Comments</strong></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>OneSpeciesLocationDataResponse</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">M</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">
                        <p>This is the root element for the response. It contains these attributes:</p>
                        <ul>
                            <li>
                                <p><span style="font-family: Courier New">NBNLogo</span>: a link to the NBN logo, which must be displayed on products using this web service.</p>
                            </li>
                            <li>
                                <p><span style="font-family: Courier New">termsAndConditions:</span> a link to the NBN terms and conditions, which must be added to products using this web service.</p>
                            </li>
                            <li>
                                <p><span style="font-family: Courier New">RecordsFound: </span><span style="font-family: Arial"><span>a boolean flag indicating whether records were found for this query.</span></span></p>
                            </li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><strong>Header</strong></span></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">M</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><span class="Apple-converted-space">Contains summary information on the&nbsp;response including status and number of records. For a full description refer to the&nbsp;&nbsp;</span></span><a href="../../Schema_Elements/#header"><span style="font-family: Courier New"><span><code>Header documentation</code></span></span></a><span style="font-family: Arial">.</span></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><strong>Species</strong></span></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Courier New">Species</span>&nbsp;refers to the species locations requested. it contains all the information about a species such as its name and authority. For a full description refer to the&nbsp;<a href="../../Schema_Elements/#species"><span style="font-family: Arial"><code>Species</code> documentation</span></a><span style="font-family: Arial">.</span></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial"><strong>AggregateSiteList</strong></span></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">
                        <p>This is a list of <span style="font-family: Courier New">AggregateSite</span> elements. In the current web service implementation it is a list of all the unique gridsquares in which species have been recorded at the resolution specified in the request.</p>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>DatasetSummaryList</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><span style="font-family: Arial">O</span></td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">This element contains a list of datasets and their associated metadata. Metadata is stored in the&nbsp;<a href="../../Schema_Elements/#providermetadata"><span style="font-family: Arial"><code>ProviderMetadata</code> documentation</span></a>.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>AggregateSite</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">M</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">A unique location at the resolution specified in which the species occurs.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>Site</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">M</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">For a full description refer to the <a href="../../Schema_Elements/#site"><span style="font-family: Arial"><code>Site</code> documentation</span></a>.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>RecordCount</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">O</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">The number of records that this location covers.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>HasSensitiveData</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">O</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">This element is set to <strong>true</strong> if any records that this location covers are flagged as sensitive.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>HasNonSensitiveData</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">O</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">This element is set to <strong>true</strong> if any records that this location covers are not flagged as sensitive.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>EarliestRecord</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">O</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">This element contains the earliest recorded year in the records that this location covers.</td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top"><strong>LatestRecord</strong></td>
                    <td bgcolor="#ffffff" align="center" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">O</td>
                    <td bgcolor="#ffffff" style="color: rgb(0,0,0); font-size: 12px; vertical-align: top">This element contais the latest recorded year in the records that this&nbsp;location covers.</td>
                </tr>
            </tbody>
        </table>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>