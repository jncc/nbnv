<#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />
<#assign resultMsg="${.data_model['successMessage']}" >

<@template.master title="NBN Gateway - User Modify"
    javascripts=["/js/jquery-ui-1.8.23.custom.min.js/","/js/passfield.min.js","/js/jquery.validate.min.js","/js/userAdmin/enable-user-admin-tabs.js"]
    csss=["/css/smoothness/jquery-ui-1.8.23.custom.css", "/css/passField/passfield.min.css", "/css/user.css"]>

    <h1>Modify User Details</h1>
    <#if (resultMsg?length > 0)>
        <div id="nbn-success-response" class="success">
            <p>${resultMsg}</p>
        </div>
    </#if>
    <div id="nbn-tabs">
        <ul>
            <li><a href="#tabs-1">User Details</a></li>
            <li><a href="#tabs-2">Change Password</a></li>
        </ul>
        <div id="tabs-1">
            <@form.form method="POST" id="modify-details-form" commandName="user" action="/User/Modify">    
                <@form.errors path="*" cssClass="message error" element="div" />
                <h3>Modify User Details</h3>
                <table class="nbn-simple-table">
                    <tr>
                        <td class="firstCol"><label for="forename">First Name</label></td>
                        <td><@form.input path="forename"/></td>
                    </tr>
                    <tr>
                        <td class="firstCol"><label for="surname">Last Name</label> </td>
                        <td><@form.input path="surname"/></td>
                    </tr>
                    <tr>
                        <td class="firstCol"><label for="phone">Phone</label></td>
                        <td><@form.input path="phone"/></td>
                    </tr>   
                </table>

                <@form.hidden path="username" />
                <@form.hidden path="email" />
                <@form.hidden path="password" id="default_password"/>
                <input type="submit" value="Change User Details" />
            </@form.form>

            <h3>Change Email Address</h3>
            <@form.form method="POST" id="modify-email-form" commandName="user" action="/User/Modify">
                <table class="nbn-simple-table">
                    <tr>
                        <td class="firstCol"><label for="email">E-mail</label></td>
                        <td><@form.input path="email"/></td>
                    </tr>
                </table>
                <input type="submit" name="changeEmail" value="Change Email Address" />
            </@form.form>
        </div>

        <div id="tabs-2">
            <@form.form method="POST" id="change-password-form" commandName="changePassword" action="/User/Modify#tabs-2">
                <@form.errors path="*" cssClass="message error" element="div" />
                <table class="nbn-simple-table">
                    <tr>
                        <td class="firstCol"><label for="password">Enter new password</label></td>
                        <td><@form.password path="password"/></td>
                    </tr>
                    <tr>
                        <td class="firstCol"><label for="password">Confirm new password</label></td>
                        <td><input type="password" name="password_confirm" id="password_confirm"/></td>
                    </tr>
                </table>
                <@form.hidden path="username"/>
                <@form.hidden path="token"/>
                <input type="submit" name="changePassword" value="Change Password" />
            </@form.form>
        </div>
    </div>

</@template.master>