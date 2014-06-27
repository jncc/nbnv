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
                var datasets = $('#nbn-dataset-ordered-table-byrecord input[name=datasetKey][type=checkbox]:checked').map(function() {
                    return $(this).attr("value");
                }).get();
            } else if ($('#nbn-select-datasets-orderby').val() === '2') {
                var datasets = $('#nbn-dataset-ordered-table-byname input[name=datasetKey][type=checkbox]:checked').map(function() {
                    return $(this).attr("value");
                }).get();
            }
            return datasets;
        },
        getSelectedDatasetsJSON: function() {
            var datasets = nbn.portal.reports.utils.datasetfields.getSelectedDatasets();
            return 'dataset:{all:false,datasets:[' + 
                        $.map(datasets, function(element) { 
                            return '\'' + String(element) + '\'';
                        }) + ']}';
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
    });
})(jQuery);