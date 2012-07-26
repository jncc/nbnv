<#include "/WEB-INF/templates/master.ftl">

<@master title="NBN Gateway - designation">

    <#assign designation=json.readURL("${api}/designations/${RequestParameters.desig}")>
    <#assign designationCategory=json.readURL("${api}/designationCategories/${designation.designationCategoryID}")>
    <#assign topLevelTaxonGroups=json.readURL("${api}/designations/${RequestParameters.desig}/topLevelTaxonNavigationGroups")>
    <#assign taxa=json.readURL("${api}/designations/${RequestParameters.desig}/taxa")>

    <div id="nbn-designation-content">
${api}/designations/${RequestParameters.desig}/taxa
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
                        <#list topLevelTaxonGroups as taxonGroup>
                            <li>
                                <h1 class="nbn-h1-minor">&nbsp;${taxonGroup.name}</h1>
                                <ul>
                                    <#if taxonGroup.parent>
                                        <#assign childTaxonGroups=json.readURL("${api}/designations/${RequestParameters.desig}/childTaxonNavigationGroups/${taxonGroup.taxonGroupId}")>
                                        <#list childTaxonGroups as childTaxonGroup>
                                            <li><a href="blah/${childTaxonGroup.taxonGroupId}">${childTaxonGroup.name}</a></li>
                                        </#list>
                                    <#else>
                                        <li><a href="blah/${taxonGroup.taxonGroupId}">${taxonGroup.name}</a></li>
                                    </#if>
                                </ul>
                        </li>
                        </#list>
                        </ul>

                    </td>
                </tr>
                <tr>
                    <th>View all species for this designation</th>
                    <td>
                                <h1>All species for designation</h1>
                                <#list taxa as taxon>
                                    <br/>${taxon.taxonName}
                                </#list>
                    </td>
                </tr>
            </table>
            <p>All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>

</@master>
