<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:actualWebserviceDocumentationPage>
    <jsp:attribute name="introduction">
        <p>The Designation list web service lists all species designations that the NBN Gateway holds information about. Each designation is described by :</p>
        <ul>
            <li>Name</li>
            <li>Key</li>
            <li>Abbreviation</li>
            <li>Description</li>
        </ul>
        <p>The information which is obtained from this web service can be used in other web services that allow species designation filters. The following is a list of web services which can be used in conjunction with this web service :</p>
        <ul>
            <li><a href="../One_Site_Data/">One Site Data</a></li>
            <li><a href="../Species_Density_Data/">Species Density Data</a></li>
            <li><a href="../Species_List/">Species List</a></li>
            <li><a href="../Taxon_Reporting_Category_List/">Taxon Reporting Category List</a></li>
        </ul>
    </jsp:attribute>
    <jsp:attribute name="request">
        <p>In order to obtain a list of Designations you need to send a DesignationListRequest XML structure to the web services. The DesignationListRequest has the ability to query only certain Designation Categories or only certain Designations.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the DesignationListRequest element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">DesignationListRequest</div>
                <img src="../images/DesignationListRequest.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DesignationFilters</div>
                <img src="../images/DesignationFilters.png" />
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
                    <th>DesignationCategoryFilter</th>
                    <td>No</td>
                    <td>Providing DesignationCategoryFilters will prompt the web service to only return a Designation lists for those Designation Categories which have been provided. This means that a response from the web service can be tailored to provide only information which is useful for your application.</td>
                </tr>
                <tr>
                    <th>DesignationFilter</th>
                    <td>No</td>
                    <td>Providing a Designation Filter will only return information regarding an individual designation. Information on multiple designations can be obtained in a single request as this element is contained in the DesignationFilters element.</td>
                </tr>
            </tbody>
        </table>
        <p>As with all the web services that the NBN provides, a registration key is required for use. You can obtain a registration key by logging into the NBN Gateway and selecting ‘Register for web services’ under ‘my account’.</p>
        <h3>Example</h3>
        <p>The following is an example of the DesignationListRequest XML structure which is used to prompt the web service in returning a list of Designations in the form of a <a href="#Response">DesignationListResponse</a></p>
        <nbn:prettyprint-code lang="xml">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:des="http://www.nbnws.net/Designation">
   <soapenv:Header/>
   <soapenv:Body>
      <des:DesignationListRequest registrationKey="YOUR_REGISTRATION_KEY_HERE" />
   </soapenv:Body>
</soapenv:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
    <jsp:attribute name="response">
        <p>The response of the Designation List web service will return a list of Designation Categories, each one being a list of Designations.</p>
        <h3>Graphical Representation</h3>
        <p>The following is a graphical representation of the structure of the DesignationListResponse element. Also provided is a table which describes the meaning of the elements in the graphical representation.</p>
        <div class="graphicalRepresentation">
            <div class="graphicalRepresentationElement">
                <div class="caption">DesignationListResponse</div>
                <img src="../images/DesignationListResponse.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DesignationCategory</div>
                <img src="../images/DesignationCategory.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">DesignationList</div>
                <img src="../images/DesignationList.png" />
			</div>
            <div class="graphicalRepresentationElement">
                <div class="caption">Designation</div>
                <img src="../images/Designation.png" />
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
                    <th>DesignationCategory:name</th>
                    <td>The value of this attribute represents the name of the designation category.</td>
                </tr>
                <tr>
                    <th>Designation:name</th>
                    <td>The value of this element represents the full name of the designation.</td>
                </tr>
                <tr>
                    <th>Designation:key</th>
                    <td>The value of this element represents a key which allows this Designation to be referenced and by other web services.</td>
                </tr>
                <tr>
                    <th>Designation:abbreviation</th>
                    <td>The value of this element represents the abbreviated name of this designation. This will give an insight into the purpose of this designation and possible which Taxons could be applied to it.</td>
                </tr>
                <tr>
                    <th>Designation:description</th>
                    <td>The value of this element provides a description of this designation.</td>
                </tr>
            </tbody>
        </table>
        <h3>Example</h3>
        <p>Provided below is an example of a DesignationListResponse which could be expected to be returned from the Designation List web service. This particular example was obtained from the Designation List using no filtering web service. It should be noted that this response may change in future invocations of this web service.</p>
        <nbn:prettyprint-code lang="xml">
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <ns6:DesignationListResponse TermsAndConditions="http://data.nbn.org.uk/help/popups/generalTerms.jsp" NBNLogo="http://data.nbn.org.uk/images/NBNPower.gif" xmlns="http://www.nbnws.net/SiteBoundary" xmlns:ns2="http://www.nbnws.net/Spatial" xmlns:ns3="http://www.nbnws.net/Taxon/Taxonomy" xmlns:ns4="http://www.nbnws.net/Taxon" xmlns:ns5="http://www.nbnws.net/TaxonReportingCategory" xmlns:ns6="http://www.nbnws.net/Designation" xmlns:ns7="http://www.nbnws.net/Habitat" xmlns:ns8="http://www.nbnws.net/Metadata" xmlns:ns9="http://www.nbnws.net/Map" xmlns:ns10="http://www.nbnws.net/" xmlns:ns11="http://www.nbnws.net/Dataset" xmlns:ns12="http://www.nbnws.net/Taxon/GatewayData">
         <ns6:DesignationCategory name="International">
            <ns6:DesignationList>
               <ns6:Designation>
                  <ns6:name>Birds Directive Annex 1</ns6:name>
                  <ns6:key>BIRDSDIR-A1</ns6:key>
                  <ns6:abbreviation>BirdsDir-A1</ns6:abbreviation>
                  <ns6:description>Birds which are the subject of special conservation measures concerning their habitat in order to ensure their survival and reproduction in their area of distribution. As appropriate, Special Protection Areas to be established to assist conservation measures. Note that the contents of this annex have been updated in April 2003 following the Treaty of Accession.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Birds Directive Annex 2.1</ns6:name>
                  <ns6:key>BIRDSDIR-A2.1</ns6:key>
                  <ns6:abbreviation>BirdsDir-A2.1</ns6:abbreviation>
                  <ns6:description>Birds which may potentially be hunted under national legislation within the geographical land and sea area to which the Directive applies. (Note that some species are protected by the national legislation of some Member States although hunting would potentially be legal under the Directive).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Birds Directive Annex 2.2</ns6:name>
                  <ns6:key>BIRDSDIR-A2.2</ns6:key>
                  <ns6:abbreviation>BirdsDir-A2.2</ns6:abbreviation>
                  <ns6:description>Birds which may potentially be hunted under national legislation only within certain specified Member States. (Note that some species are protected by the national legislation of some Member States although hunting would potentially be legal under the Directive).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Bonn Convention Appendix 1</ns6:name>
                  <ns6:key>BONN-A1</ns6:key>
                  <ns6:abbreviation>Bonn-A1</ns6:abbreviation>
                  <ns6:description>Endangered migratory species in danger of extinction throughout all or a significant portion of their range, and for which Range States are obliged to prohibit taking and to take protective measures to conserve. (Note that taking may be permitted in some circumstances as outlined in Article III.5.)</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Bonn Convention Appendix 2</ns6:name>
                  <ns6:key>BONN-A2</ns6:key>
                  <ns6:abbreviation>Bonn-A2</ns6:abbreviation>
                  <ns6:description>Migratory species having an unfavourable conservation status for which Range States are encouraged to conclude international agreements for their benefit.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Berne Convention Appendix 1</ns6:name>
                  <ns6:key>BERNE-A1</ns6:key>
                  <ns6:abbreviation>Berne-A1</ns6:abbreviation>
                  <ns6:description>Special protection (`appropriate and necessary legislative and administrative measures`) for the plant taxa listed, including prohibition of deliberate picking, collecting, cutting, uprooting and, as appropriate, possession or sale.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Berne Convention Appendix 2</ns6:name>
                  <ns6:key>BERNE-A2</ns6:key>
                  <ns6:abbreviation>Berne-A2</ns6:abbreviation>
                  <ns6:description>Special protection (`appropriate and necessary legislative and administrative measures`) for the animal taxa listed, including:
All forms of deliberate capture and keeping and deliberate killing;
The deliberate damage to or destruction of breeding or resting sites;
The deliberate disturbance of wild fauna, particularly during the period of breeding, rearing and hibernation, insofar as disturbance would be significant in relation to the objectives of this Convention;
The deliberate destruction or taking of eggs from the wild or keeping these eggs even if empty;
The possession of and internal trade in these animals, alive or dead, including stuffed animals and any readily recognisable part or derivative thereof, where this would contribute to the effectiveness of the provisions of this article.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Habitats and species directive Annex 2 - priority species</ns6:name>
                  <ns6:key>HABDIR-A2</ns6:key>
                  <ns6:abbreviation>HabDir-A2</ns6:abbreviation>
                  <ns6:description>Species which are endangered, the conservation of which the Community has a particular responsibility in view of the proportion of their natural range which falls within the territory of the Community. They require the designation of special areas of conservation.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Habitats and species directive Annex 2 - non-priority species</ns6:name>
                  <ns6:key>HABDIR-A2*</ns6:key>
                  <ns6:abbreviation>HabDir-A2*</ns6:abbreviation>
                  <ns6:description>Animal and plant species of Community interest (i.e. endangered, vulnerable, rare or endemic in the European Community) whose conservation requires the designation of special areas of conservation. Note that the contents of this annex have been updated in April 2003 following the Treaty of Accession.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Habitats and species directive Annex 4</ns6:name>
                  <ns6:key>HABDIR-A4</ns6:key>
                  <ns6:abbreviation>HabDir-A4</ns6:abbreviation>
                  <ns6:description>Animal and plant species of Community interest (i.e. endangered, vulnerable, rare or endemic in the European Community) in need of strict protection. They are protected from killing, disturbance or the destruction of them or their habitat. Note that the contents of this annex have been updated in April 2003 following the Treaty of Accession.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Habitats and species directive Annex 5</ns6:name>
                  <ns6:key>HABDIR-A5</ns6:key>
                  <ns6:abbreviation>HabDir-A5</ns6:abbreviation>
                  <ns6:description>Animal and plant species of Community interest whose taking in the wild and exploitation may be subject to management measures.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>EC CITES Annex A</ns6:name>
                  <ns6:key>ECCITES-A</ns6:key>
                  <ns6:abbreviation>ECCITES-A</ns6:abbreviation>
                  <ns6:description>All CITES Appendix I species.
Some CITES Appendix II and III species, for which the EU has adopted stricter domestic measures.
Some non-CITES species.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>EC CITES Annex B</ns6:name>
                  <ns6:key>ECCITES-B</ns6:key>
                  <ns6:abbreviation>ECCITES-B</ns6:abbreviation>
                  <ns6:description>All other CITES Appendix II species not listed in Annex A.
Some CITES Appendix III species.
Some non-CITES species.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>EC CITES Annex C</ns6:name>
                  <ns6:key>ECCITES-C</ns6:key>
                  <ns6:abbreviation>ECCITES-C</ns6:abbreviation>
                  <ns6:description>All other CITES Appendix III species not listed in Annex A or Annex B.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>EC CITES Annex D</ns6:name>
                  <ns6:key>ECCITES-D</ns6:key>
                  <ns6:abbreviation>ECCITES-D</ns6:abbreviation>
                  <ns6:description>Some CITES Appendix III species for which the EU holds a reservation (CITES reservations: English, French, Spanish).
Some non-CITES species.</ns6:description>
               </ns6:Designation>
            </ns6:DesignationList>
         </ns6:DesignationCategory>
         <ns6:DesignationCategory name="Nat Legislation">
            <ns6:DesignationList>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 1 Part 1)</ns6:name>
                  <ns6:key>WACA-SCH1_PART1</ns6:key>
                  <ns6:abbreviation>WACA-Sch1_part1</ns6:abbreviation>
                  <ns6:description>Birds which are protected by special penalties at all times.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 1 Part 2)</ns6:name>
                  <ns6:key>WACA-SCH1_PART2</ns6:key>
                  <ns6:abbreviation>WACA-Sch1_part2</ns6:abbreviation>
                  <ns6:description>Birds which are protected by special penalties during the close season.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.1 (killing/injuring))</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.1(KILL/INJURING)</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.1(kill/injuring)</ns6:abbreviation>
                  <ns6:description>Section 9.1. Animals which are protected from intentional killing or injuring.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.1 (taking))</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.1(TAKING)</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.1(taking)</ns6:abbreviation>
                  <ns6:description>Section 9.1 Animals which are protected from taking.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.2)</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.2</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.2</ns6:abbreviation>
                  <ns6:description>Section 9.2 Animals which are protected from being possessed or controlled (live or dead).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.4a)</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.4A</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.4a</ns6:abbreviation>
                  <ns6:description>Section 9.4 Animals which are protected from intentional damage or destruction to any structure or place used for shelter or protection.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.4b)</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.4B</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.4b</ns6:abbreviation>
                  <ns6:description>Section 9.4 Animals which are protected from intentional disturbance while occupying a structure or place used for shelter or protection.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.5a)</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.5A</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.5a</ns6:abbreviation>
                  <ns6:description>Section 9.5 Animals which are protected from being sold, offered for sale or being held or transported for sale either live or dead, whole or part.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5 Section 9.5b)</ns6:name>
                  <ns6:key>WACA-SCH5_SECT9.5B</ns6:key>
                  <ns6:abbreviation>WACA-Sch5_sect9.5b</ns6:abbreviation>
                  <ns6:description>Section 9.5 Animals which are protected from being published or advertised as being for sale.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 8)</ns6:name>
                  <ns6:key>WACA-SCH8</ns6:key>
                  <ns6:abbreviation>WACA-Sch8</ns6:abbreviation>
                  <ns6:description>Plants which are protected from: intentional picking, uprooting or destruction (Section 13 1a); selling, offering for sale, possessing or transporting for the purpose of sale (live or dead, part or derivative) (Section 13 2a); advertising (any of these) for buying or selling (Section 13 2b).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Wildlife (Northern Ireland) Order 1985 (Schedule 1 Part 1)</ns6:name>
                  <ns6:key>W(NI)O-SCH1_PART1</ns6:key>
                  <ns6:abbreviation>W(NI)O-Sch1_part1</ns6:abbreviation>
                  <ns6:description>Birds which are protected by special penalties at all times.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Wildlife (Northern Ireland) Order 1985 (Schedule 1 Part 2)</ns6:name>
                  <ns6:key>W(NI)O-SCH1_PART2</ns6:key>
                  <ns6:abbreviation>W(NI)O-Sch1_part2</ns6:abbreviation>
                  <ns6:description>Birds which are protected by special penalties during the close season.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Wildlife (Northern Ireland) Order 1985 (Schedule 5)</ns6:name>
                  <ns6:key>W(NI)O-SCH5</ns6:key>
                  <ns6:abbreviation>W(NI)O-Sch5</ns6:abbreviation>
                  <ns6:description>Animals which are protected at all times.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Wildlife (Northern Ireland) Order 1985 (Schedule 8 - Part 1)</ns6:name>
                  <ns6:key>W(NI)O-SCH8_PART1</ns6:key>
                  <ns6:abbreviation>W(NI)O-Sch8_part1</ns6:abbreviation>
                  <ns6:description>Plants which are protected from intentional picking, removal or destruction and from selling (in whole or part) and from advertising for sale.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Wildlife (Northern Ireland) Order 1985 (Schedule 8 - Part 2)</ns6:name>
                  <ns6:key>W(NI)O-SCH8_PART2</ns6:key>
                  <ns6:abbreviation>W(NI)O-Sch8_part2</ns6:abbreviation>
                  <ns6:description>Plants which may not be sold.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Natural Environment &amp; Rural Communities Act 2006 - Species of Principal Importance in England (s41)</ns6:name>
                  <ns6:key>ENGLAND NERC S.41</ns6:key>
                  <ns6:abbreviation>England NERC S.41</ns6:abbreviation>
                  <ns6:description>Species “of principal importance for the purpose of conserving biodiversity” covered under section 41 (England) of the NERC Act (2006) and therefore need to be taken into consideration by a public body when performing any of its functions with a view to conserving biodiversity.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Natural Environment and Rural Communities Act 2006 - Species of Principal Importance in Wales (s42)</ns6:name>
                  <ns6:key>WALES NERC S.42</ns6:key>
                  <ns6:abbreviation>Wales NERC S.42</ns6:abbreviation>
                  <ns6:description>Species “of principal importance for the purpose of conserving biodiversity” covered under Section 42 (Wales) of the NERC Act (2006) and therefore need to be taken into consideration by a public body when performing any of its functions with a view to conserving biodiversity.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Scottish Biodiversity List of species of principal importance for biodiversity conservation</ns6:name>
                  <ns6:key>SCOTTISH BIODIVERSITY LIST</ns6:key>
                  <ns6:abbreviation>Scottish Biodiversity List</ns6:abbreviation>
                  <ns6:description>The Scottish Biodiversity List is a list of flora, fauna and habitats considered by the Scottish Ministers to be of principal importance for biodiversity conservation. The development of the list has been a collaborative effort involving a great many stakeholders overseen by scientists from the Scottish Biodiversity Forum. Completion of the list is the first time such a stocktake has been done in Scotland. The Scottish Biodiversity List is a tool for public bodies and others doing their Biodiversity Duty. The publication of the Scottish Biodiversity List satisfies the requirements of Section 2(4) of The Nature Conservation (Scotland) Act 2004.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Protection of Badgers Act (1992)</ns6:name>
                  <ns6:key>PROTECTION OF BADGERS ACT (1992)</ns6:key>
                  <ns6:abbreviation>Protection of Badgers Act (1992)</ns6:abbreviation>
                  <ns6:description>The Protection of Badgers Act 1992 protects badgers from taking, injuring, killing, cruel treatment, selling, possessing, marking and having their setts interfered with, subject to exceptions.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5)</ns6:name>
                  <ns6:key>WACA-SCH5SECT9.4C</ns6:key>
                  <ns6:abbreviation>WACA-Sch5Sect9.4c</ns6:abbreviation>
                  <ns6:description>Animals which are protected from their access to any structure or place which they use for shelter or protection being obstructed.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Wildlife and Countryside Act 1981 (Schedule 5)</ns6:name>
                  <ns6:key>WACA-SCH5SECT9.4A*</ns6:key>
                  <ns6:abbreviation>WACA-Sch5Sect9.4A*</ns6:abbreviation>
                  <ns6:description>Animals which are protected from their access to any structure or place which they use for shelter or protection being obstructed.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Conservation (Natural Habitats, &amp;c.) Regulations 1994 (Schedule 2)</ns6:name>
                  <ns6:key>CONSREGS-SCH2</ns6:key>
                  <ns6:abbreviation>ConsRegs-Sch2</ns6:abbreviation>
                  <ns6:description>Schedule 2: European protected species of animals.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Conservation (Natural Habitats, &amp;c.) Regulations 1994 (Schedule 3)</ns6:name>
                  <ns6:key>CONSREGS-SCH3</ns6:key>
                  <ns6:abbreviation>ConsRegs-Sch3</ns6:abbreviation>
                  <ns6:description>Schedule 3: Animals which may not be taken or killed in certain ways.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>The Conservation (Natural Habitats, &amp;c.) Regulations 1994 (Schedule 4)</ns6:name>
                  <ns6:key>CONSREGS-SCH4</ns6:key>
                  <ns6:abbreviation>ConsRegs-Sch4</ns6:abbreviation>
                  <ns6:description>Schedule 4: European protected species of plants.</ns6:description>
               </ns6:Designation>
            </ns6:DesignationList>
         </ns6:DesignationCategory>
         <ns6:DesignationCategory name="Other rare/scarce">
            <ns6:DesignationList>
               <ns6:Designation>
                  <ns6:name>Nationally scarce</ns6:name>
                  <ns6:key>STATUS-NS</ns6:key>
                  <ns6:abbreviation>Status-NS</ns6:abbreviation>
                  <ns6:description>Occurring in 16-100 hectads in Great Britain. Excludes rare species qualifying under the main IUCN criteria.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Nationally Notable</ns6:name>
                  <ns6:key>NOTABLE</ns6:key>
                  <ns6:abbreviation>Notable</ns6:abbreviation>
                  <ns6:description>Species which are estimated to occur within the range of 16 to 100 10km squares. (subdivision into Notable A and Notable B is not always possible because there may be insufficient information available). Superseded by Nationally Scarce, and therefore no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Nationally Notable A</ns6:name>
                  <ns6:key>NOTABLE-A</ns6:key>
                  <ns6:abbreviation>Notable-A</ns6:abbreviation>
                  <ns6:description>Taxa which do not fall within RDB categories but which are none-the-less uncommon in Great Britain and thought to occur in 30 or fewer 10km squares of the National Grid or, for less well-recorded groups, within seven or fewer vice-counties. Superseded by Nationally Scarce, and therefore no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Nationally Notable B</ns6:name>
                  <ns6:key>NOTABLE-B</ns6:key>
                  <ns6:abbreviation>Notable-B</ns6:abbreviation>
                  <ns6:description>Taxa which do not fall within RDB categories but which are none-the-less uncommon in Great Britain and thought to occur in between 31 and 100 10km squares of the National Grid or, for less-well recorded groups between eight and twenty vice-counties. Superseded by Nationally Scarce, and therefore no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Nationally rare marine species</ns6:name>
                  <ns6:key>MARINE-NR</ns6:key>
                  <ns6:abbreviation>Marine-NR</ns6:abbreviation>
                  <ns6:description>Species which occur in eight or fewer 10km X 10km grid squares containing sea (or water of marine saline influence) within the three mile territorial limit.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Nationally scarce marine species</ns6:name>
                  <ns6:key>MARINE-NS</ns6:key>
                  <ns6:abbreviation>Marine-NS</ns6:abbreviation>
                  <ns6:description>Species which occur in nine to 55 10km X 10km grid squares containing sea (or water of marine saline influence) within the three mile territorial limit.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Birds of Conservation Importance (BOCI)- IUCN Globally threatened species (birds)</ns6:name>
                  <ns6:key>BOCI-GLOBAL</ns6:key>
                  <ns6:abbreviation>BOCI-Global</ns6:abbreviation>
                  <ns6:description>Species requiring monitoring of populations and the preparation of International Species Action Plans to ensure effective conservation.
Qualifying criteria:
1. Decline – extensive (>50%) decline in GB breeding population over previous 25 years and with population of &lt;100,000.
2. Decline – extensive (>50%) decline in GB breeding range over previous 25 years and with population of &lt;100,000.
3. Decline – historical population decline during and since the 19th century, and with a population of &lt;100,000.
Other criteria:
4. Rare breeder: 5-year running mean of 0.8-300 breeding pairs in GB.
5. International: significant proportion (>20%) of European breeding population found in GB.
6. International: significant proportion (>20%) of European non-breeding population found in GB.
7. Localised: >50% of GB breeding population in ten or fewer sites.
8. Localised: >50% of GB non-breeding population in ten or fewer sites.
9. Species of European concern: species of Global Conservation Concern or unfavourable European conservation status.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Birds of Conservation Importance (BOCI)- Uncommon and rapidly or historically declining British bird</ns6:name>
                  <ns6:key>BOCI-DECBRIT</ns6:key>
                  <ns6:abbreviation>BOCI-DecBrit</ns6:abbreviation>
                  <ns6:description>Species requiring monitoring of populations and the preparation of International Species Action Plans to ensure effective conservation.
Qualifying criteria:
1. Decline – extensive (>50%) decline in GB breeding population over previous 25 years and with population of &lt;100,000.
2. Decline – extensive (>50%) decline in GB breeding range over previous 25 years and with population of &lt;100,000.
3. Decline – historical population decline during and since the 19th century, and with a population of &lt;100,000.
Other criteria:
4. Rare breeder: 5-year running mean of 0.8-300 breeding pairs in GB.
5. International: significant proportion (>20%) of European breeding population found in GB.
6. International: significant proportion (>20%) of European non-breeding population found in GB.
7. Localised: >50% of GB breeding population in ten or fewer sites.
8. Localised: >50% of GB non-breeding population in ten or fewer sites.
9. Species of European concern: species of Global Conservation Concern or unfavourable European conservation status.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Birds of Conservation Importance (BOCI)-Rapidly declining but common British breeding birds</ns6:name>
                  <ns6:key>BOCI-DECCOM</ns6:key>
                  <ns6:abbreviation>BOCI-DecCom</ns6:abbreviation>
                  <ns6:description>Species for which the JNCC and country agencies will, in collaboration with NGOs, investigate causes of decline and consider their conservation requirements and, where appropriate, prepare Species Action Plans to ensure effective conservation.Qualifying criterion: Decline: extensive (>50%) decline in GB breeding population or range over previous 25 years, but with a population still greater than 100,000 breeding adults.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>BOCI - Moderately declining, historically declining but common internationally important British spp</ns6:name>
                  <ns6:key>BOCI-MODDEC</ns6:key>
                  <ns6:abbreviation>BOCI-ModDec</ns6:abbreviation>
                  <ns6:description>Species requiring monitoring of populations and, where appropriate, the preparation of Species Action Plans to ensure effective conservation. Qualifying criteria:
2. Decline – moderate (25-49%) decline in GB breeding population or range over previous 25 years.
3. Decline –historical population decline during and since the 19th century, and with a population of >100,000.
4. Rare breeder: 5-year mean of 0.8-300 breeding pairs in GB.
5. International: significant proportion (>20%) of European breeding population found in GB.
6. International: significant proportion (>20%) of European non-breeding population found in GB.
7. Localised: >50% of GB breeding population in ten or fewer sites.
8. Localised: >50% of GB non-breeding population in ten or fewer sites.
9. Species of European concern: species with unfavourable European conservation status.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Nationally rare</ns6:name>
                  <ns6:key>STATUS-NR</ns6:key>
                  <ns6:abbreviation>Status-NR</ns6:abbreviation>
                  <ns6:description>Occurring in 15 or fewer hectads in Great Britain. Excludes rare species qualifying under the main IUCN criteria.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Other rare or scarce taxa which are too poorly understood currently to receive a conservation status</ns6:name>
                  <ns6:key>STATUS-POORLY UNDERSTOOD</ns6:key>
                  <ns6:abbreviation>Status-poorly understood</ns6:abbreviation>
                  <ns6:description>Other rare or scarce taxa which are too poorly understood currently to receive a conservation status.</ns6:description>
               </ns6:Designation>
            </ns6:DesignationList>
         </ns6:DesignationCategory>
         <ns6:DesignationCategory name="Red Data List">
            <ns6:DesignationList>
               <ns6:Designation>
                  <ns6:name>RDB Birds - 1a</ns6:name>
                  <ns6:key>NONIUCN_REDLIST_BIRDS1A</ns6:key>
                  <ns6:abbreviation>NonIUCN_RedList_Birds1a</ns6:abbreviation>
                  <ns6:description>Breeding in internationally significant numbers (>20% of the north-west Europe population).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>RDB Birds - 1b</ns6:name>
                  <ns6:key>NONIUCN_REDLIST_BIRDS1B</ns6:key>
                  <ns6:abbreviation>NonIUCN_RedList_Birds1b</ns6:abbreviation>
                  <ns6:description>Non-breeding in internationally significant numbers (>20% of the north-west Europe population).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>RDB Birds - 2</ns6:name>
                  <ns6:key>NONIUCN_REDLIST_BIRDS2</ns6:key>
                  <ns6:abbreviation>NonIUCN_RedList_Birds2</ns6:abbreviation>
                  <ns6:description>Rare breeder (&lt;300 pairs).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>RDB Birds - 3</ns6:name>
                  <ns6:key>NONIUCN_REDLIST_BIRDS3</ns6:key>
                  <ns6:abbreviation>NonIUCN_RedList_Birds3</ns6:abbreviation>
                  <ns6:description>Declining breeder (>50% sustained decline since 1960).</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Extinct</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-EX</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-EX</ns6:abbreviation>
                  <ns6:description>Taxa which are no longer known to exist in the wild after repeated searches of their localities and other known likely places.Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Endangered</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST2001-EN</ns6:key>
                  <ns6:abbreviation>RedList_Global_post2001-EN</ns6:abbreviation>
                  <ns6:description>A taxon is Endangered when the best available evidence indicates that it meets any of the criteria A to E for Endangered (see Section V), and it is therefore considered to be facing a very high risk of extinction in the wild.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Vulnerable</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST2001-VU</ns6:key>
                  <ns6:abbreviation>RedList_Global_post2001-VU</ns6:abbreviation>
                  <ns6:description>A taxon is Vulnerable when the best available evidence indicates that it meets any of the criteria A to E for Vulnerable (see Section V), and it is therefore considered to be facing a high risk of extinction in the wild.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Critically endangered</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST2001-CR</ns6:key>
                  <ns6:abbreviation>RedList_Global_post2001-CR</ns6:abbreviation>
                  <ns6:description>A taxon is Critically Endangered when it is facing an extremely high risk of extinction in the wild in the immediate future, as defined by any of the criteria A to E.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Bird Population Status- amber</ns6:name>
                  <ns6:key>BIRD-AMBER</ns6:key>
                  <ns6:abbreviation>Bird-Amber</ns6:abbreviation>
                  <ns6:description>Amber list species are those with an unfavourable conservation status in Europe; those whose population or range has declined moderately in recent years; those whose population has declined historically but made a substantial recent recovery; rare breeders; and those with internationally important or localised populations.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Bird Population Status- red</ns6:name>
                  <ns6:key>BIRD-RED</ns6:key>
                  <ns6:abbreviation>Bird-Red</ns6:abbreviation>
                  <ns6:description>Red list species are those that are Globally Threatened according to IUCN criteria; those whose population or range has declined rapidly in recent years; and those that have declined historically and not shown a substantial recent recovery.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Critically endangered</ns6:name>
                  <ns6:key>REDLIST_GB_POST2001-CR</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2001-CR</ns6:abbreviation>
                  <ns6:description>A taxon is Critically Endangered when it is facing an extremely high risk of extinction in the wild in the immediate future, as defined by any of the criteria A to E.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Data Deficient</ns6:name>
                  <ns6:key>REDLIST_GB_POST2001-DD</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2001-DD</ns6:abbreviation>
                  <ns6:description>A taxon is Data Deficient when there is inadequate information to make a direct, or indirect, assessment of its risk of extinction based on its distribution and/or population status. A taxon in this category may be well studied, and its biology well known, but appropriate data on abundance and/or distribution are lacking. Data Deficient is therefore not a category of threat or Lower Risk. Listing of taxa in this category indicates that more information is required and acknowledges the possibility that future research will show that a threatened category is appropriate.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Endangered</ns6:name>
                  <ns6:key>REDLIST_GB_POST2001-EN</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2001-EN</ns6:abbreviation>
                  <ns6:description>A taxon is Endangered when the best available evidence indicates that it meets any of the criteria A to E for Endangered (see Section V), and it is therefore considered to be facing a very high risk of extinction in the wild.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Extinct in the wild</ns6:name>
                  <ns6:key>REDLIST_GB_POST2001-EW</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2001-EW</ns6:abbreviation>
                  <ns6:description>A taxon is Extinct in the wild when it is known to survive only in cultivation, in captivity or as a naturalised population (or populations) well outside the past range. A taxon is presumed extinct in the wild when exhaustive surveys in known and/or expected habitat, at appropriate times (diurnal, seasonal, annual) throughout its range have failed to record an individual. Surveys should be over a time frame appropriate to the taxon's life cycle and life form.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Extinct</ns6:name>
                  <ns6:key>REDLIST_GB_POST2001-EX</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2001-EX</ns6:abbreviation>
                  <ns6:description>Taxa which are no longer known to exist in the wild after repeated searches of their localities and other known likely places. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2001) - Lower risk - near threatened</ns6:name>
                  <ns6:key>REDLIST_GB_POST2001-NT</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2001-NT</ns6:abbreviation>
                  <ns6:description>Taxa which do not qualify for Lower Risk (conservation dependent), but which are close to qualifying for Vulnerable. In Britain, this category includes species which occur in 15 or fewer hectads but do not qualify as Critically Endangered, Endangered or Vulnerable.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (2003) - Regionally Extinct</ns6:name>
                  <ns6:key>REDLIST_GB_POST2003-RE</ns6:key>
                  <ns6:abbreviation>RedList_GB_post2003-RE</ns6:abbreviation>
                  <ns6:description>Category for a taxon when there is no reasonable doubt that the last individual potentially capable of reproduction within the region has died or has disappeared from the wild in the region, or when, if it is a former visiting taxon, the last individual has died or disappeared in the wild from the region. The setting of any time limit for listing under RE is left to the discretion of the regional Red List authority, but should not normally pre-date 1500 AD.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>RDB - Indeterm</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-INDE</ns6:key>
                  <ns6:abbreviation>RedList_GB_pre94-Inde</ns6:abbreviation>
                  <ns6:description>Taxa not seen since 1970 but require further survey before they can be declared extinctknown to be Extinct, Endangered, Vulnerable or Rare, but where there is not enough information to say which of these categories is appropriate. Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>RDB - Insuff known</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-INSU</ns6:key>
                  <ns6:abbreviation>RedList_GB_pre94-Insu</ns6:abbreviation>
                  <ns6:description>Taxa that are suspected but not definitely known to belong to any of the above categories (i.e. Endangered, Vulnerable, Rare), because of the lack of information. Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>RDB - Threatened endemic</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-THRE</ns6:key>
                  <ns6:abbreviation>RedList_GB_pre94-Thre</ns6:abbreviation>
                  <ns6:description>Taxa which are not known to occur naturally outside Britain.  Taxa within this category may also be in any of the other RDB categories or not threatened at all.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Extinct in the wild</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-EW</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-EW</ns6:abbreviation>
                  <ns6:description>A taxon is Extinct in the wild when it is known to survive only in cultivation, in captivity or as a naturalised population (or populations) well outside the past range. A taxon is presumed extinct in the wild when exhaustive surveys in known and/or expected habitat, at appropriate times (diurnal, seasonal, annual) throughout its range have failed to record an individual. Surveys should be over a time frame appropriate to the taxon's life cycle and life form.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Lower risk - near threatened</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST94-NT</ns6:key>
                  <ns6:abbreviation>RedList_Global_post94-NT</ns6:abbreviation>
                  <ns6:description>Taxa which do not qualify for Lower Risk (conservation dependent), but which are close to qualifying for Vulnerable. In Britain, this category includes species which occur in 15 or fewer hectads but do not qualify as Critically Endangered, Endangered or Vulnerable.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Lower risk - near threatened</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-NT</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-NT</ns6:abbreviation>
                  <ns6:description>Taxa which do not qualify for Lower Risk (conservation dependent), but which are close to qualifying for Vulnerable. In Britain, this category includes species which occur in 15 or fewer hectads but do not qualify as Critically Endangered, Endangered or Vulnerable.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Extinct</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-EX</ns6:key>
                  <ns6:abbreviation>RedList_GB_pre94-EX</ns6:abbreviation>
                  <ns6:description>Taxa which are no longer known to exist in the wild after repeated searches of their localities and other known likely places. Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Extinct</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_PRE94-EX</ns6:key>
                  <ns6:abbreviation>RedList_Global_pre94-EX</ns6:abbreviation>
                  <ns6:description>Taxa which are no longer known to exist in the wild after repeated searches of their localities and other known likely places. Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Endangered</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_PRE94-EN</ns6:key>
                  <ns6:abbreviation>RedList_Global_pre94-EN</ns6:abbreviation>
                  <ns6:description>Taxa in danger of extinction and whose survival is unlikely if the causal factors continue operating. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Endangered</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-EN</ns6:key>
                  <ns6:abbreviation>RedList_GB_Pre94-EN</ns6:abbreviation>
                  <ns6:description>Taxa in danger of extinction and whose survival is unlikely if the causal factors continue operating. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Vulnerable</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_PRE94-VU</ns6:key>
                  <ns6:abbreviation>RedList_Global_pre94-VU</ns6:abbreviation>
                  <ns6:description>Taxa believed likely to move into the Endangered category in the near future if the causal factors continue operating. Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Vulnerable</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-VU</ns6:key>
                  <ns6:abbreviation>RedList_GB_pre94-VU</ns6:abbreviation>
                  <ns6:description>Taxa believed likely to move into the Endangered category in the near future if the causal factors continue operating. Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Rare</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_PRE94-NR</ns6:key>
                  <ns6:abbreviation>RedList_Global_pre94-NR</ns6:abbreviation>
                  <ns6:description>Taxa with small populations that are not at present Endangered or Vulnerable, but are at risk. (In GB, this was interpreted as species which exist in fifteen or fewer 10km squares). Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (pre 1994) - Rare</ns6:name>
                  <ns6:key>REDLIST_GB_PRE94-NR</ns6:key>
                  <ns6:abbreviation>RedList_GB_pre94-NR</ns6:abbreviation>
                  <ns6:description>Taxa with small populations that are not at present Endangered or Vulnerable, but are at risk. (In GB, this was interpreted as species which exist in fifteen or fewer 10km squares). Superseded by new IUCN categories in 1994, so no longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Critically endangered</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-CR</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-CR</ns6:abbreviation>
                  <ns6:description>A taxon is Critically Endangered when it is facing an extremely high risk of extinction in the wild in the immediate future, as defined by any of the criteria A to E.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Critically endangered</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST94-CR</ns6:key>
                  <ns6:abbreviation>RedList_Global_post94-CR</ns6:abbreviation>
                  <ns6:description>A taxon is Critically Endangered when it is facing an extremely high risk of extinction in the wild in the immediate future, as defined by any of the criteria A to E.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Data Deficient</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-DD</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-DD</ns6:abbreviation>
                  <ns6:description>A taxon is Data Deficient when there is inadequate information to make a direct, or indirect, assessment of its risk of extinction based on its distribution and/or population status. A taxon in this category may be well studied, and its biology well known, but appropriate data on abundance and/or distribution are lacking. Data Deficient is therefore not a category of threat or Lower Risk. Listing of taxa in this category indicates that more information is required and acknowledges the possibility that future research will show that a threatened category is appropriate.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Endangered</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-EN</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-EN</ns6:abbreviation>
                  <ns6:description>Taxa in danger of extinction and whose survival is unlikely if the causal factors continue operating. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Endangered</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST94-EN</ns6:key>
                  <ns6:abbreviation>RedList_Global_post94-EN</ns6:abbreviation>
                  <ns6:description>Taxa in danger of extinction and whose survival is unlikely if the causal factors continue operating. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Lower risk - conservation dependent</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST94-LR(CD)</ns6:key>
                  <ns6:abbreviation>RedList_Global_post94-LR(cd)</ns6:abbreviation>
                  <ns6:description>Taxa which are the focus of a continuing taxon-specific or habitat-specific conservation programme targeted towards the taxon in question, the cessation of which would result in the taxon qualifying for one of the threatened categories above within a period of five years.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Vulnerable</ns6:name>
                  <ns6:key>REDLIST_GLOBAL_POST94-VU</ns6:key>
                  <ns6:abbreviation>RedList_Global_post94-VU</ns6:abbreviation>
                  <ns6:description>Taxa believed likely to move into the Endangered category in the near future if the causal factors continue operating. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>IUCN (1994) - Vulnerable</ns6:name>
                  <ns6:key>REDLIST_GB_POST94-VU</ns6:key>
                  <ns6:abbreviation>RedList_GB_post94-VU</ns6:abbreviation>
                  <ns6:description>Taxa believed likely to move into the Endangered category in the near future if the causal factors continue operating. Superseded by new IUCN categories in 1994, but still applicable to lists that have not been reviewed since 1994.</ns6:description>
               </ns6:Designation>
            </ns6:DesignationList>
         </ns6:DesignationCategory>
         <ns6:DesignationCategory name="UK BAP">
            <ns6:DesignationList>
               <ns6:Designation>
                  <ns6:name>Biodiversity Lists - Short List</ns6:name>
                  <ns6:key>BAP-SHORT</ns6:key>
                  <ns6:abbreviation>BAP-Short</ns6:abbreviation>
                  <ns6:description>Short list of globally threatened/declining species from the 1995 Steering Group Report, for which action plans were prepared in 1995. A subset of the Middle list. No longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Biodiversity Lists - Middle List</ns6:name>
                  <ns6:key>BAP-MIDDLE</ns6:key>
                  <ns6:abbreviation>BAP-Middle</ns6:abbreviation>
                  <ns6:description>Middle list of globally threatened/declining species from the 1995 Steering Group Report, for which action plans or species statements have been prepared since 1995. A subset of the Long list, and inclusive of all species on Short list. No longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>Biodiversity Lists - Long List</ns6:name>
                  <ns6:key>BAP-LONG</ns6:key>
                  <ns6:abbreviation>BAP-Long</ns6:abbreviation>
                  <ns6:description>Long list of key species from the 1995 Steering Group Report. Inclusive of all species on Middle and Short lists. No longer in use.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>UK BAP- Priority Species</ns6:name>
                  <ns6:key>BAP-1997</ns6:key>
                  <ns6:abbreviation>BAP-1997</ns6:abbreviation>
                  <ns6:description>Species for which Action Plans have been/are being written.</ns6:description>
               </ns6:Designation>
               <ns6:Designation>
                  <ns6:name>UK Biodiversity Action Plan priority species</ns6:name>
                  <ns6:key>BAP-2007</ns6:key>
                  <ns6:abbreviation>BAP-2007</ns6:abbreviation>
                  <ns6:description>The UK List of Priority Species and Habitats contains 1150 species and 65 habitats that have been listed as priorities for conservation action under the UK Biodiversity Action Plan (UK BAP).</ns6:description>
               </ns6:Designation>
            </ns6:DesignationList>
         </ns6:DesignationCategory>
      </ns6:DesignationListResponse>
   </S:Body>
</S:Envelope>
		</nbn:prettyprint-code>
    </jsp:attribute>
</t:actualWebserviceDocumentationPage>