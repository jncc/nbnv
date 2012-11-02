<html>
    <head>
        <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
        <title>Upload Example Result</title>
        <script type="text/javascript" src="/importer/jquery.js"></script>
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
            <input id="wrangle" type="submit" value="Wrangle!" />
            <img id="wrangleSpinnger" src="/importer/images/1-1.gif" />
        </form>

        <script>
            $(document).ready(function() {
                $("#wrangleSpinnger").hide();
            });

            $("#wrangle").click(function() {
                $("#wrangleSpinnger").show();
            });
        </script>
    </body>
</html>