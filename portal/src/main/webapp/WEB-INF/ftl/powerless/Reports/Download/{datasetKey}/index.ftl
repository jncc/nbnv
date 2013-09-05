<#assign datasetKey=URLParameters.datasetKey>
<#assign dataset=json.readURL("${api}/datasets/${datasetKey}")>
<#assign organisation=json.readURL(dataset.organisationHref)>

<@template.master title="Download Report For ${dataset.title}"
    javascripts=[
        "/js/jquery.dataTables.min.js",
        "/js/download/reports/enable-dataset-report.js"] 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>
    <script>
        nbn.nbnv.api = '${api}';
    </script>

    <h1>Download Report For ${dataset.title}</h1>
    <div class="tabbed nbn-organisation-tabbed nbn-datatable" data-dataset="${datasetKey}">
        <h3><#if organisation.hasSmallLogo><img alt="${organisation.name}" src="${organisation.smallLogo}"/></#if>${organisation.name} : [${dataset.key}] ${dataset.title}</h2>
        <div id="nbn-downloads-div-${datasetKey}"></div>
    </div>  
</@template.master>