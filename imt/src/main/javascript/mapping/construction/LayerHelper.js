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