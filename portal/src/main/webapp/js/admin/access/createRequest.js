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
                            var j = {};
                            $.extend(j, sensitive.getJson());
                            $.extend(j, taxon.getJson());
                            $.extend(j, spatial.getJson());
                            $.extend(j, year.getJson());

                            dataset.setupTable(j, '/taxonObservations/datasets/requestable');
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
		
		$('#btn').click(function () {
                    alert(JSON.stringify(j));
                });
