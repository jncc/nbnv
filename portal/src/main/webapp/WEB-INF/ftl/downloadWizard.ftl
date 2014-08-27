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
        ,"/js/filter/sensitive.js"
        ,"/js/filter/verification.js"
        ,"/js/download/downloadReason.js"
        ,"/js/download/download.js"
        ,"/js/download/downloadResult.js"
        ,"/js/jquery.fileDownload.js"] 
    csss=["//ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/jquery.qtip.min.css","/css/accessRequest.css","/css/verification.css"]>

    <script>
        nbn.nbnv.api = '${api}';
        nbn.nbnv.userID = ${user.id?c};
        nbn.nbnv.isDownload = true;
	
	$(function() {
            var json = ${data};
            nbn.nbnv.ui.download(json, $('#filter'));
	});
    </script>

    <h1>Download Wizard</h1>
    <div id="filter"></div>

    <div id="preparing-download-dialog" title="Preparing Download..." style="display: none;">
        <p>We are preparing your download, please wait</p>
        <img style="display:block;margin-left: auto;margin-right: auto;" src="/img/ajax-loader-medium.gif" />
    </div>

    <div id="error-download-dialog" style="display:none;">
        <p>There was a problem generating your download:</p>
        <p id="error-download-reason"></p>
    </div>
</@template.master>