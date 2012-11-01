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
    
    namespace("nbn.portal.reports.utils.DatasetFields", {
        doDeselectDatasetKeys: function(){
            if(isAllDatasetsChecked()){
                $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', false);
            }
        },
        doSelectDatasetKeys: function(){
            if(isAllDatasetsUnchecked()){
                $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', true);
            }
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