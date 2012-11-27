<#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />

<@template.master title="NBN Gateway - Recover your details">
    <@markdown>
#Can't Access your account?

If you can't access your account then please fill in the form below to resolve
the issue.
    </@markdown>

    <@form.form method="POST" id="recovery-form" commandName="recoveryRequest" action="/User/Recovery">
        <@form.errors path="*" cssClass="message error" element="div" />
        <fieldset>
            <legend>What have you forgotten?</legend>
            <label for="forgotten-password">Password</label> 
            <@form.radiobutton id="forgotten-password" path="forgotten" value="password"/>
            <label for="forgotten-username">Username</label> 
            <@form.radiobutton id="forgotten-username" path="forgotten" value="username"/>
        </fieldset>
        <label for="email">Your E-mail</label>           <@form.input path="email"/>

        <input type="submit" value="Send"/> <a href="/">Cancel</a>
    </@form.form>
</@template.master>