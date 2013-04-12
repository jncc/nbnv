window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.requestedAccess = function(accessJson) {
    var json = accessJson;
    
    this._render = function() {
        var data = $('<div>')
            .addClass("portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .append($('<div>')
                .addClass("portlet-header ui-widget-header ui-corner-all")
                .text("Requested Data")
            ).append($('<div>')
                .addClass("portlet-content")
                .append(nbn.nbnv.ui.util.requestJsonToText(json))
            );
       
        return data;
    }
}

/*
 * 	<div class="portlet">
		<div class="portlet-header">Requested Data</div>
		<div class="portlet-content">All non-sensitive records for <b><i>Carychium minimum</i></b> within <b>North Devon (VC 4)</b> between <b>1990</b> and <b>2010</b></div>
	</div>

 */