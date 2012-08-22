<head>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/jquery-ui.min.js"></script>
    <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.3/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/js/enable-datatable.js"></script>
    <link type="text/css" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css" />
</head>

<body>
    <#assign datasets=json.readURL("${api}/datasets")>
        <table id="nbn-datatable">
            <thead>
                <tr>
                    <th>Dataset</th>
                    <th>Provider</th>
                </tr>
            </thead>
            <tbody>
                <#list datasets as dataset>
                    <tr>
                        <td>${dataset.name?html}</td>
                        <td>${dataset.organisationName?html}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
</body>
