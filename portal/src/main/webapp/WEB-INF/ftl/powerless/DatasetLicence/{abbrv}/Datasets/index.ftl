<@template.master title="NBN Gateway - Datasets" 
    javascripts=["/js/jquery.dataTables.min.js","/js/enable-datasets-datatable.js","/js/jquery.datatables.customDate.js"] 
    csss=[] >

    <#assign datasets=json.readURL("${api}/datasets/licenced/${URLParameters.abbrv}")>
	<@template.datasetsPage "Datasets Licenced Under ${URLParameters.abbrv}" datasets />

</@template.master>

