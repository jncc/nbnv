
<@template.master title="Request Permission"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/admin/access/util/moment.min.js","/js/filter/sensitive.js","/js/filter/year.js","/js/filter/spatial.js","/js/filter/taxon.js","/js/filter/dataset.js","/js/admin/access/requestReason.js","/js/admin/access/requestResult.js","/js/admin/access/timeLimit.js"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"]>

    <style>
        .filterheader { font-weight: bold; padding-left: 25px; }
        .resulttext { padding-left: 25px; }
        .comboSpan { padding-left: 25px; width: 100px; display: inline-block; }
        .queryBlock { padding-top: 1em; }
    </style>
    <script>
	var filter;
        nbn.nbnv.api = '${api}';
	
	$(function() {
                var json = {};
		
                var reason = new nbn.nbnv.ui.requestReason();
                var sensitive = new nbn.nbnv.ui.filter.sensitive();
                var year = new nbn.nbnv.ui.filter.year();
                var spatial = new nbn.nbnv.ui.filter.spatial();
                var taxon = new nbn.nbnv.ui.filter.taxon();
                var dataset = new nbn.nbnv.ui.filter.dataset();
                var timeLimit = new nbn.nbnv.ui.timeLimit();
                var result = new nbn.nbnv.ui.requestResult();

		$('#filter').append(reason._renderHeader());
		$('#filter').append(reason._renderPanel());
		$('#filter').append(sensitive._renderHeader());
		$('#filter').append(sensitive._renderPanel());
		$('#filter').append(spatial._renderHeader());
		$('#filter').append(spatial._renderPanel());
		$('#filter').append(taxon._renderHeader());
		$('#filter').append(taxon._renderPanel());
		$('#filter').append(year._renderHeader());
		$('#filter').append(year._renderPanel());
		$('#filter').append(dataset._renderHeader());
		$('#filter').append(dataset._renderPanel());
		$('#filter').append(timeLimit._renderHeader());
		$('#filter').append(timeLimit._renderPanel());
		$('#filter').append(result._renderHeader());
		$('#filter').append(result._renderPanel());

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
                        } else if (newFilter == 'dataset') {
                            dataset._onEnter();
                        } else if (newFilter == 'timeLimit') {
                            timeLimit._onEnter();
                        } else if (newFilter == 'reason') {
                            reason._onEnter();
                        }

                        if (oldFilter == 'sensitive') {
                            sensitive._onExit();
                        } else if (oldFilter == 'year') {
                            year._onExit();
                        } else if (oldFilter == 'spatial') {
                            spatial._onExit();
                        } else if (oldFilter == 'taxon') {
                            taxon._onExit();
                        } else if (oldFilter == 'dataset') {
                            dataset._onExit();
                        } else if (oldFilter == 'timeLimit') {
                            timeLimit._onExit();
                        } else if (oldFilter == 'reason') {
                            reason._onExit();
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
</@template.master>