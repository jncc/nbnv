<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}") />
<#assign survey=json.readURL("${api}/datasets/${URLParameters.dataset}/surveys/${URLParameters.survey}") />
<#assign isAdmin=json.readURL("${api}/datasets/${URLParameters.dataset}/isAdmin") />

<@template.master title="NBN Gateway - View Survey Metadata"  
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/admin-controls.css"]>
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
            <td><#if survey.title??>${survey.title}<#else>No Title Supplied</#if></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><#if survey.description??>${survey.description}<#else>No Description Supplied</#if></td>
        </tr>
        <tr>
            <th>Geographical Coverage</th>
            <td><#if survey.geographicalCoverage??>${survey.geographicalCoverage}<#else>No Geographical Coverage Specified</#if></td>
        </tr>
        <tr>
            <th>Temporal Coverage</th>
            <td><#if survey.temporalCoverage??>${survey.temporalCoverage}<#else>No Temporal Coverage Specified</#if></td>
        </tr>
        <tr>
            <th>Data Quality</th>
            <td><#if survey.dataQuality??>${survey.dataQuality}<#else>No Data Quality Specified</#if></td>
        </tr>
        <tr>
            <th>Data Capture Method</th>
            <td><#if survey.dataCaptureMethod??>${survey.dataCaptureMethod}<#else>No Data Capture Method Specified</#if></td>
        </tr>
        <tr>
            <th>Purpose</th>
            <td><#if survey.purpose??>${survey.purpose}<#else>No Purpose Specified</#if></td>
        </tr>
        <tr>
            <th>Additional Information</th>
            <td><#if survey.additionalInformation??>${survey.additionalInformation}<#else>No Additional Information Provided</#if></td>
        </tr>
        <#if isAdmin>
        <tr><th></th><td><a style="float:right;" href="/Datasets/${URLParameters.dataset}/Surveys/${survey.id?c}/Edit">Edit Survey Metadata</a></td></tr>
        </#if>
    </table>
</@template.master>