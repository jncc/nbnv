<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:webserviceDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#Documentation under review

Please note that the documentation is still being reviewed at this time and as such some documentation may be out of date or missing, we apologise for this and are working to update the documentation right now!
            
# Logging into and using the REST services

Logging into the REST services allows you to access any records that your user has access to on the NBN Gateway and allows access to the download functionality of the API. 

To log into the API you first need to register as a user on the gateway (see [Getting started by creating your own user account](/Documentation/NBN_Gateway_Documentation/User_How_Tos/index#create_user_account).

Once you have your user credentials you can log into the API using the [user resource](../User_Resource/index) and its login function, for example if you are using JAVA to log into the API the following code snippet should log onto the API

<nbn:prettyprint-code lang="java"> 
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
 
public class JerseyClientPost {
 
    private Client client;
    private Cookie authCookie;

    public static void main(String[] args) {
        client = Client.create();

        System.out.println("Attempting to log into API....");

        WebResource webResource = client.resource("https://data.nbn.org.uk/api/user/login");
        MultivaluedMap&lt;String, String&gt; formData = new MultivaluedMapImpl();
        formData.add("username", props.getProperty("username"));
        formData.add("password", props.getProperty("password"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);

        if (response.getStatus() != 200) {
            throw new RuntimeException("FAILURE : API login with code : " + response.getStatus() + "\n     " + response.getEntity(String.class));
        }

        List&lt;NewCookie&gt; cookies = response.getCookies();
        for (NewCookie c : cookies) {
            if (c.getName().equals(props.get("sso_token_key"))) {
                authCookie = c;
            }
        }
        System.out.println("Logged into API....");
    }
}
</nbn:prettyprint-code>

Once you have successfully logged into the API, you will receive an authentication cookie, you **MUST** include this with any further communications in this session.

For example you can include this cookie using the following Java snippet

<nbn:prettyprint-code lang="java">
WebResource webResource = client.resource("https://data.nbn.org.uk/api/taxonObservations/datasets/observations?gridRef=\"TA27\"");
WebResource.Builder builder = webResource.getRequestBuilder();
// Add authenticated cookie
builder = builder.cookie(authCookie);       

System.out.println("API Sending request....");

ClientResponse response = builder.accept("application/json")
        .get(ClientResponse.class);

if (response.getStatus() != 200) {
    throw new RuntimeException("FAILURE : API call failed with response : " + response.getStatus() + "\n     " + response.getEntity(String.class));
}

System.out.println("API call succeeded");
</nbn:prettyprint-code>
    
Once you have finished we recommend that you log out of the service this can be achieved as follows;

<nbn:prettyprint-code lang="java">
// Log out
webResource = client.resource("https://data.nbn.org.uk/user/logout");
webResource.cookie(authCookie);
System.out.println("Logging out....");
response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

if (response.getStatus() != 200) {
    throw new RuntimeException("WARNING: Could not log out of API service");
} else {
    System.out.println("Successfully logged out and completed monthly batch email");
}
</nbn:prettyprint-code>

        </nbn:markdown>
    </jsp:attribute>
</t:webserviceDocumentationPage>
