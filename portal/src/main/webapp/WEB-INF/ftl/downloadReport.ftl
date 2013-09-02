<#assign downloadData=json.readURL(${api}/taxonObservations/downloadRecords)>

<@template.master title="NBN Gateway - Download Records"
    javascripts=[] 
    csss=[]>
    <h1>Download Reports</h1>
    <#list downloadData>
        
    </#list>
</@template.master>