<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Site Boundary Name web service lets you lookup the name for a site, such as a SSSI or Vice County, using the sites ID and dataset provider key.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <p>The following is a graphical representation of the structure of the SiteBoundaryNameRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryNameRequest</div>
                <img src="../images/SiteBoundaryNameRequest.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundary</div>
                <img src="../images/SiteBoundary.png" /></div>
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
                    <th>siteKey</th>
                    <td>Yes</td>
                    <td>The Site key attribute represents the site which is to be looked up. The site key is the unique identifier for a particular site that exists in a particular Dataset Provider.</td>
                </tr>
                <tr>
                    <th>providerKey</th>
                    <td>Yes</td>
                    <td>The provider key attribute is the Dataset provider key for which the &quot;siteKey&quot; has come from. This is important to define as site keys are not globally unique, they are only garenteed to be unique within the scope of a particular Dataset Provider.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryName</th>
                    <td>No</td>
                    <td>This element will be populated in the response of the web service.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryType</th>
                    <td>No</td>
                    <td>This element will be populated in the response of the web service.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example of the SiteBoundaryNameRequest XML structure which is used to prompt the web service in returning details about a particular Site Boundary given its &quot;siteKey&quot; and &quot;providerKey&quot;. The response to this request will be in the form of a <a href="#Response">SiteBoundaryNameResponse</a>.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sit="http://www.nbnws.net/SiteBoundary">
   <soapenv:Header/>
   <soapenv:Body>
      <sit:SiteBoundaryNameRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
      <sit:SiteBoundary siteKey="1" providerKey="GA000342" />
      </sit:SiteBoundaryNameRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
        </jsp:attribute>
        <jsp:attribute name="response">
        <p>The response will contain a single SiteBoundary element which has the same &quot;siteKey&quot; and &quot;providerKey&quot; values as that of the SiteBoundary element which was sent in the request. However, the response SiteBoundary element will have the its name and type elements populated with the correct data for that particular Site Boundary.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the response from the Site Boundary Name web service. The response will take the form of a SiteBoundary element contained within a SiteBoundaryNameResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryNameResponse</div>
                <img src="../images/SiteBoundaryNameResponse.png" /></div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundary</div>
                <img src="../images/SiteBoundary.png" /></div>
        </div>
        <h4>Element Descriptions</h4>
        <table class="attributeTable">
            <tbody>
                <tr class="head">
                    <th>Parameter</th>
                    <th>Comments</th>
                </tr>
                <tr>
                    <th>siteKey</th>
                    <td>The Site key attribute represents the site which has been looked up. This was provided by the SiteBoundary element sent in the request.</td>
                </tr>
                <tr>
                    <th>providerKey</th>
                    <td>The provider key attribute is the Dataset provider key for which the &quot;siteKey&quot; has come from. This was provided by the SiteBoundary element sent in the request.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryName</th>
                    <td>The value of this element represents the name of the Site Boundary which is represented by the &quot;siteKey&quot; and &quot;providerKey&quot; combination.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryType</th>
                    <td>The value of this element represents the type of the Site Boundary which is represented by the &quot;siteKey&quot; and &quot;providerKey&quot; combination.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example of a response which could be expected from the Site Boundary Name web service. The response to this web service will be in the form of a SiteBoundaryNameResponse element. This element will have the same values for the &quot;siteKey&quot; and &quot;providerKey&quot; attributes which were presented in the SiteBoundary element of the request. The response SiteBoundary element will also be populated with SiteBoundaryName and SiteBoundaryType elements</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <SiteBoundaryNameResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <SiteBoundary siteKey="1" providerKey="GA000342">
            <SiteBoundaryName>A'mhoine</SiteBoundaryName>
            <SiteBoundaryType>SSSI</SiteBoundaryType>
         </SiteBoundary>
      </SiteBoundaryNameResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>