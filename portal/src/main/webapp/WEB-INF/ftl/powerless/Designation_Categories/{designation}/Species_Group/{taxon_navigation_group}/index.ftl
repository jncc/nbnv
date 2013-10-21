<@template.master title="NBN Gateway - designation"
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]
    javascripts=["/js/download/designations/designationDownload.js"]> 

    <#assign designationId="${URLParameters.designation}">
    <#assign taxonNavigationGroupId="${URLParameters.taxon_navigation_group}">

    <#assign designation=json.readURL("${api}/designations/${designationId}")>
    <#assign designationCategory=json.readURL("${api}/designationCategories/${designation.designationCategoryID}")>
    <#assign taxonNavigationGroup=json.readURL("${api}/taxonNavigationGroups/${taxonNavigationGroupId}")>
    <#assign species=json.readURL("${api}/designations/${designationId}/species", {"taxonNavigationGroupId": taxonNavigationGroupId})>

    <div id="nbn-designation-content">
        <h4>${designation.name} : ${taxonNavigationGroup.name}</h4>
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
                    <th>Species list for: ${taxonNavigationGroup.name}</th>
                    <td>
                        <#list species as spec>
                            <a href="/Taxa/${spec.ptaxonVersionKey}">${taxon_utils.getShortName(spec)}</a><br/>
                        </#list>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td><a id="nbn-designation-download-link" href="#" data-code="${designationId}">Download Records for ${designation.label}</a></td>
                </tr>
            </table>
            <p class="nbn-designation-footer">All designation information on the NBN Gateway is collated and supplied by the <a href="http://jncc.defra.gov.uk/page-5546">Joint Nature Conservation Committee (JNCC)</a></p>
        </div>
        <div style="display:none;">
            <@report_utils.downloadTermsDialogue/>
        </div>

</@template.master>
