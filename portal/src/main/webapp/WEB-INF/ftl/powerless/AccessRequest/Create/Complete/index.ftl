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

            $.ajax({
                type: "PUT",
                url: url,
                contentType: 'application/json',
                processData: false,
                data: JSON.stringify(data),
                success: function() {
                    $('#debug-data').append("Success");
                },
                error: function() {
                    $('#debug-data').append("Fail");
                }
            });
        });

    </script>
    <h1>Access Request Submitted</h1>
    <div style="padding-bottom: 40px;">TODO: Blurb on access request process, what happens next etc</div>
    <div id="debug-data">
        ${data}
    </div>
</@template.master>
