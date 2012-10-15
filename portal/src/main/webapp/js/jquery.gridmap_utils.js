(function($){
    $(document).ready(function(){
        /* 
         * Do map refresh
         */
        $('#nbn-grid-map-form').submit(function(){
            //Get arguments and build url.....TODO
            $('#nbn-grid-map-image-src').attr('src',getURL());
            return false;
        });
        
        $('#nbn-colour-picker1').ColorPicker(getColourPickerOptions('#nbn-colour-picker1'));
        $('#nbn-colour-picker2').ColorPicker(getColourPickerOptions('#nbn-colour-picker2'));
        $('#nbn-colour-picker3').ColorPicker(getColourPickerOptions('#nbn-colour-picker3'));
        
        function getURL(){
            var formArgs = {};
            formArgs["imageSize"] = "350";
            formArgs["resolution"] = $("#nbn-grid-map-resolution").find(":selected").text();
            var queryString = "";
            var ampersand="";
            $.each(formArgs, function(key, value){
                queryString += ampersand + key + "=" + value;
                if(ampersand==""){
                    ampersand=="&";
                }
            });
            return "/nbnv-gis-0.1-SNAPSHOT/SingleSpecies/NHMSYS0020528107/map?imagesize=5&resolution=" + formArgs.resolution;
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
        
    });
})(jQuery);