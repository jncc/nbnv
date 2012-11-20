<#import "spring.ftl" as spring />
<@template.master title="NBN Gateway - Register">
    <@markdown>
#Register as a NBN User

Access to much of the data on the NBN Website is strictly controlled by the data
owner. To begin to gain access to this you must register by entering your 
details below. Find out how to gain access to data.
    </@markdown>
    
    <form id="login" action="/User/Register" method="POST">
        <#if message??>
            <div class="message error">${message!}</div>
        </#if>
        <@spring.formInput "user.username"/>
        <@spring.formInput "user.forename"/>
        <@spring.formInput "user.surname"/>
        <@spring.formInput "user.email"/>
        <@spring.formInput "user.phone"/>
        <@spring.formInput "user.password"/>

        <input type="submit" value="Login to the NBN Gateway"/>
    </form>
</@template.master>