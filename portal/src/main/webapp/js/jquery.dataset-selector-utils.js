(function($){


    function isAllDatasetsUnchecked(){
        var datasetCheckBoxes = $("INPUT[name='datasetKey'][type='checkbox']");
        var numCheckBoxesChecked = datasetCheckBoxes.filter(':checked').length;
        return numCheckBoxesChecked == 0;
    }

    function isAllDatasetsChecked(){
        var datasetCheckBoxes = $("INPUT[name='datasetKey'][type='checkbox']");
        var numCheckBoxes = datasetCheckBoxes.length;
        var numCheckBoxesChecked = datasetCheckBoxes.filter(':checked').length;
        return numCheckBoxes == numCheckBoxesChecked;
    }
    
    namespace("nbn.portal.reports.utils.datasetfields", {
        doDeselectDatasetKeys: function(){
            if(isAllDatasetsChecked()){
                $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', false);
            }
        },
        doSelectDatasetKeys: function(){
            if(isAllDatasetsUnchecked()){
                $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', true);
            }
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
            return 'dataset:{all:false,datasets:[' + 
                        nbn.portal.reports.utils.datasetfields.getSelectedDatasets().map(function(element) { 
                            return '\'' + String(element) + '\'';
                        }) + ']}';
        }
    });
    
    $(document).ready(function(){
        /* This is used to check or uncheck all datasets on a selectable dataset table
         * eg as appears on a site report page.  It wires the button's click events to the
         * appropriate action
         */
        $('#nbn-select-datasets-auto').click(function(){
            $("INPUT[name='datasetKey'][type='checkbox']").attr('checked',$('#nbn-select-datasets-auto').is(':checked'));
        });
        
        
        /* If all datasetKeys are checked then set the select/deselect check box
         * to be checked
         */
        if(isAllDatasetsChecked()){
            $('#nbn-select-datasets-auto').attr('checked', true);
        }
         
    });
})(jQuery);