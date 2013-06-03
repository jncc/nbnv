(function($) {
    $(document).ready(function() {
        $('#nbn-modify-survey-metadata').validate({
            submitHandler: function(form) {
                $('#nbn-waiting-ticker').show();
                $('#nbn-survey-metadata-update-submit').attr('disabled','disabled');
                $.ajax({
                    type: 'POST',
                    url: $('#nbn-modify-survey-metadata').attr('action'),
                    data: $('#nbn-modify-survey-metadata').serialize(),
                    success: function() {
                        location.reload();
                    },
                    error: function(error) {
                        alert("An Error Occurred");
                        $('#nbn-waiting-ticker').hide();
                        $('#nbn-survey-metadata-update-submit').removeAttr('disabled');
                    }
                });
            },
            rules: {
                providerKey: {
                    maxlength: 100
                },
                title: {
                    maxlength: 200
                }
            },
            messages: {
                providerKey: {
                    maxlength: 'Provider Key must be less than 100 characters'
                },
                title: {
                    maxlength: 'Title must be shorter than 200 characters'
                }
            }
        });
    });
})(jQuery);

