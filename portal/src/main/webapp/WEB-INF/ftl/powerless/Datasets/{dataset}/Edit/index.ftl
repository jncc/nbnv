<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}/edit")>
<#assign licences=json.readURL("${api}/datasetLicence")>

<@template.master title="Metadata for ${dataset.title}"
    javascripts=["/js/jquery.dataTables.min.js","/js/admin/metadata.js"] 
    csss=["/css/admin-controls.css"]>
            <form id="nbn-metadata-update" url="${api}/datasets/${dataset.key}">
                <table class="nbn-dataset-table nbn-simple-table">
                    <tr>
                        <th>Title</th>
                        <td><input type="text" name="title" value="${dataset.title}"></input></td>
                    </tr>
                    <tr>
                        <th>Description</th>
                        <td><textarea name="description"><#if dataset.description?has_content>${dataset.description}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Purpose of data capture</th>
                        <td><textarea name="purpose"><#if dataset.purpose?has_content>dataset.purpose</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Methods of data capture</th>
                        <td><textarea name="captureMethod"><#if dataset.captureMethod?has_content>${dataset.captureMethod}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Geographical coverage</th>
                        <td><textarea name="geographicalCoverage"><#if dataset.geographicalCoverage?has_content>${dataset.geographicalCoverage}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Temporal coverage</th>
                        <td><textarea name="temporalCoverage"><#if dataset.temporalCoverage?has_content>${dataset.temporalCoverage}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Data quality</th>
                        <td><textarea name="quality"><#if dataset.quality?has_content>${dataset.quality}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Additional information</th>
                        <td><textarea name="additionalInformation"><#if dataset.additionalInformation?has_content>${dataset.additionalInformation}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Access constraints</th>
                        <td><textarea name="accessConstraints"><#if dataset.accessConstraints?has_content>${dataset.accessConstraints}</#if></textarea></td>
                    </tr>
                    <tr>
                        <th>Use constraints</th>
                        <td><textarea name="useConstraints"><#if dataset.useConstraints?has_content>${dataset.useConstraints}</#if></textarea></td>
                    </tr>
					<tr>
						<th>Licence</th>
						<td>
							<select name="licenceID">
								<#if dataset.licenceID?has_content>
									<option value="-1">No licence selected</option>
									<#list licences as licence>
										<option value="${licence.id}" <#if dataset.licenceID == licence.id>selected</#if>>${licence.name}</option>
									</#list>
								<#else>
									<option value="-1" selected>No licence selected</option>
									<#list licences as licence>
										<option value="${licence.id}">${licence.name}</option>
									</#list>
								</#if>
							</select>
						</td>
					</tr>
                </table>
                <input type="hidden" name="key" value="${dataset.key}" />
                <button id="nbn-form-submit" data-dataset="${dataset.key}"> Update </button>
                <div id="nbn-waiting-note" style="float:right; display:none;"><img src="/img/ajax-loader.gif"/>Changing Dataset Metadata, changes may not appear on the site instantly</div>
            </form>
</@template.master>