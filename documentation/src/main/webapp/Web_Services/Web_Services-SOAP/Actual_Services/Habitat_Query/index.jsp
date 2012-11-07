<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Habitat Query web service is a spatial web service. This means that it will perform an intersection based on a user defined spatial object (for example, a polygon) and will return details regarding all of the site boundary shapes that your user defined spatial object intersects with.</p>
        <p>If you are only interested in certain layers, you can specify these and be assured that you will only get data regarding these.</p>
        <p>This web service is capable of returning the intersected shapes as clipped polygons. The default behaviour of the web services is to not return the clipped polygons unless you need them. This is done so that the default response of the web service is as compact as possible. However if you do require these simply flag &ldquo;RequiredPolygonVertices&rdquo; to true in your request.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to filter the Habitat features list to an area of interest, you need to define that area of interest. This web service can be queried using any of the spatial filters that the web services supports. Details of the valid spatial filters can be seen <a href="../../Schema_Elements/#Filter">here</a>.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the HabitatQueryRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">HabitatQueryRequest</div>
                <img src="../images/HabitatQueryRequest.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">HabitatLayerFilter</div>
                <img src="../images/HabitatLayerFilter.png" />
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
                    <th>HabitatLayerID</th>
                    <td>No</td>
                    <td>If you only wish to query a certian Habitat Layer, then you can specify it in this tag. This tag can occur multiple times in the HabitatRequest allowing you to query multiple different Habitat layers in one request. 0 occurances of this tag will create a request which queries all Habitat layers which are stored on the NBN Gateway.</td>
                </tr>
                <tr>
                    <th>RequiredPolygonVertices</th>
                    <td>No</td>
                    <td>The Habitat query web service is capable of returning the vertices of the clipped Habitat polygons which exist inside the HabitatLayerFilter. If this information is required then this tag must be added to the request with the value of true. Emitting this tag will make the web service assume that the Polygon Vertices are not required.</td>
                </tr>
                <tr>
                    <th>HabitatLayerFilter</th>
                    <td>Yes</td>
                    <td>The Habitat Layer Filter is a Spatial filter. It can take the form of either a :
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
        <p>Below is an example of a habitat query which uses a Bounded Box defined in EPSG_27700. In this example no Habitat Layers have been stated, this means that all the Habitat Layers on the NBN Gateway will be queried.</p>
        <p>It should also be noted that the &ldquo;RequiredPolygonVertices&rdquo; element contains the value &ldquo;false&rdquo;. This means that the response will not return the polygon vertices of the Habitat Features which exists inside the bounded box filter. Since this is the default behaviour of the web service, the same response would be generated if the &ldquo;RequiredPolygonVertices&rdquo; element was emitted.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns5:HabitatDiscoveryResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/Taxon" xmlns:ns2="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns3="http://www.nbnws.net/Dataset" xmlns:ns4="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns5="http://www.nbnws.net/Habitat" xmlns:ns6="http://www.nbnws.net/Spatial" xmlns:ns7="http://www.nbnws.net/SiteBoundary" xmlns:ns8="http://www.nbnws.net/Map" xmlns:ns9="http://www.nbnws.net/Designation" xmlns:ns10="http://www.nbnws.net/Metadata" xmlns:ns11="http://www.nbnws.net/" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns5:HabitatDatasetList>
            <ns10:ProviderMetadata exchangeFormatVersion="1" datestamp="17/03/2010" gatewayId="HL000001">
               <ns10:DatasetTitle>Example Habitat Dataset</ns10:DatasetTitle>
               <ns10:DatasetProvider>The imaginary provider</ns10:DatasetProvider>
               <ns10:Abstract>
                  <ns10:Description>This is an example habitat dataset which has been made up specifically for demonstration purposes</ns10:Description>
                  <ns10:DataCaptureMethod>There was no data capture method, this is a made up example.</ns10:DataCaptureMethod>
                  <ns10:DatasetPurpose>The purpose of this is to demonstrate the format of dataset when returned from the web service</ns10:DatasetPurpose>
                  <ns10:GeographicalCoverage>England</ns10:GeographicalCoverage>
                  <ns10:TemporalCoverage>From the mid 1990s onwards</ns10:TemporalCoverage>
                  <ns10:DataQuality>This is a description on the data quality which is provided by this habitat layer</ns10:DataQuality>
                  <ns10:AdditionalInformation>There is no more to say</ns10:AdditionalInformation>
               </ns10:Abstract>
               <ns10:AccessConstraints/>
               <ns10:UseConstraints/>
            </ns10:ProviderMetadata>
         </ns5:HabitatDatasetList>
      </ns5:HabitatDiscoveryResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
        </jsp:attribute>
        <jsp:attribute name="response">
        <p>At the very least, a response will contain a list of Habitats, the metadata which is stored about these Habitats and a count of the features which the spatial filter has intersected with. The response will also provide a general summary which expresses the area of the input filter, the amount of layers and total number of features that have results inside this query.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the HabitatQueryResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">HabitatQueryResponse</div>
                <img src="../images/HabitatQueryResponse.png" />
			</div>
			<div class="graphicalRepresentationElement">
                <div class="caption">SummaryInformation</div>
                <img src="../images/SummaryInformation.png" />
			</div>
			<div class="graphicalRepresentationElement">
                <div class="caption">HabitatLayers</div>
                <img src="../images/HabitatLayers.png" />
			</div>
			<div class="graphicalRepresentationElement">
                <div class="caption">HabitatLayer</div>
                <img src="../images/HabitatLayer.png" />
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
                    <th>SummaryInformation</th>
                    <td>The summary information element provides a general summary of the web service response and the results set which is contained within it. This element is the root to three other elements:
                        <ul>
                            <li>NumberOfHabitatLayers - the count of layers which were applicable to this query.</li>
                            <li>NumberOfHabitatFeatures - the count of the features which were applicable to this query</li>
                            <li>InputPolygonArea - This states the area of the filter used in the request in hectares</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>HabitatLayers</th>
                    <td>The HabitatLayers element can contain multiple HabitatLayer elements, each of which represent a Habitat layer.</td>
                </tr>
                <tr>
                    <th>HabitatLayer</th>
                    <td>This element will contain the following elements :
                        <ul>
                            <li>ProviderMetadata - this represents the stored metadata for this particular Habitat Layer</li>
                            <li>NumberOfHabitatFeatures - this represents the amount of features of this Habitat that the input filter has intersected with.</li>
                            <li>HabitatArea - this represents the intersected area between this HabitatLayer and the input filter in hectares.</li>
                            <li>HabitatCoveragePercentage - this represents the intersected area of this HabitatLayer and the input filter as a percentage of the entire area of the Habitat layer.</li>
                            <li>HabitatFeatures - this represents a list of the features which build up this Habitat Layer.</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th>HabitatFeature</th>
                    <td>This elements main use is when polygon vertices have been requested. These will be represented inside the HabitatFeature element as ClippedPolygon elements.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Below is an illustrative example of a response which could be returned by a query of this web service.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns5:HabitatQueryResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/Taxon" xmlns:ns2="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns3="http://www.nbnws.net/Dataset" xmlns:ns4="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns5="http://www.nbnws.net/Habitat" xmlns:ns6="http://www.nbnws.net/Spatial" xmlns:ns7="http://www.nbnws.net/SiteBoundary" xmlns:ns8="http://www.nbnws.net/Map" xmlns:ns9="http://www.nbnws.net/Designation" xmlns:ns10="http://www.nbnws.net/Metadata" xmlns:ns11="http://www.nbnws.net/" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns5:SummaryInformation>
            <ns5:NumberOfHabitatLayers>4</ns5:NumberOfHabitatLayers>
            <ns5:NumberOfHabitatFeatures>176</ns5:NumberOfHabitatFeatures>
            <ns5:InputPolygonArea>19489.509809985</ns5:InputPolygonArea>
         </ns5:SummaryInformation>
         <ns5:HabitatLayers>
            <ns5:HabitatLayer>
               <ns10:ProviderMetadata exchangeFormatVersion="1" datestamp="17/03/2010" gatewayId="HL000001">
                  <ns10:DatasetTitle>Lowland Grassland BAP Priority Habitat - England v2.01</ns10:DatasetTitle>
                  <ns10:DatasetProvider>Natural England</ns10:DatasetProvider>
                  <ns10:Abstract>
                     <ns10:Description>This is a spatial dataset that describes the geographic extent and location of lowland grassland habitats in England.  It combines data from the Natural England BAP priority habitat inventories for lowland calcareous grassland, lowland meadows, lowland dry-acid grassland, purple moor grass and rush pasture and lowland meadows inventories with data from the undetermined grassland inventory</ns10:Description>
                     <ns10:DataCaptureMethod>For the collated datasets, sites were assessed against BAP Priority Habitat definitions.  Where possible, all sites within priorities 1-4 were assessed, but in some cases resources only allowed priority 3 and above to be assessed and captured. &lt;/br> For sites which met the required ecological definitions, habitat boundaries were captured from existing GIS data files, or digitised from paper maps to defined standards, using OS MasterMap.  Aerial photographs were supplied to the contractors to aid identification of habitat boundaries.   A Habitat Data Capture Tool was also supplied to the contractors and used to attribute new or modified sites by habitat and degree of confidence in interpretation and mapping. &lt;/br> On the basis of evaluated data, sites were added or removed from the inventories, or amended.  Sites were removed where they no longer existed as vegetated habitat, i.e. having being developed, or due to strong evidence that grassland of priority habitat quality was no longer present.  Sites on the existing inventories were modified in terms of boundaries and/ or description based on more recent or comprehensive data.  Sites were added where supporting data met the required criteria for confidence and was comprehensive enough to establish presence of priority habitat or good quality semi-improved grassland (GQSIG), i.e. on the basis of the process undertaken to assess data and map sites.  &lt;a href="http://www.jncc.gov.uk/docs/NBN_LowlandGrasslandInventoryMetadata_England.doc">Further information.&lt;/a></ns10:DataCaptureMethod>
                     <ns10:DatasetPurpose><![CDATA[The original grassland inventories were developed during the mid 1990s to document the known localities of good quality species rich lowland grassland of nature conservation value in each English county. These inventories highlighted only those grassland sites for which English Nature, or other organisations, held detailed survey information at the time of their publication.<br></br>In 2004 English Nature published priority lowland grassland inventories, following re-interpretation of the original survey data, in light of priority grassland definitions and improved digitisation of site boundaries.  These inventories also incorporated the data captured by local record centres as part of the National Biodiversity Network South West Pilot.  These latest inventories have been published as a result of the two year joint funded project between English Nature (now part of Natural England) and the Department for Environment, Food and Rural Affairs (Defra).  This project set out to produce an up-to-date set of inventories which will be used to: <br /><br />
<ul>
<li>effectively target of Higher Level Stewardship (HLS) options within Environmental Stewardship, for which grassland is recognised as a priority by the UK Biodiversity Action Plan are a key focus</li>
<li>inform local and regional spatial planning, also contributing to delivery of the England Biodiversity Strategy</li>
<li>enable achievement of favourable conservation status for grasslands identified under Annex 1 of the Habitats Directive</li> </ul>]]></ns10:DatasetPurpose>
                     <ns10:GeographicalCoverage>England</ns10:GeographicalCoverage>
                     <ns10:TemporalCoverage>From the mid 1990s onwards</ns10:TemporalCoverage>
                     <ns10:DataQuality>The lowland grassland inventories are considered to have a good level of completeness in terms of coverage of priority grassland sites.   Sites in the inventory will have been accurately identified, though many sites may be mosaics with other habitats and scrub.  In many cases polygons are derived from the site boundary rather than the extent of the grassland stand itself. The original data sources are often more than 10 years old.
&lt;br />&lt;br />
The rate of grassland change over the last twenty years though means that many sites within the inventory, particularly those with no recent information, may have lost their conservation interest as result of agricultural intensification and changing land use.  A sample survey of non-statutory inventory sites found that only 28% of stands from polygons attributed as lowland calcareous grassland were in favourable condition.  A proportion of sites from the undetermined grassland inventory will potentially meet the criteria for one or more of the priority grassland types.</ns10:DataQuality>
                     <ns10:AdditionalInformation>The individual inventories are available to download from the Natural England website: &lt;a href="www.gis.naturalengland.org.uk">www.gis.naturalengland.org.uk&lt;/a></ns10:AdditionalInformation>
                  </ns10:Abstract>
                  <ns10:AccessConstraints/>
                  <ns10:UseConstraints/>
               </ns10:ProviderMetadata>
               <ns5:NumberOfHabitatFeatures>54</ns5:NumberOfHabitatFeatures>
               <ns5:HabitatArea>2071.9854268851564</ns5:HabitatArea>
               <ns5:HabitatCoveragePercentage>10.631285481708845</ns5:HabitatCoveragePercentage>
               <ns5:HabitatFeatures/>
            </ns5:HabitatLayer>
            <ns5:HabitatLayer>
               <ns10:ProviderMetadata exchangeFormatVersion="1" datestamp="19/03/2010" gatewayId="HL000002">
                  <ns10:DatasetTitle>Blanket Bog BAP Priority Habitat - England v2.1</ns10:DatasetTitle>
                  <ns10:DatasetProvider>Natural England</ns10:DatasetProvider>
                  <ns10:Abstract>
                     <ns10:Description>This is a spatial dataset that describes the geographic extent and location of the UK Biodiversity Action Plan blanket bog priority habitat in England. It has been derived as a result of the collation and interpretation of existing data sources including: NVC surveys (39%); Natural England’s ENSIS database, recording the main habitat on Sites of Special Scientific Interest management units (32%); National Soils Map (22%); and other ground survey (7%).
&lt;/br>
The inventory indicates an extent of blanket bog across England of 244,235 ha. This compares with the UK BAP estimate of 215,000 ha of blanket peat soil across England, of which in excess of 10% no longer support blanket bog vegetation.</ns10:Description>
                     <ns10:DataCaptureMethod>The inventories were based purely on data collation and processing exercises, no new survey work has been carried out; to create standardised, attributed, national inventories. The work was undertaken through a series of contracts with exeGesIS and ADAS and also internally.
&lt;/br>
The datasets originally used to create the inventory were Land Cover Map 2000 from CEH (Centre for Ecology and Hydrology, used under licence), ENSIS (English Nature Site Information System) and local English Nature teams digital NVC surveys. The ENSIS data (information on every Site of Special Scientific Interest in England) contains Phase1 information and upland habitats were extracted from it. The second phase of the project involved improving the definition of the upland boundary. This was originally based on the Less Favoured Area (LFA) boundary. However this boundary includes considerable areas of enclosed land. This was replaced by the Moorland Boundary of England, which encompasses some 42% of LFA land. This resulted in a loss of 9,303
ha of blanket bog from the inventory (20.18% UKP Aerial Photographs, 12,24% LCM, 3.81% ENSIS and 0.78% NVC). Additional datasets used for the update of the upland inventories were: Digital NVC Surveys, (Cumbria, North Penines, Skiddaw, Mill House, Squallcombe, Pinkworthy/Chains/Haddon Hill, Warren Farm and Holincote Moorland); South West NBN Pilot Data,
including other surveys (non-NVC); and Biological Survey of Common Land. The Soilscapes dataset was used to create a blanket bog layer outisde of the "known" areas (SSSIs and Bodmin common lands).</ns10:DataCaptureMethod>
                     <ns10:DatasetPurpose>Natural England's founding body English Nature commissioned the development of provisional England-wide upland habitat datasets to help with meeting Biodiversity Action Plan (BAP) monitoring obligations.</ns10:DatasetPurpose>
                     <ns10:GeographicalCoverage>England</ns10:GeographicalCoverage>
                     <ns10:TemporalCoverage>1995-2003&lt;/br>The dates for the contributing datasets are as follows: NVC surveys 1997 to 2003; Biological Survey of Common Lands 1995; soils data 1999; Other surveys 2000 to 2003. ENSIS derived data has been attributed based on the data capture date and not the date of condition assessments.</ns10:TemporalCoverage>
                     <ns10:DataQuality>The inventory covers the majority of significant areas of blanket bog across England. There may be poor separation from upland heathland, particularly where National Vegetation Classification (NVC) survey data is not available. Blanket bog often occurs as part of a mosaic of both habitats. Mapping accuracy may be poor for data derived from ENSIS where the whole site unit boundary defines the inventory polygon.</ns10:DataQuality>
                     <ns10:AdditionalInformation>The inventory is available to download from the Natural England website: &lt;a href=”www.gis.naturalengland.org.uk”>www.gis.naturalengland.org.uk &lt;/a></ns10:AdditionalInformation>
                  </ns10:Abstract>
                  <ns10:AccessConstraints/>
                  <ns10:UseConstraints/>
               </ns10:ProviderMetadata>
               <ns5:NumberOfHabitatFeatures>4</ns5:NumberOfHabitatFeatures>
               <ns5:HabitatArea>2961.5190539469068</ns5:HabitatArea>
               <ns5:HabitatCoveragePercentage>15.195451721569933</ns5:HabitatCoveragePercentage>
               <ns5:HabitatFeatures/>
            </ns5:HabitatLayer>
            <ns5:HabitatLayer>
               <ns10:ProviderMetadata exchangeFormatVersion="1" datestamp="19/03/2010" gatewayId="HL000005">
                  <ns10:DatasetTitle>Woodland BAP priority habitat - England v2.0</ns10:DatasetTitle>
                  <ns10:DatasetProvider>Natural England</ns10:DatasetProvider>
                  <ns10:Abstract>
                     <ns10:Description>This is a spatial dataset that describes the geographic extent and location of the UK Biodiversity Action Plan combined broadleaved woodland priority habitat (lowland beech and yew woodland, lowland mixed deciduous woodland, upland mixed ashwoods, upland oakwood and wet woodland) in England.
&lt;/br>
This inventory replaces Natural England's separate woodland BAP inventories (lowland mixed deciduous woodland, wet woodland, lowland beech and yew woodland, upland mixed ash woodland and upland oakwood). These earlier inventories were derived from Ancient Woodland Inventory polygons, attributed using SSSI and Phase II survey data and ecological modelling of habitats. The inventory provides England-wide coverage of semi-natural habitats and provides more comprehensive coverage than the previous separate inventories.</ns10:Description>
                     <ns10:DataCaptureMethod>The data is derived from Forest Research's National Inventory of Woodland and Trees (NIWT). It includes the following categories of woodland extracted from NIWT: broadleaved, shrub, coppice and coppice with standards.
&lt;/br>
The National Inventory of Woodland and Trees was based on interpretation of 1:25000 (some fill in 1:10000) aerial photography (flown 1991-2000) and plotted against OS 1:25000 mapping for the Forestry Commission. Miscellaneous adjustments to original aerial photography interpretation (API) have been made as detected by Survey Foresters, Woodland Grant Schemes (1992 to 2002) and additional aerial photography interpretation update for part of North England (flown 1999-2000).</ns10:DataCaptureMethod>
                     <ns10:DatasetPurpose>Natural England's founding body English Nature commissioned the development of provisional England-wide woodland BAP woodland habitat datasets to help with meeting Biodiversity Action Plan (BAP) monitoring obligations. These inventories were based purely on data collation and processing exercises, no new survey work was carried out; to create standardised, attributed, national inventories. The purpose of this inventory is to:
Better inform national strategic planning and target setting for delivery of the UK Biodiversity Action Plan; Better inform the identification of new designated sites for UK and EU legislation; better inform local decisions on development planning, habitat management and recreation; provide information on which to assess the national, regional and local status of habitats for reporting on UK BAP and EU directives; provide information on which to base sampling decisions for future surveillance and monitoring programmes. there is also a need to promote public enjoyment of wildlife, and in particular to raise awareness of the most threatened habitats in the UK.</ns10:DatasetPurpose>
                     <ns10:GeographicalCoverage>England</ns10:GeographicalCoverage>
                     <ns10:TemporalCoverage>1991-2000</ns10:TemporalCoverage>
                     <ns10:DataQuality>An inclusive policy has been adopted, assuming that woodlands classed as one of the above categories (i.e. non-plantation) fall with one of the Habitat Action Plan woodland categories.</ns10:DataQuality>
                     <ns10:AdditionalInformation>The inventory is available to download from the Natural England website: &lt;a href=”www.gis.naturalengland.org.uk”>www.gis.naturalengland.org.uk &lt;/a></ns10:AdditionalInformation>
                  </ns10:Abstract>
                  <ns10:AccessConstraints/>
                  <ns10:UseConstraints/>
               </ns10:ProviderMetadata>
               <ns5:NumberOfHabitatFeatures>88</ns5:NumberOfHabitatFeatures>
               <ns5:HabitatArea>829.5724307574403</ns5:HabitatArea>
               <ns5:HabitatCoveragePercentage>4.256507417813187</ns5:HabitatCoveragePercentage>
               <ns5:HabitatFeatures/>
            </ns5:HabitatLayer>
            <ns5:HabitatLayer>
               <ns10:ProviderMetadata exchangeFormatVersion="1" datestamp="19/03/2010" gatewayId="HL000007">
                  <ns10:DatasetTitle>Upland Heathland BAP Priority Habitat - England v2.1</ns10:DatasetTitle>
                  <ns10:DatasetProvider>Natural England</ns10:DatasetProvider>
                  <ns10:Abstract>
                     <ns10:Description>This is a spatial dataset that describes the geographic extent and location of the UK Biodiversity Action Plan upland heathland priority habitat in England. It has been derived as a result of the collation and interpretation of existing data sources including: NVC surveys (51%); Natural England’s ENSIS database, recording the main habitat on Sites of Special Scientific Interest management units (21%); Ordnance Survey - heath extract (18%); and other ground survey (10%).
&lt;/br>
The inventory indicates an extent of upland heath across England of 226,463 ha. This compares with the UK BAP estimate of 270,000 ha.</ns10:Description>
                     <ns10:DataCaptureMethod>The datasets originally used to create the inventory were Land Cover Map 2000 from CEH (Centre for Ecology and Hydrology, used under licence), ENSIS (English Nature Site Information System) and local English Nature teams’ digital NVC surveys. The ENSIS data (information on every Site of Special Scientific Interest in England) contains Phase 1 information and upland habitats were extracted from it. The second release of this inventory incorporated an improved definition of the upland boundary, replacing the Less Favoured Area (LFA) boundary by the Moorland Boundary of England, which encompasses some 42% of LFA land. Additional datasets used for the update of the upland inventories were: Digital NVC Surveys (Cumbria, North Pennines, Skiddaw, Mill House, Pinkworthy/Chains/Haddon Hill, Warren Farm and Holincote Moorland); South West NBN Data including other surveys (non-NVC); Biological Survey of Common Land; and Ordnance Survey MasterMap Data - Heath Extract, 41,682 ha. The Soilscapes dataset was used to help distinguish between "Acid Grassland - heath content unknown" and "Upland heathland" and to help in eliminating areas of "Upland Calcareous Grassland" and "Acid Grassland" identified by LCM 2000 where the underlying soil type indicated that these were unlikely. For the third phase of the inventory the capture of the Biological Survey of Common Land 1987-99 was completed. Additional datasets used for the update of the inventory were: National Trust East Midlands Upland BAP Survey 2002-04, United Utilities survey: Forest of Bowland 2000, Haweswater 2003; Shropshire County Council - Stiperstone 2002. Where overlap occurred with existing blanket bog polygons the habitat was assumed to be degraded blanket bog where the soils data indicated deap peat (> 0.5m)</ns10:DataCaptureMethod>
                     <ns10:DatasetPurpose>Natural England's founding body English Nature commissioned the development of provisional England-wide upland habitat datasets to help with meeting Biodiversity Action Plan (BAP) monitoring obligations. The inventories were based purely on data collation and processing exercises, no new survey work has been carried out; to create standardised, attributed, national
inventories. The work was undertaken through a series of contracts with exeGesIS and ADAS and also internally.</ns10:DatasetPurpose>
                     <ns10:GeographicalCoverage>England</ns10:GeographicalCoverage>
                     <ns10:TemporalCoverage>1987-2003&lt;/br>The dates for the contributing datasets are as follows: NVC surveys 1997 to 2003; Biological Survey of Common Lands 1995; soils data 1991; Other surveys 1987 to 2003. ENSIS derived data has been attributed based on the data capture date and not the date of condition assessments.</ns10:TemporalCoverage>
                     <ns10:DataQuality>The inventory covers the majority of significant areas of upland heathland across England. There may be poor separation from blanket bog, particularly where National Vegetation Classification (NVC) survey data is not available. The inventory includes some acid grassland, particularly where it occurs in a mosaic with dwarf shrubs.</ns10:DataQuality>
                     <ns10:AdditionalInformation>The inventory is available to download from the Natural England website: &lt;a href=”www.gis.naturalengland.org.uk”>www.gis.naturalengland.org.uk &lt;/a></ns10:AdditionalInformation>
                  </ns10:Abstract>
                  <ns10:AccessConstraints/>
                  <ns10:UseConstraints/>
               </ns10:ProviderMetadata>
               <ns5:NumberOfHabitatFeatures>30</ns5:NumberOfHabitatFeatures>
               <ns5:HabitatArea>2645.5864549166913</ns5:HabitatArea>
               <ns5:HabitatCoveragePercentage>13.574412495286493</ns5:HabitatCoveragePercentage>
               <ns5:HabitatFeatures/>
            </ns5:HabitatLayer>
         </ns5:HabitatLayers>
      </ns5:HabitatQueryResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>