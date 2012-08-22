<#assign user = json.readURL("${api}/user")/>

<#if user.id == 0>
    <a href="/SSO">Login</a>
<#else>
    Welcome ${user.forename}
</#if>