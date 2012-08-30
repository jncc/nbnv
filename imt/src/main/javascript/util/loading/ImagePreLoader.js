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