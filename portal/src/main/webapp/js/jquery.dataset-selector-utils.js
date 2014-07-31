(function($){   
    namespace("nbn.portal.reports.utils.datasetfields", {
        doDeselectDatasetKeys: function(){
            $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', false);
        },
        doSelectDatasetKeys: function(){
            $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', true);
        },
        getSelectedDatasets: function(){
            var datasets = [];
            if ($('#nbn-select-datasets-orderby').val() === '1') {
                datasets = $('#nbn-dataset-ordered-table-byrecord input[name=datasetKey][type=checkbox]:checked').map(function() {
                    return $(this).attr("value");
                }).get();
            } else if ($('#nbn-select-datasets-orderby').val() === '2') {
                datasets = $('#nbn-dataset-ordered-table-byname input[name=datasetKey][type=checkbox]:checked').map(function() {
                    return $(this).attr("value");
                }).get();
            }
            return datasets;
        },
        getSelectedDatasetsJSON: function() {
            var datasets = nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
            
            // If we have no datasets selected then act as if all are selected
            // otherwise return the selected datasets
            if (datasets.length > 0) {            
                return 'dataset:{all:false,datasets:[' + 
                            $.map(datasets, function(element) { 
                                return '\'' + String(element) + '\'';
                            }) + ']}';
            } else {
                return 'dataset:{all:true}';
            }
        },
        getSelectedDatasetsCount: function(){
            var datasets = nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
            return datasets.length;
        },
        getSelectedDatasetsJoined: function() {
            var datasets = nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
            if (datasets !== undefined && datasets.length > 0) {
                if (typeof datasets === 'string') {
                    datasets = [datasets];
                }
                return datasets.join();
            }
            return undefined;
        }
    });
    
    $(document).ready(function(){        
        $('#nbn-select-all-datasets').click(function(){
            nbn.portal.reports.utils.datasetfields.doSelectDatasetKeys();
            // Push a change event through
            $(this).change();
        });
        $('#nbn-deselect-all-datasets').click(function(){
            nbn.portal.reports.utils.datasetfields.doDeselectDatasetKeys();
            // Push a change event through
            $(this).change();            
        });   
        
        // Check box in both organisation name and number of records sorted 
        // views
        $('input[type=checkbox][name=datasetKey]').click(function() {
            $('input[type=checkbox][name=datasetKey][value=' + $(this).attr('value') + ']')
                    .prop('checked', $(this).prop('checked'));
        });
    });
})(jQuery);