<#assign user = json.readURL("${api}/user")/>

<div id="nbn-login">
    <#if user.id == 1>
        <a href="/User/SSO/Login">Login</a> | <a href="/User/Register">Register</a>
    <#else>
        Welcome ${user.forename} ${user.surname} (<a href="/User/SSO/Logout">Logout</a> | <a href="/User/Admin">My Account</a>)
    </#if>
</div>