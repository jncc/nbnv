<#assign user = json.readURL("${api}/user")/>

<#if user.id == 0>
    <a href="/User/SSO">Login</a>
<#else>
    Welcome ${user.forename} (<a href="/User/Logout">Logout</a>)
</#if>