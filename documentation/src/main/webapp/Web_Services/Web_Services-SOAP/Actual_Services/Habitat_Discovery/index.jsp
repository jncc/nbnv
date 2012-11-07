<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Habitat Discovery web service provides a list of all the Habitat Layers on the NBN Gateway. Each Habitat is provided with descriptive metadata, examples of this metadata can be seen within the &lsquo;Response&rsquo; section. The results of this web service can be used within the Habitat Query web service.</p>
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to obtain a list of Habitats you need to send a HabitatDiscoveryRequest to the web services. An example of this is provided below.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the HabitatDiscoveryRequest element.</p>
        <div class="graphicalRepresentation">
			<div class="graphicalRepresentationElement">
				<div class="caption">HabitatDiscoveryRequest</div>
				<img src="../images/HabitatDiscoveryRequest.png" />
			</div>
		</div>
        <h3>Example</h3>
        <p>The following is an example of the HabitatDiscoveryRequest XML structure which is used to prompt the web service in returning a list of Habitats in the form of a HabitatDiscoveryResponse.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:hab="http://www.nbnws.net/Habitat">
    <soapenv:Header/>
    <soapenv:Body>
        <hab:HabitatDiscoveryRequest registrationKey="YOUR_REGISTRATION_KEY "/>
    </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        <p>As with all the web services that the NBN provides, a registration key is required for use. You can obtain a registration key by logging into the NBN Gateway and selecting 'Register for web services' under 'my account'.</p>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The response will contain a list of Habitats along with the metadata which is stored for each of them.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the HabitatDiscoveryResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
			<div class="graphicalRepresentationElement">
				<div class="caption">HabitatDiscoveryResponse</div>
				<img src="../images/HabitatDiscoveryResponse.png" />
			</div>
			<div class="graphicalRepresentationElement">
				<div class="caption">HabitatDatasetList</div>
				<img src="../images/HabitatDatasetList.png" />
			</div>
			<div class="graphicalRepresentationElement">
				<div class="caption">ProviderMetadata</div>
				<img src="../images/ProviderMetadata.png" />
			</div>
			<div class="graphicalRepresentationElement">
				<div class="caption">Abstract</div>
				<img src="../images/Abstract.png" />
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
                    <th>HabitatDiscoveryResponse</th>
                    <td>This is the root element which is returned in the XML response. Note this has the attributes NBNLogo and TermsAndConditions. These should be displayed for any product using this web service.</td>
                </tr>
                <tr>
                    <th>HabitatDatasetList</th>
                    <td>This is the element which contains the list of the Habitats which are held on the NBN Gateway. Each Habitat is represented as a ProviderMetadata element</td>
                </tr>
                <tr>
                    <th>ProviderMetadata</th>
                    <td>The provider metadata is the container for the information which is held about a particular Habitat. The &quot;gatewayId&quot; attribute of this tag can be used to reference this Habitat in the Habitat Query web service</td>
                </tr>
                <tr>
                    <th>DatasetTitle</th>
                    <td>This is the title of the dataset</td>
                </tr>
                <tr>
                    <th>DatasetProvider</th>
                    <td>The dataset provider element is used to show who provided the dataset.</td>
                </tr>
                <tr>
                    <th>Description</th>
                    <td>This element appears underneath the Abstract element and provides a detailed description of this Habitat.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Provided below is a fictitious example of what could be returned by the Habitat Discovery web service. Although this is an example response, it does conform to the schema to which real world responses conform to.</p>
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
</t:actualWebserviceDocumentationPage>