<@template.master title="NBN Gateway - designation">

    <#assign designationId="${URLParameters.designation}">
    <#assign taxonNavigationGroupId="${URLParameters.taxon_navigation_group}">

    <#assign designation=json.readURL("${api}/designations/${designationId}")>
    <#assign designationCategory=json.readURL("${api}/designationCategories/${designation.designationCategoryID}")>
    <#assign taxonNavigationGroup=json.readURL("${api}/taxonNavigationGroups/${taxonNavigationGroupId}")>
    <#assign species=json.readURL("${api}/designations/${designationId}/species", {"taxonNavigationGroupId": taxonNavigationGroupId})>

    <div id="nbn-designation-content">
        <h4>${designation.name} : ${taxonNavigationGroup.taxonGroupName}</h4>
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
                    <th>Species list for: ${taxonNavigationGroup.taxonGroupName}</th>
                    <td>
                        <#list species as spec>
                            <@taxon_utils.long_name taxon=spec/><br/>
                        </#list>
                    </td>
                </tr>
            </table>
            <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>

</@template.master>
