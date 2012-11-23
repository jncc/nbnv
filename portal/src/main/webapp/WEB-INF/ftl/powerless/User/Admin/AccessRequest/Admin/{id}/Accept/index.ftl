<#assign id="${URLParameters.id}">
<#assign r=json.readURL("${api}/user/userAccesses/requests/${id}") />

<@template.master title="Requests for Datasets"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

    <h1>Accept Access Request</h1>
    <@accessrequest_utils.userRequest request=r controls=false/>
    <p>Access Expires:</p>
    <input type="text" id="nbn-admin-accept-expires" />
    <p>Message:</p>
    <textarea cols="75" rows="15" id="nbn-admin-accept-reason"></textarea>
    <div><button id="nbn-admin-accept-grant">Grant Request</button></div>

    <script>
    $(function() {
        $("#nbn-admin-accept-expires").datepicker({
            changeMonth: true,
            changeYear: true,
            dateFormat: "dd/mm/yy"
        });

        $("#nbn-admin-accept-grant").click(function() {
            var request = $.ajax({
                type: "POST",
                url: "${api}/user/userAccesses/requests/${id}",
                data: { action : 'accept', expires : $("#nbn-admin-accept-expires").val(), reason : $("#nbn-admin-accept-reason").val() }
            });

            request.done( function(data) { window.location = '/User/Admin/AccessRequests/Admin'; });
        });
    });
    </script>
</@template.master>
