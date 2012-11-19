
<@template.master title="Request Permission"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/admin/access/util/Observable.js","/js/admin/access/util/ObservableAttribute.js","/js/admin/access/sub-filter.js","/js/admin/access/filter.js","/js/admin/access/dataset-selector.js","/js/admin/access/sensitivefilter.js","/js/admin/access/result.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

    <script>
	var filter;
        nbn.nbnv.api = '${api}';
	
	$(function() {
                var json = {};
		filter = new nbn.nbnv.ui.filter();
                filter.setJSON(json);
		
		$('#filter').append(filter._render());
		
		$('#next').click(function() {
				window.location = "Request?src=0&json=" + (JSON.stringify(filter.getJSON(), null, '\t'));
			}
		);
                $('#cancel').click(function() {
				window.location = "/";
			}
		);
			
	});
	</script>

	<h1>Request Enhanced Access</h1>
	<div id="filter"></div>

	<button id="next">Next &gt;</button><button id="cancel">Cancel</button>
		

</@template.master>