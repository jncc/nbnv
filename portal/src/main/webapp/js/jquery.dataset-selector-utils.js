(function($){
    $(document).ready(function(){
        /* This is used to check or uncheck all datasets on a selectable dataset table
         * eg as appears on a site report page.  It wires the button's click events to the
         * appropriate action
         */
        $('#nbn-select-datasets-auto').click(function(){
            $("INPUT[name='datasetKey'][type='checkbox']").attr('checked',$('#nbn-select-datasets-auto').is(':checked'));
        });
        
        /* This ensures that 'all datasetKeys' are not posted if all of them are selected,
         * the api assumes all are selected if it doesn't receive any, so it is safe.
         */
        $('#nbn-site-report-form').submit(function(){
            if(isAllDatasetsChecked()){
                $("INPUT[name='datasetKey'][type='checkbox']").attr('checked', false)
            }
            return true;
        });
        
        /* If all datasetKeys are checked then set the select/deselect check box
         * to be checked
         */
         if(isAllDatasetsChecked()){
             $('#nbn-select-datasets-auto').attr('checked', true);
         }
         
         function isAllDatasetsChecked(){
            var datasetCheckBoxes = $("INPUT[name='datasetKey'][type='checkbox']");
            var numCheckBoxes = datasetCheckBoxes.length;
            var numCheckBoxesChecked = datasetCheckBoxes.filter(':checked').length;
            return numCheckBoxes == numCheckBoxesChecked;
         }
         
    });
})(jQuery);