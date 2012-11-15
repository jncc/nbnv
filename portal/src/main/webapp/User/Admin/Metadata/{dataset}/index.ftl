<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}")>

<@template.master title="Metadata for ${dataset.title}">
                <table class="nbn-dataset-table nbn-simple-table">
                    <tr>
                        <th>Title</th>
                        <td>${dataset.title}</td>
                    </tr>
                    <tr>
                        <th>Permanent key</th>
                        <td>${dataset.key}</td>
                    </tr>
                    <tr>
                        <th>Description</th>
                        <td>${dataset.description!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Date uploaded</th>
                        <td>${dataset.formattedDateUploaded}</td>
                    </tr>
                    <tr>
                        <th>Purpose of data capture</th>
                        <td>${dataset.purpose!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Methods of data capture</th>
                        <td>${dataset.captureMethod!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Geographical coverage</th>
                        <td>${dataset.geographicalCoverage!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Temporal coverage</th>
                        <td>${dataset.temporalCoverage!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Data quality</th>
                        <td>${dataset.quality!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Additional information</th>
                        <td>${dataset.additionalInformation!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Access constraints</th>
                        <td>${dataset.accessConstraints!"Not available"}</td>
                    </tr>
                    <tr>
                        <th>Use constraints</th>
                        <td>${dataset.useConstraints!"Not available"}</td>
                    </tr>
                </table>
</@template.master>