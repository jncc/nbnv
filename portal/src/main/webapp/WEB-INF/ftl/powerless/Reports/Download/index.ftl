<#assign datasets=json.readURL("${api}/datasets/adminableDatasetsByUserAndOrg")>

<@template.master title="Download Report By Dataset">
    <h1>Download Report By Dataset</h1>
    <#if datasets?has_content>
        <#list datasets as dataset>
            <#assign downloads=json.readURL("${api}/taxonObservations/download/report/${dataset.key}")>
            <#assign organisation=json.readURL(dataset.organisationHref)>
            <div class="tabbed nbn-organisation-tabbed">
                <h3><#if organisation.hasSmallLogo><img alt="${organisation.name}" src="${organisation.smallLogo}"/></#if>${organisation.name} : [${dataset.key}] ${dataset.title}</h2>
                <#if downloads?has_content>
                    <table id="nbn-datasets-datatable" class="nbn-dataset-table">
                        <tr>
                            <th>User</th>
                            <th>Organisation</th>
                            <th>Date</th>
                            <th>Download</th>
                            <th>Use Category</th>
                            <th>Use Reason</th>
                            <th>Statistics</th>
                        </tr>
                        <#list downloads as download>
                            <tr>
                                <td>${download.forename} ${download.surname}</td>
                                <td><#if download.organisationName??>${download.organisationName}<#else></#if></td>
                                <td>${download.downloadTime?number_to_datetime?string("EEE MMM dd yyyy hh:mm:ss '('zzz')'")}</td>
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
        </#list>
    <#else>
        <p>There are no datasets over which you have enough privileges to view download records</p> 
    </#if>
</@template.master>