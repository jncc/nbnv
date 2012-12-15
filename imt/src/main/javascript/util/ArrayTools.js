/**
*
* @author		:- Christopher Johnson
* @date			:- 20-Jan-2011
* @description	:-
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ArrayTools = new function() {
	this.unique = function(array) { 
		var objectForResults = {};
		var result = [];
		for(var i=0; i<array.length;i++) 
			objectForResults[array[i]] = array[i];
		for(var i in objectForResults) 
			result.push(objectForResults[i]);
		return result;
	};
	
	this.flatten = function(arrayOfArrays) {
		if(arrayOfArrays.length>0)
			return Array.prototype.concat.apply(arrayOfArrays[0],arrayOfArrays.slice(1));
		else
			return [];
	};
	
	this.uniqueFlatten = function(arrayOfArrays) {
		return this.unique(this.flatten(arrayOfArrays));
	};
	
	this.getIndexOfByFunction = function(toSearch,wanted, searchFunc) { //this is an array search function, it can't be added as a property of the array object as this will violate the for in loop
		for(var i in toSearch) {
			if(searchFunc(toSearch[i]) == wanted)
				return i;
		}
		return -1;
	};
	
	this.getArrayByFunction = function(array, elementFunction) { //this function will return an array of element which are derived by passing each element of the array into the element function
		var toReturn = [];
		for(var i in array)
			toReturn.push(elementFunction(array[i]));
		return toReturn;
	};
	
	this.getElementByFunction = function(toSearch,wanted, searchFunc) {
		return toSearch[this.getIndexOfByFunction(toSearch,wanted, searchFunc)];
	};
	
	this.removeFromByFunction = function(arrayToRemoveFrom, toRemove, searchFunc) {
		return this.removeFromPosition(this.getIndexOfByFunction(arrayToRemoveFrom, toRemove, searchFunc), arrayToRemoveFrom);
	};
	
	this.removeFromPosition = function(position, arrayToRemoveFrom) {
		if(arrayToRemoveFrom.length > position && position >= 0) {
			var toReturn = arrayToRemoveFrom[position];
			arrayToRemoveFrom.splice(position,1); //remove the element
			return toReturn;
		}
		return false;
	};
	
	this.removeFrom = function(toRemove, arrayToRemoveFrom) {
		return this.removeFromPosition($.inArray(toRemove,arrayToRemoveFrom),arrayToRemoveFrom);
	};
	
	this.toJQueryRenderedList = function(toRender, renderingFunction) {
		var toReturn = $('<ul>');
		renderingFunction = renderingFunction || function(element) { return element; }; //if no render function has been defined, create a passthrough function
                for(var i in toRender) {
			var renderedResult = renderingFunction(toRender[i], i);
			if(renderedResult)
				toReturn.append($('<li>').append(renderedResult));
		}
		return toReturn;
	};
	
	this.fromObject = function(obj) {
		var toReturn = [];
		for(var i in obj) 
			toReturn.push(i + '=' + obj[i]);
		return toReturn;
	};
	
	this.joinAndPrepend = function(arr, seperatorSym, prependSym) {
		var toReturn = arr.join(seperatorSym);
		if(arr.length)
			toReturn = (prependSym || seperatorSym) + toReturn;
		return toReturn;
	};
	
	this.equals = function(arrA, arrB) {
		if(arrA.length == arrB.length) {
			for(var i in arrA) {
				if(arrA[i] != arrB[i])
					return false;
			}
			return true;
		}
		else
			return false;
	};
};