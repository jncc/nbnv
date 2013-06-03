<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}") />
<#assign survey=json.readURL("${api}/datasets/${URLParameters.dataset}/surveys/${URLParameters.survey}") />

<@template.master title="NBN Gateway - Modify Survey Metadata"  
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/metadata/enable-survey-metadata-form.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]>
    <form method="POST" id="modify-metadata-form" action="/Datasets/${URLParameters.dataset}/Surveys/${URLParameters.survey}">
        <table>
            <tr>
                <td>Dataset</td>
                <td>Part of the "${dataset.title}" Dataset</td>
            </tr>
            <tr>
                <td>Provider Key</td>
                <td><input type="text" value="${providerKey}" /></td>
            </tr>
            <tr>
                <td>Title</td>
                <td><input type="text" path="title" /></td>
            </tr>
            <tr>
                <td>Description</td>
                <td><textarea rows="5" cols="60">${survey.description}</textarea></td>
            </tr>
            <tr>
                <td>Geographical Coverage</td>
                <td><textarea rows="5" cols="60">${survey.geographicalCoverage}</textarea></td>
            </tr>
            <tr>
                <td>Temporal Coverage</td>
                <td><textarea rows="5" cols="60">${survey.temporalCoverage}</textarea></td>
            </tr>
            <tr>
                <td>Data Quality</td>
                <td><textarea rows="5" cols="60">${survey.dataQuality}</textarea></td>
            </tr>
            <tr>
                <td>Data Capture Method</td>
                <td><textarea rows="5" cols="60">${survey.dataCaptureMethod}</textarea></td>
            </tr>
            <tr>
                <td>Purpose</td>
                <td><textarea rows="5" cols="60">${survey.purpose}</textarea></td>
            </tr>
            <tr>
                <td>Additional Information</td>
                <td><textarea rows="5" cols="60">${survet.additionalInformation}</textarea></td>
            </tr>
        </table>
        <input type="submit" value="Update Survey Metadata" />
    </form>
</@template.master>