<#assign datasetKey=URLParameters.datasetKey>
<#assign dataset=json.readURL("${api}/datasets/${datasetKey}")>
<#assign admins=json.readURL("${api}/datasets/${datasetKey}/admins")>
<#assign organisation=json.readURL("${dataset.organisationHref}")>

<@template.master title="Portal Download Report For ${dataset.title}"
    javascripts=[
        "/js/jquery.dataTables.min.js",
        "/js/jquery.validate.min.js",
        "/js/download/reports/enable-dataset-portal-report.js",
        "/js/dialog_utils.js"] 
    csss=[
        "//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css",
        "/css/download/downloadFilters.css"]>
    <script>
        nbn.nbnv.api = '${api}';
    </script>
    <h1>Download Report For ${dataset.title}</h1>
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
        <p>The views shown below are all cases where a user has viewed record data through either the portal or directly through the API, rather than through the the provided download wizard. This can only be done from the Taxon Observations Pages where are user selects an individual species or via the interactive mapper. When users use the API they can also view record data, so these views are also recorded here.</p>
        <p>These views are all attached to the filters that were used to view these records and how many records were viewed, we cannot however record the use of the records through this system. The level of access given to the records is determined by the level of access granted to the user in question at the time the view occurred.</p>
    </div>
    <div style="clear:both;"></div>
    <div class="tabbed nbn-organisation-tabbed nbn-datatable" data-dataset="${datasetKey}">
        <h3><#if organisation.hasSmallLogo><img alt="${organisation.name}" src="${organisation.smallLogo}"/></#if>${organisation.name} : [${dataset.key}] ${dataset.title}</h2>
        <div id="nbn-downloads-div-${datasetKey}"></div>
    </div>  
    <@dialog_utils.userInfoDialog />
</@template.master>