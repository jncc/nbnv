window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.subfilter = function() {
	var filterform = $('<div>').attr('id', 'filterform');
	
	this._vars = [];
	this._type = '';
	var setupJSON = {type: 'year', start: '1600', end: '2012'};
        
	$.extend(this, new nbn.util.ObservableAttribute('resultText'), '');
	
        this.setJSON = function(json) {
            setupJSON = json;
        };
	
        this._render = function() {
		var _me = this;
		
                var filterSelector = $('<select>')
				.append($('<option>').text("Year filter").attr('value', 'year'))
				.append($('<option>').text("Taxon filter").attr('value', 'taxon'))
				.append($('<option>').text("Spatial filter").attr('value', 'spatial'))
				.change(function() {
					var value = $(this).val();
		
					if (value == 'year') _me.yearform();
					else if (value == 'taxon') _me.taxonform();
					else if (value == 'spatial') _me.spatialform();
				});
                                
		var data = $('<div>')
			.addClass("sub-filter")
			.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
			.append(filterSelector);
	
                filterSelector.val(setupJSON.type);
                filterSelector.change();
				
		data.append(filterform);
			
		return data;
			
	};
	
	this.getJSON = function() {
		if (this._type == 'year')
			return this.yearformJSON();
		else if (this._type == 'taxon')
			return this.taxonformJSON();
		else if (this._type == 'spatial')
			return this.spatialformJSON();
	};

	this.yearform = function() {
		this._type = 'year';
		var _me = this;
		
                var startInput = $('<input>')
				.attr('type', "text").attr('length', "8").attr('name', "yearstart").val('1600')
				.change(function() {
					_me._vars[0] = $(this).val();
					_me.updateyearform();					
				});
                
                var endInput = $('<input>')
				.attr('type', "text").attr('length', "8").attr('name', "yearend").val('2012')
				.change(function() {
					_me._vars[1] = $(this).val();
					_me.updateyearform();					
				});
                                
		filterform.html('');
		filterform.append("between ")
			.append(startInput)
			.append(" and ")
			.append(endInput);
                        
                startInput.val(setupJSON.start);
                endInput.val(setupJSON.end);
                startInput.change();
                endInput.change();


                this.updateyearform();
	};
	
	this.updateyearform = function() {
		this.setresultText("between <b>" + this._vars[0] + "</b> and <b>" + this._vars[1] + "</b>");
	};
	
	this.yearformJSON = function() {
		return {
			type: 'year',
			start: this._vars[0],
			end: this._vars[1]
		};
	};
	
	this.taxonform = function() {
		this._type = 'taxon';
		var _me = this;

                var _speciesAutoComplete = $('<input>')
                    .autocomplete({
                        cource :function(request, response) {
                                $.getJSON(nbn.nbnv.api + '/taxa?q=' + request.term, response);
                        },
                        select: function(event, ui) {
                            _me._vars[0] = ui.item.name;
                            _me._vars[1] = ui.item.key;
                            _me.updatetaxonform();
                        }
                    });
            
                _speciesAutoComplete.data( "autocomplete" )._renderItem = function(ul, item) {
                    return $( "<li></li>" )
                        .data( "item.autocomplete", item )
                        .append( "<a><strong>" + item.value + "</strong><br><i>" + item.name + "</i>," + item.authority + "</a>" )
                        .appendTo(ul);
                };
        
		filterform.html('');
		filterform.append("for ")
                    .append(_speciesAutoComplete);
        
                if (setupJSON.sciname != null) {
                    _speciesAutoComplete.val(setupJSON.sciname);
                    _me._vars[0] = setupJSON.sciname;
                    _me._vars[1] = setupJSON.taxon;
                    _me.updatetaxonform();
                } else {
        	    this._vars[0] = '';
                    this._vars[1] = '';
		    this.updatetaxonform();
                }
	};
	
	this.updatetaxonform = function() {
		this.setresultText("for <b><i>" + this._vars[0] + "</i></b>");
	};
	
	this.taxonformJSON = function() {
		return {
			type: 'taxon',
			taxon: this._vars[1],
                        sciname: this._vars[0]
		};
	};
	
	this.spatialform = function() {
		this._type = 'spatial';
		var _me = this;
		
                var boundary = $('<select>')
                    .addClass("sub-filter-spatial-select")
                    .change(function() {
                        var value = $(this).find("option:selected").text();
		
			_me._vars[1] = value;
                        _me._vars[2] = $(this).find("option:selected").attr('value');
			_me.updatespatialform();
                    });
                
                
                var boundaryTypes = $('<select>')
                    .change(function() {
                        var value = $(this).find("option:selected").attr('value');
                        _me._vars[3] = value;
                        
                        $.ajax({
                            url: nbn.nbnv.api + '/siteBoundaryDatasets/' + value + '/siteBoundaries',
                            success: function(data) {
                                boundary.html('');
                                
                                $.each(data, function(id, sb) {
                                    boundary.append($('<option>')
                                        .text(sb.name)
                                        .attr('value', sb.identifier)
                                    )});
                       
                                if (setupJSON.feature != null) {
                                    boundary.val(setupJSON.feature);
                                    setupJSON.feature = null; // needed, but icky
                                }
                                boundary.change();
                            }
                        });
                    });
                
                $.ajax({
                   url: nbn.nbnv.api + '/siteBoundaryDatasets',
                   success: function (datasets) {
                       $.each(datasets, function(id, sbd) {
                           boundaryTypes.append($('<option>')
                               .text(sbd.title)
                               .attr('value', sbd.datasetKey)
                           );
                       });
                       
                       if (setupJSON.boundaryType != null) {
                           boundaryTypes.val(setupJSON.boundaryType);
                       }
                       
                       boundaryTypes.change();
                   }
                });
                
                var match = $('<select>')
				.append($('<option>').text("within").attr('value', 'within'))
				.append($('<option>').text("overlaps").attr('value', 'overlaps'))
				.change(function() {
					var value = $(this).find("option:selected").attr('value');
		
					_me._vars[0] = value;
					_me.updatespatialform();
				})
                
		filterform.html('');
		filterform.append(match).append(' the boundary of ')
			.append(boundaryTypes)
                        .append(boundary);
                        
                if (setupJSON.match != null) {
                    match.val(setupJSON.match);
                    match.change();
                } else {
                    this._vars[0] = 'within';
                }		
                
                boundaryTypes.change();
		
                this.updatespatialform();
	};
	
	this.updatespatialform = function() {
		this.setresultText("<b>" + this._vars[0] + "</b> the boundary of <b>" + this._vars[1] + "</b>");
	};
	
	this.spatialformJSON = function() {
		return {
			type: 'spatial',
			match: this._vars[0],
                        boundary: this._vars[1],
                        boundaryType: this._vars[3],
			feature: this._vars[2]
		};
	};
};


/*
			<div>
				<select>
					<option>Year filter</option>
					<option selected="true">Taxon filter</option>
					<option>Spatial filter</option>
					<option>Survey filter</option>
				</select>		
			</div>
*/