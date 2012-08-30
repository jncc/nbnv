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