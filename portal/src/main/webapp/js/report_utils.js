(function($){
    
    var earliestYear = 1000;

    namespace("nbn.portal.reports.utils.forms", {
        getKeyValuePairsFromForm: function(form){
            return doGetKeyValuePairsFromForm(form);
        }, 
        getQueryStringFromKeyValuePairs: function(keyValPairs, createCommaDelimitedValues){
            return doGetQueryStringFromKeyValuePairs(keyValPairs, createCommaDelimitedValues);
        },
        getNoRecordsFoundInfoBox: function(){
            return '<div class="nbn-information-panel">No records were found for your current options</div>';
        },
        getDefaultText: function(value, defaultText){
            return $.isEmptyObject(value) ? defaultText : value;
        },
        getDateText: function(date){
            return doGetDateText(date);
        },
        isFormFieldValid: function($input){
            return doIsFormFieldValid($input);
        }
    });
    
    function doIsFormFieldValid($input){
        if($input.attr('id') == 'startYear' || $input.attr('id') == "endYear"){
            if(isValidYear($input.val())){
                return true;
            }else{
                alert(getInvalidYearText());
                $input.focus();
                return false;
            }
        }else{
            return true;
        }
    }

    function isValidYear(year){
        return year=='' || (isNumber(year) && (year <= new Date().getFullYear() && year >= earliestYear));
    }
    
    function getInvalidYearText(){
        return 'You entered an invalid year.  Either leave blank, or enter a 4 digit year in the range ' + earliestYear + ' to ' + new Date().getFullYear();
    }
    
    function doGetDateText(date){
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        return (date.getDate()) + '/' + months[date.getMonth()] + '/' + date.getFullYear();
    }
    
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
        if($.isArray(value))
            return value.join();
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

    function isNumber(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

})(jQuery);