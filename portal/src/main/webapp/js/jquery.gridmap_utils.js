(function($){
    
    function getURL(formObjArray){
        console.log(formObjArray)
        var valuePairs = getKeyValuePairsFromForm(formObjArray);
        var queryString = keyValuePairsToQueryString(valuePairs);
        console.log(queryString);
//        var queryString = keyValuePairsToQueryString(getKeyValuePairsFromForm(formObjArray));
        //Harvest values into key/value pairs, multiple form values with the same name will become a single array
    
//        alert(queryString);
    //            return "/nbnv-gis-0.1-SNAPSHOT/SingleSpecies/NHMSYS0020528107/map?imagesize=5&resolution=" + formArgs.resolution;
    }
        
    function getKeyValuePairsFromForm(formObjArray){
        var toReturn = {};
        $.each(formObjArray, function(i, obj){
            if(toReturn[obj.name] == undefined)
                toReturn[obj.name] = obj.value;
            else if (typeof toReturn[obj.name] == Array)
                toReturn[obj.name].push(obj.value);
            else
                toReturn[obj.name] = [toReturn[obj.name],obj.value];
        });
        return toReturn;
    }
        
    function keyValuePairsToQueryString(formVals){
        var queryString = "";
        var ampersand="";
        $.each(formVals, function(name, value){
            queryString += ampersand + name + "=" + getArgForQueryString(value);
            if(ampersand==""){
                ampersand="&";
            }
        });
        return queryString;
    }

    function getArgForQueryString(value){
        if(typeof value == Array)
            return join(value);
        return value;
    }
        
    function getColourPickerOptions(colourPickerId){
        var currentColour = $(colourPickerId + ' div').css('backgroundColor');
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
                $(colourPickerId + ' div').css('backgroundColor', '#' + hex);
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

    $(document).ready(function(){
        /* Do map refresh */
        $('#nbn-grid-map-form').submit(function(){
            //Get arguments and build url.....TODO
            //            console.log($(this).serializeArray());
            getURL($(this).serializeArray());
            //            $('#nbn-grid-map-image-src').attr('src',getURL($(this).serializeArray()));
            return false;
        });
        
        //Setup colour pickers
        $('#nbn-colour-picker1').ColorPicker(getColourPickerOptions('#nbn-colour-picker1'));
        $('#nbn-colour-picker2').ColorPicker(getColourPickerOptions('#nbn-colour-picker2'));
        $('#nbn-colour-picker3').ColorPicker(getColourPickerOptions('#nbn-colour-picker3'));
        $('#nbn-colour-picker-outline').ColorPicker(getColourPickerOptions('#nbn-colour-picker-outline'));
        
        //When selecting a country scale region the Vice County drop down must return to 'none'
        $('#nbn-region-selector').change(function(){
            $('#nbn-vice-county-selector').val("none");
        });
        
    });
        
})(jQuery);