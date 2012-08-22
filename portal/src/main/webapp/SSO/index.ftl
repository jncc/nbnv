<@template.master title="National Biodiversity Network Gateway">
    <@markdown>
#Welcome to the NBN Gateway

Please enter your username and password in order to login to the NBN Gateway 
    </@markdown>

    <#if RequestParameters.username?has_content && RequestParameters.password?has_content>
        ${json.readURL("${api}/user/login", RequestParameters)}
    </#if>

    <form>
        <input type="text" name="username" value=""/>
        <input type="password" name="password" value=""/>
        <input type="submit" value="Login"/>
    </form>
</@template.master>