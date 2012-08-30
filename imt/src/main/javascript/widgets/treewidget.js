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