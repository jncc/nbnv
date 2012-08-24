<#assign datasetId="${URLParameters.dataset}">
<#assign siteBoundaries=json.readURL("${api}/siteBoundaryDatasets/${datasetId}/siteBoundaries")>

<table id="nbn-site-boundary-table">
    <#list siteBoundaries as siteBoundary>
        <tr>
            <td>
                <a href="/Sites/#">${siteBoundary.name}</a>
            </td>
        </tr>
    </#list>
</table>
