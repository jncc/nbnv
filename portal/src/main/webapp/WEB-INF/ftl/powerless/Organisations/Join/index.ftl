<@template.master title="NBN Gateway - Join An Organisation"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/joinRequest/join.js","/js/dialog_spinner.js"] 
    csss=["http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css","/css/dialog-spinner.css"] >

    <#assign organisations=json.readURL("${api}/organisations/joinable")>

    <h1>Request to join an organisation</h1>
    <div>
        <table id="nbn-org-datatable" class="nbn-dataset-table">
            <tr>
                <th>Select an Organisation</th>
                <td>
                    <select id="combobox">
                        <option value="-1">Please Select an Organisation</option>
                        <#list organisations?sort_by("name") as org>
                        <option value="${org.id}">${org.name}</option>
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
</@template.master>
