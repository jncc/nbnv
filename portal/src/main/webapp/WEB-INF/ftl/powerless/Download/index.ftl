<#assign user=json.readURL("${api}/user") />
<#if RequestParameters.json?has_content><#assign data="${RequestParameters.json}" /><#else><#assign data="{}" /></#if>

<@template.master title="NBN Gateway - Download Records"
    javascripts=[
        "/js/admin/access/util/jquery.qtip.min.js"
        ,"/js/admin/access/util/moment.min.js"
        ,"/js/jquery.dataTables.min.js"
        ,"/js/jquery.watermark.min.js"
        ,"/js/filter/year.js"
        ,"/js/filter/spatial.js"
        ,"/js/filter/taxon.js"
        ,"/js/filter/dataset.js"
        ,"/js/download/download.js"
        ,"/js/download/downloadResult.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/jquery.qtip.min.css","/css/accessRequest.css"]>

    <script>
        nbn.nbnv.api = '${api}';
        nbn.nbnv.userID = ${user.id?c};
	
	$(function() {
            var json = ${data};
            nbn.nbnv.ui.download(json, $('#filter'));
	});
    </script>

    <h1>Download Records</h1>
    <div id="filter"></div>
    
</@template.master>