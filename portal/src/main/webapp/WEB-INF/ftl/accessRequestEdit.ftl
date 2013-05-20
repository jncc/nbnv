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
        ,"/js/admin/access/requestReason.js"
        ,"/js/admin/access/requestResult.js"
        ,"/js/admin/access/timeLimit.js"
        ,"/js/admin/access/editRequest.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/jquery.qtip.min.css","/css/accessRequest.css"]>

    <script>
        nbn.nbnv.api = '${api}';
	
	$(function() {
            var json = ${model.filterJSON};
            nbn.nbnv.ui.editRequest(json, $('#filter'));
	});
    </script>

    <h1>Edit Enhanced Access Request</h1>
    <div id="filter"></div>
</@template.master>