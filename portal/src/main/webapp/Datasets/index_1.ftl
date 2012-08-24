<html>
<head>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/js/enable-datatable.js"></script>
    <link type="text/css" rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/smoothness/jquery-ui.css" />

<style type="text/css" media="screen">
    div.dataTables_length {
            float: left;
    }
    div.dataTables_filter {
            float: right;
    }

    div.dataTables_info {
            padding: 9px 6px 6px 6px;
            float: left;
    }
    .dataTables_paginate {
            width: 44px; height:19px;
            float: right;
    }
    .paging_two_button {
            cursor: pointer;
    }

    .paginate_disabled_previous {
    width:19px; height:19px;
    float:left;
            background-image: url('/img/datatable/back_disabled.png');
    }

    .paginate_enabled_previous {
    width:19px; height:19px;
    float:left;
            background-image: url('/img/datatable/back_enabled.png');
    }

    .paginate_disabled_next {
    width:19px; height:19px;
    float:right;
            background-image: url('/img/datatable/forward_disabled.png');
    }

    .paginate_enabled_next {
    width:19px; height:19px;
    float:right;
            background-image: url('/img/datatable/forward_enabled.png');
    }

    .sorting_asc {
            padding-right:18px;
            cursor: pointer;
            background: url('/img/datatable/sort_asc.png') no-repeat top right;
    }

    .sorting_desc {
            padding-right:18px;
            cursor: pointer;
            background: url('/img/datatable/sort_desc.png') no-repeat top right;
    }

    .sorting {
            padding-right:18px;
            cursor: pointer;
            background: url('/img/datatable/sort_both.png') no-repeat top right;
    }

    .sorting_asc_disabled {
            padding-right:18px;
            cursor: pointer;
            background: url('/img/datatable/sort_asc_disabled.png') no-repeat top right;
    }

    .sorting_desc_disabled {
            padding-right:18px;
            cursor: pointer;
            background: url('/img/datatable/sort_desc_disabled.png') no-repeat top right;
    }
</style>


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
</html>