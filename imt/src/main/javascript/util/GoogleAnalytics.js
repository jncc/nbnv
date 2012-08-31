/**
*
* @author       :- Christopher Johnson
* @date         :- 4th-July-2011
* @description  :- This script enables the IMT inital loading to be registered by Google Analytics 
* @dependencies :- Absolutely nothing.
*/
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-1444605-1']); //setting the NBN's Urchin tracker
_gaq.push(['_trackPageview']);

(function() {
	var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();