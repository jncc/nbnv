<#assign user=json.readURL("${api}/user") />

<@template.master title="Edit Access Request"
    javascripts=[
        "/js/admin/access/util/jquery.qtip.min.js"
        ,"/js/admin/access/util/moment.min.js"
        ,"/js/jquery.dataTables.min.js"
        ,"/js/jquery.watermark.min.js"
        ,"/js/filter/year.js"
        ,"/js/filter/spatial.js"
        ,"/js/filter/taxon.js"
        ,"/js/filter/dataset.js"
        ,"/js/filter/sensitive.js"
        ,"/js/admin/access/requestDetails.js"
        ,"/js/admin/access/requestEditGrantDialog.js"
        ,"/js/admin/access/timeLimit.js"
        ,"/js/admin/access/editRequest.js"
        ,"/js/admin/access/requestEditResult.js"
        ,"/js/dialog_spinner.js"] 
    csss=["/css/jquery.qtip.min.css","/css/accessRequest.css","/css/dialog-spinner.css"]>

    <script>
        nbn.nbnv.api = '${api}';
	
	$(function() {
            var json = ${model.filter.filterJSON};
            var requester = '${model.organisation.name}';
            var dataset = '${model.datasetKey}';
            var id = ${model.filter.id?c};
            nbn.nbnv.ui.editRequest(json, requester, dataset, id, $('#filter'), true);
	});
    </script>

    <h1>Edit Enhanced Access Request</h1>
    <div id="filter"></div>
</@template.master>