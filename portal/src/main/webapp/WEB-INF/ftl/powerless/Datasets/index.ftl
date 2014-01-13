<@template.master title="NBN Gateway - Datasets" 
    javascripts=["/js/jquery.dataTables.min.js","/js/enable-datasets-datatable.js","/js/jquery.datatables.customDate.js"] 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] >

    <#assign datasets=json.readURL("${api}/datasets")>
    <div>
        <h1> Datasets</h1>
        <table id="nbn-datasets-datatable" class="nbn-dataset-table">
            <thead>
                <tr>
                    <th>Dataset key</th>
                    <th>Dataset</th>
                    <th>Provider</th>
                    <th>Uploaded</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                <#list datasets as dataset>
                    <tr>
                        <td>${dataset.key}</td>
                        <td><a href="/Datasets/${dataset.key}">${dataset.title?html}</a></td>
                        <td><a href="/Organisations/${dataset.organisationID}">${dataset.organisationName?html}</a></td>
                        <td>${dataset.formattedDateUploaded?html}</td>
                        <td>${dataset.typeName?html}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>

</@template.master>
