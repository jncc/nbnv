<#include "master.ftl">
<@master title="Designation Categories">

        <p>Test message: test</p>
        <#list dcl as dc>
            <p>${dc.label}</p>
        </#list>

</@master>