window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.filterresult = function() {
	this._result = $('<div>').addClass("section-content ui-widget ui-widget-content ui-helper-clearfix ui-corner-all");

	this._render = function() {
		var data = $('<div>')
			.append($('<div>')
				.addClass('section-header')
				.addClass("ui-widget-header ui-corner-all")
				.text("Result")
			).append(this._result);
			
		return data;
	};
	
	this.setResult = function(text) {
		this._result.html(text);
	}
};

/*
	<div class="section-header">Result</div>
	<div class="portlet-content">All non-sensitive records for <b><i>Carychium minimum</i></b> within <b>North Devon (VC 4)</b> between <b>1990</b> and <b>2010</b></div>
*/