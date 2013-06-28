<@template.master title="NBN Gateway - Join An Organisation"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/joinRequest/join.js","/js/dialog_spinner.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/dialog-spinner.css"] >

    <#assign organisations=json.readURL("${api}/organisations/joinable")>
    <#assign selected="">

    <h1>Request to join an organisation</h1>
    <div>
        <table id="nbn-org-datatable" class="nbn-dataset-table">
            <tr>
                <th>Select an Organisation</th>
                <td>
                    <select id="combobox">
                        <#list organisations?sort_by("name") as org>
                        <option <#if URLParameters.organisation?number == org.id>selected="true"</#if> value="${org.id}">${org.name}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th>Reason for Joining</th>
                <td><textarea id="nbn-org-join-request-reason" rows="5" cols="60"></textarea></td>
            </tr>
        </table>
        <input id="nbn-org-join-request-submit" data-url="${api}/organisationMemberships/" type="submit" value="Send Join Request"/>
    </div>

    <div id="nbn-org-join-request-dialog" title="Submit Request" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to submit this join request<span id="nbn-org-join-request-dialog-extra"></span>?</p>
    </div>
    <div id="nbn-org-join-request-exists-dialog" title="Join Request Already Exists" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>You already have a active join request for this organisation would you like to view this request?</p>
    </div>
    <div id="nbn-org-member-already-dialog" title="Already a Member" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>You are already a member of this organisation</p>
    </div>
    <div id="nbn-org-join-request-error-dialog" title="Submit Request - Unknown Error" style="display:none;">
        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>An unknown error has occurred, please try again later</p>
    </div>
</@template.master>
