<#assign datasets=json.readURL("${api}/datasets/adminableDatasetsByUserAndOrg")>

<@template.master title="Download Report By Dataset"
    javascripts=[
        "/js/jquery.dataTables.min.js",
        "/js/download/reports/enable-dataset-report.js"] 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>
    <script>
        nbn.nbnv.api = '${api}';
    </script>
    <h1>Download Report By Dataset</h1>
    <#if datasets?has_content>
        <#list datasets as dataset>
            <#assign downloads=json.readURL("${api}/taxonObservations/download/report/${dataset.key}")>
            <#assign organisation=json.readURL(dataset.organisationHref)>
            <div class="tabbed nbn-organisation-tabbed nbn-datatable" data-dataset="${dataset.key}">
                <h3><#if organisation.hasSmallLogo><img alt="${organisation.name}" src="${organisation.smallLogo}"/></#if>${organisation.name} : [${dataset.key}] ${dataset.title}</h2>
                <div id="nbn-downloads-div-${dataset.key}"></div>
            </div>
        </#list>
    <#else>
        <p>There are no datasets over which you have enough privileges to view download records</p> 
    </#if>
</@template.master>