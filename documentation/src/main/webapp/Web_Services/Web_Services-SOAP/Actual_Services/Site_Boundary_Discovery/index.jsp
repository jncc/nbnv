<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Site Boundary Discovery web service provides a list of all the Site Boundary Layers on the NBN Gateway. Each Site Boundary is provided with descriptive metadata, examples of this metadata can be seen within the &lsquo;Response&rsquo; section. The results of this web service can be used within the Site Boundary Query web service.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the SiteBoundaryDiscoveryRequest element.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryDiscoveryRequest</div>
                <img src="../images/SiteBoundaryDiscoveryRequest.png" /></div>
        </div>
        <p>As with all the web services that the NBN provides, a registration key is required for use. You can obtain a registration key by logging into the NBN Gateway and selecting &lsquo;Register for web services&rsquo; under &lsquo;my account&rsquo;.</p>
        <h3>Example</h3>
        <p>The following is an example of the SiteBoundaryDiscoveryRequest XML structure which is used to prompt the web service in returning a list of Site Boundaries in the form of a <a href="#Response">SiteBoundaryDiscoveryResponse.</a></p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sit="http://www.nbnws.net/SiteBoundary">
    <soapenv:Header />
    <soapenv:Body>
        <sit:SiteBoundaryDiscoveryRequest registrationKey=" YOUR_REGISTRATION_KEY" />
    </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        </jsp:attribute>
        <jsp:attribute name="response">
        <p>The following is a graphical representation of the structure of the SiteBoundaryDiscoveryResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryDiscoveryResponse</div>
                <img src="../images/SiteBoundaryDiscoveryResponse.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryDatasetList</div>
                <img src="../images/SiteBoundaryDatasetList.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">ProviderMetatdata</div>
                <img src="../images/ProviderMetatdata.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Abstract</div>
                <img src="../images/Abstract.png" /></div>
        </div>
        <h4>Element Descriptions</h4>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>SiteBoundaryDiscoveryResponse</th>
                    <td>This is the root element which is returned in the XML response. Note this has the attributes NBNLogo and TermsAndConditions. These should be displayed for any product using this web service.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryDatasetList</th>
                    <td>This is the element which contains the list of the Site Boundaries which are held on the NBN Gateway. Each Site Boundary is represented as a ProviderMetadata element</td>
                </tr>
                <tr>
                    <th>ProviderMetadata</th>
                    <td>The provider metadata is the container for the information which is held about a particular Site Boundary. The &quot;gatewayId&quot; attribute of this tag can be used to reference this Site Boundary in the Site Boundary Query web service</td>
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
                    <td>This element appears underneath the Abstract element and provides a detailed description of this Site Boundary.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Provided below is a fictitious example of what could be returned by the Site Boundary Discovery web service. Although this is an example response, it does conform to the schema to which real world responses conform to.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
    <ns3:SiteBoundaryDiscoveryResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/SiteBoundary" xmlns:ns4="http://www.nbnws.net/Map" xmlns:ns5="http://www.nbnws.net/Taxon" xmlns:ns6="http://www.nbnws.net/Dataset" xmlns:ns7="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns8="http://www.nbnws.net/Habitat" xmlns:ns9="http://www.nbnws.net/Designation" xmlns:ns10="http://www.nbnws.net/Metadata" xmlns:ns11="http://www.nbnws.net/" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
        <ns3:SiteBoundaryDatasetList>
            <ns10:ProviderMetadata exchangeFormatVersion="1" datestamp="2010-07-10 09:39:00.0" gatewayId="ID_TO_QUERY_WITH">
                <ns10:DatasetTitle>I am a site boundary stored on the NBN Gateway</ns10:DatasetTitle>
                <ns10:DatasetProvider>NBN Documentation Team</ns10:DatasetProvider>
                <ns10:Abstract>
                    <ns10:Description>I am a fictitious Site Boundary, I don’t really exist on the NBN Gateway and am only here to help you get started</ns10:Description>
                </ns10:Abstract>
            </ns10:ProviderMetadata >
        </ns3:SiteBoundaryDatasetList>
    </ns3:SiteBoundaryDiscoveryResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>