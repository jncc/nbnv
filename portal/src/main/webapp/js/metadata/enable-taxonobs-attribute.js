(function($) {
    $(document).ready(function() {
        $('#nbn-dataset-attribute-modify').validate({
            submitHandler: function(form) {
                $('#nbn-waiting-ticker').show();
                $('#nbn-modify-submit').attr('disabled','disabled');
                $.ajax({
                    type: 'POST',
                    url: $('#nbn-dataset-attribute-modify').attr('action'),
                    data: $('#nbn-dataset-attribute-modify').serialize(),
                    success: function() {
                        location = '/Datasets/' + dataset + '/Attributes/Edit';
                    },
                    error: function(error) {
                        alert("An Error Occurred");
                        $('#nbn-waiting-ticker').hide();
                        $('#nbn-modify-submit').removeAttr('disabled');
                    }
                });
            }
        });
    });
})(jQuery);