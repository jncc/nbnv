(function($) {

    $(document).ready(function() {
        $('#nbn-org-metadata-edit').validate({
            submitHandler: function(form) {
                $('#nbn-waiting-ticker').show();
                $('#nbn-org-metadata-update-submit').attr('disabled', 'disabled');
                $.ajax({
                    type: 'POST',
                    url: $('#nbn-org-metadata-edit').attr('action'),
                    data: $('#nbn-org-metadata-edit').serialize(),
                    success: function() {
                        location.reload();
                    },
                    error: function(error) {
                        alert("An Error Occurred");
                        $('#nbn-waiting-ticker').hide();
                        $('#nbn-org-metadata-update-submit').removeAttr('disabled');
                    }
                });
            },
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                },
                abbreviation: {
                    maxlength: 10
                },
                address: {
                    maxlength: 200
                },
                postcode: {
                    maxlength: 10
                },
                phone: {
                    maxlength: 50
                },
                website: {
                    maxlength: 100
                },
                contactName: {
                    maxlength: 120
                },
                contactEmail: {
                    maxlength: 100
                }
            },
            messages: {
                name: {
                    required: 'Organisation MUST have a name',
                    maxlength: 'Organisation Name must be shorter than 200 characters'
                },
                abbreviation: {
                    maxlength: 'Organisation Abbreviation must be shorter than 10 characters'
                },
                address: {
                    maxlength: 'Address must be shorter than 200 characters'
                },
                postcode: {
                    maxlength: 'Postcode must be shorter than 10 characters'
                },
                phone: {
                    maxlength: 'Phone number must be shorter than 50 characters'
                },
                website: {
                    maxlength: 'Website address must be shorter than 100 characters'
                },
                contactName: {
                    maxlength: 'Contact name must be shorter than 120 characters'
                },
                contactEmail: {
                    maxlength: 'Contact email must be shorter than 100 characters'
                }
            }
        });
    });
})(jQuery);