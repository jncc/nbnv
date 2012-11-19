window.nbn = window.nbn || {};
nbn.nbnv = nbn.nbnv || {};
nbn.nbnv.ui = nbn.nbnv.ui || {};

nbn.nbnv.ui.warning = function($title, $content) {
	var data = $('<div>')
		.addClass("warning")
		.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-state-error")
		.append($('<div>')
                    .addClass("warning-header")
                    .append($title)
                ).append($('<div>')
                    .addClass("warning-text")
                    .append($content)
                );
		
	return data;
};
