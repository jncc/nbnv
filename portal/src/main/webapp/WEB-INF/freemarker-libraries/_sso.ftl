<#if RequestParameters.username?has_content && RequestParameters.password?has_content>
    ${json.readURL("${api}/user/login", RequestParameters)}
</#if>

${json.readURL("${api}/user")("toString")}

<form>
    <input type="text" name="username" value=""/>
    <input type="password" name="password" value=""/>
    <input type="submit" value="Login"/>
</form>