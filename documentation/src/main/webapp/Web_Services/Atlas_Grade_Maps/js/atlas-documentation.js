$(document).ready(function(){
	function createURLQuerySample(container) {
		var toReturn = { contentStart: $('<h4>').html('Generated Examples')};
		container.append(toReturn.contentStart)
			.append($('<div>').addClass('codeExample')
				.append($('<div>')
					.append($('<div>').addClass('label').html('Sample URL:'))
					.append( toReturn.url = $('<a>') )
				)
				.append($('<div>')
					.append($('<div>').addClass('label').html('Sample Query String:'))
					.append( toReturn.query = $('<span>') ) 
				)
			);
		return toReturn;
	}
	
	(function() {
		var _example = createURLQuerySample($('#interactive-md5Generator'));
		_example.contentStart.before($('<div>').nbn_loginGenerator({generate: function(evt, response) {
			var joinedResponse = nbn.util.ArrayTools.fromObject(response).join('&'); _example.query.html(joinedResponse);
			var URL = 'https://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/atlas/circle/map?' + joinedResponse;
			_example.url.html(URL).attr('href',URL);
		}}));
	})();
	
	(function() {
		var _example = createURLQuerySample($('#interactive-colourGenerator'));
		_example.contentStart.before($('<div>').nbn_colourPicker({
			change: function(evt, hex) {
				_example.query.html('fillcolour=' + hex + ' or outlinecolour=' + hex);
				var URL = 'https://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/atlas/circle/map?fillcolour=' + hex;
				_example.url.html(URL).attr('href',URL);
			}
		}));
	})();
});