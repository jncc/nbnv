/* This javascript will implement JQuery tabs for a div with id=nbn-tabs*/
(function($) {
    $(document).ready(function() {
        $('#nbn-tabs').tabs({
            spinner: 'Loading <img src="/img/ajax-loader.gif"/>',
            cache: true
        });
        applyTableEvenRowStyle();

        $('#modify-details-form').submit(function() {
            // Set this to constant just to make the model pass validation, 
            // never used though
            $('#default_password').val("0");
            // If the phone number is blank then set the text to "No number avail"
            if ($('#phone').val().trim() === '') {
                $('#phone').val('No number avail');
            }
            return true;
        });

        $('#modify-details-form').validate({
            submitHandler: function(form) {
                // Set this to constant just to make the model pass validation, 
                // never used though
                $('#default_password').val("0");
                // If the phone number is blank then set the text to "No number avail"
                if ($('#phone').val().trim() === '') {
                    $('#phone').val('No number avail');
                }
                form.submit();
            },
            rules: {
                forename: {
                    required: true
                },
                surname: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                }
            },
            messages: {
                forename: {
                    required: 'Please enter your First Name'
                },
                surname: {
                    required: 'Please enter your Last Name'
                },
                email: {
                    required: 'Please enter an email address',
                    email: 'Please enter a valid email address'
                }
            }
        });

        $("#change-password-form").validate({
            submitHandler: function(form) {
                // Set this to constant just to make the model pass validation, 
                // never used though
                $('#token').val('change');
                form.submit();
            },
            rules: {
                password: {
                    required: true,
                    minlength: 5
                },
                password_confirm: {
                    required: true,
                    minlength: 5,
                    equalTo: "#password"
                }
            },
            messages: {
                password: {
                    required: "Please provide a password",
                    minlength: "Your password must be at least 5 characters long"
                },
                password_confirm: {
                    required: "Please provide a password",
                    minlength: "Your password must be at least 5 characters long",
                    equalTo: "Please enter the same password as above"
                }
            }
        });
    });

    function applyTableEvenRowStyle() {
        $(".nbn-simple-table tr:even").addClass("ui-state-highlight");
    }
})(jQuery);
