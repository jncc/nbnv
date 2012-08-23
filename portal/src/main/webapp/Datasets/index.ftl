<@template.master title="NBN Gateway - Datasets" 
    javascripts=["http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.3/jquery.dataTables.min.js","/js/enable-datatable.js","/js/jqueryui.simple-table-style.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] >

    <#assign datasets=json.readURL("${api}/datasets")>
    <div>
        <table id="nbn-datatable" class="nbn-simple-table">
            <thead>
                <tr>
                    <th>Dataset</th>
                    <th>Provider</th>
                    <th>Date uploaded</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                <#list datasets as dataset>
                    <tr>
                        <td><a href="${dataset.datasetKey}">${dataset.name?html}</a></td>
                        <td>${dataset.organisationName?html}</td>
                        <td>${dataset.formattedDateUploaded?html}</td>
                        <td>${dataset.typeName?html}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>

</@template.master>
