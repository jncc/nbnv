
<@template.master title="Request Permission"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/filter/sensitive.js","/js/filter/year.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]>

    <style>
        .filterheader { font-weight: bold; padding-left: 25px; }
        .resulttext { padding-left: 25px; }
    </style>
    <script>
	var filter;
        nbn.nbnv.api = '${api}';
	
	$(function() {
                var json = {};
		
                var sensitive = new nbn.nbnv.ui.filter.sensitive();
                var year = new nbn.nbnv.ui.filter.year();
		$('#filter').append(sensitive._renderHeader());
		$('#filter').append(sensitive._renderPanel());
		$('#filter').append(year._renderHeader());
		$('#filter').append(year._renderPanel());
                $('#filter').accordion();
		
		$('#next').click(function() {
				window.location = "Create/Request?src=0&json=" + (JSON.stringify(filter.getJSON(), null, '\t'));
			}
		);
                $('#cancel').click(function() {
				sensitive._onExit();
			}
		);
			
	});
	</script>

	<h1>Request Enhanced Access</h1>
	<div id="filter">
        </div>

	<button id="next">Next &gt;</button><button id="cancel">Cancel</button>

<!--            <h3><span class="filterheader">Request Details</span></h3>
            <div>TODO</div>
            <h3><span class="filterheader">Sensitive Filter</span></h3>
            <div id="sensitive-filter"></div>
            <h3><span class="filterheader">Taxon Filter</span></h3>
            <div id="taxon-filter">Taxon filter</div>
            <h3><span class="filterheader">Spatial Filter</span></h3>
            <div id="spatial-filter">Spatial filter</div>
            <h3><span class="filterheader">Temporal Filter</span></h3>
            <div id="temporal-filter">Temporal filter</div>
            <h3><span class="filterheader">Dataset Filter</span></h3>
            <div id="dataset-filter">Dataset filter</div> -->

</@template.master>