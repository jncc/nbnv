<#if RequestParameters.json?has_content><#assign data="${RequestParameters.json}" /><#else><#assign data="{}" /></#if>

<@template.master title="Request Permission"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js"
        ,"/js/admin/access/util/jquery.qtip.min.js"
        ,"/js/admin/access/util/moment.min.js"
        ,"/js/filter/sensitive.js"
        ,"/js/filter/year.js"
        ,"/js/filter/spatial.js"
        ,"/js/filter/taxon.js"
        ,"/js/filter/dataset.js"
        ,"/js/admin/access/requestReason.js"
        ,"/js/admin/access/requestResult.js"
        ,"/js/admin/access/timeLimit.js"
        ,"/js/admin/access/createRequest.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/jquery.qtip.min.css"]>

    <style>
        .filterheader { font-weight: bold; padding-left: 25px; }
        .resulttext { padding-left: 25px; }
        .comboSpan { padding-left: 25px; width: 100px; display: inline-block; }
        .queryBlock { padding-top: 1em; }
        .dataset-label { width: 100%; display: inline-block; border: 1px solid white; }
        .spanHover { background-color: lightgrey; }
    </style>
    <script>
        nbn.nbnv.api = '${api}';
	
	$(function() {
            var json = ${data};
            nbn.nbnv.ui.createRequest(json, $('#filter'));
	});
	</script>

	<h1>Request Enhanced Access</h1>
	<div id="filter">
        </div>
</@template.master>