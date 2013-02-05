<#assign tvk=URLParameters.tvk>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>
<#assign ptaxon=json.readURL("${api}/taxa/${taxon.ptaxonVersionKey}")>
<#assign synonyms=json.readURL("${api}/taxa/${tvk}/synonyms")>
<#assign designations=json.readURL("${api}/taxa/${tvk}/designations")>
<#assign parent=json.readURL("${api}/taxa/${tvk}/taxonomy")>
<#assign children=json.readURL("${api}/taxa/${tvk}/children")>
<#assign datasets=json.readURL("${api}/taxa/${tvk}/datasets")>
<#assign weblinks=json.readURL("${api}/taxa/${tvk}/weblinks")>
<#assign output=json.readURL("${api}/taxonOutputGroups/${taxon.taxonOutputGroupKey}")>

<@template.master title="NBN Gateway - Taxon"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/jquery.dataTables.min.js","/js/taxon-page-utils.js","/js/report_utils.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/taxon-page.css"]>
    <h1>${taxon.name} <#if taxon.authority??>${taxon.authority}</#if></h1>
    <div>
        <#if taxon.taxonVersionKey == ptaxon.taxonVersionKey>
            <@taxonPageTaxonomy parent=parent taxon=taxon children=children/>
        </#if>
        <@taxonPageTaxonData taxon=taxon outputGroup=output/>
        <#if taxon.taxonVersionKey == ptaxon.taxonVersionKey>
            <@taxonPageSynonyms syn=synonyms/>
            <@taxonPageDesignations des=designations/>
            <@taxonPageLinks links=weblinks/>
        </#if>
        <@taxonPageNBNLinks taxon=ptaxon/>
        <#if taxon.taxonVersionKey == ptaxon.taxonVersionKey>
            <@gridMapContents taxon=ptaxon/>
        </#if>
    </div>
    <#if taxon.taxonVersionKey == ptaxon.taxonVersionKey>
        <@taxonPageDatasets datasets=datasets/>
    </#if>
    <div align="center">
        <a href="http://www.nhm.ac.uk/" target="_blank"><img src="/img/taxonPage/nhm_new.gif" alt="Natural History Museum logo" width="135" height="85" border="0" /></a>
        <a href="http://www.jncc.gov.uk/page-5546" target="_blank"><img src="/img/taxonPage/jncc.gif" alt="Joint Nature Conservation Committee logo" width="150" height="66" border="0" /></a>
    </div>
</@template.master>

<#macro gridMapContents taxon>
    <div class="tabbed" id="nbn-grid-map-container" gis-server="${gis}" tvk="${taxon.taxonVersionKey}">
        <h3>Map</h3>
        <img id="nbn-grid-map-image" class="nbn-centre-element" alt="Distribution of ${taxon.name} in the UK according to records accessible through the NBN Gateway" />
    </div>
</#macro>

<#macro taxonPageNBNLinks taxon>
    <div class="tabbed nbn-taxon-page-right-container">
        <h3>Explore Records</h3>
        <div class="nbn-taxon-page-list"><a href="/Taxa/${taxon.taxonVersionKey}/Grid_Map"><img src="/img/taxonPage/grid.png" class="nbn-taxon-page-link-img" />Grid Map</a></div>
        <div class="nbn-taxon-page-list"><a href="/imt/?mode=SPECIES&species=${taxon.taxonVersionKey}"><img src="/img/taxonPage/imt.png" class="nbn-taxon-page-link-img" />Interactive Map</a></div>
        <div class="nbn-taxon-page-list"><a href="/Taxa/${taxon.taxonVersionKey}/Site_Boundaries"><img src="/img/taxonPage/site.png" class="nbn-taxon-page-link-img" />List of sites</a></div>
    </div>
</#macro>

<#macro taxonPageTaxonData taxon outputGroup>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Taxon</h3>
        <table class="nbn-dataset-table nbn-simple-table">
            <tr><th>Name:</th><td>${taxon.name}</td></tr>
            <tr><th>Authority:</th><td><#if taxon.authority??>${taxon.authority}</#if></td></tr>
            <#if taxon.commonNameTaxonVersionKey??><tr><th>Common Name:</th><td>${taxon.commonName}</td></tr></#if>
            <tr><th>Taxon Version Key:</th><td>${taxon.taxonVersionKey}</td></tr>
            <#if taxon.ptaxonVersionKey != taxon.taxonVersionKey>
            <tr><th>Preferred Name:</th><td><a href="${ptaxon.taxonVersionKey}">${ptaxon.name}</a></td></tr>
            <tr><th>Preferred Name Authority:</th><td><#if ptaxon.authority??>${ptaxon.authority}</#if></td></tr>
            <tr><th>Preferred Taxon Version Key:</th><td>${ptaxon.taxonVersionKey}</td></tr>
            </#if>
            <tr><th>Rank:</th><td>${taxon.rank}</td></tr>
            <tr><th>Name Form:</th><td>${taxon.versionForm}</td></tr>
            <#if !json.isNull(outputGroup)><tr><th>Output Group:</th><td>${outputGroup.name}</td></tr></#if>
        </table>
    </div>
</#macro>

<#macro taxonPageSynonyms syn>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Synonyms</h3>
        <#if syn?has_content>
            <h4>Well formed name(s)</h4>
            <#assign w = 0 />
            <table class="nbn-dataset-table nbn-simple-table">
            <#list syn as s>
                <#if s.versionForm == 'Well-formed'>
                <#assign w = 1 />
                <tr>
                    <td><a href="${s.taxonVersionKey}">${s.name}</a> <#if s.authority??>${s.authority}</#if></td>
                </tr>
                </#if>
            </#list>
            <#if w == 0>
                <td>None</td>
            </#if>
            </table>
            <h4>Badly formed / unverified name(s)</h4>
            <#assign w = 0 />
            <table class="nbn-dataset-table nbn-simple-table">
            <#list syn as s>
                <#if s.versionForm != 'Well-formed'>
                <#assign w = 1 />
                <tr>
                    <td><a href="${s.taxonVersionKey}">${s.name}</a> <#if s.authority??>${s.authority}</#if></td>
                </tr>
                </#if>
            </#list>
            <#if w == 0>
                <td>None</td>
            </#if>
            </table>
        <#else>
            <div>None</div>
        </#if>
    </div>
</#macro>

<#macro taxonPageTaxonomy parent taxon children>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Taxonomy</h3>
        <table class="nbn-dataset-table nbn-simple-table">
            <#assign indent=0/>
            <#if parent?has_content>
                <#list parent as p>
                    <tr><th>${p.rank}</th><td><span style="padding-left: ${indent / 2}em;"><a href="${p.taxonVersionKey}">${p.name}</a> <#if p.authority??>${p.authority}</#if></span></td></tr>
                    <#assign indent = indent + 1 />
                </#list>
            </#if>
        
            <tr><th>${taxon.rank}</th><td><span style="padding-left: ${(indent + 1) / 2}em;">${taxon.name} <#if taxon.authority??>${taxon.authority}</#if></span></td></tr>

            <#if children?has_content>
                <#list children as c>
                    <tr><th>${c.rank}</th><td><span style="padding-left: ${(indent + 2) / 2}em;"><a href="${c.taxonVersionKey}">${c.name}</a> <#if c.authority??>${c.authority}</#if></span></tr>
                </#list>
            </#if>
        </table>
    </div>
</#macro>

<#macro taxonPageDesignations des>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Designations</h3>
        <#if des?has_content>
            <table class="nbn-dataset-table nbn-simple-table">
                <#list des as d>
                    <tr>
                        <td width="25%"><a href="/Designations/${d.designation.code}">${d.designation.name}</a></td>
                        <td style="width: 110px;">
                            <#if d.startDate??>From: ${d.startDate}</#if>
                            <#if d.startDate?? && d.endDate??><br/></#if>
                            <#if d.endDate??>Until: ${d.endDate}</#if>
                        </td>
                        <td><#if d.source??>${d.source}</#if></td>
                    </tr>
                </#list>
            </table>
        <#else>
            <div>None</div>
        </#if>
    </div>
</#macro>

<#macro taxonPageLinks links>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>External Links</h3>
        <#if links?has_content>
            <table>
                <#list links as link>
                    <tr>
                        <td><a href="<#if !link.link?starts_with("http")>http://</#if>${link.link}" target="_blank">${link.description}</a></td>
                    </tr>
                </#list>
            </table>
        <#else>
            None
        </#if>
    </div>
</#macro>

<#macro taxonPageDatasets datasets>
    <div class="tabbed nbn-taxon-page-dataset-container">
        <h3>Datasets</h3>
        <#if datasets?has_content>
            <table class="nbn-dataset-table nbn-simple-table">
                <#list datasets as d>
                    <tr>
                        <td><a href="/Datasets/${d.key}">${d.title}</a></td>
                        <td><a href="/Organisations/${d.organisationID}">${d.organisationName}</a></td>
                        <td>${d.formattedDateUploaded}</td>
                    </tr>
                </#list>
            </table>
        <#else>
            None
        </#if>
    </div>
</#macro>