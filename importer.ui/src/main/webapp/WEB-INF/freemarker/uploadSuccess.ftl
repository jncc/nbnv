<html>
    <head>
        <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
        <title>Upload Example Result</title>
    </head>
    <body>
        <p>Result:</p>
        <ul>
            <#list model.results as result>
                <li>${result}</li>
            </#list>
        </ul>
        <p>Headers:</p>
        <ul>
            <#list model.headers as header>
                <li>${header}</li>
            </#list>
        </ul>
    </body>
</html>