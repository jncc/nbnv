<#import "spring.ftl" as spring />
<@template.master title="National Biodiversity Network Gateway">
    <@markdown>
#Login to the NBN Gateway

Please enter your username and password in order to login to the NBN Gateway 
    </@markdown>
    
    <form id="login-form" action="/User/SSO/Login" method="POST">
        <#if message??>
            <div class="message error">${message!}</div>
        </#if>
        <label for="username">Username</label><input type="text" name="username" value="" placeholder="User name"/>
        <label for="password">Password</label><input type="password" name="password" value="" placeholder="Password"/>
        
        <input type="submit" value="Login to the NBN Gateway"/>
        
        <div class="sign-up">
            <a href="/User/Register">Need an account? Sign up free.</a>
        </div>
    </form>
</@template.master>