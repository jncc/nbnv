<#import "spring.ftl" as spring />
<@template.master title="National Biodiversity Network Gateway">
    <@markdown>
#Login to the NBN Gateway

Please enter your username and password in order to login to the NBN Gateway 
    </@markdown>
    
    <form id="login-form" action="/User/SSO/Login" method="POST">
        <#if status??>
            <div class="message error">${status!}</div>
        </#if>
        <label for="username">Username</label><input type="text" name="username" value="" placeholder="User name"/>
        <label for="password">Password</label><input type="password" name="password" value="" placeholder="Password"/>
        <input id="remember" type="checkbox" name="remember" value="true"/><label for="remember">Remember my login on this computer</label>
        <input type="hidden" name="redirect" value="${redirect}"/>
        <input type="submit" value="Login to the NBN Gateway"/>
        <a href="/User/Recovery">Can't access your account?</a>
        
        <div class="sign-up">
            <a href="/User/Register">Need an account? Sign up free.</a>
        </div>
    </form>
</@template.master>