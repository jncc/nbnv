<#assign data="${RequestParameters.json}" />

<@template.master title="Conditions"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/admin/access/util/requestJsonToText.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

    <script>
        nbn.nbnv.api = '${api}';


        $(function() {
            var userUrl = nbn.nbnv.api + '/user/userAccesses/requests';
            var orgUrl = nbn.nbnv.api + '/organisation/organisationAccesses/requests';
            var data = ${data};
            var url = ('organisationID' in data.reason && data.reason.organisationID != -1) ? orgUrl : userUrl;

            $('#submit-link').hide();

            $.ajax({
                type: "PUT",
                url: url,
                contentType: 'application/json',
                processData: false,
                data: JSON.stringify(data),
                success: function() {
                    $('#submit-status').html('');
                    $('#submit-status').append("Your access request has successfully been submitted and sent to all the relevant dataset administrators. A summary of this request has been added to your list of pending access requests, available through your user account page. You will be notified of the dataset administrators decisions (either accepting or rejecting your request) for each dataset by email. Please ensure your use of the data complies with the ")
                        .append($('<a>')
                            .attr('href', '/Terms')
                            .text('NBN Gateway Terms and Conditions')
                        ).append('.');

                    $('#submit-link').show();
                },
                error: function(error) {
                    $('#submit-status').html('');
                    
                    var obj = JSON.parse(error.responseText);

                    if (obj.status.match('^Zero records would be granted in')) {
                        $('#submit-status').append(obj.status + ', please amend your request and re-submit. If you believe this was in error please submit a screenshot of this page to access@nbn.org.uk and we will look into it')
                            .append($('<p>').text(error.responseText))
                            .append($('<p>')
                                .append('${data}')
                            );
                    }
                    else {
                        $('#submit-status').append("Your access request has failed to be submitted correctly. Please submit a screenshot of this page to access@nbn.org.uk and we will look into it.")
                            .append($('<p>').text(error.responseText))
                            .append($('<p>')
                                .append('${data}')
                            );
                    }

                    $('#submit-link').show();
                }
            });
        });

    </script>
    <h1>Access Request Submission</h1>
    <p id="submit-status">Submitting request...</p>
    <p id="submit-link"><a href="/User">Return to My Account</a></p>
</@template.master>
