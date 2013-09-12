<#assign oListCode="${URLParameters.code}">
<#assign oList=json.readURL("${api}/organisationList/code/${oListCode}")>
<#assign oListId=oList.id>
<#assign taxonNavigationGroupId="${URLParameters.taxon_navigation_group}">
<#assign taxonNavigationGroup=json.readURL("${api}/taxonNavigationGroups/${taxonNavigationGroupId}")>
<#assign species=json.readURL("${api}/organisationList/${oListId}/species", {"taxonNavigationGroupId": taxonNavigationGroupId})>

<@template.master title="NBN Gateway - ${oList.name}"> 
    <div id="nbn-designation-content">
        <h4>${oList.name} : ${taxonNavigationGroup.name}</h4>
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
                <tr>
                    <th>Species list for: ${taxonNavigationGroup.name}</th>
                    <td>
                        <#list species as spec>
                            <a href="/Taxa/${spec.ptaxonVersionKey}">${taxon_utils.getShortName(spec)}</a><br/>
                        </#list>
                    </td>
                </tr>
            </table>
            <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>

</@template.master>
