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
            throw new "Entity resolver not complete for habitats"
        },
        dataset : function(id, callback) {
            throw new "Entity resolver not complete for dataset"
        },
        datasets : function(id, callback) {
            throw new "Entity resolver not complete for datasets"
        },
        designation : function(id, callback) {
            throw new "Entity resolver not complete for designation"
        },
        datasetWithMetadata: function(id, callback) {
            throw new "Entity resolver not complete for datasetWithMetadata"
        },
        datasetsWithMetadata: function(id, callback) {
            throw new "Entity resolver not complete for datasetsWithMetadata"
        }
    };
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