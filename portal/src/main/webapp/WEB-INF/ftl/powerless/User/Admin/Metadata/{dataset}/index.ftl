<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}")>

<@template.master title="Metadata for ${dataset.title}"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/jquery.dataTables.min.js","/js/admin/metadata.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/admin-controls.css"]>
            <form id="nbn-metadata-update" url="${api}/datasets/${dataset.key}">
                <table class="nbn-dataset-table nbn-simple-table">
                    <tr>
                        <th>Title</th>
                        <td><input type="text" name="title" value="${dataset.title}"></input></td>
                    </tr>
                    <tr>
                        <th>Description</th>
                        <td><textarea name="description">${dataset.description}</textarea></td>
                    </tr>
                    <tr>
                        <th>Purpose of data capture</th>
                        <td><textarea name="purpose">${dataset.purpose}</textarea></td>
                    </tr>
                    <tr>
                        <th>Methods of data capture</th>
                        <td><textarea name="captureMethod">${dataset.captureMethod}</textarea></td>
                    </tr>
                    <tr>
                        <th>Geographical coverage</th>
                        <td><textarea name="geographicalCoverage">${dataset.geographicalCoverage}</textarea></td>
                    </tr>
                    <tr>
                        <th>Temporal coverage</th>
                        <td><textarea name="temporalCoverage">${dataset.temporalCoverage}</textarea></td>
                    </tr>
                    <tr>
                        <th>Data quality</th>
                        <td><textarea name="quality">${dataset.quality}</textarea></td>
                    </tr>
                    <tr>
                        <th>Additional information</th>
                        <td><textarea name="additionalInformation">${dataset.additionalInformation}</textarea></td>
                    </tr>
                    <tr>
                        <th>Access constraints</th>
                        <td><textarea name="accessConstraints">${dataset.accessConstraints}</textarea></td>
                    </tr>
                    <tr>
                        <th>Use constraints</th>
                        <td><textarea name="useConstraints">${dataset.useConstraints}</textarea></td>
                    </tr>
                </table>
                <input type="hidden" name="key" value="${dataset.key}" />
                <button id="nbn-form-submit"> Update </button>
            </form>
</@template.master>