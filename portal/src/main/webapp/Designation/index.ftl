<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - designation">

    <#assign designation=json.readURL("${api}/designations/${RequestParameters.desig}")>
    <#assign designationCategory=json.readURL("${api}/designationCategories/${designation.designationCategoryID}")>
    <#assign topLevelTaxonGroups=json.readURL("${api}/designations/${RequestParameters.desig}/topLevelTaxonNavigationGroups")>

    <div id="nbn-designation-content">
        <h4>${designation.name}</h4>

            <table>
                <tr>
                    <th>Abbreviation:</th>
                    <td>${designation.label}</td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td>${designation.description}</td>
                </tr>
                <tr>
                    <th>Parent category:</th>
                    <td>${designationCategory.label}: ${designationCategory.description}</td>
                </tr>
                <tr>
                    <th>Interactive map of species with this designation:</th>
                    <td><a href="http://data.nbn.org.uk/imt/?mode=DESIGNATION&designation=${designation.label}" target="_blank">Go to map</a></td>
                </tr>
                <tr>
                    <th>Groups that have species with this designation:</td>
                    <td>
                        <ul class="collapsible-list">
                        <#list topLevelTaxonGroups as topLevelTaxonGroup>
                            <li>
                                <h1 class="nbn-h1-minor">&nbsp;${topLevelTaxonGroup.taxonGroupName}</h1>
                                <ul>
                                    <#assign taxonGroupWithChildren=json.readURL("${api}/designations/${RequestParameters.desig}/taxonGroup/${topLevelTaxonGroup.taxonGroupKey}")>
                                    <#if taxonGroupWithChildren.children?has_content>
                                        <#list taxonGroupWithChildren.children as childTaxonGroup>
                                            <li><a href="${api}/designations/${RequestParameters.desig}/taxonGroup/${childTaxonGroup.taxonGroupKey}/species">${childTaxonGroup.taxonGroupName}</a></li>
                                        </#list>
                                    <#else>
                                            <li><a href="${api}/designations/${RequestParameters.desig}/taxonGroup/${topLevelTaxonGroup.taxonGroupKey}/species">${topLevelTaxonGroup.taxonGroupName}</a></li>
                                    </#if>
                                </ul>
                        </li>
                        </#list>
                        </ul>

                    </td>
                </tr>
            </table>
            <p>All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>

</@master>
