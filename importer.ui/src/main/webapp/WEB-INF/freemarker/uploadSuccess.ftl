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
        <input type="hidden" name="filename" value="${model.fileName}" />
        <ul>
            <#list model.headers as header>
                <li>
                    ${header.columnNumber}: ${header.columnLabel} -&gt;
                    <select>
                        <#list model.fields as field>
                            <option <#if field == header.field>selected="true"</#if>>${field} </option>
                        </#list>
                    </select>
                </li>
            </#list>
        </ul>
        <p>Fields:</p>
    </body>
</html>