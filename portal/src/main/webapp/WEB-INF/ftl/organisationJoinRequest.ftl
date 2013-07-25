<#assign requestingUser=request.user>
<#assign username="${requestingUser.forename} ${requestingUser.surname}">

<h1>Join Request for ${organisation.name}</h1>

<div id="nbn-org-join-request-details" class="tabbed nbn-organisation-tabbed">
    <h3>Request Details</h3>
    <table class="nbn-simple-table"> 
        <tbody>
            <tr>
                <td>
                    <p>Request to join made by ${username} (${requestingUser.email}) on ${request.requestDate}</p>
                </td>
            </tr>
            <tr>
                <td>
                    <#if (request.requestReason?length > 0)>
                        <p>${request.requestReason}</p>
                    <#else>
                        <p>No Reason Given</p>
                    </#if>
                </td>
            </tr>
        </tbody>
    </table>
</div>

<div id="nbn-org-join-request-actions" class="tabbed nbn-organisation-tabbed">
    <h3>Possible Actions</h3>
    <#if user.id == requestingUser.id>
        <div id="nbn-org-join-action" data-requestor="true">
            <#switch request.responseTypeID>
                <#case 0>
                    <p>No response received</p>
                    <input id="nbn-org-join-withdraw" data-id="${request.id}" data-url="${api}/organisationMemberships/request/${request.id}" type="button" value="Withdraw this request" />
                    <div id="nbn-org-join-withdraw-dialog" style="display:none;">
                        <p>Are you sure that you would like to withdraw this request?</p>
                    </div>
                    <#break>
                <#case 1>
                    <p>Request has been accepted on ${request.responseDate}<p>
                    <p>${request.responseReason}</p>
                    <#break>
                <#case 3>
                    <p>You withdrew this request on ${request.responseDate}</p>
                    <#break>
                <#default>
                    <p>Your request to join this organisation was rejected on ${request.responseDate} for the following reason;<p>
                    <p>${request.responseReason}</p>
            </#switch>
        </div>
    <#else>
        <div id="nbn-org-join-action" data-id=${request.id}>
            <#switch request.responseTypeID>
                <#case 0>
                    <input id="nbn-org-join-accept" data-url="${api}/organisationMemberships/request/${request.id}" type="button" value="Accept" />
                    <input id="nbn-org-join-decline" data-url="${api}/organisationMemberships/request/${request.id}" type="button" value="Decline" />
                    <div id="nbn-org-join-reason-dialog" style="display:none;">
                        <p>Please enter any details as to why you want to <span class="nbn-org-join-dialog-type"></span> this request below;</p>
                        <textarea id="nbn-org-join-reason-text" rows="5" cols="50"></textarea>
                    </div>
                    <div id="nbn-org-join-reason-check-dialog" style="display:none;">
                        <p>Are you sure that you want to <span class="nbn-org-join-dialog-type"></span> this request <span id="nbn-org-join-dialog-reason"></span></p>
                        <p id="nbn-org-join-dialog-reason-text"></p>
                    </div>
                    <#break>
                <#case 1>
                    <#if request.responseReason?length == 0>
                        <p>Request was accepted on ${request.responseDate} and no reason was given.<p>
                        <p>${request.responseReason}</p>
                    <#else>
                        <p>Request has been accepted on ${request.responseDate}<p>
                        <p>${request.responseReason}</p>
                    </#if>
                    <#break>
                <#case 3>
                    <p>Request was withdrawn by the user on ${request.responseDate}</p>
                    <#break>
                <#default>
                    <#if request.responseReason?length == 0>
                        <p>Request was rejected on ${request.responseDate} and no reason was given.<p>
                        <p>${request.responseReason}</p>
                    <#else>
                        <p>Request was rejected on ${request.responseDate} for the following reason;<p>
                        <p>${request.responseReason}</p>
                    </#if>
            </#switch>        
        </div>
    </#if>
</div>
