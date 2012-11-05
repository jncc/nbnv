(function($){
    
    var options = {
        imagesize: 5
    };
    
    var nationalExtentOptions = {
        gbi: {
            coastline: 'gbi', 
            grid100k: 'gbi100kextent', 
            grid10k: 'gbi10kextent'
        },
        gb: {
            coastline: 'gb', 
            grid100k: 'gb100kextent', 
            grid10k: 'gb10kextent'
        },
        ireland: {
            coastline: 'i', 
            grid100k: 'i100kextent', 
            grid10k: 'i10kextent'
        } 
    };

    function getURL(form){
        var formObjArray = form.serializeArray();
        var tvk = $('#tvk').val(); 
        var keyValuePairs = nbn.portal.reports.utils.forms.getKeyValuePairsFromForm(formObjArray);
        var keyValuePairsWithBusinessLogic = getKeyValuePairsWithBusinessLogic(keyValuePairs);
        var queryString = nbn.portal.reports.utils.forms.getQueryStringFromKeyValuePairs(keyValuePairsWithBusinessLogic);
console.log(('gis-server') + '/SingleSpecies/' + tvk + '/map?' + queryString);
        return form.attr('gis-server') + '/SingleSpecies/' + tvk + '/map?' + queryString;
    }
//        
//    function getKeyValuePairsFromForm(formObjArray){
//        var toReturn = {};
//        $.each(formObjArray, function(i, obj){
//            if(toReturn[obj.name] == undefined)
//                toReturn[obj.name] = obj.value;
//            else if (toReturn[obj.name] instanceof Array)
//                toReturn[obj.name].push(obj.value);
//            else
//                toReturn[obj.name] = [toReturn[obj.name],obj.value];
//        });
//        return toReturn;
//    }
    
    function getKeyValuePairsWithBusinessLogic(keyValuePairs){
        
        //Add the image size
        keyValuePairs['imagesize'] = options.imagesize;
        
        //Vice county - remove the feature argument generated when Vice County value is 'none',
        //otherwise we are zooming to a vice county so add overlay=feature to highlight the vc
        if(keyValuePairs.hasOwnProperty('feature') && keyValuePairs['feature'].toUpperCase()=='NONE'){
            delete keyValuePairs['feature'];
        }else{
            keyValuePairs['overlay'] = 'feature';
        }
        
        //Add the year bands formatted for the grid map service
        var showOutline = keyValuePairs.hasOwnProperty('showOutline');
        for(var i=1; i<4; i++){
            if(keyValuePairs.hasOwnProperty('gridLayer' + i)){
                var fillColour = $('#value-nbn-colour-picker-' + i).val();
                var outlineColour = fillColour;
                if(showOutline){
                    outlineColour = $('#value-nbn-colour-picker-outline').val();
                }
                keyValuePairs['band' + i] = keyValuePairs['startYear' + i] + '-' + keyValuePairs['endYear' + i] + ',' + fillColour.replace("#","") + ',' + outlineColour.replace("#","");
            }
            delete keyValuePairs['gridLayer' + i];
            delete keyValuePairs['startYear' + i];
            delete keyValuePairs['endYear' + i];
            delete keyValuePairs['value-nbn-colour-picker-' + i];
        }
        
        //There is a specific order that background layers should be requested to force
        //some layers to be drawn over others
        //eg - if OS is used as a background it must appear first to force vector layers to be drawn over it and not be obscured by it
        if(keyValuePairs.hasOwnProperty('background') && keyValuePairs['background'] instanceof Array){
            var orderedBackgroundArgs = ['os','vicecounty',
                nationalExtentOptions.gbi.coastline,nationalExtentOptions.gb.coastline,nationalExtentOptions.ireland.coastline,
                nationalExtentOptions.gbi.grid10k,nationalExtentOptions.gb.grid10k,nationalExtentOptions.ireland.grid10k,
                nationalExtentOptions.gbi.grid100k,nationalExtentOptions.gb.grid100k,nationalExtentOptions.ireland.grid100k
            ];
            var toReturn = [];
            $.each(orderedBackgroundArgs, function(index, value){
                if($.inArray(value,keyValuePairs['background']) > -1){
                    toReturn.push(value);
                }
            });
            keyValuePairs['background'] = toReturn;
        }
        
        //Remove the hidden tvk, just used to get the tvk from the path of the page request to here
        delete keyValuePairs['tvk'];
        
        //Remove the hidden outline colour
        delete keyValuePairs['value-nbn-colour-picker-outline'];
        delete keyValuePairs['showOutline'];
        
        return keyValuePairs;
    }
//        
//    function getQueryStringFromKeyValuePairs(keyValPairs){
//        var queryString = "";
//        var ampersand="";
//        $.each(keyValPairs, function(name, value){
//            queryString += ampersand + name + "=" + getArgForQueryString(value);
//            if(ampersand==""){
//                ampersand="&";
//            }
//        });
//        //Unfortunately the 'band' argument is used mutliple times in the query string
//        //This didn't fit into the generic form handling implemented here, so needs
//        //an edit now
//        var pattern = /band[0-9]/g;
//        var toReturn = queryString.replace(pattern,'band');
//        return toReturn;
//    }
//
//    function getArgForQueryString(value){
//        if(typeof value == Array)
//            return join(value);
//        return value;
//    }
        
    function getColourPickerOptions(colourPickerId){
        var currentColour = $('#' + colourPickerId + ' div').css('backgroundColor');
        return {
            color: colorToHex(currentColour),
            onShow: function (colpkr) {
                $(colpkr).fadeIn(500);
                return false;
            },
            onHide: function (colpkr) {
                $(colpkr).fadeOut(500);
                return false;
            },
            onChange: function (hsb, hex, rgb) {
                $('#' + colourPickerId + ' div').css('backgroundColor', '#' + hex);
                $('#value-' + colourPickerId).attr('value',hex);
            }
        }
    }
        
    function colorToHex(color) {
        if (color.substr(0, 1) === '#') {
            return color;
        }
        var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);
    
        var red = parseInt(digits[2]);
        var green = parseInt(digits[3]);
        var blue = parseInt(digits[4]);
    
        var rgb = blue | (green << 8) | (red << 16);
        return digits[1] + '#' + rgb.toString(16);
    };
    
    function updateResolutionDropDown(form){
        var tvk = $('#tvk').val();
        var feature = $('#nbn-vice-county-selector').val().toUpperCase();
        var url = form.attr('gis-server') + '/SingleSpecies/' + tvk + '/resolutions?callback=?';
        if(feature != 'NONE'){
            url += '&feature=' + feature;
        }
        $.getJSON(url, function(json){
            var resolutions = json[options.imagesize];
            var resolutionSelect = $('#nbn-grid-map-resolution');
            resolutionSelect.find('option').remove();
            $.each(resolutions, function(index, resolution){
                resolutionSelect.append(
                    $('<option></option>').val(resolution).html(resolution)
                    );
            });
        });
    }
    
    function applyRules(){
        
        //There must be at least one year band - if none are selected then turn on first year band
        if(!$("INPUT[name='gridLayer1'][type='checkbox']").is(':checked')
            && !$("INPUT[name='gridLayer2'][type='checkbox']").is(':checked')
            && !$("INPUT[name='gridLayer3'][type='checkbox']").is(':checked')
            ){
            $("INPUT[name='gridLayer1'][type='checkbox']").prop('checked',true);
        }
        
        //Set grid and coast check boxes to their region specific values
        var nationalExtent = $('#nbn-region-selector').val();
        $('#nbn-grid-map-coastline').val(nationalExtentOptions[nationalExtent].coastline);
        $('#nbn-grid-map-100k-grid').val(nationalExtentOptions[nationalExtent].grid100k);
        $('#nbn-grid-map-10k-grid').val(nationalExtentOptions[nationalExtent].grid10k);
    }
    
    function setupRegionVCInteractions(){
        //When selecting a national region the Vice County drop down must return to 'none'
        //If Ireland is selected, then disable os and vc checkboxes
        $('#nbn-region-selector').change(function(){
            $('#nbn-vice-county-selector').val("none");
            var disableNonIrishLayers = ($('#nbn-region-selector').val().toUpperCase() == 'IRELAND');
            $('#nbn-grid-map-vicecounty').prop('disabled', disableNonIrishLayers);
            $('#nbn-grid-map-os').prop('disabled', disableNonIrishLayers);
        });
        //When selecting a vice county the national region must return 'gb' and vc/os checkboxes must be enabled
        $('#nbn-vice-county-selector').change(function(){
            $('#nbn-region-selector').val("gb");
            $('#nbn-grid-map-vicecounty').prop('disabled', false);
            $('#nbn-grid-map-os').prop('disabled', false);
        });
    }
    
    function setupColourPickers(){
        $('#nbn-colour-picker-1, #nbn-colour-picker-2, #nbn-colour-picker-3, #nbn-colour-picker-outline').each(function(){
            $(this).ColorPicker(getColourPickerOptions($(this).attr('id')));
        });
    }
    
    function setupFormSubmit(){
        $('#nbn-grid-map-form').submit(function(){
            var form = $(this);
            
            //Apply any rules eg, must have at least one year band selected
            applyRules();
            
            //Deselect datasets if all are selected - requires jquery.dataset-selector-utils.js
            nbn.portal.reports.utils.DatasetFields.doDeselectDatasetKeys();
            
            //Do map refresh
            $('#nbn-grid-map-busy-image').show();
            $('#nbn-grid-map-image').attr('src',getURL(form));
            
            //Turn on all datasets if they are all off
            nbn.portal.reports.utils.DatasetFields.doSelectDatasetKeys();
            
            updateResolutionDropDown(form);
            
            return false;
        });
    }
    
    function hideBusyImageOnMapLoad(){
        $('#nbn-grid-map-image').load(function(){
            $('#nbn-grid-map-busy-image').hide();
        });
    }

    function addInitialMapImage(){
        $('#nbn-grid-map-busy-image').hide();
        $('#nbn-grid-map-image').attr('src','/img/ajax-loader-medium.gif');
        $('#nbn-grid-map-image').attr('src',getURL($('#nbn-grid-map-form')));
    }

    $(document).ready(function(){
        setupFormSubmit();
        setupColourPickers();
        setupRegionVCInteractions();
        hideBusyImageOnMapLoad();
        addInitialMapImage();
    });
        
})(jQuery);