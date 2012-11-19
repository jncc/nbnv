<@template.master title="National Biodiversity Network Gateway">
    <@markdown>
#Welcome to the NBN Gateway

Please enter your username and password in order to login to the NBN Gateway 
    </@markdown>
${message!}
    <form action="/User/SSO/Login" method="POST">
        <input type="text" name="username" value=""/>
        <input type="password" name="password" value=""/>
        <input type="submit" value="Login"/>
    </form>
</@template.master>