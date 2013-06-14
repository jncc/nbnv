<#assign datasetId="${URLParameters.dataset}">
<#assign attributeId="${URLParameters.attribute}">
<#assign dataset=json.readURL("${api}/datasets/${datasetId}")>
<#assign attribute=json.readURL("${api}/taxonDatasets/${datasetId}/attributes/${attributeId}")>
<@template.master title="NBN Gateway - Datasets"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/jquery.validate.min.js","/js/metadata/enable-taxonobs-attribute.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"] >
    <h1>${dataset.title} - Modify Attribute</h1>
    <form id="nbn-dataset-attribute-modify" action="${api}/taxonDatasets/${datasetId}/attributes/${attributeId}">
        <table id="nbn-attributes" class="nbn-simple-table nbn-dataset-table">
            <tr>
                <th>Attribute name</th>
                <th>Description</th>
            </tr>    
            <tr>
                <td>${attribute.label}</td>
                <td><input name="description" value="${attribute.description?has_content?string(attribute.description,"")}" /></td>
            </tr>
        </table>
        <input id="nbn-modify-submit" type="submit" value="Modify Attribute" />
        <div id="nbn-waiting-ticker" style="display:none; float: right;"><p>Warning it may take some time for these changes to propagate to live site <img src="/img/ajax-loader.gif" /></p></div>
    </form>
</@template.master>