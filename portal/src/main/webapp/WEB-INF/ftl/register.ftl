<#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />

<@template.master title="NBN Gateway - Register"     
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/passfield.min.js","/js/jquery.validate.min.js","/js/userAdmin/enable-user-register-validation.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/passField/passfield.min.css", "/css/user.css", "/css/registration.css"]>
    <@markdown>
#Register as a NBN User

Access to much of the data on the NBN Website is strictly controlled by the data
owner. To begin to gain access to this you must register by entering your 
details below. Find out how to gain access to data.
    </@markdown>
    <@form.form method="POST" id="register-form" commandName="user" action="/User/Register">
        <div id="errors" class="message error" style="display: none;">
            <p style="font-size: medium; margin-top:0px;">Please correct the following errors and resubmit the form;</p>
        </div>
        <@form.errors path="*" cssClass="message error" element="div" />
        <label for="username">Username</label>      <@form.input path="username"/> <div style="float:right;"></div>
        <label for="password">Password</label>      <@form.password path="password"/> <div style="float:right;"></div>
        <label for="confirmPassword">Confirm Password</label>       <input type="password" name="password_confirm" id="password_confirm"/> <div style="float:right;"></div>
        <label for="forename">First Name</label>    <@form.input path="forename" /> <div style="float:right;"></div>
        <label for="surname">Last Name</label>      <@form.input path="surname"/> <div style="float:right;"></div>
        <label for="email">Your E-mail</label>      <@form.input path="email"/> <div style="float:right;"></div>
        <label for="phone">Phone</label>            <@form.input path="phone"/> <div style="float:right;"></div>

        <input type="submit" value="Register to the NBN Gateway"/>
    </@form.form>
</@template.master>