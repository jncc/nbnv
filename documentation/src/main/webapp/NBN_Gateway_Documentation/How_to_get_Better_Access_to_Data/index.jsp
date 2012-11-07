<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#REQUESTING NEW OR BETTER ACCESS

The NBN Gateway is unique in the way it allows data owners who put their data on the system to control exactly who can do what with their data.  Some owners have chosen to make their data publicly available and these data will be visible as soon as a person accesses the website.  For data that have not been made generally available, or are only available at low levels of geographical resolution, a user will need to register and apply for new or better access.   **You must have a Gateway account and be logged on to request better access to data**

##HOW TO REQUEST NEW OR BETTER ACCESS
1. Log in to the Gateway.
2. Find and click the title of the dataset that you would like new or better access to.
3. Scroll down to the "Dataset Use" section of the metadata page.  This section gives a summary of your access.  If your access is restricted there should be an access constraint statement given by the dataset provider.
4. Click "Apply for access" or "Apply for better access" and follow the instructions and advice.

This generates an email request that is sent directly to the administrator for that dataset. Administrators are encouraged to respond to requests within 28 days.		

![HOW TO REQUEST NEW OR BETTER ACCESS](images/gateway5.png)


##Alternatively
1. Log in to the Gateway
2. Go to My Account (top right hand corner of every page)
3. Under species datasets click on "Request access" to gain access to datasets you have no access to at all.  From here you can apply for access to any of these.  You can see the metadat description for the datasets by clicking their title.
4. Or to apply for better access; under 'Species datasets - you have access to'
5. Click 'apply' under the 'better access' column. Using this method will also generate an email request to the dataset administrator
6. "Current access requests" will display the species datasets you have requested better access to that have not yet been dealt with by the dataset provider

![HOW TO REQUEST NEW OR BETTER ACCESS](images/gateway6.png)
        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
