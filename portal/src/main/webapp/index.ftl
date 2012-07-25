<#include "/WEB-INF/templates/master.ftl">

<@master title="National Biodiversity Network Gateway">
    <#assign youtube=json.readURL("http://gdata.youtube.com/feeds/api/users/nbngatewaydev/uploads", "GET", {
        "alt": "json"
    })>
    Some Content woo
    <ul>
        <#list youtube.feed.entry as currEntry>
            <#assign currVideo=currEntry["media$group"]>
            <li>
                <a href="${currVideo['media$player'][0].url}">
                    <img src="${currVideo['media$thumbnail'][0].url}"/>
                    ${currVideo['media$title']['$t']}
                </a>
            </li>
        </#list>
    </ul>
</@master>