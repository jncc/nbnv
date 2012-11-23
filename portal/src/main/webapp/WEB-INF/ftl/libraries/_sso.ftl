<#assign user = json.readURL("${api}/user")/>

<div>
    <#if user.id == 1>
        <a href="/User/SSO/Login">Login</a>
    <#else>
        Welcome ${user.forename} ${user.surname} (<a href="/User/SSO/Logout">Logout</a> | <a href="/User/Admin">My Account</a>)
    </#if>
</div>