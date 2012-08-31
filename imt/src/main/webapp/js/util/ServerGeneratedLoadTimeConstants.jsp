/**
*
* @author		:- Christopher Johnson
* @date			:- 13th-May-2011
* @description	:- This JScript obtains a set of properties which are loaded from the server at load time.
*	In order to avoid jsp tags scattered over the entire application, all server specific properties are loaded in this JScript.
*	This keeps the design cleaner and avoids possible mistakes of voiding the entire application unusable.
*	Particular care must be taken with this file to ensure that it will validly pass through the YUI minifier.
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.ServerGeneratedLoadTimeConstants = <%=nbn.webmapping.json.loadtime.ServerGeneratedLoadTimeConstantsGenerator.getConstantsAsJSONString(request)%>;