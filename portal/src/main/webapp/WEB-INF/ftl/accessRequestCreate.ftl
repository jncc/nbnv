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
        ,"/js/filter/taxon.js"
        ,"/js/filter/dataset.js"
        ,"/js/admin/access/requestReason.js"
        ,"/js/admin/access/requestResult.js"
        ,"/js/admin/access/timeLimit.js"
        ,"/js/admin/access/createRequest.js"] 
    csss=["/css/jquery.qtip.min.css","/css/accessRequest.css"]>

    <script>
        nbn.nbnv.api = '${api}';
        nbn.nbnv.userID = ${user.id?c};
	
	$(function() {
            var json = ${data};
            nbn.nbnv.ui.createRequest(json, $('#filter'));
	});
    </script>

    <h1>Request Enhanced Access</h1>
    <div id="filter"></div>

    <div id="waiting-dialog" title="Processing Request" style="display:none;">
        <p>We are currently processing your request, please do not refresh or leave this page</p>
        <img src="/img/ajax-loader-medium.gif" style="display: block; margin-left: auto; margin-right: auto;"/>
    </div>

    <div id="finished-dialog" title="Processed Request" style="display:none;">
        <p id="finished-dialog-text"></p>
    </div>
</@template.master>