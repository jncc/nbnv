<#assign tvk=URLParameters.tvk>
<#assign taxon=json.readURL("${api}/taxa/${tvk}")>
<#assign ptaxon=json.readURL("${api}/taxa/${taxon.ptaxonVersionKey}")>
<#assign synonyms=json.readURL("${api}/taxa/${tvk}/synonyms")>
<#assign designations=json.readURL("${api}/taxa/${tvk}/designations")>
<#assign parent=json.readURL("${api}/taxa/${tvk}/taxonomy")>
<#assign children=json.readURL("${api}/taxa/${tvk}/children")>
<#assign datasets=json.readURL("${api}/taxa/${tvk}/datasets")>
<#assign output=json.readURL("${api}/taxonOutputGroups/${taxon.taxonOutputGroupKey}")>
<#assign ptvk=taxon.ptaxonVersionKey>

<@template.master title="NBN Gateway - Taxon"
    javascripts=["/js/taxon-page-utils.js","/js/report_utils.js"]
    csss=["/css/taxon-page.css"]>
    <h1>${taxon.name} <#if taxon.authority??>${taxon.authority}</#if></h1>
    <div>
        <@taxonPageTaxonData taxon=taxon outputGroup=output/>
        <@taxonPageSynonyms syn=synonyms/>
        <@taxonPageTaxonomy parent=parent taxon=taxon children=children/>
        <@taxonPageDesignations des=designations/>
        <@taxonPageLinks links=taxon/>
        <@taxonPageNBNLinks taxon=taxon/>
        <@gridMapContents key=ptvk/>
        <@arkive taxon=ptaxon/>
    </div>
    <@taxonPageDatasets datasets=datasets/>
</@template.master>

<#macro gridMapContents key>
    <div class="tabbed" id="nbn-grid-map-container" gis-server="http://staging.testnbn.net/gis" tvk="${key}">
        <h3>Map</h3>
        <img id="nbn-grid-map-busy-image" src='/img/ajax-loader-medium.gif'>
        <img id="nbn-grid-map-image" class="nbn-centre-element">
    </div>
</#macro>

<#macro arkive taxon>
    <div class="tabbed nbn-taxon-page-right-container">
        <h3>Arkive Image</h3>
        <div>Not working yet!</div>
        <div id="nbn-taxon-page-arkive" sciName="${taxon.name}"></div>
    </div>
</#macro>

<#macro taxonPageNBNLinks taxon>
    <div class="tabbed nbn-taxon-page-right-container">
        <h3>Explore Records</h3>
        <div class="nbn-taxon-page-list"><a href="/Reports/Single_Species/${taxon.taxonVersionKey}/Grid_Map">Grid Map</a></div>
        <div class="nbn-taxon-page-list"><a href="/imt/?mode=SPECIES&species=${taxon.taxonVersionKey}">Interactive Map</a></div>
        <div class="nbn-taxon-page-list">List of sites</div>
    </div>
</#macro>

<#macro taxonPageTaxonData taxon outputGroup>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Taxon</h3>
        <table>
            <tr><td>Name:</td><td>${taxon.name}</td></tr>
            <tr><td>Authority:</td><td><#if taxon.authority??>${taxon.authority}</#if></td></tr>
            <#if taxon.commonNameTaxonVersionKey??><tr><td>Common Name:</td><td>${taxon.commonName}</td></tr></#if>
            <tr><td>Taxon Version Key:</td><td>${taxon.taxonVersionKey}</td></tr>
            <#if taxon.ptaxonVersionKey != taxon.taxonVersionKey>
            <tr><td>Preferred Name:</td><td><a href="${ptaxon.taxonVersionKey}">${ptaxon.name}</a></td></tr>
            <tr><td>Preferred Name Authority:</td><td><#if ptaxon.authority??>${ptaxon.authority}</#if></td></tr>
            <tr><td>Preferred Taxon Version Key:</td><td>${ptaxon.taxonVersionKey}</td></tr>
            </#if>
            <tr><td>Rank:</td><td>${taxon.rank}</td></tr>
            <tr><td>Name Status:</td><td>${taxon.nameStatus}</td></tr>
            <tr><td>Name Form:</td><td>${taxon.versionForm}</td></tr>
            <#if !json.isNull(outputGroup)><tr><td>Output Group:</td><td>${outputGroup.name}</td></tr></#if>
        </table>
    </div>
</#macro>

<#macro taxonPageSynonyms syn>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Synonyms</h3>
        <#if syn?has_content>
            <table>
            <#list syn as s>
                <tr>
                    <td><a href="${s.taxonVersionKey}">${s.name}</a></td>
                    <td><#if s.authority??>${s.authority}</#if></td>
                    <td>${s.taxonVersionKey}</td>
                </tr>
            </#list>
            </table>
        <#else>
            <div>None</div>
        </#if>
    </div>
</#macro>

<#macro taxonPageTaxonomy parent taxon children>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Taxonomy</h3>
        <table>
            <#assign indent=0/>
            <#if parent?has_content>
                <#list parent as p>
                    <tr><td><span style="padding-left: ${indent / 2}em;"><a href="${p.taxonVersionKey}">${p.name}</a></span></td><td><#if p.authority??>${p.authority}</#if></td><td>${p.rank}</td></tr>
                    <#assign indent = indent + 1 />
                </#list>
            </#if>
        
            <tr><td><span style="padding-left: ${(indent + 1) / 2}em;">${taxon.name}</span></td><td><#if taxon.authority??>${taxon.authority}</#if></td><td>${taxon.rank}</td></tr>

            <#if children?has_content>
                <#list children as c>
                    <tr><td><span style="padding-left: ${(indent + 2) / 2}em;"><a href="${c.taxonVersionKey}">${c.name}</a></span></td><td><#if c.authority??>${c.authority}</#if></td><td>${c.rank}</td></tr>
                </#list>
            </#if>
        </table>
    </div>
</#macro>

<#macro taxonPageDesignations des>
    <div class="tabbed nbn-taxon-page-taxonomy-container">
        <h3>Designations</h3>
        <#if des?has_content>
            <table>
                <#list des as d>
                    <tr>
                        <td><a href="/Designations/${d.designation.code}">${d.designation.name}</a></td>
                        <td>${d.designation.code}</td>
                        <td>TODO: start/end date</td>
                        <td>TODO: source</td>
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
        TODO: External links go here
    </div>
</#macro>

<#macro taxonPageDatasets datasets>
    <div class="tabbed nbn-taxon-page-dataset-container">
        <h3>Datasets</h3>
        <#if datasets?has_content>
            <table>
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