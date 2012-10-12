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
    });
})(jQuery);