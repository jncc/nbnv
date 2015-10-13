<#assign datasetKey=URLParameters.datasetKey>
<#assign dataset=json.readURL("${api}/datasets/${datasetKey}")>
<#assign admins=json.readURL("${api}/datasets/${datasetKey}/admins")>
<#assign organisation=json.readURL("${dataset.organisationHref}")>

<@template.master title="View Report For ${dataset.title}"
    javascripts=[
        "/js/jquery.dataTables.min.js",
        "/js/jquery.validate.min.js",
        "/js/download/reports/enable-dataset-portal-report.js",
        "/js/dialog_utils.js", "/js/jquery.fileDownload.js"] 
    csss=["/css/download/downloadFilters.css"]>
    <script>
        nbn.nbnv.api = '${api}';
    </script>
    <h1>View Report For ${dataset.title}</h1>
    <div id="nbn-download-filters-div" class="tabbed nbn-organisation-tabbed">
        <h3>Filters</h3>
        <form id="nbn-download-filter">
            <label class="nbn-download-form-filter-label">Start Date: </label>
            <input type="text" id="startDate" name="startDate" />
            <input type="hidden" id="startDateHidden" name="startDateHidden" /><br />
            <label class="nbn-download-form-filter-label">End Date: </label>
            <input type="text" id="endDate" name="endDate" />
            <input type="hidden" id="endDateHidden" name="endDateHidden" /><br />
            <input type="submit" />
        </form>
    </div>
    <div id="nbn-information-div" class="tabbed nbn-organisation-tabbed">
        <h3>Information</h3>
        <p>This table displays all cases where a user has viewed records either through the Gateway reporting pages, for example the interactive mapping tool or directly through the REST services, rather than downloaded records through the Gateway’s download wizard as recorded in the <a href="https://data.nbn.org.uk/Reports/Download">download log</a>.</p>
        <p>Included in the table are the filter used to view the records, when the records were viewed and number of records viewed at the granted level of access. The category and reason of use are not recorded when viewing records on the NBN Gateway or through the REST services so this information is not available in this log.</p>
    </div>
    <div style="clear:both;"></div>
    <div class="tabbed nbn-organisation-tabbed nbn-datatable" data-dataset="${datasetKey}">
        <h3><#if organisation.hasSmallLogo><img alt="${organisation.name}" src="${organisation.smallLogo}"/></#if>${organisation.name} : [${dataset.key}] ${dataset.title}<a id="downloadFor${dataset.key}" href="#" style="float:right">Download as CSV</a></h3>
        <div id="nbn-downloads-div-${datasetKey}"></div>
    </div>  
    <@dialog_utils.userInfoDialog />
    <@dialog_utils.downloadDialogs />
</@template.master>