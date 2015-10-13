<#assign user=json.readURL("${api}/user") />
<#if RequestParameters.json?has_content><#assign data="${RequestParameters.json}" /><#else><#assign data="{}" /></#if>

<@template.master title="Request Permission"
    javascripts=[
        "/js/admin/access/util/jquery.qtip.min.js"
        ,"/js/admin/access/util/moment.min.js"
        ,"/js/jquery.dataTables.min.js"
        ,"/js/jquery.watermark.min.js"
        ,"/js/filter/year.js"
        ,"/js/filter/spatial.js"
        ,"/js/filter/sensitive.js"
        ,"/js/filter/taxon.js"
        ,"/js/admin/access/requestPickDataset.js"
        ,"/js/admin/access/requestPickUserReason.js"
        ,"/js/admin/access/requestGrantResult.js"
        ,"/js/admin/access/timeLimit.js"
        ,"/js/admin/access/createGrant.js"] 
    csss=["/css/jquery.qtip.min.css","/css/accessRequest.css","/css/dialog-spinner.css"]>

    <script>
        nbn.nbnv.api = '${api}';
	
	$(function() {
            var json = ${data};
            nbn.nbnv.ui.createGrant(json, $('#filter'));
	});
    </script>

    <h1>Grant Enhanced Access</h1>
    <div id="filter"></div>
</@template.master>