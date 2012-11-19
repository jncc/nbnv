window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.filter = function() {
	this.subfilters = [];
	this.filterresult = new nbn.nbnv.ui.filterresult();
	this.datasetfilter = new nbn.nbnv.ui.datasetselector();
	this.sensitivefilter = new nbn.nbnv.ui.sensitivefilter();

        var filterSetupJSON = [];
        
        this.setJSON = function(json) {
            if (json.sensitive != null) {
                this.sensitivefilter.setSensitive(json.sensitive);
            }
            
            if (json.filters != null) {
                filterSetupJSON = json.filters;
            }
        }
        
	this.makeFilterText = function() {
		var filterTexts = [];
		
		this.subfilters.forEach(function(lsf) {
				filterTexts.push(lsf.getresultText());
			});
		
		this.filterresult.setResult(this.sensitivefilter.getresultText() + " " + filterTexts.join(', ') + " " + this.datasetfilter.getresultText());
	};
	
	this.getJSON = function() {
		var data = {
			sensitive: this.sensitivefilter.getJSON().sensitive,
			filters: [],
			datasetselection: this.datasetfilter.getJSON()
		};
		
		this.subfilters.forEach(function(lsf) {
				data.filters.push(lsf.getJSON());
			});
			
		return data;
	};
	
	this._render = function() {
		var _me = this;
		
		var data = $('<div>');

                var addFilterBtn = $('<button>')
			.text("Add filter")
			.click(function() { 
                            var lsf = new nbn.nbnv.ui.subfilter();
                            lsf.addresultTextUpdateListener({
                                update: function(evt, newVal) {
                                    _me.makeFilterText();
                        	}
                            });
                            _me.subfilters.push(lsf);
                            data.find('#subfilters').append(lsf._render());
                        });

		data.addClass('portlet')
			.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
			.append($('<div>')
				.append("Give me...")
				.addClass("portlet-header")
				.addClass("ui-widget-header ui-corner-all")
				.append($('<span>')
					.addClass("ui-icon ui-icon-person")
				)
			).append($('<div>')
				.addClass('portlet-content')
				.attr('id', 'subfilters')
				.append(this.sensitivefilter._render())
				.append($('<div>')
					.append(addFilterBtn)
                                 )
			).append($('<div>')
				.attr('id', 'filters')
			).append($('<div>')
				.attr('id', 'datasetselector')
			).append($('<div>')
				.attr('id', 'filterresult')
			);
		
                $.each(filterSetupJSON, function(i, filter) {
                            var lsf = new nbn.nbnv.ui.subfilter();
                            lsf.addresultTextUpdateListener({
                                update: function(evt, newVal) {
                                    _me.makeFilterText();
                        	}
                            });
                            lsf.setJSON(filter);
                            _me.subfilters.push(lsf);
                            data.find('#subfilters').append(lsf._render());
                });
                
		this.datasetfilter.addresultTextUpdateListener({
			update: function(evt, newVal) {
				_me.makeFilterText();
			}
		});

		this.sensitivefilter.addresultTextUpdateListener({
			update: function(evt, newVal) {
				_me.makeFilterText();
			}
		});
		
		data.find('#datasetselector').append(this.datasetfilter._render());
		data.find('#filterresult').append(this.filterresult._render());
			
		return data;			
	};
}
