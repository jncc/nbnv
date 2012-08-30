<%@page import="org.json.JSONObject"%>
<%@page import="nbn.webmapping.params.ParameterNormaliserFactory"%>
<%@page import="nbn.webmapping.params.ParameterNormaliser"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
	<title>NBN Interactive Map</title>
        <link href="css/unavailable.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/InteractiveMappingClientImport.js"></script>
	<script type="text/javascript">
	    function initalize() {
                <%ParameterNormaliser normaliser = ParameterNormaliserFactory.createParameterNormaliser(request.getParameterMap());%>
                <%try {%>
                    <%String jsonConstructionArguments = normaliser.createInteractiveMapOptionsJSONString(); //attempt the construct the json string before writting the interactive map constructor%>
                    interactiveMap = new nbn.mapping.InteractiveMappingClient($('.fullscreenMap'), <%=jsonConstructionArguments%>);
                <%} catch(Exception serverSideEx) {%>
                    interactiveMap = new nbn.mapping.InteractiveMappingClient($('.fullscreenMap'));
                    interactiveMap.Logger.error("A error occured whilst parsing the query string", <%=JSONObject.quote(serverSideEx.getMessage())%>);
                <%}%>

                $(window).resize(function() {
                    interactiveMap.StaticGridLayoutManager.validate();
                });
	    }
	</script>
    </head>
    <body onLoad="initalize();">
        <div class="fullscreenMap" id="map_canvas"></div> <!-- google maps-->
        <noscript>
            <div id="unavailable">
                <div class="container">
                    <div class="header">The Interactive Map Tool requires JavaScript</div>
                    <p class="message">It appears that you don't have JavaScript enabled. This is essential for the interactive map to function correctly. Please enable JavaScript and then refresh the page.</p>
                    <a class="backToNBN" alt="Please click me to go back to the NBN Gateway" href="http://data.nbn.org.uk">Please click to go back to the NBN Gateway</a>
                </div>
            </div>
        </noscript>
    </body>
</html>