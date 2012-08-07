<html>
    <head>
        <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
        <title>Upload Example Result</title>
    </head>
    <body>
        <p>Steps:</p>
        <ul>
            <#list model.steps as result>
                <li>${result}</li>
            </#list>
        </ul>
        <p>Errors:</p>
        <ul>
            <#list model.errors as result>
                <li>${result}</li>
            </#list>
        </ul>
        <p>Result:</p>
        <form action="download.html" method="POST">
            <input type="hidden" name="file" value="${model.archive}"/>
            <input type="submit" value="Download"/>
        </form>
    </body>
</html>