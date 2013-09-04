<#assign datasetKey=URLParameters.datasetKey>
<#assign dataset=json.readURL("${api}/datasets/${datasetKey}")>
<#assign organisation=json.readURL(dataset.organisationHref)>
<#assign downloads=json.readURL("${api}/taxonObservations/download/report/${dataset.key}")>

<@template.master title="Download Report For ${dataset.title}"
    javascripts=[
        "/js/jquery.dataTables.min.js",
        "/js/download/reports/enable-datatable.js",
        "/js/download/reports/enable-single-dataset-report.js"] 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css"]>
    <h1>Download Report For ${dataset.title}</h1>
    <div class="tabbed nbn-organisation-tabbed">
        <h3><#if organisation.hasSmallLogo><img alt="${organisation.name}" src="${organisation.smallLogo}"/></#if>${organisation.name} : [${dataset.key}] ${dataset.title}</h2>
        <#if downloads?has_content>
            <table id="datatable" class="nbn-dataset-table">
                <thead>
                    <th>User</th>
                    <th>Organisation</th>
                    <th>Date</th>
                    <th>Download</th>
                    <th>Use Category</th>
                    <th>Use Reason</th>
                    <th>Statistics</th>
                </thead>
                <#list downloads as download>
                    <tr>
                        <td>${download.forename} ${download.surname}</td>
                        <td><#if download.organisationName??>${download.organisationName}<#else></#if></td>
                        <td>${download.downloadTime?number_to_datetime?string("EEE MMM dd yyyy HH:mm:ss '('zzz')'")}</td>
                        <td>${download.filterText}</td>
                        <td>${download.purpose}</td>
                        <td>${download.reason}</td>
                        <td>${download.recordCount} records from this dataset in this download. This is ${download.recordCount / download.totalRecords * 100}% of this dataset and this compromises ${download.totalDownloaded / download.recordCount * 100}% of the download</td>
                    </tr>
                </#list>
            </table>
        <#else>
            <p>No downloads have been made against this dataset</p>
        </#if>
    </div>
</@template.master>