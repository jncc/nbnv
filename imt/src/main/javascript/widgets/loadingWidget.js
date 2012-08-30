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
