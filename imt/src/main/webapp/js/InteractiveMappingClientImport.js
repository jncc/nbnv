/**
*
* @author       :- Christopher Johnson
* @date         :- 16th-May-2011
* @description  :- This JScript imports all the files required for the IMT to load. 
*	The reason for this file existing is so that the ServerGeneratedLoadTimeConstants.jsp 
*	could be imported without making the entire IMT a giant JSP file. Doing so made it 
*	possible to hit the 65535 byte limit of a JSP.
* @dependencies :- Absolutely nothing.
*/
(function() {
	function loadScript(src) {
		document.write('<script src="' + src + '" type="text/javascript"></script>');
	};
	function loadCSS(src) {
		document.write('<link href="' + src + '" rel="stylesheet" type="text/css">');
	};
	loadCSS('http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/start/jquery-ui.css');
	loadCSS('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.0/css/jquery.dataTables.css');
	loadCSS('css/InteractiveMappingClient.css');
	
	loadScript('http://ecn.dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6.2&mkt=en-us');
	loadScript('http://openlayers.org/api/2.10/OpenLayers.js');
	loadScript('http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js');
	loadScript('https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui.min.js');
	loadScript('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.0/jquery.dataTables.min.js');
	loadScript('js/util/ServerGeneratedLoadTimeConstants.jsp');
	loadScript('js/InteractiveMappingClient.js');	
})();