<#assign datasets=json.readURL("${api}/datasets/adminable")>
<#assign organisations=json.readURL("${api}/organisations")>

<@template.master title="Download Report By Dataset"
    javascripts=[
        "/js/jquery.dataTables.min.js",
        "/js/jquery.validate.min.js",
        "/js/download/reports/enable-dataset-report.js"] 
    csss=[
        "//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css",
        "/css/download/downloadFilters.css"]>
    <script>
        nbn.nbnv.api = '${api}';
    </script>
    <h1>Download Report By Dataset</h1>
    <div id="nbn-download-filters-div" class="tabbed nbn-organisation-tabbed">
        <h3>Filters</h3>
        <form id="nbn-download-filter">
            <label class="nbn-download-form-filter-label">Start Date: </label>
            <input type="text" id="startDate" name="startDate" />
            <input type="hidden" id="startDateHidden" name="startDateHidden" /><br />
            <label class="nbn-download-form-filter-label">End Date: </label>
            <input type="text" id="endDate" name="endDate" />
            <input type="hidden" id="endDateHidden" name="endDateHidden" /><br />
            <label class="nbn-download-form-filter-label">Purpose: </label>
            <select id="purposeID" class="nbn-download-form-filter-select">
                <option value="-1">Any</option>
                <option value="1">Personal interest</option>
                <option value="2">Educational purposes</option>
                <option value="3">Research and scientific analysis</option>
                <option value="4">Media publication</option>
                <option value="5">Conservation NGO work</option>
                <option value="6">Professional land management</option>
                <option value="7">Data provision and interpretation (commercial)</option>
                <option value="8">Data provision and interpretation (non-profit)</option>
                <option value="9">Statutory work</option>
            </select><br />

            <label class="nbn-download-form-filter-label">Organisation: </label>
            <select id="organisationID" class="nbn-download-form-filter-select">
                <option value="-1">Any</option>
                <#list organisations as org>
                    <option value="${org.id}">${org.name}</option>
                </#list>
            </select><br />      

            <input type="submit" />
        </form>
    </div>
    <div id="nbn-download-stats-div" class="tabbed nbn-organisation-tabbed">
        <h3>Download Statistics</h3>
        <div id="downloadStats">
        </div>
    </div>
    <div style="clear:both;">
        <#if datasets?has_content>
            <#list datasets as dataset>
                <div class="tabbed nbn-organisation-tabbed nbn-datatable" data-dataset="${dataset.key}">
                    <h3>[${dataset.key}] ${dataset.title}</h3>
                    <div id="nbn-downloads-div-${dataset.key}"></div>
                </div>
            </#list>
        <#else>
            <p>There are no datasets over which you have enough privileges to view download records</p> 
        </#if>
    </div>
</@template.master>