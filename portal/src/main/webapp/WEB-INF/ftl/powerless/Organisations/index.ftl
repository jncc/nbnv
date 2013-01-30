<@template.master title="NBN Gateway - Organisations"
    javascripts=["/js/jquery.dataTables.min.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"] >

    
    <#assign organisations=json.readURL("${api}/organisations")>

    <h1>Organisations</h1>
    <div>
        <table id="nbn-org-datatable" class="nbn-dataset-table">
            <thead>
                <tr>
                    <th>Organisation</th>
                    <th>Web Page</th>
                </tr>
            </thead>
            <tbody>
                <#list organisations as org>
                    <tr>
                        <td><a href="/Organisations/${org.id}">${org.name}</a></td>
                        <td><#if org.website?has_content><a href="http://${org.website}">${org.website}</a></#if></td>
                    </tr>
                </#list>
           </tbody>
       </table>
    </div>
</@template.master>
