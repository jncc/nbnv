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
        <form action="compile.html" method="POST">
        <input type="hidden" name="filename" value="${model.fileName}" />
        <ul>
            <#list model.headers as header>
                <li>
                    ${header.columnNumber}: ${header.columnLabel} -&gt;
                    <select name="${header.columnNumber}">
                        <#list model.fields as field>
                            <option value="${field.name()}" <#if field == header.field>selected="true"</#if>>${field} </option>
                        </#list>
                    </select>
                </li>
            </#list>
        </ul>
        <input type="submit" value="Wrangle!" />
        </form>
    </body>
</html>