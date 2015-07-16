<#assign licence=json.readURL("${api}/datasetLicence/abbrv/${URLParameters.abbrv}")>

<@template.master title="NBN Gateway - Dataset Licence - ${licence.abbreviation}"
    javascripts=[] 
    csss=["/css/report.css"] >
        <h1 style="width:80%; float:left;">${licence.name}</h1>
		<#if licence.hasImg>
			<img style="max-width:20%; float:right;" src="${licence.img_href}"/>
		</#if>
		<table class="nbn-simple-table, nbn-report-dataset-table">
			<tr>
				<th>Abbreviation</th>
				<td>${licence.abbreviation}</td>
			</tr>
			<tr>
				<th>Summary</th>
				<td>${licence.summary}</td>
			</tr>
			<tr>
				<th>URL</th>
				<td><a href="${licence.href}">${licence.href}</a></td>
			</tr>
		</table>
</@template.master>