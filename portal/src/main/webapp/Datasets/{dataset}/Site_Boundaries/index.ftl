<#assign datasetId="${URLParameters.dataset}">
<#assign siteBoundaries=json.readURL("${api}/siteBoundaryDatasets/${datasetId}/siteBoundaries")>
<table id="nbn-site-boundaries">
    <thead>
        <tr>
            <th>Site boundary name</th>
            <th>Provider key</th>
        </tr>
    </thead>
    <tbody>
        <#list siteBoundaries as siteBoundary>
            <tr>
                <td><a href="/Sites/#">${siteBoundary.name}</a></td>
                <td>${siteBoundary.providerKey}</td>
            </tr>
        </#list>
    </tbody>
</table>
