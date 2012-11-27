<#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />

<@template.master title="NBN Gateway - Recover your details">
    <@markdown>
#Change your password

Use the form below to set a new password for your account
    </@markdown>

    <@form.form method="POST" id="recovery-form" commandName="changePassword" action="/User/Recovery/Password">
        <@form.errors path="*" cssClass="message error" element="div" />
        
        <label for="username">Your Username</label> <@form.input path="username"/>
        <label for="password">New Password</label> <@form.password path="password"/>

        <@form.hidden path="token"/>
        <input type="submit" value="Set Password"/> <a href="/">Cancel</a>
    </@form.form>
</@template.master>