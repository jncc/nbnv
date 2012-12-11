<#assign statistics = json.readURL("${api}/statistics")/>

<@template.master title="National Biodiversity Network Gateway">
    <div style="float:left; font-size:x-large; width: 678px;">
        <@markdown>
#Welcome to the NBN Gateway

This is the National Biodiversity Network's Gateway. Use it to explore UK 
biodiversity data, as contributed by participating data providers.
        </@markdown>
    </div>

    <div style="float: right;
                margin-right: 5em;
                margin-top: 1em;
                font-size: smaller;">
        <h1 style="background: url('img/statistics.png') no-repeat left top; 
                    padding-left: 35px;">Gateway Statistics</h1>
        <table>
            <#list statistics?keys as stat>
                <tr>
                    <td>${stat}</td>
                    <td>${statistics[stat]}</td>
                </tr>
            </#list>
        </table>
    </div>

    <div id="slides">
        <div class="slides_container">
            <div>
                <img src="http://placehold.it/1000x200">
            </div>
        </div>
    </div>

    <h1>Latest updated Datasets</h1>
    <ul>
        <#list json.readURL("${api}/datasets/latest") as dataset>
            <#assign organisation = json.readURL(dataset.organisationHref)>
            
            <li><img src="${organisation.smallLogo!""}"> ${organisation.name}  <a href="${dataset.href}">${dataset.title}</a> ${dataset.formattedDateUploaded}</li>

        </#list>
    </ul>
The following pages will demonstrate the development of the NBN Gateway.
    </@markdown>
</@template.master>