<@template.master title="NBN Gateway - Organisations Administration"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/organisation.css","/css/org-admin.css"]>

    <#assign organisationId="${.data_model['organisationID']}">
    <#assign user=json.readURL("${api}/user")>
    <#assign organisation=json.readURL("${api}/organisations/${organisationId}")>
    <#assign joinRequest=json.readURL("${api}/organisationJoinRequest/${organisationId}/${user.id}")>
    <#assign requestingUser=json.readURL("${api}/user/${joinRequest.userID}")>
    <#assign username="${requestingUser.forename} ${requestingUser.surname}">

    <h1>Join Request for ${organisation.name}</h1>
    
    <p>Request to join made by ${username} at ${joinRequest.requestDate}</p>
    <p>${joinRequest.requestReason}</p>

    <#if user.id == requestingUser.id>
        <#switch joinRequest.responseTypeID>
            <#case joinRequest.responseTypeID = 0>
                <p>No response received</p>
                <input id="withdrawRequestButton" data-user="${user.id}" data-org="${organisationId}" type="button" value="Withdraw this request" />
                <div id="nbn-org-join-confirm" style="display:none;">
                    <p>Are you sure that you would like to withdraw this request?</p>
                </div>
                <#break>
            <#case joinRequest.responseTypeID = 1>
                <p>Request has been accepted on ${joinRequest.responseDate}<p>
                <p>${joinRequest.responseReason}</p>
                <#break>
            <#default>
                <p>Your request to join this organisation was rejected on ${joinRequest.responseDate} for the following reason;<p>
                <p>${joinRequest.responseReason}</p>
        </#switch>
    <#else>
        <#switch joinRequest.responseTypeID>
            <#case joinRequest.responseTypeID = 0>
                <input id="nbn-org-join-accept" data-user="${requestingUser.id}" data-org="${organisationId}" type="button" value="Accept" />
                <input id="nbn-org-join-decline" data-user="${requestingUser.id}" data-org="${organisationId}" type="button" value="Decline" />
                <div id="nbn-org-join-confirm" style="display:none;">
                    <p>Are you sure that you would like to <span id="nbn-org-join-dialog-type"></span> this request? Please enter any further details below</p>
                    <textarea id="nbn-org-join-dialog-reason" rows="5" cols="50"></textarea>
                </div>
                <#break>
            <#case joinRequest.responseTypeID = 1>
                <p>Request has been accepted on ${joinRequest.responseDate}<p>
                <p>${joinRequest.responseReason}</p>
                <#break>
            <#default>
                <p>Request was rejected on ${joinRequest.responseDate} for the following reason;<p>
                <p>${joinRequest.responseReason}</p>
        </#switch>        
    </#if>
</@template.master>
