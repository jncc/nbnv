<#assign data="${RequestParameters.json}" />
<#assign src="${RequestParameters.src}" />

<@template.master title="Access Request"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js","/js/admin/access/warning.js","/js/admin/access/util/requestJsonToText.js","/js/admin/access/privileges.js","/js/admin/access/privilege.js","/js/admin/access/requestedAccess.js","/js/admin/access/accessRequest.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css","/css/proto-contols.css"]>

	<script>
	$(function() {
                nbn.nbnv.api = '${api}';
                var json = ${data!'{}'};
                var src = '${src!0}';

                var req = new nbn.nbnv.ui.accessRequest();
                var reqd = new nbn.nbnv.ui.requestedAccess(json);
                var privs = new nbn.nbnv.ui.privileges(json);
                privs.setAccessRenderOnly();

                $('#requested').append(reqd._render());
                $('#accesses').append(privs._render());
                $('#request').append(req._render());
                
                if (src == 1) {
                    $('#back').click(function() {
                        window.location = "..?json=" + (JSON.stringify(json, null, '\t'));
                    });
                } else if (src == 0) {
                    $('#back').click(function() {
                        window.location = "/User/Admin/AccessRequest/Create?json=" + (JSON.stringify(json, null, '\t'));
                    });
                }
                $('#next').click(function() {
                    json.request = req.getJSON();
                    window.location = "../Complete?json=" + (JSON.stringify(json, null, '\t'));
                });
                $('#cancel').click(function() {
				window.location = "/";
			}
		);
	});
	</script>

	<h1>Access Request</h1>
        <div id="requested"></div>

        <div id="accesses"></div>
        <div style="margin-top: 0.3em;"></div>
        <div id="request"></div>
	<button id="back">&lt; Back</button><button id="next">Request Access</button><button id="cancel">Cancel</button>
</@template.master>