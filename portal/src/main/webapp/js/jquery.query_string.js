(function($){
    $.query_string = function() {
        var toReturn={}, hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        $.each(hashes, function(i, hash){
            var keyVal = hash.split("="), key=keyVal[0], val=keyVal[1];
            if(toReturn[key]) { //has key already been defined
                if($.isArray(toReturn[key])) 
                    toReturn[key].push(val); //is already array add extra param
                else
                    toReturn[key] = [toReturn[key], val]; //convert to array
            }
            else
                toReturn[key] = val; //just a standard set param
        });
        return toReturn;
    }
})(jQuery);