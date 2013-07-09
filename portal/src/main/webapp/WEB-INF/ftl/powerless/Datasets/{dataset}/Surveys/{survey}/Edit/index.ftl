<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}") />
<#assign survey=json.readURL("${api}/datasets/${URLParameters.dataset}/surveys/${URLParameters.survey}") />

<@template.master title="NBN Gateway - Modify Survey Metadata"  
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/jquery.validate.min.js","/js/metadata/enable-survey-metadata-form.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/admin-controls.css"]>
    <form method="POST" id="nbn-modify-survey-metadata" action="${api}/datasets/${dataset.key}/surveys/${URLParameters.survey}">
        <table class="nbn-dataset-table nbn-simple-table">
            <tr>
                <th>Dataset</th>
                <td>Part of the "${dataset.title}" Dataset</td>
            </tr>
            <tr>
                <th>Provider Key</th>
                <td><#if survey.providerKey??>${survey.providerKey}<#else>No Provider Key Supplied</#if></td>
            </tr>
            <tr>
                <th>Title</th>
                <td><input type="text" name="title" value="<#if survey.title??>${survey.title}<#else></#if>" /></td>
            </tr>
            <tr>
                <th>Description</th>
                <td><textarea name="description"><#if survey.description??>${survey.description}<#else></#if></textarea></td>
            </tr>
            <tr>
                <th>Geographical Coverage</th>
                <td><textarea name="geographicalCoverage"><#if survey.geographicalCoverage??>${survey.geographicalCoverage}<#else></#if></textarea></td>
            </tr>
            <tr>
                <th>Temporal Coverage</th>
                <td><textarea name="temporalCoverage"><#if survey.temporalCoverage??>${survey.temporalCoverage}<#else></#if></textarea></td>
            </tr>
            <tr>
                <th>Data Quality</th>
                <td><textarea name="dataQuality"><#if survey.dataQuality??>${survey.dataQuality}<#else></#if></textarea></td>
            </tr>
            <tr>
                <th>Data Capture Method</th>
                <td><textarea name="dataCaptureMethod"><#if survey.dataCaptureMethod??>${survey.dataCaptureMethod}<#else></#if></textarea></td>
            </tr>
            <tr>
                <th>Purpose</th>
                <td><textarea name="purpose"><#if survey.purpose??>${survey.purpose}<#else></#if></textarea></td>
            </tr>
            <tr>
                <th>Additional Information</th>
                <td><textarea name="additionalInformation"><#if survey.additionalInformation??>${survey.additionalInformation}<#else></#if></textarea></td>
            </tr>
        </table>
        <input id="nbn-survey-metadata-update-submit" type="submit" value="Update Survey Metadata" />
        <div id="nbn-waiting-ticker" style="display:none; float: right;"><p>Warning it may take some time for these changes to propagate to live site <img src="/img/ajax-loader.gif" /></p></div>
    </form>
</@template.master>