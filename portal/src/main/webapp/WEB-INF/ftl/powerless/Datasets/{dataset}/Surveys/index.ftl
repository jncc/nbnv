<#assign datasetId="${URLParameters.dataset}">
<#assign surveys=json.readURL("${api}/taxonDatasets/${datasetId}/surveys")>
<#assign isAdmin=json.readURL("${api}/datasets/${datasetId}/isAdmin")>
<ul id="nbn-surveys" class="collapsible-list">
    <#list surveys as survey>
        <li class="nbn-designation-category-heading-strong"><#if survey.description??>${survey.title?has_content?string(survey.description,"No Title Supplied")}<#else>No Title Supplied</#if>
            <ul>
                <li>
                    <table class="nbn-survey-table ui-state-highlight">
                        <tr><th>Description</th><td><#if survey.description??>${survey.description?has_content?string(survey.description,"Description not available")}<#else>Description not available</#if></td></tr>
                        <tr><th>Species count</th><td>${survey.speciesCount}</td></tr>
                        <tr><th>Sample count</th><td>${survey.sampleCount}</td></tr>
                        <tr><th>Record count</th><td>${survey.recordCount}</td></tr>
                        <tr><th>Survey key</th><td>${survey.id?c}</td></tr>
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
