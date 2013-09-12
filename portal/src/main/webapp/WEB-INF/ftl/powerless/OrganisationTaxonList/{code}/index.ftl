<#assign oListCode="${URLParameters.code}">
<#assign oList=json.readURL("${api}/organisationList/code/${oListCode}")>
<#assign oListId=oList.id>
<#assign topLevelTaxonNavigationGroups=json.readURL("${api}/taxonNavigationGroups/topLevels", {"organisationListId" : "${oListId}" })>

<@template.master title="NBN Gateway - ${oList.name}">
    <div id="nbn-designation-content">
        <h4>${oList.name}</h4>
            <table>
                <tr>
                    <th>Provided By:</th>
                    <td>${oList.organisationName}</td>
                </tr>
                <tr>
                    <th>Abbreviation:</th>
                    <td>${oList.code}</td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td>${oList.description!"Not available"}</td>
                </tr>
                <#-- Removing until we can discuss adding it back in when the new IMT supports it
                <tr>
                    <th>Interactive species richness map for this organisation supplied list:</th>
                    <td><a href="//data.nbn.org.uk/imt/?mode=DESIGNATION&designation=${designation.label}" target="_blank">Go to map</a></td>
                </tr>
                -->
                <tr>
                    <th>Groups that have species with this organisation supplied list:</th>
                    <td>
                        <ul class="collapsible-list">
                            <#list topLevelTaxonNavigationGroups as topLevelTaxonNavigationGroup>
                                <li>
                                    <h1 class="nbn-h1-minor">&nbsp;${topLevelTaxonNavigationGroup.name} (${topLevelTaxonNavigationGroup.numSpecies} species)</h1>
                                    <ul>
                                        <#assign taxonNavigationGroupWithChildren=json.readURL("${api}/organisationList/${oListId}/taxonNavigationGroups/${topLevelTaxonNavigationGroup.key}")>
                                        <#if taxonNavigationGroupWithChildren.children?has_content>
                                            <#list taxonNavigationGroupWithChildren.children as childTaxonGroup>
                                                <@childTaxonNavigationGroupListItem organistionListId=oListId taxonNavigationGroup=childTaxonGroup/>
                                            </#list>
                                        <#else>
                                                <@childTaxonNavigationGroupListItem organistionListId=oListId taxonNavigationGroup=topLevelTaxonNavigationGroup/>
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

</@template.master>

<#macro childTaxonNavigationGroupListItem organistionListId taxonNavigationGroup>
    <li class="nbn-designation-nested-list"><a href="/OrganisationTaxonList/${oListCode}/Species_Group/${taxonNavigationGroup.key}">${taxonNavigationGroup.name}</a> (${taxonNavigationGroup.numSpecies} species)</li>
</#macro>