<#assign datasetId="${URLParameters.dataset}">
<#assign attributes=json.readURL("${api}/taxonDatasets/${datasetId}/attributes")>
<table id="nbn-attributes" class="nbn-simple-table nbn-dataset-table">
    <tr>
        <th>Attribute name</th>
        <th>Description</th>
    </tr>
    <#list attributes as attribute>
        <tr>
            <td>${attribute.label}</td>
            <td>${attribute.description?has_content?string(attribute.description,"Description not available")}</td>
        </tr>
    </#list>
</table>
