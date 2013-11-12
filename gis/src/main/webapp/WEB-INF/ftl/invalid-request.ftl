<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NBN GIS - Bad request</title>
    </head>
    <body>
        <h1>Invalid Request</h1>
        The given parameters are not valid:
        <ul>
            <#list exception.getConstraintViolations() as constraint>
                <li>
                    <#if constraint.getInvalidValue()?is_string>
                        ${constraint.getInvalidValue()}
                    <#elseif constraint.getInvalidValue()?is_sequence>
                        ${constraint.getInvalidValue()?join(' ')}
                    </#if>
                </li>
            </#list>
        </ul>
    </body>
</html>