<@template.master title="National Biodiversity Network Gateway">
    <@markdown>
#Login to the NBN Gateway

Please enter your username and password in order to login to the NBN Gateway 
    </@markdown>
    
    <form id="login" action="/User/SSO/Login" method="POST">
        <#if message??>
            <div class="message error">${message!}</div>
        </#if>
        <input type="text" name="username" value="" placeholder="User name"/>
        <input type="password" name="password" value="" placeholder="Password"/>
        <input type="submit" value="Login to the NBN Gateway"/>
    </form>
</@template.master>