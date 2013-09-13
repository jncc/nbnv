<#assign downloadNotification=json.readURL("${api}/datasets/${URLParameters.dataset}/userDownloadNotification")>
<@template.master title="NBN Gateway - Dataset Administration"
    javascripts=["/js/download/notifications.js"] 
    csss=[] >
    <script>
        nbn.nbnv.api = '${api}';
        nbn.nbnv.dataset = '${URLParameters.dataset}';
        nbn.nbnv.downloadNotifications = <#if downloadNotification>true<#else>false</#if>;
    </script>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>Download Notifications</h3>
        <input id="nbn-download-notification-checkbox" type="checkbox" <#if downloadNotification>checked="checked"</#if> /> <span> Please check this box if you want to receive an email every time a user downloads part of your dataset<br />
        <button id="nbn-download-notification-submit" type="submit">Save Changes</button><span id="nbn-download-notification-working"></span>
    </div>
</@template.master>