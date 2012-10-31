(function($){
    
    function getURL(form){
        var formObjArray = form.serializeArray();
        var tvk = $('#tvk').val(); 
        var keyValuePairs = getKeyValuePairsFromForm(formObjArray);
        var keyValuePairsWithBusinessLogic = getKeyValuePairsWithBusinessLogic(keyValuePairs);
        var queryString = getQueryStringFromKeyValuePairs(keyValuePairsWithBusinessLogic);
        return form.attr('gis-server') + '/SingleSpecies/' + tvk + '/map?' + queryString;
    }
        
    function getKeyValuePairsFromForm(formObjArray){
        var toReturn = {};
        $.each(formObjArray, function(i, obj){
            if(toReturn[obj.name] == undefined)
                toReturn[obj.name] = obj.value;
            else if (toReturn[obj.name] instanceof Array)
                toReturn[obj.name].push(obj.value);
            else
                toReturn[obj.name] = [toReturn[obj.name],obj.value];
        });
        return toReturn;
    }
    
    function getKeyValuePairsWithBusinessLogic(keyValuePairs){
        
        //Remove the feature argument generated when Vice County value is 'none''
        if(keyValuePairs.hasOwnProperty('feature') && keyValuePairs['feature'].toUpperCase()=='NONE'){
            delete keyValuePairs['feature'];
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
        
        //If OS is used as a background it must appear first to force vector layers to be drawn over it and not be obscured by it
        if(keyValuePairs.hasOwnProperty('background') && keyValuePairs['background'] instanceof Array){
            if($.inArray('os',keyValuePairs['background']) > -1){
                var osFirstArray = ['os'];
                $.each(keyValuePairs['background'],function(index, value){
                    if(value != 'os'){
                        osFirstArray.push(value);
                    }
                });
                keyValuePairs['background'] = osFirstArray;
            }
        }
        
        //Remove the hidden tvk, just used to get the tvk from the path of the page request to here
        delete keyValuePairs['tvk'];
        
        //Remove the hidden outline colour
        delete keyValuePairs['value-nbn-colour-picker-outline'];
        delete keyValuePairs['showOutline'];
        
        return keyValuePairs;
    }
        
    function getQueryStringFromKeyValuePairs(keyValPairs){
        var queryString = "";
        var ampersand="";
        $.each(keyValPairs, function(name, value){
            queryString += ampersand + name + "=" + getArgForQueryString(value);
            if(ampersand==""){
                ampersand="&";
            }
        });
        //Unfortunately the 'band' argument is used mutliple times in the query string
        //This didn't fit into the generic form handling implemented here, so needs
        //an edit now
        var pattern = /band[0-9]/g;
        var toReturn = queryString.replace(pattern,'band');
        console.log(toReturn);
        return toReturn;
    }

    function getArgForQueryString(value){
        if(typeof value == Array)
            return join(value);
        return value;
    }
        
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
            var imagesize = $("input[name='imagesize']").val();
            var resolutions = json[imagesize];
            var resolutionSelect = $('#nbn-grid-map-resolution');
            resolutionSelect.find('option').remove();
            $.each(resolutions, function(index, resolution){
                resolutionSelect.append(
                    $('<option></option>').val(resolution).html(resolution)
                );
            });
        });
    }

    $(document).ready(function(){

        $('#nbn-grid-map-form').submit(function(){
            var form = $(this);
            
            //Deselect datasets if all are selected - requires jquery.dataset-selector-utils.js
            nbn.portal.reports.utils.DatasetFields.doDeselectDatasetKeys();
            
            //Do map refresh
            $('#nbn-grid-map-image').attr('src',getURL(form));
            
            //Turn on all datasets if they are all off
            nbn.portal.reports.utils.DatasetFields.doSelectDatasetKeys();
            
            updateResolutionDropDown(form);
            
            return false;
        });
        
        //Setup colour pickers
        $('#nbn-colour-picker-1, #nbn-colour-picker-2, #nbn-colour-picker-3, #nbn-colour-picker-outline').each(function(){
            $(this).ColorPicker(getColourPickerOptions($(this).attr('id')));
        });
        
        //When selecting a country scale region the Vice County drop down must return to 'none'
        $('#nbn-region-selector').change(function(){
            $('#nbn-vice-county-selector').val("none");
        });
        
    });
        
})(jQuery);