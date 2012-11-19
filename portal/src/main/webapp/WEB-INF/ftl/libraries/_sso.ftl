<#assign user = json.readURL("${api}/user")/>

<#if user.id == 1>
    <div class="nbn-login-text"><a href="/User/SSO/Login">Login</a></div>
<#else>
    <div class="nbn-login-text">Welcome ${user.forename} ${user.surname} (<a href="/User/SSO/Logout">Logout</a> | <a href="/User/Admin">My Account</a>)</div>
</#if>