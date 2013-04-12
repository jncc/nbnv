window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.datasetselector = function() {
	var _datasetCount = 0;
	
	$.extend(this, new nbn.util.ObservableAttribute('resultText'), '');
	
	this._datasets = [];
	
	this._render = function() {
		var _me = this;
		
                var datasetTable = $('<table>');
                
                $.ajax({
                    url: nbn.nbnv.api + '/taxonDatasets',
                    success: function(datasets) {
                        //var datasets = $.parseJSON(data);
                        
                        $.each(datasets, function(id, td) {
                            var dr = $('<tr>')
                            		.append($('<td>')
                                            .append($('<input>')
                                                .attr('type', 'checkbox').attr('checked', 'true').attr('name', td.key)
						.change(function() { 
                                                    if ($(this).is(':checked'))
                                                        _me._addDataset($(this).attr('name'));
                                                    else
							_me._dropDataset($(this).attr('name'));
                                                    }
                                                )
                                            )
					).append($('<td>')
                                            .addClass('dataset-label')
                                            .append(td.organisationName + ' - ' + td.title)
					);
                            datasetTable.append(dr);
                            _me._addDataset(td.key);

                        })
                    }
                });
                
                var data = $('<div>')
			.append($('<div>')
				.addClass("section-header")
				.addClass("ui-widget-header ui-corner-all")
				.text("Datasets")
			).append($('<div>')
				.addClass("section-content section-content-datasetfilter")
				.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
				.append(datasetTable)
			);
		
		
		return data;
	};
	
	this._addDataset = function(dskey) {
		_datasetCount++;
		this._datasets.push(dskey);
		this._setResult();
	};

	this._dropDataset = function(dskey) {
		_datasetCount--;
		this._datasets.splice(this._datasets.indexOf(dskey), 1);
		this._setResult();
	};
	
	this._setResult = function() {
		this.setresultText("from <b>" + _datasetCount + "</b> dataset(s)");
	};
	
	this.getJSON = function() {
		return {
			count: _datasetCount,
			datasets: this._datasets
		};
	};
};

/*

		<div class="section-header">Datasets</div>
		<div class="portlet-content-dataset">
			<div><input type="checkbox" checked="true">Conchological Society of Great Britain & Ireland - Mollusc (non-marine) data for Great Britain and Ireland</input></div>

*/