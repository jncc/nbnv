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
            this._collapse(this._contentsContainer, collapsed, aniOpt, speed);
            this._notifyStateChange('collapsed', collapsed);
        },

        _setAdvancedCollapsed: function(advancedCollapsed,speed) {
	    var aniOpt = this.options.animation;
            this._collapse($('[advancedControl="true"]',this._contentsContainer), advancedCollapsed, aniOpt, speed);
            this._notifyStateChange('advancedcollapsed', advancedCollapsed);
        },
        
        _collapse: function(element, toShow, aniOpt, speed) {
            if(speed) {
                element[(toShow) ? 'hide' : 'show'](aniOpt.effect, aniOpt.options, speed);
            }
            else {
                element[(toShow) ? 'hide' : 'show']();
            }
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