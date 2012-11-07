<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
<h1>Registration of Web Service Clients</h1>

<p>From version 3.5 onwards all users of the NBN Gateway Web Services are required to register their client to receive a registration key. This registration key is used in all requests to the webservices so that we can identify our users and usage patterns. Collecting this information will allow us to provide a better service to webservice clients.</p>

<h1>Registering a Web Service Client</h1>

<ol type="1">
	<li>Log in to the NBN Gateway, and click on 'My Account'.
	<li>From the left hand menu select "apply for access" from the "webservices" section.
	<li>On the form, fill in the following details:
		<ol type="a">
			<li>Client Name: This requires either the web domain of your website, or the name of the application which will be using the webservices.</li>
			<li>Client Type: This is one of three choices:</li>
				<ol type="i">
					<li>Public Access: Your client will only have access to publically accessible data. This is the default choice and recommended for most websites.</li>
					<li>Set User Access: Your client will have access to data for the user you specify when you register. This is the equivalent of your client logging into the website every time it does a request.</li>
					<li>Variable User Access: Your client will have access to data for a user identified when each web service request is made, or publically accessible data if no user is identified. To access data for an identified user, your client will need to accept a username and password. Please only request this type of access if required.</li>
				</ol>
		</ol>
	<li>Click on the register button.</li>
	<li>Please read the terms and conditions of use carefully, then accept them.</li>
	<li>Your registration key will be emailed to the address you used when you registered with the NBN Gateway.</li>
</ol>

<h1>Using Your Registration Key</h1>

<p>Each webservice request comes with an attribute called 'registrationKey'. Please fill this attribute with your client's registration key.</p>

<h2>Java</h2>
<p>Each request class has a setRegistrationKey(String key) function. Use this to set the registration key for the request:</p>

<nbn:prettyprint-code lang="java">
SpeciesListRequest speciesListRequest = new SpeciesListRequest();
speciesListRequest.setRegistrationKey(“00000000000000000000000000000000”); // please replace with your client’s key.
</nbn:prettyprint-code>

<h2>C#</h2>
<p>Each request class has a registrationKey property. Set this property to your client's registration key for each request:</p>

<nbn:prettyprint-code lang="C#">
SpeciesListRequest speciesListRequest = new SpeciesListRequest();
speciesListRequest.registrationKey = "00000000000000000000000000000000";
</nbn:prettyprint-code>

<h2>PHP</h2>
<p>In the request object or hash set an element with name registrationKey and value of the client's registration key.</p>

<nbn:prettyprint-code lang="php">
$request = array();
$request['registrationKey'] = "00000000000000000000000000000000"; // please replace with your client's key.
</nbn:prettyprint-code>

<h1>Using the Variable User Access functionality</h1>
<p>For webservice clients which have been registered with the 'Variable User Access' type there are attributes called 'username' and 'userPassKey'. These attributes are used to provide the Gateway Web Services with credentials to use when processing the request.</p>
<p>Set the username to the user's username, and the userPassKey to a MD5 hash of the user's password.</p>
<p>If your webservice client has been registered as 'Public Access' or 'Set User Access' then these attributes will be ignored.</p>

<h2>Java</h2>
<nbn:prettyprint-code lang="java">
MessageDigest m = MessageDigest.getInstance("MD5");
m.reset();
m.update(password.getBytes());
BigInteger bigInt = new BigInteger(1, m.digest());
String hashtext = bigInt.toString(16);
while(hashtext.length() < 32 ){
    hashtext = "0"+hashtext;
}
SpeciesListRequest speciesListRequest = new SpeciesListRequest();
speciesListRequest.setRegistrationKey(“00000000000000000000000000000000”);
speciesListRequest.setUsername(username);
speciesListRequest.setUserPassKey(hashtext);
</nbn:prettyprint-code>

<h2>C#</h2>
<nbn:prettyprint-code lang="C#">
MD5 md5 = System.Security.Cryptography.MD5.Create();
byte[] inputBytes = System.Text.Encoding.ASCII.GetBytes(password);
byte[] hash = md5.ComputeHash(inputBytes);
StringBuilder sb = new StringBuilder();
for (int i = 0; i < hash.Length; i++)
{
  sb.Append(hash[i].ToString("x2"));
}
String hashtext = sb.ToString();
while (hashtext.Length < 32 ) {
  hashtext = "0" + hashtext;
}

SpeciesListRequest speciesListRequest = new SpeciesListRequest();
speciesListRequest.registrationKey = "00000000000000000000000000000000";
speciesListRequest.username = username;
speciesListRequest.userPassKey = hashtext;
</nbn:prettyprint-code>

<h2>PHP</h2>
<nbn:prettyprint-code lang="php">
$request = array();
$request['registrationKey'] = "00000000000000000000000000000000"; // please replace with your client's key.
$request['username'] = $username;
$request['userPassKey'] = md5($password);
</nbn:prettyprint-code>
    </jsp:attribute>
</t:webserviceDocumentationPage>
