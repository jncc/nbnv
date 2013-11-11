<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NBN GIS</title>
    </head>
    <body>
        <h1>Welcome to the NBN GIS Application</h1>
        <p>
            Below is a list of the map service methods which are registered to 
            this application.
        </p>
        <table border="1">
            <tr><th>Service</th><th>Query params</th><th>User login</th></tr>
            <#list mapMethods as mapMethod>
                <tr>
                    <td>${mapMethod.service}</td>
                    <td>
                        <ul>
                            <#list mapMethod.queryParams as queryParam>
                                <li>${queryParam.value()}</li>
                            </#list>
                        </ul>
                    </td>
                    <td>${mapMethod.affectedByUser?string("yes","no")}</td>
                </tr>
            </#list>
        </table>
    </body>
</html>