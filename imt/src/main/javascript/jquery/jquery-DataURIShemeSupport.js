/**
*
* @author		:- Christopher Johnson
* @date			:- 17-Feb-2011
* @description	:- This JScript will extend the feature detection capabilities of jquery and detect 
*	weather or not it is possible to use data uri schemes for loading images
*
* @usage		:- Just import
* @dependencies	:-
*	jQuery
*/
(function($, undefined) {
	var data = new Image();
	data.onload = data.onerror = function() {
		$.support.imageUri = (this.width == 1 && this.height == 1);
	}
	data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
})(jQuery);

