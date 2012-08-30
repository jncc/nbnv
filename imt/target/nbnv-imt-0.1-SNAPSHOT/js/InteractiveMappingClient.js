/**
* @description The following is a jQuery plugin which will ease namespacing of 
*	objects which are not part of jQuery.
* @author Christopher Johnson
* @dependancies jQuery
* @usage The following are sample usages of this namespacing functionality in javascript and coffeescript.
*	<code lang="javascript" filename="my/uber/awesome/namespaced/Object.js">
*		(function() {
*			$.namespace("my.uber.awesome.namespaced.Object", {
*				someMethod: function() {
*					return "Hurray, I work";				
*				}
*			});
*		})();
*	</code>
*
*	<code lang="coffeescript" filename="my/uber/awesome/namespaced/Object.coffeescript">
*		do ->
*			$.namespace "my.uber.awesome.namespaced.Object"
*				someMethod: ->	"Hurray. I Work";
*	</code>
*/
(function($, undefined) {
	$.namespace = function(name, entity) {
		var i, packages = name.split('.'), currPackage = window; //split the namespace and store the global context
		for(i=0; i<packages.length-1; i++)
			currPackage = (currPackage[packages[i]] = currPackage[packages[i]] || {}); //define and assign
		currPackage[packages[i]] = entity; //set the entity to the final namespace
	};
})(jQuery);
 /*
 * TipTip
 * Copyright 2010 Drew Wilson
 * www.drewwilson.com
 * code.drewwilson.com/entry/tiptip-jquery-plugin
 *
 * Version 1.3   -   Updated: Mar. 23, 2010
 *
 * This Plug-In will create a custom tooltip to replace the default
 * browser tooltip. It is extremely lightweight and very smart in
 * that it detects the edges of the browser window and will make sure
 * the tooltip stays within the current window size. As a result the
 * tooltip will adjust itself to be displayed above, below, to the left 
 * or to the right depending on what is necessary to stay within the
 * browser window. It is completely customizable as well via CSS.
 *
 * This TipTip jQuery plug-in is dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */

(function($){
	$.fn.tipTip = function(options) {
		var defaults = { 
			activation: "hover",
			keepAlive: false,
			maxWidth: "200px",
			edgeOffset: 3,
			defaultPosition: "bottom",
			delay: 400,
			fadeIn: 200,
			fadeOut: 200,
			attribute: "title",
			content: false, // HTML or String to fill TipTIp with
		  	enter: function(){},
		  	exit: function(){}
	  	};
	 	var opts = $.extend(defaults, options);
	 	
	 	// Setup tip tip elements and render them to the DOM
	 	if($("#tiptip_holder").length <= 0){
	 		var tiptip_holder = $('<div id="tiptip_holder" style="max-width:'+ opts.maxWidth +';"></div>');
			var tiptip_content = $('<div id="tiptip_content"></div>');
			var tiptip_arrow = $('<div id="tiptip_arrow"></div>');
			$("body").append(tiptip_holder.html(tiptip_content).prepend(tiptip_arrow.html('<div id="tiptip_arrow_inner"></div>')));
		} else {
			var tiptip_holder = $("#tiptip_holder");
			var tiptip_content = $("#tiptip_content");
			var tiptip_arrow = $("#tiptip_arrow");
		}
		
		return this.each(function(){
			var org_elem = $(this);
			if(opts.content){
				var org_title = opts.content;
			} else {
				var org_title = org_elem.attr(opts.attribute);
			}
			if(org_title != ""){
				if(!opts.content){
					org_elem.removeAttr(opts.attribute); //remove original Attribute
				}
				var timeout = false;
				
				if(opts.activation == "hover"){
					org_elem.hover(function(){
						active_tiptip();
					}, function(){
						if(!opts.keepAlive){
							deactive_tiptip();
						}
					});
					if(opts.keepAlive){
						tiptip_holder.hover(function(){}, function(){
							deactive_tiptip();
						});
					}
				} else if(opts.activation == "focus"){
					org_elem.focus(function(){
						active_tiptip();
					}).blur(function(){
						deactive_tiptip();
					});
				} else if(opts.activation == "click"){
					org_elem.click(function(){
						active_tiptip();
						return false;
					}).hover(function(){},function(){
						if(!opts.keepAlive){
							deactive_tiptip();
						}
					});
					if(opts.keepAlive){
						tiptip_holder.hover(function(){}, function(){
							deactive_tiptip();
						});
					}
				}
			
				function active_tiptip(){
					opts.enter.call(this);
					tiptip_content.html(org_title);
					tiptip_holder.hide().removeAttr("class").css("margin","0");
					tiptip_arrow.removeAttr("style");
					
					var top = parseInt(org_elem.offset()['top']);
					var left = parseInt(org_elem.offset()['left']);
					var org_width = parseInt(org_elem.outerWidth());
					var org_height = parseInt(org_elem.outerHeight());
					var tip_w = tiptip_holder.outerWidth();
					var tip_h = tiptip_holder.outerHeight();
					var w_compare = Math.round((org_width - tip_w) / 2);
					var h_compare = Math.round((org_height - tip_h) / 2);
					var marg_left = Math.round(left + w_compare);
					var marg_top = Math.round(top + org_height + opts.edgeOffset);
					var t_class = "";
					var arrow_top = "";
					var arrow_left = Math.round(tip_w - 12) / 2;

                    if(opts.defaultPosition == "bottom"){
                    	t_class = "_bottom";
                   	} else if(opts.defaultPosition == "top"){ 
                   		t_class = "_top";
                   	} else if(opts.defaultPosition == "left"){
                   		t_class = "_left";
                   	} else if(opts.defaultPosition == "right"){
                   		t_class = "_right";
                   	}
					
					var right_compare = (w_compare + left) < parseInt($(window).scrollLeft());
					var left_compare = (tip_w + left) > parseInt($(window).width());
					
					if((right_compare && w_compare < 0) || (t_class == "_right" && !left_compare) || (t_class == "_left" && left < (tip_w + opts.edgeOffset + 5))){
						t_class = "_right";
						arrow_top = Math.round(tip_h - 13) / 2;
						arrow_left = -12;
						marg_left = Math.round(left + org_width + opts.edgeOffset);
						marg_top = Math.round(top + h_compare);
					} else if((left_compare && w_compare < 0) || (t_class == "_left" && !right_compare)){
						t_class = "_left";
						arrow_top = Math.round(tip_h - 13) / 2;
						arrow_left =  Math.round(tip_w);
						marg_left = Math.round(left - (tip_w + opts.edgeOffset + 5));
						marg_top = Math.round(top + h_compare);
					}

					var top_compare = (top + org_height + opts.edgeOffset + tip_h + 8) > parseInt($(window).height() + $(window).scrollTop());
					var bottom_compare = ((top + org_height) - (opts.edgeOffset + tip_h + 8)) < 0;
					
					if(top_compare || (t_class == "_bottom" && top_compare) || (t_class == "_top" && !bottom_compare)){
						if(t_class == "_top" || t_class == "_bottom"){
							t_class = "_top";
						} else {
							t_class = t_class+"_top";
						}
						arrow_top = tip_h;
						marg_top = Math.round(top - (tip_h + 5 + opts.edgeOffset));
					} else if(bottom_compare | (t_class == "_top" && bottom_compare) || (t_class == "_bottom" && !top_compare)){
						if(t_class == "_top" || t_class == "_bottom"){
							t_class = "_bottom";
						} else {
							t_class = t_class+"_bottom";
						}
						arrow_top = -12;						
						marg_top = Math.round(top + org_height + opts.edgeOffset);
					}
				
					if(t_class == "_right_top" || t_class == "_left_top"){
						marg_top = marg_top + 5;
					} else if(t_class == "_right_bottom" || t_class == "_left_bottom"){		
						marg_top = marg_top - 5;
					}
					if(t_class == "_left_top" || t_class == "_left_bottom"){	
						marg_left = marg_left + 5;
					}
					tiptip_arrow.css({"margin-left": arrow_left+"px", "margin-top": arrow_top+"px"});
					tiptip_holder.css({"margin-left": marg_left+"px", "margin-top": marg_top+"px"}).attr("class","tip"+t_class);
					
					if (timeout){ clearTimeout(timeout); }
					timeout = setTimeout(function(){ tiptip_holder.stop(true,true).fadeIn(opts.fadeIn); }, opts.delay);	
				}
				
				function deactive_tiptip(){
					opts.exit.call(this);
					if (timeout){ clearTimeout(timeout); }
					tiptip_holder.fadeOut(opts.fadeOut);
				}
			}				
		});
	}
})(jQuery);  	
	
	/**
	 * jQuery MD5 hash algorithm function
	 * 
	 * 	<code>
	 * 		Calculate the md5 hash of a String 
	 * 		String $.md5 ( String str )
	 * 	</code>
	 * 
	 * Calculates the MD5 hash of str using the » RSA Data Security, Inc. MD5 Message-Digest Algorithm, and returns that hash. 
	 * MD5 (Message-Digest algorithm 5) is a widely-used cryptographic hash function with a 128-bit hash value. MD5 has been employed in a wide variety of security applications, and is also commonly used to check the integrity of data. The generated hash is also non-reversable. Data cannot be retrieved from the message digest, the digest uniquely identifies the data.
	 * MD5 was developed by Professor Ronald L. Rivest in 1994. Its 128 bit (16 byte) message digest makes it a faster implementation than SHA-1.
	 * This script is used to process a variable length message into a fixed-length output of 128 bits using the MD5 algorithm. It is fully compatible with UTF-8 encoding. It is very useful when u want to transfer encrypted passwords over the internet. If you plan using UTF-8 encoding in your project don't forget to set the page encoding to UTF-8 (Content-Type meta tag). 
	 * This function orginally get from the WebToolkit and rewrite for using as the jQuery plugin.
	 * 
	 * Example
	 * 	Code
	 * 		<code>
	 * 			$.md5("I'm Persian."); 
	 * 		</code>
	 * 	Result
	 * 		<code>
	 * 			"b8c901d0f02223f9761016cfff9d68df"
	 * 		</code>
	 * 
	 * @alias Muhammad Hussein Fattahizadeh < muhammad [AT] semnanweb [DOT] com >
	 * @link http://www.semnanweb.com/jquery-plugin/md5.html
	 * @see http://www.webtoolkit.info/
	 * @license http://www.gnu.org/licenses/gpl.html [GNU General Public License]
	 * @param {jQuery} {md5:function(string))
	 * @return string
	 */
	
	(function($){
		
		var rotateLeft = function(lValue, iShiftBits) {
			return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
		}
		
		var addUnsigned = function(lX, lY) {
			var lX4, lY4, lX8, lY8, lResult;
			lX8 = (lX & 0x80000000);
			lY8 = (lY & 0x80000000);
			lX4 = (lX & 0x40000000);
			lY4 = (lY & 0x40000000);
			lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
			if (lX4 & lY4) return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
			if (lX4 | lY4) {
				if (lResult & 0x40000000) return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
				else return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
			} else {
				return (lResult ^ lX8 ^ lY8);
			}
		}
		
		var F = function(x, y, z) {
			return (x & y) | ((~ x) & z);
		}
		
		var G = function(x, y, z) {
			return (x & z) | (y & (~ z));
		}
		
		var H = function(x, y, z) {
			return (x ^ y ^ z);
		}
		
		var I = function(x, y, z) {
			return (y ^ (x | (~ z)));
		}
		
		var FF = function(a, b, c, d, x, s, ac) {
			a = addUnsigned(a, addUnsigned(addUnsigned(F(b, c, d), x), ac));
			return addUnsigned(rotateLeft(a, s), b);
		};
		
		var GG = function(a, b, c, d, x, s, ac) {
			a = addUnsigned(a, addUnsigned(addUnsigned(G(b, c, d), x), ac));
			return addUnsigned(rotateLeft(a, s), b);
		};
		
		var HH = function(a, b, c, d, x, s, ac) {
			a = addUnsigned(a, addUnsigned(addUnsigned(H(b, c, d), x), ac));
			return addUnsigned(rotateLeft(a, s), b);
		};
		
		var II = function(a, b, c, d, x, s, ac) {
			a = addUnsigned(a, addUnsigned(addUnsigned(I(b, c, d), x), ac));
			return addUnsigned(rotateLeft(a, s), b);
		};
		
		var convertToWordArray = function(string) {
			var lWordCount;
			var lMessageLength = string.length;
			var lNumberOfWordsTempOne = lMessageLength + 8;
			var lNumberOfWordsTempTwo = (lNumberOfWordsTempOne - (lNumberOfWordsTempOne % 64)) / 64;
			var lNumberOfWords = (lNumberOfWordsTempTwo + 1) * 16;
			var lWordArray = Array(lNumberOfWords - 1);
			var lBytePosition = 0;
			var lByteCount = 0;
			while (lByteCount < lMessageLength) {
				lWordCount = (lByteCount - (lByteCount % 4)) / 4;
				lBytePosition = (lByteCount % 4) * 8;
				lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
				lByteCount++;
			}
			lWordCount = (lByteCount - (lByteCount % 4)) / 4;
			lBytePosition = (lByteCount % 4) * 8;
			lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
			lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
			lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
			return lWordArray;
		};
		
		var wordToHex = function(lValue) {
			var WordToHexValue = "", WordToHexValueTemp = "", lByte, lCount;
			for (lCount = 0; lCount <= 3; lCount++) {
				lByte = (lValue >>> (lCount * 8)) & 255;
				WordToHexValueTemp = "0" + lByte.toString(16);
				WordToHexValue = WordToHexValue + WordToHexValueTemp.substr(WordToHexValueTemp.length - 2, 2);
			}
			return WordToHexValue;
		};
		
		var uTF8Encode = function(string) {
			string = string.replace(/\x0d\x0a/g, "\x0a");
			var output = "";
			for (var n = 0; n < string.length; n++) {
				var c = string.charCodeAt(n);
				if (c < 128) {
					output += String.fromCharCode(c);
				} else if ((c > 127) && (c < 2048)) {
					output += String.fromCharCode((c >> 6) | 192);
					output += String.fromCharCode((c & 63) | 128);
				} else {
					output += String.fromCharCode((c >> 12) | 224);
					output += String.fromCharCode(((c >> 6) & 63) | 128);
					output += String.fromCharCode((c & 63) | 128);
				}
			}
			return output;
		};
		
		$.extend({
			md5: function(string) {
				var x = Array();
				var k, AA, BB, CC, DD, a, b, c, d;
				var S11=7, S12=12, S13=17, S14=22;
				var S21=5, S22=9 , S23=14, S24=20;
				var S31=4, S32=11, S33=16, S34=23;
				var S41=6, S42=10, S43=15, S44=21;
				string = uTF8Encode(string);
				x = convertToWordArray(string);
				a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
				for (k = 0; k < x.length; k += 16) {
					AA = a; BB = b; CC = c; DD = d;
					a = FF(a, b, c, d, x[k+0],  S11, 0xD76AA478);
					d = FF(d, a, b, c, x[k+1],  S12, 0xE8C7B756);
					c = FF(c, d, a, b, x[k+2],  S13, 0x242070DB);
					b = FF(b, c, d, a, x[k+3],  S14, 0xC1BDCEEE);
					a = FF(a, b, c, d, x[k+4],  S11, 0xF57C0FAF);
					d = FF(d, a, b, c, x[k+5],  S12, 0x4787C62A);
					c = FF(c, d, a, b, x[k+6],  S13, 0xA8304613);
					b = FF(b, c, d, a, x[k+7],  S14, 0xFD469501);
					a = FF(a, b, c, d, x[k+8],  S11, 0x698098D8);
					d = FF(d, a, b, c, x[k+9],  S12, 0x8B44F7AF);
					c = FF(c, d, a, b, x[k+10], S13, 0xFFFF5BB1);
					b = FF(b, c, d, a, x[k+11], S14, 0x895CD7BE);
					a = FF(a, b, c, d, x[k+12], S11, 0x6B901122);
					d = FF(d, a, b, c, x[k+13], S12, 0xFD987193);
					c = FF(c, d, a, b, x[k+14], S13, 0xA679438E);
					b = FF(b, c, d, a, x[k+15], S14, 0x49B40821);
					a = GG(a, b, c, d, x[k+1],  S21, 0xF61E2562);
					d = GG(d, a, b, c, x[k+6],  S22, 0xC040B340);
					c = GG(c, d, a, b, x[k+11], S23, 0x265E5A51);
					b = GG(b, c, d, a, x[k+0],  S24, 0xE9B6C7AA);
					a = GG(a, b, c, d, x[k+5],  S21, 0xD62F105D);
					d = GG(d, a, b, c, x[k+10], S22, 0x2441453);
					c = GG(c, d, a, b, x[k+15], S23, 0xD8A1E681);
					b = GG(b, c, d, a, x[k+4],  S24, 0xE7D3FBC8);
					a = GG(a, b, c, d, x[k+9],  S21, 0x21E1CDE6);
					d = GG(d, a, b, c, x[k+14], S22, 0xC33707D6);
					c = GG(c, d, a, b, x[k+3],  S23, 0xF4D50D87);
					b = GG(b, c, d, a, x[k+8],  S24, 0x455A14ED);
					a = GG(a, b, c, d, x[k+13], S21, 0xA9E3E905);
					d = GG(d, a, b, c, x[k+2],  S22, 0xFCEFA3F8);
					c = GG(c, d, a, b, x[k+7],  S23, 0x676F02D9);
					b = GG(b, c, d, a, x[k+12], S24, 0x8D2A4C8A);
					a = HH(a, b, c, d, x[k+5],  S31, 0xFFFA3942);
					d = HH(d, a, b, c, x[k+8],  S32, 0x8771F681);
					c = HH(c, d, a, b, x[k+11], S33, 0x6D9D6122);
					b = HH(b, c, d, a, x[k+14], S34, 0xFDE5380C);
					a = HH(a, b, c, d, x[k+1],  S31, 0xA4BEEA44);
					d = HH(d, a, b, c, x[k+4],  S32, 0x4BDECFA9);
					c = HH(c, d, a, b, x[k+7],  S33, 0xF6BB4B60);
					b = HH(b, c, d, a, x[k+10], S34, 0xBEBFBC70);
					a = HH(a, b, c, d, x[k+13], S31, 0x289B7EC6);
					d = HH(d, a, b, c, x[k+0],  S32, 0xEAA127FA);
					c = HH(c, d, a, b, x[k+3],  S33, 0xD4EF3085);
					b = HH(b, c, d, a, x[k+6],  S34, 0x4881D05);
					a = HH(a, b, c, d, x[k+9],  S31, 0xD9D4D039);
					d = HH(d, a, b, c, x[k+12], S32, 0xE6DB99E5);
					c = HH(c, d, a, b, x[k+15], S33, 0x1FA27CF8);
					b = HH(b, c, d, a, x[k+2],  S34, 0xC4AC5665);
					a = II(a, b, c, d, x[k+0],  S41, 0xF4292244);
					d = II(d, a, b, c, x[k+7],  S42, 0x432AFF97);
					c = II(c, d, a, b, x[k+14], S43, 0xAB9423A7);
					b = II(b, c, d, a, x[k+5],  S44, 0xFC93A039);
					a = II(a, b, c, d, x[k+12], S41, 0x655B59C3);
					d = II(d, a, b, c, x[k+3],  S42, 0x8F0CCC92);
					c = II(c, d, a, b, x[k+10], S43, 0xFFEFF47D);
					b = II(b, c, d, a, x[k+1],  S44, 0x85845DD1);
					a = II(a, b, c, d, x[k+8],  S41, 0x6FA87E4F);
					d = II(d, a, b, c, x[k+15], S42, 0xFE2CE6E0);
					c = II(c, d, a, b, x[k+6],  S43, 0xA3014314);
					b = II(b, c, d, a, x[k+13], S44, 0x4E0811A1);
					a = II(a, b, c, d, x[k+4],  S41, 0xF7537E82);
					d = II(d, a, b, c, x[k+11], S42, 0xBD3AF235);
					c = II(c, d, a, b, x[k+2],  S43, 0x2AD7D2BB);
					b = II(b, c, d, a, x[k+9],  S44, 0xEB86D391);
					a = addUnsigned(a, AA);
					b = addUnsigned(b, BB);
					c = addUnsigned(c, CC);
					d = addUnsigned(d, DD);
				}
				var tempValue = wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d);
				return tempValue.toLowerCase();
			}
		});
	})(jQuery);
/**
*
* @author		:- Christopher Johnson
* @date			:- 25-Feb-2011
* @description	:- This JScript will extend the capabilites of Jquery and create a function 
* that enables an element to be measured before it is added to the DOM
*
* @usage		:- Just import
* @dependencies	:-
*	jQuery
*/
(function($, undefined) {
	var _createToReturn = function(element) {
		return { //store all the values that I want to return
			width: element.width(),
			height: element.height(),
			outerWidth : element.outerWidth(),
			outerHeight: element.outerHeight(),
			position: element.position()
		};
	};
	
	$.fn.measure = function(constraints) {
		var element = $(this[0]).clone(false); //clone the element I shall perform this method on
		constraints = constraints || {};
		element.height(constraints.height || 'auto');
		element.width(constraints.width || 'auto');
		
		var body = $('body'), hiddenElement = $('<div>')
			.css({
				position: 'absolute',
				top: 0,
				left: -body.width() //draw the element off screen
			})
			.append(element); //add the cloned element

		if(this.is(':attached')) //is this element attached to the dom
			$(this).before(hiddenElement); //attach before to retain stylings
		else
			body.append(hiddenElement); //just render offscreen. Results may be approximate
		
		var toReturn = _createToReturn(element);
		hiddenElement.remove(); //remove the temp element and therefore the cloned element
		return toReturn;
	}
})(jQuery);
/**
*
* @author		:- Christopher Johnson
* @date			:- 21-Feb-2011
* @description	:- This JScript will work out weather or not an element is attached to the dom
* @usage		:- Just import
* @dependencies	:-
*	jquery
*/
(function( $, undefined ) {
	var _isAttached = function(_me) {
		return $.contains(document.documentElement, _me);
	};
	
	$.extend( $.expr[':'], {
		attached: _isAttached,
		detached: function(_me){ 
			return !_isAttached( _me ); 
		}
	});
})(jQuery);
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


/**
*
* @author		:- Christopher Johnson
* @date			:- 5th-May-2011
* @description	:- This JScript enables CSS properties to be set with functions, these are then continually called and set.
*	It should be noted that this jQuery plugin was specifically written to support css properties which were missing from old browsers.
*	Therefore this plugin should only be used if it is not possible or deemed incorrect to hook into the necessary events which 
*	are required to support, your dynamic css property.
*/
(function($, undefined) {
	$.fn.cssExpression = function(property, expression) {
		$(this).each(function() {
			var _me = $(this);
			var runningExpressions = _me.data('runningExpressions') || {};
			if(runningExpressions[property])//check if an expression is already running for this property
				clearInterval(runningExpressions[property]); //stop it
			_me.css(property, expression.call(_me[0])); //run once
			runningExpressions[property] = setInterval(function() { //set up a function to loop around
				_me.css(property, expression.call(_me[0]));
			},1000);
			_me.data('runningExpressions', runningExpressions); //store the expressions object
		});
	};
})(jQuery);
/**
*
* @author		:- Christopher Johnson
* @date			:- 5th-May-2011
* @description	:- This JScript will add support for Conflicting Absolute positions in browsers which do not support them.
*	An example of this is when an element has been styled without a height but does have top and bottom positions.
*	Specifically written to enable support for IE6, this fix may work for other browsers which do not support conflicting absolute positions
*	however this is untested at the time of writting.
* @dependencies	:-
*	jquery-cssExpressions
*/
(function($, undefined) {
	$(document).ready(function() {
		var container = $('<div>').css({position: 'absolute',width:10,height:10}).appendTo($('body'));
		var toCheck = $('<div>').css({position: 'absolute',top:0,bottom:0,width:10}).appendTo(container);
		$.support.conflictingAbsolutePositions = toCheck.height() === 10; //perform the support check
		container.remove(); //remove the unwanted container
		
		if(!$.support.conflictingAbsolutePositions) {
			function _hasValueSet(value) {
				return value && value.length > 0 && value !== 'auto';
			};
			
			function _createCSSConflictHookFix(fixObj) {
				$.each(fixObj.conflictProperties, function(index, toFixValue){
					$.cssHooks[toFixValue] = {
						set: function(elem, value){
							elem.style[toFixValue] = value; //set the value initially
							var _me = $(elem);
							for(var i in fixObj.conflictProperties) {
								if(!_hasValueSet(_me.css(fixObj.conflictProperties[i]))) //if no value is set, there is no need to fix
									return;
							}
							fixObj.fix(_me);//failed to break out of loop, I am in a conflicted state which needs to be fixed
						}
					};
					$.fx.step[toFixValue] = function(fx) { //fix for animate too
						$.cssHooks[toFixValue].set(fx.elem, fx.now + fx.unit);
					};
				});
			};

			_createCSSConflictHookFix({
				conflictProperties: ['top', 'bottom'],
				fix: function(_me) {
					_me.cssExpression('height', function() {
						return this.parentElement.offsetHeight - parseInt(_me.css('top')) - parseInt(_me.css('bottom'));
					});
				}
			});
			
			_createCSSConflictHookFix({
				conflictProperties: ['left', 'right'],
				fix: function(_me) {
					_me.cssExpression('width', function() {
						return this.parentElement.offsetWidth - parseInt(_me.css('left')) - parseInt(_me.css('right'));
					});
				}
			});
		}
	});
})(jQuery);
/**
*
* @author		:- Christopher Johnson
* @date			:- 5th-May-2011
* @description	:- This JScript will add support for the css max height value when the browser this 
*	plugin is running is does not support them.
* @dependencies	:-
*	jquery-cssExpressions
*/
(function($, undefined) {
	$(document).ready(function() {
		var toCheck = $('<div>').css({position: 'absolute',width:10,height:10, maxHeight:5}).appendTo($('body'));
		$.support.maxHeight = toCheck.height() === 5; //perform the support check
		toCheck.remove(); //remove the unwanted container
		
		if(!$.support.maxHeight) {
			$.cssHooks.maxHeight = {
				set: function(elem, value){
					value = parseInt(value);
					$(elem).cssExpression('height', function() {
						return this.scrollHeight > value ? value : 'auto';
					});
				}
			};
			
			$.fx.step.maxHeight = function(fx) { //fix for animate too
				$.cssHooks.maxHeight.set(fx.elem, fx.now + fx.unit);
			};
		}
	});
})(jQuery);
/**
*
* @author       :- Christopher Johnson
* @date         :- 24th-June-2011
* @description  :- This JScript creates a static grid manager which wraps up the mapping manager 
*	and provides four areas around it which elements can be added to.
*/
window.nbn = window.nbn || {};
nbn.layout = nbn.layout || {};

nbn.layout.StaticGridLayoutManager = function(centrePositionLayoutManager) {
	var _viewpoint;
	var _controlPositionMap = [];
	var _surroundingGridPositions = {};
	
	var _container = $('<div>').addClass('STATIC_GRID_CONTAINER')
		.append(_viewpoint = centrePositionLayoutManager.getLayoutContainer()
			.addClass('STATIC_GRID_VIEWPOINT')
		);
	
	this.getLayoutContainer = function() {
		return _container;
	};
	
	this.append = function(position, elementToAdd) {
		_controlPositionMap[position].append(elementToAdd); // use the mapping array to link back to the element to add to
	};

	this.validate = function(elementToValidate) { //either validate a specific element or all of them
		if(elementToValidate)
			_controlPositionMap[position].validate();
		else { //no element specified, validate the specific one
			for(var i in _surroundingGridPositions)
				_surroundingGridPositions[i].validate();
		}
	};
	
	this.ControlsPosition = new function() {
		var _creationFunctionManager = new function(_self) {
			function _storeControlPositionMap(name, mapTo) {
				_self[name] = _controlPositionMap.push(mapTo)-1;
			};
			
			this.createStaticGridAppendFunction = function(name,animationPropertiesGeneratorFunc,elementsToAnimate) {
				_storeControlPositionMap(name,_surroundingGridPositions[name] = {
					representation: $('<div>').addClass(name), //define the representation
					append: function(elementToAdd) {
						if(!$.contains(_container[0], this.representation[0])) //lazy add to the dom to reduce document size
							this.representation.appendTo(_container); 
						this.representation.append(elementToAdd.addClass(name + '_WIDGET'));
						this.validate();
					},					
					validate: function() {
						var animationProperties = animationPropertiesGeneratorFunc();
						for(var i in elementsToAnimate)
							elementsToAnimate[i].representation.css(animationProperties);
						_viewpoint.css(animationProperties); //always validate the viewpoint
						centrePositionLayoutManager.validate(); //validate the viewpoints mapping manager
					}
				});
			};
		}(this);
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_LEFT',function() {
			return {left : _surroundingGridPositions.STATIC_GRID_LEFT.representation.width()};
		});
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_RIGHT',function() {
			return {right :  _surroundingGridPositions.STATIC_GRID_RIGHT.representation.width()};
		});
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_HEADER',function() {
			return {top : _surroundingGridPositions.STATIC_GRID_HEADER.representation.height()};
		},[_surroundingGridPositions.STATIC_GRID_LEFT,_surroundingGridPositions.STATIC_GRID_RIGHT]);
		
		_creationFunctionManager.createStaticGridAppendFunction('STATIC_GRID_FOOTER',function() {
			return {bottom : _surroundingGridPositions.STATIC_GRID_FOOTER.representation.height()};
		},[_surroundingGridPositions.STATIC_GRID_LEFT,_surroundingGridPositions.STATIC_GRID_RIGHT]);
	};
};

/**
*
* @author		:- Christopher Johnson
* @date			:- 20-Jan-2011
* @description	:- This JScript defines the common behaviour for an observable object.
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.Observable = function() {
	var _listeners = [];
	
	this.notifyListeners = function(event) {
		var args = Array.prototype.slice.call(arguments, 1); //remove the first element from the arguments as that is the event name
		for(var i in _listeners) {
			if(_listeners[i][event] != undefined)
				_listeners[i][event].apply(this,args);
		}
	};
	
	this.ObservableMethods = {
		addListener: function(listener) {
			_listeners.push(listener);
		},
		removeListener: function(toRemove) {
			return nbn.util.ArrayTools.removeFrom(toRemove,_listeners);
		}
	};
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 19-Jan-2011
* @description	:- This JScript is capable of loading a image and notifying of its completion.
*	requesting an image during loading of an image will abort the initial loading and then start the next. This behaviour 
*	ensures that notification of 'completedLoading' for every 'startedLoading'
* @dependencies	:-
*	nbn.util.Observable
*	nbn.util.Logger
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.loading = nbn.util.loading || {};

nbn.util.loading.ImagePreLoader = function(logger) {
	var observer = new nbn.util.Observable();
	var _loading = false;
	var _image = new Image();
	
	var _loadComplete = this.stopLoading = _image.onload = _image.onabort = function() { //add the onload hook
		if(_loading) {
			_loading = false;
			observer.notifyListeners('completedLoading', _image);
		}
	}; 
	
	_image.onerror = function() { //handle any errors
		_loadComplete();
		if(logger)
			logger.error('nbn.util.loading.ImagePreLoader', 'Failed to load : ' +_image.src); //log the error
	};
	
	this.getImageElement = function() {
		return _image;
	};
	
	this.setSource = function(url) {
		_image.src = url;
		if(!_image.complete && !_loading) {
			_loading = true;
			observer.notifyListeners('startedLoading', _image);
		}
	};
	
	this.addLoadingListener = observer.ObservableMethods.addListener; //adapt the observable object
	this.removeLoadingListener = observer.ObservableMethods.removeListener; //adapt the observable object
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 19-Jan-2011
* @description	:- This JScript can listen to multiple Loading notifiers and consolodate all their activity into a single start/stop notification.
* A notification of an update in loading is provided also
* The Loading register is segmented into two parts:
*	- LoadingListener 	- which contains the listening functionality of this object
*	- Observable 		- which contains the methods that enable this loading register to be observerd
* @dependencies	:-
*	nbn.util.Observable
*	The dependancy is that the loading notifier will only notify of 'completedLoading' for every 'startedLoading'
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.loading = nbn.util.loading || {};

nbn.util.loading.LoadingConsolidator = function() {	
	var observer = new nbn.util.Observable();
		
	this.LoadingListener = new function() {
		var _currLoadingCount = 0;
		var _requestedLoadingCount = 0;
		
		var updateLoadingPosition = function() { //notify about change in completion
			if(_currLoadingCount!=_requestedLoadingCount) //has their been a completion percentage change?
				observer.notifyListeners('updatedLoading',1-(_currLoadingCount/_requestedLoadingCount));
		};
		
		this.startedLoading = function() {
			_requestedLoadingCount++;
			if(_currLoadingCount++ == 0)
				observer.notifyListeners('startedLoading');
			else
				updateLoadingPosition();//notify of updated loading
		};

		this.completedLoading = function() {
			if(--_currLoadingCount == 0) {
				observer.notifyListeners('completedLoading');
				_requestedLoadingCount = 0; //reset the loading count
			}
			else
				updateLoadingPosition();//notify of updated loading
		};
	};
	
	this.Observable = { //adapt the observable object
		addLoadingListener: observer.ObservableMethods.addListener,
		removeLoadingListener: observer.ObservableMethods.removeListener
	};
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 24-Jan-2011
* @description	:- This JScript can listen to multiple Loading notifiers which provide 
* update counts and notify of the average loading progress between all Loading Notifiers which are running.
* @dependencies	:-
*	nbn.util.loading.LoadingConsolidator
*	nbn.util.Observable
*	The dependancy is that the loading notifier will only notify of 'completedLoading' for every 'startedLoading'
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.loading = nbn.util.loading || {};

nbn.util.loading.AverageLoadingProgress = function() {
	var observer = new nbn.util.Observable(); //the observer to which I am going to adapt
	var _listeningObject = new Object();
	var _listenToTheLoadingOfIDCounter = 0;
	
	var _loadingProgressesListeningTo = [];
	
	var consolidator = new nbn.util.loading.LoadingConsolidator();
	consolidator.Observable.addLoadingListener({
		startedLoading: function() {
			observer.notifyListeners('startedLoading');
		},
		completedLoading: function() {
			_listeningObject = new Object();
			observer.notifyListeners('completedLoading');
		}
	});
	
	var _calculateMean = function() {
		var sigma = 0, count = 0;
		for(var i in _listeningObject) {
			sigma+=_listeningObject[i];
			count++;
		}
		return sigma/count;
	};
	
	var _notifyListeners = function() {
		observer.notifyListeners('updatedLoading',_calculateMean());
	};
	
	this.listenToTheLoadingOf = function(toListenTo) {
		var id = _listenToTheLoadingOfIDCounter++;
		toListenTo.addLoadingListener(consolidator.LoadingListener);
		var toListenTosLoadingListener = {
			startedLoading: function(currentPosition) {
				_listeningObject[id] = 0;
				_notifyListeners();
			},
			updatedLoading: function(currentPosition) {
				_listeningObject[id] = currentPosition;
				_notifyListeners();
			},
			completedLoading: function() {
				_listeningObject[id] = 1;
				_notifyListeners();
			}
		};
		
		_loadingProgressesListeningTo.push({
			loadingListener: toListenTo,
			averageLoadingProgressNotifier: toListenTosLoadingListener
		});
		
		toListenTo.addLoadingListener(toListenTosLoadingListener);
	};
	
	this.stopListeningToTheLoadingOf = function(removeListeningOf) {
		var toRemove = nbn.util.ArrayTools.removeFromByFunction(_loadingProgressesListeningTo, removeListeningOf, function(currElement) {
			return currElement.loadingListener;
		});
		
		if(toRemove) { //if the element to stop listening to exists
			toRemove.loadingListener.removeLoadingListener(consolidator.LoadingListener);
			toRemove.loadingListener.removeLoadingListener(toRemove.averageLoadingProgressNotifier);
			return true;
		}
		return false;
	};
	
	this.Observable = { //adapt the observable object
		addLoadingListener: observer.ObservableMethods.addListener,
		removeLoadingListener: observer.ObservableMethods.removeListener
	};
}
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
/**
*
* @author		:- Christopher Johnson
* @date			:- 30th-March-2011
* @description	:- This JScript is adapted from the code provided at http://www.quirksmode.org/js/cookies.html. It has been put into a form
*	which matches the code stylings used by the interactive mapper.
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.CookieTools = new function() {
	this.set = function(name,value,days) {
		if (days) {
			var date = new Date();
			date.setTime(date.getTime()+(days*24*60*60*1000));
			var expires = "; expires="+date.toGMTString();
		}
		else 
			var expires = "";
		document.cookie = name+"="+value+expires+"; path=/";
	}

	this.read = function(name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') 
				c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) 
				return c.substring(nameEQ.length,c.length);
		}
		return null;
	}

	this.erase = function(name) {
		this.set(name,"",0);
	}
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 2-Feb-2011
* @description	:- This JScript defines the behaviour for an observable attribute.
*	It is intended that instances of this object are extendees for objects which want a specific attribute.
*
* @usage		:- 
*	$.extend(objectToAddObservableAttributeTo, new nbn.util.ObservableAttribute('Name', 'initial Name'));
*	This will provide objectToAddObservableAttributeTo with methods:
*		getName();
*		setName();
*		addNameUpdatedListener();
*		removeNameUpdatedListener();
* @dependencies	:-
*	nbn.util.Observable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ObservableAttribute = function(_type, _content, _equalityFunction) {
	_type = _type || ''; //if no type is provided, this will just be a normal attribute
	_equalityFunction = _equalityFunction || function() { //if not equality function is provided, provide one which always returns false
		return false;
	};
	var _observable = new nbn.util.Observable();
	
	this['get' + _type] = function() {
		return _content;
	}
	
	this['set' + _type] = function(contentToSet) {
		if(!(_content == contentToSet || _equalityFunction(_content, contentToSet))) {
			_content = contentToSet;
			_observable.notifyListeners(_type,contentToSet); //do a segmented call
			_observable.notifyListeners('update',_type,contentToSet); //do a default update call, so that can be consolidated into a single function
		}
	}

	this['add' + _type + 'UpdateListener'] = _observable.ObservableMethods.addListener;
	this['remove' + _type + 'UpdateListener'] = _observable.ObservableMethods.removeListener;
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 27-Apr-2011
* @description	:- This JScript extends the ObservableAttribute and adds the method getTypeNextElement.
*	The content of this object may or may not be an array. If it is not an array, the getTypeNextElement 
*	method will always return the content of the ObservableAttribute.
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	nbn.util.ArrayTools
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ElementIterator = function(_type, _content) {
	var _currElement = 0;
	$.extend(this, new nbn.util.ObservableAttribute(_type, _content, nbn.util.ArrayTools.equals));
	
	this['get' + _type + 'NextElement'] = function() {//override the get method of the nbn.util.ObservableAttribute
		var content = this['get' + _type](); //call super
		if($.isArray(content)) { //if the content is an array then use it as is
			_currElement = (_currElement < content.length) ? _currElement : 0; //loop if nessersary
			return content[_currElement++]; //iterate return the curr element
		}
		else
			return content; //a single element has been detected, use this
	};
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 7-April-2011
* @description	:- This JScript will resolve the properties of an object to there complete nbn concept.
*	If the property is already complete then it will be returned in a callback without bothering the JSON service
* @dependencies	:-
*	jquery
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.EntityResolver = new function() {
	this.resolve = function(data, callback) {
		var resolveNeeded = false;
		var dataToResolve = {};
		for(var i in data) {//go through each of the properties and check if they are strings, if they are resolve them using the JSON Name servlet
			if(typeof data[i] === "string") {
				dataToResolve[i] = data[i];
				resolveNeeded = true;
			}
		}
		
		if(resolveNeeded) {
			_previousNameResolvingRequest = $.getJSON('EntityResolver', dataToResolve, function(resolvedData) {
				callback($.extend(data,resolvedData));
			});
		}
		else
			callback(data);
	};
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 28-Feb-2011
* @description	:- This JScript file will allow errors which have occured to be registered and shown to the use in a clean way
* @dependencies	:-
*	jquery ui
*/
window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.Logger = function() {
	var _neverShow = false;
	var _errorDialog, _errorMessages;
	
	var _showDialog = function() {
		if(!_neverShow) {
			if(!_errorDialog) {
				_errorDialog = $('<div>')
					.append($('<div>')
						.addClass('ui-state-error')
						.append('<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;">')
						.append('<strong>An error has occurred whilst exploring the map and may result in unexpected behaviour. This does not necessarily prevent you from carrying on and viewing records but if this error persists then please report it through the NBN Gateway feedback. Close this dialog to continue.</strong>')
						.append('<p>Below are the errors which caused this interruption: </p>')
						.append(_errorMessages = $('<div style="border-style:solid; height: 200px; width: 600px; overflow:auto;">'))
					)
					.dialog({
						title: "Whoops, There has been an error", 
						dialogClass: 'ui-state-error',
						modal: true,
						buttons: {
							"Close and Never Show" : function() {
								_neverShow = true; //flag to never open again
								$( this ).dialog( "close" );
							},
							Close: function() {
								$( this ).dialog( "close" );
							}
						},
						width: '800'
					});
			}
			else if(!_errorDialog.dialog('isOpen')) {
				_errorMessages.empty();
				_errorDialog.dialog('open');
			}
		}
	}
	
	this.error = function(type, desc) {
		_showDialog();
		_errorMessages.append($('<div>').html(type + ' : ' + desc));
	};
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 4-Feb-2011
* @description	:- This JScript defines the behaviour for an observable collection.
*	Notifications can be added when the underlying array is manipulated
*
* @usage		:- 
*	$.extend(objectToAddObservableCollectionTo, new nbn.util.ObservableCollection('Name'));
* @dependencies	:-
*	nbn.util.Observable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.ObservableCollection = function(_type) {
	_type = _type || ''; //if no name is specified then this will just be a normal collection
	var _observable = new nbn.util.Observable();
	var _underlyingCollection = [];
	
	var _notify = function(event) { //the maximum number of arguments I will cope with is 3
		var args = Array.prototype.slice.call(arguments, 1); //remove the first element from the arguments as that is the event name
		args.unshift(event, _underlyingCollection);
		_observable.notifyListeners.apply(this,args);//notify of change of underlying collection
		args.unshift('update');
		_observable.notifyListeners.apply(this,args);//call general update 
	};
	
	this['add' + _type] = function(toAdd) {
		_underlyingCollection.push(toAdd); //store this layer filter
		_notify('add', toAdd);
	};
	
	this['remove' + _type] = function(toRemove) {
		var toReturn = nbn.util.ArrayTools.removeFrom(toRemove,_underlyingCollection);
		if(toReturn)
			_notify('remove', toRemove);
		return toReturn;
	};
	
	this['position' + _type] = function(element, position) { //this function will take the element and insert it to a specific position. if it already exists then the original will be removed
		nbn.util.ArrayTools.removeFrom(element, _underlyingCollection); //remove the element if it exists
		_underlyingCollection.splice(position, 0, element);
		_notify('reposition', element, position);
	};
	
	this['forEach' + _type] = function(func) {
		for(var i in _underlyingCollection)
			func(_underlyingCollection[i], i, _underlyingCollection); //call the passed in function for each element
	};
	
	/**	This function will return the underlying array, it should be noted that changes to this array will not cause notifications*/
	this['getUnderlying' + _type + 'Array'] = function() {
		return _underlyingCollection;
	};
	
	this['add' + _type + 'CollectionUpdateListener'] = _observable.ObservableMethods.addListener;
	this['remove' + _type + 'CollectionUpdateListener'] = _observable.ObservableMethods.removeListener;
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 29-July-2011
* @description	:- Elements added to this type of ObservableCollection must be objects with an ObservableAttribute of type "_hashedParam".
* These objects can then be obtained with there current name by using the get<Type> method
*
* @usage		:- 
*	$.extend(objectToAddObservableCollectionTo, new nbn.util.HashedObservableCollection('Name', <_hashedParam>));
* @dependencies	:-
*	nbn.util.Observable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.HashedObservableCollection = function(_type, _hashedParam) {
	var _resolvableObject = {};
	var _underlyingObservableCollection;
	$.extend(this, _underlyingObservableCollection = new nbn.util.ObservableCollection(_type)); //extend a named ObservableCollection
	
	function _createHashedParamChangeListener(object) {
		var currentName = object['get' + _hashedParam]();
		return function(newName) {
			_resolvableObject[newName] = _resolvableObject[currentName]; //transfer the object
			delete _resolvableObject[currentName]; //remove the old reference
			currentName = newName; //save the current name as old name
		}
	};
	
	this['get' + _type] = function(hashParamValue) {
		return _resolvableObject[hashParamValue].item;
	};
	
	this['has' + _type] = function(hashParamValue) {
		return _resolvableObject[hashParamValue] != undefined;
	};
	
	this['add' + _type] = function(toAdd) {
		var itemToStore = _resolvableObject[toAdd['get' + _hashedParam]()] = {
			item: toAdd,
			listener: new (function() {
				this[_hashedParam] = _createHashedParamChangeListener(toAdd)
			})
		};
		toAdd['add' + _hashedParam + 'UpdateListener'](itemToStore.listener); //create a listener to see when the name changes
		_underlyingObservableCollection['add' + _type].apply(this, arguments);
	};
	
	this['remove' + _type] = function(toRemove) {
		var internalToRemove = _resolvableObject[toRemove['get' + _hashedParam]()]; //get the element to remove from the internal store
		toRemove['remove' + _hashedParam + 'UpdateListener'](toRemove.listener); //remove the listener
		delete internalToRemove; //remove the reference
		_underlyingObservableCollection['remove' + _type].apply(this, arguments);
	};
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 4-Feb-2011
* @description	:- This JScript defines the behaviour for an observable collection whose elements can be observed collectively.
*	Notifications can be added when the underlying array is manipulated and when the underlying elements have been changed 
*	given a list of events to listen to
*
* @usage		:- 
*	$.extend(objectToAddObservableCollectionTo, new nbn.util.PropagatingObservableCollection('Name'));
* @dependencies	:-
*	nbn.util.Observable
*	nbn.util.ObservableCollection
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};

nbn.util.PropagatingObservableCollection = function(_type, _eventsToListenTo) {
	var observableCollection = new nbn.util.ObservableCollection(_type);
	$.extend(this, observableCollection); //extend the ObservableCollection
	
	var _observable = new nbn.util.Observable();
	var _eventListener = new function() { //register all the events to a single object
		var _propertiesToAdd = _eventsToListenTo.slice(); //clone the events to add
		_propertiesToAdd.unshift('update'); //add an update property to deal with the common update notification
		for(var i in _propertiesToAdd) {
			this[_propertiesToAdd[i]] = new function() { //create a new function which returns the correct function
				var _currProperty = _propertiesToAdd[i]; //store the current property so that the function I call below has the correct args
				return function() {
					var args = Array.prototype.slice.call(arguments); //convert the arguments object to an array
					args.unshift(_currProperty); //put the argument name at the front of the new arguments array
					_observable.notifyListeners.apply(this,args); //call the notifyListeners with the correct params
				};
			};
		}
	};
	
	var _callEventListenerMethod = function(toCallOn, type) {
		for(var i in _eventsToListenTo)
			toCallOn[type + _eventsToListenTo[i] + 'UpdateListener'](_eventListener);
	};
	
	this['add' + _type] = function(toAdd) {
		observableCollection['add' + _type](toAdd); //store this layer filter
		_callEventListenerMethod(toAdd,'add');
	};
	
	this['remove' + _type] = function(toRemove) {
		var toReturn = observableCollection['remove' + _type](toRemove);
		if(toReturn) 
			_callEventListenerMethod(toRemove,'remove'); //remove update listener
		return toReturn;
	};
	
	this['add' + _type + 'PropagatingObservableCollectionUpdateListener'] = _observable.ObservableMethods.addListener;
	this['remove' + _type + 'PropagatingObservableCollectionUpdateListener'] = _observable.ObservableMethods.removeListener;
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This JScript will validate to see if a user is part of the nbn gateway and if so, will store there details to be obtained later
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	jquery
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.user = nbn.util.user || {};

nbn.util.user.Loginable = function() {
	var userAttribute = new nbn.util.ObservableAttribute('User');
	$.extend(this, userAttribute); //make this extend a user attribute
	delete this.setUser; //remove the set user method
	this.doUserLogin = function(username, passhash, callback) {
        $.getJSON('LoginServlet?username=' + username + '&userkey=' + passhash, function(result) {
			if (result.success) {
				userAttribute.setUser({
					userID: result.userid,
					username: username,
					passhash: passhash,
					fullname: result.name
				});
			}
			if($.isFunction(callback))
				callback(result); //notify callbacks
		});
    };
	
	this.isUserLoggedIn = function() {
		return userAttribute.getUser() & true;
	};
	
	this.doUserLogout = function() {
		userAttribute.setUser(undefined);
	};
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This JScript extends on nbn.util.user.Loginable by adding support for cookies
* @dependencies	:-
*	nbn.util.user.Loginable
*/

window.nbn = window.nbn || {};
nbn.util = nbn.util || {};
nbn.util.user = nbn.util.user || {};

nbn.util.user.CookieLoginable = function() {
	var _me = this;
	var _cookieLogin = function() {
		var contents = nbn.util.CookieTools.read('auth');
		if (contents) {
			var contentparts = contents.split('#');
			_me.doUserLogin(contentparts[0], contentparts[1]);
		} 
	};
	
	var _cookieStore = function(newUser) {
		if(newUser) 
			nbn.util.CookieTools.set('auth', newUser.username + '#' + newUser.passhash);
		else 
			nbn.util.CookieTools.erase('auth');
	};
	
	$.extend(this, new nbn.util.user.Loginable()); // extend an instance of Loginable
	
	this.addUserUpdateListener({ //add listener to set cookie on login
		User: _cookieStore
	});
	
	_cookieLogin(); //do cookie login
};
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
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 19-Nov-2010
* @description	:- This JScript defines the widget which contains the copyright statements of the NBN interactive mapper.
* @usage		:- $('<div>').nbn_copyrightController({map:interactiveMapper.Map});
* @dependencies	:-
*   jquery.ui.core.js
*   jquery.ui.widget.js
*/
(function( $, undefined ) {
$.widget( "ui.nbn_copyrightController", {
		options: {
			globalCopyright: '&copy; NBN ' + nbn.util.ServerGeneratedLoadTimeConstants.date.year + ' - <a href="http://data.nbn.org.uk/help/popups/generalTerms.jsp" target="_blank">Terms & Conditions</a>'
		},
		
		_create: function() {
			var _me = this;
			this.element.addClass( "nbn-copyrightControl ui-widget ui-widget-content" ); //set the class of the container to an nbn-copyright controller

			function _updateCopyright(type){
				var copyrightText = _me.options.globalCopyright;
				var baseLayerCopyrightText = type.getCopyright();
				if(baseLayerCopyrightText) //is there an extra copyright statement on the base layer?
					copyrightText += ' ' + baseLayerCopyrightText; //append it
				_me.element.html(copyrightText);
			};

			_updateCopyright(this.options.map.Map.getBaseLayer()); //set the copyright text the first time
			this._updateCopyrightListener = {BaseLayer:_updateCopyright};
			this.options.map.Map.addBaseLayerUpdateListener(this._updateCopyrightListener);
		},

		destroy: function() {
			this.element.removeClass( "nbn-copyrightControl ui-widget ui-widget-content"); //remove the appended class
			this.options.map.Map.removeBaseLayerTypeListener(this._updateCopyrightListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});

	$.extend( $.ui.nbn_copyrightController, {
		version: "@VERSION"
	});

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 19-Nov-2010
* @description	:- This JScript defines Jquery UI widget which will create a tree view. This particular Tree Widget
					is based on the DHTMLx widget
* @dependencies	:-
*   jquery.ui.core.js
*   jquery.ui.widget.js	
*	dhtmlxcommon.js
*	dhtmlxtree.js
*	dhtmlxtree_json.js
*	dhtmlxtree_xw.js
* @abilities	:- A tree widget provides two event listeners:
*	(selection) a selection listener which will notify of the current set of elements have been selected.
*	(selected) a seleted listener which will notify that a new element has been selected.
*
*/

(function( $, undefined ) {
    $.widget( "ui.nbn_treewidget", {
		options: {
			treeRoot: 0,
			allowMultipleSelection: 'checkbox',
			loadingText: 'Loading...'
		},
		
		_createTree: function() {
			var _me = this; //store a reference to me
			this._treeRepresentation = new dhtmlXTreeObject(_me._treeContainer[0], "100%", "100%", this.options.treeRoot); //creat the tree
			this._treeRepresentation.setSkin('dhx_skyblue');
			this._treeRepresentation.setImagePath("js/tree/codebase/imgs/csh_bluebooks/");
			this._treeRepresentation.setXMLAutoLoading(this.options.urlOfDescriptionFile);
			
			this._isTreeLoaded = false; //hold a flag to see if the tree has been loaded
			this._treeRepresentation.loadJSON(this.options.urlOfDescriptionFile, function() {
				_me._isTreeLoaded = true; //the tree has now loaded and is suitable for state saves
				_me._trigger("loaded");
			});
			
			this._treeRepresentation.setDataMode("JSON");

			if(this.options.allowMultipleSelection === 'checkbox') {
				this._treeRepresentation.enableCheckBoxes(true, true); //set to either enable multi select or single
				this._treeRepresentation.enableThreeStateCheckboxes(true);
			}
			else if (this.options.allowMultipleSelection === 'radio') {
				this._treeRepresentation.enableRadioButtons(true);
				this._treeRepresentation.enableSingleRadioMode(true, 0);
			}
			else if (this.options.allowMultipleSelection === 'none') {
				this._treeRepresentation.attachEvent('onSelect', function(id) {
					var dhtmlTreeNode = _me._parseDHTMLTreeID(id); //I only want to notify if the new id selected was that of a child
					if(dhtmlTreeNode.isChild)
						_me._trigger("selected",0, [dhtmlTreeNode.id]); //expected response is array, return array
				});
			}

			this._treeRepresentation.attachEvent('onCheck', function(id) {
				_me._trigger("selected", 0, id); //add a listener which will notify the latest addition, will notify of parents and children

				if(_me.options.allowMultipleSelection === 'checkbox') //deal with multiple selection listener
					_me._trigger("childrenSelectionListener",0,_me.getAllChildrenChecked());
				else if (_me.options.allowMultipleSelection === 'radio') { //deal with single selection listener
					var dhtmlTreeNode = _me._parseDHTMLTreeID(id); //I only want to notify if the new id selected was that of a child
					if(dhtmlTreeNode.isChild) {
						if (_me._treeRepresentation.isItemChecked(id))
							_me._trigger("childrenSelectionListener",0, [dhtmlTreeNode.id]); //expected response is array, return array
						else
							_me._trigger("childrenSelectionListener",0, []); //expected response is array, return array
					}
				}
			});
			
			this._treeRepresentation.setSerializationLevel(true,true);
			this._treeRepresentation.enableLoadingItem(this.options.loadingText);
		},
		
		_create: function() {
			this.element.append(this._treeContainer = $('<div>')
				.addClass( "nbn-treewidget" ) //set the class of the nbn-treewidget
			);
			this._createTree();
			if(this.options.selectDeselect === true)
				this.addSelectDeselect();
		},
		
		getState: function() {
			if(this._isTreeLoaded) 
				return $.parseJSON(this._treeRepresentation.serializeTreeToJSON());
			else
				return false;
		},
		
		setState: function(stateToLoad) {
			if(stateToLoad) {
				this._treeRepresentation.deleteChildItems(this.options.treeRoot);
				this._treeRepresentation.loadJSONObject(stateToLoad);
			}
		},

		setUrlOfDescriptionFile : function(newUrl) {
			this.options.urlOfDescriptionFile = newUrl;
			this._treeRepresentation.destructor(); //call the tree destruction function
			this._createTree();
		},
		
		getChildText : function(id) {
			return this._treeRepresentation.getItemText('C' + id);
		},
		
		getChildUserData : function(id, wantedData) {
			return this._treeRepresentation.getUserData('C' + id, wantedData);
		},
		
		getUserDataForAllChildrenChecked: function(wantedUserData) {
			var selectedChildren = this.getAllChildrenChecked();
			var toReturn = new Array();
			for(var currChild in selectedChildren)
				toReturn[toReturn.length] = this.getChildUserData(selectedChildren[currChild], wantedUserData); //this will convert the node to a child node ('C')
			return toReturn;
		},

		_nodeStringListToChildList : function(nodeListString) {
			var checkedTreeIDs = nodeListString.toString().split(','); //convert to string just incase it is a nubmer split the array around the comma
			var toReturn = [];
			for(var i=0; i<checkedTreeIDs.length; i++) {
				var checkedTreeID = this._parseDHTMLTreeID(checkedTreeIDs[i]);
				if(checkedTreeID.isChild)
					toReturn.push(checkedTreeID.id);
			}
			return toReturn;
		},
		
		getAllChildrenChecked: function() { //this function will remove all the parent ids from the CSV and return a selected element array
			return this._nodeStringListToChildList(this._treeRepresentation.getAllChecked());
		},
		
		getAllChildrenUnchecked: function() {
			return this._nodeStringListToChildList(this._treeRepresentation.getAllUnchecked());
		},
	
		isFullyChecked: function() {
			return this.getAllChildrenUnchecked().length === 0;
		},
		
		_checkAllWithValue: function(toCheck) {
			var checkedTreeIDs = ((toCheck) ? this._treeRepresentation.getAllUnchecked() : this._treeRepresentation.getAllChecked()).toString().split(','); //convert to string just incase it is a nubmer split the array around the comma
			for(var i=0; i<checkedTreeIDs.length; i++)
				this._treeRepresentation.setCheck(checkedTreeIDs[i], toCheck);
			this._trigger("childrenSelectionListener",0,this.getAllChildrenChecked()); //notify of check change
		},
		
		_createCheckAllButton: function(check, name) {
			var _me = this;
			return $('<button>')
				.button({
					label: name
				})
				.click(function() {
					_me._checkAllWithValue(check); 
				});
		},
		
		addSelectDeselect: function() {
			this.element.append(this._createCheckAllButton(true, 'Select All'));
			this.element.append(this._createCheckAllButton(false, 'Deselect All'));
		},
		
		checkAll: function() {
			this._checkAllWithValue(true);
		},
		
		unCheckAll: function() {
			this._checkAllWithValue(false);
		},

		_parseDHTMLTreeID: function(idToParse) {
			return {
				isChild: idToParse.charAt(0) == 'C',
				id: idToParse.substring(1)
			};
		},

		destroy: function() {
			this.element.removeClass( "nbn-treewidget" ); //remove the appended class
			this._treeRepresentation.destructor(); //call the tree destruction function
			this._treeContainer.remove();
			$.Widget.prototype.destroy.apply( this, arguments );
		},

		setItemStyle: function(itemId, style) {
			this._treeRepresentation.setItemStyle(itemId, style);
		}
	});

    $.extend( $.ui.nbn_treewidget, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 8th-Feb-2011
* @description	:- This Jquery widget will render the status of an nbn.util.user.Loginable and allow login
* @dependencies	:-
*	nbn.util.user.Loginable
*/
(function( $, undefined ) {

$.widget( "ui.nbn_login", {
	_setLoginErrorMessage: function(message) {
		this._messageBox = this._messageBox || $('<div>').addClass('nbn-login-errorMessage ui-state-error ui-corner-all');
		if(message)
			this.element.prepend(this._messageBox.html(message));
		else
			this._messageBox.remove();
	},
	
    _create: function() {
        this.element.addClass("nbn-login ui-widget ui-widget-content ui-corner-all");
        var _me = this;
		
		var _updateLoginWidget = function(newUser) {
			_me.element.empty();
			if(newUser)
				_me.element
					.append($('<div>').html('You are currently logged in as ' + newUser.fullname))
					.append($('<div>').nbn_buttonbar({
						buttons:{
							Logout: function() { 
								_me.options.loginUser.doUserLogout(); 
							}
						}
					}));
			else {
				_me.element
					.append($('<div>Username</div>').addClass('nbn-login-label'))
					.append(_me._usernameField = $('<input type="text" name="username"/>').addClass('nbn-login-input'))
					.append($('<div>Password</div>').addClass('nbn-login-label'))
					.append(_me._passwordField = $('<input type="password" name="password"/>').addClass('nbn-login-input'))
					.append($('<div>').nbn_buttonbar({
						buttons:{
							Login: function() {
								_me._setLoginErrorMessage(); //clear the message box
								_me.options.loginUser.doUserLogin(_me._usernameField.val(), $.md5(_me._passwordField.val()), function(result) {
									if(!result.success)
										_me._setLoginErrorMessage('Login has failed, please try again');
								}); 
							}
						}
					}));
			}
		};
		
		this._loginUpdateListener = {
			User: _updateLoginWidget
		};
		
		this.options.loginUser.addUserUpdateListener(this._loginUpdateListener);
		_updateLoginWidget(_me.options.loginUser.getUser()); //do an initial update of the user
    },

    destroy: function() {
        this.element.removeClass( "nbn-login ui-widget ui-widget-content ui-corner-all" ); //remove the appended class
		this.options.loginUser.removeUserUpdateListener(this._loginUpdateListener);
        $.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
    }
})

$.extend( $.ui.nbn_login, {
	version: "@VERSION"
});

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 2-Feb-2011
* @description	:- This JQuery Ui Widget will render the textual description of a dynamic or static filter. 
* @dependencies	:-
*	jquery
*	jquery.ui
* @usage 		:-
*	$('<div>').nbn_filterStatus({filter: FilterToRender});
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_filterStatus", {
		options: {
			statusFunction : function() {
				return '';
			},
			activeFilteringLabel: "",
			inactiveFilteringLabel: ""
		},
		
		_updateLabels: function() {
			var filter = this.options.layerFilter; //get the filter
			if(filter.isFiltering()) {
				this._label.html(this.options.activeFilteringLabel);
				this._status.html(this.options.statusFunction.call(filter.getFilter())); //if no description, remove
			}
			else {
				this._label.html(this.options.inactiveFilteringLabel);
				this._status.html('');
			}
		},
		
		_create: function() {
			var _me = this;
			this.element
				.append(this._label = $('<span>'))
				.append(this._status = $('<span>'));
			this._updateLabels(); //update the filter the first time
			this._updateListener = {
				update: function() {
					_me._updateLabels();
				}
			}
			this.options.layerFilter.addFilterUpdateListener(this._updateListener);
		},

		destroy: function() {
			this._label.remove();
			this._status.remove();
			this.options.layerFilter.removeFilterUpdateListener(this._updateListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_filterStatus, {
		version: "@VERSION"
    });

})( jQuery );

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 19-Jan-2011
* @description	:- This JScript animates when one of its loading listeners is currently loading. The logic for this is provided in nbn.util.loading.LoadingRegister
* @dependencies	:-
*   jquery.ui.core.js
*   jquery.ui.widget.js	
*	nbn.util.loading.LoadingConsolidator
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_loading", {
			options: {
				showProgressBar: true
			},
			
			_create: function() {
				this._averageLoadingProgress = new nbn.util.loading.AverageLoadingProgress();
				this.element
					.addClass('nbn-loading')
					.fadeTo(0,0); //hide by default
				
				var _element = this.element;
				if(this.options.showProgressBar) {
					_element.progressbar({ value: 0 });
					this._averageLoadingProgress.Observable.addLoadingListener(this._progressBarListener = {
						startedLoading : function() {
							_element.progressbar("option", "value", 0);//create a progress bar
						},
						updatedLoading : function(percentComplete) {
							_element.progressbar("option", "value", percentComplete * 100);
						},
						completedLoading : function() {
							_element.progressbar("option", "value", 100);
						}
					});
				}
				
				this._averageLoadingProgress.Observable.addLoadingListener(this._loadingAnimationListener = {
					startedLoading : function() {
						_element.stop(true, false).fadeTo('slow', 1);
					},
					completedLoading : function() {
						_element.stop(true, false).fadeTo('slow', 0);
					}
				}); //animate the loading bar
			},
			
			listenToTheLoadingOf: function(toListenTo) {
				this._averageLoadingProgress.listenToTheLoadingOf(toListenTo);
			},
			
			stopListeningToTheLoadingOf: function(toStopListeningTo) {
				this._averageLoadingProgress.stopListeningToTheLoadingOf(toStopListeningTo);
			},
		
			destroy: function() {
				this.element.removeClass('nbn-loading'); //remove the appended class
				if(this.options.showProgressBar) //if loaded with progress bar
					this._averageLoadingProgress.Observable.removeLoadingListener(this._progressBarListener); //remove loading listeners
				this._averageLoadingProgress.Observable.removeLoadingListener(this._loadingAnimationListener); //remove loading listeners
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_loading, {
		version: "@VERSION"
    });

})( jQuery );

/**
*
* @author		:- Christopher Johnson
* @date			:- 17-Feb-2011
* @description	:- This JScript defines the legend widget
*
* @usage		:- 
*	$('<div>').nbn_legend({layer: new nbn.layer.ArcGISMap()});
* @dependencies	:-
*	jquery
*	jquery.ui
*	nbn.layer.ArcGISMap
*	nbn.util.DataURIShemeSupport
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_legend", {
		options: {
			titleLayer:true,
			animationDuration: 'fast'
		},
		
		_createLayerLegendList : function(layer) {
			var _me = this; //store a reference to me
			var _legendList = $('<ul>').addClass('nbn-legend-list');
			$.each(layer.legend, function(i, entry) {
				var toAdd = $('<li>')
					.append($('<img>')
						.attr('src', ($.support.imageUri) //is image uri supported
							? 'data:' + entry.contentType + ';base64,' + entry.imageData //use uri
							: _me.options.layer.getHostsNextElement() + _me.options.layer.getMapService() + '/' + layer.layerId + '/images/' + entry.url //use image url
						)
						.addClass('legend-layer-item-image')
					)
					.append($('<a>')
						.addClass('legend-layer-item-name')
						.html((layer.legend.length!=1) ? entry.label : layer.layerName)
					)
					.addClass('legend-layer-item')
				toAdd.attr("description", (layer.legend.length!=1) ? entry.description : layer.description);
				_legendList.append(toAdd);
			});
			return _legendList;
		},
		
		_createLayerLegend: function(layer) {
			var _layerDiv = $('<div>').addClass('nbn-legend-layer');
			if(this.options.titleLayer && layer.legend.length!=1) {
				_layerDiv.append($('<p>')
					.addClass('nbn-legend-layer-title')
					.html(layer.layerName)
				);
			}
			var legendList = this._createLayerLegendList(layer); //create the legend
			var subLegendCount = Math.floor(150/legendList.measure().width); //how many sub legends should there be
			return _layerDiv.append(legendList.split(subLegendCount).width(Math.ceil(200/subLegendCount))); //split it
		},
		
		_renderLegend : function() {
			var _me = this; //store a reference to me
			_me._legendDiv.stop(true, false).fadeTo(_me.options.animationDuration, 0, function() {
				if(_me._lastLegendCall)
					_me._lastLegendCall.abort(); //kill old requests
				if(_me.options.layer.getMapService()) {
					_me._lastLegendCall = _me.options.layer.getLegend(function(json) { //perform an async call
						_me._legendDiv.empty(); //remove any old legend details
						$.each(json.layers,	function(i, item) {
							_me._legendDiv.append(_me._createLayerLegend(item));
						});
						$('.legend-layer-item[description]',_me._legendDiv).tipTip({attribute: "description"});
						_me._legendDiv.stop(true, false).fadeTo(_me.options.animationDuration, 1); //fade the legend back in
						_me._trigger('contentchange');
					});
				}
				else { //no map, remove legend
					_me._legendDiv.empty(); //remove any old legend details
					_me._trigger('contentchange');
				}
			});
		},
		
		_create: function() {
			var _me = this;
			this._updateObject = {
				update: function() {
					_me._renderLegend();
				}
			};
			
			this.element
				.addClass('nbn-legend')
				.append(this._legendDiv = $('<div>'));
			this.options.layer.addCurrentVisibleLayersUpdateListener(this._updateObject);
			this.options.layer.addMapServiceUpdateListener(this._updateObject);
			this._renderLegend(); //do an initial render
		},

		destroy: function() {
			this.options.layer.removeMapServiceUpdateListener(this._updateObject);
			this.options.layer.removeCurrentVisibleLayersUpdateListener(this._updateObject);
			this._legendDiv.remove();
			this.element.removeClass('nbn-legend');
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_legend, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 15-Feb-2011
* @description	:- This widget represents a button which is based on a jquery ui icon,
* 
* Tooltips, click operations and icons can be defined per state
* @dependencies	:-
*	Widget.css
*	jquery 1.4.4
*	jquery ui-1.8.8
*/

(function( $, undefined ) {
	$.widget( "ui.nbn_statefulbutton", {
		options: {
			initialState: 'default',
			states: { //defines the states along with there respective icon
				"default" : {
					icon: "ui-icon-close"
				}
			}
		},

		_create: function() {
			this.element
				.append(this._icon = $('<span>')
					.addClass('ui-icon') //set the initial state as the current
				)
				.addClass('ui-corner-all nbn-statefulButton');
			this._setNewState(this.options.initialState);

			this.element.hover(function() {
				$(this).toggleClass('ui-state-hover');
			});
			var _me = this;
			this.element.click(function() {
			if(_me._currentState.click != undefined)
				_me._currentState.click.apply(_me.element);
			});
		},

		setState: function(newState) {
			this._icon.removeClass(this._currentState.icon);
			this._setNewState(newState);
			this._trigger('statechange',newState);
		},

		_setNewState : function(newState) {
			this._currentState = this.options.states[newState]; //set the new current state
			this._icon.addClass(this._currentState.icon); //set the correct icon
			if(this._currentState.tooltip)
				this._icon.attr('title',this._currentState.tooltip); //set the title
			else
				this._icon.removeAttr('title'); //no title to set
		},

		destroy: function() {
			this._icon.remove();
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});

	$.extend( $.ui.nbn_statefulbutton, {
		version: "@VERSION"
	});

})( jQuery );
(function( $, undefined ) {
    $.widget( "ui.nbn_statefulbox", {
        options: {
            title: '',
            createCollapsibleButton: true,
            createAdvancedButton: false,
            createDestructionButton: false,
            collapsed: false,
            advancedCollapsed: true,
            padding: true,
			animation: {
				effect: 'blind',
				speed: 'medium'
			},
			outerClass: '',

            collaspsibleButtonToolTip: {
                expanded: "Click to collapse this box",
                collapsed: "Click to expand this box"
            },
            advancedCollaspsibleButtonToolTip: {
                expanded: "Click to hide the advanced settings of this box",
                collapsed: "Click to show the advanced settings of this box"
            },
            destructionButtonToolTip: "Click to destroy this box"
        },

        _notifyStateChange: function(event, newValue) {
            this._internalWidgetStateObservable.notifyListeners(event,newValue);
            this._trigger(event,0 ,{newValue: newValue}); //notify of change
        },

        _createCollapsibleButton: function() {
            var _me = this;
            var button = $('<a>')
                .addClass('nbn-statefulBox-collapsibleButton')
                .nbn_statefulbutton({
                    padding: false,
                    initialState: 'expanded',
                    states: {
                        expanded : {
                            icon : "ui-icon-minus",
                            click : function() {
                                _me.setCollapsed(true);
                            },
                            tooltip: _me.options.collaspsibleButtonToolTip.expanded
                        },
                        collapsed : {
                            icon : "ui-icon-plus",
                            click : function() {
                                _me.setCollapsed(false);
                            },
                            tooltip: _me.options.collaspsibleButtonToolTip.collapsed
                        }
                    }
                });
            this._internalWidgetStateObservable.ObservableMethods.addListener({
                collapsed: function(col) {
                    button.nbn_statefulbutton('setState',(col) ? 'collapsed' : 'expanded');
                }
            });
            return button;
        },

        _createAdvancedButton: function() {
            var _me = this;
            var button = $('<a>')
                .addClass('nbn-statefulBox-advancedButton')
                .nbn_statefulbutton({
                    padding: false,
                    initialState: 'expanded',
                    states: {
                        expanded : {
                            icon : "ui-icon-cancel",
                            click : function() {
                                _me.setAdvancedCollapsed(true);
                            },
                            tooltip: _me.options.advancedCollaspsibleButtonToolTip.expanded
                        },
                        collapsed : {
                            icon : "ui-icon-gear",
                            click : function() {
                                _me.setAdvancedCollapsed(false);
                            },
                            tooltip: _me.options.advancedCollaspsibleButtonToolTip.collapsed
                        }
                    }
                });
            this._internalWidgetStateObservable.ObservableMethods.addListener({
                advancedcollapsed: function(col) {
                    button.nbn_statefulbutton('setState',(col) ? 'collapsed' : 'expanded');
                },
                collapsed: function(collapsed) {
                    button.toggle(!collapsed);
                }
            });
            return button;
        },

        _createDestructionButton: function() {
            var _me = this;
            return $('<a>')
                .addClass('nbn-statefulBox-destructionButton')
                .nbn_statefulbutton({
                    initialState: 'default',
                    states: {
                        "default" : {
                            icon : "ui-icon-closethick",
                            click: function() {_me.element.slideUp('slow', _me.options.destroyFunc);},
                            tooltip: _me.options.destructionButtonToolTip
                        }
                    }
                });
        },
		
		_createAdditionalButtons: function() {
			var _me = this;
			$.each(this.options.additionalButtons, function(index, item) {
				_me._handleBar.append($('<a>').nbn_statefulbutton(item));
			});
		},

        _createButtonBar : function() {
            return $('<div>')
                .addClass('nbn-statefulBox-buttonBar')
                .nbn_buttonbar({
                    buttons: this.options.buttons
                });
        },

		_create: function() {
            this._internalWidgetStateObservable = new nbn.util.Observable(); //This will look after the observations of which can change dependant on which buttons are created
			var contents = this.element.contents(); //store the contents of the element
			this.element.addClass('nbn-statefulBox ui-widget ui-widget-content ui-corner-all ' + this.options.outerClass)
                .append(this._handleBar = $('<div>')
                    .addClass('nbn-statefulBox-handleBar')
                    .append(this._titleBar = $('<div>')
                        .addClass('nbn-statefulBox-titleBar')
                    )
                    .addClass('ui-widget-header ui-corner-all ui-helper-clearfix')
                )
                .append(this._contentsContainer = $('<div>')
                    .append(this._contents = $('<div>')
                        .append(contents)
                        .addClass('nbn-statefulBox-content')
                    )
                );
        },

        _init: function() {
            if(this.options.createCollapsibleButton)
                this._handleBar.append(this._collapsibleButton = this._createCollapsibleButton());
            if(this.options.createAdvancedButton)
                this._handleBar.append(this._advancedButton = this._createAdvancedButton());
            if(this.options.createDestructionButton)
                this._handleBar.append(this._createDestructionButton());
			if(this.options.additionalButtons)
				this._createAdditionalButtons(); //create any additional stateful buttons as requested
            if(this.options.buttons)
                this._contentsContainer.append(this._buttonBar = this._createButtonBar());
            if(this.options.padding)
                this._contents.addClass('nbn-statefulBox-content-padding');
            this.setTitle(this.options.title); //set the title initially
            this._setCollapsed(this.options.collapsed); //set the initial collapsed value
            this._setAdvancedCollapsed(this.options.advancedCollapsed); //set the initial collapsed value
        },

        setTitle: function(newTitle) {
            this._titleBar.html(newTitle);
        },

        _setCollapsed: function(collapsed, speed) {
			var aniOpt = this.options.animation;
            this._contentsContainer[(collapsed) ? 'hide' : 'show'](aniOpt.effect, aniOpt.options, speed);
            this._notifyStateChange('collapsed', collapsed);
        },

        _setAdvancedCollapsed: function(advancedCollapsed,speed) {
			var aniOpt = this.options.animation;
            $('[advancedControl="true"]',this._contentsContainer)[(advancedCollapsed) ? 'hide' : 'show'](aniOpt.effect, aniOpt.options, speed);
            this._notifyStateChange('advancedcollapsed', advancedCollapsed);
        },
		
		setAdvancedCollapsed: function(advancedCollapsed) {
			this._setAdvancedCollapsed(advancedCollapsed,this.options.animation.speed);
		},
		
		setCollapsed: function(collapsed) {
			this._setCollapsed(collapsed,this.options.animation.speed);
		},
		
        destroy: function() {
            this.element
                .removeClass('nbn-statefulBox ui-widget ui-widget-content ui-corner-all ' + this.options.outerClass)
                .removeAttr('state')
                .removeAttr('type')
                .append(this._contents.contents()); //reset the contents to the element
            $.Widget.prototype.destroy.apply( this, arguments );
        }
    });
	
    $.extend( $.ui.nbn_statefulbox, {
        version: "@VERSION"
    });

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 15-Feb-2011
* @description	:- This widget represents a bar of buttons, named by the properties of the passed in buttons option.
*	The functions which are performed on button presses are the values of the given property.
* @dependencies	:-
*	Widget.css
*	jquery 1.4.4
*	jquery ui-1.8.8
*/

(function( $, undefined ) {
	$.widget( "ui.nbn_buttonbar", {
		options: {
			buttons: {}
		},

		_create: function() {
			this.element
				.append(this._buttonSet = $('<div>')
					.addClass('nbn-buttonBar-buttonSet') //set the initial state as the current
				)
			.addClass('nbn-buttonBar ui-widget-content ui-helper-clearfix');
			for(var i in this.options.buttons) {
				this._buttonSet.append($('<button>')
					.button({
						label: i //set the button name
					})
					.click(this.options.buttons[i])
				);
			}
		},

		destroy: function() {
			this._buttonSet.remove();
			this.element.removeClass('nbn-buttonBar ui-widget-content ui-helper-clearfix');
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});

	$.extend( $.ui.nbn_buttonbar, {
		version: "@VERSION"
	});

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 22-August-2011
* @description	:- This widget will listen to changes of a particular observable attribute and execute the specified 
*	update function in the context of the dom element which is going to be updated.
* @dependencies	:- 
*	nbn.util.ObservableAttribute
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_contentListener", {
			_create: function() {
				var _me = this;
				var _updateFunction = function(newName) {
					_me.options.updateFunction.call(_me.element,newName);
					_me._trigger('contentchange');
				};
				this.options.content['add' + this.options.type + 'UpdateListener'](this._updateObject = new function(){
					this[_me.options.type] = _updateFunction;
				});
				_updateFunction(this.options.content['get' + this.options.type]());
			},
		
			destroy: function() {
				this.options.content['remove' + this.options.type + 'UpdateListener'](this._updateObject);
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_contentListener, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 21-Feb-2011
* @description	:- This widget will listen to changes of a label of a specific type and render them on change
* @dependencies	:- 
*	nbn_contentListener
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_label", {
			options: {
				type: 'Name' //the default label to render is name
			},
			
			_create: function() {
				this.element.nbn_contentListener({
					type: this.options.type,
					content: this.options.label,
					updateFunction: function(newName) {$(this).html(newName);}//define function which updates label
				});
			},
		
			destroy: function() {
				this.element.nbn_contentListener('destroy');
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_label, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 
* @description	:- This is a helper widget which creates the dom structure nessersary for creating a tabs widget
* @dependencies	:- jQuery
*	nbn.util.IDTools
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_dynamictabs", {			
			_create: function() {
				this.element
					.append(this._tabs = $('<ul>'))
					.append(this._contents = $('<div>'))
			},
			
			_createTab: function(label, id) {
				var tabTitle = $('<a>').attr('href','#' + id);
				if(typeof label == "string")
					tabTitle.html(label);
				else
					tabTitle.nbn_label({label: label});
				this._tabs.prepend($('<li>').append(tabTitle));
			},
			
			add: function(label, elementToAdd) {
				var id = 'dynamic-tabs-' + nbn.util.IDTools.generateUniqueID();
				elementToAdd
					.attr('id', id)
					.appendTo(this._contents);
				this._createTab(label, id);
			},
		
			destroy: function() {
				this.element.tabs('destroy');
				$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
			}
		});

    $.extend( $.ui.nbn_dynamictabs, {
		version: "@VERSION"
    });

})( jQuery );
(function( $, undefined ) {
    $.widget( "ui.nbn_list", {
		options: {
			data: []
		},
		
		_create: function() {
			this.element.addClass('ui-widget');
			this._updateList();
		},
		
		_updateList : function() {
			this.element.empty();
			if(this.options.sortFunction) //if has a sort function, then sort
				this.options.data.sort(this.options.sortFunction); //sort it with the sort function
			for(var i in this.options.data) 
				this.element.append($('<li>').append(this.options.elementRenderFunction(this.options.data[i])));
		},
		
		_animateUpdateList : function() {
			var _me = this;
			this.element.stop(true,false).fadeTo('fast', 0, function() {//fade out
				_me._updateList();
				_me.element.fadeTo('fast', 1); //fade back in
			})
		},
		
		setData: function(data) {
			this.options.data = (data) ? data.slice(0) : [] //perform a shallow copy of the array
			this._animateUpdateList(); //update the list
		},
		
		setSortFunction: function(sortFunction) {
			this.options.sortFunction = sortFunction;
			this._animateUpdateList(); //update the list
		},
		
		destroy: function() {
			this.element.removeClass('ui-widget');
		}
	});
		
    $.extend( $.ui.nbn_list, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 21-Feb-2011
* @description	:- This JScript defines an auto resizable widget. This implementation will only resize in the vertical component
* @usage		:- 
* @dependencies	:-
*	jquery
*	jquery.ui
*	jquery-measure
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_autosize", {
		options: {
			animationDuration: 'fast'
		},
		
		_create: function() {
			this.resize();
		},
		
		resize: function() {
			/*var _measuredElement = this.element.measure({width: this.element.width()});
			this.element.stop(true, false).animate({
				height: _measuredElement.height
			}, (this.element.is(':attached')) ? this.options.animationDuration : 0, function() {
				$(this).css({ //once complete remove the explicit height value
					height: 'auto'
				});
			});*/
		},
		
		destroy: function() {
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_autosize, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 22-March-2011
* @description	:- This JQuery Ui Widget will render and allow interaction with an renderableControl object
* @dependencies	:-
*	jquery
*	jquery.ui 1.8.10
* @usage 		:-
*	$('<div>').nbn_renderableControlDialog({renderableControl: FilterToRender});
*/

(function( $, undefined ) {

    $.widget( "ui.nbn_renderableControlDialog", {
		options: {
			resetButtonText: 'Reset',
			applyButtonText: 'Ok',
			title: 'Renderable Control Dialog',
			resizable: false,
			width: 500,
			autoOpen:false,
			modal: true
		},
		
		_createDialogOptions: function() {
			var _me = this;
			var dialogOptionsToReturn = {};
			$.extend(dialogOptionsToReturn, this.options, {
				buttons: new function() {
					this[_me.options.resetButtonText] = function() {
						_me.options.renderableControl.reset();
					};
					
					this[_me.options.applyButtonText] = function() {
						_me.options.renderableControl.apply();
						_me._backTrackableState = undefined;
						_me.close();
					};
				},
				open: function() {
					_me._backTrackableState = _me.options.renderableControl.getState(); //save the state of the representation when the dialog opens
				},
				close: function() {
					if(_me._backTrackableState)
						_me.options.renderableControl.setState(_me._backTrackableState);
				}
			});
			return dialogOptionsToReturn;
		},
		
		/*
			WARNING- THIS FUNCTION USES KNOWLEDGE OF THE JQUERY UI DIALOG IN ORDER TO GET A HANDLE ON THE BUTTONS SO THAT THEY CAN BE MANIPULATED
			THIS MAKES THIS WIDGET VERY MUCH TIED TO VERSION 1.8.10 OF JQUERY UI
		*/
		_buildReferencesToButtons: function () {
			var _me = this;
			var buttons = $('.ui-dialog-buttonset button',this.element.parent());
			this._buttons = {};
			$.each(buttons, function(index,elem) {
				_me._buttons[$('.ui-button-text', elem).html()] = $(elem);
			});
		},
		
		_create: function() {
			var _me = this;
			this.element
				.append(this._content = this.options.renderableControl.getRepresentation())
				.dialog(this._createDialogOptions());
			this._buildReferencesToButtons();

			var _renderableUpdateFunc = function(value) {
				_me._buttons[_me.options.applyButtonText].button((value) ? 'enable' : 'disable');
			};	
			
			_renderableUpdateFunc(this.options.renderableControl.getRenderable());		
			this.options.renderableControl.addRenderableUpdateListener(this._renderableUpdateListener = {
				Renderable: _renderableUpdateFunc
			});
		},
		
		open: function() {
			this.element.dialog('open');
		},
		
		close: function() {
			this.element.dialog('close');
		},

		destroy: function() {
			this._content.remove();
			this.options.renderableControl.removeRenderableUpdateListener(this._renderableUpdateListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_renderableControlDialog, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 23-March-2011
* @description	:- This JQuery Ui Widget will render a box selection pane window
* @dependencies	:-
*	jquery
*	jquery.ui
* @usage 		:-
*/
(function( $, undefined ) {
	$.widget( "ui.nbn_selectionPane", {
		options: {
			label: 'Choose an option',
			selectables:{}
		},
		
		_create: function() {
			var _me = this;//store reference to me
			
			this.element
				.append(this._selectionBox = $('<span>')
					.append(this.options.label)
					.append(this._selectionDropDown = $('<select>'))
				)
				.append(this._selectionContentArea = $('<div>'))
				.addClass("nbn-selectionPane");
			
			for(var i in this.options.selectables) { //add all the selectable items
				this._selectionDropDown.append($('<option>')
					.attr('value',i)
					.html(i)
				);
			}
			
			this._selectionDropDown.change(function() {
				if(_me._currentlySelected && _me._currentlySelected.content) //if currently selected and has content, then remove it from the selection pane
					_me._currentlySelected.content.detach();
				
				var currVal = $(this).val();
				var currSelected = _me._currentlySelected = _me.options.selectables[currVal]; //set the newly selected element

				var selectedFunc = currSelected.selected;
				if($.isFunction(selectedFunc))
					selectedFunc.call(_me.element);
				
				if(currSelected.content) //if this one has content, then append it to the selection pane
					_me._selectionContentArea.append(currSelected.content);
				_me._trigger('selectionchange',currVal);
			});
			
			this.setSelected(this.options.initial); //call for an inital time
		},
		
		getSelected: function() {
			return this._selectionDropDown.val();
		},
		
		setSelected: function(name) {
			this._selectionDropDown.prop('selectedIndex',$('option[value="' + name + '"]',this._selectionDropDown).index());
			this._selectionDropDown.change();
		},
		
		destroy: function() {
			this._selectionBox.remove();
			this._selectionContentArea.remove();
			this.element.removeClass( "nbn-selectionPane" ); //remove the appended class
			$.Widget.prototype.destroy.apply( this, arguments );
		}
	});
	
    $.extend( $.ui.nbn_selectionPane, {
		version: "@VERSION"
    });
})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 20th-April-2011
* @description	:- This JScript file will resolve and render a datasets metadata
* @dependencies	:-
*	nbn.util.EntityResolver
*/
(function( $, undefined ) {

$.widget( "ui.nbn_datasetmetadata", {
	options: {
		animationDuration: 'fast',
		renderFunction: function(datasetToRender) {
			return {
				title: $('<a>')
					.html(datasetToRender.name)
					.attr('title', 'Click for more information')
					.attr('target', '_blank')
					.attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetToRender.datasetKey),
				content: datasetToRender.description
			}
		}
	},
	
	_create: function() {
        this.element
			.addClass("nbn-datasetmetadata ui-widget ui-widget-content ui-corner-all")
			.append(this._titleContainer = $('<div>').addClass('nbn-datasetmetadata-title ui-widget-header'))
			.append(this._contentContainer = $('<div>').addClass('nbn-datasetmetadata-content'));
			
		this.setDataset(this.options.dataset);
    },

	_render: function(datasetToRender) {
		var renderedResults = this.options.renderFunction(datasetToRender);
		this._titleContainer.empty().append(renderedResults.title);
		this._contentContainer.empty().append(renderedResults.content);
	},
	
	setDataset: function(dataset) {
		var _me = this;
		_me.element.stop(true, false).fadeTo(_me.options.animationDuration, 0, function() {
			if(dataset) {
				nbn.util.EntityResolver.resolve({
					datasetWithMetadata: dataset
				}, function(result) {
					_me._render(result.datasetWithMetadata);
					_me.element.stop(true, false).fadeTo(_me.options.animationDuration, 1);
				});
			}
		});
	},
	
    destroy: function() {
        this.element.removeClass( "nbn-datasetmetadata ui-widget ui-widget-content ui-corner-all" ); //remove the appended class
		$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
    }
})

$.extend( $.ui.nbn_datasetmetadata, {
	version: "@VERSION"
});

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates an NBN Icon widget
* @usage		:- $('<div>').nbn_icon({icon: 'icontype', label: 'label'});
* @dependencies	:-
*	jquery
*	jquery.ui
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_icon", {
		_makeElementAButton: function() {
			if(this.options.active)
				this.element.addClass('ui-state-default');
			else
				this.element.hover(function() {$(this).toggleClass('ui-state-hover');});
				
			if(!this.options.partOfButtonSet) {
				function removeActiveClass() {$(this).removeClass('ui-state-active');}
				this.element.mouseup(removeActiveClass);
				this.element.mouseleave(removeActiveClass);
				this.element.mousedown(function(){$(this).addClass('ui-state-active');});
			}
			this.element.disableSelection();
		},
		
		_create: function() {	
			this.element.addClass('nbn-icon ui-widget ui-widget-content ui-corner-all');
			this.element.append(this._backgroundElement = $('<div>').addClass('nbn-iconImage ui-corner-top'));
			this.element.append(this._iconLabel = $('<div>').addClass('nbn-iconLabel ui-corner-bottom'));
			this.setContent(this.options);
			this._makeElementAButton();
		},
		
		setContent: function(content){
			this.setIcon(content.icon);
			this.setLabel(content.label);
		},
		
		setIcon: function(iconCSS) {
			if(this._currentIconCSS)
				this._backgroundElement.removeClass(this._currentIconCSS);
			this._backgroundElement.addClass(this._currentIconCSS = iconCSS);
		},
		
		setLabel: function(label) {
			this._iconLabel.html(label);
		},
		
		destroy: function() {
			this._backgroundElement.remove(); //remove elements
			this._iconLabel.remove();
			this.element.removeClass('nbn-icon ui-widget ui-widget-content ui-corner-all'); //remove classes
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_icon, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates an icon which represents a given base layer in the NBN Mapping framework
* @usage		:- $('<div>').nbn_baseLayerControllerWidget({map:map});
* @dependencies	:-
*	nbn_icon widget
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_baseLayerIcon", {
		_create: function() {
			var _icon = this.element;		
			this._nameAndIDListener = {
				ID: function(newID) 	{ _icon.nbn_icon('setIcon',newID);},
				Name: function(newName) {_icon.nbn_icon('setLabel',newName);}
			};
			this.element.nbn_icon(this._addListenersAndCreateContent());
		},
		
		/*The following function allows a base layer to be changed*/
		setBaseLayer: function(baseLayer) {
			this._removeListeners(); //remove existing listeners;
			this.options.baseLayer = baseLayer; //update the baselayer
			this.element.nbn_icon('setContent',this._addListenersAndCreateContent()); //addListeners and set content
		},
		
		_addListenersAndCreateContent: function() {
			this.options.baseLayer.addNameUpdateListener(this._nameAndIDListener);
			this.options.baseLayer.addIDUpdateListener(this._nameAndIDListener);
			return $.extend({
				icon: this.options.baseLayer.getID(),
				label: this.options.baseLayer.getName()
			},this.options);
		},
		
		_removeListeners: function() {
			this.options.baseLayer.removeNameUpdateListener(this._nameAndIDListener);
			this.options.baseLayer.removeIDUpdateListener(this._nameAndIDListener);
		},
		
		destroy: function() {
			this._removeListeners();
			this.element.nbn_icon('destroy');
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});
	
	$.extend( $.ui.nbn_baseLayerIcon, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates a graphical base layer icon, which represents a maps current view
* @usage		:- $('<div>').nbn_currentBaseLayerIndicator({map:map});
* @dependencies	:-
*	nbn_baseLayerIcon widget
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_currentBaseLayerIndicator", {
		_create: function() {
			var _me = this;
			this.options.map.addBaseLayerUpdateListener(this._baseLayerUpdateObj = {
				BaseLayer: function(newIcon) {_me.element.nbn_baseLayerIcon('setBaseLayer', newIcon);}
			});
			this.element.nbn_baseLayerIcon({baseLayer: this.options.map.getBaseLayer()}); //set the baseLayer in it's initial state
		},
		
		destroy: function() {
			this.options.map.removeBaseLayerUpdateListener(this._baseLayerUpdateObj);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});
	
	$.extend( $.ui.nbn_currentBaseLayerIndicator, {
		version: "@VERSION"
    });

})( jQuery );

/**
*
* @author		:- Christopher Johnson
* @date			:- 22-August-2011
* @description	:- This JScript creates an icon which represents a given base layer in the NBN Mapping framework
* @usage		:- $('<div>').nbn_baseLayerControllerWidget({map:map});
* @dependencies	:-
*	nbn_baseLayerIcon
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_baseLayerSelector", {
		options : {
			selectionPaneWidth: 410
		},
		
		_create: function() {
			var _me = this;
			this.element
				.addClass('nbn-baseLayerSelector ui-corner-all')
				.append(this._baseLayerButton = $('<div>').nbn_currentBaseLayerIndicator({map: this.options.map}).addClass('ui-state-default')) //create a base layer icon for this selectors map. Give it a default state
				.append(this._selectionPane = $('<div>').hide().addClass('nbn-baseLayerSelectorSelectionPane ui-widget ui-widget-content ui-corner-all') //create the baseLayer selection pane, add classes and hide
					.append(this._title = $('<div>').addClass('nbn-baseLayerSelectorTitle').html('Available Layers'))
					.append(this._baseLayerTypes = $('<div>').addClass('nbn-baseLayerSelectorTypes ui-helper-clearfix'))
				);
			
			/*Decide when the selection pane should become visible*/
			this.element.hover(
				function() { _me._selectionPane.show().stop(true, false).animate({width: _me.options.selectionPaneWidth});},
				function() { _me._selectionPane.stop(true, false).animate({width: 0}, function(){_me._selectionPane.hide()});}
			);
			
			this._redrawListener = { update: function() {_me._render();	}};
			this.options.map.addBaseLayerTypeCollectionUpdateListener(this._redrawListener);
			this.options.map.addBaseLayerUpdateListener(this._redrawListener);
			this._render(); //do an initial draw		
		},
		
		_render: function() {
			var _me = this;
			_me._baseLayerTypes.empty(); //clear the pane
			$.each(_me.options.map.getUnderlyingBaseLayerTypeArray(), function(index, currLayer) { //iterate around the layer types
				var isBaseLayer = currLayer === _me.options.map.getBaseLayer();
				var toAdd = $('<div>')
					.nbn_baseLayerIcon({baseLayer: currLayer, active: isBaseLayer, partOfButtonSet: true})
					.click(function() {_me.options.map.setBaseLayer(currLayer); }); //set the new base layer
				_me._baseLayerTypes.append(toAdd);
			});
		},
		
		destroy: function() {
			this.element.removeClass('nbn-baseLayerSelector ui-corner-all'); //remove class
			this._baseLayerButton.remove(); //remove elements
			this._selectionPane.remove();
			this.options.map.removeBaseLayerTypeCollectionUpdateListener(this._redrawListener);//remove listeners
			this.options.map.removeBaseLayerUpdateListener(this._redrawListener);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
	});
	
	$.extend( $.ui.nbn_baseLayerSelector, {
		version: "@VERSION"
    });

})( jQuery );
dhtmlx=function(obj){
	for (var a in obj) dhtmlx[a]=obj[a];
	return dhtmlx; //simple singleton
};
dhtmlx.extend_api=function(name,map,ext){
	var t = window[name];
	if (!t) return; //component not defined
	window[name]=function(obj){
		if (obj && typeof obj == "object" && !obj.tagName){
			var that = t.apply(this,(map._init?map._init(obj):arguments));
			//global settings
			for (var a in dhtmlx)
				if (map[a]) this[map[a]](dhtmlx[a]);			
			//local settings
			for (var a in obj){
				if (map[a]) this[map[a]](obj[a]);
				else if (a.indexOf("on")==0){
					this.attachEvent(a,obj[a]);
				}
			}
		} else
			var that = t.apply(this,arguments);
		if (map._patch) map._patch(this);
		return that||this;
	};
	window[name].prototype=t.prototype;
	if (ext)
		dhtmlXHeir(window[name].prototype,ext);
};

dhtmlxAjax={
	get:function(url,callback){
		var t=new dtmlXMLLoaderObject(true);
		t.async=(arguments.length<3);
		t.waitCall=callback;
		t.loadXML(url)
		return t;
	},
	post:function(url,post,callback){
		var t=new dtmlXMLLoaderObject(true);
		t.async=(arguments.length<4);
		t.waitCall=callback;
		t.loadXML(url,true,post)
		return t;
	},
	getSync:function(url){
		return this.get(url,null,true)
	},
	postSync:function(url,post){
		return this.post(url,post,null,true);		
	}
}

/**
  *     @desc: xmlLoader object
  *     @type: private
  *     @param: funcObject - xml parser function
  *     @param: object - jsControl object
  *     @param: async - sync/async mode (async by default)
  *     @param: rSeed - enable/disable random seed ( prevent IE caching)
  *     @topic: 0
  */
function dtmlXMLLoaderObject(funcObject, dhtmlObject, async, rSeed){
	this.xmlDoc="";

	if (typeof (async) != "undefined")
		this.async=async;
	else
		this.async=true;

	this.onloadAction=funcObject||null;
	this.mainObject=dhtmlObject||null;
	this.waitCall=null;
	this.rSeed=rSeed||false;
	return this;
};
/**
  *     @desc: xml loading handler
  *     @type: private
  *     @param: dtmlObject - xmlLoader object
  *     @topic: 0
  */
dtmlXMLLoaderObject.prototype.waitLoadFunction=function(dhtmlObject){
	var once = true;
	this.check=function (){
		if ((dhtmlObject)&&(dhtmlObject.onloadAction != null)){
			if ((!dhtmlObject.xmlDoc.readyState)||(dhtmlObject.xmlDoc.readyState == 4)){
				if (!once)
					return;

				once=false; //IE 5 fix
				if (typeof dhtmlObject.onloadAction == "function")
					dhtmlObject.onloadAction(dhtmlObject.mainObject, null, null, null, dhtmlObject);

				if (dhtmlObject.waitCall){
					dhtmlObject.waitCall.call(this,dhtmlObject);
					dhtmlObject.waitCall=null;
				}
			}
		}
	};
	return this.check;
};

/**
  *     @desc: return XML top node
  *     @param: tagName - top XML node tag name (not used in IE, required for Safari and Mozilla)
  *     @type: private
  *     @returns: top XML node
  *     @topic: 0  
  */
dtmlXMLLoaderObject.prototype.getXMLTopNode=function(tagName, oldObj){
	if (this.xmlDoc.responseXML){
		var temp = this.xmlDoc.responseXML.getElementsByTagName(tagName);
		if(temp.length==0 && tagName.indexOf(":")!=-1)
			var temp = this.xmlDoc.responseXML.getElementsByTagName((tagName.split(":"))[1]);
		var z = temp[0];
	} else
		var z = this.xmlDoc.documentElement;

	if (z){
		this._retry=false;
		return z;
	}

	if ((_isIE)&&(!this._retry)){
		//fall back to MS.XMLDOM
		var xmlString = this.xmlDoc.responseText;
		var oldObj = this.xmlDoc;
		this._retry=true;
		this.xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		this.xmlDoc.async=false;
		this.xmlDoc["loadXM"+"L"](xmlString);

		return this.getXMLTopNode(tagName, oldObj);
	}
	dhtmlxError.throwError("LoadXML", "Incorrect XML", [
		(oldObj||this.xmlDoc),
		this.mainObject
	]);

	return document.createElement("DIV");
};

/**
  *     @desc: load XML from string
  *     @type: private
  *     @param: xmlString - xml string
  *     @topic: 0  
  */
dtmlXMLLoaderObject.prototype.loadXMLString=function(xmlString){
	{
		try{
			var parser = new DOMParser();
			this.xmlDoc=parser.parseFromString(xmlString, "text/xml");
		}
		catch (e){
			this.xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			this.xmlDoc.async=this.async;
			this.xmlDoc["loadXM"+"L"](xmlString);
		}
	}

	this.onloadAction(this.mainObject, null, null, null, this);

	if (this.waitCall){
		this.waitCall();
		this.waitCall=null;
	}
}
/**
  *     @desc: load XML
  *     @type: private
  *     @param: filePath - xml file path
  *     @param: postMode - send POST request
  *     @param: postVars - list of vars for post request
  *     @topic: 0
  */
dtmlXMLLoaderObject.prototype.loadXML=function(filePath, postMode, postVars, rpc){
	if (this.rSeed)
		filePath+=((filePath.indexOf("?") != -1) ? "&" : "?")+"a_dhx_rSeed="+(new Date()).valueOf();
	this.filePath=filePath;

	if ((!_isIE)&&(window.XMLHttpRequest))
		this.xmlDoc=new XMLHttpRequest();
	else {
		this.xmlDoc=new ActiveXObject("Microsoft.XMLHTTP");
	}

	if (this.async)
		this.xmlDoc.onreadystatechange=new this.waitLoadFunction(this);
	this.xmlDoc.open(postMode ? "POST" : "GET", filePath, this.async);

	if (rpc){
		this.xmlDoc.setRequestHeader("User-Agent", "dhtmlxRPC v0.1 ("+navigator.userAgent+")");
		this.xmlDoc.setRequestHeader("Content-type", "text/xml");
	}

	else if (postMode)
		this.xmlDoc.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		
	this.xmlDoc.setRequestHeader("X-Requested-With","XMLHttpRequest");
	this.xmlDoc.send(null||postVars);

	if (!this.async)
		(new this.waitLoadFunction(this))();
};
/**
  *     @desc: destructor, cleans used memory
  *     @type: private
  *     @topic: 0
  */
dtmlXMLLoaderObject.prototype.destructor=function(){
	this._filterXPath = null;
	this._getAllNamedChilds = null;
	this._retry = null;
	this.async = null;
	this.rSeed = null;
	this.filePath = null;
	this.onloadAction = null;
	this.mainObject = null;
	this.xmlDoc = null;
	this.doXPath = null;
	this.doXPathOpera = null;
	this.doXSLTransToObject = null;
	this.doXSLTransToString = null;
	this.loadXML = null;
	this.loadXMLString = null;
	// this.waitLoadFunction = null;
	this.doSerialization = null;
	this.xmlNodeToJSON = null;
	this.getXMLTopNode = null;
	this.setXSLParamValue = null;
	return null;
}

dtmlXMLLoaderObject.prototype.xmlNodeToJSON = function(node){
        var t={};
        for (var i=0; i<node.attributes.length; i++)
            t[node.attributes[i].name]=node.attributes[i].value;
        t["_tagvalue"]=node.firstChild?node.firstChild.nodeValue:"";
        for (var i=0; i<node.childNodes.length; i++){
            var name=node.childNodes[i].tagName;
            if (name){
                if (!t[name]) t[name]=[];
                t[name].push(this.xmlNodeToJSON(node.childNodes[i]));
            }            
        }        
        return t;
    }

/**  
  *     @desc: Call wrapper
  *     @type: private
  *     @param: funcObject - action handler
  *     @param: dhtmlObject - user data
  *     @returns: function handler
  *     @topic: 0  
  */
function callerFunction(funcObject, dhtmlObject){
	this.handler=function(e){
		if (!e)
			e=window.event;
		funcObject(e, dhtmlObject);
		return true;
	};
	return this.handler;
};

/**  
  *     @desc: Calculate absolute position of html object
  *     @type: private
  *     @param: htmlObject - html object
  *     @topic: 0  
  */
function getAbsoluteLeft(htmlObject){
	return getOffset(htmlObject).left;
}
/**
  *     @desc: Calculate absolute position of html object
  *     @type: private
  *     @param: htmlObject - html object
  *     @topic: 0  
  */
function getAbsoluteTop(htmlObject){
	return getOffset(htmlObject).top;
}

function getOffsetSum(elem) {
	var top=0, left=0;
	while(elem) {
		top = top + parseInt(elem.offsetTop);
		left = left + parseInt(elem.offsetLeft);
		elem = elem.offsetParent;
	}
	return {top: top, left: left};
}
function getOffsetRect(elem) {
	var box = elem.getBoundingClientRect();
	var body = document.body;
	var docElem = document.documentElement;
	var scrollTop = window.pageYOffset || docElem.scrollTop || body.scrollTop;
	var scrollLeft = window.pageXOffset || docElem.scrollLeft || body.scrollLeft;
	var clientTop = docElem.clientTop || body.clientTop || 0;
	var clientLeft = docElem.clientLeft || body.clientLeft || 0;
	var top  = box.top +  scrollTop - clientTop;
	var left = box.left + scrollLeft - clientLeft;
	return { top: Math.round(top), left: Math.round(left) };
}
function getOffset(elem) {
	if (elem.getBoundingClientRect) {
		return getOffsetRect(elem);
	} else {
		return getOffsetSum(elem);
	}
}

/**  
*     @desc: Convert string to it boolean representation
*     @type: private
*     @param: inputString - string for covertion
*     @topic: 0
*/
function convertStringToBoolean(inputString){
	if (typeof (inputString) == "string")
		inputString=inputString.toLowerCase();

	switch (inputString){
		case "1":
		case "true":
		case "yes":
		case "y":
		case 1:
		case true:
			return true;
			break;

		default: return false;
	}
}

/**  
*     @desc: find out what symbol to use as url param delimiters in further params
*     @type: private
*     @param: str - current url string
*     @topic: 0  
*/
function getUrlSymbol(str){
	if (str.indexOf("?") != -1)
		return "&"
	else
		return "?"
}

function dhtmlDragAndDropObject(){
	if (window.dhtmlDragAndDrop)
		return window.dhtmlDragAndDrop;

	this.lastLanding=0;
	this.dragNode=0;
	this.dragStartNode=0;
	this.dragStartObject=0;
	this.tempDOMU=null;
	this.tempDOMM=null;
	this.waitDrag=0;
	window.dhtmlDragAndDrop=this;

	return this;
};

dhtmlDragAndDropObject.prototype.removeDraggableItem=function(htmlNode){
	htmlNode.onmousedown=null;
	htmlNode.dragStarter=null;
	htmlNode.dragLanding=null;
}
dhtmlDragAndDropObject.prototype.addDraggableItem=function(htmlNode, dhtmlObject){
	htmlNode.onmousedown=this.preCreateDragCopy;
	htmlNode.dragStarter=dhtmlObject;
	this.addDragLanding(htmlNode, dhtmlObject);
}
dhtmlDragAndDropObject.prototype.addDragLanding=function(htmlNode, dhtmlObject){
	htmlNode.dragLanding=dhtmlObject;
}
dhtmlDragAndDropObject.prototype.preCreateDragCopy=function(e){
	if ((e||window.event) && (e||event).button == 2)
		return;

	if (window.dhtmlDragAndDrop.waitDrag){
		window.dhtmlDragAndDrop.waitDrag=0;
		document.body.onmouseup=window.dhtmlDragAndDrop.tempDOMU;
		document.body.onmousemove=window.dhtmlDragAndDrop.tempDOMM;
		return false;
	}

	window.dhtmlDragAndDrop.waitDrag=1;
	window.dhtmlDragAndDrop.tempDOMU=document.body.onmouseup;
	window.dhtmlDragAndDrop.tempDOMM=document.body.onmousemove;
	window.dhtmlDragAndDrop.dragStartNode=this;
	window.dhtmlDragAndDrop.dragStartObject=this.dragStarter;
	document.body.onmouseup=window.dhtmlDragAndDrop.preCreateDragCopy;
	document.body.onmousemove=window.dhtmlDragAndDrop.callDrag;
	window.dhtmlDragAndDrop.downtime = new Date().valueOf();
	

	if ((e)&&(e.preventDefault)){
		e.preventDefault();
		return false;
	}
	return false;
};
dhtmlDragAndDropObject.prototype.callDrag=function(e){
	if (!e)
		e=window.event;
	dragger=window.dhtmlDragAndDrop;
	if ((new Date()).valueOf()-dragger.downtime<100) return;

	if ((e.button == 0)&&(_isIE))
		return dragger.stopDrag();

	if (!dragger.dragNode&&dragger.waitDrag){
		dragger.dragNode=dragger.dragStartObject._createDragNode(dragger.dragStartNode, e);

		if (!dragger.dragNode)
			return dragger.stopDrag();

		dragger.dragNode.onselectstart=function(){return false;}
		dragger.gldragNode=dragger.dragNode;
		document.body.appendChild(dragger.dragNode);
		document.body.onmouseup=dragger.stopDrag;
		dragger.waitDrag=0;
		dragger.dragNode.pWindow=window;
		dragger.initFrameRoute();
	}

	if (dragger.dragNode.parentNode != window.document.body){
		var grd = dragger.gldragNode;

		if (dragger.gldragNode.old)
			grd=dragger.gldragNode.old;

		//if (!document.all) dragger.calculateFramePosition();
		grd.parentNode.removeChild(grd);
		var oldBody = dragger.dragNode.pWindow;

		if (grd.pWindow &&	grd.pWindow.dhtmlDragAndDrop.lastLanding)
			grd.pWindow.dhtmlDragAndDrop.lastLanding.dragLanding._dragOut(grd.pWindow.dhtmlDragAndDrop.lastLanding);	
			
		//		var oldp=dragger.dragNode.parentObject;
		if (_isIE){
			var div = document.createElement("Div");
			div.innerHTML=dragger.dragNode.outerHTML;
			dragger.dragNode=div.childNodes[0];
		} else
			dragger.dragNode=dragger.dragNode.cloneNode(true);

		dragger.dragNode.pWindow=window;
		//		dragger.dragNode.parentObject=oldp;

		dragger.gldragNode.old=dragger.dragNode;
		document.body.appendChild(dragger.dragNode);
		oldBody.dhtmlDragAndDrop.dragNode=dragger.dragNode;
	}

	dragger.dragNode.style.left=e.clientX+15+(dragger.fx
		? dragger.fx*(-1)
		: 0)
		+(document.body.scrollLeft||document.documentElement.scrollLeft)+"px";
	dragger.dragNode.style.top=e.clientY+3+(dragger.fy
		? dragger.fy*(-1)
		: 0)
		+(document.body.scrollTop||document.documentElement.scrollTop)+"px";

	if (!e.srcElement)
		var z = e.target;
	else
		z=e.srcElement;
	dragger.checkLanding(z, e);
}

dhtmlDragAndDropObject.prototype.calculateFramePosition=function(n){
	//this.fx = 0, this.fy = 0;
	if (window.name){
		var el = parent.frames[window.name].frameElement.offsetParent;
		var fx = 0;
		var fy = 0;

		while (el){
			fx+=el.offsetLeft;
			fy+=el.offsetTop;
			el=el.offsetParent;
		}

		if ((parent.dhtmlDragAndDrop)){
			var ls = parent.dhtmlDragAndDrop.calculateFramePosition(1);
			fx+=ls.split('_')[0]*1;
			fy+=ls.split('_')[1]*1;
		}

		if (n)
			return fx+"_"+fy;
		else
			this.fx=fx;
		this.fy=fy;
	}
	return "0_0";
}
dhtmlDragAndDropObject.prototype.checkLanding=function(htmlObject, e){
	if ((htmlObject)&&(htmlObject.dragLanding)){
		if (this.lastLanding)
			this.lastLanding.dragLanding._dragOut(this.lastLanding);
		this.lastLanding=htmlObject;
		this.lastLanding=this.lastLanding.dragLanding._dragIn(this.lastLanding, this.dragStartNode, e.clientX,
			e.clientY, e);
		this.lastLanding_scr=(_isIE ? e.srcElement : e.target);
	} else {
		if ((htmlObject)&&(htmlObject.tagName != "BODY"))
			this.checkLanding(htmlObject.parentNode, e);
		else {
			if (this.lastLanding)
				this.lastLanding.dragLanding._dragOut(this.lastLanding, e.clientX, e.clientY, e);
			this.lastLanding=0;

			if (this._onNotFound)
				this._onNotFound();
		}
	}
}
dhtmlDragAndDropObject.prototype.stopDrag=function(e, mode){
	dragger=window.dhtmlDragAndDrop;

	if (!mode){
		dragger.stopFrameRoute();
		var temp = dragger.lastLanding;
		dragger.lastLanding=null;

		if (temp)
			temp.dragLanding._drag(dragger.dragStartNode, dragger.dragStartObject, temp, (_isIE
				? event.srcElement
				: e.target));
	}
	dragger.lastLanding=null;

	if ((dragger.dragNode)&&(dragger.dragNode.parentNode == document.body))
		dragger.dragNode.parentNode.removeChild(dragger.dragNode);
	dragger.dragNode=0;
	dragger.gldragNode=0;
	dragger.fx=0;
	dragger.fy=0;
	dragger.dragStartNode=0;
	dragger.dragStartObject=0;
	document.body.onmouseup=dragger.tempDOMU;
	document.body.onmousemove=dragger.tempDOMM;
	dragger.tempDOMU=null;
	dragger.tempDOMM=null;
	dragger.waitDrag=0;
}

dhtmlDragAndDropObject.prototype.stopFrameRoute=function(win){
	if (win)
		window.dhtmlDragAndDrop.stopDrag(1, 1);

	for (var i = 0; i < window.frames.length; i++){
		try{
		if ((window.frames[i] != win)&&(window.frames[i].dhtmlDragAndDrop))
			window.frames[i].dhtmlDragAndDrop.stopFrameRoute(window);
		} catch(e){}
	}

	try{
	if ((parent.dhtmlDragAndDrop)&&(parent != window)&&(parent != win))
		parent.dhtmlDragAndDrop.stopFrameRoute(window);
	} catch(e){}
}
dhtmlDragAndDropObject.prototype.initFrameRoute=function(win, mode){
	if (win){
		window.dhtmlDragAndDrop.preCreateDragCopy();
		window.dhtmlDragAndDrop.dragStartNode=win.dhtmlDragAndDrop.dragStartNode;
		window.dhtmlDragAndDrop.dragStartObject=win.dhtmlDragAndDrop.dragStartObject;
		window.dhtmlDragAndDrop.dragNode=win.dhtmlDragAndDrop.dragNode;
		window.dhtmlDragAndDrop.gldragNode=win.dhtmlDragAndDrop.dragNode;
		window.document.body.onmouseup=window.dhtmlDragAndDrop.stopDrag;
		window.waitDrag=0;

		if (((!_isIE)&&(mode))&&((!_isFF)||(_FFrv < 1.8)))
			window.dhtmlDragAndDrop.calculateFramePosition();
	}
	try{
	if ((parent.dhtmlDragAndDrop)&&(parent != window)&&(parent != win))
		parent.dhtmlDragAndDrop.initFrameRoute(window);
	}catch(e){}

	for (var i = 0; i < window.frames.length; i++){
		try{
		if ((window.frames[i] != win)&&(window.frames[i].dhtmlDragAndDrop))
			window.frames[i].dhtmlDragAndDrop.initFrameRoute(window, ((!win||mode) ? 1 : 0));
		} catch(e){}
	}
}

var _isFF = false;
var _isIE = false;
var _isOpera = false;
var _isKHTML = false;
var _isMacOS = false;
var _isChrome = false;

if (navigator.userAgent.indexOf('Macintosh') != -1)
	_isMacOS=true;


if (navigator.userAgent.toLowerCase().indexOf('chrome')>-1)
	_isChrome=true;

if ((navigator.userAgent.indexOf('Safari') != -1)||(navigator.userAgent.indexOf('Konqueror') != -1)){
	var _KHTMLrv = parseFloat(navigator.userAgent.substr(navigator.userAgent.indexOf('Safari')+7, 5));

	if (_KHTMLrv > 525){ //mimic FF behavior for Safari 3.1+
		_isFF=true;
		var _FFrv = 1.9;
	} else
		_isKHTML=true;
} else if (navigator.userAgent.indexOf('Opera') != -1){
	_isOpera=true;
	_OperaRv=parseFloat(navigator.userAgent.substr(navigator.userAgent.indexOf('Opera')+6, 3));
}


else if (navigator.appName.indexOf("Microsoft") != -1){
	_isIE=true;
	if (navigator.appVersion.indexOf("MSIE 8.0")!= -1 && document.compatMode != "BackCompat") _isIE=8;
	if (navigator.appVersion.indexOf("MSIE 9.0")!= -1 && document.compatMode != "BackCompat") _isIE=8;
} else {
	_isFF=true;
	var _FFrv = parseFloat(navigator.userAgent.split("rv:")[1])
}


//multibrowser Xpath processor
dtmlXMLLoaderObject.prototype.doXPath=function(xpathExp, docObj, namespace, result_type){
	if (_isKHTML || (!_isIE && !window.XPathResult))
		return this.doXPathOpera(xpathExp, docObj);

	if (_isIE){ //IE
		if (!docObj)
			if (!this.xmlDoc.nodeName)
				docObj=this.xmlDoc.responseXML
			else
				docObj=this.xmlDoc;

		if (!docObj)
			dhtmlxError.throwError("LoadXML", "Incorrect XML", [
				(docObj||this.xmlDoc),
				this.mainObject
			]);

		if (namespace != null)
			docObj.setProperty("SelectionNamespaces", "xmlns:xsl='"+namespace+"'"); //

		if (result_type == 'single'){
			return docObj.selectSingleNode(xpathExp);
		}
		else {
			return docObj.selectNodes(xpathExp)||new Array(0);
		}
	} else { //Mozilla
		var nodeObj = docObj;

		if (!docObj){
			if (!this.xmlDoc.nodeName){
				docObj=this.xmlDoc.responseXML
			}
			else {
				docObj=this.xmlDoc;
			}
		}

		if (!docObj)
			dhtmlxError.throwError("LoadXML", "Incorrect XML", [
				(docObj||this.xmlDoc),
				this.mainObject
			]);

		if (docObj.nodeName.indexOf("document") != -1){
			nodeObj=docObj;
		}
		else {
			nodeObj=docObj;
			docObj=docObj.ownerDocument;
		}
		var retType = XPathResult.ANY_TYPE;

		if (result_type == 'single')
			retType=XPathResult.FIRST_ORDERED_NODE_TYPE
		var rowsCol = new Array();
		var col = docObj.evaluate(xpathExp, nodeObj, function(pref){
			return namespace
		}, retType, null);

		if (retType == XPathResult.FIRST_ORDERED_NODE_TYPE){
			return col.singleNodeValue;
		}
		var thisColMemb = col.iterateNext();

		while (thisColMemb){
			rowsCol[rowsCol.length]=thisColMemb;
			thisColMemb=col.iterateNext();
		}
		return rowsCol;
	}
}

function _dhtmlxError(type, name, params){
	if (!this.catches)
		this.catches=new Array();

	return this;
}

_dhtmlxError.prototype.catchError=function(type, func_name){
	this.catches[type]=func_name;
}
_dhtmlxError.prototype.throwError=function(type, name, params){
	if (this.catches[type])
		return this.catches[type](type, name, params);

	if (this.catches["ALL"])
		return this.catches["ALL"](type, name, params);

	alert("Error type: "+arguments[0]+"\nDescription: "+arguments[1]);
	return null;
}

window.dhtmlxError=new _dhtmlxError();


//opera fake, while 9.0 not released
//multibrowser Xpath processor
dtmlXMLLoaderObject.prototype.doXPathOpera=function(xpathExp, docObj){
	//this is fake for Opera
	var z = xpathExp.replace(/[\/]+/gi, "/").split('/');
	var obj = null;
	var i = 1;

	if (!z.length)
		return [];

	if (z[0] == ".")
		obj=[docObj]; else if (z[0] == ""){
		obj=(this.xmlDoc.responseXML||this.xmlDoc).getElementsByTagName(z[i].replace(/\[[^\]]*\]/g, ""));
		i++;
	} else
		return [];

	for (i; i < z.length; i++)obj=this._getAllNamedChilds(obj, z[i]);

	if (z[i-1].indexOf("[") != -1)
		obj=this._filterXPath(obj, z[i-1]);
	return obj;
}

dtmlXMLLoaderObject.prototype._filterXPath=function(a, b){
	var c = new Array();
	var b = b.replace(/[^\[]*\[\@/g, "").replace(/[\[\]\@]*/g, "");

	for (var i = 0; i < a.length; i++)
		if (a[i].getAttribute(b))
			c[c.length]=a[i];

	return c;
}
dtmlXMLLoaderObject.prototype._getAllNamedChilds=function(a, b){
	var c = new Array();

	if (_isKHTML)
		b=b.toUpperCase();

	for (var i = 0; i < a.length; i++)for (var j = 0; j < a[i].childNodes.length; j++){
		if (_isKHTML){
			if (a[i].childNodes[j].tagName&&a[i].childNodes[j].tagName.toUpperCase() == b)
				c[c.length]=a[i].childNodes[j];
		}

		else if (a[i].childNodes[j].tagName == b)
			c[c.length]=a[i].childNodes[j];
	}

	return c;
}

function dhtmlXHeir(a, b){
	for (var c in b)
		if (typeof (b[c]) == "function")
			a[c]=b[c];
	return a;
}

function dhtmlxEvent(el, event, handler){
	if (el.addEventListener)
		el.addEventListener(event, handler, false);

	else if (el.attachEvent)
		el.attachEvent("on"+event, handler);
}

//============= XSL Extension ===================================

dtmlXMLLoaderObject.prototype.xslDoc=null;
dtmlXMLLoaderObject.prototype.setXSLParamValue=function(paramName, paramValue, xslDoc){
	if (!xslDoc)
		xslDoc=this.xslDoc

	if (xslDoc.responseXML)
		xslDoc=xslDoc.responseXML;
	var item =
		this.doXPath("/xsl:stylesheet/xsl:variable[@name='"+paramName+"']", xslDoc,
			"http:/\/www.w3.org/1999/XSL/Transform", "single");

	if (item != null)
		item.firstChild.nodeValue=paramValue
}
dtmlXMLLoaderObject.prototype.doXSLTransToObject=function(xslDoc, xmlDoc){
	if (!xslDoc)
		xslDoc=this.xslDoc;

	if (xslDoc.responseXML)
		xslDoc=xslDoc.responseXML

	if (!xmlDoc)
		xmlDoc=this.xmlDoc;

	if (xmlDoc.responseXML)
		xmlDoc=xmlDoc.responseXML

	//MOzilla
	if (!_isIE){
		if (!this.XSLProcessor){
			this.XSLProcessor=new XSLTProcessor();
			this.XSLProcessor.importStylesheet(xslDoc);
		}
		var result = this.XSLProcessor.transformToDocument(xmlDoc);
	} else {
		var result = new ActiveXObject("Msxml2.DOMDocument.3.0");
		try{
			xmlDoc.transformNodeToObject(xslDoc, result);
		}catch(e){
			result = xmlDoc.transformNode(xslDoc);
		}
	}
	return result;
}

dtmlXMLLoaderObject.prototype.doXSLTransToString=function(xslDoc, xmlDoc){
	var res = this.doXSLTransToObject(xslDoc, xmlDoc);
	if(typeof(res)=="string")
		return res;
	return this.doSerialization(res);
}

dtmlXMLLoaderObject.prototype.doSerialization=function(xmlDoc){
	if (!xmlDoc)
			xmlDoc=this.xmlDoc;
	if (xmlDoc.responseXML)
			xmlDoc=xmlDoc.responseXML
	if (!_isIE){
		var xmlSerializer = new XMLSerializer();
		return xmlSerializer.serializeToString(xmlDoc);
	} else
		return xmlDoc.xml;
}

/**
*   @desc: 
*   @type: private
*/
dhtmlxEventable=function(obj){
		obj.attachEvent=function(name, catcher, callObj){
			name='ev_'+name.toLowerCase();
			if (!this[name])
				this[name]=new this.eventCatcher(callObj||this);
				
			return(name+':'+this[name].addEvent(catcher)); //return ID (event name & event ID)
		}
		obj.callEvent=function(name, arg0){ 
			name='ev_'+name.toLowerCase();
			if (this[name])
				return this[name].apply(this, arg0);
			return true;
		}
		obj.checkEvent=function(name){
			return (!!this['ev_'+name.toLowerCase()])
		}
		obj.eventCatcher=function(obj){
			var dhx_catch = [];
			var z = function(){
				var res = true;
				for (var i = 0; i < dhx_catch.length; i++){
					if (dhx_catch[i] != null){
						var zr = dhx_catch[i].apply(obj, arguments);
						res=res&&zr;
					}
				}
				return res;
			}
			z.addEvent=function(ev){
				if (typeof (ev) != "function")
					ev=eval(ev);
				if (ev)
					return dhx_catch.push(ev)-1;
				return false;
			}
			z.removeEvent=function(id){
				dhx_catch[id]=null;
			}
			return z;
		}
		obj.detachEvent=function(id){
			if (id != false){
				var list = id.split(':');           //get EventName and ID
				this[list[0]].removeEvent(list[1]); //remove event
			}
		}
		obj.detachAllEvents = function(){
			for (var name in this){
				if (name.indexOf("ev_")==0) 
					delete this[name];
			}
		}
}

//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/


function xmlPointer(data){this.d=data;};xmlPointer.prototype={text:function(){if (!_isFF)return this.d.xml;var x = new XMLSerializer();return x.serializeToString(this.d);},
 get:function(name){return this.d.getAttribute(name);},
 exists:function(){return !!this.d },
 content:function(){return this.d.firstChild?this.d.firstChild.data:"";}, 
 each:function(name,f,t,i){var a=this.d.childNodes;var c=new xmlPointer();if (a.length)for (i=i||0;i<a.length;i++)if (a[i].tagName==name){c.d=a[i];if(f.apply(t,[c,i])==-1) return;}},
 get_all:function(){var a={};var b=this.d.attributes;for (var i=0;i<b.length;i++)a[b[i].name]=b[i].value;return a;},
 sub:function(name){var a=this.d.childNodes;var c=new xmlPointer();if (a.length)for (var i=0;i<a.length;i++)if (a[i].tagName==name){c.d=a[i];return c;}},
 up:function(name){return new xmlPointer(this.d.parentNode);},
 set:function(name,val){this.d.setAttribute(name,val);},
 clone:function(name){return new xmlPointer(this.d);},
 sub_exists:function(name){var a=this.d.childNodes;if (a.length)for (var i=0;i<a.length;i++)if (a[i].tagName==name)return true;return false;},
 through:function(name,rule,v,f,t){var a=this.d.childNodes;if (a.length)for (var i=0;i<a.length;i++){if (a[i].tagName==name && a[i].getAttribute(rule)!=null && a[i].getAttribute(rule)!="" && (!v || a[i].getAttribute(rule)==v )) {var c=new xmlPointer(a[i]);f.apply(t,[c,i]);};var w=this.d;this.d=a[i];this.through(name,rule,v,f,t);this.d=w;}}};function dhtmlXTreeObject(htmlObject, width, height, rootId){if (_isIE)try {document.execCommand("BackgroundImageCache", false, true);}catch (e){};if (typeof(htmlObject)!="object")
 this.parentObject=document.getElementById(htmlObject);else
 this.parentObject=htmlObject;this.parentObject.style.overflow="hidden";this._itim_dg=true;this.dlmtr=",";this.dropLower=false;this.enableIEImageFix();this.xmlstate=0;this.mytype="tree";this.smcheck=true;this.width=width;this.height=height;this.rootId=rootId;this.childCalc=null;this.def_img_x="18px";this.def_img_y="18px";this.def_line_img_x="18px";this.def_line_img_y="18px";this._dragged=new Array();this._selected=new Array();this.style_pointer="pointer";if (_isIE)this.style_pointer="hand";this._aimgs=true;this.htmlcA=" [";this.htmlcB="]";this.lWin=window;this.cMenu=0;this.mlitems=0;this.iconURL="";this.dadmode=0;this.slowParse=false;this.autoScroll=true;this.hfMode=0;this.nodeCut=new Array();this.XMLsource=0;this.XMLloadingWarning=0;this._idpull={};this._pullSize=0;this.treeLinesOn=true;this.tscheck=false;this.timgen=true;this.dpcpy=false;this._ld_id=null;this._oie_onXLE=[];this.imPath=window.dhx_globalImgPath||"";this.checkArray=new Array("iconUncheckAll.gif","iconCheckAll.gif","iconCheckGray.gif","iconUncheckDis.gif","iconCheckDis.gif","iconCheckDis.gif");this.radioArray=new Array("radio_off.gif","radio_on.gif","radio_on.gif","radio_off.gif","radio_on.gif","radio_on.gif");this.lineArray=new Array("line2.gif","line3.gif","line4.gif","blank.gif","blank.gif","line1.gif");this.minusArray=new Array("minus2.gif","minus3.gif","minus4.gif","minus.gif","minus5.gif");this.plusArray=new Array("plus2.gif","plus3.gif","plus4.gif","plus.gif","plus5.gif");this.imageArray=new Array("leaf.gif","folderOpen.gif","folderClosed.gif");this.cutImg= new Array(0,0,0);this.cutImage="but_cut.gif";dhtmlxEventable(this);this.dragger= new dhtmlDragAndDropObject();this.htmlNode=new dhtmlXTreeItemObject(this.rootId,"",0,this);this.htmlNode.htmlNode.childNodes[0].childNodes[0].style.display="none";this.htmlNode.htmlNode.childNodes[0].childNodes[0].childNodes[0].className="hiddenRow";this.allTree=this._createSelf();this.allTree.appendChild(this.htmlNode.htmlNode);if(_isFF){this.allTree.childNodes[0].width="100%";this.allTree.childNodes[0].style.overflow="hidden";};var self=this;this.allTree.onselectstart=new Function("return false;");if (_isMacOS)this.allTree.oncontextmenu = function(e){return self._doContClick(e||window.event);};this.allTree.onmousedown = function(e){return self._doContClick(e||window.event);};this.XMLLoader=new dtmlXMLLoaderObject(this._parseXMLTree,this,true,this.no_cashe);if (_isIE)this.preventIECashing(true);this.selectionBar=document.createElement("DIV");this.selectionBar.className="selectionBar";this.selectionBar.innerHTML="&nbsp;";this.selectionBar.style.display="none";this.allTree.appendChild(this.selectionBar);if (window.addEventListener)window.addEventListener("unload",function(){try{self.destructor();}catch(e){}},false);if (window.attachEvent)window.attachEvent("onunload",function(){try{self.destructor();}catch(e){}});this.setImagesPath=this.setImagePath;this.setIconsPath=this.setIconPath;if (dhtmlx.image_path)this.setImagePath(dhtmlx.image_path);if (dhtmlx.skin)this.setSkin(dhtmlx.skin);return this;};dhtmlXTreeObject.prototype.setDataMode=function(mode){this._datamode=mode;};dhtmlXTreeObject.prototype._doContClick=function(ev){if (ev.button!=2){if(this._acMenu){if (this._acMenu.hideContextMenu)this._acMenu.hideContextMenu()
 else
 this.cMenu._contextEnd();};return true;};var el=(_isIE?ev.srcElement:ev.target);while ((el)&&(el.tagName!="BODY")) {if (el.parentObject)break;el=el.parentNode;};if ((!el)||(!el.parentObject)) return true;var obj=el.parentObject;if (!this.callEvent("onRightClick",[obj.id,ev]))
 (ev.srcElement||ev.target).oncontextmenu = function(e){(e||event).cancelBubble=true;return false;};this._acMenu=(obj.cMenu||this.cMenu);if (this._acMenu){if (!(this.callEvent("onBeforeContextMenu", [
 obj.id
 ]))) return true;(ev.srcElement||ev.target).oncontextmenu = function(e){(e||event).cancelBubble=true;return false;};if (this._acMenu.showContextMenu){var dEl0=window.document.documentElement;var dEl1=window.document.body;var corrector = new Array((dEl0.scrollLeft||dEl1.scrollLeft),(dEl0.scrollTop||dEl1.scrollTop));if (_isIE){var x= ev.clientX+corrector[0];var y = ev.clientY+corrector[1];}else {var x= ev.pageX;var y = ev.pageY;};this._acMenu.showContextMenu(x-1,y-1)
 this.contextID=obj.id;ev.cancelBubble=true;this._acMenu._skip_hide=true;}else {el.contextMenuId=obj.id;el.contextMenu=this._acMenu;el.a=this._acMenu._contextStart;el.a(el, ev);el.a=null;};return false;};return true;};dhtmlXTreeObject.prototype.enableIEImageFix=function(mode){if (!mode){this._getImg=function(id){return document.createElement((id==this.rootId)?"div":"img");};this._setSrc=function(a,b){a.src=b;};this._getSrc=function(a){return a.src;}}else {this._getImg=function(){var z=document.createElement("DIV");z.innerHTML="&nbsp;";z.className="dhx_bg_img_fix";return z;};this._setSrc=function(a,b){a.style.backgroundImage="url("+b+")";};this._getSrc=function(a){var z=a.style.backgroundImage;return z.substr(4,z.length-5);}}};dhtmlXTreeObject.prototype.destructor=function(){for (var a in this._idpull){var z=this._idpull[a];if (!z)continue;z.parentObject=null;z.treeNod=null;z.childNodes=null;z.span=null;z.tr.nodem=null;z.tr=null;z.htmlNode.objBelong=null;z.htmlNode=null;this._idpull[a]=null;};this.parentObject.innerHTML="";if(this.XMLLoader)this.XMLLoader.destructor();this.allTree.onselectstart = null;this.allTree.oncontextmenu = null;this.allTree.onmousedown = null;for(var a in this){this[a]=null;}};function cObject(){return this;};cObject.prototype= new Object;cObject.prototype.clone = function () {function _dummy(){};_dummy.prototype=this;return new _dummy();};function dhtmlXTreeItemObject(itemId,itemText,parentObject,treeObject,actionHandler,mode){this.htmlNode="";this.acolor="";this.scolor="";this.tr=0;this.childsCount=0;this.tempDOMM=0;this.tempDOMU=0;this.dragSpan=0;this.dragMove=0;this.span=0;this.closeble=1;this.childNodes=new Array();this.userData=new cObject();this.checkstate=0;this.treeNod=treeObject;this.label=itemText;this.parentObject=parentObject;this.actionHandler=actionHandler;this.images=new Array(treeObject.imageArray[0],treeObject.imageArray[1],treeObject.imageArray[2]);this.id=treeObject._globalIdStorageAdd(itemId,this);if (this.treeNod.checkBoxOff )this.htmlNode=this.treeNod._createItem(1,this,mode);else this.htmlNode=this.treeNod._createItem(0,this,mode);this.htmlNode.objBelong=this;return this;};dhtmlXTreeObject.prototype._globalIdStorageAdd=function(itemId,itemObject){if (this._globalIdStorageFind(itemId,1,1)) {itemId=itemId +"_"+(new Date()).valueOf();return this._globalIdStorageAdd(itemId,itemObject);};this._idpull[itemId]=itemObject;this._pullSize++;return itemId;};dhtmlXTreeObject.prototype._globalIdStorageSub=function(itemId){if (this._idpull[itemId]){this._unselectItem(this._idpull[itemId]);this._idpull[itemId]=null;this._pullSize--;};if ((this._locker)&&(this._locker[itemId])) this._locker[itemId]=false;};dhtmlXTreeObject.prototype._globalIdStorageFind=function(itemId,skipXMLSearch,skipParsing,isreparse){var z=this._idpull[itemId]
 if (z){if ((z.unParsed)&&(!skipParsing))
 {this.reParse(z,0);};if (this._srnd && !z.htmlNode)this._buildSRND(z,skipParsing);if ((isreparse)&&(this._edsbpsA)){for (var j=0;j<this._edsbpsA.length;j++)if (this._edsbpsA[j][2]==itemId){dhtmlxError.throwError("getItem","Requested item still in parsing process.",itemId);return null;}};return z;};if ((this.slowParse)&&(itemId!=0)&&(!skipXMLSearch)) return this.preParse(itemId);else


 return null;};dhtmlXTreeObject.prototype._getSubItemsXML=function(p){var z=[];p.each("item",function(c){z.push(c.get("id"));},this)
 return z.join(",");};dhtmlXTreeObject.prototype.enableSmartXMLParsing=function(mode) {this.slowParse=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.findXML=function(node,par,val){};dhtmlXTreeObject.prototype._getAllCheckedXML=function(p,list,mode){var z=[];if (mode==2)p.through("item","checked",mode,function(c){z.push(c.get("id"));},this);if (mode==1)p.through("item","id",null,function(c){if (c.get("checked")&& (c.get("checked") !=-1))
 z.push(c.get("id"));},this);if (mode==0)p.through("item","id",null,function(c){if (!c.get("checked")|| c.get("checked")==0 )
 z.push(c.get("id"));},this);return list+(list?",":"")+z.join(",");if (list)return list;else return "";};dhtmlXTreeObject.prototype._setSubCheckedXML=function(state,p){var val= state?"1":"";p.through("item","id",null,function(c){c.set("checked",val);},this);};dhtmlXTreeObject.prototype._getAllScraggyItemsXML=function(p,x){var z=[];var fff=function(c){if (!c.sub_exists("item"))
 z.push(c.get("id"));else
 c.each("item",fff,this);};fff(p);return z.join(",");};dhtmlXTreeObject.prototype._getAllFatItemsXML=function(p,x){var z=[];var fff=function(c){if (!c.sub_exists("item"))
 return;z.push(c.get("id"));c.each("item",fff,this);};fff(p);return z.join(",");};dhtmlXTreeObject.prototype._getAllSubItemsXML=function(itemId,z,p){var z=[];p.through("item","id",null,function(c){z.push(c.get("id"));},this)
 return z.join(",");};dhtmlXTreeObject.prototype.reParse=function(node){var that=this;if (!this.parsCount)that.callEvent("onXLS",[that,node.id]);this.xmlstate=1;var tmp=node.unParsed;node.unParsed=0;this.XMLloadingWarning=1;var oldpid=this.parsingOn;var oldmd=this.waitUpdateXML;var oldpa=this.parsedArray;this.parsedArray=new Array();this.waitUpdateXML=false;this.parsingOn=node.id;this.parsedArray=new Array();this.setCheckList="";this._parse(tmp,node.id,2);var chArr=this.setCheckList.split(this.dlmtr);for (var i=0;i<this.parsedArray.length;i++)node.htmlNode.childNodes[0].appendChild(this.parsedArray[i]);if (tmp.get("order")&& tmp.get("order")!="none")
 this._reorderBranch(node,tmp.get("order"),true);this.oldsmcheck=this.smcheck;this.smcheck=false;for (var n=0;n<chArr.length;n++)if (chArr[n])this.setCheck(chArr[n],1);this.smcheck=this.oldsmcheck;this.parsingOn=oldpid;this.waitUpdateXML=oldmd;this.parsedArray=oldpa;this.XMLloadingWarning=0;this._redrawFrom(this,node);if (this._srnd && !node._sready)this.prepareSR(node.id);this.xmlstate=0;return true;};dhtmlXTreeObject.prototype.preParse=function(itemId){if (!itemId || !this._p)return null;var result=false;this._p.clone().through("item","id",itemId,function(c){this._globalIdStorageFind(c.up().get("id"));return result=true;},this);if (result){var n=this._globalIdStorageFind(itemId,true,false);if (!n)dhtmlxError.throwError("getItem","The item "+itemId+" not operable. Seems you have non-unique|incorrect IDs in tree's XML.",itemId);};return n;};dhtmlXTreeObject.prototype._escape=function(str){switch(this.utfesc){case "none":
 return str;break;case "utf8":
 return encodeURIComponent(str);break;default:
 return escape(str);break;}};dhtmlXTreeObject.prototype._drawNewTr=function(htmlObject,node)
 {var tr =document.createElement('tr');var td1=document.createElement('td');var td2=document.createElement('td');td1.appendChild(document.createTextNode(" "));td2.colSpan=3;td2.appendChild(htmlObject);tr.appendChild(td1);tr.appendChild(td2);return tr;};dhtmlXTreeObject.prototype.loadXMLString=function(xmlString,afterCall){var that=this;if (!this.parsCount)this.callEvent("onXLS",[that,null]);this.xmlstate=1;if (afterCall)this.XMLLoader.waitCall=afterCall;this.XMLLoader.loadXMLString(xmlString);};dhtmlXTreeObject.prototype.loadXML=function(file,afterCall){if (this._datamode && this._datamode!="xml")return this["load"+this._datamode.toUpperCase()](file,afterCall);var that=this;if (!this.parsCount)this.callEvent("onXLS",[that,this._ld_id]);this._ld_id=null;this.xmlstate=1;this.XMLLoader=new dtmlXMLLoaderObject(this._parseXMLTree,this,true,this.no_cashe);if (afterCall)this.XMLLoader.waitCall=afterCall;this.XMLLoader.loadXML(file);};dhtmlXTreeObject.prototype._attachChildNode=function(parentObject,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,childs,beforeNode,afterNode){if (beforeNode && beforeNode.parentObject)parentObject=beforeNode.parentObject;if (((parentObject.XMLload==0)&&(this.XMLsource))&&(!this.XMLloadingWarning))
 {parentObject.XMLload=1;this._loadDynXML(parentObject.id);};var Count=parentObject.childsCount;var Nodes=parentObject.childNodes;if (afterNode && afterNode.tr.previousSibling){if (afterNode.tr.previousSibling.previousSibling){beforeNode=afterNode.tr.previousSibling.nodem;}else
 optionStr=optionStr.replace("TOP","")+",TOP";};if (beforeNode){var ik,jk;for (ik=0;ik<Count;ik++)if (Nodes[ik]==beforeNode){for (jk=Count;jk!=ik;jk--)Nodes[1+jk]=Nodes[jk];break;};ik++;Count=ik;};if (optionStr){var tempStr=optionStr.split(",");for (var i=0;i<tempStr.length;i++){switch(tempStr[i])
 {case "TOP": if (parentObject.childsCount>0){beforeNode=new Object;beforeNode.tr=parentObject.childNodes[0].tr.previousSibling;};parentObject._has_top=true;for (ik=Count;ik>0;ik--)Nodes[ik]=Nodes[ik-1];Count=0;break;}}};var n;if (!(n=this._idpull[itemId])|| n.span!=-1){n=Nodes[Count]=new dhtmlXTreeItemObject(itemId,itemText,parentObject,this,itemActionHandler,1);itemId = Nodes[Count].id;parentObject.childsCount++;};if(!n.htmlNode){n.label=itemText;n.htmlNode=this._createItem((this.checkBoxOff?1:0),n);n.htmlNode.objBelong=n;};if(image1)n.images[0]=image1;if(image2)n.images[1]=image2;if(image3)n.images[2]=image3;var tr=this._drawNewTr(n.htmlNode);if ((this.XMLloadingWarning)||(this._hAdI))
 n.htmlNode.parentNode.parentNode.style.display="none";if ((beforeNode)&&(beforeNode.tr.nextSibling))
 parentObject.htmlNode.childNodes[0].insertBefore(tr,beforeNode.tr.nextSibling);else
 if (this.parsingOn==parentObject.id){this.parsedArray[this.parsedArray.length]=tr;}else
 parentObject.htmlNode.childNodes[0].appendChild(tr);if ((beforeNode)&&(!beforeNode.span)) beforeNode=null;if (this.XMLsource)if ((childs)&&(childs!=0)) n.XMLload=0;else n.XMLload=1;n.tr=tr;tr.nodem=n;if (parentObject.itemId==0)tr.childNodes[0].className="hiddenRow";if ((parentObject._r_logic)||(this._frbtr))
 this._setSrc(n.htmlNode.childNodes[0].childNodes[0].childNodes[1].childNodes[0],this.imPath+this.radioArray[0]);if (optionStr){var tempStr=optionStr.split(",");for (var i=0;i<tempStr.length;i++){switch(tempStr[i])
 {case "SELECT": this.selectItem(itemId,false);break;case "CALL": this.selectItem(itemId,true);break;case "CHILD": n.XMLload=0;break;case "CHECKED":
 if (this.XMLloadingWarning)this.setCheckList+=this.dlmtr+itemId;else
 this.setCheck(itemId,1);break;case "HCHECKED":
 this._setCheck(n,"unsure");break;case "OPEN": n.openMe=1;break;}}};if (!this.XMLloadingWarning){if ((this._getOpenState(parentObject)<0)&&(!this._hAdI)) this.openItem(parentObject.id);if (beforeNode){this._correctPlus(beforeNode);this._correctLine(beforeNode);};this._correctPlus(parentObject);this._correctLine(parentObject);this._correctPlus(n);if (parentObject.childsCount>=2){this._correctPlus(Nodes[parentObject.childsCount-2]);this._correctLine(Nodes[parentObject.childsCount-2]);};if (parentObject.childsCount!=2)this._correctPlus(Nodes[0]);if (this.tscheck)this._correctCheckStates(parentObject);if (this._onradh){if (this.xmlstate==1){var old=this.onXLE;this.onXLE=function(id){this._onradh(itemId);if (old)old(id);}}else
 this._onradh(itemId);}};return n;};dhtmlXTreeObject.prototype.enableContextMenu=function(menu){if (menu)this.cMenu=menu;};dhtmlXTreeObject.prototype.setItemContextMenu=function(itemId,cMenu){var l=itemId.toString().split(this.dlmtr);for (var i=0;i<l.length;i++){var temp=this._globalIdStorageFind(l[i]);if (!temp)continue;temp.cMenu=cMenu;}};dhtmlXTreeObject.prototype.insertNewItem=function(parentId,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children){var parentObject=this._globalIdStorageFind(parentId);if (!parentObject)return (-1);var nodez=this._attachChildNode(parentObject,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children);if ((!this.XMLloadingWarning)&&(this.childCalc)) this._fixChildCountLabel(parentObject);return nodez;};dhtmlXTreeObject.prototype.insertNewChild=function(parentId,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children){return this.insertNewItem(parentId,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children);};dhtmlXTreeObject.prototype._parseXMLTree=function(a,b,c,d,xml){var p=new xmlPointer(xml.getXMLTopNode("tree"));a._parse(p);a._p=p;};dhtmlXTreeObject.prototype._parseItem=function(c,temp,preNode,befNode){var id;if (this._srnd && (!this._idpull[id=c.get("id")] || !this._idpull[id].span))
 {this._addItemSRND(temp.id,id,c);return;};var a=c.get_all();if ((typeof(this.waitUpdateXML)=="object")&&(!this.waitUpdateXML[a.id])){this._parse(c,a.id,1);return;};if ((a.text===null)||(typeof(a.text)=="undefined")){a.text=c.sub("itemtext");if (a.text)a.text=a.text.content();};var zST=[];if (a.select)zST.push("SELECT");if (a.top)zST.push("TOP");if (a.call)this.nodeAskingCall=a.id;if (a.checked==-1)zST.push("HCHECKED");else if (a.checked)zST.push("CHECKED");if (a.open)zST.push("OPEN");if (this.waitUpdateXML){if (this._globalIdStorageFind(a.id))
 var newNode=this.updateItem(a.id,a.text,a.im0,a.im1,a.im2,a.checked);else{if (this.npl==0)zST.push("TOP");else preNode=temp.childNodes[this.npl];var newNode=this._attachChildNode(temp,a.id,a.text,0,a.im0,a.im1,a.im2,zST.join(","),a.child,0,preNode);preNode=null;}}else
 var newNode=this._attachChildNode(temp,a.id,a.text,0,a.im0,a.im1,a.im2,zST.join(","),a.child,(befNode||0),preNode);if (a.tooltip)newNode.span.parentNode.parentNode.title=a.tooltip;if (a.style)if (newNode.span.style.cssText)newNode.span.style.cssText+=(";"+a.style);else
 newNode.span.setAttribute("style",newNode.span.getAttribute("style")+";"+a.style);if (a.radio)newNode._r_logic=true;if (a.nocheckbox){var check_node=newNode.span.parentNode.previousSibling.previousSibling;check_node.childNodes[0].style.display='none';if (window._KHTMLrv)check_node.style.display="none";newNode.nocheckbox=true;};if (a.disabled){if (a.checked!=null)this._setCheck(newNode,a.checked);this.disableCheckbox(newNode,1);};newNode._acc=a.child||0;if (this.parserExtension)this.parserExtension._parseExtension.call(this,c,a,(temp?temp.id:0));this.setItemColor(newNode,a.aCol,a.sCol);if (a.locked=="1")this.lockItem(newNode.id,true,true);if ((a.imwidth)||(a.imheight)) this.setIconSize(a.imwidth,a.imheight,newNode);if ((a.closeable=="0")||(a.closeable=="1")) this.setItemCloseable(newNode,a.closeable);var zcall="";if (a.topoffset)this.setItemTopOffset(newNode,a.topoffset);if ((!this.slowParse)||(typeof(this.waitUpdateXML)=="object")){if (c.sub_exists("item"))
 zcall=this._parse(c,a.id,1);}else {if ((!newNode.childsCount)&& c.sub_exists("item"))
 newNode.unParsed=c.clone();c.each("userdata",function(u){this.setUserData(a.id,u.get("name"),u.content());},this);};if (zcall!="")this.nodeAskingCall=zcall;c.each("userdata",function(u){this.setUserData(c.get("id"),u.get("name"),u.content());},this)
 
 
 };dhtmlXTreeObject.prototype._parse=function(p,parentId,level,start){if (this._srnd && !this.parentObject.offsetHeight){var self=this;return window.setTimeout(function(){self._parse(p,parentId,level,start);},100);};if (!p.exists()) return;this.skipLock=true;if (!parentId){parentId=p.get("id");if (p.get("radio"))
 this.htmlNode._r_logic=true;this.parsingOn=parentId;this.parsedArray=new Array();this.setCheckList="";this.nodeAskingCall="";};var temp=this._globalIdStorageFind(parentId);if (!temp)return dhtmlxError.throwError("DataStructure","XML refers to not existing parent");this.parsCount=this.parsCount?(this.parsCount+1):1;this.XMLloadingWarning=1;if ((temp.childsCount)&&(!start)&&(!this._edsbps)&&(!temp._has_top))
 var preNode=0;else
 var preNode=0;this.npl=0;p.each("item",function(c,i){temp.XMLload=1;if ((this._epgps)&&(this._epgpsC==this.npl)){this._setNextPageSign(temp,this.npl+1*(start||0),level,node);return -1;};this._parseItem(c,temp,0,preNode);if ((this._edsbps)&&(this.npl==this._edsbpsC)){this._distributedStart(p,i+1,parentId,level,temp.childsCount);return -1;};this.npl++;},this,start);if (!level){p.each("userdata",function(u){this.setUserData(p.get("id"),u.get("name"),u.content());},this);temp.XMLload=1;if (this.waitUpdateXML){this.waitUpdateXML=false;for (var i=temp.childsCount-1;i>=0;i--)if (temp.childNodes[i]._dmark)this.deleteItem(temp.childNodes[i].id);};var parsedNodeTop=this._globalIdStorageFind(this.parsingOn);for (var i=0;i<this.parsedArray.length;i++)temp.htmlNode.childNodes[0].appendChild(this.parsedArray[i]);this.parsedArray = [];this.lastLoadedXMLId=parentId;this.XMLloadingWarning=0;var chArr=this.setCheckList.split(this.dlmtr);for (var n=0;n<chArr.length;n++)if (chArr[n])this.setCheck(chArr[n],1);if ((this.XMLsource)&&(this.tscheck)&&(this.smcheck)&&(temp.id!=this.rootId)){if (temp.checkstate===0)this._setSubChecked(0,temp);else if (temp.checkstate===1)this._setSubChecked(1,temp);};this._redrawFrom(this,null,start)
 if (p.get("order")&& p.get("order")!="none")
 this._reorderBranch(temp,p.get("order"),true);if (this.nodeAskingCall!="")this.callEvent("onClick",[this.nodeAskingCall,this.getSelectedItemId()]);if (this._branchUpdate)this._branchUpdateNext(p);};if (this.parsCount==1){this.parsingOn=null;if (this._srnd && temp.id!=this.rootId){this.prepareSR(temp.id);if (this.XMLsource)this.openItem(temp.id)
 };p.through("item","open",null,function(c){this.openItem(c.get("id"));},this);if ((!this._edsbps)||(!this._edsbpsA.length)){var that=this;window.setTimeout( function(){that.callEvent("onXLE",[that,parentId]);},1);this.xmlstate=0;};this.skipLock=false;};this.parsCount--;var that=this;if (this._edsbps)window.setTimeout(function(){that._distributedStep(parentId);},this._edsbpsD);if ((this._epgps)&&(start))
 this._setPrevPageSign(temp,(start||0),level,node);if (!level && this.onXLE)this.onXLE(this,parentId);return this.nodeAskingCall;};dhtmlXTreeObject.prototype._branchUpdateNext=function(p){p.each("item",function(c){var nid=c.get("id");if (this._idpull[nid] && (!this._idpull[nid].XMLload)) return;this._branchUpdate++;this.smartRefreshItem(c.get("id"),c);},this)
 this._branchUpdate--;};dhtmlXTreeObject.prototype.checkUserData=function(node,parentId){if ((node.nodeType==1)&&(node.tagName == "userdata"))
 {var name=node.getAttribute("name");if ((name)&&(node.childNodes[0]))
 this.setUserData(parentId,name,node.childNodes[0].data);}};dhtmlXTreeObject.prototype._redrawFrom=function(dhtmlObject,itemObject,start,visMode){if (!itemObject){var tempx=dhtmlObject._globalIdStorageFind(dhtmlObject.lastLoadedXMLId);dhtmlObject.lastLoadedXMLId=-1;if (!tempx)return 0;}else tempx=itemObject;var acc=0;for (var i=(start?start-1:0);i<tempx.childsCount;i++)
 {if ((!this._branchUpdate)||(this._getOpenState(tempx)==1))
 if ((!itemObject)||(visMode==1)) tempx.childNodes[i].htmlNode.parentNode.parentNode.style.display="";if (tempx.childNodes[i].openMe==1){this._openItem(tempx.childNodes[i]);tempx.childNodes[i].openMe=0;};dhtmlObject._redrawFrom(dhtmlObject,tempx.childNodes[i]);if (this.childCalc!=null){if ((tempx.childNodes[i].unParsed)||((!tempx.childNodes[i].XMLload)&&(this.XMLsource)))
 {if (tempx.childNodes[i]._acc)tempx.childNodes[i].span.innerHTML=tempx.childNodes[i].label+this.htmlcA+tempx.childNodes[i]._acc+this.htmlcB;else
 tempx.childNodes[i].span.innerHTML=tempx.childNodes[i].label;};if ((tempx.childNodes[i].childNodes.length)&&(this.childCalc))
 {if (this.childCalc==1){tempx.childNodes[i].span.innerHTML=tempx.childNodes[i].label+this.htmlcA+tempx.childNodes[i].childsCount+this.htmlcB;};if (this.childCalc==2){var zCount=tempx.childNodes[i].childsCount-(tempx.childNodes[i].pureChilds||0);if (zCount)tempx.childNodes[i].span.innerHTML=tempx.childNodes[i].label+this.htmlcA+zCount+this.htmlcB;if (tempx.pureChilds)tempx.pureChilds++;else tempx.pureChilds=1;};if (this.childCalc==3){tempx.childNodes[i].span.innerHTML=tempx.childNodes[i].label+this.htmlcA+tempx.childNodes[i]._acc+this.htmlcB;};if (this.childCalc==4){var zCount=tempx.childNodes[i]._acc;if (zCount)tempx.childNodes[i].span.innerHTML=tempx.childNodes[i].label+this.htmlcA+zCount+this.htmlcB;}}else if (this.childCalc==4){acc++;};acc+=tempx.childNodes[i]._acc;if (this.childCalc==3){acc++;}}};if ((!tempx.unParsed)&&((tempx.XMLload)||(!this.XMLsource)))
 tempx._acc=acc;dhtmlObject._correctLine(tempx);dhtmlObject._correctPlus(tempx);if ((this.childCalc)&&(!itemObject)) dhtmlObject._fixChildCountLabel(tempx);};dhtmlXTreeObject.prototype._createSelf=function(){var div=document.createElement('div');div.className="containerTableStyle";div.style.width=this.width;div.style.height=this.height;this.parentObject.appendChild(div);return div;};dhtmlXTreeObject.prototype._xcloseAll=function(itemObject)
 {if (itemObject.unParsed)return;if (this.rootId!=itemObject.id){if (!itemObject.htmlNode)return;var Nodes=itemObject.htmlNode.childNodes[0].childNodes;var Count=Nodes.length;for (var i=1;i<Count;i++)Nodes[i].style.display="none";this._correctPlus(itemObject);};for (var i=0;i<itemObject.childsCount;i++)if (itemObject.childNodes[i].childsCount)this._xcloseAll(itemObject.childNodes[i]);};dhtmlXTreeObject.prototype._xopenAll=function(itemObject)
 {this._HideShow(itemObject,2);for (var i=0;i<itemObject.childsCount;i++)this._xopenAll(itemObject.childNodes[i]);};dhtmlXTreeObject.prototype._correctPlus=function(itemObject){if (!itemObject.htmlNode)return;var imsrc=itemObject.htmlNode.childNodes[0].childNodes[0].childNodes[0].lastChild;var imsrc2=itemObject.htmlNode.childNodes[0].childNodes[0].childNodes[2].childNodes[0];var workArray=this.lineArray;if ((this.XMLsource)&&(!itemObject.XMLload))
 {var workArray=this.plusArray;this._setSrc(imsrc2,this.iconURL+itemObject.images[2]);if (this._txtimg)return (imsrc.innerHTML="[+]");}else
 if ((itemObject.childsCount)||(itemObject.unParsed))
 {if ((itemObject.htmlNode.childNodes[0].childNodes[1])&&( itemObject.htmlNode.childNodes[0].childNodes[1].style.display!="none" ))
 {if (!itemObject.wsign)var workArray=this.minusArray;this._setSrc(imsrc2,this.iconURL+itemObject.images[1]);if (this._txtimg)return (imsrc.innerHTML="[-]");}else
 {if (!itemObject.wsign)var workArray=this.plusArray;this._setSrc(imsrc2,this.iconURL+itemObject.images[2]);if (this._txtimg)return (imsrc.innerHTML="[+]");}}else
 {this._setSrc(imsrc2,this.iconURL+itemObject.images[0]);};var tempNum=2;if (!itemObject.treeNod.treeLinesOn)this._setSrc(imsrc,this.imPath+workArray[3]);else {if (itemObject.parentObject)tempNum=this._getCountStatus(itemObject.id,itemObject.parentObject);this._setSrc(imsrc,this.imPath+workArray[tempNum]);}};dhtmlXTreeObject.prototype._correctLine=function(itemObject){if (!itemObject.htmlNode)return;var sNode=itemObject.parentObject;if (sNode)if ((this._getLineStatus(itemObject.id,sNode)==0)||(!this.treeLinesOn))
 for(var i=1;i<=itemObject.childsCount;i++){if (!itemObject.htmlNode.childNodes[0].childNodes[i])break;itemObject.htmlNode.childNodes[0].childNodes[i].childNodes[0].style.backgroundImage="";itemObject.htmlNode.childNodes[0].childNodes[i].childNodes[0].style.backgroundRepeat="";}else
 for(var i=1;i<=itemObject.childsCount;i++){if (!itemObject.htmlNode.childNodes[0].childNodes[i])break;itemObject.htmlNode.childNodes[0].childNodes[i].childNodes[0].style.backgroundImage="url("+this.imPath+this.lineArray[5]+")";itemObject.htmlNode.childNodes[0].childNodes[i].childNodes[0].style.backgroundRepeat="repeat-y";}};dhtmlXTreeObject.prototype._getCountStatus=function(itemId,itemObject){if (itemObject.childsCount<=1){if (itemObject.id==this.rootId)return 4;else return 0;};if (itemObject.childNodes[0].id==itemId)if (itemObject.id==this.rootId)return 2;else return 1;if (itemObject.childNodes[itemObject.childsCount-1].id==itemId)return 0;return 1;};dhtmlXTreeObject.prototype._getLineStatus =function(itemId,itemObject){if (itemObject.childNodes[itemObject.childsCount-1].id==itemId)return 0;return 1;};dhtmlXTreeObject.prototype._HideShow=function(itemObject,mode){if ((this.XMLsource)&&(!itemObject.XMLload)) {if (mode==1)return;itemObject.XMLload=1;this._loadDynXML(itemObject.id);return;};if (itemObject.unParsed)this.reParse(itemObject);var Nodes=itemObject.htmlNode.childNodes[0].childNodes;var Count=Nodes.length;if (Count>1){if ( ( (Nodes[1].style.display!="none")|| (mode==1) ) && (mode!=2) ) {this.allTree.childNodes[0].border = "1";this.allTree.childNodes[0].border = "0";nodestyle="none";}else nodestyle="";for (var i=1;i<Count;i++)Nodes[i].style.display=nodestyle;};this._correctPlus(itemObject);};dhtmlXTreeObject.prototype._getOpenState=function(itemObject){if (!itemObject.htmlNode)return 0;var z=itemObject.htmlNode.childNodes[0].childNodes;if (z.length<=1)return 0;if (z[1].style.display!="none")return 1;else return -1;};dhtmlXTreeObject.prototype.onRowClick2=function(){var that=this.parentObject.treeNod;if (!that.callEvent("onDblClick",[this.parentObject.id,that])) return false;if ((this.parentObject.closeble)&&(this.parentObject.closeble!="0"))
 that._HideShow(this.parentObject);else
 that._HideShow(this.parentObject,2);if (that.checkEvent("onOpenEnd"))
 if (!that.xmlstate)that.callEvent("onOpenEnd",[this.parentObject.id,that._getOpenState(this.parentObject)]);else{that._oie_onXLE.push(that.onXLE);that.onXLE=that._epnFHe;};return false;};dhtmlXTreeObject.prototype.onRowClick=function(){var that=this.parentObject.treeNod;if (!that.callEvent("onOpenStart",[this.parentObject.id,that._getOpenState(this.parentObject)])) return 0;if ((this.parentObject.closeble)&&(this.parentObject.closeble!="0"))
 that._HideShow(this.parentObject);else
 that._HideShow(this.parentObject,2);if (that.checkEvent("onOpenEnd"))
 if (!that.xmlstate)that.callEvent("onOpenEnd",[this.parentObject.id,that._getOpenState(this.parentObject)]);else{that._oie_onXLE.push(that.onXLE);that.onXLE=that._epnFHe;}};dhtmlXTreeObject.prototype._epnFHe=function(that,id,flag){if (id!=this.rootId)this.callEvent("onOpenEnd",[id,that.getOpenState(id)]);that.onXLE=that._oie_onXLE.pop();if (!flag && !that._oie_onXLE.length)if (that.onXLE)that.onXLE(that,id);};dhtmlXTreeObject.prototype.onRowClickDown=function(e){e=e||window.event;var that=this.parentObject.treeNod;that._selectItem(this.parentObject,e);};dhtmlXTreeObject.prototype.getSelectedItemId=function()
 {var str=new Array();for (var i=0;i<this._selected.length;i++)str[i]=this._selected[i].id;return (str.join(this.dlmtr));};dhtmlXTreeObject.prototype._selectItem=function(node,e){if (this.checkEvent("onSelect")) this._onSSCFold=this.getSelectedItemId();if ((!this._amsel)||(!e)||((!e.ctrlKey)&&(!e.metaKey)&&(!e.shiftKey)))


 this._unselectItems();if ((node.i_sel)&&(this._amsel)&&(e)&&(e.ctrlKey || e.metaKey))
 this._unselectItem(node);else
 if ((!node.i_sel)&&((!this._amselS)||(this._selected.length==0)||(this._selected[0].parentObject==node.parentObject)))
 if ((this._amsel)&&(e)&&(e.shiftKey)&&(this._selected.length!=0)&&(this._selected[this._selected.length-1].parentObject==node.parentObject)){var a=this._getIndex(this._selected[this._selected.length-1]);var b=this._getIndex(node);if (b<a){var c=a;a=b;b=c;};for (var i=a;i<=b;i++)if (!node.parentObject.childNodes[i].i_sel)this._markItem(node.parentObject.childNodes[i]);}else


 this._markItem(node);if (this.checkEvent("onSelect")) {var z=this.getSelectedItemId();if (z!=this._onSSCFold)this.callEvent("onSelect",[z]);}};dhtmlXTreeObject.prototype._markItem=function(node){if (node.scolor)node.span.style.color=node.scolor;node.span.className="selectedTreeRow";node.i_sel=true;this._selected[this._selected.length]=node;};dhtmlXTreeObject.prototype.getIndexById=function(itemId){var z=this._globalIdStorageFind(itemId);if (!z)return null;return this._getIndex(z);};dhtmlXTreeObject.prototype._getIndex=function(w){var z=w.parentObject;for (var i=0;i<z.childsCount;i++)if (z.childNodes[i]==w)return i;};dhtmlXTreeObject.prototype._unselectItem=function(node){if ((node)&&(node.i_sel))
 {node.span.className="standartTreeRow";if (node.acolor)node.span.style.color=node.acolor;node.i_sel=false;for (var i=0;i<this._selected.length;i++)if (!this._selected[i].i_sel){this._selected.splice(i,1);break;}}};dhtmlXTreeObject.prototype._unselectItems=function(){for (var i=0;i<this._selected.length;i++){var node=this._selected[i];node.span.className="standartTreeRow";if (node.acolor)node.span.style.color=node.acolor;node.i_sel=false;};this._selected=new Array();};dhtmlXTreeObject.prototype.onRowSelect=function(e,htmlObject,mode){e=e||window.event;var obj=this.parentObject;if (htmlObject)obj=htmlObject.parentObject;var that=obj.treeNod;var lastId=that.getSelectedItemId();if ((!e)||(!e.skipUnSel))
 that._selectItem(obj,e);if (!mode){if (obj.actionHandler)obj.actionHandler(obj.id,lastId);else that.callEvent("onClick",[obj.id,lastId]);}};dhtmlXTreeObject.prototype._correctCheckStates=function(dhtmlObject){if (!this.tscheck)return;if (!dhtmlObject)return;if (dhtmlObject.id==this.rootId)return;var act=dhtmlObject.childNodes;var flag1=0;var flag2=0;if (dhtmlObject.childsCount==0)return;for (var i=0;i<dhtmlObject.childsCount;i++){if (act[i].dscheck)continue;if (act[i].checkstate==0)flag1=1;else if (act[i].checkstate==1)flag2=1;else {flag1=1;flag2=1;break;}};if ((flag1)&&(flag2)) this._setCheck(dhtmlObject,"unsure");else if (flag1)this._setCheck(dhtmlObject,false);else this._setCheck(dhtmlObject,true);this._correctCheckStates(dhtmlObject.parentObject);};dhtmlXTreeObject.prototype.onCheckBoxClick=function(e){if (!this.treeNod.callEvent("onBeforeCheck",[this.parentObject.id,this.parentObject.checkstate]))
 return;if (this.parentObject.dscheck)return true;if (this.treeNod.tscheck)if (this.parentObject.checkstate==1)this.treeNod._setSubChecked(false,this.parentObject);else this.treeNod._setSubChecked(true,this.parentObject);else
 if (this.parentObject.checkstate==1)this.treeNod._setCheck(this.parentObject,false);else this.treeNod._setCheck(this.parentObject,true);this.treeNod._correctCheckStates(this.parentObject.parentObject);return this.treeNod.callEvent("onCheck",[this.parentObject.id,this.parentObject.checkstate]);};dhtmlXTreeObject.prototype._createItem=function(acheck,itemObject,mode){var table=document.createElement('table');table.cellSpacing=0;table.cellPadding=0;table.border=0;if(this.hfMode)table.style.tableLayout="fixed";table.style.margin=0;table.style.padding=0;var tbody=document.createElement('tbody');var tr=document.createElement('tr');var td1=document.createElement('td');td1.className="standartTreeImage";if(this._txtimg){var img0=document.createElement("div");td1.appendChild(img0);img0.className="dhx_tree_textSign";}else
 {var img0=this._getImg(itemObject.id);img0.border="0";if (img0.tagName=="IMG")img0.align="absmiddle";td1.appendChild(img0);img0.style.padding=0;img0.style.margin=0;img0.style.width=this.def_line_img_x;img0.style.height=this.def_line_img_y;};var td11=document.createElement('td');var inp=this._getImg(this.cBROf?this.rootId:itemObject.id);inp.checked=0;this._setSrc(inp,this.imPath+this.checkArray[0]);inp.style.width="16px";inp.style.height="16px";if (!acheck)((!_isIE)?td11:inp).style.display="none";td11.appendChild(inp);if ((!this.cBROf)&&(inp.tagName=="IMG")) inp.align="absmiddle";inp.onclick=this.onCheckBoxClick;inp.treeNod=this;inp.parentObject=itemObject;if (!window._KHTMLrv)td11.width="20px";else td11.width="16px";var td12=document.createElement('td');td12.className="standartTreeImage";var img=this._getImg(this.timgen?itemObject.id:this.rootId);img.onmousedown=this._preventNsDrag;img.ondragstart=this._preventNsDrag;img.border="0";if (this._aimgs){img.parentObject=itemObject;if (img.tagName=="IMG")img.align="absmiddle";img.onclick=this.onRowSelect;};if (!mode)this._setSrc(img,this.iconURL+this.imageArray[0]);td12.appendChild(img);img.style.padding=0;img.style.margin=0;if (this.timgen){td12.style.width=img.style.width=this.def_img_x;img.style.height=this.def_img_y;}else
 {img.style.width="0px";img.style.height="0px";if (_isOpera || window._KHTMLrv )td12.style.display="none";};var td2=document.createElement('td');td2.className="standartTreeRow";itemObject.span=document.createElement('span');itemObject.span.className="standartTreeRow";if (this.mlitems){itemObject.span.style.width=this.mlitems;itemObject.span.style.display="block";}else td2.noWrap=true;if (_isIE && _isIE>7)td2.style.width="999999px";else if (!window._KHTMLrv)td2.style.width="100%";itemObject.span.innerHTML=itemObject.label;td2.appendChild(itemObject.span);td2.parentObject=itemObject;td1.parentObject=itemObject;td2.onclick=this.onRowSelect;td1.onclick=this.onRowClick;td2.ondblclick=this.onRowClick2;if (this.ettip)tr.title=itemObject.label;if (this.dragAndDropOff){if (this._aimgs){this.dragger.addDraggableItem(td12,this);td12.parentObject=itemObject;};this.dragger.addDraggableItem(td2,this);};itemObject.span.style.paddingLeft="5px";itemObject.span.style.paddingRight="5px";td2.style.verticalAlign="";td2.style.fontSize="10pt";td2.style.cursor=this.style_pointer;tr.appendChild(td1);tr.appendChild(td11);tr.appendChild(td12);tr.appendChild(td2);tbody.appendChild(tr);table.appendChild(tbody);if (this.ehlt || this.checkEvent("onMouseIn")|| this.checkEvent("onMouseOut")){tr.onmousemove=this._itemMouseIn;tr[(_isIE)?"onmouseleave":"onmouseout"]=this._itemMouseOut;};return table;};dhtmlXTreeObject.prototype.setImagePath=function( newPath ){this.imPath=newPath;this.iconURL=newPath;};dhtmlXTreeObject.prototype.setIconPath=function(path){this.iconURL=path;};dhtmlXTreeObject.prototype._getLeafCount=function(itemNode){var a=0;for (var b=0;b<itemNode.childsCount;b++)if (itemNode.childNodes[b].childsCount==0)a++;return a;};dhtmlXTreeObject.prototype._getChildCounterValue=function(itemId){var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;if ((temp.unParsed)||((!temp.XMLload)&&(this.XMLsource)))
 return temp._acc
 switch(this.childCalc)
 {case 1: return temp.childsCount;break;case 2: return this._getLeafCount(temp);break;case 3: return temp._acc;break;case 4: return temp._acc;break;}};dhtmlXTreeObject.prototype._fixChildCountLabel=function(itemNode,index){if (this.childCalc==null)return;if ((itemNode.unParsed)||((!itemNode.XMLload)&&(this.XMLsource)))
 {if (itemNode._acc)itemNode.span.innerHTML=itemNode.label+this.htmlcA+itemNode._acc+this.htmlcB;else
 itemNode.span.innerHTML=itemNode.label;return;};switch(this.childCalc){case 1:
 if (itemNode.childsCount!=0)itemNode.span.innerHTML=itemNode.label+this.htmlcA+itemNode.childsCount+this.htmlcB;else itemNode.span.innerHTML=itemNode.label;break;case 2:
 var z=this._getLeafCount(itemNode);if (z!=0)itemNode.span.innerHTML=itemNode.label+this.htmlcA+z+this.htmlcB;else itemNode.span.innerHTML=itemNode.label;break;case 3:
 if (itemNode.childsCount!=0){var bcc=0;for (var a=0;a<itemNode.childsCount;a++){if (!itemNode.childNodes[a]._acc)itemNode.childNodes[a]._acc=0;bcc+=itemNode.childNodes[a]._acc*1;};bcc+=itemNode.childsCount*1;itemNode.span.innerHTML=itemNode.label+this.htmlcA+bcc+this.htmlcB;itemNode._acc=bcc;}else {itemNode.span.innerHTML=itemNode.label;itemNode._acc=0;};if ((itemNode.parentObject)&&(itemNode.parentObject!=this.htmlNode))
 this._fixChildCountLabel(itemNode.parentObject);break;case 4:
 if (itemNode.childsCount!=0){var bcc=0;for (var a=0;a<itemNode.childsCount;a++){if (!itemNode.childNodes[a]._acc)itemNode.childNodes[a]._acc=1;bcc+=itemNode.childNodes[a]._acc*1;};itemNode.span.innerHTML=itemNode.label+this.htmlcA+bcc+this.htmlcB;itemNode._acc=bcc;}else {itemNode.span.innerHTML=itemNode.label;itemNode._acc=1;};if ((itemNode.parentObject)&&(itemNode.parentObject!=this.htmlNode))
 this._fixChildCountLabel(itemNode.parentObject);break;}};dhtmlXTreeObject.prototype.setChildCalcMode=function( mode ){switch(mode){case "child": this.childCalc=1;break;case "leafs": this.childCalc=2;break;case "childrec": this.childCalc=3;break;case "leafsrec": this.childCalc=4;break;case "disabled": this.childCalc=null;break;default: this.childCalc=4;}};dhtmlXTreeObject.prototype.setChildCalcHTML=function( htmlA,htmlB ){this.htmlcA=htmlA;this.htmlcB=htmlB;};dhtmlXTreeObject.prototype.setOnRightClickHandler=function(func){this.attachEvent("onRightClick",func);};dhtmlXTreeObject.prototype.setOnClickHandler=function(func){this.attachEvent("onClick",func);};dhtmlXTreeObject.prototype.setOnSelectStateChange=function(func){this.attachEvent("onSelect",func);};dhtmlXTreeObject.prototype.setXMLAutoLoading=function(filePath){this.XMLsource=filePath;};dhtmlXTreeObject.prototype.setOnCheckHandler=function(func){this.attachEvent("onCheck",func);};dhtmlXTreeObject.prototype.setOnOpenHandler=function(func){this.attachEvent("onOpenStart",func);};dhtmlXTreeObject.prototype.setOnOpenStartHandler=function(func){this.attachEvent("onOpenStart",func);};dhtmlXTreeObject.prototype.setOnOpenEndHandler=function(func){this.attachEvent("onOpenEnd",func);};dhtmlXTreeObject.prototype.setOnDblClickHandler=function(func){this.attachEvent("onDblClick",func);};dhtmlXTreeObject.prototype.openAllItems=function(itemId)
 {var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;this._xopenAll(temp);};dhtmlXTreeObject.prototype.getOpenState=function(itemId){var temp=this._globalIdStorageFind(itemId);if (!temp)return "";return this._getOpenState(temp);};dhtmlXTreeObject.prototype.closeAllItems=function(itemId)
 {if (itemId===window.undefined)itemId=this.rootId;var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;this._xcloseAll(temp);this.allTree.childNodes[0].border = "1";this.allTree.childNodes[0].border = "0";};dhtmlXTreeObject.prototype.setUserData=function(itemId,name,value){var sNode=this._globalIdStorageFind(itemId,0,true);if (!sNode)return;if(name=="hint")sNode.htmlNode.childNodes[0].childNodes[0].title=value;if (typeof(sNode.userData["t_"+name])=="undefined"){if (!sNode._userdatalist)sNode._userdatalist=name;else sNode._userdatalist+=","+name;};sNode.userData["t_"+name]=value;};dhtmlXTreeObject.prototype.getUserData=function(itemId,name){var sNode=this._globalIdStorageFind(itemId,0,true);if (!sNode)return;return sNode.userData["t_"+name];};dhtmlXTreeObject.prototype.getItemColor=function(itemId)
 {var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;var res= new Object();if (temp.acolor)res.acolor=temp.acolor;if (temp.scolor)res.scolor=temp.scolor;return res;};dhtmlXTreeObject.prototype.setItemColor=function(itemId,defaultColor,selectedColor)
 {if ((itemId)&&(itemId.span))
 var temp=itemId;else
 var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;else {if (temp.i_sel){if (selectedColor)temp.span.style.color=selectedColor;}else
 {if (defaultColor)temp.span.style.color=defaultColor;};if (selectedColor)temp.scolor=selectedColor;if (defaultColor)temp.acolor=defaultColor;}};dhtmlXTreeObject.prototype.getItemText=function(itemId)
 {var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;return(temp.htmlNode.childNodes[0].childNodes[0].childNodes[3].childNodes[0].innerHTML);};dhtmlXTreeObject.prototype.getParentId=function(itemId)
 {var temp=this._globalIdStorageFind(itemId);if ((!temp)||(!temp.parentObject)) return "";return temp.parentObject.id;};dhtmlXTreeObject.prototype.changeItemId=function(itemId,newItemId)
 {if (itemId==newItemId)return;var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;temp.id=newItemId;temp.span.contextMenuId=newItemId;this._idpull[newItemId]=this._idpull[itemId];delete this._idpull[itemId];};dhtmlXTreeObject.prototype.doCut=function(){if (this.nodeCut)this.clearCut();this.nodeCut=(new Array()).concat(this._selected);for (var i=0;i<this.nodeCut.length;i++){var tempa=this.nodeCut[i];tempa._cimgs=new Array();tempa._cimgs[0]=tempa.images[0];tempa._cimgs[1]=tempa.images[1];tempa._cimgs[2]=tempa.images[2];tempa.images[0]=tempa.images[1]=tempa.images[2]=this.cutImage;this._correctPlus(tempa);}};dhtmlXTreeObject.prototype.doPaste=function(itemId){var tobj=this._globalIdStorageFind(itemId);if (!tobj)return 0;for (var i=0;i<this.nodeCut.length;i++){if (this._checkPNodes(tobj,this.nodeCut[i])) continue;this._moveNode(this.nodeCut[i],tobj);};this.clearCut();};dhtmlXTreeObject.prototype.clearCut=function(){for (var i=0;i<this.nodeCut.length;i++){var tempa=this.nodeCut[i];tempa.images[0]=tempa._cimgs[0];tempa.images[1]=tempa._cimgs[1];tempa.images[2]=tempa._cimgs[2];this._correctPlus(tempa);};this.nodeCut=new Array();};dhtmlXTreeObject.prototype._moveNode=function(itemObject,targetObject){var mode=this.dadmodec;if (mode==1){var z=targetObject;if (this.dadmodefix<0){while (true){z=this._getPrevNode(z);if ((z==-1)) {z=this.htmlNode;break;};if ((z.tr==0)||(z.tr.style.display=="")||(!z.parentObject)) break;};var nodeA=z;var nodeB=targetObject;}else
 {while (true){z=this._getNextNode(z);if ((z==-1)) {z=this.htmlNode;break;};if ((z.tr.style.display=="")||(!z.parentObject)) break;};var nodeB=z;var nodeA=targetObject;};if (this._getNodeLevel(nodeA,0)>this._getNodeLevel(nodeB,0))
 {if (!this.dropLower)return this._moveNodeTo(itemObject,nodeA.parentObject);else
 if (nodeB.id!=this.rootId)return this._moveNodeTo(itemObject,nodeB.parentObject,nodeB);else
 return this._moveNodeTo(itemObject,this.htmlNode,null);}else
 {return this._moveNodeTo(itemObject,nodeB.parentObject,nodeB);}}else


 return this._moveNodeTo(itemObject,targetObject);};dhtmlXTreeObject.prototype._fixNodesCollection=function(target,zParent){var flag=0;var icount=0;var Nodes=target.childNodes;var Count=target.childsCount-1;if (zParent==Nodes[Count])return;for (var i=0;i<Count;i++)if (Nodes[i]==Nodes[Count]){Nodes[i]=Nodes[i+1];Nodes[i+1]=Nodes[Count];};for (var i=0;i<Count+1;i++){if (flag){var temp=Nodes[i];Nodes[i]=flag;flag=temp;}else 
 if (Nodes[i]==zParent){flag=Nodes[i];Nodes[i]=Nodes[Count];}}};dhtmlXTreeObject.prototype._recreateBranch=function(itemObject,targetObject,beforeNode,level){var i;var st="";if (beforeNode){for (i=0;i<targetObject.childsCount;i++)if (targetObject.childNodes[i]==beforeNode)break;if (i!=0)beforeNode=targetObject.childNodes[i-1];else{st="TOP";beforeNode="";}};var t2=this._onradh;this._onradh=null;var newNode=this._attachChildNode(targetObject,itemObject.id,itemObject.label,0,itemObject.images[0],itemObject.images[1],itemObject.images[2],st,0,beforeNode);newNode._userdatalist=itemObject._userdatalist;newNode.userData=itemObject.userData.clone();newNode.XMLload=itemObject.XMLload;if (t2){this._onradh=t2;this._onradh(newNode.id);};if (itemObject.treeNod.dpcpy)itemObject.treeNod._globalIdStorageFind(itemObject.id);else newNode.unParsed=itemObject.unParsed;this._correctPlus(newNode);for (var i=0;i<itemObject.childsCount;i++)this._recreateBranch(itemObject.childNodes[i],newNode,0,1);if ((!level)&&(this.childCalc)) {this._redrawFrom(this,targetObject);};return newNode;};dhtmlXTreeObject.prototype._moveNodeTo=function(itemObject,targetObject,beforeNode){if (itemObject.treeNod._nonTrivialNode)return itemObject.treeNod._nonTrivialNode(this,targetObject,beforeNode,itemObject);if (targetObject.mytype)var framesMove=(itemObject.treeNod.lWin!=targetObject.lWin);else
 var framesMove=(itemObject.treeNod.lWin!=targetObject.treeNod.lWin);if (!this.callEvent("onDrag",[itemObject.id,targetObject.id,(beforeNode?beforeNode.id:null),itemObject.treeNod,targetObject.treeNod])) return false;if ((targetObject.XMLload==0)&&(this.XMLsource))
 {targetObject.XMLload=1;this._loadDynXML(targetObject.id);};this.openItem(targetObject.id);var oldTree=itemObject.treeNod;var c=itemObject.parentObject.childsCount;var z=itemObject.parentObject;if ((framesMove)||(oldTree.dpcpy)) {var _otiid=itemObject.id;itemObject=this._recreateBranch(itemObject,targetObject,beforeNode);if (!oldTree.dpcpy)oldTree.deleteItem(_otiid);}else
 {var Count=targetObject.childsCount;var Nodes=targetObject.childNodes;if (Count==0)targetObject._open=true;oldTree._unselectItem(itemObject);Nodes[Count]=itemObject;itemObject.treeNod=targetObject.treeNod;targetObject.childsCount++;var tr=this._drawNewTr(Nodes[Count].htmlNode);if (!beforeNode){targetObject.htmlNode.childNodes[0].appendChild(tr);if (this.dadmode==1)this._fixNodesCollection(targetObject,beforeNode);}else
 {targetObject.htmlNode.childNodes[0].insertBefore(tr,beforeNode.tr);this._fixNodesCollection(targetObject,beforeNode);Nodes=targetObject.childNodes;}};if ((!oldTree.dpcpy)&&(!framesMove)) {var zir=itemObject.tr;if ((document.all)&&(navigator.appVersion.search(/MSIE\ 5\.0/gi)!=-1))
 {window.setTimeout(function() {zir.parentNode.removeChild(zir);}, 250 );}else 

 itemObject.parentObject.htmlNode.childNodes[0].removeChild(itemObject.tr);if ((!beforeNode)||(targetObject!=itemObject.parentObject)){for (var i=0;i<z.childsCount;i++){if (z.childNodes[i].id==itemObject.id){z.childNodes[i]=0;break;}}}else z.childNodes[z.childsCount-1]=0;oldTree._compressChildList(z.childsCount,z.childNodes);z.childsCount--;};if ((!framesMove)&&(!oldTree.dpcpy)) {itemObject.tr=tr;tr.nodem=itemObject;itemObject.parentObject=targetObject;if (oldTree!=targetObject.treeNod){if(itemObject.treeNod._registerBranch(itemObject,oldTree)) return;this._clearStyles(itemObject);this._redrawFrom(this,itemObject.parentObject);};this._correctPlus(targetObject);this._correctLine(targetObject);this._correctLine(itemObject);this._correctPlus(itemObject);if (beforeNode){this._correctPlus(beforeNode);}else 
 if (targetObject.childsCount>=2){this._correctPlus(Nodes[targetObject.childsCount-2]);this._correctLine(Nodes[targetObject.childsCount-2]);};this._correctPlus(Nodes[targetObject.childsCount-1]);if (this.tscheck)this._correctCheckStates(targetObject);if (oldTree.tscheck)oldTree._correctCheckStates(z);};if (c>1){oldTree._correctPlus(z.childNodes[c-2]);oldTree._correctLine(z.childNodes[c-2]);};oldTree._correctPlus(z);oldTree._correctLine(z);this._fixChildCountLabel(targetObject);oldTree._fixChildCountLabel(z);this.callEvent("onDrop",[itemObject.id,targetObject.id,(beforeNode?beforeNode.id:null),oldTree,targetObject.treeNod]);return itemObject.id;};dhtmlXTreeObject.prototype._clearStyles=function(itemObject){if (!itemObject.htmlNode)return;var td1=itemObject.htmlNode.childNodes[0].childNodes[0].childNodes[1];var td3=td1.nextSibling.nextSibling;itemObject.span.innerHTML=itemObject.label;itemObject.i_sel=false;if (itemObject._aimgs)this.dragger.removeDraggableItem(td1.nextSibling);if (this.checkBoxOff){td1.childNodes[0].style.display="";td1.childNodes[0].onclick=this.onCheckBoxClick;this._setSrc(td1.childNodes[0],this.imPath+this.checkArray[itemObject.checkstate]);}else td1.childNodes[0].style.display="none";td1.childNodes[0].treeNod=this;this.dragger.removeDraggableItem(td3);if (this.dragAndDropOff)this.dragger.addDraggableItem(td3,this);if (this._aimgs)this.dragger.addDraggableItem(td1.nextSibling,this);td3.childNodes[0].className="standartTreeRow";td3.onclick=this.onRowSelect;td3.ondblclick=this.onRowClick2;td1.previousSibling.onclick=this.onRowClick;this._correctLine(itemObject);this._correctPlus(itemObject);for (var i=0;i<itemObject.childsCount;i++)this._clearStyles(itemObject.childNodes[i]);};dhtmlXTreeObject.prototype._registerBranch=function(itemObject,oldTree){if (oldTree)oldTree._globalIdStorageSub(itemObject.id);itemObject.id=this._globalIdStorageAdd(itemObject.id,itemObject);itemObject.treeNod=this;for (var i=0;i<itemObject.childsCount;i++)this._registerBranch(itemObject.childNodes[i],oldTree);return 0;};dhtmlXTreeObject.prototype.enableThreeStateCheckboxes=function(mode) {this.tscheck=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.setOnMouseInHandler=function(func){this.ehlt=true;this.attachEvent("onMouseIn",func);};dhtmlXTreeObject.prototype.setOnMouseOutHandler=function(func){this.ehlt=true;this.attachEvent("onMouseOut",func);};dhtmlXTreeObject.prototype.enableMercyDrag=function(mode){this.dpcpy=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.enableTreeImages=function(mode) {this.timgen=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.enableFixedMode=function(mode) {this.hfMode=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.enableCheckBoxes=function(mode, hidden){this.checkBoxOff=convertStringToBoolean(mode);this.cBROf=(!(this.checkBoxOff||convertStringToBoolean(hidden)));};dhtmlXTreeObject.prototype.setStdImages=function(image1,image2,image3){this.imageArray[0]=image1;this.imageArray[1]=image2;this.imageArray[2]=image3;};dhtmlXTreeObject.prototype.enableTreeLines=function(mode){this.treeLinesOn=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.setImageArrays=function(arrayName,image1,image2,image3,image4,image5){switch(arrayName){case "plus": this.plusArray[0]=image1;this.plusArray[1]=image2;this.plusArray[2]=image3;this.plusArray[3]=image4;this.plusArray[4]=image5;break;case "minus": this.minusArray[0]=image1;this.minusArray[1]=image2;this.minusArray[2]=image3;this.minusArray[3]=image4;this.minusArray[4]=image5;break;}};dhtmlXTreeObject.prototype.openItem=function(itemId){var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;else return this._openItem(temp);};dhtmlXTreeObject.prototype._openItem=function(item){var state=this._getOpenState(item);if ((state<0)||(((this.XMLsource)&&(!item.XMLload)))){if (!this.callEvent("onOpenStart",[item.id,state])) return 0;this._HideShow(item,2);if (this.checkEvent("onOpenEnd")){if (this.onXLE==this._epnFHe)this._epnFHe(this,item.id,true);if (!this.xmlstate || !this.XMLsource)this.callEvent("onOpenEnd",[item.id,this._getOpenState(item)]);else{this._oie_onXLE.push(this.onXLE);this.onXLE=this._epnFHe;}}}else if (this._srnd)this._HideShow(item,2);if (item.parentObject && !this._skip_open_parent)this._openItem(item.parentObject);};dhtmlXTreeObject.prototype.closeItem=function(itemId){if (this.rootId==itemId)return 0;var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;if (temp.closeble)this._HideShow(temp,1);};dhtmlXTreeObject.prototype.getLevel=function(itemId){var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;return this._getNodeLevel(temp,0);};dhtmlXTreeObject.prototype.setItemCloseable=function(itemId,flag)
 {flag=convertStringToBoolean(flag);if ((itemId)&&(itemId.span)) 
 var temp=itemId;else 
 var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;temp.closeble=flag;};dhtmlXTreeObject.prototype._getNodeLevel=function(itemObject,count){if (itemObject.parentObject)return this._getNodeLevel(itemObject.parentObject,count+1);return(count);};dhtmlXTreeObject.prototype.hasChildren=function(itemId){var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;else 
 {if ( (this.XMLsource)&&(!temp.XMLload) ) return true;else 
 return temp.childsCount;}};dhtmlXTreeObject.prototype._getLeafCount=function(itemNode){var a=0;for (var b=0;b<itemNode.childsCount;b++)if (itemNode.childNodes[b].childsCount==0)a++;return a;};dhtmlXTreeObject.prototype.setItemText=function(itemId,newLabel,newTooltip)
 {var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;temp.label=newLabel;temp.span.innerHTML=newLabel;if (this.childCalc)this._fixChildCountLabel(temp);temp.span.parentNode.parentNode.title=newTooltip||"";};dhtmlXTreeObject.prototype.getItemTooltip=function(itemId){var temp=this._globalIdStorageFind(itemId);if (!temp)return "";return (temp.span.parentNode.parentNode._dhx_title||temp.span.parentNode.parentNode.title||"");};dhtmlXTreeObject.prototype.refreshItem=function(itemId){if (!itemId)itemId=this.rootId;var temp=this._globalIdStorageFind(itemId);this.deleteChildItems(itemId);this._loadDynXML(itemId);};dhtmlXTreeObject.prototype.setItemImage2=function(itemId, image1,image2,image3){var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;temp.images[1]=image2;temp.images[2]=image3;temp.images[0]=image1;this._correctPlus(temp);};dhtmlXTreeObject.prototype.setItemImage=function(itemId,image1,image2)
 {var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;if (image2){temp.images[1]=image1;temp.images[2]=image2;}else temp.images[0]=image1;this._correctPlus(temp);};dhtmlXTreeObject.prototype.getSubItems =function(itemId)
 {var temp=this._globalIdStorageFind(itemId,0,1);if (!temp)return 0;if(temp.unParsed)return (this._getSubItemsXML(temp.unParsed));var z="";for (i=0;i<temp.childsCount;i++){if (!z)z=temp.childNodes[i].id;else z+=this.dlmtr+temp.childNodes[i].id;};return z;};dhtmlXTreeObject.prototype._getAllScraggyItems =function(node)
 {var z="";for (var i=0;i<node.childsCount;i++){if ((node.childNodes[i].unParsed)||(node.childNodes[i].childsCount>0))
 {if (node.childNodes[i].unParsed)var zb=this._getAllScraggyItemsXML(node.childNodes[i].unParsed,1);else
 var zb=this._getAllScraggyItems(node.childNodes[i])

 if (zb)if (z)z+=this.dlmtr+zb;else z=zb;}else
 if (!z)z=node.childNodes[i].id;else z+=this.dlmtr+node.childNodes[i].id;};return z;};dhtmlXTreeObject.prototype._getAllFatItems =function(node)
 {var z="";for (var i=0;i<node.childsCount;i++){if ((node.childNodes[i].unParsed)||(node.childNodes[i].childsCount>0))
 {if (!z)z=node.childNodes[i].id;else z+=this.dlmtr+node.childNodes[i].id;if (node.childNodes[i].unParsed)var zb=this._getAllFatItemsXML(node.childNodes[i].unParsed,1);else
 var zb=this._getAllFatItems(node.childNodes[i])

 if (zb)z+=this.dlmtr+zb;}};return z;};dhtmlXTreeObject.prototype._getAllSubItems =function(itemId,z,node)
 {if (node)temp=node;else {var temp=this._globalIdStorageFind(itemId);};if (!temp)return 0;z="";for (var i=0;i<temp.childsCount;i++){if (!z)z=temp.childNodes[i].id;else z+=this.dlmtr+temp.childNodes[i].id;var zb=this._getAllSubItems(0,z,temp.childNodes[i])

 if (zb)z+=this.dlmtr+zb;};if (temp.unParsed)z=this._getAllSubItemsXML(itemId,z,temp.unParsed);return z;};dhtmlXTreeObject.prototype.selectItem=function(itemId,mode,preserve){mode=convertStringToBoolean(mode);var temp=this._globalIdStorageFind(itemId);if ((!temp)||(!temp.parentObject)) return 0;if (this.XMLloadingWarning)temp.parentObject.openMe=1;else
 this._openItem(temp.parentObject);var ze=null;if (preserve){ze=new Object;ze.ctrlKey=true;if (temp.i_sel)ze.skipUnSel=true;};if (mode)this.onRowSelect(ze,temp.htmlNode.childNodes[0].childNodes[0].childNodes[3],false);else
 this.onRowSelect(ze,temp.htmlNode.childNodes[0].childNodes[0].childNodes[3],true);};dhtmlXTreeObject.prototype.getSelectedItemText=function()
 {var str=new Array();for (var i=0;i<this._selected.length;i++)str[i]=this._selected[i].span.innerHTML;return (str.join(this.dlmtr));};dhtmlXTreeObject.prototype._compressChildList=function(Count,Nodes)
 {Count--;for (var i=0;i<Count;i++){if (Nodes[i]==0){Nodes[i]=Nodes[i+1];Nodes[i+1]=0;}}};dhtmlXTreeObject.prototype._deleteNode=function(itemId,htmlObject,skip){if ((!htmlObject)||(!htmlObject.parentObject)) return 0;var tempos=0;var tempos2=0;if (htmlObject.tr.nextSibling)tempos=htmlObject.tr.nextSibling.nodem;if (htmlObject.tr.previousSibling)tempos2=htmlObject.tr.previousSibling.nodem;var sN=htmlObject.parentObject;var Count=sN.childsCount;var Nodes=sN.childNodes;for (var i=0;i<Count;i++){if (Nodes[i].id==itemId){if (!skip)sN.htmlNode.childNodes[0].removeChild(Nodes[i].tr);Nodes[i]=0;break;}};this._compressChildList(Count,Nodes);if (!skip){sN.childsCount--;};if (tempos){this._correctPlus(tempos);this._correctLine(tempos);};if (tempos2){this._correctPlus(tempos2);this._correctLine(tempos2);};if (this.tscheck)this._correctCheckStates(sN);if (!skip){this._globalIdStorageRecSub(htmlObject);}};dhtmlXTreeObject.prototype.setCheck=function(itemId,state){var sNode=this._globalIdStorageFind(itemId,0,1);if (!sNode)return;if (state==="unsure")this._setCheck(sNode,state);else
 {state=convertStringToBoolean(state);if ((this.tscheck)&&(this.smcheck)) this._setSubChecked(state,sNode);else this._setCheck(sNode,state);};if (this.smcheck)this._correctCheckStates(sNode.parentObject);};dhtmlXTreeObject.prototype._setCheck=function(sNode,state){if (!sNode)return;if (((sNode.parentObject._r_logic)||(this._frbtr))&&(state))
 if (this._frbtrs){if (this._frbtrL)this.setCheck(this._frbtrL.id,0);this._frbtrL=sNode;}else
 for (var i=0;i<sNode.parentObject.childsCount;i++)this._setCheck(sNode.parentObject.childNodes[i],0);var z=sNode.htmlNode.childNodes[0].childNodes[0].childNodes[1].childNodes[0];if (state=="unsure")sNode.checkstate=2;else if (state)sNode.checkstate=1;else sNode.checkstate=0;if (sNode.dscheck)sNode.checkstate=sNode.dscheck;this._setSrc(z,this.imPath+((sNode.parentObject._r_logic||this._frbtr)?this.radioArray:this.checkArray)[sNode.checkstate]);};dhtmlXTreeObject.prototype.setSubChecked=function(itemId,state){var sNode=this._globalIdStorageFind(itemId);this._setSubChecked(state,sNode);this._correctCheckStates(sNode.parentObject);};dhtmlXTreeObject.prototype._setSubChecked=function(state,sNode){state=convertStringToBoolean(state);if (!sNode)return;if (((sNode.parentObject._r_logic)||(this._frbtr))&&(state))
 for (var i=0;i<sNode.parentObject.childsCount;i++)this._setSubChecked(0,sNode.parentObject.childNodes[i]);if (sNode.unParsed)this._setSubCheckedXML(state,sNode.unParsed)


 if (sNode._r_logic||this._frbtr)this._setSubChecked(state,sNode.childNodes[0]);else
 for (var i=0;i<sNode.childsCount;i++){this._setSubChecked(state,sNode.childNodes[i]);};var z=sNode.htmlNode.childNodes[0].childNodes[0].childNodes[1].childNodes[0];if (state)sNode.checkstate=1;else sNode.checkstate=0;if (sNode.dscheck)sNode.checkstate=sNode.dscheck;this._setSrc(z,this.imPath+((sNode.parentObject._r_logic||this._frbtr)?this.radioArray:this.checkArray)[sNode.checkstate]);};dhtmlXTreeObject.prototype.isItemChecked=function(itemId){var sNode=this._globalIdStorageFind(itemId);if (!sNode)return;return sNode.checkstate;};dhtmlXTreeObject.prototype.deleteChildItems=function(itemId)
 {var sNode=this._globalIdStorageFind(itemId);if (!sNode)return;var j=sNode.childsCount;for (var i=0;i<j;i++){this._deleteNode(sNode.childNodes[0].id,sNode.childNodes[0]);}};dhtmlXTreeObject.prototype.deleteItem=function(itemId,selectParent){if ((!this._onrdlh)||(this._onrdlh(itemId))){var z=this._deleteItem(itemId,selectParent);this._fixChildCountLabel(z);};this.allTree.childNodes[0].border = "1";this.allTree.childNodes[0].border = "0";};dhtmlXTreeObject.prototype._deleteItem=function(itemId,selectParent,skip){selectParent=convertStringToBoolean(selectParent);var sNode=this._globalIdStorageFind(itemId);if (!sNode)return;var pid=this.getParentId(itemId);var zTemp=sNode.parentObject;this._deleteNode(itemId,sNode,skip);this._correctPlus(zTemp);this._correctLine(zTemp);if ((selectParent)&&(pid!=this.rootId)) this.selectItem(pid,1);return zTemp;};dhtmlXTreeObject.prototype._globalIdStorageRecSub=function(itemObject){for(var i=0;i<itemObject.childsCount;i++){this._globalIdStorageRecSub(itemObject.childNodes[i]);this._globalIdStorageSub(itemObject.childNodes[i].id);};this._globalIdStorageSub(itemObject.id);var z=itemObject;z.span=null;z.tr.nodem=null;z.tr=null;z.htmlNode=null;};dhtmlXTreeObject.prototype.insertNewNext=function(itemId,newItemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children){var sNode=this._globalIdStorageFind(itemId);if ((!sNode)||(!sNode.parentObject)) return (0);var nodez=this._attachChildNode(0,newItemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children,sNode);if ((!this.XMLloadingWarning)&&(this.childCalc)) this._fixChildCountLabel(sNode.parentObject);return nodez;};dhtmlXTreeObject.prototype.getItemIdByIndex=function(itemId,index){var z=this._globalIdStorageFind(itemId);if ((!z)||(index>=z.childsCount)) return null;return z.childNodes[index].id;};dhtmlXTreeObject.prototype.getChildItemIdByIndex=function(itemId,index){var z=this._globalIdStorageFind(itemId);if ((!z)||(index>=z.childsCount)) return null;return z.childNodes[index].id;};dhtmlXTreeObject.prototype.setDragHandler=function(func){this.attachEvent("onDrag",func);};dhtmlXTreeObject.prototype._clearMove=function(){if (this._lastMark){this._lastMark.className=this._lastMark.className.replace(/dragAndDropRow/g,"");this._lastMark=null;};this.selectionBar.style.display="none";this.allTree.className=this.allTree.className.replace(" selectionBox","");};dhtmlXTreeObject.prototype.enableDragAndDrop=function(mode,rmode){if (mode=="temporary_disabled"){this.dADTempOff=false;mode=true;}else
 this.dADTempOff=true;this.dragAndDropOff=convertStringToBoolean(mode);if (this.dragAndDropOff)this.dragger.addDragLanding(this.allTree,this);if (arguments.length>1)this._ddronr=(!convertStringToBoolean(rmode));};dhtmlXTreeObject.prototype._setMove=function(htmlNode,x,y){if (htmlNode.parentObject.span){var a1=getAbsoluteTop(htmlNode);var a2=getAbsoluteTop(this.allTree)-this.allTree.scrollTop;this.dadmodec=this.dadmode;this.dadmodefix=0;if (this.dadmode==2){var z=y-a1+(document.body.scrollTop||document.documentElement.scrollTop)-2-htmlNode.offsetHeight/2;if ((Math.abs(z)-htmlNode.offsetHeight/6)>0)
 {this.dadmodec=1;if (z<0)this.dadmodefix=0-htmlNode.offsetHeight;}else this.dadmodec=0;};if (this.dadmodec==0){var zN=htmlNode.parentObject.span;zN.className+=" dragAndDropRow";this._lastMark=zN;}else{this._clearMove();this.selectionBar.style.top=(a1-a2+((parseInt(htmlNode.parentObject.span.parentNode.previousSibling.childNodes[0].style.height)||18)-1)+this.dadmodefix)+"px";this.selectionBar.style.left="5px";if (this.allTree.offsetWidth>20)this.selectionBar.style.width=(this.allTree.offsetWidth-(_isFF?30:25))+"px";this.selectionBar.style.display="";};this._autoScroll(null,a1,a2);}};dhtmlXTreeObject.prototype._autoScroll=function(node,a1,a2){if (this.autoScroll){if (node){a1=getAbsoluteTop(node);a2=getAbsoluteTop(this.allTree);};if ( (a1-a2-parseInt(this.allTree.scrollTop))>(parseInt(this.allTree.offsetHeight)-50) )
 this.allTree.scrollTop=parseInt(this.allTree.scrollTop)+20;if ( (a1-a2)<(parseInt(this.allTree.scrollTop)+30) )
 this.allTree.scrollTop=parseInt(this.allTree.scrollTop)-20;}};dhtmlXTreeObject.prototype._createDragNode=function(htmlObject,e){if (!this.dADTempOff)return null;var obj=htmlObject.parentObject;if (!this.callEvent("onBeforeDrag",[obj.id])) return null;if (!obj.i_sel)this._selectItem(obj,e);this._checkMSelectionLogic();var dragSpan=document.createElement('div');var text=new Array();if (this._itim_dg)for (var i=0;i<this._selected.length;i++)text[i]="<table cellspacing='0' cellpadding='0'><tr><td><img width='18px' height='18px' src='"+this._getSrc(this._selected[i].span.parentNode.previousSibling.childNodes[0])+"'></td><td>"+this._selected[i].span.innerHTML+"</td></tr></table>";else
 text=this.getSelectedItemText().split(this.dlmtr);dragSpan.innerHTML=text.join("");dragSpan.style.position="absolute";dragSpan.className="dragSpanDiv";this._dragged=(new Array()).concat(this._selected);return dragSpan;};dhtmlXTreeObject.prototype._focusNode=function(item){var z=getAbsoluteTop(item.htmlNode)-getAbsoluteTop(this.allTree);if ((z>(this.allTree.offsetHeight-30))||(z<0))
 this.allTree.scrollTop=z+this.allTree.scrollTop;};dhtmlXTreeObject.prototype._preventNsDrag=function(e){if ((e)&&(e.preventDefault)) {e.preventDefault();return false;};return false;};dhtmlXTreeObject.prototype._drag=function(sourceHtmlObject,dhtmlObject,targetHtmlObject){if (this._autoOpenTimer)clearTimeout(this._autoOpenTimer);if (!targetHtmlObject.parentObject){targetHtmlObject=this.htmlNode.htmlNode.childNodes[0].childNodes[0].childNodes[1].childNodes[0];this.dadmodec=0;};this._clearMove();var z=sourceHtmlObject.parentObject.treeNod;if ((z)&&(z._clearMove)) z._clearMove("");if ((!this.dragMove)||(this.dragMove()))
 {if ((!z)||(!z._clearMove)||(!z._dragged)) var col=new Array(sourceHtmlObject.parentObject);else var col=z._dragged;var trg=targetHtmlObject.parentObject;for (var i=0;i<col.length;i++){var newID=this._moveNode(col[i],trg);if ((this.dadmodec)&&(newID!==false)) trg=this._globalIdStorageFind(newID,true,true);if ((newID)&&(!this._sADnD)) this.selectItem(newID,0,1);}};if (z)z._dragged=new Array();};dhtmlXTreeObject.prototype._dragIn=function(htmlObject,shtmlObject,x,y){if (!this.dADTempOff)return 0;var fobj=shtmlObject.parentObject;var tobj=htmlObject.parentObject;if ((!tobj)&&(this._ddronr)) return;if (!this.callEvent("onDragIn",[fobj.id,tobj?tobj.id:null,fobj.treeNod,this])){if (tobj)this._autoScroll(htmlObject);return 0;};if (!tobj)this.allTree.className+=" selectionBox";else
 {if (fobj.childNodes==null){this._setMove(htmlObject,x,y);return htmlObject;};var stree=fobj.treeNod;for (var i=0;i<stree._dragged.length;i++)if (this._checkPNodes(tobj,stree._dragged[i])){this._autoScroll(htmlObject);return 0;};this.selectionBar.parentNode.removeChild(this.selectionBar);tobj.span.parentNode.appendChild(this.selectionBar);this._setMove(htmlObject,x,y);if (this._getOpenState(tobj)<=0){this._autoOpenId=tobj.id;this._autoOpenTimer=window.setTimeout(new callerFunction(this._autoOpenItem,this),1000);}};return htmlObject;};dhtmlXTreeObject.prototype._autoOpenItem=function(e,treeObject){treeObject.openItem(treeObject._autoOpenId);};dhtmlXTreeObject.prototype._dragOut=function(htmlObject){this._clearMove();if (this._autoOpenTimer)clearTimeout(this._autoOpenTimer);};dhtmlXTreeObject.prototype._getNextNode=function(item,mode){if ((!mode)&&(item.childsCount)) return item.childNodes[0];if (item==this.htmlNode)return -1;if ((item.tr)&&(item.tr.nextSibling)&&(item.tr.nextSibling.nodem))
 return item.tr.nextSibling.nodem;return this._getNextNode(item.parentObject,true);};dhtmlXTreeObject.prototype._lastChild=function(item){if (item.childsCount)return this._lastChild(item.childNodes[item.childsCount-1]);else return item;};dhtmlXTreeObject.prototype._getPrevNode=function(node,mode){if ((node.tr)&&(node.tr.previousSibling)&&(node.tr.previousSibling.nodem))
 return this._lastChild(node.tr.previousSibling.nodem);if (node.parentObject)return node.parentObject;else return -1;};dhtmlXTreeObject.prototype.findItem=function(searchStr,direction,top){var z=this._findNodeByLabel(searchStr,direction,(top?this.htmlNode:null));if (z){this.selectItem(z.id,true);this._focusNode(z);return z.id;}else return null;};dhtmlXTreeObject.prototype.findItemIdByLabel=function(searchStr,direction,top){var z=this._findNodeByLabel(searchStr,direction,(top?this.htmlNode:null));if (z)return z.id
 else return null;};dhtmlXTreeObject.prototype.findStrInXML=function(node,field,cvalue){if (!node.childNodes && node.item)return this.findStrInJSON(node,field,cvalue);for (var i=0;i<node.childNodes.length;i++){if (node.childNodes[i].nodeType==1){var z=node.childNodes[i].getAttribute(field);if (!z && node.childNodes[i].tagName=="itemtext")z=node.childNodes[i].firstChild.data;if ((z)&&(z.toLowerCase().search(cvalue)!=-1))
 return true;if (this.findStrInXML(node.childNodes[i],field,cvalue)) return true;}};return false;};dhtmlXTreeObject.prototype.findStrInJSON=function(node,field,cvalue){for (var i=0;i<node.item.length;i++){var z=node.item[i].text;if ((z)&&(z.toLowerCase().search(cvalue)!=-1))
 return true;if (node.item[i].item && this.findStrInJSON(node.item[i],field,cvalue)) return true;};return false;};dhtmlXTreeObject.prototype._findNodeByLabel=function(searchStr,direction,fromNode){var searchStr=searchStr.replace(new RegExp("^( )+"),"").replace(new RegExp("( )+$"),"");searchStr = new RegExp(searchStr.replace(/([\?\*\+\\\[\]\(\)]{1})/gi,"\\$1").replace(/ /gi,".*"),"gi");if (!fromNode){fromNode=this._selected[0];if (!fromNode)fromNode=this.htmlNode;};var startNode=fromNode;if (!direction){if ((fromNode.unParsed)&&(this.findStrInXML(fromNode.unParsed.d,"text",searchStr)))
 this.reParse(fromNode);fromNode=this._getNextNode(startNode);if (fromNode==-1)fromNode=this.htmlNode.childNodes[0];}else
 {var z2=this._getPrevNode(startNode);if (z2==-1)z2=this._lastChild(this.htmlNode);if ((z2.unParsed)&&(this.findStrInXML(z2.unParsed.d,"text",searchStr)))
 {this.reParse(z2);fromNode=this._getPrevNode(startNode);}else fromNode=z2;if (fromNode==-1)fromNode=this._lastChild(this.htmlNode);};while ((fromNode)&&(fromNode!=startNode)){if ((fromNode.label)&&(fromNode.label.search(searchStr)!=-1))
 return (fromNode);if (!direction){if (fromNode==-1){if (startNode==this.htmlNode)break;fromNode=this.htmlNode.childNodes[0];};if ((fromNode.unParsed)&&(this.findStrInXML(fromNode.unParsed.d,"text",searchStr)))
 this.reParse(fromNode);fromNode=this._getNextNode(fromNode);if (fromNode==-1)fromNode=this.htmlNode;}else
 {var z2=this._getPrevNode(fromNode);if (z2==-1)z2=this._lastChild(this.htmlNode);if ((z2.unParsed)&&(this.findStrInXML(z2.unParsed.d,"text",searchStr)))
 {this.reParse(z2);fromNode=this._getPrevNode(fromNode);}else fromNode=z2;if (fromNode==-1)fromNode=this._lastChild(this.htmlNode);}};return null;};dhtmlXTreeObject.prototype.moveItem=function(itemId,mode,targetId,targetTree)
{var sNode=this._globalIdStorageFind(itemId);if (!sNode)return (0);switch(mode){case "right": alert('Not supported yet');break;case "item_child":
 var tNode=(targetTree||this)._globalIdStorageFind(targetId);if (!tNode)return (0);(targetTree||this)._moveNodeTo(sNode,tNode,0);break;case "item_sibling":
 var tNode=(targetTree||this)._globalIdStorageFind(targetId);if (!tNode)return (0);(targetTree||this)._moveNodeTo(sNode,tNode.parentObject,tNode);break;case "item_sibling_next":
 var tNode=(targetTree||this)._globalIdStorageFind(targetId);if (!tNode)return (0);if ((tNode.tr)&&(tNode.tr.nextSibling)&&(tNode.tr.nextSibling.nodem))
 (targetTree||this)._moveNodeTo(sNode,tNode.parentObject,tNode.tr.nextSibling.nodem);else
 (targetTree||this)._moveNodeTo(sNode,tNode.parentObject);break;case "left": if (sNode.parentObject.parentObject)this._moveNodeTo(sNode,sNode.parentObject.parentObject,sNode.parentObject);break;case "up": var z=this._getPrevNode(sNode);if ((z==-1)||(!z.parentObject)) return;this._moveNodeTo(sNode,z.parentObject,z);break;case "up_strict": var z=this._getIndex(sNode);if (z!=0)this._moveNodeTo(sNode,sNode.parentObject,sNode.parentObject.childNodes[z-1]);break;case "down_strict": var z=this._getIndex(sNode);var count=sNode.parentObject.childsCount-2;if (z==count)this._moveNodeTo(sNode,sNode.parentObject);else if (z<count)this._moveNodeTo(sNode,sNode.parentObject,sNode.parentObject.childNodes[z+2]);break;case "down": var z=this._getNextNode(this._lastChild(sNode));if ((z==-1)||(!z.parentObject)) return;if (z.parentObject==sNode.parentObject)var z=this._getNextNode(z);if (z==-1){this._moveNodeTo(sNode,sNode.parentObject);}else
 {if ((z==-1)||(!z.parentObject)) return;this._moveNodeTo(sNode,z.parentObject,z);};break;};if (_isIE && _isIE<8){this.allTree.childNodes[0].border = "1";this.allTree.childNodes[0].border = "0";}};dhtmlXTreeObject.prototype.setDragBehavior=function(mode,select){this._sADnD=(!convertStringToBoolean(select));switch (mode) {case "child": this.dadmode=0;break;case "sibling": this.dadmode=1;break;case "complex": this.dadmode=2;break;}};dhtmlXTreeObject.prototype._loadDynXML=function(id,src) {src=src||this.XMLsource;var sn=(new Date()).valueOf();this._ld_id=id;if (this.xmlalb=="function"){if (src)src(this._escape(id));}else
 if (this.xmlalb=="name")this.loadXML(src+this._escape(id));else
 if (this.xmlalb=="xmlname")this.loadXML(src+this._escape(id)+".xml?uid="+sn);else

 this.loadXML(src+getUrlSymbol(src)+"uid="+sn+"&id="+this._escape(id));};dhtmlXTreeObject.prototype.enableMultiselection=function(mode,strict) {this._amsel=convertStringToBoolean(mode);this._amselS=convertStringToBoolean(strict);};dhtmlXTreeObject.prototype._checkMSelectionLogic=function() {var usl=new Array();for (var i=0;i<this._selected.length;i++)for (var j=0;j<this._selected.length;j++)if ((i!=j)&&(this._checkPNodes(this._selected[j],this._selected[i])))
 usl[usl.length]=this._selected[j];for (var i=0;i<usl.length;i++)this._unselectItem(usl[i]);};dhtmlXTreeObject.prototype._checkPNodes=function(item1,item2){if (item2==item1)return 1
 if (item1.parentObject)return this._checkPNodes(item1.parentObject,item2);else return 0;};dhtmlXTreeObject.prototype.enableDistributedParsing=function(mode,count,delay){this._edsbps=convertStringToBoolean(mode);this._edsbpsA=new Array();this._edsbpsC=count||10;this._edsbpsD=delay||250;};dhtmlXTreeObject.prototype.getDistributedParsingState=function(){return (!((!this._edsbpsA)||(!this._edsbpsA.length)));};dhtmlXTreeObject.prototype.getItemParsingState=function(itemId){var z=this._globalIdStorageFind(itemId,true,true)
 if (!z)return 0;if (this._edsbpsA)for (var i=0;i<this._edsbpsA.length;i++)if (this._edsbpsA[i][2]==itemId)return -1;return 1;};dhtmlXTreeObject.prototype._distributedStart=function(node,start,parentId,level,start2){if (!this._edsbpsA)this._edsbpsA=new Array();this._edsbpsA[this._edsbpsA.length]=[node,start,parentId,level,start2];};dhtmlXTreeObject.prototype._distributedStep=function(pId){var self=this;if ((!this._edsbpsA)||(!this._edsbpsA.length)) {self.XMLloadingWarning=0;return;};var z=this._edsbpsA[0];this.parsedArray=new Array();this._parse(z[0],z[2],z[3],z[1]);var zkx=this._globalIdStorageFind(z[2]);this._redrawFrom(this,zkx,z[4],this._getOpenState(zkx));var chArr=this.setCheckList.split(this.dlmtr);for (var n=0;n<chArr.length;n++)if (chArr[n])this.setCheck(chArr[n],1);this._edsbpsA=(new Array()).concat(this._edsbpsA.slice(1));if ((!this._edsbpsA.length)){window.setTimeout( function(){if (self.onXLE)self.onXLE(self,pId);self.callEvent("onXLE",[self,pId]);},1);self.xmlstate=0;}};dhtmlXTreeObject.prototype.enablePaging=function(mode,page_size){this._epgps=convertStringToBoolean(mode);this._epgpsC=page_size||50;};dhtmlXTreeObject.prototype._setPrevPageSign=function(node,pos,level,xmlnode){var z=document.createElement("DIV");z.innerHTML="Previous "+this._epgpsC+" items";z.className="dhx_next_button";var self=this;z.onclick=function(){self._prevPageCall(this);};z._pageData=[node,pos,level,xmlnode];var w=node.childNodes[0];var w2=w.span.parentNode.parentNode.parentNode.parentNode.parentNode;w2.insertBefore(z,w2.firstChild);};dhtmlXTreeObject.prototype._setNextPageSign=function(node,pos,level,xmlnode){var z=document.createElement("DIV");z.innerHTML="Next "+this._epgpsC+" items";z.className="dhx_next_button";var self=this;z.onclick=function(){self._nextPageCall(this);};z._pageData=[node,pos,level,xmlnode];var w=node.childNodes[node.childsCount-1];w.span.parentNode.parentNode.parentNode.parentNode.parentNode.appendChild(z);};dhtmlXTreeObject.prototype._nextPageCall=function(node){tree.deleteChildItems(node._pageData[0].id);node.parentNode.removeChild(node);var f=this._getOpenState(node._pageData[0]);this._parseXMLTree(this,node._pageData[3],node._pageData[0].id,node._pageData[2],null,node._pageData[1]);this._redrawFrom(this,node._pageData[0],0);if (f>-1)this._openItem(node._pageData[0]);node._pageData=null;};dhtmlXTreeObject.prototype._prevPageCall=function(node){tree.deleteChildItems(node._pageData[0].id);node.parentNode.removeChild(node);var f=this._getOpenState(node._pageData[0]);var xz=node._pageData[1]-this._epgpsC;if (xz<0)xz=0;this._parseXMLTree(this,node._pageData[3],node._pageData[0].id,node._pageData[2],null,xz);this._redrawFrom(this,node._pageData[0],0);if (f>-1)this._openItem(node._pageData[0]);node._pageData=null;};dhtmlXTreeObject.prototype.enableTextSigns=function(mode){this._txtimg=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.preventIECaching=function(mode){this.no_cashe = convertStringToBoolean(mode);this.XMLLoader.rSeed=this.no_cashe;};dhtmlXTreeObject.prototype.preventIECashing=dhtmlXTreeObject.prototype.preventIECaching;dhtmlXTreeObject.prototype.disableCheckbox=function(itemId,mode) {if (typeof(itemId)!="object")
 var sNode=this._globalIdStorageFind(itemId,0,1);else
 var sNode=itemId;if (!sNode)return;sNode.dscheck=convertStringToBoolean(mode)?(((sNode.checkstate||0)%3)+3):((sNode.checkstate>2)?(sNode.checkstate-3):sNode.checkstate);this._setCheck(sNode);if (sNode.dscheck<3)sNode.dscheck=false;};dhtmlXTreeObject.prototype.smartRefreshBranch=function(itemId,source){this._branchUpdate=1;this.smartRefreshItem(itemId,source);};dhtmlXTreeObject.prototype.smartRefreshItem=function(itemId,source){var sNode=this._globalIdStorageFind(itemId);for (var i=0;i<sNode.childsCount;i++)sNode.childNodes[i]._dmark=true;this.waitUpdateXML=true;if (source && source.exists)this._parse(source,itemId);else
 this._loadDynXML(itemId,source);};dhtmlXTreeObject.prototype.refreshItems=function(itemIdList,source){var z=itemIdList.toString().split(this.dlmtr);this.waitUpdateXML=new Array();for (var i=0;i<z.length;i++)this.waitUpdateXML[z[i]]=true;this.loadXML((source||this.XMLsource)+getUrlSymbol(source||this.XMLsource)+"ids="+this._escape(itemIdList));};dhtmlXTreeObject.prototype.updateItem=function(itemId,name,im0,im1,im2,achecked){var sNode=this._globalIdStorageFind(itemId);sNode.userData=new cObject();if (name)sNode.label=name;sNode.images=new Array(im0||this.imageArray[0],im1||this.imageArray[1],im2||this.imageArray[2]);this.setItemText(itemId,name);if (achecked)this._setCheck(sNode,true);this._correctPlus(sNode);sNode._dmark=false;return sNode;};dhtmlXTreeObject.prototype.setDropHandler=function(func){this.attachEvent("onDrop",func);};dhtmlXTreeObject.prototype.setOnLoadingStart=function(func){this.attachEvent("onXLS",func);};dhtmlXTreeObject.prototype.setOnLoadingEnd=function(func){this.attachEvent("onXLE",func);};dhtmlXTreeObject.prototype.setXMLAutoLoadingBehaviour=function(mode) {this.xmlalb=mode;};dhtmlXTreeObject.prototype.enableSmartCheckboxes=function(mode) {this.smcheck=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.getXMLState=function(){return (this.xmlstate==1);};dhtmlXTreeObject.prototype.setItemTopOffset=function(itemId,value){if (typeof(itemId)=="string")
 var node=this._globalIdStorageFind(itemId);else
 var node=itemId;var z=node.span.parentNode.parentNode;node.span.style.paddingBottom="1px";for (var i=0;i<z.childNodes.length;i++){if (i!=0){z.childNodes[i].style.height=18+parseInt(value)+"px";}else{var w=z.childNodes[i].firstChild;if (z.childNodes[i].firstChild.tagName!='DIV'){w=document.createElement("DIV");z.childNodes[i].insertBefore(w,z.childNodes[i].firstChild);};w.style.height=parseInt(value)+"px";if ((node.parentObject.id!=this.rootId || node.parentObject.childNodes[0]!=node)&& this.treeLinesOn)
 w.style.backgroundImage="url("+this.imPath+this.lineArray[5]+")";w.innerHTML="&nbsp;";w.style.overflow='hidden';if (parseInt(value)==0)
 z.childNodes[i].removeChild(w);};z.childNodes[i].style.verticalAlign="bottom";if (_isIE){this.allTree.childNodes[0].border = "1";this.allTree.childNodes[0].border = "0";}}};dhtmlXTreeObject.prototype.setIconSize=function(newWidth,newHeight,itemId)
{if (itemId){if ((itemId)&&(itemId.span))
 var sNode=itemId;else
 var sNode=this._globalIdStorageFind(itemId);if (!sNode)return (0);var img=sNode.span.parentNode.previousSibling.childNodes[0];if (newWidth){img.style.width=newWidth;if (window._KHTMLrv)img.parentNode.style.width=newWidth;};if (newHeight){img.style.height=newHeight;if (window._KHTMLrv)img.parentNode.style.height=newHeight;}}else{this.def_img_x=newWidth;this.def_img_y=newHeight;}};dhtmlXTreeObject.prototype.getItemImage=function(itemId,imageInd,fullPath){var node=this._globalIdStorageFind(itemId);if (!node)return "";var img=node.images[imageInd||0];if (fullPath)img=this.iconURL+img;return img;};dhtmlXTreeObject.prototype.enableRadioButtons=function(itemId,mode){if (arguments.length==1){this._frbtr=convertStringToBoolean(itemId);this.checkBoxOff=this.checkBoxOff||this._frbtr;return;};var node=this._globalIdStorageFind(itemId);if (!node)return "";mode=convertStringToBoolean(mode);if ((mode)&&(!node._r_logic)){node._r_logic=true;for (var i=0;i<node.childsCount;i++)this._setCheck(node.childNodes[i],node.childNodes[i].checkstate);};if ((!mode)&&(node._r_logic)){node._r_logic=false;for (var i=0;i<node.childsCount;i++)this._setCheck(node.childNodes[i],node.childNodes[i].checkstate);}};dhtmlXTreeObject.prototype.enableSingleRadioMode=function(mode){this._frbtrs=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.openOnItemAdded=function(mode){this._hAdI=!convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.openOnItemAdding=function(mode){this._hAdI=!convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.enableMultiLineItems=function(width) {if (width===true)this.mlitems="100%";else this.mlitems=width;};dhtmlXTreeObject.prototype.enableAutoTooltips=function(mode) {this.ettip=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.clearSelection=function(itemId){if (itemId)this._unselectItem(this._globalIdStorageFind(itemId));else
 this._unselectItems();};dhtmlXTreeObject.prototype.showItemSign=function(itemId,state){var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;var z=temp.span.parentNode.previousSibling.previousSibling.previousSibling;if (!convertStringToBoolean(state)){this._openItem(temp)
 temp.closeble=false;temp.wsign=true;}else
 {temp.closeble=true;temp.wsign=false;};this._correctPlus(temp);};dhtmlXTreeObject.prototype.showItemCheckbox=function(itemId,state){if (!itemId)for (var a in this._idpull)this.showItemCheckbox(this._idpull[a],state);if (typeof(itemId)!="object")
 itemId=this._globalIdStorageFind(itemId,0,0);if (!itemId)return 0;itemId.nocheckbox=!convertStringToBoolean(state);var t=itemId.span.parentNode.previousSibling.previousSibling.childNodes[0];t.style.display=(!itemId.nocheckbox)?"":"none";if (window._KHTMLrv)t.parentNode.style.display=(!itemId.nocheckbox)?"":"none";};dhtmlXTreeObject.prototype.setListDelimeter=function(separator){this.dlmtr=separator;};dhtmlXTreeObject.prototype.setEscapingMode=function(mode){this.utfesc=mode;};dhtmlXTreeObject.prototype.enableHighlighting=function(mode) {this.ehlt=true;this.ehlta=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype._itemMouseOut=function(){var that=this.childNodes[3].parentObject;var tree=that.treeNod;tree.callEvent("onMouseOut",[that.id]);if (that.id==tree._l_onMSI)tree._l_onMSI=null;if (!tree.ehlta)return;that.span.className=that.span.className.replace("_lor","");};dhtmlXTreeObject.prototype._itemMouseIn=function(){var that=this.childNodes[3].parentObject;var tree=that.treeNod;if (tree._l_onMSI!=that.id)tree.callEvent("onMouseIn",[that.id]);tree._l_onMSI=that.id;if (!tree.ehlta)return;that.span.className=that.span.className.replace("_lor","");that.span.className=that.span.className.replace(/((standart|selected)TreeRow)/,"$1_lor");};dhtmlXTreeObject.prototype.enableActiveImages=function(mode){this._aimgs=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.focusItem=function(itemId){var sNode=this._globalIdStorageFind(itemId);if (!sNode)return (0);this._focusNode(sNode);};dhtmlXTreeObject.prototype.getAllSubItems =function(itemId){return this._getAllSubItems(itemId);};dhtmlXTreeObject.prototype.getAllChildless =function(){return this._getAllScraggyItems(this.htmlNode);};dhtmlXTreeObject.prototype.getAllLeafs=dhtmlXTreeObject.prototype.getAllChildless;dhtmlXTreeObject.prototype._getAllScraggyItems =function(node)
 {var z="";for (var i=0;i<node.childsCount;i++){if ((node.childNodes[i].unParsed)||(node.childNodes[i].childsCount>0))
 {if (node.childNodes[i].unParsed)var zb=this._getAllScraggyItemsXML(node.childNodes[i].unParsed,1);else
 var zb=this._getAllScraggyItems(node.childNodes[i])

 if (zb)if (z)z+=this.dlmtr+zb;else z=zb;}else
 if (!z)z=node.childNodes[i].id;else z+=this.dlmtr+node.childNodes[i].id;};return z;};dhtmlXTreeObject.prototype._getAllFatItems =function(node)
 {var z="";for (var i=0;i<node.childsCount;i++){if ((node.childNodes[i].unParsed)||(node.childNodes[i].childsCount>0))
 {if (!z)z=node.childNodes[i].id;else z+=this.dlmtr+node.childNodes[i].id;if (node.childNodes[i].unParsed)var zb=this._getAllFatItemsXML(node.childNodes[i].unParsed,1);else
 var zb=this._getAllFatItems(node.childNodes[i])

 if (zb)z+=this.dlmtr+zb;}};return z;};dhtmlXTreeObject.prototype.getAllItemsWithKids =function(){return this._getAllFatItems(this.htmlNode);};dhtmlXTreeObject.prototype.getAllFatItems=dhtmlXTreeObject.prototype.getAllItemsWithKids;dhtmlXTreeObject.prototype.getAllChecked=function(){return this._getAllChecked("","",1);};dhtmlXTreeObject.prototype.getAllUnchecked=function(itemId){if (itemId)itemId=this._globalIdStorageFind(itemId);return this._getAllChecked(itemId,"",0);};dhtmlXTreeObject.prototype.getAllPartiallyChecked=function(){return this._getAllChecked("","",2);};dhtmlXTreeObject.prototype.getAllCheckedBranches=function(){var temp= this._getAllChecked("","",1);if (temp!="")temp+=this.dlmtr;return temp+this._getAllChecked("","",2);};dhtmlXTreeObject.prototype._getAllChecked=function(htmlNode,list,mode){if (!htmlNode)htmlNode=this.htmlNode;if (htmlNode.checkstate==mode)if (!htmlNode.nocheckbox){if (list)list+=this.dlmtr+htmlNode.id;else list=htmlNode.id;};var j=htmlNode.childsCount;for (var i=0;i<j;i++){list=this._getAllChecked(htmlNode.childNodes[i],list,mode);};if (htmlNode.unParsed)list=this._getAllCheckedXML(htmlNode.unParsed,list,mode);if (list)return list;else return "";};dhtmlXTreeObject.prototype.setItemStyle=function(itemId,style_string,resetCss){var resetCss= resetCss|| false;var temp=this._globalIdStorageFind(itemId);if (!temp)return 0;if (!temp.span.style.cssText)temp.span.setAttribute("style",temp.span.getAttribute("style")+";"+style_string);else 
 temp.span.style.cssText = resetCss? style_string : temp.span.style.cssText+";"+style_string;};dhtmlXTreeObject.prototype.enableImageDrag=function(mode){this._itim_dg=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.setOnDragIn=function(func){this.attachEvent("onDragIn",func);};dhtmlXTreeObject.prototype.enableDragAndDropScrolling=function(mode){this.autoScroll=convertStringToBoolean(mode);};dhtmlXTreeObject.prototype.setSkin=function(name){var tmp = this.parentObject.className.replace(/dhxtree_[^ ]*/gi,"");this.parentObject.className= tmp+" dhxtree_"+name;};(function(){dhtmlx.extend_api("dhtmlXTreeObject",{_init:function(obj){return [obj.parent,(obj.width||"100%"),(obj.height||"100%"),(obj.root_id||0)];},
 auto_save_selection:"enableAutoSavingSelected",
 auto_tooltip:"enableAutoTooltips",
 checkbox:"enableCheckBoxes",
 checkbox_3_state:"enableThreeStateCheckboxes",
 checkbox_smart:"enableSmartCheckboxes",
 context_menu:"enableContextMenu",
 distributed_parsing:"enableDistributedParsing",
 drag:"enableDragAndDrop",
 drag_copy:"enableMercyDrag",
 drag_image:"enableImageDrag",
 drag_scroll:"enableDragAndDropScrolling",
 editor:"enableItemEditor",
 hover:"enableHighlighting",
 images:"enableTreeImages",
 image_fix:"enableIEImageFix",
 image_path:"setImagePath",
 lines:"enableTreeLines",
 loading_item:"enableLoadingItem",
 multiline:"enableMultiLineItems",
 multiselect:"enableMultiselection",
 navigation:"enableKeyboardNavigation",
 radio:"enableRadioButtons",
 radio_single:"enableSingleRadioMode",
 rtl:"enableRTL",
 search:"enableKeySearch",
 smart_parsing:"enableSmartXMLParsing",
 smart_rendering:"enableSmartRendering",
 text_icons:"enableTextSigns",
 xml:"loadXML",
 skin:"setSkin"
 },{});})();dhtmlXTreeObject.prototype._dp_init=function(dp){dp.attachEvent("insertCallback", function(upd, id, parent) {var data = this._loader.doXPath(".//item",upd);var text = data[0].getAttribute('text');this.obj.insertNewItem(parent, id, text, 0, 0, 0, 0, "CHILD");});dp.attachEvent("updateCallback", function(upd, id, parent) {var data = this._loader.doXPath(".//item",upd);var text = data[0].getAttribute('text');this.obj.setItemText(id, text);if (this.obj.getParentId(id)!= parent) {this.obj.moveItem(id, 'item_child', parent);};this.setUpdated(id, true, 'updated');});dp.attachEvent("deleteCallback", function(upd, id, parent) {this.obj.setUserData(id, this.action_param, "true_deleted");this.obj.deleteItem(id, false);});dp._methods=["setItemStyle","","changeItemId","deleteItem"];this.attachEvent("onEdit",function(state,id){if (state==3)dp.setUpdated(id,true)
 return true;});this.attachEvent("onDrop",function(id,id_2,id_3,tree_1,tree_2){if (tree_1==tree_2)dp.setUpdated(id,true);});this._onrdlh=function(rowId){var z=dp.getState(rowId);if (z=="inserted"){dp.set_invalid(rowId,false);dp.setUpdated(rowId,false);return true;};if (z=="true_deleted"){dp.setUpdated(rowId,false);return true;};dp.setUpdated(rowId,true,"deleted")
 return false;};this._onradh=function(rowId){dp.setUpdated(rowId,true,"inserted")
 };dp._getRowData=function(rowId){var data = {};var z=this.obj._globalIdStorageFind(rowId);var z2=z.parentObject;var i=0;for (i=0;i<z2.childsCount;i++)if (z2.childNodes[i]==z)break;data["tr_id"] = z.id;data["tr_pid"] = z2.id;data["tr_order"] = i;data["tr_text"] = z.span.innerHTML;z2=(z._userdatalist||"").split(",");for (i=0;i<z2.length;i++)data[z2[i]]=z.userData["t_"+z2[i]];return data;}};//(c)dhtmlx ltd. www.dhtmlx.com
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
function jsonPointer(data,parent){this.d=data;this.dp=parent;};jsonPointer.prototype={text:function(){var afff=function(n){var p=[];for(var i=0;i<n.length;i++)p.push("{"+sfff(n[i])+"}");return p.join(",");};var sfff=function(n){var p=[];for (var a in n)if (typeof(n[a])=="object"){if (a.length)p.push('"'+a+'":['+afff(n[a])+"]");else p.push('"'+a+'":{'+sfff(n[a])+"}");}else p.push('"'+a+'":"'+n[a]+'"');return p.join(",");};return "{"+sfff(this.d)+"}";},
 get:function(name){return this.d[name];},
 exists:function(){return !!this.d },
 content:function(){return this.d.content;},
 each:function(name,f,t){var a=this.d[name];var c=new jsonPointer();if (a)for (var i=0;i<a.length;i++){c.d=a[i];f.apply(t,[c,i]);}},
 get_all:function(){return this.d;},
 sub:function(name){return new jsonPointer(this.d[name],this.d) },
 sub_exists:function(name){return !!this.d[name];},
 each_x:function(name,rule,f,t,i){var a=this.d[name];var c=new jsonPointer(0,this.d);if (a)for (i=i||0;i<a.length;i++)if (a[i][rule]){c.d=a[i];if(f.apply(t,[c,i])==-1) return;}},
 up:function(name){return new jsonPointer(this.dp,this.d);},
 set:function(name,val){this.d[name]=val;},
 clone:function(name){return new jsonPointer(this.d,this.dp);},
 through:function(name,rule,v,f,t){var a=this.d[name];if (a.length)for (var i=0;i<a.length;i++){if (a[i][rule]!=null && a[i][rule]!="" && (!v || a[i][rule]==v )) {var c=new jsonPointer(a[i],this.d);f.apply(t,[c,i]);};var w=this.d;this.d=a[i];if (this.sub_exists(name)) this.through(name,rule,v,f,t);this.d=w;}}};dhtmlXTreeObject.prototype.loadJSArrayFile=function(file,afterCall){if (!this.parsCount)this.callEvent("onXLS",[this,this._ld_id]);this._ld_id=null;this.xmlstate=1;var that=this;this.XMLLoader=new dtmlXMLLoaderObject(function(){eval("var z="+arguments[4].xmlDoc.responseText);that.loadJSArray(z);},this,true,this.no_cashe);if (afterCall)this.XMLLoader.waitCall=afterCall;this.XMLLoader.loadXML(file);};dhtmlXTreeObject.prototype.loadCSV=function(file,afterCall){if (!this.parsCount)this.callEvent("onXLS",[this,this._ld_id]);this._ld_id=null;this.xmlstate=1;var that=this;this.XMLLoader=new dtmlXMLLoaderObject(function(){that.loadCSVString(arguments[4].xmlDoc.responseText);},this,true,this.no_cashe);if (afterCall)this.XMLLoader.waitCall=afterCall;this.XMLLoader.loadXML(file);};dhtmlXTreeObject.prototype.loadJSArray=function(ar,afterCall){var z=[];for (var i=0;i<ar.length;i++){if (!z[ar[i][1]])z[ar[i][1]]=[];z[ar[i][1]].push({id:ar[i][0],text:ar[i][2]});};var top={id: this.rootId};var f=function(top,f){if (z[top.id]){top.item=z[top.id];for (var j=0;j<top.item.length;j++)f(top.item[j],f);}};f(top,f);this.loadJSONObject(top,afterCall);};dhtmlXTreeObject.prototype.loadCSVString=function(csv,afterCall){var z=[];var ar=csv.split("\n");for (var i=0;i<ar.length;i++){var t=ar[i].split(",");if (!z[t[1]])z[t[1]]=[];z[t[1]].push({id:t[0],text:t[2]});};var top={id: this.rootId};var f=function(top,f){if (z[top.id]){top.item=z[top.id];for (var j=0;j<top.item.length;j++)f(top.item[j],f);}};f(top,f);this.loadJSONObject(top,afterCall);};dhtmlXTreeObject.prototype.loadJSONObject=function(json,afterCall){if (!this.parsCount)this.callEvent("onXLS",[this,null]);this.xmlstate=1;var p=new jsonPointer(json);this._parse(p);this._p=p;if (afterCall)afterCall();};dhtmlXTreeObject.prototype.loadJSON=function(file,afterCall){if (!this.parsCount)this.callEvent("onXLS",[this,this._ld_id]);this._ld_id=null;this.xmlstate=1;var that=this;this.XMLLoader=new dtmlXMLLoaderObject(function(){try {eval("var t="+arguments[4].xmlDoc.responseText);}catch(e){dhtmlxError.throwError("LoadXML", "Incorrect JSON", [
 (arguments[4].xmlDoc),
 this
 ]);return;};var p=new jsonPointer(t);that._parse(p);that._p=p;},this,true,this.no_cashe);if (afterCall)this.XMLLoader.waitCall=afterCall;this.XMLLoader.loadXML(file);};dhtmlXTreeObject.prototype.serializeTreeToJSON=function(){var out=['{"id":"'+this.rootId+'", "item":['];var p=[];for (var i=0;i<this.htmlNode.childsCount;i++)p.push(this._serializeItemJSON(this.htmlNode.childNodes[i]));out.push(p.join(","));out.push("]}");return out.join("");};dhtmlXTreeObject.prototype._serializeItemJSON=function(itemNode){var out=[];if (itemNode.unParsed)return (itemNode.unParsed.text());if (this._selected.length)var lid=this._selected[0].id;else lid="";var text=itemNode.span.innerHTML;if (this._xescapeEntities)for (var i=0;i<this._serEnts.length;i++)text=text.replace(this._serEnts[i][2],this._serEnts[i][1]);if (!this._xfullXML)out.push('{"id":"'+itemNode.id+'", '+(this._getOpenState(itemNode)==1?' "open":"1", ':'')+(lid==itemNode.id?' "select":"1",':'')+' "text":"'+text+'"'+( ((this.XMLsource)&&(itemNode.XMLload==0))?', "child":"1" ':''));else
 out.push('{"id":"'+itemNode.id+'", '+(this._getOpenState(itemNode)==1?' "open":"1", ':'')+(lid==itemNode.id?' "select":"1",':'')+' "text":"'+text+'", "im0":"'+itemNode.images[0]+'", "im1":"'+itemNode.images[1]+'", "im2":"'+itemNode.images[2]+'" '+(itemNode.acolor?(', "aCol":"'+itemNode.acolor+'" '):'')+(itemNode.scolor?(', "sCol":"'+itemNode.scolor+'" '):'')+(itemNode.checkstate==1?', "checked":"1" ':(itemNode.checkstate==2?', "checked":"-1"':''))+(itemNode.closeable?', "closeable":"1" ':'')+( ((this.XMLsource)&&(itemNode.XMLload==0))?', "child":"1" ':''));if ((this._xuserData)&&(itemNode._userdatalist))
 {out.push(', "userdata":[');var names=itemNode._userdatalist.split(",");var p=[];for (var i=0;i<names.length;i++)p.push('{"name":"'+names[i]+'" , "content":"'+itemNode.userData["t_"+names[i]]+'" }');out.push(p.join(","));out.push("]");};if (itemNode.childsCount){out.push(', "item":[');var p=[];for (var i=0;i<itemNode.childsCount;i++)p.push(this._serializeItemJSON(itemNode.childNodes[i]));out.push(p.join(","));out.push("]\n");};out.push("}\n")
 return out.join("");};//(c)dhtmlx ltd. www.dhtmlx.com
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/


dhtmlXTreeObject.prototype._serEnts=[["&","&amp;"],["<","&lt;"],[">","&gt;"]];dhtmlXTreeObject.prototype.registerXMLEntity=function(rChar,rEntity){this._serEnts[this._serEnts.length]=[rChar,rEntity,new RegExp(rChar,"g")];};dhtmlXTreeObject.prototype.setSerializationLevel=function(userData,fullXML,escapeEntities,userDataAsCData,DTD){this._xuserData=convertStringToBoolean(userData);this._xfullXML=convertStringToBoolean(fullXML);this._dtd=DTD;this._xescapeEntities=convertStringToBoolean(escapeEntities);if (convertStringToBoolean(userDataAsCData)){this._apreUC="<![CDATA[";this._apstUC="]]>";}else{};for (var i=0;i< this._serEnts.length;i++)this._serEnts[i][2]=new RegExp(this._serEnts[i][0],"g");};dhtmlXTreeObject.prototype.serializeTree=function(){if (this.stopEdit)this.stopEdit();this._apreUC=this._apreUC||"";this._apstUC=this._apstUC||"";var out='<?xml version="1.0"?>';if (this._dtd)out+="<!DOCTYPE tree SYSTEM \""+this._dtd+"\">";out+='<tree id="'+this.rootId+'">';if ((this._xuserData)&&(this._idpull[this.rootId]._userdatalist))
 {var names=this._idpull[this.rootId]._userdatalist.split(",");for (var i=0;i<names.length;i++)out+="<userdata name=\""+names[i]+"\">"+this._apreUC+this._idpull[this.rootId].userData["t_"+names[i]]+this._apstUC+"</userdata>";};for (var i=0;i<this.htmlNode.childsCount;i++)out+=this._serializeItem(this.htmlNode.childNodes[i]);out+="</tree>";return out;};dhtmlXTreeObject.prototype._serializeItem=function(itemNode){if (itemNode.unParsed)if (document.all)return itemNode.unParsed.d.xml;else{var xmlSerializer = new XMLSerializer();return xmlSerializer.serializeToString(itemNode.unParsed.d);};var out="";if (this._selected.length)var lid=this._selected[0].id;else lid="\"";var text=itemNode.span.innerHTML;if (this._xescapeEntities)for (var i=0;i<this._serEnts.length;i++)text=text.replace(this._serEnts[i][2],this._serEnts[i][1]);if (!this._xfullXML)out='<item id="'+itemNode.id+'" '+(this._getOpenState(itemNode)==1?' open="1" ':'')+(lid==itemNode.id?' select="1"':'')+' text="'+text+'"'+( ((this.XMLsource)&&(itemNode.XMLload==0))?" child=\"1\" ":"")+'>';else
 out='<item id="'+itemNode.id+'" '+(this._getOpenState(itemNode)==1?' open="1" ':'')+(lid==itemNode.id?' select="1"':'')+' text="'+text+'" im0="'+itemNode.images[0]+'" im1="'+itemNode.images[1]+'" im2="'+itemNode.images[2]+'" '+(itemNode.acolor?('aCol="'+itemNode.acolor+'" '):'')+(itemNode.scolor?('sCol="'+itemNode.scolor+'" '):'')+(itemNode.checkstate==1?'checked="1" ':(itemNode.checkstate==2?'checked="-1"':''))+(itemNode.closeable?'closeable="1" ':'')+( ((this.XMLsource)&&(itemNode.XMLload==0))?" child=\"1\" ":"")+'>';if ((this._xuserData)&&(itemNode._userdatalist))
 {var names=itemNode._userdatalist.split(",");for (var i=0;i<names.length;i++)out+="<userdata name=\""+names[i]+"\">"+this._apreUC+itemNode.userData["t_"+names[i]]+this._apstUC+"</userdata>";};for (var i=0;i<itemNode.childsCount;i++)out+=this._serializeItem(itemNode.childNodes[i]);out+="</item>";return out;};dhtmlXTreeObject.prototype.saveSelectedItem=function(name,cookie_param){name=name||"";this.setCookie("treeStateSelected"+name,this.getSelectedItemId(),cookie_param);};dhtmlXTreeObject.prototype.restoreSelectedItem=function(name){name=name||"";var z=this.getCookie("treeStateSelected"+name);this.selectItem(z,false);};dhtmlXTreeObject.prototype.enableAutoSavingSelected=function(mode,cookieName){this.assMode=convertStringToBoolean(mode);if ((this.assMode)&&(!this.oldOnSelect)){this.oldOnSelect=this.onRowSelect;this.onRowSelect=function(e,htmlObject,mode){if (!htmlObject)htmlObject=this;htmlObject.parentObject.treeNod.oldOnSelect(e,htmlObject,mode);if (htmlObject.parentObject.treeNod.assMode)htmlObject.parentObject.treeNod.saveSelectedItem(htmlObject.parentObject.treeNod.assCookieName);}};this.assCookieName=cookieName;};dhtmlXTreeObject.prototype.saveState=function(name,cookie_param){var z=this._escape(this.serializeTree());var kusok = 4000;if (z.length>kusok){if(navigator.appName.indexOf("Microsoft")!=-1)
 return false;this.setCookie("treeStatex"+name,Math.ceil(z.length/kusok));for (var i=0;i<Math.ceil(z.length/kusok);i++)
 {this.setCookie("treeStatex"+name+"x"+i,z.substr(i*kusok,kusok),cookie_param);}}else
 this.setCookie("treeStatex"+name,z,cookie_param);var z=this.getCookie("treeStatex"+name);if (!z){this.setCookie("treeStatex"+name,"",cookie_param);return false;};return true;};dhtmlXTreeObject.prototype.loadState=function(name){var z=this.getCookie("treeStatex"+name);if (!z)return false;if (z.length){if (z.toString().length<4)
 {var z2="";for (var i=0;i<z;i++){z2+=this.getCookie("treeStatex"+name+"x"+i);};z=z2;};this.loadXMLString((this.utfesc=="utf8")?decodeURI(z):unescape(z));};return true;};dhtmlXTreeObject.prototype.setCookie=function(name,value,cookie_param) {var str = name + "=" + value + (cookie_param?(";"+cookie_param):"");document.cookie = str;};dhtmlXTreeObject.prototype.getCookie=function(name) {var search = name + "=";if (document.cookie.length > 0){var offset = document.cookie.indexOf(search);if (offset != -1){offset += search.length;var end = document.cookie.indexOf(";", offset);if (end == -1)end = document.cookie.length;return document.cookie.substring(offset, end);}}};dhtmlXTreeObject.prototype.saveOpenStates=function(name,cookie_param){var z=[];for (var i=0;i<this.htmlNode.childsCount;i++)z=z.concat(this._collectOpenStates(this.htmlNode.childNodes[i]));z=z.join(this.dlmtr);this.setCookie("treeOpenStatex"+name,z,cookie_param);};dhtmlXTreeObject.prototype.loadOpenStates=function(name){for (var i=0;i<this.htmlNode.childsCount;i++)this._xcloseAll(this.htmlNode.childNodes[i]);this.allTree.childNodes[0].border = "1";this.allTree.childNodes[0].border = "0";var z=getCookie("treeOpenStatex"+name);if (z){var arr=z.split(this.dlmtr);for (var i=0;i<arr.length;i++){var zNode=this._globalIdStorageFind(arr[i]);if (zNode){if ((this.XMLsource)&&(!zNode.XMLload)&&(zNode.id!=this.rootId)){this._delayedLoad(zNode,"loadOpenStates('"+name+"')");return;}else
 this.openItem(arr[i]);}}};this.callEvent("onAllOpenDynamic",[]);};dhtmlXTreeObject.prototype._delayedLoad=function(node,name){this.afterLoadMethod=name;this.onLoadReserve = this.onXLE;this.onXLE=this._delayedLoadStep2;this.loadXML(this.XMLsource+getUrlSymbol(this.XMLsource)+"id="+this._escape(node.id));};dhtmlXTreeObject.prototype._delayedLoadStep2=function(tree){tree.onXLE=tree.onLoadReserve;window.setTimeout( function() {eval("tree."+tree.afterLoadMethod);},100);if (tree.onXLE)tree.onXLE(tree);tree.callEvent("onXLE",[tree]);};dhtmlXTreeObject.prototype._collectOpenStates=function(node){var list=[];if (this._getOpenState(node)==1)
 {list.push(node.id);for (var i=0;i<node.childsCount;i++)list=list.concat(this._collectOpenStates(node.childNodes[i]));};return list;};function setCookie(name,value) {document.cookie = name+'='+value;};function getCookie(name) {var search = name + "=";if (document.cookie.length > 0){var offset = document.cookie.indexOf(search);if (offset != -1){offset += search.length;var end = document.cookie.indexOf(";", offset);if (end == -1)end = document.cookie.length;return (document.cookie.substring(offset, end));}}};dhtmlXTreeObject.prototype.openAllItemsDynamic = function(itemId)
 {this.ClosedElem=new Array();this.G_node=null;var itemNode = this._globalIdStorageFind(itemId||this.rootId);if (itemNode.id != this.rootId && tree.getOpenState(itemNode.id)!= 0) this.openItem(itemId);this._openAllNodeChilds(itemNode, 0);if(this.ClosedElem.length>0){this.onLoadReserve = this.onXLE;this.onXLE=this._loadAndOpen;this._loadAndOpen(this);}};dhtmlXTreeObject.prototype._openAllNodeChilds = function(itemNode)
 {if ((itemNode.XMLload==0)||(itemNode.unParsed)) this.ClosedElem.push(itemNode);for (var i=0;i<itemNode.childsCount;i++){if(this._getOpenState(itemNode.childNodes[i])<0) this._HideShow(itemNode.childNodes[i],2);if(itemNode.childNodes[i].childsCount>0)this._openAllNodeChilds(itemNode.childNodes[i]);if ((itemNode.childNodes[i].XMLload==0)||(itemNode.childNodes[i].unParsed)) this.ClosedElem.push(itemNode.childNodes[i]);}};dhtmlXTreeObject.prototype._loadAndOpen = function(that)
 {if(that.G_node){that._openItem(that.G_node);that._openAllNodeChilds(that.G_node);that.G_node = null;};if(that.ClosedElem.length>0)that.G_node = that.ClosedElem.shift();if(that.G_node)if (that.G_node.unParsed)that.reParse(that.G_node);else
 window.setTimeout( function(){that._loadDynXML(that.G_node.id);},100);else
 {that.onXLE = that.onLoadReserve;if (that.onXLE)that.onXLE(that);that.callEvent("onAllOpenDynamic",[that]);}};dhtmlXTreeObject.prototype.openItemsDynamic=function(list,flag){if (this.onXLE==this._stepOpen)return;this._opnItmsDnmcFlg=convertStringToBoolean(flag);this.onLoadReserve = this.onXLE;this.onXLE=this._stepOpen;this.ClosedElem=list.split(",").reverse();this._stepOpen(this);};dhtmlXTreeObject.prototype._stepOpen=function(that){if(!that.ClosedElem.length){that.onXLE = that.onLoadReserve;if (that._opnItmsDnmcFlg)that.selectItem(that.G_node,true);if ((that.onXLE)&&(arguments[1])) that.onXLE.apply(that,arguments);that.callEvent("onOpenDynamicEnd",[]);return;};that.G_node=that.ClosedElem.pop();var temp=that._globalIdStorageFind(that.G_node);if (temp.XMLload===0)that.openItem(that.G_node);else{that.openItem(that.G_node);that._stepOpen(that);}};
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/




 dhtmlXTreeObject.prototype.enableLoadingItem=function(text) {this.attachEvent("onXLS",this._showFakeItem);this.attachEvent("onXLE",this._hideFakeItem);this._tfi_text=text||"Loading...";};dhtmlXTreeObject.prototype._showFakeItem=function(tree,id) {if ((id===null)||(this._globalIdStorageFind("fake_load_xml_"+id))) return;var temp = this.XMLsource;this.XMLsource=null;this.insertNewItem(id,"fake_load_xml_"+id,this._tfi_text);this.XMLsource=temp;};dhtmlXTreeObject.prototype._hideFakeItem=function(tree,id) {if (id===null)return;this.deleteItem("fake_load_xml_"+id);};
//v.2.6 build 100722

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.SiteBoundaryPicker = function(layerToQuery) {
	$.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
		createPickerDiv: function(idresults, position, callback) {
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
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.SpeciesLayerPicker = function(layerToQuery) {	
	/*
	* This function will convert the result of the ESRI Rest Identify call to a feature array 
	* which can be used by the Species Layer Picker Servlet. The values specfied below correspond to nbn.common.feature.FeatureType
	*/
	function createFeaturesFoundArray(idresults) { 
		return $.map(idresults.results, function(currResult) {
			var attributes = currResult.attributes;
			if(attributes.gridRef || attributes.GRIDREF)
				return 'GRID:' + attributes.gridRef || attributes.GRIDREF;
			else if (attributes.adminSiteKey)
				return 'SITEBOUNDARY:' + attributes.adminSiteKey;
		});
	}
	
	/*The following function defines the servlet which should be called depending on the current mode of the species layer*/ 
	function getSpeciesPickerServlet() {
		switch(layerToQuery.getMode()) {
			case layerToQuery.Modes.SPECIES : return 'SingleSpeciesPickerServlet';
			case layerToQuery.Modes.DESIGNATION : return 'DesignationPickerServlet';
			case layerToQuery.Modes.SINGLE_DATASET : return 'DatasetPickerServlet';
		}
	}
	
	/*This function will give the servlet url qualified with the parameters for a given layers current state*/
	function getSpeciesPickerServletWithParams(resultsFromIdentify, sitesFound) {	
		var servletParameters = layerToQuery.getNBNSpeciesLayerFilters();
		servletParameters.sites = sitesFound; //append the sites found to the parameter object for the layer
		return getSpeciesPickerServlet() + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(servletParameters),'&','?');
	}
	
	/*This function will take a javascript object which has a name and perhaps a link attribute and create a label from it*/
	function createLabel(toLabel) {
		return (!toLabel.link) ? toLabel.name : $('<a>').attr('target','_blank').attr('href',toLabel.link).html(toLabel.name);
	}
	
	/*This function will render a dataset object as html. Creates a link to metadata and shows the providing organisation*/ 
	function createDatasetLabel(dataset) {
		return $('<div>')
			.append($('<a>').attr('target','_blank').attr('href',dataset.link).html('Info'))
			.append(' - ')
			.append($('<strong>').html(dataset.organisation))
			.append(': ' + dataset.name);
	}
	
	/*This function creates the html representation of a record object*/
	function createRecordLabel(record) {
		var toReturn = $('<span>').addClass('nbn-picker-' + ((record.presence) ? 'presence' : 'absence') + 'Record');
		
		if (!record.presence)
			toReturn.append($('<span>').addClass('nbn-picker-recordType').append('Absence : '));
		if (record.sensitive) 
			toReturn.append($('<span>').addClass('nbn-picker-sensitiveRecord').append('Sensitive record - '));
		
		toReturn.append(record.species + ' - ' + record.date);
		if (record.site !== 'Site name protected') 
			toReturn.append(' - Site: ' + record.site);
		if (record.recorder !== '') 
			toReturn.append(' - Recorder: ' + record.recorder);
		if (record.determiner !== '') 
			toReturn.append(' - Determiner: ' + record.determiner);
		return toReturn;
	}
	
	/*START DEFINITIONS OF TAB CREATION FUNCTIONS*/
	function createSpeciesTabDiv(pickerResults) {
		var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
		$.each(pickerResults.SITES, function(sitename, site) {
			toReturn.append(createLabel(site));
			if(site.SPECIES.length > 1)
				toReturn.append(' - (' + site.SPECIES.length + ' Species)');
			toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(site.SPECIES, function(nbnsysCode) {
				return createLabel(pickerResults.TAXON[nbnsysCode]);
			}));
		});
		return toReturn;
	}
	
	function createDatasetsTabDiv(pickerResults) {
		var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
		$.each(pickerResults.SITES, function(sitename, site) {
			toReturn.append(createLabel(site));
			toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(site.DATASETS, function(records, datasetKey) {
				return createDatasetLabel(pickerResults.DATASETS[datasetKey]);
			}));
		});
		return toReturn;
	}
	
	function createRecordsTabDiv(pickerResults) {
		var toReturn = $('<div>'); //create a div to be returned to and turned into a tab
		$.each(pickerResults.SITES, function(sitename, site) {
			toReturn.append(createLabel(site)).append(' - (Maximum 250 Records Shown)');
			toReturn.append(nbn.util.ArrayTools.toJQueryRenderedList(site.DATASETS, function(records, datasetKey) {
				var datasetTitle = $('<div>').append($('<div>').html(createDatasetLabel(pickerResults.DATASETS[datasetKey]))); //append the dataset as a title for the records
				if(records.length) { //only return a response if there are records to show
					records = records.sort(function(a,b) { return pickerResults.RECORDS[b].presence - pickerResults.RECORDS[a].presence; }); //order the results so that presence and absence are grouped
					
					return datasetTitle.append(nbn.util.ArrayTools.toJQueryRenderedList(records, function(recordID) {
						return createRecordLabel(pickerResults.RECORDS[recordID]);
					}));
				}
				else if(!pickerResults.DATASETS[datasetKey].downloadRawData) //or leave a message if the user does does not have downloadRawData?
					return datasetTitle.append(nbn.util.ArrayTools.toJQueryRenderedList(["You do not have download raw data privilege for this dataset"],function(element) {
						return $('<em>').html(element); //convert the text to an em element
					}));
				else
					return false;
			}));
			if(site.additional && site.additional.recordComment) //Fix for NBNIV-553, could this be done in a better way?
				toReturn.append(createLabel(site.additional.recordComment).addClass('moreInfo'));
		});
		return toReturn;
	}
	/*END DEFINITIONS OF TAB CREATION FUNCTIONS*/
	
	$.extend(this, new nbn.layer.picker.ArcGisLayerFeaturePicker(layerToQuery, {
		createPickerDiv: function(resultsFromIdentify, position, callback) {
			var featuresFound = createFeaturesFoundArray(resultsFromIdentify);
			
			if(featuresFound.length!==0) {
				this.__lastRequest = $.getJSON(getSpeciesPickerServletWithParams(resultsFromIdentify, featuresFound), function(pickerResults) {
					var toReturn = $('<div>').addClass('nbn-picker-speciesResults');
					toReturn.nbn_dynamictabs();
					toReturn.nbn_dynamictabs('add','Records',createRecordsTabDiv(pickerResults));
					toReturn.nbn_dynamictabs('add','Datasets',createDatasetsTabDiv(pickerResults));
					toReturn.nbn_dynamictabs('add','Species',createSpeciesTabDiv(pickerResults));
					toReturn.tabs();
					callback(toReturn);
				});
				this.__lastRequest.error(function() {callback($('<div>').html('An error occured whilst trying to obtain a response from the picker server'));});
			}
			else
				callback($('<div>').html('No Results here'));
		},
		
		abort: function() {
			if(this.__lastRequest__) 
				this.__lastRequest__.abort(); //if there is a last request then abort it
		}
	}));
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 10th-December-2010
* @description	:- This JScript file will create a picker window of the current state of a list of NBNLayers 
* @dependencies	:-
*	nbn.util.ObservableCollection of type NBNLayers
*	nbn.layer.ArcGISMap
*	Google Maps V3
*	jquery ui
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.PickerInfoWindow = function(map, nbnMapLayers) {
	this.createOrUpdateInfoWindowIfApplicable = function(position) { //create the content of the Info window to put into the create info window
		var displayFlag = false;
		var containerDiv = $('<div>').nbn_dynamictabs();
		
		var _addDynamicTab = function(layer, pickerToAdd) {
			var currpickerInfo = pickerToAdd.getInfoWindowContents(position);//all infoable windows have the ability to return a contents to be added given a position
			if(currpickerInfo) {
				displayFlag=true;
				containerDiv.nbn_dynamictabs('add', (pickerToAdd.name) ? pickerToAdd.name : layer, currpickerInfo);
			}
		};
		
		nbnMapLayers.forEachNBNMapLayer(function(currLayer){
			if(currLayer.picker) { //does this layer have a picker?
				if($.isArray(currLayer.picker)) {
					for(var i in currLayer.picker)
						_addDynamicTab(currLayer.layer, currLayer.picker[i]);
				}
				else
					_addDynamicTab(currLayer.layer, currLayer.picker);
			}
		});
		containerDiv.tabs();
                
		if(displayFlag)
			map.showPickingDialogMapDialog(containerDiv,position);
	};
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 21-Feb-2010
* @description	:- This JScript file defines a means for the common logic of feature picking to be easily expanded. 
*
*	This class will initiate the JSON call required for identifying features on a map, whilst that is being processed a 
*	loading screen will be displayed. 
*
*	Processing of the results is caried out by the featureProcessor function which will return a jquery element representing the results. 
*	On completion, the results will fade in.
*
* @usage		:- new nbn.layer.picker.ArcGisLayerFeaturePicker(layer, function() {
*		return $('<div'); //returning the results
*	});
* @dependencies	:-
*	nbn.layer.ArcGISMap
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.picker = nbn.layer.picker || {};

nbn.layer.picker.ArcGisLayerFeaturePicker = function(layerToQuery, featureProcessor) {
	var _lastIdentifyCall;
	this.getInfoWindowContents = function(position) { //function required by PickerInfoWindow
		if(layerToQuery.isToRender() && layerToQuery.getEnabled()) {
			var infoContainer = $('<div>').addClass("nbn-picker-container");	 //create the container
			var loadingDiv = $('<div>').addClass('loading');
			infoContainer.append(loadingDiv);
			
			if(_lastIdentifyCall) //if there is a _lastIdentifyCall, abort it 
				_lastIdentifyCall.abort(); 
				
			_lastIdentifyCall = layerToQuery.identifyFeature(position, function (results) {
				if($.isFunction(featureProcessor.abort)) //if the feature processor has an abort function, call it
					featureProcessor.abort(); 
				featureProcessor.createPickerDiv(results, position, function(resultsDiv) {
					loadingDiv.fadeOut('slow',function() {//once returned then this is done loading
						$(this).remove();
						infoContainer.append(resultsDiv.fadeIn('slow'));
					});	
				});
			});
			return infoContainer;
		}
		else
			return false;
	};
};
/**
*
* @author       :- Christopher Johnson
* @date         :- 13th-December-2010
* @description  :- This JScript was created to replace the ArcGIS Library so that tiling can be done performantly.
* @dependencies :-
*   jquery
*   google maps
*	nbn.layer.ExtendedProjectedImageMap
*   nbn.uti.ArrayTools
*   nbn.util.ObservableAttribute
*   nbn.util.ObservableCollection
*   nbn.util.PropagatingObservableCollection
*   nbn.util.Logger
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.ArcGISMap = function(mapHosts, mapService, map, options) {
	options = options || {}; //ensure options is set
	var _me = this;
	var _updateTiles;
	
	var currVisLayers = new nbn.util.ObservableAttribute('CurrentVisibleLayers', [], nbn.util.ArrayTools.equals), currFilters = new nbn.util.ObservableAttribute('CurrentFilters', {});
	var currZoom = new nbn.util.ObservableAttribute('CurrentZoom'), currProjection = new nbn.util.ObservableAttribute('CurrentProjection');
		
	var _updateCurrentLayerFilterContainer = function() { //this function will combine all the filters and return the resultant filter
		var objOfFilters = {}, arrOfVisibleLayers = [];
		_me.forEachLayerFilter(function(currElement) {
			if(currElement.isFiltering()) { //is this arcgismap filter filtering?
				var currFilterContainer = currElement.getFilter(currZoom.getCurrentZoom()); //execute the filter function
				if(currFilterContainer.filters)
					$.extend(objOfFilters, currFilterContainer.filters);
				if(currFilterContainer.visibleLayers)
					arrOfVisibleLayers.push(currFilterContainer.visibleLayers);
			}
		});
		currFilters.setCurrentFilters(objOfFilters);
		currVisLayers.setCurrentVisibleLayers(nbn.util.ArrayTools.uniqueFlatten(arrOfVisibleLayers));
	};
	
	var _createIdentifyURL = function(latLng) {
		var boundedBox = map.getViewportBBox();
		var mapDiv = map.getViewportDimensions();
		var bboxSR = _me.getCurrentProjection().latLngEPSG;
		return encodeURI(
			_me.getHostsNextElement() + _me.getMapService() + '/identify?geometry={' +
			'x:' + latLng.lon +
			',y:' + latLng.lat + ',spatialReference:{wkid:' + bboxSR + '}}&geometryType=esriGeometryPoint&mapExtent={' +
			'xmin:' + boundedBox.xmin +
			',ymin:' + boundedBox.ymin +
			',xmax:' + boundedBox.xmax +
			',ymax:' + boundedBox.ymax + ',spatialReference:{wkid:' + bboxSR + '}}' +
			'&tolerance=' + _me.getIdentify() + '&sr=' + bboxSR + '&imageDisplay=' + mapDiv.width + ',' + mapDiv.height + ',96' +
			nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(_me.getCurrentFilters()),'&') +
			'&layers=all:' + _me.getCurrentVisibleLayers().join(',') +'&returnGeometry=false&f=json&callback=?'
		);
	};
	
	var _checkIfInPositionToMakeACall = function(typeOfCall) {
		if(!_me.getMapService() || !_me.getHosts())
			throw "I can not construct a valid url as either a map service or host has not been defined. Therefore an " + typeOfCall + " call can not be performed";
	};
	
	function createLegendURL() {
		var urlParams = $.extend({}, _me.getCurrentFilters(), { callback:'?', f:'pjson', layers: _me.getCurrentVisibleLayers().join(',')});
		return encodeURI(
			_me.getHostsNextElement() + _me.getMapService() + '/legend' + 
			nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(urlParams),'&', '?') 
		);
	}
	
	this.identifyFeature = function(latLng, callback) { //call the identification server
		_checkIfInPositionToMakeACall('identify');
		return $.getJSON(_createIdentifyURL(latLng), callback);
	};
	
	this.getLegend = function(callback) {
		_checkIfInPositionToMakeACall('legend');
		return $.getJSON(createLegendURL(), callback);
	};

	$.extend(this,
		new nbn.util.ObservableAttribute('ToRenderLogic',options.toRenderLogic || function(combinedFilter) {
			return combinedFilter.getCurrentVisibleLayers().length != 0;
		}),
		new nbn.util.ObservableAttribute('Identify', options.identifyTolerance || 3),
		new nbn.util.ObservableAttribute('UpdateDelay', options.updateDelay || 300),
		new nbn.util.ElementIterator('Hosts', mapHosts),
		new nbn.util.ObservableAttribute('MapService', mapService),
		new nbn.util.PropagatingObservableCollection('LayerFilter', ['Filter']),
		currVisLayers, currFilters, currZoom, currProjection
	);
	
	$.extend(this, new map.ImageMapConstructor(function(updateTiles) {
		var _updateTimeoutHandle;
		_updateTiles = function() {
			clearTimeout(_updateTimeoutHandle); //clear the original timeout
			_updateTimeoutHandle = setTimeout(function() {
				updateTiles();
			},_me.getUpdateDelay());
		};
		
		this.isToRender = function() {
			return (_me.getHosts() && _me.getMapService() && _me.getToRenderLogic()(_me)) == true;
		};
		
		this.getTileUrl = function(position, zoom, projection) {
			currZoom.setCurrentZoom(zoom);
			currProjection.setCurrentProjection(projection);
			if(this.isToRender()) {
				return encodeURI(
					_me.getHostsNextElement() + _me.getMapService() + 
                                        '?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap' +
                                        '&BBOX=' + position.xmin + ',' + position.ymin + ',' + position.xmax + ',' + position.ymax + 
					'&WIDTH=' + _me.tileSize.width + 
                                        '&HEIGHT=' + _me.tileSize.height +
					//'&imageSR=' + projection.imageEPSG +
					'&SRS=EPSG:' + projection.latLngEPSG +
					nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(_me.getCurrentFilters()),'&') +
					'&LAYERS='+ _me.getCurrentVisibleLayers().join(',') + 
					'&TRANSPARENT=true' +
					'&FORMAT=image/png'
				);
			}
		};
	}, map.getUnderlyingMap(), options));
	
	delete this.setCurrentVisibleLayers;
	delete this.setCurrentFilters;
	delete this.setCurrentZoom;
	delete this.setCurrentProjection;
	
	//Add action listeners to changes in attributes
	this.addToRenderLogicUpdateListener({ToRenderLogic: _updateTiles});
	this.addMapServiceUpdateListener({MapService: _updateTiles}); 
	this.addCurrentZoomUpdateListener({update: _updateCurrentLayerFilterContainer});
	
	var currentFilterUpdate = {
		update: function() {
			_updateCurrentLayerFilterContainer();
			_updateTiles();
		}
	};
	this.addLayerFilterCollectionUpdateListener(currentFilterUpdate);
	this.addLayerFilterPropagatingObservableCollectionUpdateListener(currentFilterUpdate);
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 1st-Feb-2011
* @description	:- This JScript represents the model for a Context-less ArcGisMapFilter. It adapts the observable 
*	JScript to provide listenable notifications of update events.
*
* 	The context-less nature means that a call to .getFilter() will require a zoom level to be presented. The context less logic means that a 
*	ArcGisMapFilter can be used on different ArcGisMaps simultaneously even if they are rendered on different Google Maps
* @dependencies	:-
*	nbn.util.ObservableAttribute
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.ArcGisMapFilter = function() { 
	var _observableAttribute = new nbn.util.ObservableAttribute('Filter');//adapts the observable attribute object
	$.extend(this,_observableAttribute); //copy all the observableAttributes methods

	/**This method will return a filter object for a particular zoom level*/
	this.getFilter = function(zoom) { //override the getFilterMethod
		var _filter = _observableAttribute.getFilter();
		if($.isFunction(_filter)) 
			return _filter(zoom); //execute dynamic filter
		else 
			return _filter; //return static filter
	};
		
	this.clearFilter = function() {
		_observableAttribute.setFilter(undefined);
	};
	
	/**This method will determine weather or not this ArcGisMapFilter has a filter set*/
	this.isFiltering = function() {
		return _observableAttribute.getFilter() != undefined;
	};
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 7th-April-2011
* @dependencies	:- This JScript file defines arc gis filter which will filter a layer based on a resolveable entity.
*	jquery
*	nbn.layer.ArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.ResolvingVisibleLayersArcGisMapFilter = function(type, initial) {
	var _underlyingFilter, _resolvedFilter;
	$.extend(this, _underlyingFilter = new nbn.layer.ArcGisMapFilter());
	
	this.setFilter = function(filterToSet) {
		if(filterToSet) {
			nbn.util.EntityResolver.resolve(new function() {
					this[type] = filterToSet;
				},function(resolvedFilter) {
					_resolvedFilter = resolvedFilter; //store the resolved object
					_underlyingFilter.setFilter({
						visibleLayers: (!$.isArray(resolvedFilter[type])) ? [resolvedFilter[type].gisLayerID] : nbn.util.ArrayTools.getArrayByFunction(resolvedFilter[type], function(element) {
							return element.gisLayerID;
						})
					});
			});
		}
	};
	
	this.getResolvedFilter = function() {
		return _resolvedFilter;
	};
	
	this.setFilter(initial); //set the map for an initial call
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 23rd-May-2011
* @description	:- This JScript creates a ArcGisMap layer which is based on a single ResolvingVisibleLayersArcGisMapFilter.
*	This object is cabaple of being reconstructed by the Construction factory of the IMT
* @dependencies	:-
*	nbn.layer.ArcGISMap
*	nbn.layer.ResolvingVisibleLayersArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.ResolvingVisibleContextLayer = function(type, resolvingParam, reconstructionObjectFromResolvedFilter, hosts, url, googleMap, options) {
	var _resolvingVisibleFilter = new nbn.layer.ResolvingVisibleLayersArcGisMapFilter(resolvingParam, options[resolvingParam])
	$.extend(this, new nbn.layer.ArcGISMap(hosts, url, googleMap, options));
	this.addLayerFilter(_resolvingVisibleFilter);
	
	this.getResolvingVisibleFilter = function() {
		return _resolvingVisibleFilter;
	};
	
	this.getReconstructionObject = function() {
		var toReturn = {type: type};
		if(_resolvingVisibleFilter.isFiltering())
			toReturn[resolvingParam] = reconstructionObjectFromResolvedFilter(_resolvingVisibleFilter.getResolvedFilter()[resolvingParam])
		return toReturn;
	};
}
/**
*
* @author		:- Christopher Johnson
* @date			:- 1st-Feb-2011
* @description	:- This JScript extends the ArcGisFilter by adding the ability to switch the logic on and off
* @dependencies	:-
*	nbn.util.ObservableAttribute
*	nbn.layer.ArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.SwitchableArcGisMapFilter = function(defaultState) {
	var _me = this, underlyingFilterLogic, underlyingFilter;
	$.extend(this,
		new nbn.util.ObservableAttribute('Enabled',defaultState),
		underlyingFilter = new nbn.layer.ArcGisMapFilter()
	);
	
	this.setFilter = function(filterLogic) {
		underlyingFilterLogic = filterLogic;
		if(_me.getEnabled())
			underlyingFilter.setFilter(filterLogic); //pass the logic through if this filter is enabled
	};
	
	this.addEnabledUpdateListener({
		Enabled: function(newValue) {
			if(newValue)
				_me.setFilter(underlyingFilterLogic);
			else
				_me.clearFilter(); //in effect disable filter
		}
	});
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 1st-Feb-2011
* @description	:- This JScript represents the model for a Context ArcGisMapFilter. The context being a ArcGISMap object.
*	This particular filter requires some context to exist in so that calls to .getFilter() can return a contextual response.
*
*	Optionally a ArcGisMapFilter to which this one is based on can be provided. This heirarchy enables a general filter to 
*	be used in multiple contexts. That is a single filter for multiple ArcGisMaps
* @dependencies	:-
*	nbn.util.Observable
*	nbn.layer.ArcGisMapFilter
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};
nbn.layer.ContextArcGisMapFilter = function(context, filter) {
	$.extend(this, filter = filter || new nbn.layer.ArcGisMapFilter()); //either extend a passed in filters functionality, or create a new one to extend
	
	var filterInContext = new nbn.util.ObservableAttribute('Filter', filter.getFilter(context.getZoom()));
	var _contextListenerHandle, amountOfListeners = 0; //vars which handle the context listenering
	
	var _updateFunction = function() { //create the function to call when an update to either the zoom or the filter changes
		filterInContext.setFilter(filter.getFilter(context.getZoom()));
	};
	
	var _filterObject = { //create an update object to add to the filter this object represents
		update: _updateFunction
	};

	this.getFilter = filterInContext.getFilter; //return the contextual filter rather than the underlyinging, possibly dynamic filter
	
	this.addFilterUpdateListener = function(toAdd) {
		if(++amountOfListeners == 1) { //if added first listener, register to the context listener
			context.addZoomUpdateListener(_filterObject);
			filter.addFilterUpdateListener(_filterObject);
		}
		filterInContext.addFilterUpdateListener(toAdd);
	};
	
	this.removeFilterUpdateListener = function(toRemove) {
		if(--amountOfListeners == 0) { //if removed last lisener, remove the context listener
			context.removeZoomUpdateListener(_filterObject);
			filter.removeFilterUpdateListener(_filterObject);
		}
			
		return filterInContext.removeFilterUpdateListener(toRemove);
	};
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 23rd-May-2011
* @description	:- This JScript defines the Concrete constructors for the Context Layers
* @dependencies	:-
*	nbn.layer.ResolvingVisibleContextLayer
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

(function() {
	var obtainElementDatasetKey = function(elem) {
		return elem.datasetKey;
	}
	
	var reconstructionObjectFromResolvedFilter = function(resolvedObject) {
		return nbn.util.ArrayTools.getArrayByFunction(resolvedObject, obtainElementDatasetKey).join(',');
	}
	
	nbn.layer.BoundaryLayer = function(hosts, googleMap, options) {	
		$.extend(this, new nbn.layer.ResolvingVisibleContextLayer('Boundary', 'boundary', obtainElementDatasetKey, hosts, 'arcgis/rest/services/siteBoundary/siteBoundaries/MapServer', googleMap, options)); //extend the layer
	};
	
	nbn.layer.HabitatLayer = function(hosts, googleMap, options) {	
		$.extend(this, new nbn.layer.ResolvingVisibleContextLayer('Habitat', 'habitats', reconstructionObjectFromResolvedFilter, hosts, 'arcgis/rest/services/habitat/habitats/MapServer', googleMap, options)); //extend the layer
	};
})();
/**
*
* @author		:- Christopher Johnson
* @date			:- 23rd-Feb-2011
* @description	:- This JScript file defines the logic of switching between the different map services based on what filters are set.
* @dependencies	:-
*	jquery
*	nbn.layer.ArcGISMap
*/

window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.SpeciesLayer = function(hosts, googleMap, options) {
	var _me = this;
	var _modeAttribute = new nbn.util.ObservableAttribute('Mode');
	var _datasetFilter = new nbn.layer.ArcGisMapFilter();
	var _temporalFilter = new nbn.layer.ArcGisMapFilter();
	var _abundanceFilter = new nbn.layer.SwitchableArcGisMapFilter({filters:{abundance:"all"}});
	var _modeFunctions = [], _modeNameLookup = [];
	
	var additionalFilterParams = {}, _constructionFilterParams = {};
	var _arcGisMap, _descriptionAttribute, _layerParameters;

    $.extend(this,
		_arcGisMap = new nbn.layer.ArcGISMap(hosts, undefined, googleMap, options),
		_modeAttribute,
		_descriptionAttribute = new nbn.util.ObservableAttribute('Description', 'No species selected'),
		_layerParameters = new nbn.util.ObservableAttribute('LayerParameters')
	);
	
	this.Modes = new function() {
		var _previousNameResolvingRequest;
		
		var _creationFunctionManager = new function(_self) {	
			this.createNBNSpeciesLayerMode = function(name, func) {
				_self[name] = _modeFunctions.push(function(){
					if(_previousNameResolvingRequest)
						_previousNameResolvingRequest.abort();
					func.apply(this,arguments);
				})-1;
				_modeNameLookup[_self[name]] = name; //store the id to do a lookup later
			};
		}(this);
		
		var _setDatasets = function(datasets) {
			if(datasets && datasets.length > 0) {
				var datasetKeys = [];
				for(var i in datasets)
					datasetKeys.push(datasets[i].datasetKey);
				_datasetFilter.setFilter({ //set the filter to the correct state
					filters: {
						datasets: datasetKeys.join(',')
					}
				});
			}
			else
				_datasetFilter.clearFilter();
		};
		
		var _createDatasetFilteringSummary = function(datasets) {
			if(datasets && datasets.length > 0) {
				if(datasets.length == 1)
					return ' filtered by ' + datasets[0].name;
				else
					return ' filtered by ' + datasets.length + ' datasets.';
			}
			else
				return '';
		};
			
		_creationFunctionManager.createNBNSpeciesLayerMode('SPECIES', function(species, datasets) {
			nbn.util.EntityResolver.resolve({
					species: species,
					datasets: datasets
				}, function(data) {
					_setDatasets(data.datasets);
					_abundanceFilter.setEnabled(true);
					_constructionFilterParams = additionalFilterParams = {species: data.species.taxonVersionKey};
					_arcGisMap.setMapService('SingleSpecies/' + data.species.taxonVersionKey);
					_descriptionAttribute.setDescription('Single Species Map for ' + data.species.name + _createDatasetFilteringSummary(data.datasets));
					_layerParameters.setLayerParameters(data);
			});
		});
		
		_creationFunctionManager.createNBNSpeciesLayerMode('DESIGNATION', function(designation, datasets) {
			nbn.util.EntityResolver.resolve({
					designation: designation,
					datasets: datasets
				}, function(data) {
					_setDatasets(data.datasets);
					_abundanceFilter.setEnabled(false);
					_constructionFilterParams = {designation: data.designation.designationKey};
					additionalFilterParams = {desig: data.designation.designationKey};
					_arcGisMap.setMapService('DesignationSpeciesDensity/' + data.designation.designationKey);
					_descriptionAttribute.setDescription('Designation Map for ' + data.designation.name + _createDatasetFilteringSummary(data.datasets));
					_layerParameters.setLayerParameters(data);
			});
		});
		
		_creationFunctionManager.createNBNSpeciesLayerMode('SINGLE_DATASET', function(dataset) {
			nbn.util.EntityResolver.resolve({
					dataset: dataset
				}, function(data) {
					_datasetFilter.clearFilter();
					_abundanceFilter.setEnabled(false);
					_constructionFilterParams = {dataset: data.dataset.datasetKey};
					additionalFilterParams = {datasets: data.dataset.datasetKey};
					_arcGisMap.setMapService('DatasetSpeciesDensity/' + data.dataset.datasetKey);
					_descriptionAttribute.setDescription('Single Dataset map for ' + data.dataset.name);
					_layerParameters.setLayerParameters(data);
			});	
		});
		
		_creationFunctionManager.createNBNSpeciesLayerMode('NONE', function() {
			_datasetFilter.clearFilter();
			_constructionFilterParams = additionalFilterParams = {};
			_arcGisMap.setMapService();
			_descriptionAttribute.setDescription('No species selected');
			_layerParameters.setLayerParameters();
		});
	};
	
	delete this.setMapService;
	delete this.setDescription;
	delete this.setLayerParameters;
	
	this.addLayerFilter(_datasetFilter);
	this.addLayerFilter(this.YearFilter = _temporalFilter); //publicise the year filter
	this.addLayerFilter(this.AbundanceFilter = _abundanceFilter); //publicise the year filter
	
	this.getNBNSpeciesLayerFilters = function() {
		return $.extend({},additionalFilterParams,this.getCurrentFilters());
	};
	
	this.getReconstructionObject = function() {
		var toReturn = {type: 'Species'};
		if(_modeAttribute.getMode() !== _me.Modes.NONE) { //if this mode is filtering
			$.extend(toReturn, {
				mode: _modeNameLookup[_modeAttribute.getMode()]
			}, _constructionFilterParams);
			
			if(_datasetFilter.isFiltering()) $.extend(toReturn, _datasetFilter.getFilter().filters); //add datasets if they exist
			if(_temporalFilter.isFiltering()) $.extend(toReturn, _temporalFilter.getFilter().filters); //add temp filter if it exists
		}
		return toReturn;
	};
	
	this.setMode = function(mode) {
		try {
			var args = Array.prototype.slice.call(arguments, 1); //remove the first element from the arguments as that is the mode
			var modeDetails = _modeFunctions[mode].apply(this,args);
			_modeAttribute.setMode(mode); //call the overridden setMode method
		}
		catch(err) {
			throw "Unable to set mode " + options.mode + " " + err;
		}
	};
	
	this.getSpeciesLayerServiceName = function() { //define the function which returns the current Service name for the current set mode
		switch(_me.getMode()) {
			case _me.Modes.DESIGNATION 		: return "Designation Species Density";
			case _me.Modes.SINGLE_DATASET 	: return "Dataset Species Density";
			case _me.Modes.SPECIES	 		: return "Single Species Layer";
			case _me.Modes.NONE				: return "No Species Layer Selected";
			default	: return "Unknown Mode";
		}		
	};

	this.addCurrentVisibleLayersUpdateListener = function(filter) { //override the addCurrentVisibleLayersUpdateListener to notify of changes about abundance
		_abundanceFilter.addFilterUpdateListener(filter);
		_arcGisMap.addCurrentVisibleLayersUpdateListener(filter);
	};
	
	this.removeCurrentVisibleLayersUpdateListener = function(filter) { //override the removeCurrentVisibleLayersUpdateListener to notify of changes about abundance
		_abundanceFilter.removeFilterUpdateListener(filter);
		_arcGisMap.removeCurrentVisibleLayersUpdateListener(filter);
	};

	(function() { //define constuction function
		var initialMode = options.mode || 'NONE'; //set the default mode
		switch(initialMode) {
			case 'SPECIES':
				_me.setMode(_me.Modes[initialMode], options.species, options.datasets);
			break;
			case 'DESIGNATION':
				_me.setMode(_me.Modes[initialMode], options.designation, options.datasets);
			break;
			case 'SINGLE_DATASET':
				_me.setMode(_me.Modes[initialMode], options.dataset);
			break;
			case 'NONE' :
				_me.setMode(_me.Modes[initialMode]);
			break;
			default:
				throw "Unknown Species Layer Mode : " + options.mode;
		}
		
		if(options.startyear || options.endyear) {//initalise the temportal filter
			_temporalFilter.setFilter({
				filters: {
					startyear: options.startyear,
					endyear: options.endyear
				}
			});
		}
	})();
}
/**
*
* @author       :- Christopher Johnson
* @date         :- 4-Aug-2011
* @description  :- This JScript represents a BaseLayer which can represent a base layer of the interactive map.
*	It copies the pre constructed nbn layer and adds the additional properties required to be an interactive map base layer
*/
window.nbn = window.nbn || {};
nbn.layer = nbn.layer || {};

nbn.layer.BaseLayer = function(layer, options) {
	options = options || {}; //ensure options is constructed 
	
	$.extend(this, layer, 
		new nbn.util.ObservableAttribute('ID',options.id || layer.getName())//simply extend the input layer and add an ID param
	); 
};
/*
  proj4js.js -- Javascript reprojection library. 
  
  Authors:      Mike Adair madairATdmsolutions.ca
                Richard Greenwood richATgreenwoodmap.com
                Didier Richard didier.richardATign.fr
                Stephen Irons
  License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html 
                Note: This program is an almost direct port of the C library
                Proj4.
*/
/* ======================================================================
    proj4js.js
   ====================================================================== */

/*
Author:       Mike Adair madairATdmsolutions.ca
              Richard Greenwood rich@greenwoodmap.com
License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: Proj.js 2956 2007-07-09 12:17:52Z steven $
*/

/**
 * Namespace: Proj4js
 *
 * Proj4js is a JavaScript library to transform point coordinates from one 
 * coordinate system to another, including datum transformations.
 *
 * This library is a port of both the Proj.4 and GCTCP C libraries to JavaScript. 
 * Enabling these transformations in the browser allows geographic data stored 
 * in different projections to be combined in browser-based web mapping 
 * applications.
 * 
 * Proj4js must have access to coordinate system initialization strings (which
 * are the same as for PROJ.4 command line).  Thes can be included in your 
 * application using a <script> tag or Proj4js can load CS initialization 
 * strings from a local directory or a web service such as spatialreference.org.
 *
 * Similarly, Proj4js must have access to projection transform code.  These can
 * be included individually using a <script> tag in your page, built into a 
 * custom build of Proj4js or loaded dynamically at run-time.  Using the
 * -combined and -compressed versions of Proj4js includes all projection class
 * code by default.
 *
 * Note that dynamic loading of defs and code happens ascynchrously, check the
 * Proj.readyToUse flag before using the Proj object.  If the defs and code
 * required by your application are loaded through script tags, dynamic loading
 * is not required and the Proj object will be readyToUse on return from the 
 * constructor.
 * 
 * All coordinates are handled as points which have a .x and a .y property
 * which will be modified in place.
 *
 * Override Proj4js.reportError for output of alerts and warnings.
 *
 * See http://trac.osgeo.org/proj4js/wiki/UserGuide for full details.
*/

/**
 * Global namespace object for Proj4js library
 */
Proj4js = {

    /**
     * Property: defaultDatum
     * The datum to use when no others a specified
     */
    defaultDatum: 'WGS84',                  //default datum

    /** 
    * Method: transform(source, dest, point)
    * Transform a point coordinate from one map projection to another.  This is
    * really the only public method you should need to use.
    *
    * Parameters:
    * source - {Proj4js.Proj} source map projection for the transformation
    * dest - {Proj4js.Proj} destination map projection for the transformation
    * point - {Object} point to transform, may be geodetic (long, lat) or
    *     projected Cartesian (x,y), but should always have x,y properties.
    */
    transform: function(source, dest, point) {
        if (!source.readyToUse || !dest.readyToUse) {
            this.reportError("Proj4js initialization for "+source.srsCode+" not yet complete");
            return point;
        }
        
        // Workaround for Spherical Mercator
        if ((source.srsProjNumber =="900913" && dest.datumCode != "WGS84") ||
            (dest.srsProjNumber == "900913" && source.datumCode != "WGS84")) {
            var wgs84 = Proj4js.WGS84;
            this.transform(source, wgs84, point);
            source = wgs84;
        }

        // Transform source points to long/lat, if they aren't already.
        if ( source.projName=="longlat") {
            point.x *= Proj4js.common.D2R;  // convert degrees to radians
            point.y *= Proj4js.common.D2R;
        } else {
            if (source.to_meter) {
                point.x *= source.to_meter;
                point.y *= source.to_meter;
            }
            source.inverse(point); // Convert Cartesian to longlat
        }

        // Adjust for the prime meridian if necessary
        if (source.from_greenwich) { 
            point.x += source.from_greenwich; 
        }

        // Convert datums if needed, and if possible.
        point = this.datum_transform( source.datum, dest.datum, point );

        // Adjust for the prime meridian if necessary
        if (dest.from_greenwich) {
            point.x -= dest.from_greenwich;
        }

        if( dest.projName=="longlat" ) {             
            // convert radians to decimal degrees
            point.x *= Proj4js.common.R2D;
            point.y *= Proj4js.common.R2D;
        } else  {               // else project
            dest.forward(point);
            if (dest.to_meter) {
                point.x /= dest.to_meter;
                point.y /= dest.to_meter;
            }
        }
        return point;
    }, // transform()

    /** datum_transform()
      source coordinate system definition,
      destination coordinate system definition,
      point to transform in geodetic coordinates (long, lat, height)
    */
    datum_transform : function( source, dest, point ) {

      // Short cut if the datums are identical.
      if( source.compare_datums( dest ) ) {
          return point; // in this case, zero is sucess,
                    // whereas cs_compare_datums returns 1 to indicate TRUE
                    // confusing, should fix this
      }

      // Explicitly skip datum transform by setting 'datum=none' as parameter for either source or dest
      if( source.datum_type == Proj4js.common.PJD_NODATUM
          || dest.datum_type == Proj4js.common.PJD_NODATUM) {
          return point;
      }

      // If this datum requires grid shifts, then apply it to geodetic coordinates.
      if( source.datum_type == Proj4js.common.PJD_GRIDSHIFT )
      {
        alert("ERROR: Grid shift transformations are not implemented yet.");
        /*
          pj_apply_gridshift( pj_param(source.params,"snadgrids").s, 0,
                              point_count, point_offset, x, y, z );
          CHECK_RETURN;

          src_a = SRS_WGS84_SEMIMAJOR;
          src_es = 0.006694379990;
        */
      }

      if( dest.datum_type == Proj4js.common.PJD_GRIDSHIFT )
      {
        alert("ERROR: Grid shift transformations are not implemented yet.");
        /*
          dst_a = ;
          dst_es = 0.006694379990;
        */
      }

      // Do we need to go through geocentric coordinates?
      if( source.es != dest.es || source.a != dest.a
          || source.datum_type == Proj4js.common.PJD_3PARAM
          || source.datum_type == Proj4js.common.PJD_7PARAM
          || dest.datum_type == Proj4js.common.PJD_3PARAM
          || dest.datum_type == Proj4js.common.PJD_7PARAM)
      {

        // Convert to geocentric coordinates.
        source.geodetic_to_geocentric( point );
        // CHECK_RETURN;

        // Convert between datums
        if( source.datum_type == Proj4js.common.PJD_3PARAM || source.datum_type == Proj4js.common.PJD_7PARAM ) {
          source.geocentric_to_wgs84(point);
          // CHECK_RETURN;
        }

        if( dest.datum_type == Proj4js.common.PJD_3PARAM || dest.datum_type == Proj4js.common.PJD_7PARAM ) {
          dest.geocentric_from_wgs84(point);
          // CHECK_RETURN;
        }

        // Convert back to geodetic coordinates
        dest.geocentric_to_geodetic( point );
          // CHECK_RETURN;
      }

      // Apply grid shift to destination if required
      if( dest.datum_type == Proj4js.common.PJD_GRIDSHIFT )
      {
        alert("ERROR: Grid shift transformations are not implemented yet.");
        // pj_apply_gridshift( pj_param(dest.params,"snadgrids").s, 1, point);
        // CHECK_RETURN;
      }
      return point;
    }, // cs_datum_transform

    /**
     * Function: reportError
     * An internal method to report errors back to user. 
     * Override this in applications to report error messages or throw exceptions.
     */
    reportError: function(msg) {
      //console.log(msg);
    },

/**
 *
 * Title: Private Methods
 * The following properties and methods are intended for internal use only.
 *
 * This is a minimal implementation of JavaScript inheritance methods so that 
 * Proj4js can be used as a stand-alone library.
 * These are copies of the equivalent OpenLayers methods at v2.7
 */
 
/**
 * Function: extend
 * Copy all properties of a source object to a destination object.  Modifies
 *     the passed in destination object.  Any properties on the source object
 *     that are set to undefined will not be (re)set on the destination object.
 *
 * Parameters:
 * destination - {Object} The object that will be modified
 * source - {Object} The object with properties to be set on the destination
 *
 * Returns:
 * {Object} The destination object.
 */
    extend: function(destination, source) {
      destination = destination || {};
      if(source) {
          for(var property in source) {
              var value = source[property];
              if(value !== undefined) {
                  destination[property] = value;
              }
          }
      }
      return destination;
    },

/**
 * Constructor: Class
 * Base class used to construct all other classes. Includes support for 
 *     multiple inheritance. 
 *  
 */
    Class: function() {
      var Class = function() {
          this.initialize.apply(this, arguments);
      };
  
      var extended = {};
      var parent;
      for(var i=0; i<arguments.length; ++i) {
          if(typeof arguments[i] == "function") {
              // get the prototype of the superclass
              parent = arguments[i].prototype;
          } else {
              // in this case we're extending with the prototype
              parent = arguments[i];
          }
          Proj4js.extend(extended, parent);
      }
      Class.prototype = extended;
      
      return Class;
    },

    /**
     * Function: bind
     * Bind a function to an object.  Method to easily create closures with
     *     'this' altered.
     * 
     * Parameters:
     * func - {Function} Input function.
     * object - {Object} The object to bind to the input function (as this).
     * 
     * Returns:
     * {Function} A closure with 'this' set to the passed in object.
     */
    bind: function(func, object) {
        // create a reference to all arguments past the second one
        var args = Array.prototype.slice.apply(arguments, [2]);
        return function() {
            // Push on any additional arguments from the actual function call.
            // These will come after those sent to the bind call.
            var newArgs = args.concat(
                Array.prototype.slice.apply(arguments, [0])
            );
            return func.apply(object, newArgs);
        };
    },
    
/**
 * The following properties and methods handle dynamic loading of JSON objects.
 *
    /**
     * Property: scriptName
     * {String} The filename of this script without any path.
     */
    scriptName: "proj4js-combined.js",

    /**
     * Property: defsLookupService
     * AJAX service to retreive projection definition parameters from
     */
    defsLookupService: 'http://spatialreference.org/ref',

    /**
     * Property: libPath
     * internal: http server path to library code.
     */
    libPath: null,

    /**
     * Function: getScriptLocation
     * Return the path to this script.
     *
     * Returns:
     * Path to this script
     */
    getScriptLocation: function () {
        if (this.libPath) return this.libPath;
        var scriptName = this.scriptName;
        var scriptNameLen = scriptName.length;

        var scripts = document.getElementsByTagName('script');
        for (var i = 0; i < scripts.length; i++) {
            var src = scripts[i].getAttribute('src');
            if (src) {
                var index = src.lastIndexOf(scriptName);
                // is it found, at the end of the URL?
                if ((index > -1) && (index + scriptNameLen == src.length)) {
                    this.libPath = src.slice(0, -scriptNameLen);
                    break;
                }
            }
        }
        return this.libPath||"";
    },

    /**
     * Function: loadScript
     * Load a JS file from a URL into a <script> tag in the page.
     * 
     * Parameters:
     * url - {String} The URL containing the script to load
     * onload - {Function} A method to be executed when the script loads successfully
     * onfail - {Function} A method to be executed when there is an error loading the script
     * loadCheck - {Function} A boolean method that checks to see if the script 
     *            has loaded.  Typically this just checks for the existance of
     *            an object in the file just loaded.
     */
    loadScript: function(url, onload, onfail, loadCheck) {
      var script = document.createElement('script');
      script.defer = false;
      script.type = "text/javascript";
      script.id = url;
      script.src = url;
      script.onload = onload;
      script.onerror = onfail;
      script.loadCheck = loadCheck;
      if (/MSIE/.test(navigator.userAgent)) {
        script.onreadystatechange = this.checkReadyState;
      }
      document.getElementsByTagName('head')[0].appendChild(script);
    },
    
    /**
     * Function: checkReadyState
     * IE workaround since there is no onerror handler.  Calls the user defined 
     * loadCheck method to determine if the script is loaded.
     * 
     */
    checkReadyState: function() {
      if (this.readyState == 'loaded') {
        if (!this.loadCheck()) {
          this.onerror();
        } else {
          this.onload();
        }
      }
    }
};

/**
 * Class: Proj4js.Proj
 *
 * Proj objects provide transformation methods for point coordinates
 * between geodetic latitude/longitude and a projected coordinate system. 
 * once they have been initialized with a projection code.
 *
 * Initialization of Proj objects is with a projection code, usually EPSG codes,
 * which is the key that will be used with the Proj4js.defs array.
 * 
 * The code passed in will be stripped of colons and converted to uppercase
 * to locate projection definition files.
 *
 * A projection object has properties for units and title strings.
 */
Proj4js.Proj = Proj4js.Class({

  /**
   * Property: readyToUse
   * Flag to indicate if initialization is complete for this Proj object
   */
  readyToUse: false,   
  
  /**
   * Property: title
   * The title to describe the projection
   */
  title: null,  
  
  /**
   * Property: projName
   * The projection class for this projection, e.g. lcc (lambert conformal conic,
   * or merc for mercator).  These are exactly equivalent to their Proj4 
   * counterparts.
   */
  projName: null,
  /**
   * Property: units
   * The units of the projection.  Values include 'm' and 'degrees'
   */
  units: null,
  /**
   * Property: datum
   * The datum specified for the projection
   */
  datum: null,
  /**
   * Property: x0
   * The x coordinate origin
   */
  x0: 0,
  /**
   * Property: y0
   * The y coordinate origin
   */
  y0: 0,

  /**
   * Constructor: initialize
   * Constructor for Proj4js.Proj objects
  *
  * Parameters:
  * srsCode - a code for map projection definition parameters.  These are usually
  * (but not always) EPSG codes.
  */
  initialize: function(srsCode) {
      this.srsCodeInput = srsCode;
      // DGR 2008-08-03 : support urn and url
      if (srsCode.indexOf('urn:') == 0) {
          //urn:ORIGINATOR:def:crs:CODESPACE:VERSION:ID
          var urn = srsCode.split(':');
          if ((urn[1] == 'ogc' || urn[1] =='x-ogc') &&
              (urn[2] =='def') &&
              (urn[3] =='crs')) {
              srsCode = urn[4]+':'+urn[urn.length-1];
          }
      } else if (srsCode.indexOf('http://') == 0) {
          //url#ID
          var url = srsCode.split('#');
          if (url[0].match(/epsg.org/)) {
            // http://www.epsg.org/#
            srsCode = 'EPSG:'+url[1];
          } else if (url[0].match(/RIG.xml/)) {
            //http://librairies.ign.fr/geoportail/resources/RIG.xml#
            //http://interop.ign.fr/registers/ign/RIG.xml#
            srsCode = 'IGNF:'+url[1];
          }
      }
      this.srsCode = srsCode.toUpperCase();
      if (this.srsCode.indexOf("EPSG") == 0) {
          this.srsCode = this.srsCode;
          this.srsAuth = 'epsg';
          this.srsProjNumber = this.srsCode.substring(5);
      // DGR 2007-11-20 : authority IGNF
      } else if (this.srsCode.indexOf("IGNF") == 0) {
          this.srsCode = this.srsCode;
          this.srsAuth = 'IGNF';
          this.srsProjNumber = this.srsCode.substring(5);
      // DGR 2008-06-19 : pseudo-authority CRS for WMS
      } else if (this.srsCode.indexOf("CRS") == 0) {
          this.srsCode = this.srsCode;
          this.srsAuth = 'CRS';
          this.srsProjNumber = this.srsCode.substring(4);
      } else {
          this.srsAuth = '';
          this.srsProjNumber = this.srsCode;
      }
      this.loadProjDefinition();
  },
  
/**
 * Function: loadProjDefinition
 *    Loads the coordinate system initialization string if required.
 *    Note that dynamic loading happens asynchronously so an application must 
 *    wait for the readyToUse property is set to true.
 *    To prevent dynamic loading, include the defs through a script tag in
 *    your application.
 *
 */
    loadProjDefinition: function() {
      //check in memory
      if (Proj4js.defs[this.srsCode]) {
        this.defsLoaded();
        return;
      }

      //else check for def on the server
      var url = Proj4js.getScriptLocation() + 'defs/' + this.srsAuth.toUpperCase() + this.srsProjNumber + '.js';
      Proj4js.loadScript(url, 
                Proj4js.bind(this.defsLoaded, this),
                Proj4js.bind(this.loadFromService, this),
                Proj4js.bind(this.checkDefsLoaded, this) );
    },

/**
 * Function: loadFromService
 *    Creates the REST URL for loading the definition from a web service and 
 *    loads it.
 *
 */
    loadFromService: function() {
      //else load from web service
      var url = Proj4js.defsLookupService +'/' + this.srsAuth +'/'+ this.srsProjNumber + '/proj4js/';
      Proj4js.loadScript(url, 
            Proj4js.bind(this.defsLoaded, this),
            Proj4js.bind(this.defsFailed, this),
            Proj4js.bind(this.checkDefsLoaded, this) );
    },

/**
 * Function: defsLoaded
 * Continues the Proj object initilization once the def file is loaded
 *
 */
    defsLoaded: function() {
      this.parseDefs();
      this.loadProjCode(this.projName);
    },
    
/**
 * Function: checkDefsLoaded
 *    This is the loadCheck method to see if the def object exists
 *
 */
    checkDefsLoaded: function() {
      if (Proj4js.defs[this.srsCode]) {
        return true;
      } else {
        return false;
      }
    },

 /**
 * Function: defsFailed
 *    Report an error in loading the defs file, but continue on using WGS84
 *
 */
   defsFailed: function() {
      Proj4js.reportError('failed to load projection definition for: '+this.srsCode);
      Proj4js.defs[this.srsCode] = Proj4js.defs['WGS84'];  //set it to something so it can at least continue
      this.defsLoaded();
    },

/**
 * Function: loadProjCode
 *    Loads projection class code dynamically if required.
 *     Projection code may be included either through a script tag or in
 *     a built version of proj4js
 *
 */
    loadProjCode: function(projName) {
      if (Proj4js.Proj[projName]) {
        this.initTransforms();
        return;
      }

      //the URL for the projection code
      var url = Proj4js.getScriptLocation() + 'projCode/' + projName + '.js';
      Proj4js.loadScript(url, 
              Proj4js.bind(this.loadProjCodeSuccess, this, projName),
              Proj4js.bind(this.loadProjCodeFailure, this, projName), 
              Proj4js.bind(this.checkCodeLoaded, this, projName) );
    },

 /**
 * Function: loadProjCodeSuccess
 *    Loads any proj dependencies or continue on to final initialization.
 *
 */
    loadProjCodeSuccess: function(projName) {
      if (Proj4js.Proj[projName].dependsOn){
        this.loadProjCode(Proj4js.Proj[projName].dependsOn);
      } else {
        this.initTransforms();
      }
    },

 /**
 * Function: defsFailed
 *    Report an error in loading the proj file.  Initialization of the Proj
 *    object has failed and the readyToUse flag will never be set.
 *
 */
    loadProjCodeFailure: function(projName) {
      Proj4js.reportError("failed to find projection file for: " + projName);
      //TBD initialize with identity transforms so proj will still work?
    },
    
/**
 * Function: checkCodeLoaded
 *    This is the loadCheck method to see if the projection code is loaded
 *
 */
    checkCodeLoaded: function(projName) {
      if (Proj4js.Proj[projName]) {
        return true;
      } else {
        return false;
      }
    },

/**
 * Function: initTransforms
 *    Finalize the initialization of the Proj object
 *
 */
    initTransforms: function() {
      Proj4js.extend(this, Proj4js.Proj[this.projName]);
      this.init();
      this.readyToUse = true;
  },

/**
 * Function: parseDefs
 * Parses the PROJ.4 initialization string and sets the associated properties.
 *
 */
  parseDefs: function() {
      this.defData = Proj4js.defs[this.srsCode];
      var paramName, paramVal;
      if (!this.defData) {
        return;
      }
      var paramArray=this.defData.split("+");

      for (var prop=0; prop<paramArray.length; prop++) {
          var property = paramArray[prop].split("=");
          paramName = property[0].toLowerCase();
          paramVal = property[1];

          switch (paramName.replace(/\s/gi,"")) {  // trim out spaces
              case "": break;   // throw away nameless parameter
              case "title":  this.title = paramVal; break;
              case "proj":   this.projName =  paramVal.replace(/\s/gi,""); break;
              case "units":  this.units = paramVal.replace(/\s/gi,""); break;
              case "datum":  this.datumCode = paramVal.replace(/\s/gi,""); break;
              case "nadgrids": this.nagrids = paramVal.replace(/\s/gi,""); break;
              case "ellps":  this.ellps = paramVal.replace(/\s/gi,""); break;
              case "a":      this.a =  parseFloat(paramVal); break;  // semi-major radius
              case "b":      this.b =  parseFloat(paramVal); break;  // semi-minor radius
              // DGR 2007-11-20
              case "rf":     this.rf = parseFloat(paramVal); break; // inverse flattening rf= a/(a-b)
              case "lat_0":  this.lat0 = paramVal*Proj4js.common.D2R; break;        // phi0, central latitude
              case "lat_1":  this.lat1 = paramVal*Proj4js.common.D2R; break;        //standard parallel 1
              case "lat_2":  this.lat2 = paramVal*Proj4js.common.D2R; break;        //standard parallel 2
              case "lat_ts": this.lat_ts = paramVal*Proj4js.common.D2R; break;      // used in merc and eqc
              case "lon_0":  this.long0 = paramVal*Proj4js.common.D2R; break;       // lam0, central longitude
              case "alpha":  this.alpha =  parseFloat(paramVal)*Proj4js.common.D2R; break;  //for somerc projection
              case "lonc":   this.longc = paramVal*Proj4js.common.D2R; break;       //for somerc projection
              case "x_0":    this.x0 = parseFloat(paramVal); break;  // false easting
              case "y_0":    this.y0 = parseFloat(paramVal); break;  // false northing
              case "k_0":    this.k0 = parseFloat(paramVal); break;  // projection scale factor
              case "k":      this.k0 = parseFloat(paramVal); break;  // both forms returned
              case "r_a":    this.R_A = true; break;                 // sphere--area of ellipsoid
              case "zone":   this.zone = parseInt(paramVal); break;  // UTM Zone
              case "south":   this.utmSouth = true; break;  // UTM north/south
              case "towgs84":this.datum_params = paramVal.split(","); break;
              case "to_meter": this.to_meter = parseFloat(paramVal); break; // cartesian scaling
              case "from_greenwich": this.from_greenwich = paramVal*Proj4js.common.D2R; break;
              // DGR 2008-07-09 : if pm is not a well-known prime meridian take
              // the value instead of 0.0, then convert to radians
              case "pm":     paramVal = paramVal.replace(/\s/gi,"");
                             this.from_greenwich = Proj4js.PrimeMeridian[paramVal] ?
                                Proj4js.PrimeMeridian[paramVal] : parseFloat(paramVal);
                             this.from_greenwich *= Proj4js.common.D2R; 
                             break;
              case "no_defs": break; 
              default: //alert("Unrecognized parameter: " + paramName);
          } // switch()
      } // for paramArray
      this.deriveConstants();
  },

/**
 * Function: deriveConstants
 * Sets several derived constant values and initialization of datum and ellipse
 *     parameters.
 *
 */
  deriveConstants: function() {
      if (this.nagrids == '@null') this.datumCode = 'none';
      if (this.datumCode && this.datumCode != 'none') {
        var datumDef = Proj4js.Datum[this.datumCode];
        if (datumDef) {
          this.datum_params = datumDef.towgs84 ? datumDef.towgs84.split(',') : null;
          this.ellps = datumDef.ellipse;
          this.datumName = datumDef.datumName ? datumDef.datumName : this.datumCode;
        }
      }
      if (!this.a) {    // do we have an ellipsoid?
          var ellipse = Proj4js.Ellipsoid[this.ellps] ? Proj4js.Ellipsoid[this.ellps] : Proj4js.Ellipsoid['WGS84'];
          Proj4js.extend(this, ellipse);
      }
      if (this.rf && !this.b) this.b = (1.0 - 1.0/this.rf) * this.a;
      if (Math.abs(this.a - this.b)<Proj4js.common.EPSLN) {
        this.sphere = true;
        this.b= this.a;
      }
      this.a2 = this.a * this.a;          // used in geocentric
      this.b2 = this.b * this.b;          // used in geocentric
      this.es = (this.a2-this.b2)/this.a2;  // e ^ 2
      this.e = Math.sqrt(this.es);        // eccentricity
      if (this.R_A) {
        this.a *= 1. - this.es * (Proj4js.common.SIXTH + this.es * (Proj4js.common.RA4 + this.es * Proj4js.common.RA6));
        this.a2 = this.a * this.a;
        this.b2 = this.b * this.b;
        this.es = 0.;
      }
      this.ep2=(this.a2-this.b2)/this.b2; // used in geocentric
      if (!this.k0) this.k0 = 1.0;    //default value

      this.datum = new Proj4js.datum(this);
  }
});

Proj4js.Proj.longlat = {
  init: function() {
    //no-op for longlat
  },
  forward: function(pt) {
    //identity transform
    return pt;
  },
  inverse: function(pt) {
    //identity transform
    return pt;
  }
};

/**
  Proj4js.defs is a collection of coordinate system definition objects in the 
  PROJ.4 command line format.
  Generally a def is added by means of a separate .js file for example:

    <SCRIPT type="text/javascript" src="defs/EPSG26912.js"></SCRIPT>

  def is a CS definition in PROJ.4 WKT format, for example:
    +proj="tmerc"   //longlat, etc.
    +a=majorRadius
    +b=minorRadius
    +lat0=somenumber
    +long=somenumber
*/
Proj4js.defs = {
  // These are so widely used, we'll go ahead and throw them in
  // without requiring a separate .js file
  'WGS84': "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees",
  'EPSG:4326': "+title=long/lat:WGS84 +proj=longlat +a=6378137.0 +b=6356752.31424518 +ellps=WGS84 +datum=WGS84 +units=degrees",
  'EPSG:4269': "+title=long/lat:NAD83 +proj=longlat +a=6378137.0 +b=6356752.31414036 +ellps=GRS80 +datum=NAD83 +units=degrees",
  'EPSG:3785': "+title= Google Mercator +proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs"
};
Proj4js.defs['GOOGLE'] = Proj4js.defs['EPSG:3785'];
Proj4js.defs['EPSG:900913'] = Proj4js.defs['EPSG:3785'];
Proj4js.defs['EPSG:102113'] = Proj4js.defs['EPSG:3785'];

Proj4js.common = {
  PI : 3.141592653589793238, //Math.PI,
  HALF_PI : 1.570796326794896619, //Math.PI*0.5,
  TWO_PI : 6.283185307179586477, //Math.PI*2,
  FORTPI : 0.78539816339744833,
  R2D : 57.29577951308232088,
  D2R : 0.01745329251994329577,
  SEC_TO_RAD : 4.84813681109535993589914102357e-6, /* SEC_TO_RAD = Pi/180/3600 */
  EPSLN : 1.0e-10,
  MAX_ITER : 20,
  // following constants from geocent.c
  COS_67P5 : 0.38268343236508977,  /* cosine of 67.5 degrees */
  AD_C : 1.0026000,                /* Toms region 1 constant */

  /* datum_type values */
  PJD_UNKNOWN  : 0,
  PJD_3PARAM   : 1,
  PJD_7PARAM   : 2,
  PJD_GRIDSHIFT: 3,
  PJD_WGS84    : 4,   // WGS84 or equivalent
  PJD_NODATUM  : 5,   // WGS84 or equivalent
  SRS_WGS84_SEMIMAJOR : 6378137.0,  // only used in grid shift transforms

  // ellipoid pj_set_ell.c
  SIXTH : .1666666666666666667, /* 1/6 */
  RA4   : .04722222222222222222, /* 17/360 */
  RA6   : .02215608465608465608, /* 67/3024 */
  RV4   : .06944444444444444444, /* 5/72 */
  RV6   : .04243827160493827160, /* 55/1296 */

// Function to compute the constant small m which is the radius of
//   a parallel of latitude, phi, divided by the semimajor axis.
// -----------------------------------------------------------------
  msfnz : function(eccent, sinphi, cosphi) {
      var con = eccent * sinphi;
      return cosphi/(Math.sqrt(1.0 - con * con));
  },

// Function to compute the constant small t for use in the forward
//   computations in the Lambert Conformal Conic and the Polar
//   Stereographic projections.
// -----------------------------------------------------------------
  tsfnz : function(eccent, phi, sinphi) {
    var con = eccent * sinphi;
    var com = .5 * eccent;
    con = Math.pow(((1.0 - con) / (1.0 + con)), com);
    return (Math.tan(.5 * (this.HALF_PI - phi))/con);
  },

// Function to compute the latitude angle, phi2, for the inverse of the
//   Lambert Conformal Conic and Polar Stereographic projections.
// ----------------------------------------------------------------
  phi2z : function(eccent, ts) {
    var eccnth = .5 * eccent;
    var con, dphi;
    var phi = this.HALF_PI - 2 * Math.atan(ts);
    for (i = 0; i <= 15; i++) {
      con = eccent * Math.sin(phi);
      dphi = this.HALF_PI - 2 * Math.atan(ts *(Math.pow(((1.0 - con)/(1.0 + con)),eccnth))) - phi;
      phi += dphi;
      if (Math.abs(dphi) <= .0000000001) return phi;
    }
    alert("phi2z has NoConvergence");
    return (-9999);
  },

/* Function to compute constant small q which is the radius of a 
   parallel of latitude, phi, divided by the semimajor axis. 
------------------------------------------------------------*/
  qsfnz : function(eccent,sinphi) {
    var con;
    if (eccent > 1.0e-7) {
      con = eccent * sinphi;
      return (( 1.0- eccent * eccent) * (sinphi /(1.0 - con * con) - (.5/eccent)*Math.log((1.0 - con)/(1.0 + con))));
    } else {
      return(2.0 * sinphi);
    }
  },

/* Function to eliminate roundoff errors in asin
----------------------------------------------*/
  asinz : function(x) {
    if (Math.abs(x)>1.0) {
      x=(x>1.0)?1.0:-1.0;
    }
    return Math.asin(x);
  },

// following functions from gctpc cproj.c for transverse mercator projections
  e0fn : function(x) {return(1.0-0.25*x*(1.0+x/16.0*(3.0+1.25*x)));},
  e1fn : function(x) {return(0.375*x*(1.0+0.25*x*(1.0+0.46875*x)));},
  e2fn : function(x) {return(0.05859375*x*x*(1.0+0.75*x));},
  e3fn : function(x) {return(x*x*x*(35.0/3072.0));},
  mlfn : function(e0,e1,e2,e3,phi) {return(e0*phi-e1*Math.sin(2.0*phi)+e2*Math.sin(4.0*phi)-e3*Math.sin(6.0*phi));},

  srat : function(esinp, exp) {
    return(Math.pow((1.0-esinp)/(1.0+esinp), exp));
  },

// Function to return the sign of an argument
  sign : function(x) { if (x < 0.0) return(-1); else return(1);},

// Function to adjust longitude to -180 to 180; input in radians
  adjust_lon : function(x) {
    x = (Math.abs(x) < this.PI) ? x: (x - (this.sign(x)*this.TWO_PI) );
    return x;
  },

// IGNF - DGR : algorithms used by IGN France

// Function to adjust latitude to -90 to 90; input in radians
  adjust_lat : function(x) {
    x= (Math.abs(x) < this.HALF_PI) ? x: (x - (this.sign(x)*this.PI) );
    return x;
  },

// Latitude Isometrique - close to tsfnz ...
  latiso : function(eccent, phi, sinphi) {
    if (Math.abs(phi) > this.HALF_PI) return +Number.NaN;
    if (phi==this.HALF_PI) return Number.POSITIVE_INFINITY;
    if (phi==-1.0*this.HALF_PI) return -1.0*Number.POSITIVE_INFINITY;

    var con= eccent*sinphi;
    return Math.log(Math.tan((this.HALF_PI+phi)/2.0))+eccent*Math.log((1.0-con)/(1.0+con))/2.0;
  },

  fL : function(x,L) {
    return 2.0*Math.atan(x*Math.exp(L)) - this.HALF_PI;
  },

// Inverse Latitude Isometrique - close to ph2z
  invlatiso : function(eccent, ts) {
    var phi= this.fL(1.0,ts);
    var Iphi= 0.0;
    var con= 0.0;
    do {
      Iphi= phi;
      con= eccent*Math.sin(Iphi);
      phi= this.fL(Math.exp(eccent*Math.log((1.0+con)/(1.0-con))/2.0),ts)
    } while (Math.abs(phi-Iphi)>1.0e-12);
    return phi;
  },

// Needed for Gauss Schreiber
// Original:  Denis Makarov (info@binarythings.com)
// Web Site:  http://www.binarythings.com
  sinh : function(x)
  {
    var r= Math.exp(x);
    r= (r-1.0/r)/2.0;
    return r;
  },

  cosh : function(x)
  {
    var r= Math.exp(x);
    r= (r+1.0/r)/2.0;
    return r;
  },

  tanh : function(x)
  {
    var r= Math.exp(x);
    r= (r-1.0/r)/(r+1.0/r);
    return r;
  },

  asinh : function(x)
  {
    var s= (x>= 0? 1.0:-1.0);
    return s*(Math.log( Math.abs(x) + Math.sqrt(x*x+1.0) ));
  },

  acosh : function(x)
  {
    return 2.0*Math.log(Math.sqrt((x+1.0)/2.0) + Math.sqrt((x-1.0)/2.0));
  },

  atanh : function(x)
  {
    return Math.log((x-1.0)/(x+1.0))/2.0;
  },

// Grande Normale
  gN : function(a,e,sinphi)
  {
    var temp= e*sinphi;
    return a/Math.sqrt(1.0 - temp*temp);
  }

};

/** datum object
*/
Proj4js.datum = Proj4js.Class({

  initialize : function(proj) {
    this.datum_type = Proj4js.common.PJD_WGS84;   //default setting
    if (proj.datumCode && proj.datumCode == 'none') {
      this.datum_type = Proj4js.common.PJD_NODATUM;
    }
    if (proj && proj.datum_params) {
      for (var i=0; i<proj.datum_params.length; i++) {
        proj.datum_params[i]=parseFloat(proj.datum_params[i]);
      }
      if (proj.datum_params[0] != 0 || proj.datum_params[1] != 0 || proj.datum_params[2] != 0 ) {
        this.datum_type = Proj4js.common.PJD_3PARAM;
      }
      if (proj.datum_params.length > 3) {
        if (proj.datum_params[3] != 0 || proj.datum_params[4] != 0 ||
            proj.datum_params[5] != 0 || proj.datum_params[6] != 0 ) {
          this.datum_type = Proj4js.common.PJD_7PARAM;
          proj.datum_params[3] *= Proj4js.common.SEC_TO_RAD;
          proj.datum_params[4] *= Proj4js.common.SEC_TO_RAD;
          proj.datum_params[5] *= Proj4js.common.SEC_TO_RAD;
          proj.datum_params[6] = (proj.datum_params[6]/1000000.0) + 1.0;
        }
      }
    }
    if (proj) {
      this.a = proj.a;    //datum object also uses these values
      this.b = proj.b;
      this.es = proj.es;
      this.ep2 = proj.ep2;
      this.datum_params = proj.datum_params;
    }
  },

  /****************************************************************/
  // cs_compare_datums()
  //   Returns 1 (TRUE) if the two datums match, otherwise 0 (FALSE).
  compare_datums : function( dest ) {
    if( this.datum_type != dest.datum_type ) {
      return false; // false, datums are not equal
    } else if( this.a != dest.a || Math.abs(this.es-dest.es) > 0.000000000050 ) {
      // the tolerence for es is to ensure that GRS80 and WGS84
      // are considered identical
      return false;
    } else if( this.datum_type == Proj4js.common.PJD_3PARAM ) {
      return (this.datum_params[0] == dest.datum_params[0]
              && this.datum_params[1] == dest.datum_params[1]
              && this.datum_params[2] == dest.datum_params[2]);
    } else if( this.datum_type == Proj4js.common.PJD_7PARAM ) {
      return (this.datum_params[0] == dest.datum_params[0]
              && this.datum_params[1] == dest.datum_params[1]
              && this.datum_params[2] == dest.datum_params[2]
              && this.datum_params[3] == dest.datum_params[3]
              && this.datum_params[4] == dest.datum_params[4]
              && this.datum_params[5] == dest.datum_params[5]
              && this.datum_params[6] == dest.datum_params[6]);
    } else if( this.datum_type == Proj4js.common.PJD_GRIDSHIFT ) {
      return strcmp( pj_param(this.params,"snadgrids").s,
                     pj_param(dest.params,"snadgrids").s ) == 0;
    } else {
      return true; // datums are equal
    }
  }, // cs_compare_datums()

  /*
   * The function Convert_Geodetic_To_Geocentric converts geodetic coordinates
   * (latitude, longitude, and height) to geocentric coordinates (X, Y, Z),
   * according to the current ellipsoid parameters.
   *
   *    Latitude  : Geodetic latitude in radians                     (input)
   *    Longitude : Geodetic longitude in radians                    (input)
   *    Height    : Geodetic height, in meters                       (input)
   *    X         : Calculated Geocentric X coordinate, in meters    (output)
   *    Y         : Calculated Geocentric Y coordinate, in meters    (output)
   *    Z         : Calculated Geocentric Z coordinate, in meters    (output)
   *
   */
  geodetic_to_geocentric : function(p) {
    var Longitude = p.x;
    var Latitude = p.y;
    var Height = p.z ? p.z : 0;   //Z value not always supplied
    var X;  // output
    var Y;
    var Z;

    var Error_Code=0;  //  GEOCENT_NO_ERROR;
    var Rn;            /*  Earth radius at location  */
    var Sin_Lat;       /*  Math.sin(Latitude)  */
    var Sin2_Lat;      /*  Square of Math.sin(Latitude)  */
    var Cos_Lat;       /*  Math.cos(Latitude)  */

    /*
    ** Don't blow up if Latitude is just a little out of the value
    ** range as it may just be a rounding issue.  Also removed longitude
    ** test, it should be wrapped by Math.cos() and Math.sin().  NFW for PROJ.4, Sep/2001.
    */
    if( Latitude < -Proj4js.common.HALF_PI && Latitude > -1.001 * Proj4js.common.HALF_PI ) {
        Latitude = -Proj4js.common.HALF_PI;
    } else if( Latitude > Proj4js.common.HALF_PI && Latitude < 1.001 * Proj4js.common.HALF_PI ) {
        Latitude = Proj4js.common.HALF_PI;
    } else if ((Latitude < -Proj4js.common.HALF_PI) || (Latitude > Proj4js.common.HALF_PI)) {
      /* Latitude out of range */
      Proj4js.reportError('geocent:lat out of range:'+Latitude);
      return null;
    }

    if (Longitude > Proj4js.common.PI) Longitude -= (2*Proj4js.common.PI);
    Sin_Lat = Math.sin(Latitude);
    Cos_Lat = Math.cos(Latitude);
    Sin2_Lat = Sin_Lat * Sin_Lat;
    Rn = this.a / (Math.sqrt(1.0e0 - this.es * Sin2_Lat));
    X = (Rn + Height) * Cos_Lat * Math.cos(Longitude);
    Y = (Rn + Height) * Cos_Lat * Math.sin(Longitude);
    Z = ((Rn * (1 - this.es)) + Height) * Sin_Lat;

    p.x = X;
    p.y = Y;
    p.z = Z;
    return Error_Code;
  }, // cs_geodetic_to_geocentric()


  geocentric_to_geodetic : function (p) {
/* local defintions and variables */
/* end-criterium of loop, accuracy of sin(Latitude) */
var genau = 1.E-12;
var genau2 = (genau*genau);
var maxiter = 30;

    var P;        /* distance between semi-minor axis and location */
    var RR;       /* distance between center and location */
    var CT;       /* sin of geocentric latitude */
    var ST;       /* cos of geocentric latitude */
    var RX;
    var RK;
    var RN;       /* Earth radius at location */
    var CPHI0;    /* cos of start or old geodetic latitude in iterations */
    var SPHI0;    /* sin of start or old geodetic latitude in iterations */
    var CPHI;     /* cos of searched geodetic latitude */
    var SPHI;     /* sin of searched geodetic latitude */
    var SDPHI;    /* end-criterium: addition-theorem of sin(Latitude(iter)-Latitude(iter-1)) */
    var At_Pole;     /* indicates location is in polar region */
    var iter;        /* # of continous iteration, max. 30 is always enough (s.a.) */

    var X = p.x;
    var Y = p.y;
    var Z = p.z ? p.z : 0.0;   //Z value not always supplied
    var Longitude;
    var Latitude;
    var Height;

    At_Pole = false;
    P = Math.sqrt(X*X+Y*Y);
    RR = Math.sqrt(X*X+Y*Y+Z*Z);

/*      special cases for latitude and longitude */
    if (P/this.a < genau) {

/*  special case, if P=0. (X=0., Y=0.) */
        At_Pole = true;
        Longitude = 0.0;

/*  if (X,Y,Z)=(0.,0.,0.) then Height becomes semi-minor axis
 *  of ellipsoid (=center of mass), Latitude becomes PI/2 */
        if (RR/this.a < genau) {
            Latitude = Proj4js.common.HALF_PI;
            Height   = -this.b;
            return;
        }
    } else {
/*  ellipsoidal (geodetic) longitude
 *  interval: -PI < Longitude <= +PI */
        Longitude=Math.atan2(Y,X);
    }

/* --------------------------------------------------------------
 * Following iterative algorithm was developped by
 * "Institut f�r Erdmessung", University of Hannover, July 1988.
 * Internet: www.ife.uni-hannover.de
 * Iterative computation of CPHI,SPHI and Height.
 * Iteration of CPHI and SPHI to 10**-12 radian resp.
 * 2*10**-7 arcsec.
 * --------------------------------------------------------------
 */
    CT = Z/RR;
    ST = P/RR;
    RX = 1.0/Math.sqrt(1.0-this.es*(2.0-this.es)*ST*ST);
    CPHI0 = ST*(1.0-this.es)*RX;
    SPHI0 = CT*RX;
    iter = 0;

/* loop to find sin(Latitude) resp. Latitude
 * until |sin(Latitude(iter)-Latitude(iter-1))| < genau */
    do
    {
        iter++;
        RN = this.a/Math.sqrt(1.0-this.es*SPHI0*SPHI0);

/*  ellipsoidal (geodetic) height */
        Height = P*CPHI0+Z*SPHI0-RN*(1.0-this.es*SPHI0*SPHI0);

        RK = this.es*RN/(RN+Height);
        RX = 1.0/Math.sqrt(1.0-RK*(2.0-RK)*ST*ST);
        CPHI = ST*(1.0-RK)*RX;
        SPHI = CT*RX;
        SDPHI = SPHI*CPHI0-CPHI*SPHI0;
        CPHI0 = CPHI;
        SPHI0 = SPHI;
    }
    while (SDPHI*SDPHI > genau2 && iter < maxiter);

/*      ellipsoidal (geodetic) latitude */
    Latitude=Math.atan(SPHI/Math.abs(CPHI));

    p.x = Longitude;
    p.y = Latitude;
    p.z = Height;
    return p;
  }, // cs_geocentric_to_geodetic()

  /** Convert_Geocentric_To_Geodetic
   * The method used here is derived from 'An Improved Algorithm for
   * Geocentric to Geodetic Coordinate Conversion', by Ralph Toms, Feb 1996
   */
  geocentric_to_geodetic_noniter : function (p) {
    var X = p.x;
    var Y = p.y;
    var Z = p.z ? p.z : 0;   //Z value not always supplied
    var Longitude;
    var Latitude;
    var Height;

    var W;        /* distance from Z axis */
    var W2;       /* square of distance from Z axis */
    var T0;       /* initial estimate of vertical component */
    var T1;       /* corrected estimate of vertical component */
    var S0;       /* initial estimate of horizontal component */
    var S1;       /* corrected estimate of horizontal component */
    var Sin_B0;   /* Math.sin(B0), B0 is estimate of Bowring aux variable */
    var Sin3_B0;  /* cube of Math.sin(B0) */
    var Cos_B0;   /* Math.cos(B0) */
    var Sin_p1;   /* Math.sin(phi1), phi1 is estimated latitude */
    var Cos_p1;   /* Math.cos(phi1) */
    var Rn;       /* Earth radius at location */
    var Sum;      /* numerator of Math.cos(phi1) */
    var At_Pole;  /* indicates location is in polar region */

    X = parseFloat(X);  // cast from string to float
    Y = parseFloat(Y);
    Z = parseFloat(Z);

    At_Pole = false;
    if (X != 0.0)
    {
        Longitude = Math.atan2(Y,X);
    }
    else
    {
        if (Y > 0)
        {
            Longitude = Proj4js.common.HALF_PI;
        }
        else if (Y < 0)
        {
            Longitude = -Proj4js.common.HALF_PI;
        }
        else
        {
            At_Pole = true;
            Longitude = 0.0;
            if (Z > 0.0)
            {  /* north pole */
                Latitude = Proj4js.common.HALF_PI;
            }
            else if (Z < 0.0)
            {  /* south pole */
                Latitude = -Proj4js.common.HALF_PI;
            }
            else
            {  /* center of earth */
                Latitude = Proj4js.common.HALF_PI;
                Height = -this.b;
                return;
            }
        }
    }
    W2 = X*X + Y*Y;
    W = Math.sqrt(W2);
    T0 = Z * Proj4js.common.AD_C;
    S0 = Math.sqrt(T0 * T0 + W2);
    Sin_B0 = T0 / S0;
    Cos_B0 = W / S0;
    Sin3_B0 = Sin_B0 * Sin_B0 * Sin_B0;
    T1 = Z + this.b * this.ep2 * Sin3_B0;
    Sum = W - this.a * this.es * Cos_B0 * Cos_B0 * Cos_B0;
    S1 = Math.sqrt(T1*T1 + Sum * Sum);
    Sin_p1 = T1 / S1;
    Cos_p1 = Sum / S1;
    Rn = this.a / Math.sqrt(1.0 - this.es * Sin_p1 * Sin_p1);
    if (Cos_p1 >= Proj4js.common.COS_67P5)
    {
        Height = W / Cos_p1 - Rn;
    }
    else if (Cos_p1 <= -Proj4js.common.COS_67P5)
    {
        Height = W / -Cos_p1 - Rn;
    }
    else
    {
        Height = Z / Sin_p1 + Rn * (this.es - 1.0);
    }
    if (At_Pole == false)
    {
        Latitude = Math.atan(Sin_p1 / Cos_p1);
    }

    p.x = Longitude;
    p.y = Latitude;
    p.z = Height;
    return p;
  }, // geocentric_to_geodetic_noniter()

  /****************************************************************/
  // pj_geocentic_to_wgs84( p )
  //  p = point to transform in geocentric coordinates (x,y,z)
  geocentric_to_wgs84 : function ( p ) {

    if( this.datum_type == Proj4js.common.PJD_3PARAM )
    {
      // if( x[io] == HUGE_VAL )
      //    continue;
      p.x += this.datum_params[0];
      p.y += this.datum_params[1];
      p.z += this.datum_params[2];

    }
    else if (this.datum_type == Proj4js.common.PJD_7PARAM)
    {
      var Dx_BF =this.datum_params[0];
      var Dy_BF =this.datum_params[1];
      var Dz_BF =this.datum_params[2];
      var Rx_BF =this.datum_params[3];
      var Ry_BF =this.datum_params[4];
      var Rz_BF =this.datum_params[5];
      var M_BF  =this.datum_params[6];
      // if( x[io] == HUGE_VAL )
      //    continue;
      var x_out = M_BF*(       p.x - Rz_BF*p.y + Ry_BF*p.z) + Dx_BF;
      var y_out = M_BF*( Rz_BF*p.x +       p.y - Rx_BF*p.z) + Dy_BF;
      var z_out = M_BF*(-Ry_BF*p.x + Rx_BF*p.y +       p.z) + Dz_BF;
      p.x = x_out;
      p.y = y_out;
      p.z = z_out;
    }
  }, // cs_geocentric_to_wgs84

  /****************************************************************/
  // pj_geocentic_from_wgs84()
  //  coordinate system definition,
  //  point to transform in geocentric coordinates (x,y,z)
  geocentric_from_wgs84 : function( p ) {

    if( this.datum_type == Proj4js.common.PJD_3PARAM )
    {
      //if( x[io] == HUGE_VAL )
      //    continue;
      p.x -= this.datum_params[0];
      p.y -= this.datum_params[1];
      p.z -= this.datum_params[2];

    }
    else if (this.datum_type == Proj4js.common.PJD_7PARAM)
    {
      var Dx_BF =this.datum_params[0];
      var Dy_BF =this.datum_params[1];
      var Dz_BF =this.datum_params[2];
      var Rx_BF =this.datum_params[3];
      var Ry_BF =this.datum_params[4];
      var Rz_BF =this.datum_params[5];
      var M_BF  =this.datum_params[6];
      var x_tmp = (p.x - Dx_BF) / M_BF;
      var y_tmp = (p.y - Dy_BF) / M_BF;
      var z_tmp = (p.z - Dz_BF) / M_BF;
      //if( x[io] == HUGE_VAL )
      //    continue;

      p.x =        x_tmp + Rz_BF*y_tmp - Ry_BF*z_tmp;
      p.y = -Rz_BF*x_tmp +       y_tmp + Rx_BF*z_tmp;
      p.z =  Ry_BF*x_tmp - Rx_BF*y_tmp +       z_tmp;
    } //cs_geocentric_from_wgs84()
  }
});

/** point object, nothing fancy, just allows values to be
    passed back and forth by reference rather than by value.
    Other point classes may be used as long as they have
    x and y properties, which will get modified in the transform method.
*/
Proj4js.Point = Proj4js.Class({

    /**
     * Constructor: Proj4js.Point
     *
     * Parameters:
     * - x {float} or {Array} either the first coordinates component or
     *     the full coordinates
     * - y {float} the second component
     * - z {float} the third component, optional.
     */
    initialize : function(x,y,z) {
      if (typeof x == 'object') {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2] || 0.0;
      } else if (typeof x == 'string') {
        var coords = x.split(',');
        this.x = parseFloat(coords[0]);
        this.y = parseFloat(coords[1]);
        this.z = parseFloat(coords[2]) || 0.0;
      } else {
        this.x = x;
        this.y = y;
        this.z = z || 0.0;
      }
    },

    /**
     * APIMethod: clone
     * Build a copy of a Proj4js.Point object.
     *
     * Return:
     * {Proj4js}.Point the cloned point.
     */
    clone : function() {
      return new Proj4js.Point(this.x, this.y, this.z);
    },

    /**
     * APIMethod: toString
     * Return a readable string version of the point
     *
     * Return:
     * {String} String representation of Proj4js.Point object. 
     *           (ex. <i>"x=5,y=42"</i>)
     */
    toString : function() {
        return ("x=" + this.x + ",y=" + this.y);
    },

    /** 
     * APIMethod: toShortString
     * Return a short string version of the point.
     *
     * Return:
     * {String} Shortened String representation of Proj4js.Point object. 
     *         (ex. <i>"5, 42"</i>)
     */
    toShortString : function() {
        return (this.x + ", " + this.y);
    }
});

Proj4js.PrimeMeridian = {
    "greenwich": 0.0,               //"0dE",
    "lisbon":     -9.131906111111,   //"9d07'54.862\"W",
    "paris":       2.337229166667,   //"2d20'14.025\"E",
    "bogota":    -74.080916666667,  //"74d04'51.3\"W",
    "madrid":     -3.687938888889,  //"3d41'16.58\"W",
    "rome":       12.452333333333,  //"12d27'8.4\"E",
    "bern":        7.439583333333,  //"7d26'22.5\"E",
    "jakarta":   106.807719444444,  //"106d48'27.79\"E",
    "ferro":     -17.666666666667,  //"17d40'W",
    "brussels":    4.367975,        //"4d22'4.71\"E",
    "stockholm":  18.058277777778,  //"18d3'29.8\"E",
    "athens":     23.7163375,       //"23d42'58.815\"E",
    "oslo":       10.722916666667   //"10d43'22.5\"E"
};

Proj4js.Ellipsoid = {
  "MERIT": {a:6378137.0, rf:298.257, ellipseName:"MERIT 1983"},
  "SGS85": {a:6378136.0, rf:298.257, ellipseName:"Soviet Geodetic System 85"},
  "GRS80": {a:6378137.0, rf:298.257222101, ellipseName:"GRS 1980(IUGG, 1980)"},
  "IAU76": {a:6378140.0, rf:298.257, ellipseName:"IAU 1976"},
  "airy": {a:6377563.396, b:6356256.910, ellipseName:"Airy 1830"},
  "APL4.": {a:6378137, rf:298.25, ellipseName:"Appl. Physics. 1965"},
  "NWL9D": {a:6378145.0, rf:298.25, ellipseName:"Naval Weapons Lab., 1965"},
  "mod_airy": {a:6377340.189, b:6356034.446, ellipseName:"Modified Airy"},
  "andrae": {a:6377104.43, rf:300.0, ellipseName:"Andrae 1876 (Den., Iclnd.)"},
  "aust_SA": {a:6378160.0, rf:298.25, ellipseName:"Australian Natl & S. Amer. 1969"},
  "GRS67": {a:6378160.0, rf:298.2471674270, ellipseName:"GRS 67(IUGG 1967)"},
  "bessel": {a:6377397.155, rf:299.1528128, ellipseName:"Bessel 1841"},
  "bess_nam": {a:6377483.865, rf:299.1528128, ellipseName:"Bessel 1841 (Namibia)"},
  "clrk66": {a:6378206.4, b:6356583.8, ellipseName:"Clarke 1866"},
  "clrk80": {a:6378249.145, rf:293.4663, ellipseName:"Clarke 1880 mod."},
  "CPM": {a:6375738.7, rf:334.29, ellipseName:"Comm. des Poids et Mesures 1799"},
  "delmbr": {a:6376428.0, rf:311.5, ellipseName:"Delambre 1810 (Belgium)"},
  "engelis": {a:6378136.05, rf:298.2566, ellipseName:"Engelis 1985"},
  "evrst30": {a:6377276.345, rf:300.8017, ellipseName:"Everest 1830"},
  "evrst48": {a:6377304.063, rf:300.8017, ellipseName:"Everest 1948"},
  "evrst56": {a:6377301.243, rf:300.8017, ellipseName:"Everest 1956"},
  "evrst69": {a:6377295.664, rf:300.8017, ellipseName:"Everest 1969"},
  "evrstSS": {a:6377298.556, rf:300.8017, ellipseName:"Everest (Sabah & Sarawak)"},
  "fschr60": {a:6378166.0, rf:298.3, ellipseName:"Fischer (Mercury Datum) 1960"},
  "fschr60m": {a:6378155.0, rf:298.3, ellipseName:"Fischer 1960"},
  "fschr68": {a:6378150.0, rf:298.3, ellipseName:"Fischer 1968"},
  "helmert": {a:6378200.0, rf:298.3, ellipseName:"Helmert 1906"},
  "hough": {a:6378270.0, rf:297.0, ellipseName:"Hough"},
  "intl": {a:6378388.0, rf:297.0, ellipseName:"International 1909 (Hayford)"},
  "kaula": {a:6378163.0, rf:298.24, ellipseName:"Kaula 1961"},
  "lerch": {a:6378139.0, rf:298.257, ellipseName:"Lerch 1979"},
  "mprts": {a:6397300.0, rf:191.0, ellipseName:"Maupertius 1738"},
  "new_intl": {a:6378157.5, b:6356772.2, ellipseName:"New International 1967"},
  "plessis": {a:6376523.0, rf:6355863.0, ellipseName:"Plessis 1817 (France)"},
  "krass": {a:6378245.0, rf:298.3, ellipseName:"Krassovsky, 1942"},
  "SEasia": {a:6378155.0, b:6356773.3205, ellipseName:"Southeast Asia"},
  "walbeck": {a:6376896.0, b:6355834.8467, ellipseName:"Walbeck"},
  "WGS60": {a:6378165.0, rf:298.3, ellipseName:"WGS 60"},
  "WGS66": {a:6378145.0, rf:298.25, ellipseName:"WGS 66"},
  "WGS72": {a:6378135.0, rf:298.26, ellipseName:"WGS 72"},
  "WGS84": {a:6378137.0, rf:298.257223563, ellipseName:"WGS 84"},
  "sphere": {a:6370997.0, b:6370997.0, ellipseName:"Normal Sphere (r=6370997)"}
};

Proj4js.Datum = {
  "WGS84": {towgs84: "0,0,0", ellipse: "WGS84", datumName: "WGS84"},
  "GGRS87": {towgs84: "-199.87,74.79,246.62", ellipse: "GRS80", datumName: "Greek_Geodetic_Reference_System_1987"},
  "NAD83": {towgs84: "0,0,0", ellipse: "GRS80", datumName: "North_American_Datum_1983"},
  "NAD27": {nadgrids: "@conus,@alaska,@ntv2_0.gsb,@ntv1_can.dat", ellipse: "clrk66", datumName: "North_American_Datum_1927"},
  "potsdam": {towgs84: "606.0,23.0,413.0", ellipse: "bessel", datumName: "Potsdam Rauenberg 1950 DHDN"},
  "carthage": {towgs84: "-263.0,6.0,431.0", ellipse: "clark80", datumName: "Carthage 1934 Tunisia"},
  "hermannskogel": {towgs84: "653.0,-212.0,449.0", ellipse: "bessel", datumName: "Hermannskogel"},
  "ire65": {towgs84: "482.530,-130.596,564.557,-1.042,-0.214,-0.631,8.15", ellipse: "mod_airy", datumName: "Ireland 1965"},
  "nzgd49": {towgs84: "59.47,-5.04,187.44,0.47,-0.1,1.024,-4.5993", ellipse: "intl", datumName: "New Zealand Geodetic Datum 1949"},
  "OSGB36": {towgs84: "446.448,-125.157,542.060,0.1502,0.2470,0.8421,-20.4894", ellipse: "airy", datumName: "Airy 1830"}
};

Proj4js.WGS84 = new Proj4js.Proj('WGS84');
Proj4js.Datum['OSB36'] = Proj4js.Datum['OSGB36']; //as returned from spatialreference.org
/* ======================================================================
    projCode/aea.js
   ====================================================================== */

/*******************************************************************************
NAME                     ALBERS CONICAL EQUAL AREA 

PURPOSE:	Transforms input longitude and latitude to Easting and Northing
		for the Albers Conical Equal Area projection.  The longitude
		and latitude must be in radians.  The Easting and Northing
		values will be returned in meters.

PROGRAMMER              DATE
----------              ----
T. Mittan,       	Feb, 1992

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/


Proj4js.Proj.aea = {
  init : function() {

    if (Math.abs(this.lat1 + this.lat2) < Proj4js.common.EPSLN) {
       Proj4js.reportError("aeaInitEqualLatitudes");
       return;
    }
    this.temp = this.b / this.a;
    this.es = 1.0 - Math.pow(this.temp,2);
    this.e3 = Math.sqrt(this.es);

    this.sin_po=Math.sin(this.lat1);
    this.cos_po=Math.cos(this.lat1);
    this.t1=this.sin_po;
    this.con = this.sin_po;
    this.ms1 = Proj4js.common.msfnz(this.e3,this.sin_po,this.cos_po);
    this.qs1 = Proj4js.common.qsfnz(this.e3,this.sin_po,this.cos_po);

    this.sin_po=Math.sin(this.lat2);
    this.cos_po=Math.cos(this.lat2);
    this.t2=this.sin_po;
    this.ms2 = Proj4js.common.msfnz(this.e3,this.sin_po,this.cos_po);
    this.qs2 = Proj4js.common.qsfnz(this.e3,this.sin_po,this.cos_po);

    this.sin_po=Math.sin(this.lat0);
    this.cos_po=Math.cos(this.lat0);
    this.t3=this.sin_po;
    this.qs0 = Proj4js.common.qsfnz(this.e3,this.sin_po,this.cos_po);

    if (Math.abs(this.lat1 - this.lat2) > Proj4js.common.EPSLN) {
      this.ns0 = (this.ms1 * this.ms1 - this.ms2 *this.ms2)/ (this.qs2 - this.qs1);
    } else {
      this.ns0 = this.con;
    }
    this.c = this.ms1 * this.ms1 + this.ns0 * this.qs1;
    this.rh = this.a * Math.sqrt(this.c - this.ns0 * this.qs0)/this.ns0;
  },

/* Albers Conical Equal Area forward equations--mapping lat,long to x,y
  -------------------------------------------------------------------*/
  forward: function(p){

    var lon=p.x;
    var lat=p.y;

    this.sin_phi=Math.sin(lat);
    this.cos_phi=Math.cos(lat);

    var qs = Proj4js.common.qsfnz(this.e3,this.sin_phi,this.cos_phi);
    var rh1 =this.a * Math.sqrt(this.c - this.ns0 * qs)/this.ns0;
    var theta = this.ns0 * Proj4js.common.adjust_lon(lon - this.long0); 
    var x = rh1 * Math.sin(theta) + this.x0;
    var y = this.rh - rh1 * Math.cos(theta) + this.y0;

    p.x = x; 
    p.y = y;
    return p;
  },


  inverse: function(p) {
    var rh1,qs,con,theta,lon,lat;

    p.x -= this.x0;
    p.y = this.rh - p.y + this.y0;
    if (this.ns0 >= 0) {
      rh1 = Math.sqrt(p.x *p.x + p.y * p.y);
      con = 1.0;
    } else {
      rh1 = -Math.sqrt(p.x * p.x + p.y *p.y);
      con = -1.0;
    }
    theta = 0.0;
    if (rh1 != 0.0) {
      theta = Math.atan2(con * p.x, con * p.y);
    }
    con = rh1 * this.ns0 / this.a;
    qs = (this.c - con * con) / this.ns0;
    if (this.e3 >= 1e-10) {
      con = 1 - .5 * (1.0 -this.es) * Math.log((1.0 - this.e3) / (1.0 + this.e3))/this.e3;
      if (Math.abs(Math.abs(con) - Math.abs(qs)) > .0000000001 ) {
          lat = this.phi1z(this.e3,qs);
      } else {
          if (qs >= 0) {
             lat = .5 * PI;
          } else {
             lat = -.5 * PI;
          }
      }
    } else {
      lat = this.phi1z(e3,qs);
    }

    lon = Proj4js.common.adjust_lon(theta/this.ns0 + this.long0);
    p.x = lon;
    p.y = lat;
    return p;
  },
  
/* Function to compute phi1, the latitude for the inverse of the
   Albers Conical Equal-Area projection.
-------------------------------------------*/
  phi1z: function (eccent,qs) {
    var con, com, dphi;
    var phi = Proj4js.common.asinz(.5 * qs);
    if (eccent < Proj4js.common.EPSLN) return phi;
    
    var eccnts = eccent * eccent; 
    for (var i = 1; i <= 25; i++) {
        sinphi = Math.sin(phi);
        cosphi = Math.cos(phi);
        con = eccent * sinphi; 
        com = 1.0 - con * con;
        dphi = .5 * com * com / cosphi * (qs / (1.0 - eccnts) - sinphi / com + .5 / eccent * Math.log((1.0 - con) / (1.0 + con)));
        phi = phi + dphi;
        if (Math.abs(dphi) <= 1e-7) return phi;
    }
    Proj4js.reportError("aea:phi1z:Convergence error");
    return null;
  }
  
};



/* ======================================================================
    projCode/sterea.js
   ====================================================================== */


Proj4js.Proj.sterea = {
  dependsOn : 'gauss',

  init : function() {
    Proj4js.Proj['gauss'].init.apply(this);
    if (!this.rc) {
      Proj4js.reportError("sterea:init:E_ERROR_0");
      return;
    }
    this.sinc0 = Math.sin(this.phic0);
    this.cosc0 = Math.cos(this.phic0);
    this.R2 = 2.0 * this.rc;
    if (!this.title) this.title = "Oblique Stereographic Alternative";
  },

  forward : function(p) {
    p.x = Proj4js.common.adjust_lon(p.x-this.long0); /* adjust del longitude */
    Proj4js.Proj['gauss'].forward.apply(this, [p]);
    sinc = Math.sin(p.y);
    cosc = Math.cos(p.y);
    cosl = Math.cos(p.x);
    k = this.k0 * this.R2 / (1.0 + this.sinc0 * sinc + this.cosc0 * cosc * cosl);
    p.x = k * cosc * Math.sin(p.x);
    p.y = k * (this.cosc0 * sinc - this.sinc0 * cosc * cosl);
    p.x = this.a * p.x + this.x0;
    p.y = this.a * p.y + this.y0;
    return p;
  },

  inverse : function(p) {
    var lon,lat;
    p.x = (p.x - this.x0) / this.a; /* descale and de-offset */
    p.y = (p.y - this.y0) / this.a;

    p.x /= this.k0;
    p.y /= this.k0;
    if ( (rho = Math.sqrt(p.x*p.x + p.y*p.y)) ) {
      c = 2.0 * Math.atan2(rho, this.R2);
      sinc = Math.sin(c);
      cosc = Math.cos(c);
      lat = Math.asin(cosc * this.sinc0 + p.y * sinc * this.cosc0 / rho);
      lon = Math.atan2(p.x * sinc, rho * this.cosc0 * cosc - p.y * this.sinc0 * sinc);
    } else {
      lat = this.phic0;
      lon = 0.;
    }

    p.x = lon;
    p.y = lat;
    Proj4js.Proj['gauss'].inverse.apply(this,[p]);
    p.x = Proj4js.common.adjust_lon(p.x + this.long0); /* adjust longitude to CM */
    return p;
  }
};

/* ======================================================================
    projCode/poly.js
   ====================================================================== */

/* Function to compute, phi4, the latitude for the inverse of the
   Polyconic projection.
------------------------------------------------------------*/
function phi4z (eccent,e0,e1,e2,e3,a,b,c,phi) {
	var sinphi, sin2ph, tanph, ml, mlp, con1, con2, con3, dphi, i;

	phi = a;
	for (i = 1; i <= 15; i++) {
		sinphi = Math.sin(phi);
		tanphi = Math.tan(phi);
		c = tanphi * Math.sqrt (1.0 - eccent * sinphi * sinphi);
		sin2ph = Math.sin (2.0 * phi);
		/*
		ml = e0 * *phi - e1 * sin2ph + e2 * sin (4.0 *  *phi);
		mlp = e0 - 2.0 * e1 * cos (2.0 *  *phi) + 4.0 * e2 *  cos (4.0 *  *phi);
		*/
		ml = e0 * phi - e1 * sin2ph + e2 * Math.sin (4.0 *  phi) - e3 * Math.sin (6.0 * phi);
		mlp = e0 - 2.0 * e1 * Math.cos (2.0 *  phi) + 4.0 * e2 * Math.cos (4.0 *  phi) - 6.0 * e3 * Math.cos (6.0 *  phi);
		con1 = 2.0 * ml + c * (ml * ml + b) - 2.0 * a *  (c * ml + 1.0);
		con2 = eccent * sin2ph * (ml * ml + b - 2.0 * a * ml) / (2.0 *c);
		con3 = 2.0 * (a - ml) * (c * mlp - 2.0 / sin2ph) - 2.0 * mlp;
		dphi = con1 / (con2 + con3);
		phi += dphi;
		if (Math.abs(dphi) <= .0000000001 ) return(phi);   
	}
	Proj4js.reportError("phi4z: No convergence");
	return null;
}


/* Function to compute the constant e4 from the input of the eccentricity
   of the spheroid, x.  This constant is used in the Polar Stereographic
   projection.
--------------------------------------------------------------------*/
function e4fn(x) {
	var con, com;
	con = 1.0 + x;
	com = 1.0 - x;
	return (Math.sqrt((Math.pow(con,con))*(Math.pow(com,com))));
}





/*******************************************************************************
NAME                             POLYCONIC 

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Polyconic projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE
----------              ----
T. Mittan		Mar, 1993

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/

Proj4js.Proj.poly = {

	/* Initialize the POLYCONIC projection
	  ----------------------------------*/
	init: function() {
		var temp;			/* temporary variable		*/
		if (this.lat0=0) this.lat0=90;//this.lat0 ca

		/* Place parameters in static storage for common use
		  -------------------------------------------------*/
		this.temp = this.b / this.a;
		this.es = 1.0 - Math.pow(this.temp,2);// devait etre dans tmerc.js mais n y est pas donc je commente sinon retour de valeurs nulles 
		this.e = Math.sqrt(this.es);
		this.e0 = Proj4js.common.e0fn(this.es);
		this.e1 = Proj4js.common.e1fn(this.es);
		this.e2 = Proj4js.common.e2fn(this.es);
		this.e3 = Proj4js.common.e3fn(this.es);
		this.ml0 = Proj4js.common.mlfn(this.e0, this.e1,this.e2, this.e3, this.lat0);//si que des zeros le calcul ne se fait pas
		//if (!this.ml0) {this.ml0=0;}
	},


	/* Polyconic forward equations--mapping lat,long to x,y
	  ---------------------------------------------------*/
	forward: function(p) {
		var sinphi, cosphi;	/* sin and cos value				*/
		var al;				/* temporary values				*/
		var c;				/* temporary values				*/
		var con, ml;		/* cone constant, small m			*/
		var ms;				/* small m					*/
		var x,y;

		var lon=p.x;
		var lat=p.y;	

		con = Proj4js.common.adjust_lon(lon - this.long0);
		if (Math.abs(lat) <= .0000001) {
			x = this.x0 + this.a * con;
			y = this.y0 - this.a * this.ml0;
		} else {
			sinphi = Math.sin(lat);
			cosphi = Math.cos(lat);	   

			ml = Proj4js.common.mlfn(this.e0, this.e1, this.e2, this.e3, lat);
			ms = Proj4js.common.msfnz(this.e,sinphi,cosphi);
			con = sinphi;
			x = this.x0 + this.a * ms * Math.sin(con)/sinphi;
			y = this.y0 + this.a * (ml - this.ml0 + ms * (1.0 - Math.cos(con))/sinphi);
		}

		p.x=x;
		p.y=y;   
		return p;
	},


	/* Inverse equations
	-----------------*/
	inverse: function(p) {
		var sin_phi, cos_phi;	/* sin and cos value				*/
		var al;					/* temporary values				*/
		var b;					/* temporary values				*/
		var c;					/* temporary values				*/
		var con, ml;			/* cone constant, small m			*/
		var iflg;				/* error flag					*/
		var lon,lat;
		p.x -= this.x0;
		p.y -= this.y0;
		al = this.ml0 + p.y/this.a;
		iflg = 0;

		if (Math.abs(al) <= .0000001) {
			lon = p.x/this.a + this.long0;
			lat = 0.0;
		} else {
			b = al * al + (p.x/this.a) * (p.x/this.a);
			iflg = phi4z(this.es,this.e0,this.e1,this.e2,this.e3,this.al,b,c,lat);
			if (iflg != 1) return(iflg);
			lon = Proj4js.common.adjust_lon((Proj4js.common.asinz(p.x * c / this.a) / Math.sin(lat)) + this.long0);
		}

		p.x=lon;
		p.y=lat;
		return p;
	}
};



/* ======================================================================
    projCode/equi.js
   ====================================================================== */

/*******************************************************************************
NAME                             EQUIRECTANGULAR 

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Equirectangular projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE
----------              ----
T. Mittan		Mar, 1993

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/
Proj4js.Proj.equi = {

  init: function() {
    if(!this.x0) this.x0=0;
    if(!this.y0) this.y0=0;
    if(!this.lat0) this.lat0=0;
    if(!this.long0) this.long0=0;
    ///this.t2;
  },



/* Equirectangular forward equations--mapping lat,long to x,y
  ---------------------------------------------------------*/
  forward: function(p) {

    var lon=p.x;				
    var lat=p.y;			

    var dlon = Proj4js.common.adjust_lon(lon - this.long0);
    var x = this.x0 +this. a * dlon *Math.cos(this.lat0);
    var y = this.y0 + this.a * lat;

    this.t1=x;
    this.t2=Math.cos(this.lat0);
    p.x=x;
    p.y=y;
    return p;
  },  //equiFwd()



/* Equirectangular inverse equations--mapping x,y to lat/long
  ---------------------------------------------------------*/
  inverse: function(p) {

    p.x -= this.x0;
    p.y -= this.y0;
    var lat = p.y /this. a;

    if ( Math.abs(lat) > Proj4js.common.HALF_PI) {
        Proj4js.reportError("equi:Inv:DataError");
    }
    var lon = Proj4js.common.adjust_lon(this.long0 + p.x / (this.a * Math.cos(this.lat0)));
    p.x=lon;
    p.y=lat;
  }//equiInv()
};


/* ======================================================================
    projCode/merc.js
   ====================================================================== */

/*******************************************************************************
NAME                            MERCATOR

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Mercator projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE
----------              ----
D. Steinwand, EROS      Nov, 1991
T. Mittan		Mar, 1993

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/

//static double r_major = a;		   /* major axis 				*/
//static double r_minor = b;		   /* minor axis 				*/
//static double lon_center = long0;	   /* Center longitude (projection center) */
//static double lat_origin =  lat0;	   /* center latitude			*/
//static double e,es;		           /* eccentricity constants		*/
//static double m1;		               /* small value m			*/
//static double false_northing = y0;   /* y offset in meters			*/
//static double false_easting = x0;	   /* x offset in meters			*/
//scale_fact = k0 

Proj4js.Proj.merc = {
  init : function() {
	//?this.temp = this.r_minor / this.r_major;
	//this.temp = this.b / this.a;
	//this.es = 1.0 - Math.sqrt(this.temp);
	//this.e = Math.sqrt( this.es );
	//?this.m1 = Math.cos(this.lat_origin) / (Math.sqrt( 1.0 - this.es * Math.sin(this.lat_origin) * Math.sin(this.lat_origin)));
	//this.m1 = Math.cos(0.0) / (Math.sqrt( 1.0 - this.es * Math.sin(0.0) * Math.sin(0.0)));
    if (this.lat_ts) {
      if (this.sphere) {
        this.k0 = Math.cos(this.lat_ts);
      } else {
        this.k0 = Proj4js.common.msfnz(this.es, Math.sin(this.lat_ts), Math.cos(this.lat_ts));
      }
    }
  },

/* Mercator forward equations--mapping lat,long to x,y
  --------------------------------------------------*/

  forward : function(p) {	
    //alert("ll2m coords : "+coords);
    var lon = p.x;
    var lat = p.y;
    // convert to radians
    if ( lat*Proj4js.common.R2D > 90.0 && 
          lat*Proj4js.common.R2D < -90.0 && 
          lon*Proj4js.common.R2D > 180.0 && 
          lon*Proj4js.common.R2D < -180.0) {
      Proj4js.reportError("merc:forward: llInputOutOfRange: "+ lon +" : " + lat);
      return null;
    }

    var x,y;
    if(Math.abs( Math.abs(lat) - Proj4js.common.HALF_PI)  <= Proj4js.common.EPSLN) {
      Proj4js.reportError("merc:forward: ll2mAtPoles");
      return null;
    } else {
      if (this.sphere) {
        x = this.x0 + this.a * this.k0 * Proj4js.common.adjust_lon(lon - this.long0);
        y = this.y0 + this.a * this.k0 * Math.log(Math.tan(Proj4js.common.FORTPI + 0.5*lat));
      } else {
        var sinphi = Math.sin(lat);
        var ts = Proj4js.common.tsfnz(this.e,lat,sinphi);
        x = this.x0 + this.a * this.k0 * Proj4js.common.adjust_lon(lon - this.long0);
        y = this.y0 - this.a * this.k0 * Math.log(ts);
      }
      p.x = x; 
      p.y = y;
      return p;
    }
  },


  /* Mercator inverse equations--mapping x,y to lat/long
  --------------------------------------------------*/
  inverse : function(p) {	

    var x = p.x - this.x0;
    var y = p.y - this.y0;
    var lon,lat;

    if (this.sphere) {
      lat = Proj4js.common.HALF_PI - 2.0 * Math.atan(Math.exp(-y / this.a * this.k0));
    } else {
      var ts = Math.exp(-y / (this.a * this.k0));
      lat = Proj4js.common.phi2z(this.e,ts);
      if(lat == -9999) {
        Proj4js.reportError("merc:inverse: lat = -9999");
        return null;
      }
    }
    lon = Proj4js.common.adjust_lon(this.long0+ x / (this.a * this.k0));

    p.x = lon;
    p.y = lat;
    return p;
  }
};


/* ======================================================================
    projCode/utm.js
   ====================================================================== */

/*******************************************************************************
NAME                            TRANSVERSE MERCATOR

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Transverse Mercator projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/


/**
  Initialize Transverse Mercator projection
*/

Proj4js.Proj.utm = {
  dependsOn : 'tmerc',

  init : function() {
    if (!this.zone) {
      Proj4js.reportError("utm:init: zone must be specified for UTM");
      return;
    }
    this.lat0 = 0.0;
    this.long0 = ((6 * Math.abs(this.zone)) - 183) * Proj4js.common.D2R;
    this.x0 = 500000.0;
    this.y0 = this.utmSouth ? 10000000.0 : 0.0;
    this.k0 = 0.9996;

    Proj4js.Proj['tmerc'].init.apply(this);
    this.forward = Proj4js.Proj['tmerc'].forward;
    this.inverse = Proj4js.Proj['tmerc'].inverse;
  }
};
/* ======================================================================
    projCode/eqdc.js
   ====================================================================== */

/*******************************************************************************
NAME                            EQUIDISTANT CONIC 

PURPOSE:	Transforms input longitude and latitude to Easting and Northing
		for the Equidistant Conic projection.  The longitude and
		latitude must be in radians.  The Easting and Northing values
		will be returned in meters.

PROGRAMMER              DATE
----------              ----
T. Mittan		Mar, 1993

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/

/* Variables common to all subroutines in this code file
  -----------------------------------------------------*/

Proj4js.Proj.eqdc = {

/* Initialize the Equidistant Conic projection
  ------------------------------------------*/
  init: function() {

    /* Place parameters in static storage for common use
      -------------------------------------------------*/

    if(!this.mode) this.mode=0;//chosen default mode
    this.temp = this.b / this.a;
    this.es = 1.0 - Math.pow(this.temp,2);
    this.e = Math.sqrt(this.es);
    this.e0 = Proj4js.common.e0fn(this.es);
    this.e1 = Proj4js.common.e1fn(this.es);
    this.e2 = Proj4js.common.e2fn(this.es);
    this.e3 = Proj4js.common.e3fn(this.es);

    this.sinphi=Math.sin(this.lat1);
    this.cosphi=Math.cos(this.lat1);

    this.ms1 = Proj4js.common.msfnz(this.e,this.sinphi,this.cosphi);
    this.ml1 = Proj4js.common.mlfn(this.e0, this.e1, this.e2,this.e3, this.lat1);

    /* format B
    ---------*/
    if (this.mode != 0) {
      if (Math.abs(this.lat1 + this.lat2) < Proj4js.common.EPSLN) {
            Proj4js.reportError("eqdc:Init:EqualLatitudes");
            //return(81);
       }
       this.sinphi=Math.sin(this.lat2);
       this.cosphi=Math.cos(this.lat2);   

       this.ms2 = Proj4js.common.msfnz(this.e,this.sinphi,this.cosphi);
       this.ml2 = Proj4js.common.mlfn(this.e0, this.e1, this.e2, this.e3, this.lat2);
       if (Math.abs(this.lat1 - this.lat2) >= Proj4js.common.EPSLN) {
         this.ns = (this.ms1 - this.ms2) / (this.ml2 - this.ml1);
       } else {
          this.ns = this.sinphi;
       }
    } else {
      this.ns = this.sinphi;
    }
    this.g = this.ml1 + this.ms1/this.ns;
    this.ml0 = Proj4js.common.mlfn(this.e0, this.e1,this. e2, this.e3, this.lat0);
    this.rh = this.a * (this.g - this.ml0);
  },


/* Equidistant Conic forward equations--mapping lat,long to x,y
  -----------------------------------------------------------*/
  forward: function(p) {
    var lon=p.x;
    var lat=p.y;

    /* Forward equations
      -----------------*/
    var ml = Proj4js.common.mlfn(this.e0, this.e1, this.e2, this.e3, lat);
    var rh1 = this.a * (this.g - ml);
    var theta = this.ns * Proj4js.common.adjust_lon(lon - this.long0);

    var x = this.x0  + rh1 * Math.sin(theta);
    var y = this.y0 + this.rh - rh1 * Math.cos(theta);
    p.x=x;
    p.y=y;
    return p;
  },

/* Inverse equations
  -----------------*/
  inverse: function(p) {
    p.x -= this.x0;
    p.y  = this.rh - p.y + this.y0;
    var con, rh1;
    if (this.ns >= 0) {
       var rh1 = Math.sqrt(p.x *p.x + p.y * p.y); 
       var con = 1.0;
    } else {
       rh1 = -Math.sqrt(p.x *p. x +p. y * p.y); 
       con = -1.0;
    }
    var theta = 0.0;
    if (rh1 != 0.0) theta = Math.atan2(con *p.x, con *p.y);
    var ml = this.g - rh1 /this.a;
    var lat = this.phi3z(this.ml,this.e0,this.e1,this.e2,this.e3);
    var lon = Proj4js.common.adjust_lon(this.long0 + theta / this.ns);

     p.x=lon;
     p.y=lat;  
     return p;
    },
    
/* Function to compute latitude, phi3, for the inverse of the Equidistant
   Conic projection.
-----------------------------------------------------------------*/
  phi3z: function(ml,e0,e1,e2,e3) {
    var phi;
    var dphi;

    phi = ml;
    for (var i = 0; i < 15; i++) {
      dphi = (ml + e1 * Math.sin(2.0 * phi) - e2 * Math.sin(4.0 * phi) + e3 * Math.sin(6.0 * phi))/ e0 - phi;
      phi += dphi;
      if (Math.abs(dphi) <= .0000000001) {
        return phi;
      }
    }
    Proj4js.reportError("PHI3Z-CONV:Latitude failed to converge after 15 iterations");
    return null;
  }

    
};
/* ======================================================================
    projCode/tmerc.js
   ====================================================================== */

/*******************************************************************************
NAME                            TRANSVERSE MERCATOR

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Transverse Mercator projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/


/**
  Initialize Transverse Mercator projection
*/

Proj4js.Proj.tmerc = {
  init : function() {
    this.e0 = Proj4js.common.e0fn(this.es);
    this.e1 = Proj4js.common.e1fn(this.es);
    this.e2 = Proj4js.common.e2fn(this.es);
    this.e3 = Proj4js.common.e3fn(this.es);
    this.ml0 = this.a * Proj4js.common.mlfn(this.e0, this.e1, this.e2, this.e3, this.lat0);
  },

  /**
    Transverse Mercator Forward  - long/lat to x/y
    long/lat in radians
  */
  forward : function(p) {
    var lon = p.x;
    var lat = p.y;

    var delta_lon = Proj4js.common.adjust_lon(lon - this.long0); // Delta longitude
    var con;    // cone constant
    var x, y;
    var sin_phi=Math.sin(lat);
    var cos_phi=Math.cos(lat);

    if (this.sphere) {  /* spherical form */
      var b = cos_phi * Math.sin(delta_lon);
      if ((Math.abs(Math.abs(b) - 1.0)) < .0000000001)  {
        Proj4js.reportError("tmerc:forward: Point projects into infinity");
        return(93);
      } else {
        x = .5 * this.a * this.k0 * Math.log((1.0 + b)/(1.0 - b));
        con = Math.acos(cos_phi * Math.cos(delta_lon)/Math.sqrt(1.0 - b*b));
        if (lat < 0) con = - con;
        y = this.a * this.k0 * (con - this.lat0);
      }
    } else {
      var al  = cos_phi * delta_lon;
      var als = Math.pow(al,2);
      var c   = this.ep2 * Math.pow(cos_phi,2);
      var tq  = Math.tan(lat);
      var t   = Math.pow(tq,2);
      con = 1.0 - this.es * Math.pow(sin_phi,2);
      var n   = this.a / Math.sqrt(con);
      var ml  = this.a * Proj4js.common.mlfn(this.e0, this.e1, this.e2, this.e3, lat);

      x = this.k0 * n * al * (1.0 + als / 6.0 * (1.0 - t + c + als / 20.0 * (5.0 - 18.0 * t + Math.pow(t,2) + 72.0 * c - 58.0 * this.ep2))) + this.x0;
      y = this.k0 * (ml - this.ml0 + n * tq * (als * (0.5 + als / 24.0 * (5.0 - t + 9.0 * c + 4.0 * Math.pow(c,2) + als / 30.0 * (61.0 - 58.0 * t + Math.pow(t,2) + 600.0 * c - 330.0 * this.ep2))))) + this.y0;

    }
    p.x = x; p.y = y;
    return p;
  }, // tmercFwd()

  /**
    Transverse Mercator Inverse  -  x/y to long/lat
  */
  inverse : function(p) {
    var con, phi;  /* temporary angles       */
    var delta_phi; /* difference between longitudes    */
    var i;
    var max_iter = 6;      /* maximun number of iterations */
    var lat, lon;

    if (this.sphere) {   /* spherical form */
      var f = Math.exp(p.x/(this.a * this.k0));
      var g = .5 * (f - 1/f);
      var temp = this.lat0 + p.y/(this.a * this.k0);
      var h = Math.cos(temp);
      con = Math.sqrt((1.0 - h * h)/(1.0 + g * g));
      lat = Proj4js.common.asinz(con);
      if (temp < 0)
        lat = -lat;
      if ((g == 0) && (h == 0)) {
        lon = this.long0;
      } else {
        lon = Proj4js.common.adjust_lon(Math.atan2(g,h) + this.long0);
      }
    } else {    // ellipsoidal form
      var x = p.x - this.x0;
      var y = p.y - this.y0;

      con = (this.ml0 + y / this.k0) / this.a;
      phi = con;
      for (i=0;true;i++) {
        delta_phi=((con + this.e1 * Math.sin(2.0*phi) - this.e2 * Math.sin(4.0*phi) + this.e3 * Math.sin(6.0*phi)) / this.e0) - phi;
        phi += delta_phi;
        if (Math.abs(delta_phi) <= Proj4js.common.EPSLN) break;
        if (i >= max_iter) {
          Proj4js.reportError("tmerc:inverse: Latitude failed to converge");
          return(95);
        }
      } // for()
      if (Math.abs(phi) < Proj4js.common.HALF_PI) {
        // sincos(phi, &sin_phi, &cos_phi);
        var sin_phi=Math.sin(phi);
        var cos_phi=Math.cos(phi);
        var tan_phi = Math.tan(phi);
        var c = this.ep2 * Math.pow(cos_phi,2);
        var cs = Math.pow(c,2);
        var t = Math.pow(tan_phi,2);
        var ts = Math.pow(t,2);
        con = 1.0 - this.es * Math.pow(sin_phi,2);
        var n = this.a / Math.sqrt(con);
        var r = n * (1.0 - this.es) / con;
        var d = x / (n * this.k0);
        var ds = Math.pow(d,2);
        lat = phi - (n * tan_phi * ds / r) * (0.5 - ds / 24.0 * (5.0 + 3.0 * t + 10.0 * c - 4.0 * cs - 9.0 * this.ep2 - ds / 30.0 * (61.0 + 90.0 * t + 298.0 * c + 45.0 * ts - 252.0 * this.ep2 - 3.0 * cs)));
        lon = Proj4js.common.adjust_lon(this.long0 + (d * (1.0 - ds / 6.0 * (1.0 + 2.0 * t + c - ds / 20.0 * (5.0 - 2.0 * c + 28.0 * t - 3.0 * cs + 8.0 * this.ep2 + 24.0 * ts))) / cos_phi));
      } else {
        lat = Proj4js.common.HALF_PI * Proj4js.common.sign(y);
        lon = this.long0;
      }
    }
    p.x = lon;
    p.y = lat;
    return p;
  } // tmercInv()
};
/* ======================================================================
    defs/GOOGLE.js
   ====================================================================== */

Proj4js.defs["GOOGLE"]="+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
Proj4js.defs["EPSG:900913"]=Proj4js.defs["GOOGLE"];
/* ======================================================================
    projCode/gstmerc.js
   ====================================================================== */

Proj4js.Proj.gstmerc = {
  init : function() {

    // array of:  a, b, lon0, lat0, k0, x0, y0
      var temp= this.b / this.a;
      this.e= Math.sqrt(1.0 - temp*temp);
      this.lc= this.long0;
      this.rs= Math.sqrt(1.0+this.e*this.e*Math.pow(Math.cos(this.lat0),4.0)/(1.0-this.e*this.e));
      var sinz= Math.sin(this.lat0);
      var pc= Math.asin(sinz/this.rs);
      var sinzpc= Math.sin(pc);
      this.cp= Proj4js.common.latiso(0.0,pc,sinzpc)-this.rs*Proj4js.common.latiso(this.e,this.lat0,sinz);
      this.n2= this.k0*this.a*Math.sqrt(1.0-this.e*this.e)/(1.0-this.e*this.e*sinz*sinz);
      this.xs= this.x0;
      this.ys= this.y0-this.n2*pc;

      if (!this.title) this.title = "Gauss Schreiber transverse mercator";
    },


    // forward equations--mapping lat,long to x,y
    // -----------------------------------------------------------------
    forward : function(p) {

      var lon= p.x;
      var lat= p.y;

      var L= this.rs*(lon-this.lc);
      var Ls= this.cp+(this.rs*Proj4js.common.latiso(this.e,lat,Math.sin(lat)));
      var lat1= Math.asin(Math.sin(L)/Proj4js.common.cosh(Ls));
      var Ls1= Proj4js.common.latiso(0.0,lat1,Math.sin(lat1));
      p.x= this.xs+(this.n2*Ls1);
      p.y= this.ys+(this.n2*Math.atan(Proj4js.common.sinh(Ls)/Math.cos(L)));
      return p;
    },

  // inverse equations--mapping x,y to lat/long
  // -----------------------------------------------------------------
  inverse : function(p) {

    var x= p.x;
    var y= p.y;

    var L= Math.atan(Proj4js.common.sinh((x-this.xs)/this.n2)/Math.cos((y-this.ys)/this.n2));
    var lat1= Math.asin(Math.sin((y-this.ys)/this.n2)/Proj4js.common.cosh((x-this.xs)/this.n2));
    var LC= Proj4js.common.latiso(0.0,lat1,Math.sin(lat1));
    p.x= this.lc+L/this.rs;
    p.y= Proj4js.common.invlatiso(this.e,(LC-this.cp)/this.rs);
    return p;
  }

};
/* ======================================================================
    projCode/ortho.js
   ====================================================================== */

/*******************************************************************************
NAME                             ORTHOGRAPHIC 

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Orthographic projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE
----------              ----
T. Mittan		Mar, 1993

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/

Proj4js.Proj.ortho = {

  /* Initialize the Orthographic projection
    -------------------------------------*/
  init: function(def) {
    //double temp;			/* temporary variable		*/

    /* Place parameters in static storage for common use
      -------------------------------------------------*/;
    this.sin_p14=Math.sin(this.lat0);
    this.cos_p14=Math.cos(this.lat0);	
  },


  /* Orthographic forward equations--mapping lat,long to x,y
    ---------------------------------------------------*/
  forward: function(p) {
    var sinphi, cosphi;	/* sin and cos value				*/
    var dlon;		/* delta longitude value			*/
    var coslon;		/* cos of longitude				*/
    var ksp;		/* scale factor					*/
    var g;		
    var lon=p.x;
    var lat=p.y;	
    /* Forward equations
      -----------------*/
    dlon = Proj4js.common.adjust_lon(lon - this.long0);

    sinphi=Math.sin(lat);
    cosphi=Math.cos(lat);	

    coslon = Math.cos(dlon);
    g = this.sin_p14 * sinphi + this.cos_p14 * cosphi * coslon;
    ksp = 1.0;
    if ((g > 0) || (Math.abs(g) <= Proj4js.common.EPSLN)) {
      var x = this.a * ksp * cosphi * Math.sin(dlon);
      var y = this.y0 + this.a * ksp * (this.cos_p14 * sinphi - this.sin_p14 * cosphi * coslon);
    } else {
      Proj4js.reportError("orthoFwdPointError");
    }
    p.x=x;
    p.y=y;
    return p;
  },


  inverse: function(p) {
    var rh;		/* height above ellipsoid			*/
    var z;		/* angle					*/
    var sinz,cosz;	/* sin of z and cos of z			*/
    var temp;
    var con;
    var lon , lat;
    /* Inverse equations
      -----------------*/
    p.x -= this.x0;
    p.y -= this.y0;
    rh = Math.sqrt(p.x * p.x + p.y * p.y);
    if (rh > this.a + .0000001) {
      Proj4js.reportError("orthoInvDataError");
    }
    z = Proj4js.common.asinz(rh / this.a);

    sinz=Math.sin(z);
    cosz=Math.cos(z);

    lon = this.long0;
    if (Math.abs(rh) <= Proj4js.common.EPSLN) {
      lat = this.lat0; 
    }
    lat = Proj4js.common.asinz(cosz * this.sin_p14 + (p.y * sinz * this.cos_p14)/rh);
    con = Math.abs(lat0) - Proj4js.common.HALF_PI;
    if (Math.abs(con) <= Proj4js.common.EPSLN) {
       if (this.lat0 >= 0) {
          lon = Proj4js.common.adjust_lon(this.long0 + Math.atan2(p.x, -p.y));
       } else {
          lon = Proj4js.common.adjust_lon(this.long0 -Math.atan2(-p.x, p.y));
       }
    }
    con = cosz - this.sin_p14 * Math.sin(lat);
    if ((Math.abs(con) >= Proj4js.common.EPSLN) || (Math.abs(x) >= Proj4js.common.EPSLN)) {
       lon = Proj4js.common.adjust_lon(this.long0 + Math.atan2((p.x * sinz * this.cos_p14), (con * rh)));
    }
    p.x=lon;
    p.y=lat;
    return p;
  }
};


/* ======================================================================
    projCode/somerc.js
   ====================================================================== */

/*******************************************************************************
NAME                       SWISS OBLIQUE MERCATOR

PURPOSE:	Swiss projection.
WARNING:  X and Y are inverted (weird) in the swiss coordinate system. Not
   here, since we want X to be horizontal and Y vertical.

ALGORITHM REFERENCES
1. "Formules et constantes pour le Calcul pour la
 projection cylindrique conforme à axe oblique et pour la transformation entre
 des systèmes de référence".
 http://www.swisstopo.admin.ch/internet/swisstopo/fr/home/topics/survey/sys/refsys/switzerland.parsysrelated1.31216.downloadList.77004.DownloadFile.tmp/swissprojectionfr.pdf

*******************************************************************************/

Proj4js.Proj.somerc = {

  init: function() {
    var phy0 = this.lat0;
    this.lambda0 = this.long0;
    var sinPhy0 = Math.sin(phy0);
    var semiMajorAxis = this.a;
    var invF = this.rf;
    var flattening = 1 / invF;
    var e2 = 2 * flattening - Math.pow(flattening, 2);
    var e = this.e = Math.sqrt(e2);
    this.R = semiMajorAxis * Math.sqrt(1 - e2) / (1 - e2 * Math.pow(sinPhy0, 2.0));
    this.alpha = Math.sqrt(1 + e2 / (1 - e2) * Math.pow(Math.cos(phy0), 4.0));
    this.b0 = Math.asin(sinPhy0 / this.alpha);
    this.K = Math.log(Math.tan(Math.PI / 4.0 + this.b0 / 2.0))
            - this.alpha
            * Math.log(Math.tan(Math.PI / 4.0 + phy0 / 2.0))
            + this.alpha
            * e / 2
            * Math.log((1 + e * sinPhy0)
            / (1 - e * sinPhy0));
  },


  forward: function(p) {
    var Sa1 = Math.log(Math.tan(Math.PI / 4.0 - p.y / 2.0));
    var Sa2 = this.e / 2.0
            * Math.log((1 + this.e * Math.sin(p.y))
            / (1 - this.e * Math.sin(p.y)));
    var S = -this.alpha * (Sa1 + Sa2) + this.K;

        // spheric latitude
    var b = 2.0 * (Math.atan(Math.exp(S)) - Math.PI / 4.0);

        // spheric longitude
    var I = this.alpha * (p.x - this.lambda0);

        // psoeudo equatorial rotation
    var rotI = Math.atan(Math.sin(I)
            / (Math.sin(this.b0) * Math.tan(b) +
               Math.cos(this.b0) * Math.cos(I)));

    var rotB = Math.asin(Math.cos(this.b0) * Math.sin(b) -
                         Math.sin(this.b0) * Math.cos(b) * Math.cos(I));

    p.y = this.R / 2.0
            * Math.log((1 + Math.sin(rotB)) / (1 - Math.sin(rotB)))
            + this.y0;
    p.x = this.R * rotI + this.x0;
    return p;
  },

  inverse: function(p) {
    var Y = p.x - this.x0;
    var X = p.y - this.y0;

    var rotI = Y / this.R;
    var rotB = 2 * (Math.atan(Math.exp(X / this.R)) - Math.PI / 4.0);

    var b = Math.asin(Math.cos(this.b0) * Math.sin(rotB)
            + Math.sin(this.b0) * Math.cos(rotB) * Math.cos(rotI));
    var I = Math.atan(Math.sin(rotI)
            / (Math.cos(this.b0) * Math.cos(rotI) - Math.sin(this.b0)
            * Math.tan(rotB)));

    var lambda = this.lambda0 + I / this.alpha;

    var S = 0.0;
    var phy = b;
    var prevPhy = -1000.0;
    var iteration = 0;
    while (Math.abs(phy - prevPhy) > 0.0000001)
    {
      if (++iteration > 20)
      {
        Proj4js.reportError("omercFwdInfinity");
        return;
      }
      //S = Math.log(Math.tan(Math.PI / 4.0 + phy / 2.0));
      S = 1.0
              / this.alpha
              * (Math.log(Math.tan(Math.PI / 4.0 + b / 2.0)) - this.K)
              + this.e
              * Math.log(Math.tan(Math.PI / 4.0
              + Math.asin(this.e * Math.sin(phy))
              / 2.0));
      prevPhy = phy;
      phy = 2.0 * Math.atan(Math.exp(S)) - Math.PI / 2.0;
    }

    p.x = lambda;
    p.y = phy;
    return p;
  }
};
/* ======================================================================
    projCode/stere.js
   ====================================================================== */


// Initialize the Stereographic projection

Proj4js.Proj.stere = {
  ssfn_: function(phit, sinphi, eccen) {
  	sinphi *= eccen;
  	return (Math.tan (.5 * (Proj4js.common.HALF_PI + phit)) * Math.pow((1. - sinphi) / (1. + sinphi), .5 * eccen));
  },
  TOL:	1.e-8,
  NITER:	8,
  CONV:	1.e-10,
  S_POLE:	0,
  N_POLE:	1,
  OBLIQ:	2,
  EQUIT:	3,

  init : function() {
  	this.phits = this.lat_ts ? this.lat_ts : Proj4js.common.HALF_PI;
    var t = Math.abs(this.lat0);
  	if ((Math.abs(t) - Proj4js.common.HALF_PI) < Proj4js.common.EPSLN) {
  		this.mode = this.lat0 < 0. ? this.S_POLE : this.N_POLE;
  	} else {
  		this.mode = t > Proj4js.common.EPSLN ? this.OBLIQ : this.EQUIT;
    }
  	this.phits = Math.abs(this.phits);
  	if (this.es) {
  		var X;

  		switch (this.mode) {
  		case this.N_POLE:
  		case this.S_POLE:
  			if (Math.abs(this.phits - Proj4js.common.HALF_PI) < Proj4js.common.EPSLN) {
  				this.akm1 = 2. * this.k0 / Math.sqrt(Math.pow(1+this.e,1+this.e)*Math.pow(1-this.e,1-this.e));
  			} else {
          t = Math.sin(this.phits);
  				this.akm1 = Math.cos(this.phits) / Proj4js.common.tsfnz(this.e, this.phits, t);
  				t *= this.e;
  				this.akm1 /= Math.sqrt(1. - t * t);
  			}
  			break;
  		case this.EQUIT:
  			this.akm1 = 2. * this.k0;
  			break;
  		case this.OBLIQ:
  			t = Math.sin(this.lat0);
  			X = 2. * Math.atan(this.ssfn_(this.lat0, t, this.e)) - Proj4js.common.HALF_PI;
  			t *= this.e;
  			this.akm1 = 2. * this.k0 * Math.cos(this.lat0) / Math.sqrt(1. - t * t);
  			this.sinX1 = Math.sin(X);
  			this.cosX1 = Math.cos(X);
  			break;
  		}
  	} else {
  		switch (this.mode) {
  		case this.OBLIQ:
  			this.sinph0 = Math.sin(this.lat0);
  			this.cosph0 = Math.cos(this.lat0);
  		case this.EQUIT:
  			this.akm1 = 2. * this.k0;
  			break;
  		case this.S_POLE:
  		case this.N_POLE:
  			this.akm1 = Math.abs(this.phits - Proj4js.common.HALF_PI) >= Proj4js.common.EPSLN ?
  			   Math.cos(this.phits) / Math.tan(Proj4js.common.FORTPI - .5 * this.phits) :
  			   2. * this.k0 ;
  			break;
  		}
  	}
  }, 

// Stereographic forward equations--mapping lat,long to x,y
  forward: function(p) {
    var lon = p.x;
    lon = Proj4js.common.adjust_lon(lon - this.long0);
    var lat = p.y;
    var x, y;
    
    if (this.sphere) {
    	var  sinphi, cosphi, coslam, sinlam;

    	sinphi = Math.sin(lat);
    	cosphi = Math.cos(lat);
    	coslam = Math.cos(lon);
    	sinlam = Math.sin(lon);
    	switch (this.mode) {
    	case this.EQUIT:
    		y = 1. + cosphi * coslam;
    		if (y <= Proj4js.common.EPSLN) {
          F_ERROR;
        }
        y = this.akm1 / y;
    		x = y * cosphi * sinlam;
        y *= sinphi;
    		break;
    	case this.OBLIQ:
    		y = 1. + this.sinph0 * sinphi + this.cosph0 * cosphi * coslam;
    		if (y <= Proj4js.common.EPSLN) {
          F_ERROR;
        }
        y = this.akm1 / y;
    		x = y * cosphi * sinlam;
    		y *= this.cosph0 * sinphi - this.sinph0 * cosphi * coslam;
    		break;
    	case this.N_POLE:
    		coslam = -coslam;
    		lat = -lat;
        //Note  no break here so it conitnues through S_POLE
    	case this.S_POLE:
    		if (Math.abs(lat - Proj4js.common.HALF_PI) < this.TOL) {
          F_ERROR;
        }
        y = this.akm1 * Math.tan(Proj4js.common.FORTPI + .5 * lat);
    		x = sinlam * y;
    		y *= coslam;
    		break;
    	}
    } else {
    	coslam = Math.cos(lon);
    	sinlam = Math.sin(lon);
    	sinphi = Math.sin(lat);
    	if (this.mode == this.OBLIQ || this.mode == this.EQUIT) {
        X = 2. * Math.atan(this.ssfn_(lat, sinphi, this.e));
    		sinX = Math.sin(X - Proj4js.common.HALF_PI);
    		cosX = Math.cos(X);
    	}
    	switch (this.mode) {
    	case this.OBLIQ:
    		A = this.akm1 / (this.cosX1 * (1. + this.sinX1 * sinX + this.cosX1 * cosX * coslam));
    		y = A * (this.cosX1 * sinX - this.sinX1 * cosX * coslam);
    		x = A * cosX;
    		break;
    	case this.EQUIT:
    		A = 2. * this.akm1 / (1. + cosX * coslam);
    		y = A * sinX;
    		x = A * cosX;
    		break;
    	case this.S_POLE:
    		lat = -lat;
    		coslam = - coslam;
    		sinphi = -sinphi;
    	case this.N_POLE:
    		x = this.akm1 * Proj4js.common.tsfnz(this.e, lat, sinphi);
    		y = - x * coslam;
    		break;
    	}
    	x = x * sinlam;
    }
    p.x = x*this.a + this.x0;
    p.y = y*this.a + this.y0;
    return p;
  },


//* Stereographic inverse equations--mapping x,y to lat/long
  inverse: function(p) {
    var x = (p.x - this.x0)/this.a;   /* descale and de-offset */
    var y = (p.y - this.y0)/this.a;
    var lon, lat;

    var cosphi, sinphi, tp=0.0, phi_l=0.0, rho, halfe=0.0, pi2=0.0;
    var i;

    if (this.sphere) {
    	var  c, rh, sinc, cosc;

      rh = Math.sqrt(x*x + y*y);
      c = 2. * Math.atan(rh / this.akm1);
    	sinc = Math.sin(c);
    	cosc = Math.cos(c);
    	lon = 0.;
    	switch (this.mode) {
    	case this.EQUIT:
    		if (Math.abs(rh) <= Proj4js.common.EPSLN) {
    			lat = 0.;
    		} else {
    			lat = Math.asin(y * sinc / rh);
        }
    		if (cosc != 0. || x != 0.) lon = Math.atan2(x * sinc, cosc * rh);
    		break;
    	case this.OBLIQ:
    		if (Math.abs(rh) <= Proj4js.common.EPSLN) {
    			lat = this.phi0;
    		} else {
    			lat = Math.asin(cosc * sinph0 + y * sinc * cosph0 / rh);
        }
        c = cosc - sinph0 * Math.sin(lat);
    		if (c != 0. || x != 0.) {
    			lon = Math.atan2(x * sinc * cosph0, c * rh);
        }
    		break;
    	case this.N_POLE:
    		y = -y;
    	case this.S_POLE:
    		if (Math.abs(rh) <= Proj4js.common.EPSLN) {
    			lat = this.phi0;
    		} else {
    			lat = Math.asin(this.mode == this.S_POLE ? -cosc : cosc);
        }
    		lon = (x == 0. && y == 0.) ? 0. : Math.atan2(x, y);
    		break;
    	}
    } else {
    	rho = Math.sqrt(x*x + y*y);
    	switch (this.mode) {
    	case this.OBLIQ:
    	case this.EQUIT:
        tp = 2. * Math.atan2(rho * this.cosX1 , this.akm1);
    		cosphi = Math.cos(tp);
    		sinphi = Math.sin(tp);
        if( rho == 0.0 ) {
    		  phi_l = Math.asin(cosphi * this.sinX1);
        } else {
    		  phi_l = Math.asin(cosphi * this.sinX1 + (y * sinphi * this.cosX1 / rho));
        }

    		tp = Math.tan(.5 * (Proj4js.common.HALF_PI + phi_l));
    		x *= sinphi;
    		y = rho * this.cosX1 * cosphi - y * this.sinX1* sinphi;
    		pi2 = Proj4js.common.HALF_PI;
    		halfe = .5 * this.e;
    		break;
    	case this.N_POLE:
    		y = -y;
    	case this.S_POLE:
        tp = - rho / this.akm1;
    		phi_l = Proj4js.common.HALF_PI - 2. * Math.atan(tp);
    		pi2 = -Proj4js.common.HALF_PI;
    		halfe = -.5 * this.e;
    		break;
    	}
    	for (i = this.NITER; i--; phi_l = lat) { //check this
    		sinphi = this.e * Math.sin(phi_l);
    		lat = 2. * Math.atan(tp * Math.pow((1.+sinphi)/(1.-sinphi), halfe)) - pi2;
    		if (Math.abs(phi_l - lat) < this.CONV) {
    			if (this.mode == this.S_POLE) lat = -lat;
    			lon = (x == 0. && y == 0.) ? 0. : Math.atan2(x, y);
          p.x = Proj4js.common.adjust_lon(lon + this.long0);
          p.y = lat;
    			return p;
    		}
    	}
    }
  }
}; 
/* ======================================================================
    projCode/nzmg.js
   ====================================================================== */

/*******************************************************************************
NAME                            NEW ZEALAND MAP GRID

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the New Zealand Map Grid projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.


ALGORITHM REFERENCES

1.  Department of Land and Survey Technical Circular 1973/32
      http://www.linz.govt.nz/docs/miscellaneous/nz-map-definition.pdf

2.  OSG Technical Report 4.1
      http://www.linz.govt.nz/docs/miscellaneous/nzmg.pdf


IMPLEMENTATION NOTES

The two references use different symbols for the calculated values. This
implementation uses the variable names similar to the symbols in reference [1].

The alogrithm uses different units for delta latitude and delta longitude.
The delta latitude is assumed to be in units of seconds of arc x 10^-5.
The delta longitude is the usual radians. Look out for these conversions.

The algorithm is described using complex arithmetic. There were three
options:
   * find and use a Javascript library for complex arithmetic
   * write my own complex library
   * expand the complex arithmetic by hand to simple arithmetic

This implementation has expanded the complex multiplication operations
into parallel simple arithmetic operations for the real and imaginary parts.
The imaginary part is way over to the right of the display; this probably
violates every coding standard in the world, but, to me, it makes it much
more obvious what is going on.

The following complex operations are used:
   - addition
   - multiplication
   - division
   - complex number raised to integer power
   - summation

A summary of complex arithmetic operations:
   (from http://en.wikipedia.org/wiki/Complex_arithmetic)
   addition:       (a + bi) + (c + di) = (a + c) + (b + d)i
   subtraction:    (a + bi) - (c + di) = (a - c) + (b - d)i
   multiplication: (a + bi) x (c + di) = (ac - bd) + (bc + ad)i
   division:       (a + bi) / (c + di) = [(ac + bd)/(cc + dd)] + [(bc - ad)/(cc + dd)]i

The algorithm needs to calculate summations of simple and complex numbers. This is
implemented using a for-loop, pre-loading the summed value to zero.

The algorithm needs to calculate theta^2, theta^3, etc while doing a summation.
There are three possible implementations:
   - use Math.pow in the summation loop - except for complex numbers
   - precalculate the values before running the loop
   - calculate theta^n = theta^(n-1) * theta during the loop
This implementation uses the third option for both real and complex arithmetic.

For example
   psi_n = 1;
   sum = 0;
   for (n = 1; n <=6; n++) {
      psi_n1 = psi_n * psi;       // calculate psi^(n+1)
      psi_n = psi_n1;
      sum = sum + A[n] * psi_n;
   }


TEST VECTORS

NZMG E, N:         2487100.638      6751049.719     metres
NZGD49 long, lat:      172.739194       -34.444066  degrees

NZMG E, N:         2486533.395      6077263.661     metres
NZGD49 long, lat:      172.723106       -40.512409  degrees

NZMG E, N:         2216746.425      5388508.765     metres
NZGD49 long, lat:      169.172062       -46.651295  degrees

Note that these test vectors convert from NZMG metres to lat/long referenced
to NZGD49, not the more usual WGS84. The difference is about 70m N/S and about
10m E/W.

These test vectors are provided in reference [1]. Many more test
vectors are available in
   http://www.linz.govt.nz/docs/topography/topographicdata/placenamesdatabase/nznamesmar08.zip
which is a catalog of names on the 260-series maps.


EPSG CODES

NZMG     EPSG:27200
NZGD49   EPSG:4272

http://spatialreference.org/ defines these as
  Proj4js.defs["EPSG:4272"] = "+proj=longlat +ellps=intl +datum=nzgd49 +no_defs ";
  Proj4js.defs["EPSG:27200"] = "+proj=nzmg +lat_0=-41 +lon_0=173 +x_0=2510000 +y_0=6023150 +ellps=intl +datum=nzgd49 +units=m +no_defs ";


LICENSE
  Copyright: Stephen Irons 2008
  Released under terms of the LGPL as per: http://www.gnu.org/copyleft/lesser.html

*******************************************************************************/


/**
  Initialize New Zealand Map Grip projection
*/

Proj4js.Proj.nzmg = {

  /**
   * iterations: Number of iterations to refine inverse transform.
   *     0 -> km accuracy
   *     1 -> m accuracy -- suitable for most mapping applications
   *     2 -> mm accuracy
   */
  iterations: 1,

  init : function() {
    this.A = new Array();
    this.A[1]  = +0.6399175073;
    this.A[2]  = -0.1358797613;
    this.A[3]  = +0.063294409;
    this.A[4]  = -0.02526853;
    this.A[5]  = +0.0117879;
    this.A[6]  = -0.0055161;
    this.A[7]  = +0.0026906;
    this.A[8]  = -0.001333;
    this.A[9]  = +0.00067;
    this.A[10] = -0.00034;

    this.B_re = new Array();        this.B_im = new Array();
    this.B_re[1] = +0.7557853228;   this.B_im[1] =  0.0;
    this.B_re[2] = +0.249204646;    this.B_im[2] = +0.003371507;
    this.B_re[3] = -0.001541739;    this.B_im[3] = +0.041058560;
    this.B_re[4] = -0.10162907;     this.B_im[4] = +0.01727609;
    this.B_re[5] = -0.26623489;     this.B_im[5] = -0.36249218;
    this.B_re[6] = -0.6870983;      this.B_im[6] = -1.1651967;

    this.C_re = new Array();        this.C_im = new Array();
    this.C_re[1] = +1.3231270439;   this.C_im[1] =  0.0;
    this.C_re[2] = -0.577245789;    this.C_im[2] = -0.007809598;
    this.C_re[3] = +0.508307513;    this.C_im[3] = -0.112208952;
    this.C_re[4] = -0.15094762;     this.C_im[4] = +0.18200602;
    this.C_re[5] = +1.01418179;     this.C_im[5] = +1.64497696;
    this.C_re[6] = +1.9660549;      this.C_im[6] = +2.5127645;

    this.D = new Array();
    this.D[1] = +1.5627014243;
    this.D[2] = +0.5185406398;
    this.D[3] = -0.03333098;
    this.D[4] = -0.1052906;
    this.D[5] = -0.0368594;
    this.D[6] = +0.007317;
    this.D[7] = +0.01220;
    this.D[8] = +0.00394;
    this.D[9] = -0.0013;
  },

  /**
    New Zealand Map Grid Forward  - long/lat to x/y
    long/lat in radians
  */
  forward : function(p) {
    var lon = p.x;
    var lat = p.y;

    var delta_lat = lat - this.lat0;
    var delta_lon = lon - this.long0;

    // 1. Calculate d_phi and d_psi    ...                          // and d_lambda
    // For this algorithm, delta_latitude is in seconds of arc x 10-5, so we need to scale to those units. Longitude is radians.
    var d_phi = delta_lat / Proj4js.common.SEC_TO_RAD * 1E-5;       var d_lambda = delta_lon;
    var d_phi_n = 1;  // d_phi^0

    var d_psi = 0;
    for (n = 1; n <= 10; n++) {
      d_phi_n = d_phi_n * d_phi;
      d_psi = d_psi + this.A[n] * d_phi_n;
    }

    // 2. Calculate theta
    var th_re = d_psi;                                              var th_im = d_lambda;

    // 3. Calculate z
    var th_n_re = 1;                                                var th_n_im = 0;  // theta^0
    var th_n_re1;                                                   var th_n_im1;

    var z_re = 0;                                                   var z_im = 0;
    for (n = 1; n <= 6; n++) {
      th_n_re1 = th_n_re*th_re - th_n_im*th_im;                     th_n_im1 = th_n_im*th_re + th_n_re*th_im;
      th_n_re = th_n_re1;                                           th_n_im = th_n_im1;
      z_re = z_re + this.B_re[n]*th_n_re - this.B_im[n]*th_n_im;    z_im = z_im + this.B_im[n]*th_n_re + this.B_re[n]*th_n_im;
    }

    // 4. Calculate easting and northing
    x = (z_im * this.a) + this.x0;
    y = (z_re * this.a) + this.y0;

    p.x = x; p.y = y;

    return p;
  },


  /**
    New Zealand Map Grid Inverse  -  x/y to long/lat
  */
  inverse : function(p) {

    var x = p.x;
    var y = p.y;

    var delta_x = x - this.x0;
    var delta_y = y - this.y0;

    // 1. Calculate z
    var z_re = delta_y / this.a;                                              var z_im = delta_x / this.a;

    // 2a. Calculate theta - first approximation gives km accuracy
    var z_n_re = 1;                                                           var z_n_im = 0;  // z^0
    var z_n_re1;                                                              var z_n_im1;

    var th_re = 0;                                                            var th_im = 0;
    for (n = 1; n <= 6; n++) {
      z_n_re1 = z_n_re*z_re - z_n_im*z_im;                                    z_n_im1 = z_n_im*z_re + z_n_re*z_im;
      z_n_re = z_n_re1;                                                       z_n_im = z_n_im1;
      th_re = th_re + this.C_re[n]*z_n_re - this.C_im[n]*z_n_im;              th_im = th_im + this.C_im[n]*z_n_re + this.C_re[n]*z_n_im;
    }

    // 2b. Iterate to refine the accuracy of the calculation
    //        0 iterations gives km accuracy
    //        1 iteration gives m accuracy -- good enough for most mapping applications
    //        2 iterations bives mm accuracy
    for (i = 0; i < this.iterations; i++) {
       var th_n_re = th_re;                                                      var th_n_im = th_im;
       var th_n_re1;                                                             var th_n_im1;

       var num_re = z_re;                                                        var num_im = z_im;
       for (n = 2; n <= 6; n++) {
         th_n_re1 = th_n_re*th_re - th_n_im*th_im;                               th_n_im1 = th_n_im*th_re + th_n_re*th_im;
         th_n_re = th_n_re1;                                                     th_n_im = th_n_im1;
         num_re = num_re + (n-1)*(this.B_re[n]*th_n_re - this.B_im[n]*th_n_im);  num_im = num_im + (n-1)*(this.B_im[n]*th_n_re + this.B_re[n]*th_n_im);
       }

       th_n_re = 1;                                                              th_n_im = 0;
       var den_re = this.B_re[1];                                                var den_im = this.B_im[1];
       for (n = 2; n <= 6; n++) {
         th_n_re1 = th_n_re*th_re - th_n_im*th_im;                               th_n_im1 = th_n_im*th_re + th_n_re*th_im;
         th_n_re = th_n_re1;                                                     th_n_im = th_n_im1;
         den_re = den_re + n * (this.B_re[n]*th_n_re - this.B_im[n]*th_n_im);    den_im = den_im + n * (this.B_im[n]*th_n_re + this.B_re[n]*th_n_im);
       }

       // Complex division
       var den2 = den_re*den_re + den_im*den_im;
       th_re = (num_re*den_re + num_im*den_im) / den2;                           th_im = (num_im*den_re - num_re*den_im) / den2;
    }

    // 3. Calculate d_phi              ...                                    // and d_lambda
    var d_psi = th_re;                                                        var d_lambda = th_im;
    var d_psi_n = 1;  // d_psi^0

    var d_phi = 0;
    for (n = 1; n <= 9; n++) {
       d_psi_n = d_psi_n * d_psi;
       d_phi = d_phi + this.D[n] * d_psi_n;
    }

    // 4. Calculate latitude and longitude
    // d_phi is calcuated in second of arc * 10^-5, so we need to scale back to radians. d_lambda is in radians.
    var lat = this.lat0 + (d_phi * Proj4js.common.SEC_TO_RAD * 1E5);
    var lon = this.long0 +  d_lambda;

    p.x = lon;
    p.y = lat;

    return p;
  }
};
/* ======================================================================
    projCode/mill.js
   ====================================================================== */

/*******************************************************************************
NAME                    MILLER CYLINDRICAL 

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Miller Cylindrical projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE            
----------              ----           
T. Mittan		March, 1993

This function was adapted from the Lambert Azimuthal Equal Area projection
code (FORTRAN) in the General Cartographic Transformation Package software
which is available from the U.S. Geological Survey National Mapping Division.
 
ALGORITHM REFERENCES

1.  "New Equal-Area Map Projections for Noncircular Regions", John P. Snyder,
    The American Cartographer, Vol 15, No. 4, October 1988, pp. 341-355.

2.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

3.  "Software Documentation for GCTP General Cartographic Transformation
    Package", U.S. Geological Survey National Mapping Division, May 1982.
*******************************************************************************/

Proj4js.Proj.mill = {

/* Initialize the Miller Cylindrical projection
  -------------------------------------------*/
  init: function() {
    //no-op
  },


  /* Miller Cylindrical forward equations--mapping lat,long to x,y
    ------------------------------------------------------------*/
  forward: function(p) {
    var lon=p.x;
    var lat=p.y;
    /* Forward equations
      -----------------*/
    var dlon = Proj4js.common.adjust_lon(lon -this.long0);
    var x = this.x0 + this.a * dlon;
    var y = this.y0 + this.a * Math.log(Math.tan((Proj4js.common.PI / 4.0) + (lat / 2.5))) * 1.25;

    p.x=x;
    p.y=y;
    return p;
  },//millFwd()

  /* Miller Cylindrical inverse equations--mapping x,y to lat/long
    ------------------------------------------------------------*/
  inverse: function(p) {
    p.x -= this.x0;
    p.y -= this.y0;

    var lon = Proj4js.common.adjust_lon(this.long0 + p.x /this.a);
    var lat = 2.5 * (Math.atan(Math.exp(0.8*p.y/this.a)) - Proj4js.common.PI / 4.0);

    p.x=lon;
    p.y=lat;
    return p;
  }//millInv()
};
/* ======================================================================
    projCode/gnom.js
   ====================================================================== */

/*****************************************************************************
NAME                             GNOMONIC

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Gnomonic Projection.
                Implementation based on the existing sterea and ortho
                implementations.

PROGRAMMER              DATE
----------              ----
Richard Marsden         November 2009

ALGORITHM REFERENCES

1.  Snyder, John P., "Flattening the Earth - Two Thousand Years of Map 
    Projections", University of Chicago Press 1993

2.  Wolfram Mathworld "Gnomonic Projection"
    http://mathworld.wolfram.com/GnomonicProjection.html
    Accessed: 12th November 2009
******************************************************************************/

Proj4js.Proj.gnom = {

  /* Initialize the Gnomonic projection
    -------------------------------------*/
  init: function(def) {

    /* Place parameters in static storage for common use
      -------------------------------------------------*/
    this.sin_p14=Math.sin(this.lat0);
    this.cos_p14=Math.cos(this.lat0);
    // Approximation for projecting points to the horizon (infinity)
    this.infinity_dist = 1000 * this.a;
  },


  /* Gnomonic forward equations--mapping lat,long to x,y
    ---------------------------------------------------*/
  forward: function(p) {
    var sinphi, cosphi;	/* sin and cos value				*/
    var dlon;		/* delta longitude value			*/
    var coslon;		/* cos of longitude				*/
    var ksp;		/* scale factor					*/
    var g;		
    var lon=p.x;
    var lat=p.y;	
    /* Forward equations
      -----------------*/
    dlon = Proj4js.common.adjust_lon(lon - this.long0);

    sinphi=Math.sin(lat);
    cosphi=Math.cos(lat);	

    coslon = Math.cos(dlon);
    g = this.sin_p14 * sinphi + this.cos_p14 * cosphi * coslon;
    ksp = 1.0;
    if ((g > 0) || (Math.abs(g) <= Proj4js.common.EPSLN)) {
      x = this.x0 + this.a * ksp * cosphi * Math.sin(dlon) / g;
      y = this.y0 + this.a * ksp * (this.cos_p14 * sinphi - this.sin_p14 * cosphi * coslon) / g;
    } else {
      Proj4js.reportError("orthoFwdPointError");

      // Point is in the opposing hemisphere and is unprojectable
      // We still need to return a reasonable point, so we project 
      // to infinity, on a bearing 
      // equivalent to the northern hemisphere equivalent
      // This is a reasonable approximation for short shapes and lines that 
      // straddle the horizon.

      x = this.x0 + this.infinity_dist * cosphi * Math.sin(dlon);
      y = this.y0 + this.infinity_dist * (this.cos_p14 * sinphi - this.sin_p14 * cosphi * coslon);

    }
    p.x=x;
    p.y=y;
    return p;
  },


  inverse: function(p) {
    var rh;		/* Rho */
    var z;		/* angle */
    var sinc, cosc;
    var c;
    var lon , lat;

    /* Inverse equations
      -----------------*/
    p.x = (p.x - this.x0) / this.a;
    p.y = (p.y - this.y0) / this.a;

    p.x /= this.k0;
    p.y /= this.k0;

    if ( (rh = Math.sqrt(p.x * p.x + p.y * p.y)) ) {
      c = Math.atan2(rh, this.rc);
      sinc = Math.sin(c);
      cosc = Math.cos(c);

      lat = Proj4js.common.asinz(cosc*this.sin_p14 + (p.y*sinc*this.cos_p14) / rh);
      lon = Math.atan2(p.x*sinc, rh*this.cos_p14*cosc - p.y*this.sin_p14*sinc);
      lon = Proj4js.common.adjust_lon(this.long0+lon);
    } else {
      lat = this.phic0;
      lon = 0.0;
    }
 
    p.x=lon;
    p.y=lat;
    return p;
  }
};


/* ======================================================================
    projCode/sinu.js
   ====================================================================== */

/*******************************************************************************
NAME                  		SINUSOIDAL

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Sinusoidal projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE            
----------              ----           
D. Steinwand, EROS      May, 1991     

This function was adapted from the Sinusoidal projection code (FORTRAN) in the 
General Cartographic Transformation Package software which is available from 
the U.S. Geological Survey National Mapping Division.
 
ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  "Software Documentation for GCTP General Cartographic Transformation
    Package", U.S. Geological Survey National Mapping Division, May 1982.
*******************************************************************************/

Proj4js.Proj.sinu = {

	/* Initialize the Sinusoidal projection
	  ------------------------------------*/
	init: function() {
		/* Place parameters in static storage for common use
		  -------------------------------------------------*/
		this.R = 6370997.0; //Radius of earth
	},

	/* Sinusoidal forward equations--mapping lat,long to x,y
	-----------------------------------------------------*/
	forward: function(p) {
		var x,y,delta_lon;	
		var lon=p.x;
		var lat=p.y;	
		/* Forward equations
		-----------------*/
		delta_lon = Proj4js.common.adjust_lon(lon - this.long0);
		x = this.R * delta_lon * Math.cos(lat) + this.x0;
		y = this.R * lat + this.y0;

		p.x=x;
		p.y=y;	
		return p;
	},

	inverse: function(p) {
		var lat,temp,lon;	

		/* Inverse equations
		  -----------------*/
		p.x -= this.x0;
		p.y -= this.y0;
		lat = p.y / this.R;
		if (Math.abs(lat) > Proj4js.common.HALF_PI) {
		    Proj4js.reportError("sinu:Inv:DataError");
		}
		temp = Math.abs(lat) - Proj4js.common.HALF_PI;
		if (Math.abs(temp) > Proj4js.common.EPSLN) {
			temp = this.long0+ p.x / (this.R *Math.cos(lat));
			lon = Proj4js.common.adjust_lon(temp);
		} else {
			lon = this.long0;
		}
		  
		p.x=lon;
		p.y=lat;
		return p;
	}
};


/* ======================================================================
    projCode/vandg.js
   ====================================================================== */

/*******************************************************************************
NAME                    VAN DER GRINTEN 

PURPOSE:	Transforms input Easting and Northing to longitude and
		latitude for the Van der Grinten projection.  The
		Easting and Northing must be in meters.  The longitude
		and latitude values will be returned in radians.

PROGRAMMER              DATE            
----------              ----           
T. Mittan		March, 1993

This function was adapted from the Van Der Grinten projection code
(FORTRAN) in the General Cartographic Transformation Package software
which is available from the U.S. Geological Survey National Mapping Division.
 
ALGORITHM REFERENCES

1.  "New Equal-Area Map Projections for Noncircular Regions", John P. Snyder,
    The American Cartographer, Vol 15, No. 4, October 1988, pp. 341-355.

2.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

3.  "Software Documentation for GCTP General Cartographic Transformation
    Package", U.S. Geological Survey National Mapping Division, May 1982.
*******************************************************************************/

Proj4js.Proj.vandg = {

/* Initialize the Van Der Grinten projection
  ----------------------------------------*/
	init: function() {
		this.R = 6370997.0; //Radius of earth
	},

	forward: function(p) {

		var lon=p.x;
		var lat=p.y;	

		/* Forward equations
		-----------------*/
		var dlon = Proj4js.common.adjust_lon(lon - this.long0);
		var x,y;

		if (Math.abs(lat) <= Proj4js.common.EPSLN) {
			x = this.x0  + this.R * dlon;
			y = this.y0;
		}
		var theta = Proj4js.common.asinz(2.0 * Math.abs(lat / Proj4js.common.PI));
		if ((Math.abs(dlon) <= Proj4js.common.EPSLN) || (Math.abs(Math.abs(lat) - Proj4js.common.HALF_PI) <= Proj4js.common.EPSLN)) {
			x = this.x0;
			if (lat >= 0) {
				y = this.y0 + Proj4js.common.PI * this.R * Math.tan(.5 * theta);
			} else {
				y = this.y0 + Proj4js.common.PI * this.R * - Math.tan(.5 * theta);
			}
			//  return(OK);
		}
		var al = .5 * Math.abs((Proj4js.common.PI / dlon) - (dlon / Proj4js.common.PI));
		var asq = al * al;
		var sinth = Math.sin(theta);
		var costh = Math.cos(theta);

		var g = costh / (sinth + costh - 1.0);
		var gsq = g * g;
		var m = g * (2.0 / sinth - 1.0);
		var msq = m * m;
		var con = Proj4js.common.PI * this.R * (al * (g - msq) + Math.sqrt(asq * (g - msq) * (g - msq) - (msq + asq) * (gsq - msq))) / (msq + asq);
		if (dlon < 0) {
		 con = -con;
		}
		x = this.x0 + con;
		con = Math.abs(con / (Proj4js.common.PI * this.R));
		if (lat >= 0) {
		 y = this.y0 + Proj4js.common.PI * this.R * Math.sqrt(1.0 - con * con - 2.0 * al * con);
		} else {
		 y = this.y0 - Proj4js.common.PI * this.R * Math.sqrt(1.0 - con * con - 2.0 * al * con);
		}
		p.x = x;
		p.y = y;
		return p;
	},

/* Van Der Grinten inverse equations--mapping x,y to lat/long
  ---------------------------------------------------------*/
	inverse: function(p) {
		var dlon;
		var xx,yy,xys,c1,c2,c3;
		var al,asq;
		var a1;
		var m1;
		var con;
		var th1;
		var d;

		/* inverse equations
		-----------------*/
		p.x -= this.x0;
		p.y -= this.y0;
		con = Proj4js.common.PI * this.R;
		xx = p.x / con;
		yy =p.y / con;
		xys = xx * xx + yy * yy;
		c1 = -Math.abs(yy) * (1.0 + xys);
		c2 = c1 - 2.0 * yy * yy + xx * xx;
		c3 = -2.0 * c1 + 1.0 + 2.0 * yy * yy + xys * xys;
		d = yy * yy / c3 + (2.0 * c2 * c2 * c2 / c3 / c3 / c3 - 9.0 * c1 * c2 / c3 /c3) / 27.0;
		a1 = (c1 - c2 * c2 / 3.0 / c3) / c3;
		m1 = 2.0 * Math.sqrt( -a1 / 3.0);
		con = ((3.0 * d) / a1) / m1;
		if (Math.abs(con) > 1.0) {
			if (con >= 0.0) {
				con = 1.0;
			} else {
				con = -1.0;
			}
		}
		th1 = Math.acos(con) / 3.0;
		if (p.y >= 0) {
			lat = (-m1 *Math.cos(th1 + Proj4js.common.PI / 3.0) - c2 / 3.0 / c3) * Proj4js.common.PI;
		} else {
			lat = -(-m1 * Math.cos(th1 + PI / 3.0) - c2 / 3.0 / c3) * Proj4js.common.PI;
		}

		if (Math.abs(xx) < Proj4js.common.EPSLN) {
			lon = this.long0;
		}
		lon = Proj4js.common.adjust_lon(this.long0 + Proj4js.common.PI * (xys - 1.0 + Math.sqrt(1.0 + 2.0 * (xx * xx - yy * yy) + xys * xys)) / 2.0 / xx);

		p.x=lon;
		p.y=lat;
		return p;
	}
};
/* ======================================================================
    projCode/cea.js
   ====================================================================== */

/*******************************************************************************
NAME                    LAMBERT CYLINDRICAL EQUAL AREA

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Lambert Cylindrical Equal Area projection.
                This class of projection includes the Behrmann and 
                Gall-Peters Projections.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE            
----------              ----
R. Marsden              August 2009
Winwaed Software Tech LLC, http://www.winwaed.com

This function was adapted from the Miller Cylindrical Projection in the Proj4JS
library.

Note: This implementation assumes a Spherical Earth. The (commented) code 
has been included for the ellipsoidal forward transform, but derivation of 
the ellispoidal inverse transform is beyond me. Note that most of the 
Proj4JS implementations do NOT currently support ellipsoidal figures. 
Therefore this is not seen as a problem - especially this lack of support 
is explicitly stated here.
 
ALGORITHM REFERENCES

1.  "Cartographic Projection Procedures for the UNIX Environment - 
     A User's Manual" by Gerald I. Evenden, USGS Open File Report 90-284
    and Release 4 Interim Reports (2003)

2.  Snyder, John P., "Flattening the Earth - Two Thousand Years of Map 
    Projections", Univ. Chicago Press, 1993
*******************************************************************************/

Proj4js.Proj.cea = {

/* Initialize the Cylindrical Equal Area projection
  -------------------------------------------*/
  init: function() {
    //no-op
  },


  /* Cylindrical Equal Area forward equations--mapping lat,long to x,y
    ------------------------------------------------------------*/
  forward: function(p) {
    var lon=p.x;
    var lat=p.y;
    /* Forward equations
      -----------------*/
    dlon = Proj4js.common.adjust_lon(lon -this.long0);
    var x = this.x0 + this.a * dlon * Math.cos(this.lat_ts);
    var y = this.y0 + this.a * Math.sin(lat) / Math.cos(this.lat_ts);
   /* Elliptical Forward Transform
      Not implemented due to a lack of a matchign inverse function
    {
      var Sin_Lat = Math.sin(lat);
      var Rn = this.a * (Math.sqrt(1.0e0 - this.es * Sin_Lat * Sin_Lat ));
      x = this.x0 + this.a * dlon * Math.cos(this.lat_ts);
      y = this.y0 + Rn * Math.sin(lat) / Math.cos(this.lat_ts);
    }
   */


    p.x=x;
    p.y=y;
    return p;
  },//ceaFwd()

  /* Cylindrical Equal Area inverse equations--mapping x,y to lat/long
    ------------------------------------------------------------*/
  inverse: function(p) {
    p.x -= this.x0;
    p.y -= this.y0;

    var lon = Proj4js.common.adjust_lon( this.long0 + (p.x / this.a) / Math.cos(this.lat_ts) );

    var lat = Math.asin( (p.y/this.a) * Math.cos(this.lat_ts) );

    p.x=lon;
    p.y=lat;
    return p;
  }//ceaInv()
};
/* ======================================================================
    projCode/eqc.js
   ====================================================================== */

/* similar to equi.js FIXME proj4 uses eqc */
Proj4js.Proj.eqc = {
  init : function() {

      if(!this.x0) this.x0=0;
      if(!this.y0) this.y0=0;
      if(!this.lat0) this.lat0=0;
      if(!this.long0) this.long0=0;
      if(!this.lat_ts) this.lat_ts=0;
      if (!this.title) this.title = "Equidistant Cylindrical (Plate Carre)";

      this.rc= Math.cos(this.lat_ts);
    },


    // forward equations--mapping lat,long to x,y
    // -----------------------------------------------------------------
    forward : function(p) {

      var lon= p.x;
      var lat= p.y;

      var dlon = Proj4js.common.adjust_lon(lon - this.long0);
      var dlat = Proj4js.common.adjust_lat(lat - this.lat0 );
      p.x= this.x0 + (this.a*dlon*this.rc);
      p.y= this.y0 + (this.a*dlat        );
      return p;
    },

  // inverse equations--mapping x,y to lat/long
  // -----------------------------------------------------------------
  inverse : function(p) {

    var x= p.x;
    var y= p.y;

    p.x= Proj4js.common.adjust_lon(this.long0 + ((x - this.x0)/(this.a*this.rc)));
    p.y= Proj4js.common.adjust_lat(this.lat0  + ((y - this.y0)/(this.a        )));
    return p;
  }

};
/* ======================================================================
    projCode/cass.js
   ====================================================================== */

/*******************************************************************************
NAME                            CASSINI

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Cassini projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.
    Ported from PROJ.4.


ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
*******************************************************************************/


//Proj4js.defs["EPSG:28191"] = "+proj=cass +lat_0=31.73409694444445 +lon_0=35.21208055555556 +x_0=170251.555 +y_0=126867.909 +a=6378300.789 +b=6356566.435 +towgs84=-275.722,94.7824,340.894,-8.001,-4.42,-11.821,1 +units=m +no_defs";

// Initialize the Cassini projection
// -----------------------------------------------------------------

Proj4js.Proj.cass = {
  init : function() {
    if (!this.sphere) {
      this.en = this.pj_enfn(this.es)
      this.m0 = this.pj_mlfn(this.lat0, Math.sin(this.lat0), Math.cos(this.lat0), this.en);
    }
  },

  C1:	.16666666666666666666,
  C2:	.00833333333333333333,
  C3:	.04166666666666666666,
  C4:	.33333333333333333333,
  C5:	.06666666666666666666,


/* Cassini forward equations--mapping lat,long to x,y
  -----------------------------------------------------------------------*/
  forward: function(p) {

    /* Forward equations
      -----------------*/
    var x,y;
    var lam=p.x;
    var phi=p.y;
    lam = Proj4js.common.adjust_lon(lam - this.long0);
    
    if (this.sphere) {
      x = Math.asin(Math.cos(phi) * Math.sin(lam));
      y = Math.atan2(Math.tan(phi) , Math.cos(lam)) - this.phi0;
    } else {
        //ellipsoid
      this.n = Math.sin(phi);
      this.c = Math.cos(phi);
      y = this.pj_mlfn(phi, this.n, this.c, this.en);
      this.n = 1./Math.sqrt(1. - this.es * this.n * this.n);
      this.tn = Math.tan(phi); 
      this.t = this.tn * this.tn;
      this.a1 = lam * this.c;
      this.c *= this.es * this.c / (1 - this.es);
      this.a2 = this.a1 * this.a1;
      x = this.n * this.a1 * (1. - this.a2 * this.t * (this.C1 - (8. - this.t + 8. * this.c) * this.a2 * this.C2));
      y -= this.m0 - this.n * this.tn * this.a2 * (.5 + (5. - this.t + 6. * this.c) * this.a2 * this.C3);
    }
    
    p.x = this.a*x + this.x0;
    p.y = this.a*y + this.y0;
    return p;
  },//cassFwd()

/* Inverse equations
  -----------------*/
  inverse: function(p) {
    p.x -= this.x0;
    p.y -= this.y0;
    var x = p.x/this.a;
    var y = p.y/this.a;
    
    if (this.sphere) {
      this.dd = y + this.lat0;
      phi = Math.asin(Math.sin(this.dd) * Math.cos(x));
      lam = Math.atan2(Math.tan(x), Math.cos(this.dd));
    } else {
      /* ellipsoid */
      ph1 = this.pj_inv_mlfn(this.m0 + y, this.es, this.en);
      this.tn = Math.tan(ph1); 
      this.t = this.tn * this.tn;
      this.n = Math.sin(ph1);
      this.r = 1. / (1. - this.es * this.n * this.n);
      this.n = Math.sqrt(this.r);
      this.r *= (1. - this.es) * this.n;
      this.dd = x / this.n;
      this.d2 = this.dd * this.dd;
      phi = ph1 - (this.n * this.tn / this.r) * this.d2 * (.5 - (1. + 3. * this.t) * this.d2 * this.C3);
      lam = this.dd * (1. + this.t * this.d2 * (-this.C4 + (1. + 3. * this.t) * this.d2 * this.C5)) / Math.cos(ph1);
    }
    p.x = Proj4js.common.adjust_lon(this.long0+lam);
    p.y = phi;
    return p;
  },//lamazInv()


  //code from the PROJ.4 pj_mlfn.c file;  this may be useful for other projections
  pj_enfn: function(es) {
    en = new Array();
    en[0] = this.C00 - es * (this.C02 + es * (this.C04 + es * (this.C06 + es * this.C08)));
    en[1] = es * (this.C22 - es * (this.C04 + es * (this.C06 + es * this.C08)));
    var t = es * es;
    en[2] = t * (this.C44 - es * (this.C46 + es * this.C48));
    t *= es;
    en[3] = t * (this.C66 - es * this.C68);
    en[4] = t * es * this.C88;
    return en;
  },
  
  pj_mlfn: function(phi, sphi, cphi, en) {
    cphi *= sphi;
    sphi *= sphi;
    return(en[0] * phi - cphi * (en[1] + sphi*(en[2]+ sphi*(en[3] + sphi*en[4]))));
  },
  
  pj_inv_mlfn: function(arg, es, en) {
    k = 1./(1.-es);
    phi = arg;
    for (i = Proj4js.common.MAX_ITER; i ; --i) { /* rarely goes over 2 iterations */
      s = Math.sin(phi);
      t = 1. - es * s * s;
      //t = this.pj_mlfn(phi, s, Math.cos(phi), en) - arg;
      //phi -= t * (t * Math.sqrt(t)) * k;
      t = (this.pj_mlfn(phi, s, Math.cos(phi), en) - arg) * (t * Math.sqrt(t)) * k;
      phi -= t;
      if (Math.abs(t) < Proj4js.common.EPSLN)
        return phi;
    }
    Proj4js.reportError("cass:pj_inv_mlfn: Convergence error");
    return phi;
  },

/* meridinal distance for ellipsoid and inverse
**	8th degree - accurate to < 1e-5 meters when used in conjuction
**		with typical major axis values.
**	Inverse determines phi to EPS (1e-11) radians, about 1e-6 seconds.
*/
  C00: 1.0,
  C02: .25,
  C04: .046875,
  C06: .01953125,
  C08: .01068115234375,
  C22: .75,
  C44: .46875,
  C46: .01302083333333333333,
  C48: .00712076822916666666,
  C66: .36458333333333333333,
  C68: .00569661458333333333,
  C88: .3076171875

}
/* ======================================================================
    projCode/gauss.js
   ====================================================================== */


Proj4js.Proj.gauss = {

  init : function() {
    sphi = Math.sin(this.lat0);
    cphi = Math.cos(this.lat0);  
    cphi *= cphi;
    this.rc = Math.sqrt(1.0 - this.es) / (1.0 - this.es * sphi * sphi);
    this.C = Math.sqrt(1.0 + this.es * cphi * cphi / (1.0 - this.es));
    this.phic0 = Math.asin(sphi / this.C);
    this.ratexp = 0.5 * this.C * this.e;
    this.K = Math.tan(0.5 * this.phic0 + Proj4js.common.FORTPI) / (Math.pow(Math.tan(0.5*this.lat0 + Proj4js.common.FORTPI), this.C) * Proj4js.common.srat(this.e*sphi, this.ratexp));
  },

  forward : function(p) {
    var lon = p.x;
    var lat = p.y;

    p.y = 2.0 * Math.atan( this.K * Math.pow(Math.tan(0.5 * lat + Proj4js.common.FORTPI), this.C) * Proj4js.common.srat(this.e * Math.sin(lat), this.ratexp) ) - Proj4js.common.HALF_PI;
    p.x = this.C * lon;
    return p;
  },

  inverse : function(p) {
    var DEL_TOL = 1e-14;
    var lon = p.x / this.C;
    var lat = p.y;
    num = Math.pow(Math.tan(0.5 * lat + Proj4js.common.FORTPI)/this.K, 1./this.C);
    for (var i = Proj4js.common.MAX_ITER; i>0; --i) {
      lat = 2.0 * Math.atan(num * Proj4js.common.srat(this.e * Math.sin(p.y), -0.5 * this.e)) - Proj4js.common.HALF_PI;
      if (Math.abs(lat - p.y) < DEL_TOL) break;
      p.y = lat;
    }	
    /* convergence failed */
    if (!i) {
      Proj4js.reportError("gauss:inverse:convergence failed");
      return null;
    }
    p.x = lon;
    p.y = lat;
    return p;
  }
};

/* ======================================================================
    projCode/omerc.js
   ====================================================================== */

/*******************************************************************************
NAME                       OBLIQUE MERCATOR (HOTINE) 

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Oblique Mercator projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE
----------              ----
T. Mittan		Mar, 1993

ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.
*******************************************************************************/

Proj4js.Proj.omerc = {

  /* Initialize the Oblique Mercator  projection
    ------------------------------------------*/
  init: function() {
    if (!this.mode) this.mode=0;
    if (!this.lon1)   {this.lon1=0;this.mode=1;}
    if (!this.lon2)   this.lon2=0;
    if (!this.lat2)    this.lat2=0;

    /* Place parameters in static storage for common use
      -------------------------------------------------*/
    var temp = this.b/ this.a;
    var es = 1.0 - Math.pow(temp,2);
    var e = Math.sqrt(es);

    this.sin_p20=Math.sin(this.lat0);
    this.cos_p20=Math.cos(this.lat0);

    this.con = 1.0 - this.es * this.sin_p20 * this.sin_p20;
    this.com = Math.sqrt(1.0 - es);
    this.bl = Math.sqrt(1.0 + this.es * Math.pow(this.cos_p20,4.0)/(1.0 - es));
    this.al = this.a * this.bl * this.k0 * this.com / this.con;
    if (Math.abs(this.lat0) < Proj4js.common.EPSLN) {
       this.ts = 1.0;
       this.d = 1.0;
       this.el = 1.0;
    } else {
       this.ts = Proj4js.common.tsfnz(this.e,this.lat0,this.sin_p20);
       this.con = Math.sqrt(this.con);
       this.d = this.bl * this.com / (this.cos_p20 * this.con);
       if ((this.d * this.d - 1.0) > 0.0) {
          if (this.lat0 >= 0.0) {
             this.f = this.d + Math.sqrt(this.d * this.d - 1.0);
          } else {
             this.f = this.d - Math.sqrt(this.d * this.d - 1.0);
          }
       } else {
         this.f = this.d;
       }
       this.el = this.f * Math.pow(this.ts,this.bl);
    }

    //this.longc=52.60353916666667;

    if (this.mode != 0) {
       this.g = .5 * (this.f - 1.0/this.f);
       this.gama = Proj4js.common.asinz(Math.sin(this.alpha) / this.d);
       this.longc= this.longc - Proj4js.common.asinz(this.g * Math.tan(this.gama))/this.bl;

       /* Report parameters common to format B
       -------------------------------------*/
       //genrpt(azimuth * R2D,"Azimuth of Central Line:    ");
       //cenlon(lon_origin);
      // cenlat(lat_origin);

       this.con = Math.abs(this.lat0);
       if ((this.con > Proj4js.common.EPSLN) && (Math.abs(this.con - Proj4js.common.HALF_PI) > Proj4js.common.EPSLN)) {
            this.singam=Math.sin(this.gama);
            this.cosgam=Math.cos(this.gama);

            this.sinaz=Math.sin(this.alpha);
            this.cosaz=Math.cos(this.alpha);

            if (this.lat0>= 0) {
               this.u =  (this.al / this.bl) * Math.atan(Math.sqrt(this.d*this.d - 1.0)/this.cosaz);
            } else {
               this.u =  -(this.al / this.bl) *Math.atan(Math.sqrt(this.d*this.d - 1.0)/this.cosaz);
            }
          } else {
            Proj4js.reportError("omerc:Init:DataError");
          }
       } else {
       this.sinphi =Math. sin(this.at1);
       this.ts1 = Proj4js.common.tsfnz(this.e,this.lat1,this.sinphi);
       this.sinphi = Math.sin(this.lat2);
       this.ts2 = Proj4js.common.tsfnz(this.e,this.lat2,this.sinphi);
       this.h = Math.pow(this.ts1,this.bl);
       this.l = Math.pow(this.ts2,this.bl);
       this.f = this.el/this.h;
       this.g = .5 * (this.f - 1.0/this.f);
       this.j = (this.el * this.el - this.l * this.h)/(this.el * this.el + this.l * this.h);
       this.p = (this.l - this.h) / (this.l + this.h);
       this.dlon = this.lon1 - this.lon2;
       if (this.dlon < -Proj4js.common.PI) this.lon2 = this.lon2 - 2.0 * Proj4js.common.PI;
       if (this.dlon > Proj4js.common.PI) this.lon2 = this.lon2 + 2.0 * Proj4js.common.PI;
       this.dlon = this.lon1 - this.lon2;
       this.longc = .5 * (this.lon1 + this.lon2) -Math.atan(this.j * Math.tan(.5 * this.bl * this.dlon)/this.p)/this.bl;
       this.dlon  = Proj4js.common.adjust_lon(this.lon1 - this.longc);
       this.gama = Math.atan(Math.sin(this.bl * this.dlon)/this.g);
       this.alpha = Proj4js.common.asinz(this.d * Math.sin(this.gama));

       /* Report parameters common to format A
       -------------------------------------*/

       if (Math.abs(this.lat1 - this.lat2) <= Proj4js.common.EPSLN) {
          Proj4js.reportError("omercInitDataError");
          //return(202);
       } else {
          this.con = Math.abs(this.lat1);
       }
       if ((this.con <= Proj4js.common.EPSLN) || (Math.abs(this.con - HALF_PI) <= Proj4js.common.EPSLN)) {
           Proj4js.reportError("omercInitDataError");
                //return(202);
       } else {
         if (Math.abs(Math.abs(this.lat0) - Proj4js.common.HALF_PI) <= Proj4js.common.EPSLN) {
            Proj4js.reportError("omercInitDataError");
            //return(202);
         }
       }

       this.singam=Math.sin(this.gam);
       this.cosgam=Math.cos(this.gam);

       this.sinaz=Math.sin(this.alpha);
       this.cosaz=Math.cos(this.alpha);  


       if (this.lat0 >= 0) {
          this.u =  (this.al/this.bl) * Math.atan(Math.sqrt(this.d * this.d - 1.0)/this.cosaz);
       } else {
          this.u = -(this.al/this.bl) * Math.atan(Math.sqrt(this.d * this.d - 1.0)/this.cosaz);
       }
     }
  },


  /* Oblique Mercator forward equations--mapping lat,long to x,y
    ----------------------------------------------------------*/
  forward: function(p) {
    var theta;		/* angle					*/
    var sin_phi, cos_phi;/* sin and cos value				*/
    var b;		/* temporary values				*/
    var c, t, tq;	/* temporary values				*/
    var con, n, ml;	/* cone constant, small m			*/
    var q,us,vl;
    var ul,vs;
    var s;
    var dlon;
    var ts1;

    var lon=p.x;
    var lat=p.y;
    /* Forward equations
      -----------------*/
    sin_phi = Math.sin(lat);
    dlon = Proj4js.common.adjust_lon(lon - this.longc);
    vl = Math.sin(this.bl * dlon);
    if (Math.abs(Math.abs(lat) - Proj4js.common.HALF_PI) > Proj4js.common.EPSLN) {
       ts1 = Proj4js.common.tsfnz(this.e,lat,sin_phi);
       q = this.el / (Math.pow(ts1,this.bl));
       s = .5 * (q - 1.0 / q);
       t = .5 * (q + 1.0/ q);
       ul = (s * this.singam - vl * this.cosgam) / t;
       con = Math.cos(this.bl * dlon);
       if (Math.abs(con) < .0000001) {
          us = this.al * this.bl * dlon;
       } else {
          us = this.al * Math.atan((s * this.cosgam + vl * this.singam) / con)/this.bl;
          if (con < 0) us = us + Proj4js.common.PI * this.al / this.bl;
       }
    } else {
       if (lat >= 0) {
          ul = this.singam;
       } else {
          ul = -this.singam;
       }
       us = this.al * lat / this.bl;
    }
    if (Math.abs(Math.abs(ul) - 1.0) <= Proj4js.common.EPSLN) {
       //alert("Point projects into infinity","omer-for");
       Proj4js.reportError("omercFwdInfinity");
       //return(205);
    }
    vs = .5 * this.al * Math.log((1.0 - ul)/(1.0 + ul)) / this.bl;
    us = us - this.u;
    var x = this.x0 + vs * this.cosaz + us * this.sinaz;
    var y = this.y0 + us * this.cosaz - vs * this.sinaz;

    p.x=x;
    p.y=y;
    return p;
  },

  inverse: function(p) {
    var delta_lon;	/* Delta longitude (Given longitude - center 	*/
    var theta;		/* angle					*/
    var delta_theta;	/* adjusted longitude				*/
    var sin_phi, cos_phi;/* sin and cos value				*/
    var b;		/* temporary values				*/
    var c, t, tq;	/* temporary values				*/
    var con, n, ml;	/* cone constant, small m			*/
    var vs,us,q,s,ts1;
    var vl,ul,bs;
    var dlon;
    var  flag;

    /* Inverse equations
      -----------------*/
    p.x -= this.x0;
    p.y -= this.y0;
    flag = 0;
    vs = p.x * this.cosaz - p.y * this.sinaz;
    us = p.y * this.cosaz + p.x * this.sinaz;
    us = us + this.u;
    q = Math.exp(-this.bl * vs / this.al);
    s = .5 * (q - 1.0/q);
    t = .5 * (q + 1.0/q);
    vl = Math.sin(this.bl * us / this.al);
    ul = (vl * this.cosgam + s * this.singam)/t;
    if (Math.abs(Math.abs(ul) - 1.0) <= Proj4js.common.EPSLN)
       {
       lon = this.longc;
       if (ul >= 0.0) {
          lat = Proj4js.common.HALF_PI;
       } else {
         lat = -Proj4js.common.HALF_PI;
       }
    } else {
       con = 1.0 / this.bl;
       ts1 =Math.pow((this.el / Math.sqrt((1.0 + ul) / (1.0 - ul))),con);
       lat = Proj4js.common.phi2z(this.e,ts1);
       //if (flag != 0)
          //return(flag);
       //~ con = Math.cos(this.bl * us /al);
       theta = this.longc - Math.atan2((s * this.cosgam - vl * this.singam) , con)/this.bl;
       lon = Proj4js.common.adjust_lon(theta);
    }
    p.x=lon;
    p.y=lat;
    return p;
  }
};
/* ======================================================================
    projCode/lcc.js
   ====================================================================== */

/*******************************************************************************
NAME                            LAMBERT CONFORMAL CONIC

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Lambert Conformal Conic projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.


ALGORITHM REFERENCES

1.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

2.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
*******************************************************************************/


//<2104> +proj=lcc +lat_1=10.16666666666667 +lat_0=10.16666666666667 +lon_0=-71.60561777777777 +k_0=1 +x0=-17044 +x0=-23139.97 +ellps=intl +units=m +no_defs  no_defs

// Initialize the Lambert Conformal conic projection
// -----------------------------------------------------------------

//Proj4js.Proj.lcc = Class.create();
Proj4js.Proj.lcc = {
  init : function() {

    // array of:  r_maj,r_min,lat1,lat2,c_lon,c_lat,false_east,false_north
    //double c_lat;                   /* center latitude                      */
    //double c_lon;                   /* center longitude                     */
    //double lat1;                    /* first standard parallel              */
    //double lat2;                    /* second standard parallel             */
    //double r_maj;                   /* major axis                           */
    //double r_min;                   /* minor axis                           */
    //double false_east;              /* x offset in meters                   */
    //double false_north;             /* y offset in meters                   */

      if (!this.lat2){this.lat2=this.lat0;}//if lat2 is not defined
      if (!this.k0) this.k0 = 1.0;

    // Standard Parallels cannot be equal and on opposite sides of the equator
      if (Math.abs(this.lat1+this.lat2) < Proj4js.common.EPSLN) {
        Proj4js.reportError("lcc:init: Equal Latitudes");
        return;
      }

      var temp = this.b / this.a;
      this.e = Math.sqrt(1.0 - temp*temp);

      var sin1 = Math.sin(this.lat1);
      var cos1 = Math.cos(this.lat1);
      var ms1 = Proj4js.common.msfnz(this.e, sin1, cos1);
      var ts1 = Proj4js.common.tsfnz(this.e, this.lat1, sin1);

      var sin2 = Math.sin(this.lat2);
      var cos2 = Math.cos(this.lat2);
      var ms2 = Proj4js.common.msfnz(this.e, sin2, cos2);
      var ts2 = Proj4js.common.tsfnz(this.e, this.lat2, sin2);

      var ts0 = Proj4js.common.tsfnz(this.e, this.lat0, Math.sin(this.lat0));

      if (Math.abs(this.lat1 - this.lat2) > Proj4js.common.EPSLN) {
        this.ns = Math.log(ms1/ms2)/Math.log(ts1/ts2);
      } else {
        this.ns = sin1;
      }
      this.f0 = ms1 / (this.ns * Math.pow(ts1, this.ns));
      this.rh = this.a * this.f0 * Math.pow(ts0, this.ns);
      if (!this.title) this.title = "Lambert Conformal Conic";
    },


    // Lambert Conformal conic forward equations--mapping lat,long to x,y
    // -----------------------------------------------------------------
    forward : function(p) {

      var lon = p.x;
      var lat = p.y;

    // convert to radians
      if ( lat <= 90.0 && lat >= -90.0 && lon <= 180.0 && lon >= -180.0) {
        //lon = lon * Proj4js.common.D2R;
        //lat = lat * Proj4js.common.D2R;
      } else {
        Proj4js.reportError("lcc:forward: llInputOutOfRange: "+ lon +" : " + lat);
        return null;
      }

      var con  = Math.abs( Math.abs(lat) - Proj4js.common.HALF_PI);
      var ts, rh1;
      if (con > Proj4js.common.EPSLN) {
        ts = Proj4js.common.tsfnz(this.e, lat, Math.sin(lat) );
        rh1 = this.a * this.f0 * Math.pow(ts, this.ns);
      } else {
        con = lat * this.ns;
        if (con <= 0) {
          Proj4js.reportError("lcc:forward: No Projection");
          return null;
        }
        rh1 = 0;
      }
      var theta = this.ns * Proj4js.common.adjust_lon(lon - this.long0);
      p.x = this.k0 * (rh1 * Math.sin(theta)) + this.x0;
      p.y = this.k0 * (this.rh - rh1 * Math.cos(theta)) + this.y0;

      return p;
    },

  // Lambert Conformal Conic inverse equations--mapping x,y to lat/long
  // -----------------------------------------------------------------
  inverse : function(p) {

    var rh1, con, ts;
    var lat, lon;
    x = (p.x - this.x0)/this.k0;
    y = (this.rh - (p.y - this.y0)/this.k0);
    if (this.ns > 0) {
      rh1 = Math.sqrt (x * x + y * y);
      con = 1.0;
    } else {
      rh1 = -Math.sqrt (x * x + y * y);
      con = -1.0;
    }
    var theta = 0.0;
    if (rh1 != 0) {
      theta = Math.atan2((con * x),(con * y));
    }
    if ((rh1 != 0) || (this.ns > 0.0)) {
      con = 1.0/this.ns;
      ts = Math.pow((rh1/(this.a * this.f0)), con);
      lat = Proj4js.common.phi2z(this.e, ts);
      if (lat == -9999) return null;
    } else {
      lat = -Proj4js.common.HALF_PI;
    }
    lon = Proj4js.common.adjust_lon(theta/this.ns + this.long0);

    p.x = lon;
    p.y = lat;
    return p;
  }
};




/* ======================================================================
    projCode/laea.js
   ====================================================================== */

/*******************************************************************************
NAME                  LAMBERT AZIMUTHAL EQUAL-AREA
 
PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the Lambert Azimuthal Equal-Area projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE            
----------              ----           
D. Steinwand, EROS      March, 1991   

This function was adapted from the Lambert Azimuthal Equal Area projection
code (FORTRAN) in the General Cartographic Transformation Package software
which is available from the U.S. Geological Survey National Mapping Division.
 
ALGORITHM REFERENCES

1.  "New Equal-Area Map Projections for Noncircular Regions", John P. Snyder,
    The American Cartographer, Vol 15, No. 4, October 1988, pp. 341-355.

2.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.

3.  "Software Documentation for GCTP General Cartographic Transformation
    Package", U.S. Geological Survey National Mapping Division, May 1982.
*******************************************************************************/

Proj4js.Proj.laea = {
  S_POLE: 1,
  N_POLE: 2,
  EQUIT: 3,
  OBLIQ: 4,


/* Initialize the Lambert Azimuthal Equal Area projection
  ------------------------------------------------------*/
  init: function() {
    var t = Math.abs(this.lat0);
    if (Math.abs(t - Proj4js.common.HALF_PI) < Proj4js.common.EPSLN) {
      this.mode = this.lat0 < 0. ? this.S_POLE : this.N_POLE;
    } else if (Math.abs(t) < Proj4js.common.EPSLN) {
      this.mode = this.EQUIT;
    } else {
      this.mode = this.OBLIQ;
    }
    if (this.es > 0) {
      var sinphi;
  
      this.qp = Proj4js.common.qsfnz(this.e, 1.0);
      this.mmf = .5 / (1. - this.es);
      this.apa = this.authset(this.es);
      switch (this.mode) {
        case this.N_POLE:
        case this.S_POLE:
          this.dd = 1.;
          break;
        case this.EQUIT:
          this.rq = Math.sqrt(.5 * this.qp);
          this.dd = 1. / this.rq;
          this.xmf = 1.;
          this.ymf = .5 * this.qp;
          break;
        case this.OBLIQ:
          this.rq = Math.sqrt(.5 * this.qp);
          sinphi = Math.sin(this.lat0);
          this.sinb1 = Proj4js.common.qsfnz(this.e, sinphi) / this.qp;
          this.cosb1 = Math.sqrt(1. - this.sinb1 * this.sinb1);
          this.dd = Math.cos(this.lat0) / (Math.sqrt(1. - this.es * sinphi * sinphi) * this.rq * this.cosb1);
          this.ymf = (this.xmf = this.rq) / this.dd;
          this.xmf *= this.dd;
          break;
      }
    } else {
      if (this.mode == this.OBLIQ) {
        this.sinph0 = Math.sin(this.lat0);
        this.cosph0 = Math.cos(this.lat0);
      }
    }
  },

/* Lambert Azimuthal Equal Area forward equations--mapping lat,long to x,y
  -----------------------------------------------------------------------*/
  forward: function(p) {

    /* Forward equations
      -----------------*/
    var x,y;
    var lam=p.x;
    var phi=p.y;
    lam = Proj4js.common.adjust_lon(lam - this.long0);
    
    if (this.sphere) {
        var coslam, cosphi, sinphi;
      
        sinphi = Math.sin(phi);
        cosphi = Math.cos(phi);
        coslam = Math.cos(lam);
        switch (this.mode) {
          case this.EQUIT:
            y = (this.mode == this.EQUIT) ? 1. + cosphi * coslam : 1. + this.sinph0 * sinphi + this.cosph0 * cosphi * coslam;
            if (y <= Proj4js.common.EPSLN) {
              Proj4js.reportError("laea:fwd:y less than eps");
              return null;
            }
            y = Math.sqrt(2. / y);
            x = y * cosphi * Math.sin(lam);
            y *= (this.mode == this.EQUIT) ? sinphi : this.cosph0 * sinphi - this.sinph0 * cosphi * coslam;
            break;
          case this.N_POLE:
            coslam = -coslam;
          case this.S_POLE:
            if (Math.abs(phi + this.phi0) < Proj4js.common.EPSLN) {
              Proj4js.reportError("laea:fwd:phi < eps");
              return null;
            }
            y = Proj4js.common.FORTPI - phi * .5;
            y = 2. * ((this.mode == this.S_POLE) ? Math.cos(y) : Math.sin(y));
            x = y * Math.sin(lam);
            y *= coslam;
            break;
        }
    } else {
        var coslam, sinlam, sinphi, q, sinb=0.0, cosb=0.0, b=0.0;
      
        coslam = Math.cos(lam);
        sinlam = Math.sin(lam);
        sinphi = Math.sin(phi);
        q = Proj4js.common.qsfnz(this.e, sinphi);
        if (this.mode == this.OBLIQ || this.mode == this.EQUIT) {
          sinb = q / this.qp;
          cosb = Math.sqrt(1. - sinb * sinb);
        }
        switch (this.mode) {
          case this.OBLIQ:
            b = 1. + this.sinb1 * sinb + this.cosb1 * cosb * coslam;
            break;
          case this.EQUIT:
            b = 1. + cosb * coslam;
            break;
          case this.N_POLE:
            b = Proj4js.common.HALF_PI + phi;
            q = this.qp - q;
            break;
          case this.S_POLE:
            b = phi - Proj4js.common.HALF_PI;
            q = this.qp + q;
            break;
        }
        if (Math.abs(b) < Proj4js.common.EPSLN) {
            Proj4js.reportError("laea:fwd:b < eps");
            return null;
        }
        switch (this.mode) {
          case this.OBLIQ:
          case this.EQUIT:
            b = Math.sqrt(2. / b);
            if (this.mode == this.OBLIQ) {
              y = this.ymf * b * (this.cosb1 * sinb - this.sinb1 * cosb * coslam);
            } else {
              y = (b = Math.sqrt(2. / (1. + cosb * coslam))) * sinb * this.ymf;
            }
            x = this.xmf * b * cosb * sinlam;
            break;
          case this.N_POLE:
          case this.S_POLE:
            if (q >= 0.) {
              x = (b = Math.sqrt(q)) * sinlam;
              y = coslam * ((this.mode == this.S_POLE) ? b : -b);
            } else {
              x = y = 0.;
            }
            break;
        }
    }

    //v 1.0
    /*
    var sin_lat=Math.sin(lat);
    var cos_lat=Math.cos(lat);

    var sin_delta_lon=Math.sin(delta_lon);
    var cos_delta_lon=Math.cos(delta_lon);

    var g =this.sin_lat_o * sin_lat +this.cos_lat_o * cos_lat * cos_delta_lon;
    if (g == -1.0) {
      Proj4js.reportError("laea:fwd:Point projects to a circle of radius "+ 2.0 * R);
      return null;
    }
    var ksp = this.a * Math.sqrt(2.0 / (1.0 + g));
    var x = ksp * cos_lat * sin_delta_lon + this.x0;
    var y = ksp * (this.cos_lat_o * sin_lat - this.sin_lat_o * cos_lat * cos_delta_lon) + this.y0;
    */
    p.x = this.a*x + this.x0;
    p.y = this.a*y + this.y0;
    return p;
  },//lamazFwd()

/* Inverse equations
  -----------------*/
  inverse: function(p) {
    p.x -= this.x0;
    p.y -= this.y0;
    var x = p.x/this.a;
    var y = p.y/this.a;
    
    if (this.sphere) {
        var  cosz=0.0, rh, sinz=0.0;
      
        rh = Math.sqrt(x*x + y*y);
        var phi = rh * .5;
        if (phi > 1.) {
          Proj4js.reportError("laea:Inv:DataError");
          return null;
        }
        phi = 2. * Math.asin(phi);
        if (this.mode == this.OBLIQ || this.mode == this.EQUIT) {
          sinz = Math.sin(phi);
          cosz = Math.cos(phi);
        }
        switch (this.mode) {
        case this.EQUIT:
          phi = (Math.abs(rh) <= Proj4js.common.EPSLN) ? 0. : Math.asin(y * sinz / rh);
          x *= sinz;
          y = cosz * rh;
          break;
        case this.OBLIQ:
          phi = (Math.abs(rh) <= Proj4js.common.EPSLN) ? this.phi0 : Math.asin(cosz * sinph0 + y * sinz * cosph0 / rh);
          x *= sinz * cosph0;
          y = (cosz - Math.sin(phi) * sinph0) * rh;
          break;
        case this.N_POLE:
          y = -y;
          phi = Proj4js.common.HALF_PI - phi;
          break;
        case this.S_POLE:
          phi -= Proj4js.common.HALF_PI;
          break;
        }
        lam = (y == 0. && (this.mode == this.EQUIT || this.mode == this.OBLIQ)) ? 0. : Math.atan2(x, y);
    } else {
        var cCe, sCe, q, rho, ab=0.0;
      
        switch (this.mode) {
          case this.EQUIT:
          case this.OBLIQ:
            x /= this.dd;
            y *=  this.dd;
            rho = Math.sqrt(x*x + y*y);
            if (rho < Proj4js.common.EPSLN) {
              p.x = 0.;
              p.y = this.phi0;
              return p;
            }
            sCe = 2. * Math.asin(.5 * rho / this.rq);
            cCe = Math.cos(sCe);
            x *= (sCe = Math.sin(sCe));
            if (this.mode == this.OBLIQ) {
              ab = cCe * this.sinb1 + y * sCe * this.cosb1 / rho
              q = this.qp * ab;
              y = rho * this.cosb1 * cCe - y * this.sinb1 * sCe;
            } else {
              ab = y * sCe / rho;
              q = this.qp * ab;
              y = rho * cCe;
            }
            break;
          case this.N_POLE:
            y = -y;
          case this.S_POLE:
            q = (x * x + y * y);
            if (!q ) {
              p.x = 0.;
              p.y = this.phi0;
              return p;
            }
            /*
            q = this.qp - q;
            */
            ab = 1. - q / this.qp;
            if (this.mode == this.S_POLE) {
              ab = - ab;
            }
            break;
        }
        lam = Math.atan2(x, y);
        phi = this.authlat(Math.asin(ab), this.apa);
    }

    /*
    var Rh = Math.Math.sqrt(p.x *p.x +p.y * p.y);
    var temp = Rh / (2.0 * this.a);

    if (temp > 1) {
      Proj4js.reportError("laea:Inv:DataError");
      return null;
    }

    var z = 2.0 * Proj4js.common.asinz(temp);
    var sin_z=Math.sin(z);
    var cos_z=Math.cos(z);

    var lon =this.long0;
    if (Math.abs(Rh) > Proj4js.common.EPSLN) {
       var lat = Proj4js.common.asinz(this.sin_lat_o * cos_z +this. cos_lat_o * sin_z *p.y / Rh);
       var temp =Math.abs(this.lat0) - Proj4js.common.HALF_PI;
       if (Math.abs(temp) > Proj4js.common.EPSLN) {
          temp = cos_z -this.sin_lat_o * Math.sin(lat);
          if(temp!=0.0) lon=Proj4js.common.adjust_lon(this.long0+Math.atan2(p.x*sin_z*this.cos_lat_o,temp*Rh));
       } else if (this.lat0 < 0.0) {
          lon = Proj4js.common.adjust_lon(this.long0 - Math.atan2(-p.x,p.y));
       } else {
          lon = Proj4js.common.adjust_lon(this.long0 + Math.atan2(p.x, -p.y));
       }
    } else {
      lat = this.lat0;
    }
    */
    //return(OK);
    p.x = Proj4js.common.adjust_lon(this.long0+lam);
    p.y = phi;
    return p;
  },//lamazInv()
  
/* determine latitude from authalic latitude */
  P00: .33333333333333333333,
  P01: .17222222222222222222,
  P02: .10257936507936507936,
  P10: .06388888888888888888,
  P11: .06640211640211640211,
  P20: .01641501294219154443,
  
  authset: function(es) {
    var t;
    var APA = new Array();
    APA[0] = es * this.P00;
    t = es * es;
    APA[0] += t * this.P01;
    APA[1] = t * this.P10;
    t *= es;
    APA[0] += t * this.P02;
    APA[1] += t * this.P11;
    APA[2] = t * this.P20;
    return APA;
  },
  
  authlat: function(beta, APA) {
    var t = beta+beta;
    return(beta + APA[0] * Math.sin(t) + APA[1] * Math.sin(t+t) + APA[2] * Math.sin(t+t+t));
  }
  
};



/* ======================================================================
    projCode/aeqd.js
   ====================================================================== */

Proj4js.Proj.aeqd = {

  init : function() {
    this.sin_p12=Math.sin(this.lat0);
    this.cos_p12=Math.cos(this.lat0);
  },

  forward: function(p) {
    var lon=p.x;
    var lat=p.y;
    var ksp;

    var sinphi=Math.sin(p.y);
    var cosphi=Math.cos(p.y); 
    var dlon = Proj4js.common.adjust_lon(lon - this.long0);
    var coslon = Math.cos(dlon);
    var g = this.sin_p12 * sinphi + this.cos_p12 * cosphi * coslon;
    if (Math.abs(Math.abs(g) - 1.0) < Proj4js.common.EPSLN) {
       ksp = 1.0;
       if (g < 0.0) {
         Proj4js.reportError("aeqd:Fwd:PointError");
         return;
       }
    } else {
       var z = Math.acos(g);
       ksp = z/Math.sin(z);
    }
    p.x = this.x0 + this.a * ksp * cosphi * Math.sin(dlon);
    p.y = this.y0 + this.a * ksp * (this.cos_p12 * sinphi - this.sin_p12 * cosphi * coslon);
    return p;
  },

  inverse: function(p){
    p.x -= this.x0;
    p.y -= this.y0;

    var rh = Math.sqrt(p.x * p.x + p.y *p.y);
    if (rh > (2.0 * Proj4js.common.HALF_PI * this.a)) {
       Proj4js.reportError("aeqdInvDataError");
       return;
    }
    var z = rh / this.a;

    var sinz=Math.sin(z);
    var cosz=Math.cos(z);

    var lon = this.long0;
    var lat;
    if (Math.abs(rh) <= Proj4js.common.EPSLN) {
      lat = this.lat0;
    } else {
      lat = Proj4js.common.asinz(cosz * this.sin_p12 + (p.y * sinz * this.cos_p12) / rh);
      var con = Math.abs(this.lat0) - Proj4js.common.HALF_PI;
      if (Math.abs(con) <= Proj4js.common.EPSLN) {
        if (lat0 >= 0.0) {
          lon = Proj4js.common.adjust_lon(this.long0 + Math.atan2(p.x , -p.y));
        } else {
          lon = Proj4js.common.adjust_lon(this.long0 - Math.atan2(-p.x , p.y));
        }
      } else {
        con = cosz - this.sin_p12 * Math.sin(lat);
        if ((Math.abs(con) < Proj4js.common.EPSLN) && (Math.abs(p.x) < Proj4js.common.EPSLN)) {
           //no-op, just keep the lon value as is
        } else {
          var temp = Math.atan2((p.x * sinz * this.cos_p12), (con * rh));
          lon = Proj4js.common.adjust_lon(this.long0 + Math.atan2((p.x * sinz * this.cos_p12), (con * rh)));
        }
      }
    }

    p.x = lon;
    p.y = lat;
    return p;
  } 
};
/* ======================================================================
    projCode/moll.js
   ====================================================================== */

/*******************************************************************************
NAME                            MOLLWEIDE

PURPOSE:	Transforms input longitude and latitude to Easting and
		Northing for the MOllweide projection.  The
		longitude and latitude must be in radians.  The Easting
		and Northing values will be returned in meters.

PROGRAMMER              DATE
----------              ----
D. Steinwand, EROS      May, 1991;  Updated Sept, 1992; Updated Feb, 1993
S. Nelson, EDC		Jun, 2993;	Made corrections in precision and
					number of iterations.

ALGORITHM REFERENCES

1.  Snyder, John P. and Voxland, Philip M., "An Album of Map Projections",
    U.S. Geological Survey Professional Paper 1453 , United State Government
    Printing Office, Washington D.C., 1989.

2.  Snyder, John P., "Map Projections--A Working Manual", U.S. Geological
    Survey Professional Paper 1395 (Supersedes USGS Bulletin 1532), United
    State Government Printing Office, Washington D.C., 1987.
*******************************************************************************/

Proj4js.Proj.moll = {

  /* Initialize the Mollweide projection
    ------------------------------------*/
  init: function(){
    //no-op
  },

  /* Mollweide forward equations--mapping lat,long to x,y
    ----------------------------------------------------*/
  forward: function(p) {

    /* Forward equations
      -----------------*/
    var lon=p.x;
    var lat=p.y;

    var delta_lon = Proj4js.common.adjust_lon(lon - this.long0);
    var theta = lat;
    var con = Proj4js.common.PI * Math.sin(lat);

    /* Iterate using the Newton-Raphson method to find theta
      -----------------------------------------------------*/
    for (var i=0;true;i++) {
       var delta_theta = -(theta + Math.sin(theta) - con)/ (1.0 + Math.cos(theta));
       theta += delta_theta;
       if (Math.abs(delta_theta) < Proj4js.common.EPSLN) break;
       if (i >= 50) {
          Proj4js.reportError("moll:Fwd:IterationError");
         //return(241);
       }
    }
    theta /= 2.0;

    /* If the latitude is 90 deg, force the x coordinate to be "0 + false easting"
       this is done here because of precision problems with "cos(theta)"
       --------------------------------------------------------------------------*/
    if (Proj4js.common.PI/2 - Math.abs(lat) < Proj4js.common.EPSLN) delta_lon =0;
    var x = 0.900316316158 * this.a * delta_lon * Math.cos(theta) + this.x0;
    var y = 1.4142135623731 * this.a * Math.sin(theta) + this.y0;

    p.x=x;
    p.y=y;
    return p;
  },

  inverse: function(p){
    var theta;
    var arg;

    /* Inverse equations
      -----------------*/
    p.x-= this.x0;
    //~ p.y -= this.y0;
    var arg = p.y /  (1.4142135623731 * this.a);

    /* Because of division by zero problems, 'arg' can not be 1.0.  Therefore
       a number very close to one is used instead.
       -------------------------------------------------------------------*/
    if(Math.abs(arg) > 0.999999999999) arg=0.999999999999;
    var theta =Math.asin(arg);
    var lon = Proj4js.common.adjust_lon(this.long0 + (p.x / (0.900316316158 * this.a * Math.cos(theta))));
    if(lon < (-Proj4js.common.PI)) lon= -Proj4js.common.PI;
    if(lon > Proj4js.common.PI) lon= Proj4js.common.PI;
    arg = (2.0 * theta + Math.sin(2.0 * theta)) / Proj4js.common.PI;
    if(Math.abs(arg) > 1.0)arg=1.0;
    var lat = Math.asin(arg);
    //return(OK);

    p.x=lon;
    p.y=lat;
    return p;
  }
};


/**
*
* @author       :- Christopher Johnson
* @date         :- 2-Dec-2011
* @description  :- This JScript wraps an OpenLayers Layer and presents it in a form which is compatible with the nbn mapping framework
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersLayer = function(layer, options) {
	$.extend(this,
		new nbn.util.ObservableAttribute('Name',options.name || options.type), //create attributes, with defaults
		new nbn.util.ObservableAttribute('Copyright',options.copyright || '')
	);
	
	this.mappingType = options.mappingType;
	this.layer = layer;
};

/**
*
* @author		:- Christopher Johnson
* @date			:- 9-March-2011
* @description	:- This JScript will wrap up the ArcGISMap and allow it to act like a Open Map Layer 
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersImageMap = function(projectedImageMapTypeOptions, openLayersMap, options) {
	var _me = this;
	
	var _getCurrentVisibility = function() {
		return _me.getEnabled() && ($.isFunction(projectedImageMapTypeOptions.isToRender) ? projectedImageMapTypeOptions.isToRender() : true);
	};
	
	var _updateVisibility = function() {
		_me.layer.setVisibility(_getCurrentVisibility());
	};
	
	projectedImageMapTypeOptions  = $.isFunction(projectedImageMapTypeOptions) ? new projectedImageMapTypeOptions(function() {
		_updateVisibility();
		_me.layer.redraw();
	}) : projectedImageMapTypeOptions;
	
	projectedImageMapTypeOptions.tileSize = {
		height: 256,
		width: 256
	};
	
	$.extend(this,
		new nbn.util.ObservableAttribute('Name',options.name || ''), //create attributes, with defaults
		new nbn.util.ObservableAttribute('Copyright',options.copyright || ''), 
		new nbn.util.ObservableAttribute('Enabled',options.enabled || true), 
		new nbn.util.ObservableAttribute('Opacity',options.opacity || 1),
		projectedImageMapTypeOptions
	);
	
	this.layer = new OpenLayers.Layer.ZoomDelayedTMS("Name", "f", { 
		'type':'png',
		'getURL': function(bounds) {
			return projectedImageMapTypeOptions.getTileUrl({
				xmin: bounds.left,
				ymin: bounds.bottom,
				xmax: bounds.right,
				ymax: bounds.top
			}, this.map.getZoom(), {
				imageEPSG : openLayersMap.getProjection().substring(5),
				latLngEPSG: openLayersMap.getProjection().substring(5) 
			});
		},
		buffer:0,
		tileZoomDelay:1000,
		isBaseLayer: options.isBaseLayer === true,
		opacity : options.opacity,
		visibility: _getCurrentVisibility(),
		resolutions: options.resolutions
	});
	
	$.extend(this, (function() { //this object takes the form of a nbn.util.loading.LoadingConsolidator but is specifically designed to hook into the open layers controls
		var loadingObs = new nbn.util.Observable();
		var maxTileLoadingCount = 0;
		
		_me.layer.events.register('loadstart', this, function() {
			loadingObs.notifyListeners('startedLoading');
		});
		
		_me.layer.events.register('loadend', this, function() {
			loadingObs.notifyListeners('completedLoading');
			maxTileLoadingCount = 0;
		});	
		_me.layer.events.register('loadcancel', this, function() {
			loadingObs.notifyListeners('completedLoading');
			maxTileLoadingCount = 0;
		});		
		
		_me.layer.events.register('tileloaded', this, function() {
			maxTileLoadingCount = ((_me.layer.numLoadingTiles+1) > maxTileLoadingCount) ? _me.layer.numLoadingTiles+1 : maxTileLoadingCount;
			loadingObs.notifyListeners('updatedLoading',1-(_me.layer.numLoadingTiles/maxTileLoadingCount));
		});
		
		return { //adapt the observable object
			addLoadingListener: loadingObs.ObservableMethods.addListener,
			removeLoadingListener: loadingObs.ObservableMethods.removeListener
		};
	})());

	this.addEnabledUpdateListener({
		Enabled: function(newVal) {
			_updateVisibility();
		}
	});
	
	this.addOpacityUpdateListener({
		Opacity: function(newVal) {
			_me.layer.setOpacity(newVal);
		}
	});
};
/**
*
* @author       :- Christopher Johnson
* @date         :- 1-Aug-2011
* @description  :- This JScript represents a OpenLayers Bing Map in a form which is compatible with the nbn mapping framework
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersBingLayer = function(options) {
	options.mappingType = "BING"; //set the mapping type
	var _bingLayerInNBNForm = new nbn.mapping.openlayers.OpenLayersLayer(new OpenLayers.Layer.VirtualEarth(options.type, {
			type: VEMapStyle[options.type],
			isBaseLayer: true,
			sphericalMercator: true,
			animationEnabled: false,
			projection: new OpenLayers.Projection("EPSG:3857"),
			maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
			numZoomLevels:18
		}), options);
	
	$.extend(this, new nbn.layer.BaseLayer(_bingLayerInNBNForm, options)); //make this layer be an nbn base layer form of a bing map
};
/**
*
* @author		:- Christopher Johnson
* @date			:- 27-April-2011
* @description	:- This JScript is the the Open Layers underlying mapping technology implementation for the NBN Interactive Mapping Tool
* @dependencies	:-
*	OpenLayersSpatialReferenceSystemDefs.js
*	OpenLayers.js
*	virtualearth.js
*	proj4js-combined.js
*	nbn.mapping.openlayers.OpenLayersImageMap.js
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersMap = function(options, interactiveMap) {
	var EPSG_27700_RESOLUTIONS = [45074.742999999995, 22537.389, 11268.6965, 5634.3485, 2817.174, 1408.587, 704.2935, 352.147, 176.0735, 88.0365, 44.0185, 22.009, 11.005, 5.502, 2.751, 1.376, 0.688, 0.344];
	var _me = this;
	var _infoWindow, _zoomAttribute, _baseLayerAttribute;
	
	var _map = new OpenLayers.Map({
		maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
		maxResolution:156543.0339,
		units:'m',
		projection: new OpenLayers.Projection("EPSG:3857"),
		displayProjection: new OpenLayers.Projection("EPSG:4326")
	}); //create open layers map
	
	var _mappingManager = new nbn.mapping.openlayers.OpenLayersMappingLayoutManager(_map);

	$.extend(this, //extend the attribute which I wish to make public
		_zoomAttribute = new nbn.util.ObservableAttribute('Zoom',_map.getZoom()),
		_baseLayerAttribute = new nbn.util.ObservableAttribute('BaseLayer'),
		new nbn.util.HashedObservableCollection('BaseLayerType', 'ID')
	);
		
	delete this.setZoom; //delete redundant methods
	
	/*Start Defining external private methods*/	
	this._registerNBNLayersManipulation = function(nbnLayers) {
		var _setLayerIndexes = function(collection) {
			for(var i in collection) 
				_map.setLayerIndex(collection[i].layer.layer, i);
		};
		
		nbnLayers.addNBNMapLayerCollectionUpdateListener({
			add: function(collection, added) {
				_map.addLayer(added.layer.layer);
				_setLayerIndexes(collection);
			},
			remove: function(collection, removed) {
				_map.removeLayer(removed.layer.layer);
				_setLayerIndexes(collection);
			},
			reposition : function(collection, moved, position) {
				_map.setLayerIndex(moved.layer.layer, position);
			}
		});
	}

	this._registerPickingListener = function(nbnLayers) {
		var pickerController = new nbn.layer.picker.PickerInfoWindow(this, nbnLayers); //create the picker window
		var clickHandler = new OpenLayers.Handler.Click({ 'map': _map },{
			'click': function(event) {
				var lonlat = _map.getLonLatFromPixel(event.xy);
				pickerController.createOrUpdateInfoWindowIfApplicable(lonlat);
			}}
		);
		clickHandler.activate();
	};
	
	var _updateBaseLayer = function(baseLayer) {
		_map.addLayer(baseLayer.layer);
		_baseLayerAttribute.setBaseLayer(baseLayer); // update the state of the base layer attribute
	};
	
	/*Start Defining external public methods*/	
	this.setBaseLayer = function(baseLayer) {
		var centre = _map.getCenter();
		var zoom = _map.getZoom();
		var oldProjection = _map.getProjectionObject();
		_map.removeLayer(_map.baseLayer); //remove the current base layer
		_updateBaseLayer(baseLayer);
		_map.setCenter(centre.transform(oldProjection, _map.getProjectionObject()),zoom); //this line is required to fix a bug in OpenLayers 2.10 (Ticket #1249)#
	};
	
	this.closePickingDialogMapDialog = function() {//close the picking dialog if it is open
		if(_infoWindow) {
			_infoWindow.destroy();
			_infoWindow = undefined;
		}
	};
	
	this.showPickingDialogMapDialog = function(containerDiv,position) {
		this.closePickingDialogMapDialog(); //close the picking dialog if it is open
		var measurements = containerDiv.measure();
		_infoWindow = new OpenLayers.Popup.FramedCloud("_infoWindow", position, new OpenLayers.Size(300,300), '<div style="width: ' + measurements.outerWidth + 'px; height: ' + measurements.outerHeight + 'px;" id="picker_100">', undefined, true);

		_map.addPopup(_infoWindow);
		$('#picker_100').empty().append(containerDiv);
	};
	
	this.getUnderlyingMap = function() {
		return _map;
	};
	
	this.getMappingLayoutManager = function() {
		return _mappingManager;
	};
	
	this.getViewportDimensions = function() {
		var size = _map.getSize();
		return {
			height: size.h,
			width: size.w
		}
	};
	
	this.getViewportBBox = function() {
		var bbox = _map.getExtent().transform(_map.getProjectionObject(), new OpenLayers.Projection("EPSG:4326"));
		return {
			xmin: bbox.left,
			ymin: bbox.bottom,
			xmax: bbox.right,
			ymax: bbox.top
		}
	};
	
	this.initalize = function() {
		_map.render(_mappingManager.getMapDiv()[0]);
		if(!_me.hasBaseLayerType(options.baselayer)) {
			var replacementLayer = _me.getUnderlyingBaseLayerTypeArray()[0];
			interactiveMap.Logger.error('Invalid Selected Layer', 'The passed in layer : ' + options.baselayer + ' is unknown. Going to use ' + replacementLayer.getID() + ' instead.');
			_updateBaseLayer(replacementLayer);
		}
		else
			_updateBaseLayer(_me.getBaseLayerType(options.baselayer)); //set the base layer initially
		_mappingManager.append(_mappingManager.ControlsPosition.RIGHT_TOP, $('<div>').nbn_baseLayerSelector({map: _me})); //add the layer control now that the base layers have been created
	
		var extent = options.extent;
		_map.zoomToExtent(new OpenLayers.Bounds(extent.xmin, extent.ymin, extent.xmax, extent.ymax).transform(new OpenLayers.Projection("EPSG:4326"), _map.getProjectionObject()));
	};
	
	/*Add Scale widgets*/
	_mappingManager.append(_mappingManager.ControlsPosition.BOTTOM_LEFT, $('<div>').nbn_openLayersScaleWidget({openLayersMap: _map})); //pass in the open layers map of which the scale is based on
	
	this.ImageMapConstructor = nbn.mapping.openlayers.OpenLayersImageMap;
	
	/*Add event listeners*/
	this.addBaseLayerUpdateListener({
		update: function(type, baseLayer) {
			_mappingManager.setMappingForLayerType(baseLayer.mappingType); // update the mapping layout manager
		}
	});
	
	_map.events.register('zoomend', this, function() {
		_zoomAttribute.setZoom(_map.getZoom());
	});
	
	/*Define the Base layers and add them*/
	(function() {
		_me.addBaseLayerType(new nbn.mapping.openlayers.OpenLayersBingLayer({type : 'Shaded'}));
		_me.addBaseLayerType(new nbn.mapping.openlayers.OpenLayersBingLayer({type : 'Hybrid'}));
		_me.addBaseLayerType(new nbn.mapping.openlayers.OpenLayersBingLayer({type : 'Aerial'}));
		_me.addBaseLayerType((function() {
			var customOutlineLayer = new nbn.layer.ArcGISMap(nbn.util.ServerGeneratedLoadTimeConstants.gisServers, "arcgis/rest/services/general/CoastsAndVCs/MapServer/export",_me, {
				isBaseLayer: true,
				name: 'Outline',
				resolutions: EPSG_27700_RESOLUTIONS
			});
			var _filter = new nbn.layer.ArcGisMapFilter();
			_filter.setFilter({	visibleLayers:['0','1','2']	});
			customOutlineLayer.layer.projection = new OpenLayers.Projection("EPSG:27700"); //projection the layer should be added in
			customOutlineLayer.addLayerFilter(_filter);
			return new nbn.layer.BaseLayer(customOutlineLayer);
		})());
		_me.addBaseLayerType(new nbn.layer.BaseLayer(new nbn.mapping.openlayers.OpenLayersLayer(
				new OpenLayers.Layer.WMS("OS Map", nbn.util.ServerGeneratedLoadTimeConstants.gisServers + "OS-Modern", {
					layers: "MiniScale-NoGrid,OS250k,OS50k,OS25k", 
					format:"image/png"}, {
					isBaseLayer:true, 
					projection:new OpenLayers.Projection("EPSG:27700"), 
					resolutions: EPSG_27700_RESOLUTIONS
				}),{
					name: 'Ordnance Survey', 	//name displayed in the selection boxes
					copyright: '&copy; Crown copyright and database rights 2011 Ordnance Survey [100017955]' 		//Copyright statement, which gets appended to the copyright controller
				}
			),{ id:'OS' })//ID which will be used in getMapURL
		);
	})();
}
/**
*
* @author       :- Christopher Johnson
* @date         :- 24th-June-2011
* @description  :- This JScript enables controls to be drawn to specific areas on the map.
*	Elements appended through this mapping manager will be floated above the map.
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.openlayers = nbn.mapping.openlayers || {};

nbn.mapping.openlayers.OpenLayersMappingLayoutManager = function(map) {
	var _previousMappingForLayerType;
	var _positions = {}, _containers = {};
	var _elementObserver = new nbn.util.Observable();
	
	var _mapDiv;
	var _container = $('<div>').append(_mapDiv = $('<div>').css({
		height: '100%',
		width: '100%',
		position: 'absolute'
	}));

	var _createControlDiv = function(position) {
		return $('<div>')
			.addClass(_positions[position] + '_CONTAINER')
			.appendTo(_container);
	};
	
	this.append = function(position, elementToAdd) {
		if(!elementToAdd.jquery) { //if it is not a jquery object, then it is potentially a notifyable object, deal with accordingly
			_elementObserver.ObservableMethods.addListener(elementToAdd);
			elementToAdd = elementToAdd.representation;
		}
		if(_containers[position] == undefined)
			_containers[position] = _createControlDiv(position);
		elementToAdd.addClass(_positions[position] + '_WIDGET');
		_containers[position].append(elementToAdd);
	};
	
	this.setMappingForLayerType = function(type) {
		if(_previousMappingForLayerType)
			_container.removeClass(_previousMappingForLayerType); //remove all mapping for layer type classes
		if(type) //if a class exists
			_container.addClass(_previousMappingForLayerType = 'MAPPING_TYPE-' + type); //then add it
	};
	
	this.removeMappingForLayerType = function() {
		return this.setMappingForLayerType();
	};
	
	this.getLayoutContainer = function() {
		return _container;
	};
	
	this.validate = function() {
		map.updateSize();
		_elementObserver.notifyListeners('validate', _container.height(), _container.width()); //notify self positioning elements
	};
	
	this.ControlsPosition = new function() {
		var drawablePositions = ['TOP_LEFT', 'RIGHT_TOP', 'TOP_RIGHT', 'RIGHT_BOTTOM', 'BOTTOM_CENTER', 'BOTTOM_LEFT'];
		for(var i in drawablePositions) {
			var currPosition = drawablePositions[i];
			this[currPosition] = i;
			_positions[i] = currPosition; //define css name
		}
	};
	
	this.getMapDiv = function() {
		return _mapDiv;
	};
};
//this is actually SR-ORG:7094, rather than EPSG:3857, it matches the bing map better than EPSG:3857. See NBNIV-534
Proj4js.defs['EPSG:3857'] = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs";
Proj4js.defs["EPSG:27700"] = "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 +x_0=400000 +y_0=-100000 +ellps=airy +datum=OSGB36 +units=m +no_defs";
/**
*
* @author		:- Christopher Johnson
* @date			:- 25rd-May-2011
* @description	:- This JScript wraps up the logic of OpenLayers.Tile.Image and ensures that draw operations only 
*	occur a specific time after a zoom. Doing so ensures that a rapid zoom activity does not result in many map requests
*	which can add unnecessary load to the Mapping servers and get blocked by a clients browser.
* @dependencies	:-
*	OpenLayers.Tile.Image
*	nbn.util.ArrayTools
*/

OpenLayers.Tile.ZoomDelayImage = OpenLayers.Class(OpenLayers.Tile.Image, {
	draw: function() {
		var layer = this.layer;
		this.clear(); //clear the original image so that there are no tile artifacts
		nbn.util.ArrayTools.removeFrom(this, layer._toRedraw);
		
		if(layer._lastZoom !== layer.map.zoom) { //timer is not running but will be
			layer._toRedraw.push(this);
			clearTimeout(layer._timer);
			layer._timer = setTimeout(function() { //delay the loading of the tiles
				while(layer._toRedraw.length)
					OpenLayers.Tile.Image.prototype.draw.apply(layer._toRedraw.pop());
				layer._timer = undefined;
			},layer.options.tileZoomDelay);
			layer._lastZoom = layer.map.zoom;
		}
		else if(layer._timer) //timer is running
			layer._toRedraw.push(this); //add to living timer
		else //no change in zoom, this is a pan operation. Load like normal
			OpenLayers.Tile.Image.prototype.draw.apply(this);
		return true;
	},
	
	CLASS_NAME: "OpenLayers.Tile.ZoomDelayImage"
});
/**
*
* @author		:- Christopher Johnson
* @date			:- 25rd-May-2011
* @description	:- This JScript wraps up the OpenLayers.Layer.TMS logic and delates the delay logic to the ZoomDelayImage
* @dependencies	:-
*	OpenLayers.Layer.TMS
*	OpenLayers.Tile.ZoomDelayImage
*/

OpenLayers.Layer.ZoomDelayedTMS = OpenLayers.Class(OpenLayers.Layer.TMS, {
	_toRedraw:[],
	
	initialize: function(name, url, options) {
		options.tileZoomDelay = options.tileZoomDelay || 1000; //set the default tileZoom Delay
		OpenLayers.Layer.TMS.prototype.initialize.apply(this,arguments);
	},
	
	addTile : function(bounds, position) { //add a tile in the form of a ZoomDelayImage
		return new OpenLayers.Tile.ZoomDelayImage(this, position, bounds, null, this.tileSize, this.tileOptions);
	},
	
	setMap : function(map) {
		OpenLayers.Layer.TMS.prototype.setMap.apply(this,arguments);
		this._lastZoom = map.zoom; //store the last zoom
	},
	
	CLASS_NAME: "OpenLayers.Layer.ZoomDelayedTMS"
});
/**
*
* @author		:- Christopher Johnson
* @date			:- 28-July-2011
* @description	:- This JScript creates a JQuery widget styled Openlayers scale bar
* @usage		:- $('<div>').nbn_openLayersScaleWidget({openLayersMap:openLayersMap});
* @dependencies	:-
*	jquery
*	jquery.ui
*/
(function( $, undefined ) {
    $.widget( "ui.nbn_openLayersScaleWidget", {
		_create: function() {
			this._scale = new OpenLayers.Control.ScaleLine({geodesic:true}); //create a scale
			this.options.openLayersMap.addControl(this._scale); //add the scale to the map
			this.element
				.append($(this._scale.div)) //switch this widgets element to the scale div
				.addClass('ui-widget ui-widget-content ui-corner-all nbn-openLayersScaleWidget');
		},
		
		destroy: function() {
			this.element.removeClass('ui-widget ui-widget-content ui-corner-all nbn-openLayersScaleWidget');
			this.options.openLayersMap.removeControl(this._scale);
			$.Widget.prototype.destroy.apply( this, arguments ); //run the default widget distroy method
		}
    });

    $.extend( $.ui.nbn_openLayersScaleWidget, {
		version: "@VERSION"
    });

})( jQuery );
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 3rd-December-2010
* @description	:- This JScript file defines the the options which are used to create the interactive mapper widgets
* @dependencies	:-
*	mapLayer.js
*/
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.construction = nbn.mapping.construction || {};

nbn.mapping.construction.WidgetOptionsCreator = function(interactiveMapper){
	this._map = interactiveMapper;
	var _layerHelper = this._layerHelper = new nbn.mapping.construction.LayerHelper(interactiveMapper);

	function _createNamedFilter(name, layers, initialState) {
		return { name: name, filter: (function(){
			var toReturn = new nbn.layer.SwitchableArcGisMapFilter(initialState & true === true);
			toReturn.setFilter({visibleLayers: ($.isArray(layers)) ? layers : [layers]});
			return toReturn;
		})() };
	};
	
    var _createZoomFilters = function() {
		return [_createNamedFilter('10km', '3'),_createNamedFilter('2km', '2'),_createNamedFilter('1km', '1'),_createNamedFilter('100m', '0'),{
                name: 'Default',
                filter: function() {
                    var zoomFilter = new nbn.layer.SwitchableArcGisMapFilter(true);
                    zoomFilter.setFilter(function(zoom) {
                    var hundredMAppear = 13;
                    var oneKAppear = 10;
                    var twoKAppear = 8;
                    if((zoom >= twoKAppear ) && (zoom < oneKAppear))
                        return {visibleLayers: ['2']}
                    else if((zoom >= oneKAppear) && (zoom < hundredMAppear))
                        return {visibleLayers: ['1']}
                    else if(zoom >= hundredMAppear)
                        return {visibleLayers: ['0']}
                    else
                        return {visibleLayers: ['3']}
                    });
                    return zoomFilter;
                }()
            }];
    };

	function _createPolygonFilters() {
		return [_createNamedFilter('Polygons', '5', true), _createNamedFilter('Markers', '4', true)];
	};
	
    this.createNBNSpeciesLayer = function(options) {
        var speciesMapLayer = _createFilteringMapLayer(options);
        var zoomFilters = _createZoomFilters();
		var polygonFilters = _createPolygonFilters();
        for(var i in zoomFilters) speciesMapLayer.addLayerFilter(zoomFilters[i].filter);
        for(var i in polygonFilters) speciesMapLayer.addLayerFilter(polygonFilters[i].filter);
        speciesMapLayer.addLayerFilter(interactiveMapper.LoginFilter);
        var layerToReturn = {layer: speciesMapLayer};
        
		layerToReturn.view = _createSpeciesLayerBox(layerToReturn, zoomFilters, polygonFilters, options);
		layerToReturn.picker = new nbn.layer.picker.SpeciesLayerPicker(speciesMapLayer);

        return layerToReturn;
    };

    var _createFilteringMapLayer = function(options) {
        options = $.extend({
            opacity: 1,
            name: 'Species'
        }, options);
        
        options.logger = interactiveMapper.Logger; //force the logger to be the one which created this map layer
	return new nbn.layer.SpeciesLayer(nbn.util.ServerGeneratedLoadTimeConstants.gisServers, interactiveMapper.Map, options);
    };

    var _createYearFilter = function(temporalFilter) {
        var startDate = 1799, endDate = nbn.util.ServerGeneratedLoadTimeConstants.date.year;
		var currFilter = temporalFilter.getFilter();
		var currStartDate = (temporalFilter.isFiltering() && currFilter.filters.startyear) ? currFilter.filters.startyear : startDate;
		var currEndDate = (temporalFilter.isFiltering() && currFilter.filters.endyear) ? currFilter.filters.endyear : endDate;
		
		var slider = $('<div>').slider({ //create the date range slider
			range: true,
			min: startDate,
			max: endDate,
			values: [ currStartDate, currEndDate ],
			slide: function(event, ui) {
				if(ui.values[0] != startDate || ui.values[1] != endDate) {
					temporalFilter.setFilter({
						filters: {
							startyear: ui.values[0],
							endyear: ui.values[1]
						}
					});
				}
				else
					temporalFilter.clearFilter();
			}
		});
		
		temporalFilter.addFilterUpdateListener({ //add a listener to the filter and update the slider when nessersary
			Filter: function(val) {
				slider.slider('values',0,(val && val.filters.startyear) ? val.filters.startyear : startDate);
				slider.slider('values',1,(val && val.filters.endyear) ? val.filters.endyear : endDate);
			}
		});
		
        return $('<div style="padding-bottom: 15px;">')
            .append($('<div>').nbn_filterStatus({
				statusFunction : function() {
					return ((this.filters.startyear == startDate) ? 'pre ' : '') + (this.filters.startyear || startDate) +  " - " + (this.filters.endyear || endDate);
				},
				layerFilter:temporalFilter,
				inactiveFilteringLabel:'All Years'
			}))
            .append(slider);
    };

    var _createVisibleLayersControl = function(zoomFilters) {
        var toReturn = $('<table>');
        var row1 = $('<tr>');
        for(var i in zoomFilters) {
            $('<td>')
                .text(zoomFilters[i].name)
                .appendTo(row1);
        }
        row1.appendTo(toReturn);
        var row2 = $('<tr>');
        for(var i in zoomFilters) {
            $('<td>')
                .append(_layerHelper.createCheckboxControl(zoomFilters[i].filter))
                .appendTo(row2);
        }
        row2.appendTo(toReturn);
        return toReturn;
    };

	function _createAbundanceLabeledContent(filter) {
		var content = $('<table><tr><td>All</td><td>Presence</td><td>Absence</td></tr></table>'); //create the table with headers
		var uniqueRadioButtonID = nbn.util.IDTools.generateUniqueID();
		
		function createRadioButtonTableEntry(abundanceValueToSetOnSelect, selected) {
			return $('<td>').append($('<input type="radio"' + ((selected) ? 'checked="checked"' :'') + '>').attr('value',abundanceValueToSetOnSelect).attr('name',uniqueRadioButtonID).click(function() {
				filter.setFilter({filters:{abundance: abundanceValueToSetOnSelect}}); //on click set the new filter value
			}));
		}

		content.append($('<tr>')
			.append(createRadioButtonTableEntry("all", true))
			.append(createRadioButtonTableEntry("presence"))
			.append(createRadioButtonTableEntry("absence"))
		);
		
		var toReturn = _layerHelper.createLabeledContent('Abundance: ',content); //create the labled content
		
		function setVisibilityOfAbundanceFilter(visible) { toReturn[(visible) ? 'show' : 'hide'].call(toReturn); }//set the visiblity of the abundance filter
		setVisibilityOfAbundanceFilter(filter.getEnabled());
		filter.addEnabledUpdateListener({Enabled:setVisibilityOfAbundanceFilter});
		return $('<div>').append(toReturn); //wrap up so that show/ hide functionality works
	};
	
    var _createSpeciesLayerBox = function(nbnLayer, zoomFilters, polygonFilters, options) {	
        var _createSpeciesAppliedFilters = function(layer, zoomFilters) {
		
			var _gridVisibleLayersBox = _layerHelper.createLabeledContent('Visible Layers: ',_createVisibleLayersControl(zoomFilters)).attr('advancedControl','true');
            			
			return $('<div>')
                .append($('<div>') //filter species status
                    .append(_layerHelper.createLabeledContent('Summary: ',$('<div>')
                        .nbn_autosize()
                        .nbn_label({
                            label: layer,
                            type:'Description',
                            contentchange: function() {$(this).nbn_autosize('resize');}
                        }))
                    )
                )
                .append(_layerHelper.createLabeledContent('Years: ',_createYearFilter(layer.YearFilter)).attr('advancedControl','true'))
                .append(_layerHelper.createLabeledContent('Polygon Records: ', _createVisibleLayersControl(polygonFilters)).attr('advancedControl','true'))
                .append(_createAbundanceLabeledContent(layer.AbundanceFilter).attr('advancedControl','true'))
                .append(_gridVisibleLayersBox);
        };
        var _speciesDialog = _createSpeciesDialog(nbnLayer.layer, options);
        var _datasetsUsedDialog = _createDatasetInfoDialog(nbnLayer.layer); //the map to create the datasets used for dialog
        return _layerHelper.createNBNLayerBox($('<div>').append(_createSpeciesAppliedFilters(nbnLayer.layer, zoomFilters)), nbnLayer, {
                "Dataset Info" : function() {_datasetsUsedDialog.dialog('open');},
                "Species Selector" : function(){_speciesDialog.dialog("open");}
            });
    };

    var _createSpeciesDialog = function(layer, options) {
        var singleSpecies = function(renderableToControl) {
            var _me = this, _selectedSpecies, _selectedDatasets = [], _datasetSelectionBox, _singleSpeciesDatasetSelectionTree;
            var _setSelectedSpecies = function(selection) {
                    if(_selectedSpecies = selection) {//save the species selection
                            var currUser = interactiveMapper.getUser();//get the current user
                            _singleSpeciesDatasetSelectionTree.nbn_treewidget('setUrlOfDescriptionFile','TreeWidgetGenerator?type=spd&tvk=' + _selectedSpecies.taxonVersionKey + '&user=' + ((currUser) ? currUser.userID : 0));
                            _datasetSelectionBox.show(); //show the selection box
                    }
                    _selectedDatasets=[];
                    renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            var _setSelectedSpeciesDatasets = function(selected) {
                    _selectedDatasets = [];
                    if(!_singleSpeciesDatasetSelectionTree.nbn_treewidget('isFullyChecked')) {
                        for(var currentDatasetKey in selected){
                                _selectedDatasets.push({
                                        datasetKey:selected[currentDatasetKey],
                                        name: _singleSpeciesDatasetSelectionTree.nbn_treewidget('getChildUserData', selected[currentDatasetKey],'name')
                                });
                        }
                    }
                    renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            var _speciesAutoComplete = $('<input>')
                    .addClass("nbn-autocomplete")
                    .autocomplete({
                            source : 'TaxonSearch',
                            select: function(event, ui) {
                                    _setSelectedSpecies({
                                            taxonVersionKey: ui.item.key,
                                            name: ui.item.name
                                    });
                            }
                    });
            _speciesAutoComplete.data( "autocomplete" )._renderItem = function(ul, item) {
                    return $( "<li></li>" )
                            .data( "item.autocomplete", item )
                            .append( "<a><strong>" + item.value + "</strong><br>" + item.authority + ", " + item.taxonRank + ", " + item.taxonGroup + "</a>" )
                            .appendTo(ul);
            };

            var _speciesTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=sp',
                    allowMultipleSelection: 'none',
                    selected: function(event, selected) {
                            _setSelectedSpecies({
                                    taxonVersionKey: selected,
                                    name: $(this).nbn_treewidget('getChildText', selected)
                            });
                    }
            });

            _singleSpeciesDatasetSelectionTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=spd&tvk=NBNSYS0000005629&user=0',
                    allowMultipleSelection: 'checkbox',
                    selectDeselect: true,
                    childrenSelectionListener: function(event, selected) {
                        _setSelectedSpeciesDatasets(selected);
                    },
                    loaded: function() {
                        _setSelectedSpeciesDatasets($(this).nbn_treewidget("getAllChildrenChecked"));
                    }
            });

            var _content = $('<div>')
                    .append($('<span>')
                            .append('Search for species:')
                            .append(_speciesAutoComplete)
                    )
                    .append(_speciesTree)
                    .append(_datasetSelectionBox = $('<div>')
                            .hide() //tree box hidden by default
                            .append($('<div>').html('Datasets:'))
                            .append(_singleSpeciesDatasetSelectionTree)
                    );


            this.isRenderable = function() {
                    return _selectedSpecies != undefined && (_selectedDatasets.length > 0 || _singleSpeciesDatasetSelectionTree.nbn_treewidget('isFullyChecked'));
            };

            this.getRepresentation = function() {
                    return _content;
            };

            this.reset = function() {
                    _setSelectedSpecies(undefined); //set no selected species
            };

            this.apply = function() {
                    layer.setMode(layer.Modes.SPECIES,_selectedSpecies,_selectedDatasets);
            };

            this.getState = function() {
            };

            this.setState = function(state) {
            };
        };

        var dataset = function(renderableToControl) {
            var _selectedDataset;
            var _datasetMetadata = $('<div>').nbn_datasetmetadata();
			
            var _setSelectedDataset = function(selection) {
                    _selectedDataset = selection//save the species selection
                    _datasetMetadata.nbn_datasetmetadata('setDataset', _selectedDataset.datasetKey + '');
                    renderableToControl.setRenderable(_selectedDataset != undefined); //is this map now renderable?
            };

            var _singleDatasetSelectionAutocomplete = $('<input>')
                    .addClass("nbn-autocomplete")
                    .autocomplete({
                            source : 'DatasetSearch',
                            select: function(event, ui) {
                                    _setSelectedDataset({
                                            datasetKey: ui.item.key,
                                            name: ui.item.name
                                    });
                            }
                    });

            var _singleDatasetSelectionTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=ds',
                    allowMultipleSelection: 'none',
                    selected: function(event, selected) {
                                    _setSelectedDataset({
                                            datasetKey: selected,
                                            name: $(this).nbn_treewidget('getChildUserData', selected, 'name')
                                    });
                            }
                    });
			
            var _content = $('<div>')
                    .append($('<span>')
                            .html('Select a dataset:')
                            .append(_singleDatasetSelectionAutocomplete)
                    )
                    .append(_singleDatasetSelectionTree)
                    .append(_datasetMetadata);

            this.isRenderable = function() {
                    return _selectedDataset != undefined;
            };

            this.getRepresentation = function() {
                    return _content;
            };

            this.reset = function() {
                    _setSelectedDataset(undefined); //set no selected species
            };

            this.apply = function() {
                    layer.setMode(layer.Modes.SINGLE_DATASET,_selectedDataset);
            };

            this.getState = function() {
            };

            this.setState = function(state) {
            };
        };

        var designation = function(renderableToControl) {
            var _me = this, _selectedDesignation, _selectedDatasets=[], _datasetSelectionBox, _designationDatasetsTree;

            var _setSelectedDesignation = function(selection) {
                    if(_selectedDesignation = selection) {//save the species selection
                            var currUser = interactiveMapper.getUser();//get the current user
                            _designationDatasetsTree.nbn_treewidget('setUrlOfDescriptionFile','TreeWidgetGenerator?type=dd&desig=' + _selectedDesignation.designationKey + '&user=' + ((currUser) ? currUser.userID : 0));
                            _datasetSelectionBox.show();
                    }
                    _selectedDatasets=[];
                    renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            var _setSelectedDesignationDatasets = function(selected) {
                _selectedDatasets = [];
                if(!_designationDatasetsTree.nbn_treewidget('isFullyChecked')) {
                    for(var currentDatasetKey in selected){
                            _selectedDatasets.push({
                                    datasetKey:selected[currentDatasetKey],
                                    name: _designationDatasetsTree.nbn_treewidget('getChildUserData', selected[currentDatasetKey], 'name')
                            });
                    }
                }
                renderableToControl.setRenderable(_me.isRenderable()); //is this map now renderable?
            };

            _designationDatasetsTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=dd&desig=BIRDSDIR-A1&user=0',
                    allowMultipleSelection: 'checkbox',
                    selectDeselect: true,
                    childrenSelectionListener: function(event, selected) {
                        _setSelectedDesignationDatasets(selected);
                    },
                    loaded: function() {
                        _setSelectedDesignationDatasets($(this).nbn_treewidget("getAllChildrenChecked"));
                    }
            });

            var _designationTree = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=d',
                    allowMultipleSelection: 'none',
                    selected: function(event, selected) {
                            _setSelectedDesignation({
                                    designationKey: selected,
                                    name: $(this).nbn_treewidget('getChildText', selected)
                            });
                    }
            });

            var _content = $('<div>')
                    .append('Select a designation:')
                    .append(_designationTree)
                    .append(_datasetSelectionBox = $('<div>')
                            .hide() //tree box hidden by default
                            .append($('<div>').html('Datasets:'))
                            .append(_designationDatasetsTree)
                    );

            this.isRenderable = function() {
                    return _selectedDesignation != undefined && (_selectedDatasets.length > 0 || _designationDatasetsTree.nbn_treewidget('isFullyChecked'));
            };

            this.getRepresentation = function() {
                    return _content;
            };

            this.reset = function() {
                    _setSelectedDesignation(undefined); //set no selected species
            };

            this.apply = function() {
                    layer.setMode(layer.Modes.DESIGNATION,_selectedDesignation, _selectedDatasets);
            };

            this.getState = function() {
            };

            this.setState = function(state) {
            };
        };
	return $('<div>').nbn_renderableControlDialog({
            width: 600,
            height: 600,
            renderableControl: new function() {
                var _me = this;
                $.extend(this, new nbn.util.ObservableAttribute('Renderable',false));

                var _none = {
                    isRenderable: function() {
                        return true;
                    },
                    apply: function() {
                        layer.setMode(layer.Modes.NONE);
                    }
                };
                var _dataset = new dataset(_me);
                var _species = new singleSpecies(_me);
                var _designation = new designation(_me);

                var _selectedWindow = _none;
                var _setSelected = function(newSelection) {
                    _selectedWindow = newSelection;
                    _me.setRenderable(newSelection.isRenderable());
                };

                var content = $('<div>').nbn_selectionPane({
                    label: 'Choose a type of map:',
                    initial: "None",
                    selectables: {
                        "None" : {
                            selected: function() {
                                _setSelected(_none);
                            }
                        },
                        "Single Species" : {
                            content: _species.getRepresentation(),
                            selected: function() {
                                _setSelected(_species);
                            }
                        },
                        "Dataset" : {
                            content: _dataset.getRepresentation(),
                            selected: function() {
                                _setSelected(_dataset);
                            }
                        },
                        "Designation" : {
                            content: _designation.getRepresentation(),
                            selected: function() {
                                _setSelected(_designation);
                            }
                        }
                    }
                });

                this.getRepresentation = function() {return content;};
                this.reset = function() {content.nbn_selectionPane('setSelected','None');};
                this.apply = function() {_selectedWindow.apply();};

                this.getState = function() {
                    return {
                        currMode: content.nbn_selectionPane('getSelected'),
                        datasetWindow: _dataset.getState(),
                        speciesWindow: _species.getState(),
                        designationWindow: _designation.getState()
                    };
                };

                this.setState = function(state) {
                    _dataset.setState(state.datasetWindow);
                    _species.setState(state.speciesWindow);
                    _designation.setState(state.designationWindow);
                    content.nbn_selectionPane('setSelected', state.currMode);
                };
            },
            title: 'Species Selector',
            resetButtonText: 'Clear Layer',
			autoOpen: options.configureOnLoad
        });
    };

    var _createDatasetInfoDialog = function(nbnMapLayer) {
        var _organisationNameSortFunction = function(a,b) {
            var x = a.name, y = b.name;
            return ((x < y) ? -1 : ((x > y) ? 1 : 0));
        };

        var _getDatasetProviderRecordCount = function(datasetProvider) {
            var toReturn = 0;
            for(var i in datasetProvider.datasets)
                toReturn += datasetProvider.datasets[i].contextRecordCount || datasetProvider.datasets[i].recordCount;
            return toReturn;
        };

        var _recordCountSortFunction = function(a,b) {
            return _getDatasetProviderRecordCount(b) - _getDatasetProviderRecordCount(a);
        };

        var _renderDatasetBoolean = function(value) {
            var toReturn = $('<span>');
            if(value)
                toReturn.append($('<span>').addClass('ui-icon ui-icon-check'));
            return toReturn;
        };

        var datasetsSelectedTable = function() {
            return $('<ul>').addClass('dataset-provider-list').nbn_list({
            sortFunction: _recordCountSortFunction,
            elementRenderFunction: function(datasetProviderData) {
                var organisationProviderTitle = $('<div>').addClass('nbn-dataset-provider');
                if(datasetProviderData.imageUrl) {
                    organisationProviderTitle.append($('<span>')
                        .css('backgroundImage', 'url(' + datasetProviderData.imageUrl + ')')
                        .addClass('nbn-dataset-provider-logo')
                    );
                }
                organisationProviderTitle.append($('<a>')
                    .html(datasetProviderData.name)
                    .attr('href','http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=' + datasetProviderData.organisationKey)
                    .addClass('nbn-dataset-provider-title')
                )

                return $('<div>')
                    .append(organisationProviderTitle)
                    .append($('<ul>').addClass('dataset-list').nbn_list({
                        elementRenderFunction: function(datasetData) {
							return $('<a>')
                                .addClass('dataset-name')
                                .html(datasetData.name)
                                .attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetData.datasetKey)
                                .attr('target','_blank')
                                .add($('<span>').addClass("dataset-resolution").html(datasetData.datasetResolution))
                                .add($('<span>').addClass("dataset-userResolution").html((datasetData.hasFullAccess) ? "Full" : datasetData.userResolution))
                                .add(_renderDatasetBoolean(datasetData.sensitiveAccess).addClass('dataset-sensitiveAccess'))
                                .add(_renderDatasetBoolean(datasetData.downloadRawData).addClass('dataset-downloadRawData'))
                                .add(_renderDatasetBoolean(datasetData.viewAttributes).addClass('dataset-viewAttributes'))
                                .add(_renderDatasetBoolean(datasetData.viewRecorder).addClass('dataset-viewRecorder'));
                        },
                        data: datasetProviderData.datasets
                    }));
            }
        })};

        var datasetsNotSelectedTable = function() {
            return $('<ul>').addClass('dataset-provider-list').nbn_list({
            sortFunction: _recordCountSortFunction,
            elementRenderFunction: function(datasetProviderData) {
                if(datasetProviderData){
                    var organisationProviderTitle = $('<div>').addClass('nbn-dataset-provider').addClass('datasetlist').addClass('ui-widget');
                    if(datasetProviderData.imageUrl) {
                        organisationProviderTitle.append($('<span>')
                            .css('backgroundImage', 'url(' + datasetProviderData.imageUrl + ')')
                            .addClass('nbn-dataset-provider-logo')
                        );
                    }
                    organisationProviderTitle.append($('<a>')
                        .html(datasetProviderData.name)
                        .attr('href','http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=' + datasetProviderData.organisationKey)
                        .addClass('nbn-dataset-provider-title')
                    )
                    return $('<div>')
                        .append(organisationProviderTitle)
                        .append($('<ul>').addClass('dataset-list').nbn_list({
                            elementRenderFunction: function(datasetData) {
                                return $('<a>')
                                    .addClass('dataset-name')
                                    .html(datasetData.name)
                                    .attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetData.datasetKey)
                                    .attr('target','_blank')
                                    .add($('<span>').addClass("dataset-resolution").html(datasetData.datasetResolution))
                                    .add($('<span>').addClass("dataset-userResolution").html((datasetData.hasFullAccess) ? "Full" : datasetData.userResolution))
                                    .add(_renderDatasetBoolean(datasetData.sensitiveAccess).addClass('dataset-sensitiveAccess'))
                                    .add(_renderDatasetBoolean(datasetData.downloadRawData).addClass('dataset-downloadRawData'))
                                    .add(_renderDatasetBoolean(datasetData.viewAttributes).addClass('dataset-viewAttributes'))
                                    .add(_renderDatasetBoolean(datasetData.viewRecorder).addClass('dataset-viewRecorder'));
                            },
                            data: datasetProviderData.datasets
                        }));
                }
            }
        })};
        
        var datasetsNotViewableTable = function() {
            return $('<ul>').addClass('dataset-provider-list').nbn_list({
            sortFunction: _recordCountSortFunction,
            elementRenderFunction: function(datasetProviderData) {
                var organisationProviderTitle = $('<div>').addClass('nbn-dataset-provider');
                if(datasetProviderData.imageUrl) {
                    organisationProviderTitle.append($('<span>')
                        .css('backgroundImage', 'url(' + datasetProviderData.imageUrl + ')')
                        .addClass('nbn-dataset-provider-logo')
                    );
                }
                organisationProviderTitle.append($('<a>')
                    .html(datasetProviderData.name)
                    .attr('href','http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=' + datasetProviderData.organisationKey)
                    .addClass('nbn-dataset-provider-title')
                )
                return $('<div>')
                    .append(organisationProviderTitle)
                    .append($('<ul>').addClass('dataset-list').nbn_list({
                        elementRenderFunction: function(datasetData) {
                            return $('<a>')
                                .addClass('dataset-name')
                                .html(datasetData.name)
                                .attr('href', 'http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=' + datasetData.datasetKey)
                                .attr('target','_blank');
                        },
                        data: datasetProviderData.datasets
                    }));
            }
        })};

        var _createSortByButtonSet = function(sortableTables) {
            return $('<div class="dataset-list-sortByControl" id="radio">')
                .append($('<span>')
                    .html('Sort By :')
                    .addClass('dataset-list-sortByControl-label')
                )
                .append('<input type="radio" id="nbn-datasetNoOfRecordsSortBtn" name="nbn-datasetSortButton" checked="checked"/><label for="nbn-datasetNoOfRecordsSortBtn">Number of records</label>')
                .append('<input type="radio" id="nbn-datasetAlphaSortBtn" name="nbn-datasetSortButton"/><label for="nbn-datasetAlphaSortBtn">Organisation name</label>')
                .buttonset()
                .change(function(){
                    var selected = $(':checked',this).attr('id');
                    if(selected == 'nbn-datasetNoOfRecordsSortBtn'){
                        $.each(sortableTables,function(){
                            this.nbn_list('setSortFunction',_recordCountSortFunction);
                        });
                    }else if(selected == 'nbn-datasetAlphaSortBtn'){
                              $.each(sortableTables,function(){
                                   this.nbn_list('setSortFunction',_organisationNameSortFunction);
                        });
                    }
                });
        };

        var headerDatasetsSelected = $('<div class="ui-widget-header">')
            .append($('<span>').addClass('dataset-name-heading').html('Datasets you have selected'))
            .append($('<span>').addClass('dataset-resolution-heading').html('Dataset Resolution'))
            .append($('<span>').addClass('dataset-userResolution-heading').html('Your Resolution'))
            .append($('<span>').addClass('dataset-sensitiveAccess-heading').html('Sensitive Access'))
            .append($('<span>').addClass('dataset-downloadRawData-heading').html('Download Raw Data'))
            .append($('<span>').addClass('dataset-viewAttributes-heading').html('View Attributes'))
            .append($('<span>').addClass('dataset-viewRecorder-heading').html('View Recorder'));

        var headerDatasetsNotSelected = $('<div class="ui-widget-header">')
            .append($('<span>').addClass('dataset-name-heading').html('Datasets you have not selected'))
            .append($('<span>').addClass('dataset-resolution-heading').html('Dataset Resolution'))
            .append($('<span>').addClass('dataset-userResolution-heading').html('Your Resolution'))
            .append($('<span>').addClass('dataset-sensitiveAccess-heading').html('Sensitive Access'))
            .append($('<span>').addClass('dataset-downloadRawData-heading').html('Download Raw Data'))
            .append($('<span>').addClass('dataset-viewAttributes-heading').html('View Attributes'))
            .append($('<span>').addClass('dataset-viewRecorder-heading').html('View Recorder'));

        var headerDatasetsNotViewable = function(){
                return $('<div class="ui-widget-header">')
                    .empty()
                    .append($('<span>').addClass('dataset-heading datasetName')).html('Datasets you don\'t have access to. ')
                    .html(
                    (interactiveMapper.getUser()) ?
                        'Datasets you don\'t have access to. To request access, click on them and look for \'Apply for access\'.'
                    :
                        'Datasets you don\'t have access to. You will need to login to apply for access to them.'
                    )
        };

        var tabContainerDiv;
        
        var addDynamicTab = function(tabName, contentHeader, contentData){
            tabContainerDiv.nbn_dynamictabs('add',tabName,
                $('<div>')
                    .addClass('dataset-list-container')
                    .append(contentHeader)
                    .append(contentData)
            );
        }

        return $('<div>')
            .addClass('dataset-dialog')
            .dialog({
                title: 'Datasets',
                autoOpen: false,
                modal: true,
                resizable: false,
                width: 830,
                height: 590,
                open: function() { //on open refresh
                    var _me = $(this);
                    _me.empty();
                    var loadingDiv = $('<div>').addClass('loading');
                    _me.append(loadingDiv);
                    $.getJSON('OrganisationAcknowledgementServlet' + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(nbnMapLayer.getNBNSpeciesLayerFilters()),'&', '?'), function(response) {
                        var dataDiv = $('<div>')
                        dataDiv.append(tabContainerDiv = $('<div>').nbn_dynamictabs());
                        _me.append($('<div>')
                            .addClass('nbn-datasetmetadata-caveat')
                            .append($('<div>')
                              .addClass('nbn-datasetmetadata-caveat-header')
                              .html("IMPORTANT NOTICE TO THOSE INTENDING TO USE THESE DATA FOR RESEARCH, PLANNING OR LAND MANAGEMENT PURPOSES")
                            ).append($('<div>')
                              .addClass('nbn-datasetmetadata-caveat-text')
                              .html("You may not have full access to all the datasets relating to this query. Your current level of access can be identified in the list provided below.  You are STRONGLY ADVISED to seek improved access by selecting the relevant datasets below and then clicking to request improved access.  Please ensure that your use of these data complies with the NBN Gateway <a href=\"/help/popups/generalTerms.jsp\" target=\"_blank\">Terms and Conditions</a>")
                            )
                        );
                        _me.append(dataDiv);
                    var sortableTables = new Array();
                        if(response){
                            if(response.datasetsUnviewable){
                                sortableTables.push(datasetsNotViewableTable().nbn_list('setData',response.datasetsUnviewable));
                                addDynamicTab(
                                    'Datasets Not Available',
                                    headerDatasetsNotViewable(),
                                    sortableTables[sortableTables.length - 1]
                                );
                            }
                            if(response.datasetsNotUsed){
                                sortableTables.push(datasetsNotSelectedTable().nbn_list('setData',response.datasetsNotUsed));
                                addDynamicTab(
                                    'Datasets Not Selected',
                                    headerDatasetsNotSelected,
                                    sortableTables[sortableTables.length - 1]
                                );
                            }
                            if(response.datasetsUsed){
                                sortableTables.push(datasetsSelectedTable().nbn_list('setData',response.datasetsUsed));
                                addDynamicTab(
                                    'Datasets Selected',
                                    headerDatasetsSelected,
                                    sortableTables[sortableTables.length - 1]
                                );
                            }
                            loadingDiv.fadeOut('slow');
                            tabContainerDiv.tabs();
                            _me.append(_createSortByButtonSet(sortableTables));
                        }else{
                            loadingDiv.fadeOut('slow');
                            _me.append($('<div>').html('<h2>You don\'t currently have any species data on the map.</h2>'));
                        }
                    });
                }
            });
    };

};
window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.construction = nbn.mapping.construction || {};

nbn.mapping.construction.LayerHelper = function(interactiveMapper) {
	var _me = this;
    var _createLayerControls = function(layerToControl) { //gives basic idea, needs a clean up really
        var slider = $('<div>').addClass('nbn-opacitySlider').slider({ //create the opacity slider
			min:0,
			max: 1,
			step: 0.01,
			value: layerToControl.getOpacity(),
			slide: function(event, ui) {
				layerToControl.setOpacity(ui.value);
			}
		});
		
		layerToControl.addOpacityUpdateListener({
			Opacity: function(val) {
				slider.slider('value',val);
			}
		});
		
		return $('<div advancedControl="true">')
            .append(_me.createLabeledContent('Opacity:', slider))
            .append(_me.createLabeledContent('Visiblity :', _me.createCheckboxControl(layerToControl)));
    };
		
    this.createCheckboxControl = function(toControl) {
        return $('<input type="checkbox">')
            .prop('checked', toControl.getEnabled()) //set the value initally
            .change(function() {
                toControl.setEnabled($(this).is(':checked'));
            });
    };

    this.createNBNLayerBox = function(toLayerBox, nbnLayer, buttons) {
		var layer = nbnLayer.layer;
        layer.addNameUpdateListener({ //liste to name changes
            Name: function(newName) {toLayerBox.nbn_statefulbox('setTitle', newName);}
        });
        return toLayerBox
            .prepend($('<div>')
                .nbn_autosize()
                .nbn_legend({
                    layer: layer,
                    contentchange: function() {$(this).nbn_autosize('resize');}
                })
            )
            .append(_createLayerControls(layer))
            .nbn_statefulbox({
                title: layer.getName(),
                createAdvancedButton: true,
				createDestructionButton : true,
				destroyFunc: function() {interactiveMapper.removeNBNMapLayer(nbnLayer);},
                buttons: buttons
            });
    };

    this.createLabeledContent = function(label, content) {
        return $('<div>')
            .addClass('nbn-labeledContent')
            .append($('<div>')
                .addClass('nbn-label')
                .html(label)
            )
            .append($('<div>')
                .addClass('nbn-content')
                .append(content)
            );
    };
	
	this.createMapLayer = function(toCreate, options, name) {
        options = $.extend({
            opacity: 0.6,
            name: name
        }, options);
        options.logger = interactiveMapper.Logger; //force the logger to be the one which created this map layer
		return new toCreate(nbn.util.ServerGeneratedLoadTimeConstants.gisServers,interactiveMapper.Map, options);
    };
}
nbn.mapping.construction.WidgetOptionsCreator.prototype.createCopyrightWidget = function() {
	return $('<div>').nbn_copyrightController({map:this._map});
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createLayersCreatorBox = function() {
	var _me = this;
	
	function _createLayerIcon(type, dialog) {
		return $('<div>').nbn_icon({label: type, icon: type})
			.click(function() {
				_me._map.addNBNMapLayer(_me['createNBN' + type + 'Layer']({configureOnLoad: true}));
				dialog.dialog('destroy');
			});
	}
	
	var _dialog = $('<div>').addClass('nbn-layersCreator')
		.append($('<h3>').html('What type of Layer would you like to create?').addClass('nbn-layersCreatorTitle'))
		.dialog({
			modal: true,
			resizable: false,
			title: 'Create a Layer',
			width: 450
		});
		
	_dialog.append($('<div>').addClass('nbn-layersCreatorIconBar')
		.append(_createLayerIcon('Species',_dialog))
		.append(_createLayerIcon('Habitat',_dialog))
		.append(_createLayerIcon('Boundary',_dialog))
	)
	return _dialog;
}
nbn.mapping.construction.WidgetOptionsCreator.prototype.createLayersContainer = function() {
	var _me = this;
	var interactiveMapper = this._map;

	var layersBoxContainer = $('<div class="nbn-layersContainer">');
	interactiveMapper.addNBNMapLayerCollectionUpdateListener({
		add: function(collection, added) {layersBoxContainer.prepend(added.view);},
		remove: function(collection, removed) {removed.view.remove(); },//remove the layer
		reposition: function(collection) {
			for(var i in collection)
			  layersBoxContainer.prepend(collection[i].view);
		}
	});
	
	return {
		representation: $('<div>')
			.append(layersBoxContainer.sortable({
				handle:'.nbn-statefulBox-handleBar',
				placeholder: "ui-state-highlight",
				forcePlaceholderSize: true,
				delay: 300,
				update: function(event, ui) {
					var underlyingArray = interactiveMapper.getUnderlyingNBNMapLayerArray();
					var initPosition = nbn.util.ArrayTools.getIndexOfByFunction(underlyingArray,ui.item.context, function(element){
						return element.view[0];
					});
					var newPosition = ($(this).children().size()-1) - $(ui.item.context).index(); //calculate the correct position for the layer
					interactiveMapper.positionNBNMapLayer(underlyingArray[initPosition],newPosition);
				}
			}).disableSelection()).nbn_statefulbox({
				title: "Layers",
				padding: false,
				outerClass: 'nbn-layersContainer-outerBox',
				additionalButtons: [{ //define the create new layer button
                    initialState: 'default',
                    states: {
                        "default" : {
                            icon : "ui-icon ui-icon-newwin",
                            click: function() {_me.createLayersCreatorBox()},
                            tooltip: 'Create a new Layer'
                        }
                    }
                }]
			}
		),
		validate: function(newVal) {layersBoxContainer.css({ maxHeight: newVal-170 });}
	};
}
nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoadingWidget = function() {
	var loadingWidget = $('<div>').nbn_loading();
	this._map.addNBNMapLayerCollectionUpdateListener({
		add: function(collection, added) {
			loadingWidget.nbn_loading('listenToTheLoadingOf',added.layer);
		},
		remove: function(collection, removed) {
			loadingWidget.nbn_loading('stopListeningToTheLoadingOf', removed.layer);
		}
	});
	return loadingWidget;
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoginWidget = function() {
	return $('<div>').nbn_login({
		loginUser: this._map
	});
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createGetURLWidget = function() {
	var interactiveMapper = this._map, urlBox, message;
	
	function generateMapURL() {
		var mapURL = interactiveMapper.getMapURL();
		urlBox.toggle(mapURL != false); //hide or show the get map url box
		message.html((mapURL) ? 'Below is the url for your map' : 'Unfortunately it is currently not possible to create a url for a map with multiple layers of the same time');
		if(mapURL)
			urlBox.attr('value',mapURL);
	}
		
	var getURLDialog = $('<div>')
		.append(message = $('<h3>'))
		.append(urlBox = $('<input type="text">').width(400))
		.dialog({ title: 'Interactive Map URL', autoOpen: false, modal: true, resizable: false, width: 500, open: generateMapURL });
		
	return $('<div>')
		.html('Get Map URL')
		.button()
		.click(function() {
			getURLDialog.dialog('open');
		});
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createLogger = function () {
	var toReturn =  new nbn.util.Logger();
	dhtmlxError.catchError('ALL',function(type, desc, erData) { //log dhtmlxErrors
		toReturn.error('nbn.widget.treeWidget', type + ' ' + desc + ' ' + erData[1].filePath);
	});
	return toReturn;
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createLoginFilter = function() {
	var loginFilter = new nbn.layer.ArcGisMapFilter(); //make the login filter availble as a property
	this._map.addUserUpdateListener({
		User: function(newUser) {
			if(newUser) {
				loginFilter.setFilter({
					filters: {
						username: newUser.username,
						userkey: newUser.passhash
					}
				});
			}
			else
				loginFilter.clearFilter();
		}
	});
	return loginFilter;
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createNBNHabitatLayer = function(options) {
	var _layerHelper = this._layerHelper; //take the layer helper from the contstructor factory
	function _createHabitatLayerBox(layer, filter) {
        var _createHabitatAppliedFilters = function(tempFilter) {
            return $('<div>')
                .append($('<div>') //filter species status
                    .append(_layerHelper.createLabeledContent('',$('<div style="height: 20px; overflow-y:auto; overflow-x:hidden;">').nbn_filterStatus({layerFilter: tempFilter,inactiveFilteringLabel:'No habitats selected'})))
                );
        };
        var _habitatDialog = $('<div>').nbn_renderableControlDialog({
            renderableControl: new function() {
                var content = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=h',
                    allowMultipleSelection: 'checkbox'
                });

                this.apply = function() {
                    var layers = content.nbn_treewidget('getAllChildrenChecked');
                    if(layers.length) {
                        filter.setFilter(nbn.util.ArrayTools.getArrayByFunction(layers, function(element) {
                            return {
                                gisLayerID: element,
								datasetKey: content.nbn_treewidget('getChildUserData', element, 'datasetKey')
                            };
                        }));
                    }
                    else
                        filter.clearFilter();
                };
				
				this.getRepresentation = function() {return content;};
                this.reset = function() {content.nbn_treewidget('unCheckAll');};
                this.getState = function() {return content.nbn_treewidget('getState');};
                this.setState = function(state) {content.nbn_treewidget('setState', state);};
				
                $.extend(this, new nbn.util.ObservableAttribute('Renderable',true));
            },
            title: 'Habitats',
            resetButtonText: 'Clear Layer',
			autoOpen: options.configureOnLoad
        });
        return _layerHelper.createNBNLayerBox($('<div>').append(_createHabitatAppliedFilters(filter)), layer, {
            "Choose Habitats" : function() {
                _habitatDialog.nbn_renderableControlDialog("open");
            }
        });
    };

	var habitatMapLayer = _layerHelper.createMapLayer(nbn.layer.HabitatLayer, options, 'Habitat');
	var toReturn =  {
		layer: habitatMapLayer,
		picker : new nbn.layer.picker.HabitatPicker(habitatMapLayer)
	};
	
	toReturn.view = _createHabitatLayerBox(toReturn,habitatMapLayer.getResolvingVisibleFilter());
	return toReturn;
};
nbn.mapping.construction.WidgetOptionsCreator.prototype.createNBNBoundaryLayer = function(options) {
	var _layerHelper = this._layerHelper; //take the layer helper from the contstructor factory
	function _createBoundaryLayerBox(layer, filter) {    
        var _createBoundaryAppliedFilters = function(tempFilter) {
            return $('<div>')
                .append($('<div>') //filter species status
                    .append(_layerHelper.createLabeledContent('',$('<div style="height: 20px; overflow-y:auto; overflow-x:hidden;">').nbn_filterStatus({layerFilter: tempFilter,inactiveFilteringLabel:'No boundary selected'})))
                );
        };

        var _boundaryDialog = $('<div>').nbn_renderableControlDialog({
            renderableControl: new function() {
                var content = $('<div>').nbn_treewidget({
                    urlOfDescriptionFile : 'TreeWidgetGenerator?type=s',
                    allowMultipleSelection: 'radio'
                });

                this.apply = function() {
                    var layers = content.nbn_treewidget('getAllChildrenChecked');
                    if(layers.length) {
                        var element = layers[0];
						filter.setFilter({
							gisLayerID: element,
							datasetKey: content.nbn_treewidget('getChildUserData', element, 'datasetKey')
						});
                    }
                    else
                        filter.clearFilter();
                };
				
				this.getRepresentation = function() {return content;};
                this.reset = function() {content.nbn_treewidget('unCheckAll');};
                this.getState = function() {return content.nbn_treewidget('getState');};
                this.setState = function(state) {content.nbn_treewidget('setState', state);};

                $.extend(this, new nbn.util.ObservableAttribute('Renderable',true));
            },
            title: 'Boundary',
            resetButtonText: 'Clear Layer',
			autoOpen: options.configureOnLoad
        });
        return _layerHelper.createNBNLayerBox($('<div>').append(_createBoundaryAppliedFilters(filter)), layer, {
            "Choose Boundary" : function() {
                _boundaryDialog.nbn_renderableControlDialog("open");
            }
        });
    };

	var siteboundaryMapLayer = _layerHelper.createMapLayer(nbn.layer.BoundaryLayer, options, 'Boundary')
	var toReturn = {
		layer: siteboundaryMapLayer,
		picker : new nbn.layer.picker.SiteBoundaryPicker(siteboundaryMapLayer)
	};
	toReturn.view = _createBoundaryLayerBox(toReturn,siteboundaryMapLayer.getResolvingVisibleFilter());
	return toReturn;
}
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 25th-Jan-2011
* @description	:- This JScript creates an interactive mapper
* @dependencies	:-
*	layout/MappingLayoutManager.js
*	layout/StaticGridLayoutManager.js
*	nbn.util.ObservableCollection
*/
$.namespace("nbn.mapping.InteractiveMappingClient", function(toDrawTo, options) {
	/*DEFINE DEFAULT OPTIONS*/
	options = $.extend(true, {
		map: {
			type: nbn.mapping.openlayers.OpenLayersMap,
			baselayer: 'Hybrid',
			extent: {
				xmin: -14.489099982674913,
				xmax: 7.87906407581859,
				ymin: 49.825193671965025,
				ymax: 59.45733404137668
			}
		},
		initialLayers: [{type:"Species"}]
	}, options);

	/*START CREATING GOOGLE MAP AND PROVIDING EXTERNAL METHODS*/
	var _me = this; //store a reference to me
	var optionsCreator = this.ConstructionFactory = new nbn.mapping.construction.WidgetOptionsCreator(this); //create a widget options creator for this interactive map

	$.extend(this,
		new nbn.util.ObservableCollection('NBNMapLayer'),
		new nbn.util.user.CookieLoginable()
	); //extend an observable collection
	
	/*CONSTRUCT THE NBN INTERACTIVE MAPPER*/
	this.Map = new options.map.type(options.map, this);

	this.LoginFilter = optionsCreator.createLoginFilter();
	this.Logger = optionsCreator.createLogger();

	var mappingLayoutManager = this.Map.getMappingLayoutManager(); //wrap it up in a mapping manger
	var gridLayoutManager = this.StaticGridLayoutManager = new nbn.layout.StaticGridLayoutManager(mappingLayoutManager); //wrap the mapping manger into a grid layout manager
	toDrawTo.append(gridLayoutManager.getLayoutContainer()); //draw the map to whereever

	if(this.Map.initalize) //does the map need to be explicty initalized
		this.Map.initalize();

	/*POSITION THE WIDGETS*/
	gridLayoutManager.append(gridLayoutManager.ControlsPosition.STATIC_GRID_FOOTER,optionsCreator.createCopyrightWidget());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.BOTTOM_CENTER,optionsCreator.createLoadingWidget());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.RIGHT_BOTTOM,optionsCreator.createLayersContainer());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.TOP_RIGHT,optionsCreator.createGetURLWidget());
	mappingLayoutManager.append(mappingLayoutManager.ControlsPosition.TOP_LEFT,optionsCreator.createLoginWidget());

	this.Map._registerNBNLayersManipulation(this);
	this.Map._registerPickingListener(this);
	
	/*PICKING CLOSING FUNCTIONALITY*/
	(function(interactiveMap) {
		var _updateEventsToListenTo = ['CurrentVisibleLayers', 'CurrentFilters', 'Identify', 'ToRenderLogic', 'MapService']; //event types to listen to
		var _layerUpdateObject = { update: interactiveMap.Map.closePickingDialogMapDialog }; //action to perform on update
		var _setListenToEvents = function(addOrRemove, layer) {
			for(var i in _updateEventsToListenTo)
				layer[addOrRemove + _updateEventsToListenTo[i] + 'UpdateListener'](_layerUpdateObject);
		};
		
		interactiveMap.addNBNMapLayerCollectionUpdateListener({
			add: 	function(collection, added) {	_setListenToEvents('add', added.layer);		},
			remove: function(collection, removed) {	_setListenToEvents('remove', removed.layer);}
		});
	})(_me);

	/*SET UP INITIAL MAPS*/
	for(var i in options.initialLayers) {
		var currLayerToCreate = options.initialLayers[i];
		try {
			var layerCreationFunction = optionsCreator['createNBN' + currLayerToCreate.type + 'Layer'];
			if(layerCreationFunction)
				_me.addNBNMapLayer(layerCreationFunction.call(optionsCreator,currLayerToCreate));
			else
				 this.Logger.error('Attempted To Create an initial layer of Unknown type',currLayerToCreate.type);
		}
		catch(err) {
			this.Logger.error('There was an error trying to set initial layer ' + i, err);
		}
	}

	/*VALIDATE ALL THE CONTROLS*/
	gridLayoutManager.validate();
});
/**
*
* @author		:- Christopher Johnson
* @date			:- 23-May-2011
* @description	:- This JScript addes the getMapURL method to the interactive mapper
* @dependencies	:-
*	nbn.mapping.InteractiveMappingClient.js (MUST BE IMPORTED FIRST)
*	nbn.util.ServerGeneratedLoadTimeConstants
*	nbn.util.ArrayTools
*/

nbn.mapping.InteractiveMappingClient.prototype.getMapURL = function() {
	function _createMapParams(map) {
		var extent = map.Map.getViewportBBox();
		return {
			baselayer: map.Map.getBaseLayer().getID(),
			bbox: [extent.xmin,extent.ymin,extent.xmax,extent.ymax].join(',')
		}
	}
	
	function _parameteriseLayers(layers) {
		var typesSeen = {}, toReturn = {};
		
		function _hasAlreadyBeenSeen(type) { //util function, will flag the elements which have been seen
			var toReturn = typesSeen[type];
			typesSeen[type] = true; //flag the element as having been seen
			return toReturn;
		}
		
		for(var i in layers) {
			var currLayerConstructionObject = layers[i].layer.getReconstructionObject();
			if(!_hasAlreadyBeenSeen(currLayerConstructionObject.type)) { //has this element be seen before
				delete currLayerConstructionObject.type; //remove the type
				$.extend(toReturn,currLayerConstructionObject);
			}
			else
				return false; //can not create a paramaterised layer object
		}
		return toReturn;
	}

	function _createMapURL(map) {
		var paramLayers = _parameteriseLayers(map.getUnderlyingNBNMapLayerArray());
		if(paramLayers) {
			var toParameterize = $.extend(_createMapParams(map), paramLayers);
			return nbn.util.ServerGeneratedLoadTimeConstants.context.appPath + nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(toParameterize),'&','/?');
		}
		return false;
	};

	return _createMapURL(this);
};

