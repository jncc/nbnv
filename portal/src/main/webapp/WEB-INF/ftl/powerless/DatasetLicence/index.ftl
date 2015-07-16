<#assign licences=json.readURL("${api}/datasetLicence")>

<@template.master title="NBN Gateway - Dataset Licences"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/"] 
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css"] >
	<h1>Dataset Licences</h1>
	<#list licences as licence>
		<table class="nbn-simple-table">
			<tr>
				<td>
					<#if licence.hasImg>
						<img class="nbn-provider-table-logo" src="${licence.img_href}"/>
					</#if>
					<a href="/DatasetLicence/${licence.abbreviation}">${licence.name} (${licence.abbreviation})</a>
				</td>
			</tr>
		</table>
	</#list>
</@template.master>