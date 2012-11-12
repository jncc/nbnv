
(function($) {
    $.namespace("nbn.construction.search", {
       createTaxonTable : function(callback) {
            return nbn.construction.search__createSearchTable({
                 title : "Taxon Name", 
                 searchSource : "taxa",
                 callbackField: "pTaxonVersionKey"
            }, callback);
       },
       createTaxonDatasetTable : function(callback) {
           return nbn.construction.search__createSearchTable({
                 title : "Taxon Dataset Name", 
                 searchSource : "taxonDatasets",
                 callbackField: "record_id"
            }, callback);
       },
       createDesignationsTable : function(callback) {
           return nbn.construction.search__createSearchTable({
                 title : "Designation Name", 
                 searchSource : "designations",
                 callbackField: "code"
            }, callback);
       }
    });
})(jQuery);