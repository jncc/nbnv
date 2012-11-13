<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Site Boundary List web service lets you lookup a list of site names and IDs for a site type, such as a SSSI or Vice County, using a site type code.</p>

    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to be able to use the site boundary list web service a SiteBoundaryListRequest XML element must be created. The structure and requirements of this element are presented below.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the SiteBoundaryListRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryListRequest</div>
                <img src="../images/SiteBoundaryListRequest.png" />
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
                    <th>SiteBoundaryType</th>
                    <td>Yes</td>
                    <td>
                        <p>This is the Site Boundary Type to which you wish to find more information about. Valid values for this tag are the well known abbreviations of Site Boundary Types.</p>
                        <p>The following is a comprehensive list of Site Boundary Type well known abbreviations along with there full name: </p>
                        <ul>
                            <li>ASSI	Areas of Special Scientific Interest</li>
                            <li>LBAP Areas - LBAP Areas</li>
                            <li>Common lands - Common lands</li>
                            <li>LERC - Leicestershire Wildlife Sites</li>
                            <li>Ramsar - Ramsar</li>
                            <li>SAC - Special Area of Conservation</li>
                            <li>SPA - Special Protection Area</li>
                            <li>SSSI - Site of Special Scientific Interest</li>
                            <li>NNR - National Nature Reserve</li>
                            <li>MNR - Marine Nature Reserve</li>
                            <li>LNR - Local Nature Reserve</li>
                            <li>AONB - Area of Outstanding Natural Beauty</li>
                            <li>VC - Watsonian Vice County</li>
                            <li>Landscape Classification - Landscape Classification</li>
                            <li>Administrative - Administrative boundaries</li>
                            <li>SWT - Scottish Wildlife Trust Reserves</li>
                            <li>SBBRC - Scottish Borders Biological Records Centre</li>
                            <li>IBA - RSPB Important Bird Areas</li>
                            <li>NT - National Trust Boundaries</li>
                            <li>RSPB Nature Reserve - RSPB Nature Reserves in the UK</li>
                            <li>LRC Boundaries - Local Record Centre boundaries</li>
                            <li>NTS - The National Trust for Scotland</li>
                            <li>AI - Aggregate Industries Sites</li>
                            <li>JMT - John Muir Trust Boundaries</li>
                            <li>HBRC - Hertfordshire BRC Wildlife Sites</li>
                        </ul>
                    </td>
                </tr>
            </tbody>
        </table>

        <h3>Example</h3>
        <p>The following is an example of a constructed SiteBoundaryListRequest XML element which is to be sent to the Site Boundary List web service.</p>
        <p>This particular example will lookup the site names and IDs for Site Boundarys of type "SSSI".</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sit="http://www.nbnws.net/SiteBoundary">
   <soapenv:Header/>
   <soapenv:Body>
      <sit:SiteBoundaryListRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
         <sit:SiteBoundaryType>SSSI</sit:SiteBoundaryType>
      </sit:SiteBoundaryListRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The response to the Site Boundary List web service will provide a list of Site Boundaries which are of the type which was specified in the SiteBoundaryListRequest. The list is presented in the form of a SiteBoundaryListResponse XML structure.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the SiteBoundaryListResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryListResponse</div>
                <img src="../images/SiteBoundaryListResponse.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundaryList</div>
                <img src="../images/SiteBoundaryList.png" />
            </div>
            <div class="graphicalRepresentationElement">
                <div class="caption">SiteBoundary</div>
                <img src="../images/SiteBoundary.png" />
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
                    <th>SiteBoundaryList</th>
                    <td>This is a list of Site Boundary elements which are of the same type as that specified in the SiteBoundaryListRequest.</td>
                </tr>
                <tr>
                    <th>siteKey</th>
                    <td>The Site key attribute represents the site ID for a particular Site Boundary Element.</td>
                </tr>
                <tr>
                    <th>providerKey</th>
                    <td>The provider key attribute is the ID of the Dataset provider key for which the "siteKey" has come from.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryName</th>
                    <td>The value of this element represents the name of the Site Boundary which is represented by the "siteKey" and "providerKey" combination.</td>
                </tr>
                <tr>
                    <th>SiteBoundaryType</th>
                    <td>The value of this element represents the type of the Site Boundary which is represented by the "siteKey" and "providerKey" combination.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example of a response which could be expected from this web service. This particular response has come about by requesting a list of Site Boundaries of type Ramsar.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <SiteBoundaryListResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <SiteBoundaryList>
            <SiteBoundary siteKey="7UK127" providerKey="GA000333">
               <SiteBoundaryName>Ballynahone Bog Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK123" providerKey="GA000333">
               <SiteBoundaryName>Belfast Lough Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK142" providerKey="GA000333">
               <SiteBoundaryName>Black Bog Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK119" providerKey="GA000333">
               <SiteBoundaryName>Carlingford Lough Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK128" providerKey="GA000333">
               <SiteBoundaryName>Cuilcagh Mountain Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK143" providerKey="GA000333">
               <SiteBoundaryName>Fairy Water Bogs Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK155" providerKey="GA000333">
               <SiteBoundaryName>Fardrum And Roosky Turloughs</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK129" providerKey="GA000333">
               <SiteBoundaryName>Garron Plateau Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK149" providerKey="GA000333">
               <SiteBoundaryName>Garry Bog Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK109" providerKey="GA000333">
               <SiteBoundaryName>Larne Lough Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK133" providerKey="GA000333">
               <SiteBoundaryName>Lough Foyle Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK009" providerKey="GA000333">
               <SiteBoundaryName>Lough Neagh And Lough Beg Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK105" providerKey="GA000333">
               <SiteBoundaryName>Pettigoe Plateau Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK145" providerKey="GA000333">
               <SiteBoundaryName>Slieve Beagh Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK120" providerKey="GA000333">
               <SiteBoundaryName>Strangford Lough Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK156" providerKey="GA000333">
               <SiteBoundaryName>Turmennan</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
            <SiteBoundary siteKey="7UK110" providerKey="GA000333">
               <SiteBoundaryName>Upper Lough Erne Ramsar Site</SiteBoundaryName>
               <SiteBoundaryType>Ramsar</SiteBoundaryType>
            </SiteBoundary>
         </SiteBoundaryList>
      </SiteBoundaryListResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>