<#assign datasetId="${URLParameters.dataset}">
<#assign surveys=json.readURL("${api}/taxonDatasets/${datasetId}/surveys")>
<#assign isAdmin=json.readURL("${api}/datasets/${datasetId}/isAdmin")>
<ul id="nbn-surveys" class="collapsible-list">
    <#list surveys as survey>
        <li class="nbn-designation-category-heading-strong"><#if survey.title??>${survey.title?has_content?string(survey.title,"No Title Supplied")}<#else>No Title Supplied</#if>
            <ul>
                <li>
                    <table class="nbn-survey-table ui-state-highlight">
                        <tr><th>Survey Key</th><td>${survey.providerKey}</td></tr>                        
                        <tr><th>Description</th><td><#if survey.description??>${survey.description?has_content?string(survey.description,"Description not available")}<#else>Description not available</#if></td></tr>
                        <tr><th>Geographical Coverage</th><td><#if survey.geographicalCoverage??>${survey.geographicalCoverage?has_content?string(survey.geographicalCoverage, "None Supplied")}<#else>None Supplied</#if></td></tr>
                        <tr><th>Temporal Coverage</th><td><#if survey.temporalCoverage??>${survey.temporalCoverage?has_content?string(survey.temporalCoverage, "None Supplied")}<#else>None Supplied</#if></td></tr>
                        <tr><th>Data Capture Method</th><td><#if survey.dataCaptureMethod??>${survey.dataCaptureMethod?has_content?string(survey.dataCaptureMethod, "None Supplied")}<#else>None Supplied</#if></td></tr>
                        <tr><th>Purpose</th><td><#if survey.purpose??>${survey.purpose?has_content?string(survey.purpose, "None Supplied")}<#else>None Supplied</#if></td></tr>
                        <tr><th>Data Quality</th><td><#if survey.dataQuality??>${survey.dataQuality?has_content?string(survey.dataQuality, "None Supplied")}<#else>None Supplied</#if></td></tr>
                        <tr><th>Additional Information</th><td><#if survey.additionalInformation??>${survey.additionalInformation?has_content?string(survey.additionalInformation, "None Supplied")}<#else>None Supplied</#if></td></tr>
                        <tr><th>Species count</th><td>${survey.speciesCount}</td></tr>
                        <tr><th>Sample count</th><td>${survey.sampleCount}</td></tr>
                        <tr><th>Record count</th><td>${survey.recordCount}</td></tr>
                        
                        <#if isAdmin>
                        <tr><th></th><td><a href="/Datasets/${datasetId}/Surveys/${survey.id?c}/Edit">Edit Survey Metadata</a></td></tr>
                        </#if>
                    </table>
                    </li>
            </ul>
        </li>
    </#list>
    </li>
</ul>
