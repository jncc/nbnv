<#macro userInfoDialog> 
    <div id="nbn-further-info-user" title="User Information" style="display:none">
        <table class="nbn-dataset-table">
            <tr>
                <td>User Name:</td>
                <td id="nbn-further-info-user-name"></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td id="nbn-further-info-user-email"></td>
            </tr>
            <tr><td></td><td></td></tr>
            <tr>
                <td id="nbn-further-info-user-org-desc">Organisation Memberships:</td>
                <td id="nbn-further-info-user-orgs"></td>
            </tr>
        </table>        
    </div>
</#macro>

<#macro downloadDialogs>
    <div id="preparing-download-dialog" title="Preparing Download..." style="display: none;">
        <p>We are preparing your download, please wait</p>
        <img style="display:block;margin-left: auto;margin-right: auto;" src="/img/ajax-loader-medium.gif" />
    </div>

    <div id="error-download-dialog" style="display:none;">
        <p>There was a problem generating your download:</p>
        <p id="error-download-reason"></p>
    </div>
</#macro>