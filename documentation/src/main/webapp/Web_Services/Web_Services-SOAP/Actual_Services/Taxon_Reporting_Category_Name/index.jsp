<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The NBN Gateway Taxon Reporting Category Name web service lets you lookup the name for a taxon reporting category using its ID. These IDs can be obtained from many of the other web services and can be discovered using the <a href="../Taxon_Reporting_Category_List">TaxonReportingCategoryList</a> web service.</p>
        
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to be able to obtain the name of a Taxon Reporting category, its Taxon Reporting Category ID needs to be known. The ID of a Taxon Reporting needs to be supplied within the a TaxonReportingCategoryNameRequest XML structure.</p>
        <p>The TaxonReportingCategoryNameRequest XML structure has only one child element, TaxonReportingCategoryKey, the value of this element within the request will be the ID of the Taxon Reporting Category to look up.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the TaxonReportingCategoryNameRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">TaxonReportingCategoryNameRequest</div>
                <img src="../images/TaxonReportingCategoryNameRequest.png" />
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
                    <th>TaxonReportingCategoryKey</th>
                    <td>Yes</td>
                    <td>The value of this element is the ID of the Taxon Reporting Category whose name is to be obtained.</td>
                </tr>
            </tbody>
        </table>

        <h3>Example</h3>
        <p>Below is an example of the TaxonReportingCategoryNameRequest element with an ID set, NHMSYS0000080071.</p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tax="http://www.nbnws.net/TaxonReportingCategory">
   <soapenv:Header/>
   <soapenv:Body>
      <tax:TaxonReportingCategoryNameRequest registrationKey="YOUR_REGISTRATION_KEY_HERE">
         <tax:TaxonReportingCategoryKey>NHMSYS0000080071</tax:TaxonReportingCategoryKey>
      </tax:TaxonReportingCategoryNameRequest>
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The name of the Taxon Reporting Category which has been requested from the web services will be provided in the form of a TaxonReportingCategoryNameResponse XML structure. The TaxonReportingCategoryNameResponse XML structure will have a single child element, TaxonReportingCategory whose value is name of the Taxon reporting category whose name was requested.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the TaxonReportingCategoryNameResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">TaxonReportingCategoryNameResponse</div>
                <img src="../images/TaxonReportingCategoryNameResponse.png" />
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
                    <th>TaxonReportingCategory</th>
                    <td>The value of this element will be the name of the Taxon Reporting Category which was requested by ID using a TaxonReportingCategoryRequest element.</td>
                </tr>
                <tr>
                    <th>taxonReportingCategoryKey</th>
                    <td>This attribute will contain the same ID to that of the inital request which was sent to the Taxon Reporting Category Name web service.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>The following is an example response of the Taxon Reporting Category Name web service. This particular example is what could be expected if the example in the <a href="#Request">request</a> section is sent to the web service.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns5:TaxonReportingCategoryNameResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns5:TaxonReportingCategory taxonReportingCategoryKey="NHMSYS0000080071">insect - dragonfly (Odonata)</ns5:TaxonReportingCategory>
      </ns5:TaxonReportingCategoryNameResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>