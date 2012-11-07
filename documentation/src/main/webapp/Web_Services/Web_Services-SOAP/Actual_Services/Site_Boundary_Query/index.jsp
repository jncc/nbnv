<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>If you are only interested in certain layers, you can specify these and be assured that you will only get data regarding these.</p>
        <p>This web service is capable of returning the intersected shapes as clipped polygons. The default behaviour of the web services is to not return the clipped polygons unless you need them. This is done so that the default response of the web service is as compact as possible. However if you do require these simply flag &ldquo;RequiredPolygonVertices&rdquo; to true in your request.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to filter the Site Boundary features list to an area of interest, you need to define that area of interest. This web service can be queried using any of the spatial filters that the web services supports. Details of the valid spatial filters can be seen <a href="../../Schema_Elements/#Filter">here</a>.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the SiteBoundaryQueryRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryQueryRequest</div>
                <img src="../images/SiteBoundaryQueryRequest.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryLayerFilter</div>
                <img src="../images/SiteBoundaryLayerFilter.png" /></div>
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
                    <th>SiteBoundaryLayerID</th>
                    <td>No</td>
                    <td>If you only wish to query a certian Site Boundary Layer, then you can specify it in this tag. This tag can occur multiple times in the SiteBoundaryQueryRequest allowing you to query multiple different site boundary layers in one request. 0 occurances of this tag will create a request which queries all Site Boundary layers which are stored on the NBN Gateway.</td>
                </tr>
                <tr>
                    <th>RequiredPolygonVertices</th>
                    <td>No</td>
                    <td>The Site Boundary query web service is capable of returning the vertices of the clipped Site Boundary polygons which exist inside the SiteBoundaryLayerFilter. If this information is required then this tag must be added to the request with the value of true. Emitting this tag will make the web service assume that the Polygon Vertices are not required.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryLayerFilter</th>
                    <td>Yes</td>
                    <td>The Site Boundary Layer Filter is a Spatial filter. It can take the form of either a :
                        <ul>
                            <li>BoundingBox</li>
                            <li>Buffer</li>
                            <li>Polygon</li>
                            <li>MultiPartPolygon</li>
                        </ul>
                    </td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Below is an example of a site boundary query which uses a buffer defined in EPSG_4326 and centred around 53.657661N 1.82676W with a radius of 100 metres. This query will only return site boundary information in the requested layers (SITE_BOUNDARY_LAYER, ANOTHER_SITE_BOUNDARY_LAYER).</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sit="http://www.nbnws.net/SiteBoundary" xmlns:spat="http://www.nbnws.net/Spatial">
  <soapenv:Header />
  <soapenv:Body>
    <sit:SiteBoundaryQueryRequest registrationKey="YOUR_REGISTRATION_KEY">
      <sit:SiteBoundaryLayerID>ID_OF_A_SITE_BOUNDARY_LAYER</sit:SiteBoundaryLayerID>
      <sit:SiteBoundaryLayerID>ID_OF_ANOTHER_SITE_BOUNDARY_LAYER</sit:SiteBoundaryLayerID>
      <sit:RequiredPolygonVertices>true</sit:RequiredPolygonVertices>
      <sit:SiteBoundaryLayerFilter>
        <spat:Buffer>
          <spat:Point srs="EPSG_4326" x="-1.82676" y="53.657661" />
          <spat:Distance>100</spat:Distance>
        </spat:Buffer>
      </sit:SiteBoundaryLayerFilter>
    </sit:SiteBoundaryQueryRequest>
  </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        <p>Notice that the element RequiredPolygonVertices contains the value true, therefore the response will contain vertices that represent the polygon contained within the spatial filter. This means that the resultant vertices will not extend the bounds of the &quot;SiteBoundaryLayerFilter&quot;.</p>

    </jsp:attribute>
    <jsp:attribute name="response">
        <p>At the very least, a response will contain a list of Site Boundaries, the metadata which is stored about this site boundary and a count of the features which the spatial filter has intersected with. The response will also provide a general summary which expresses the area of the input filter, the amount of layers and total number of features that have results inside this query.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the SiteBoundaryQueryResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryQueryResponse</div>
                <img src="../images/SiteBoundaryQueryResponse.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SummaryInformation</div>
                <img src="../images/SummaryInformation.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryLayers</div>
                <img src="../images/SiteBoundaryLayers.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryLayer</div>
                <img src="../images/SiteBoundaryLayer.png" /></div>
        </div>
        <h4>Element Descriptions</h4>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>SummaryInformation</th>
                    <td>The summary information element provides a general summary of the web service response and the results set which is contained within it. This element is the root to three other elements:
                        <ul>
                            <li>NumberOfSiteBoundaryLayers - the count of layers which were applicable to this query.</li>
                            <li>NumberOfSiteBoundaryFeatures - the count of the features which were applicable to this query</li>
                            <li>InputPolygonArea - This states the area of the filter used in the request in hectares</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>SiteBoundaryLayers</th>
                    <td>The SiteBoundaryLayers element can contain multiple SiteBoundaryLayer elements, each of which represent a Site Boundary layer.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryLayer</th>
                    <td>This element will contain the following elements :
                        <ul>
                            <li>ProviderMetadata - this represents the stored metadata for this particular Site Boundary Layer</li>
                            <li>NumberOfSiteBoundaryFeatures - this represents the amount of features of this Site Boundary that the input filter has intersected with.</li>
                            <li>SiteBoundaryArea - this represents the intersected area between this SiteBoundaryLayer and the input filter in hectares.</li>
                            <li>SiteBoundaryCoveragePercentage - this represents the intersected area of this SiteBoundaryLayer and the input filter as a percentage of the entire area of the SiteBoundaryLayer.</li>
                            <li>SiteBoundaryFeatures - this represents a list of the features which build up this Site Boundary Layer.</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>SiteBoundaryFeature</th>
                    <td>This elements main use is when polygon vertices have been requested. These will be represented inside the SiteBoundaryFeature element as ClippedPolygon elements.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Below is an illustrative example of a response which could be returned by a query of this web service.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <SiteBoundaryQueryResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <SummaryInformation>
            <NumberOfSiteBoundaryLayers>4</NumberOfSiteBoundaryLayers>
            <NumberOfSiteBoundaryFeatures>4</NumberOfSiteBoundaryFeatures>
            <InputPolygonArea>3.090169550632324</InputPolygonArea>
         </SummaryInformation>
         <SiteBoundaryLayers>
            <SiteBoundaryLayer>
               <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="2005-08-10 09:49:00.0" gatewayId="GA000325">
                  <ns8:DatasetTitle>Character Areas for England</ns8:DatasetTitle>
                  <ns8:DatasetProvider>Natural England</ns8:DatasetProvider>
                  <ns8:Abstract>
                     <ns8:Description>Character Areas for England</ns8:Description>
                  </ns8:Abstract>
               </ns8:ProviderMetadata>
               <NumberOfSiteBoundaryFeatures>1</NumberOfSiteBoundaryFeatures>
               <SiteBoundaryArea>3.090169550632324</SiteBoundaryArea>
               <SiteBoundaryCoveragePercentage>100.0</SiteBoundaryCoveragePercentage>
               <SiteBoundaryFeatures>
                  <SiteBoundaryFeature>
                     <SiteBoundary siteKey="37" providerKey="GA000325">
                        <SiteBoundaryName>Yorkshire Southern Pennine Fringe</SiteBoundaryName>
                        <SiteBoundaryType>Landscape Classification</SiteBoundaryType>
                     </SiteBoundary>
                  </SiteBoundaryFeature>
               </SiteBoundaryFeatures>
            </SiteBoundaryLayer>
            <SiteBoundaryLayer>
               <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="2010-08-05 12:20:00.0" gatewayId="GA000329">
                  <ns8:DatasetTitle>UK LBAP Boundaries</ns8:DatasetTitle>
                  <ns8:DatasetProvider>Joint Nature Conservation Committee</ns8:DatasetProvider>
                  <ns8:Abstract>
                     <ns8:Description><![CDATA[Note: this dataset does not contain the following 6 very large LBAPs:
<ul>
<li>Action for biodiversity in the South-West, a series of habitat and species plans to guide delivery
<li>Action for Wildlife in East Anglia
<li>Northumbrian Water Biodiversity Action Plan
<li>The Biodiversity of South East England: An Audit and Assessment
<li>WBG Vision
<li>Yorkshire and Humberside Regional Audit
<ul>]]></ns8:Description>
                  </ns8:Abstract>
               </ns8:ProviderMetadata>
               <NumberOfSiteBoundaryFeatures>1</NumberOfSiteBoundaryFeatures>
               <SiteBoundaryArea>3.090169550632324</SiteBoundaryArea>
               <SiteBoundaryCoveragePercentage>100.0</SiteBoundaryCoveragePercentage>
               <SiteBoundaryFeatures>
                  <SiteBoundaryFeature>
                     <SiteBoundary siteKey="505" providerKey="GA000329">
                        <SiteBoundaryName>Kirklees Biodiversity Action Plan</SiteBoundaryName>
                        <SiteBoundaryType>LBAP Areas</SiteBoundaryType>
                     </SiteBoundary>
                  </SiteBoundaryFeature>
               </SiteBoundaryFeatures>
            </SiteBoundaryLayer>
            <SiteBoundaryLayer>
               <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="2005-12-14 16:54:00.0" gatewayId="GA000344">
                  <ns8:DatasetTitle>Watsonian Vice Counties</ns8:DatasetTitle>
                  <ns8:DatasetProvider>National Biodiversity Network Trust</ns8:DatasetProvider>
                  <ns8:Abstract>
                     <ns8:Description>Watsonian Vice Counties version 1.0 June 2003. First test release for England, Scotland and Wales</ns8:Description>
                  </ns8:Abstract>
               </ns8:ProviderMetadata>
               <NumberOfSiteBoundaryFeatures>1</NumberOfSiteBoundaryFeatures>
               <SiteBoundaryArea>3.090169550632324</SiteBoundaryArea>
               <SiteBoundaryCoveragePercentage>100.0</SiteBoundaryCoveragePercentage>
               <SiteBoundaryFeatures>
                  <SiteBoundaryFeature>
                     <SiteBoundary siteKey="63" providerKey="GA000344">
                        <SiteBoundaryName>South-west Yorkshire</SiteBoundaryName>
                        <SiteBoundaryType>VC</SiteBoundaryType>
                     </SiteBoundary>
                  </SiteBoundaryFeature>
               </SiteBoundaryFeatures>
            </SiteBoundaryLayer>
            <SiteBoundaryLayer>
               <ns8:ProviderMetadata exchangeFormatVersion="1" datestamp="2010-08-12 16:23:00.0" gatewayId="GA000416">
                  <ns8:DatasetTitle>Local Record Centre boundaries for England</ns8:DatasetTitle>
                  <ns8:DatasetProvider>Natural England</ns8:DatasetProvider>
                  <ns8:Abstract>
                     <ns8:Description>This dataset provides draft boundaries of local record centres in England and reflects the current administrative coverage (LRC Review 2006).  The boundaries are derived from Ordnance Survey Boundaryline data, August 2003.    The boundaries have been generalised to the nearest 50m.  Where boundaries are known to combine administrative with vice-counties, e.g. Cheshire, these have been included.  Boundaries between different LRCs may overlap.  Please contact Richard Alexander, Natural England, regarding any corrections to this dataset.</ns8:Description>
                  </ns8:Abstract>
               </ns8:ProviderMetadata>
               <NumberOfSiteBoundaryFeatures>1</NumberOfSiteBoundaryFeatures>
               <SiteBoundaryArea>3.090169550632324</SiteBoundaryArea>
               <SiteBoundaryCoveragePercentage>100.0</SiteBoundaryCoveragePercentage>
               <SiteBoundaryFeatures>
                  <SiteBoundaryFeature>
                     <SiteBoundary siteKey="28" providerKey="GA000416">
                        <SiteBoundaryName>West Yorkshire</SiteBoundaryName>
                        <SiteBoundaryType>LRC Boundaries</SiteBoundaryType>
                     </SiteBoundary>
                  </SiteBoundaryFeature>
               </SiteBoundaryFeatures>
            </SiteBoundaryLayer>
         </SiteBoundaryLayers>
      </SiteBoundaryQueryResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>