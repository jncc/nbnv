window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.HabitatPicker = function(layerToQuery) {
	var _NODISPLAY = { 'OBJECTID':'', 'Shape':'', 'Shape.area':'', 'Shape.len':'' };
	var _NEFILTER = { 'INCID':'', 'HABDEFVER':'', 'PRIHABTXT':'', 'PRIDET':'', 'INTERPQUAL':'', 'PRIDETCOM':'', 'PHABFEANOT':'',
		'SOURCE1TXT':'', 'S1CAPTDATE':'', 'S1HABCLASS':'', 'S1HABTYPE':'', 'S1BOUNDARY':'', 'S1HABID':'',
		'SOURCE2TXT':'', 'S2CAPTDATE':'', 'S2HABCLASS':'', 'S2HABTYPE':'', 'S2BOUNDARY':'', 'S2HABID':'',
		'SOURCE3TXT':'', 'S3CAPTDATE':'', 'S3HABCLASS':'', 'S3HABTYPE':'', 'S3BOUNDARY':'', 'S3HABID':'' };

	$.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
		createPickerDiv: function(idresults, position, callback) {
			var toReturn = $('<div>');
			var res = idresults.results;
			for (var i in res) {
				var resultsList = $('<ul>');
				var resultDiv = $("<div>")
					.addClass("result")
					.append($("<div>" + res[i].layerName + " - " + res[i].value + "</div>")
						.addClass("title")
					)
					.append(resultsList)
					.appendTo(toReturn);

				for (var j in res[i].attributes) {
					if (res[i].attributes[j] !== "" && !(j in _NODISPLAY) && !(res[i].layerName.indexOf("NE_") != -1 && !(j in _NEFILTER))) {
						resultsList.append($('<li>')
							.html(j + " : " + res[i].attributes[j])
						);
					}
				}
			}
			if(res.length==0)
				toReturn.append($('<div>No Results here</div>'));
			callback(toReturn);
		}
	}));
}