window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.SiteBoundaryPicker = function(layerToQuery) {
	$.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
		createPickerDiv: function(idresults, position, callback) {
console.log(idresults);
			var toReturn = $('<div>');
			var res = idresults.results;
			for (var i in res) {
				var adminSiteKey = res[i].attributes.ADMINSITEKEY || res[i].attributes.adminSiteKey;
				$('<div>')
					.append($('<a>' + res[i].displayFieldName + ' - ' + res[i].value + '</a>')
						.attr('href', 'http://data.nbn.org.uk/siteInfo/siteSpeciesGroups.jsp?useIntersects=1&engOrd=false&allDs=1&maxRes=1&siteKey=' + adminSiteKey)
						.attr('target', '_blank')
						.attr('alt', 'Site name')
					)
					.appendTo(toReturn);
			}
			if(res.length==0)
				toReturn.append($('<div>No Results here</div>'));
			callback(toReturn);
		}
	}));
}