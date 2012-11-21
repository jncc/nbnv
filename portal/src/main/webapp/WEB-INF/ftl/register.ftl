<#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />

<@template.master title="NBN Gateway - Register">
    <@markdown>
#Register as a NBN User

Access to much of the data on the NBN Website is strictly controlled by the data
owner. To begin to gain access to this you must register by entering your 
details below. Find out how to gain access to data.
    </@markdown>

    <@form.form method="POST" id="login" commandName="user" action="/User/Register">
        <@form.errors path="*" cssClass="message error" element="div" />
        <label for="username">Username</label>      <@form.input path="username"/>
        <label for="password">Password</label>      <@form.password path="password"/>
        <label for="forename">First Name</label>    <@form.input path="forename"/>
        <label for="surname">Last Name</label>      <@form.input path="surname"/>
        <label for="email">Your E-mail</label>      <@form.input path="email"/>
        <label for="phone">Phone</label>            <@form.input path="phone"/>

        <input type="submit" value="Register to the NBN Gateway"/>
    </@form.form>
</@template.master>