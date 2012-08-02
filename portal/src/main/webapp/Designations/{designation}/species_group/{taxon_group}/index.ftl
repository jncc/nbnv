<#include "/WEB-INF/templates/master.ftl">


<@master title="NBN Gateway - designation">

    <#assign designationId="${URLParameters.designation}">
    <#assign taxonGroupId="${URLParameters.taxon_group}">

    <#assign designation=json.readURL("${api}/designations/${designationId}")>
    <#assign designationCategory=json.readURL("${api}/designationCategories/${designation.designationCategoryID}")>
    <#assign taxonGroup=json.readURL("${api}/taxonGroups/${taxonGroupId}")>
    <#assign species=json.readURL("${api}/designations/${designationId}/species", {"taxonGroupId": taxonGroupId})>

    <div id="nbn-designation-content">
        <h4>${designation.name} : ${taxonGroup.taxonGroupName}</h4>
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
                    <th>Species list for: ${taxonGroup.taxonGroupName}</th>
                    <td>
                        <#list species as spec>
                            ${spec.taxonName} <br/>
                        </#list>
                    </td>
                </tr>
            </table>
            <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>

</@master>
