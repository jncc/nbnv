<@template.master title="NBN Gateway - Datasets" 
    javascripts=["/js/jquery.dataTables.min.js","/js/enable-datasets-datatable.js","/js/jqueryui.simple-table-style.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] >

    <#assign datasets=json.readURL("${api}/datasets")>
    <div>
        <h1> Datasets</h1>
        <table id="nbn-datasets-datatable" class="nbn-dataset-table">
            <thead>
                <tr>
                    <th>Dataset</th>
                    <th>Dataset key</th>
                    <th>Provider</th>
                    <th>Uploaded</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                <#list datasets as dataset>
                    <tr>
                        <td><a href="${dataset.datasetKey}">${dataset.name?html}</a></td>
                        <td>${dataset.datasetKey}</td>
                        <td>${dataset.organisationName?html}</td>
                        <td>${dataset.formattedDateUploaded?html}</td>
                        <td>${dataset.typeName?html}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>

</@template.master>
