(function($) {
    $(document).ready(function() {
        $('#password').passField({
            acceptRate: 1,
            pattern: 'Abcde12',
            showToggle: false,
            showGenerate: false,
            showWarn: false,
            localeMsg: {
                noCharType: "Password should contain {}",
                passTooShort: "Password should be more than {} characters long"
            },
            locale: "en"
        });

        var container = $('#errors');

        $('#register-form').validate({
            errorLabelContainer: $('#register-form div.error'),
            submitHandler: function(form) {
                // If the phone number is blank then set the text to "No number avail"
                if ($.trim($('#phone').val()) === '') {
                    $('#phone').val('No number avail');
                }
                $('#username').val($.trim($('#username').val()));
                form.submit();
            },
            invalidHandler: function() {
                $('#errors').show();
                $('#user.errors').hide();
            },
            rules: {
                username: {
                    required: true
                },
                forename: {
                    required: true
                },
                surname: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                    minlength: 5
                },
                password_confirm: {
                    equalTo: '#password'
                }
            },
            messages: {
                username: {
                    required: 'Please enter a username'
                },
                forename: {
                    required: 'Please enter your First Name'
                },
                surname: {
                    required: 'Please enter your Last Name'
                },
                email: {
                    required: 'Please enter a valid email address',
                    email: 'Please enter a valid email address'
                },
                password: {
                    required: "Please provide a password",
                    minlength: "Your password must be at least 5 characters long"
                },
                password_confirm: {
                    equalTo: "Please enter the same password as above"
                }
            }
        });
    });
})(jQuery);
