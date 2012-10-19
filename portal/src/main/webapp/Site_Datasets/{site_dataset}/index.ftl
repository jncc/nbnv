<@template.master title="NBN Gateway - Site Boundaries"
    javascripts=["/js/enable-site-boundary-datatable.js","/js/jquery.dataTables.min.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] >

<#assign datasetId="${URLParameters.site_dataset}">
<#assign siteBoundaries=json.readURL("${api}/siteBoundaryDatasets/${datasetId}/siteBoundaries")>
<table id="nbn-site-boundary-datatable" class="nbn-dataset-table">
    <thead>
        <tr>
            <th>Site boundary name</th>
            <th>Site key</th>
        </tr>
    </thead>
    <tbody>
        <#list siteBoundaries as siteBoundary>
            <tr>
                <td><a href="/Reports/Sites/${siteBoundary.featureID}/Groups">${siteBoundary.name}</a></td>
                <td>${siteBoundary.providerKey}</td>
            </tr>
        </#list>
    </tbody>
</table>

</@template.master>
