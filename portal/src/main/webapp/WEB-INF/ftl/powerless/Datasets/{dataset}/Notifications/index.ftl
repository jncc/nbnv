<#assign downloadNotification=json.readURL("${api}/${URLParameters.dataset}/userDownloadNotification")>
<@template.master title="NBN Gateway - Dataset Administration"
    javascripts=["/js/download/notifications.js"] 
    csss=[] >
    <script>
        nbn.nbnv.api = '${api}';
        nbn.nbnv.dataset = '${api}';
    </script>
    <div class="tabbed nbn-organisation-tabbed">
        <h3>Download Notifications</h3>
        <input id="nbn-download-notification-checkbox" type="checkbox" checked="<#if downloadNotification>checked</#if>" /><br />
        <button id="nbn-download-notification-submit" type="submit">Save Changes</button>
        <span id="nbn-download-notification-working"></span>
    </div>
</@template.master>