<#assign datasetId="${URLParameters.dataset}">
<#assign attributes=json.readURL("${api}/taxonDatasets/${datasetId}/attributes")>
<#assign isAdmin=json.readURL("${api}/datasets/${datasetId}/isAdmin")>
<table id="nbn-attributes" class="nbn-simple-table nbn-dataset-table">
    <tr>
        <th>Attribute name</th>
        <th>Description</th>
        <#if isAdmin>
        <th></th>
        </#if>
    </tr>
    <#list attributes as attribute>
        <tr>
            <td>${attribute.label}</td>
            <td>${attribute.description?has_content?string(attribute.description,"Description not available")}</td>
            <#if isAdmin>
            <td style="width:100px; text-alight:right;"><a href="/Datasets/${datasetId}/Attributes/${attribute.attributeID}">Modify Attribute</a></td>
            </#if>
        </tr>
        
    </#list>
</table>
