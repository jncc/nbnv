<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
# Getting started by creating your own user account

1. Click on ["Login: Need an account? Sign up free"](/User/Register) from home page.
2. Enter a username, password and account details to register account.
3. Read [Terms and Conditions](/Terms).
4. You will be sent a verification e-mail. Follow the link to setup your account.
5. Login to access your [account page](/User). Click on ['Can't access your account?'](/User/Recovery) if you want your username or password to be emailed to you.
6. You can change your user details by clicking on ['Change Your User Details'](/User/Modify) in the 'Your Account' section on your account page.
        </nbn:markdown>
        </jsp:attribute>
</t:gatewayDocumentationPage>