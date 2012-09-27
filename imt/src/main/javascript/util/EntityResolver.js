/**
*
* @author		:- Christopher Johnson
* @date			:- 7-April-2011
* @description	:- This JScript will resolve the properties of an object to there complete nbn concept.
*	If the property is already complete then it will be returned in a callback without bothering the JSON service
* @dependencies	:-
*	jquery
*/
$.namespace("nbn.util.EntityResolver", new function() {
    var dataResolvers = {
        boundary : function(id, callback) {
            return $.getJSON(nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/siteBoundaryDatasets/" + id, callback);
        },
        habitats : function(id, callback) {
            throw "Entity resolver not complete for habitats";
        },
        dataset : function(id, callback) {
            return $.getJSON(nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/datasets/" + id, callback);
        },
        datasets : function(id, callback) {
            throw "Entity resolver not complete for datasets";
        },
        designation : function(id, callback) {
            return $.getJSON(nbn.util.ServerGeneratedLoadTimeConstants.data_api + "/designations/" + id, callback);
        }
    };
    //The data api does not filter out requests for metadata. Reuse the respective method
    dataResolvers.datasetWithMetadata = dataResolvers.dataset;
    dataResolvers.datasetsWithMetadata = dataResolvers.datasets;
    
    this.resolve = function(data, callback) {
        var dataTypesToResolve = [], dataToResolve = {}, ajaxResponses;
        
        for(var i in data) {//go through each of the properties and check if they are strings, if they are resolve them using the JSON Name servlet
            if(typeof data[i] === "string") {
                dataToResolve[i] = data[i];
                dataTypesToResolve.push(i);
            }
        }

        if(dataTypesToResolve.length) {
            //go through the object of data to resolve and resolve all of them. 
            //Keep a handle on the jqXHR to pass to the when function
            ajaxResponses = $.map(dataTypesToResolve, function(type) {     
                //execute the resolution function. Store the result in the data 
                //object which was passed into this function
                return dataResolvers[type](dataToResolve[type], function(result) { data[type] = result; });
            });
            
            $.when.apply(this, ajaxResponses).done(function() {
                callback(data);
            });
        }
        else
            callback(data);
    };
});