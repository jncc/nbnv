<@template.master title="NBN Gateway - Datasets" 
    javascripts=["/js/jquery.dataTables.min.js","/js/enable-datasets-datatable.js","/js/jquery.datatables.customDate.js"] 
    csss=[] >

    <#assign datasets=json.readURL("${api}/datasets")>
	<@template.datasetsPage "Datasets" datasets />
	
	<p>Find out more about NBN Dataset Licencing <a href="/DatasetLicence">here</a></p>
</@template.master>
