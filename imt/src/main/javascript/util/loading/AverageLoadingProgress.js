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