<#assign dataset=json.readURL("${api}/datasets/${URLParameters.dataset}") />
<#assign survey=json.readURL("${api}/datasets/${URLParameters.dataset}/surveys/${URLParameters.survey}") />

<@template.master title="NBN Gateway - View Survey Metadata"  
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/admin-controls.css"]>
    <table class="nbn-dataset-table nbn-simple-table">
        <tr>
            <td>Dataset</td>
            <td>Part of the "${dataset.title}" Dataset</td>
        </tr>
        <tr>
            <td>Provider Key</td>
            <td><#if survey.providerKey??>${survey.providerKey}<#else>No Provider Key Supplied</#if></td>
        </tr>
        <tr>
            <td>Title</td>
            <td><#if survey.title??>${survey.title}<#else>No Title Supplied</#if></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><#if survey.description??>${survey.description}<#else>No Description Supplied</#if></td>
        </tr>
        <tr>
            <td>Geographical Coverage</td>
            <td><#if survey.geographicalCoverage??>${survey.geographicalCoverage}<#else>No Geographical Coverage Specified</#if></td>
        </tr>
        <tr>
            <td>Temporal Coverage</td>
            <td><#if survey.temporalCoverage??>${survey.temporalCoverage}<#else>No Temporal Coverage Specified</#if></td>
        </tr>
        <tr>
            <td>Data Quality</td>
            <td><#if survey.dataQuality??>${survey.dataQuality}<#else>No Data Quality Specified</#if></td>
        </tr>
        <tr>
            <td>Data Capture Method</td>
            <td><#if survey.dataCaptureMethod??>${survey.dataCaptureMethod}<#else>No Data Capture Method Specified</#if></td>
        </tr>
        <tr>
            <td>Purpose</td>
            <td><#if survey.purpose??>${survey.purpose}<#else>No Purpose Specified</#if></td>
        </tr>
        <tr>
            <td>Additional Information</td>
            <td><#if survey.additionalInformation??>${survey.additionalInformation}<#else>No Additional Information Provided</#if></td>
        </tr>
    </table>
</@template.master>