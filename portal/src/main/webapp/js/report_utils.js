(function($){
    namespace("nbn.portal.reports.utils.forms", {
        getKeyValuePairsFromForm: function(formObjArray){
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
        },
        getQueryStringFromKeyValuePairs: function(keyValPairs){
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
            return toReturn;
        }
    });
    
    function getArgForQueryString(value){
        if(typeof value == Array)
            return join(value);
        return value;
    }

})(jQuery);