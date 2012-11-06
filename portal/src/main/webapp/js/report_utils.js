(function($){
    namespace("nbn.portal.reports.utils.forms", {
        getKeyValuePairsFromForm: function(form){
            return doGetKeyValuePairsFromForm(form);
        }, 
        getQueryStringFromKeyValuePairs: function(keyValPairs, createCommaDelimitedValues){
            return doGetQueryStringFromKeyValuePairs(keyValPairs, createCommaDelimitedValues);
        },
        getNoRecordsFoundInfoBox: function(){
            return '<div class="nbn-information-panel">No records were found for your current options</div>';
        }
    });
    
    function doGetKeyValuePairsFromForm(form){
        var formObjArray = form.serializeArray();
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
    
    function doGetQueryStringFromKeyValuePairs(keyValPairs, createCommaDelimitedValues){
        var queryString = "";
        var ampersand="";
        $.each(keyValPairs, function(name, value){
            if(createCommaDelimitedValues){
                queryString += ampersand + name + "=" + getArgForQueryString(value);
            }else{
                queryString += ampersand + getArgsForQueryString(name, value);
            }
            if(ampersand==""){
                ampersand="&";
            }
        });
        //Unfortunately the 'band' argument is used mutliple times in the query string
        //This didn't fit into the generic form handling implemented here, so needs
        //an edit now
        var pattern = /band[0-9]/g;
        var toReturn = queryString.replace(pattern,'band');
        if(toReturn != ''){
            toReturn = '?' + toReturn;
        }
        return toReturn;
    }
        

    function getArgForQueryString(value){
        if(value instanceof Array)
            return join(value);
        return value;
    }
    
    function getArgsForQueryString(name, value){
        if(value instanceof Array){
            var ampersand = '';
            var toReturn = ''
            $.each(value, function(index, value){
                toReturn += ampersand + name + '=' + value;
                if(ampersand == ''){
                    ampersand = '&';
                }
            });
            return toReturn;
        }else{
            return name + '=' + value;
        }
    }

})(jQuery);