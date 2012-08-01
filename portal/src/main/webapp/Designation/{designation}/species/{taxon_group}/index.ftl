<#include "/WEB-INF/templates/master.ftl">


<@master title="NBN Gateway - designation">

    <#assign designationId="${URLParameters.designation}">
    <#assign taxonGroupId="${URLParameters.taxon_group}">
    <#assign designation=json.readURL("${api}/designations/${designationId}")>
    <#assign designationCategory=json.readURL("${api}/designation_categories/${designation.designationCategoryID}")>
    <#assign species=json.readURL("${api}/species/taxon_group/${taxonGroupId}/designation/${designationId}")>

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
                    <th>Interactive map of species with this designation:</th>
                    <td><a href="http://data.nbn.org.uk/imt/?mode=DESIGNATION&designation=${designation.label}" target="_blank">Go to map</a></td>
                </tr>
                <tr>
                    <th></th>
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
