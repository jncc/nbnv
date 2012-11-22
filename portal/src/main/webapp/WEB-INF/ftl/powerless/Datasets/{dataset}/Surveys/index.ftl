<#assign datasetId="${URLParameters.dataset}">
<#assign surveys=json.readURL("${api}/taxonDatasets/${datasetId}/surveys")>
<ul id="nbn-surveys" class="collapsible-list">
    <#list surveys as survey>
        <li class="nbn-designation-category-heading-strong">${survey.title}
            <ul>
                <li>
                    <table class="nbn-survey-table ui-state-highlight">
                        <tr><th>Description</th><td>${survey.description?has_content?string(survey.description,"Description not available")}</td></tr>
                        <tr><th>Species count</th><td>${survey.speciesCount}</td></tr>
                        <tr><th>Sample count</th><td>${survey.sampleCount}</td></tr>
                        <tr><th>Record count</th><td>${survey.recordCount}</td></tr>
                        <tr><th>Survey key</th><td>${survey.id}</td></tr>
                    </table>
                    </li>
            </ul>
        </li>
    </#list>
    </li>
</ul>
