/**
*
* @author		:- Christopher Johnson
* @date			:- 25-Feb-2011
* @description	:- This JScript will split a given jquery element into multiple parts. With its children spread equally over those elements
*
* @usage		:- Just import
* @dependencies	:-
*	jQuery
*/
(function($, undefined) {
	$.fn.split = function(amountOfParts) {
		amountOfParts = (amountOfParts < 1) ? 1 : parseInt(amountOfParts);
		var element = $(this[0]).clone(); //get element as jquery but leave original intact
		var childrenToSplit = element.children().remove();
		var elementsPerColumn = Math.ceil(childrenToSplit.length/amountOfParts);
		var toReturn = $();
		var childrenLeft = childrenToSplit.length;
		while(childrenLeft>0) {
			toReturn = toReturn.add(element.clone(false).append(childrenToSplit.splice(0, (elementsPerColumn>childrenLeft) ? childrenLeft: elementsPerColumn)));
			childrenLeft-=elementsPerColumn;
		}
		return toReturn;
	};
})(jQuery);