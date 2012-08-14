<#function queryString parameterMap>
    <#list parameterMap?keys as parameter>
        <#list parameterMap[parameter] as parameterVal>
            <#-- Check if the we are dealing with the first output parameter -->
            <#if parameter_index==0 && parameterVal_index==0>
                <#assign toReturn = "?${parameter}=${parameterVal}"/>
            <#else>  
                <#assign toReturn = "${toReturn}&${parameter}=${parameterVal}"/>
            </#if>
        </#list>
    </#list>
    <#return toReturn/>
</#function>