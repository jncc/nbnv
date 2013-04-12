
<@template.master title="Request Permission"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/filter/sensitive.js","/js/filter/year.js","/js/filter/spatial.js","/js/filter/taxon.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]>

    <style>
        .filterheader { font-weight: bold; padding-left: 25px; }
        .resulttext { padding-left: 25px; }
        .comboSpan { padding-left: 25px; width: 100px; display: inline-block; }
    </style>
    <script>
	var filter;
        nbn.nbnv.api = '${api}';
	
	$(function() {
                var json = {};
		
                var sensitive = new nbn.nbnv.ui.filter.sensitive();
                var year = new nbn.nbnv.ui.filter.year();
                var spatial = new nbn.nbnv.ui.filter.spatial();
                var taxon = new nbn.nbnv.ui.filter.taxon();

		$('#filter').append(sensitive._renderHeader());
		$('#filter').append(sensitive._renderPanel());
		$('#filter').append(spatial._renderHeader());
		$('#filter').append(spatial._renderPanel());
		$('#filter').append(taxon._renderHeader());
		$('#filter').append(taxon._renderPanel());
		$('#filter').append(year._renderHeader());
		$('#filter').append(year._renderPanel());
                $('#filter').accordion({
                    autoHeight: false,
                    change: function(event, ui) {
                        var newFilter = ui.newHeader.attr('filtertype');
                        var oldFilter = ui.oldHeader.attr('filtertype');

                        if (newFilter == 'sensitive') {
                            sensitive._onEnter();
                        } else if (newFilter == 'year') {
                            year._onEnter();
                        } else if (newFilter == 'spatial') {
                            spatial._onEnter();
                        } else if (newFilter == 'taxon') {
                            taxon._onEnter();
                        }

                        if (oldFilter == 'sensitive') {
                            sensitive._onExit();
                        } else if (oldFilter == 'year') {
                            year._onExit();
                        } else if (oldFilter == 'spatial') {
                            spatial._onExit();
                        } else if (oldFilter == 'taxon') {
                            taxon._onExit();
                        }
                    }
                });
		
		$('#next').click(function() {
				window.location = "Create/Request?src=0&json=" + (JSON.stringify(filter.getJSON(), null, '\t'));
			}
		);
                $('#cancel').click(function() {
				sensitive._onExit(); year._onExit();
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