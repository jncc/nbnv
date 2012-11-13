/**
*
* @author	    :- Christopher Johnson
* @date		    :- 19-Nov-2010
* @description	:- This JScript defines Jquery UI widget which will create a tree view. This particular Tree Widget
					is based on the DHTMLx widget
* @dependencies	:-
*   jquery.ui.core.js
*   jquery.ui.widget.js	
*   dynatree.js 1.2.1
*   json2.js
* @abilities	:- A tree widget provides two event listeners:
*	(selection) a selection listener which will notify of the current set of elements have been selected.
*	(selected) a seleted listener which will notify that a new element has been selected.
*
*/
var times = 0;
(function( $, undefined ) {
    $.widget( "ui.nbn_treewidget", {
        options: {
            treeRoot: 0,
            allowMultipleSelection: 'checkbox',
            loadingText: 'Loading...',
            dataFilter : function(data) { return data; }
        },
		
        _createTree: function() {
            var _me = this;
            this.element.prepend(this._treeRepresentation = $('<div>').dynatree({
                 checkbox: true,
                 selectMode: (_me.options.allowMultipleSelection === 'radio') ? 1 : 2,
                 initAjax:{
                    url: _me.options.urlOfDescriptionFile,
                    dataFilter : function(data) {
                        //convert the data that came in and present it as a object 
                        //that dynatree can cope with. Use the dataFilterFunction 
                        //to transform the object if nessersary
                        return JSON.stringify($.map($.parseJSON(data), _me.options.dataFilter));
                    }
                },
                onSelect : function(flag, dtnode) {
                    if(!dtnode.hasChildren()) {
                        _me._trigger("selected", 0, dtnode.data);
                    }
                    
                    _me._trigger("childrenSelectionListener", 0, _me.getAllChildrenChecked()); //notify of check change
                }, 
                onPostInit: function(node) {
                    _me._trigger("loaded", 0); //notify of check change
                },
                classNames: {checkbox: (_me.options.allowMultipleSelection === 'radio') ? "dynatree-radio" : "dynatree-checkbox"}
             }));
             this._tree = this._treeRepresentation.dynatree("getTree");
             
        }, 
        
        _create: function() {
            this.element.addClass( "nbn-treewidget" ); //set the class of the nbn-treewidget       
            this._createTree();
            if(this.options.selectDeselect === true)
                this.addSelectDeselect();
        },
		
        getState: function() {
            console.log(["getState", arguments]); 
        },
		
        setState: function(stateToLoad) {
            console.log(["setState", arguments]); 
        },

        setUrlOfDescriptionFile : function(newUrl) {
            this.options.urlOfDescriptionFile = newUrl;
            this._treeRepresentation.remove();
            this._createTree();
        },
		
        getChildText : function(id) {
           console.log(["getChildText", arguments]); 
        },
		
        getChildUserData : function(id, wantedData) {
            return this._tree.getNodeByKey(id).data[wantedData];
        },
		
        getUserDataForAllChildrenChecked: function(wantedUserData) {
	    var _me = this;
            return $.map(this.getAllChildrenChecked(), function(child) {
                return _me.getChildUserData(child, wantedUserData);
            });
        },
		
        getAllChildrenChecked: function() { //this function will remove all the parent ids from the CSV and return a selected element array
            return this._getAllChildrenIDs(true);
        },
		
        getAllChildrenUnchecked: function() {
            return this._getAllChildrenIDs(false);
        },
        
        _getAllChildrenIDs: function(checked) {
            return $.map(this._getAllChildrenNodes(checked), function(node) {
                return node.data.key;
            });
        },
        
        _getAllChildrenNodes: function(checked) {
            var toReturn = [];
            $.each(this._tree.getRoot().getChildren(), function(i, node) {
                if(!node.hasChildren() && node.isSelected() === checked) {
                    toReturn.push(node);
                }
            });
            return toReturn;
        },
	
        isFullyChecked: function() {
            return this._tree.count() === this._tree.getSelectedNodes().length;
        },
		
        _checkAllWithValue: function(toCheck) {
            $.each(this._getAllChildrenNodes(!toCheck), function(i, node) {
                node.select(toCheck);
            });
            
            this._trigger("childrenSelectionListener", 0, this.getAllChildrenChecked()); //notify of check change
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


        destroy: function() {
            this.element.removeClass( "nbn-treewidget" ); //remove the appended class	
            $.Widget.prototype.destroy.apply( this, arguments );
        },

        setItemStyle: function(itemId, style) {
			
        }
    });

    $.extend( $.ui.nbn_treewidget, {
        version: "@VERSION"
    });

})( jQuery );