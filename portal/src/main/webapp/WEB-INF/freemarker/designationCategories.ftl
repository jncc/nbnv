<#include "master.ftl">
<#assign contentTitle="Designation Categories">
<@master title="${contentTitle}">

        <h4>${contentTitle}</h4>
        <p class="greyBlock">Browse these designation categories to find the designations you need. 
           Select individual designations to obtain more information about them and associated species.</p>
        <div class="greyBlock">
            <ul>
            <#list dcl as dc>
                <li>${dc.label}
            </#list>
            </ul>
        </div>

</@master>