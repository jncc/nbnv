(function($){


    function isAllDatasetsUnchecked(){
       var datasetCheckBoxes = $("INPUT[name='datasetKey'][type='checkbox']");
       var numCheckBoxesChecked = datasetCheckBoxes.filter(':checked').length;
       return numCheckBoxesChecked == 0;
    }
    
    /*This is useful for checking all dataset check boxes when none are selected.
     *This looks nicer for the user since non-selected is the same as using all.
     */
    namespace("nbn.portal.reports.utils.DatasetFields", {
        doSelectDatasetKeys: function(){
            if(isAllDatasetsUnchecked()){
                   $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', true);
            }
        }
    });

    function isAllDatasetsChecked(){
       var datasetCheckBoxes = $("INPUT[name='datasetKey'][type='checkbox']");
       var numCheckBoxes = datasetCheckBoxes.length;
       var numCheckBoxesChecked = datasetCheckBoxes.filter(':checked').length;
       return numCheckBoxes == numCheckBoxesChecked;
    }
    
    /* This ensures that 'all datasetKeys' are not posted if all of them are selected,
     * the api assumes all are selected if it doesn't receive any, so it is safe.
     * It is added to theSubmit event of report site and grid map forms.
     */
    namespace("nbn.portal.reports.utils.DatasetFields", {
        doDeselectDatasetKeys: function(){
            if(isAllDatasetsChecked()){
               $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', false);
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