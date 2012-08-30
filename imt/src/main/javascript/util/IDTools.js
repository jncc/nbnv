/**
*
* @author		:- Christopher Johnson
* @date			:- 23th-August-2011
* @description	:- This JScript generates ID's which are guarenteed to be unique within the IMT
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.IDTools = new function() {
	var idCounter = 0;
	this.generateUniqueID = function() {
		return "IMT-UniqueID-" + idCounter++;
	}
};