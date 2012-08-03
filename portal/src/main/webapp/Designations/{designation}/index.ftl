<#include "/WEB-INF/templates/master.ftl">


<@master title="NBN Gateway - designation">

    <#assign designationId="${URLParameters.designation}">
    <#assign designation=json.readURL("${api}/designations/${designationId}")>
    <#assign designationCategory=json.readURL("${api}/designationCategories/${designation.designationCategoryID}")>
    <#assign topLevelTaxonGroups=json.readURL("${api}/taxonGroups/topLevels", {"designationId" : designationId })>

    <div id="nbn-designation-content">
        <h4>${designation.name}</h4>

            <table>
                <tr>
                    <th>Abbreviation:</th>
                    <td>${designation.label}</td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td>${designation.description!"Not available"}</td>
                </tr>
                <tr>
                    <th>Parent category:</th>
                    <td>${designationCategory.label}: ${designationCategory.description!""}</td>
                </tr>
                <tr>
                    <th>Interactive species richness map for this designation:</th>
                    <td><a href="http://data.nbn.org.uk/imt/?mode=DESIGNATION&designation=${designation.label}" target="_blank">Go to map</a></td>
                </tr>
                <tr>
                    <th>Groups that have species with this designation:</th>
                    <td>
                        <ul class="collapsible-list">
                            <#list topLevelTaxonGroups as topLevelTaxonGroup>
                                <li>
                                    <h1 class="nbn-h1-minor">&nbsp;${topLevelTaxonGroup.taxonGroupName} (${topLevelTaxonGroup.numSpecies} species)</h1>
                                    <ul>
                                        <#assign taxonGroupWithChildren=json.readURL("${api}/designations/${designationId}/taxonGroups/${topLevelTaxonGroup.taxonGroupKey}")>
                                        <#if taxonGroupWithChildren.children?has_content>
                                            <#list taxonGroupWithChildren.children as childTaxonGroup>
                                                <@childTaxonGroupListItem designationId=designationId taxonGroup=childTaxonGroup/>
                                            </#list>
                                        <#else>
                                                <@childTaxonGroupListItem designationId=designationId taxonGroup=topLevelTaxonGroup/>
                                        </#if>
                                    </ul>
                                </li>
                            </#list>
                        </ul>
                    </td>
                </tr>
            </table>
            <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>

</@master>

<#macro childTaxonGroupListItem designationId taxonGroup>
    <li class="nbn-designation-nested-list"><a href="/Designations/${designationId}/Species_Group/${taxonGroup.taxonGroupKey}">${taxonGroup.taxonGroupName}</a> (${taxonGroup.numSpecies} species)</li>
</#macro>