<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>
<t:gatewayDocumentationPage>
	<jsp:attribute name="body">
        <nbn:markdown>
#How to use the Comments feature

As part of the National Biodiversity Network's ongoing effort to increase the quality of biodiversity data in the UK the NBN Gateway has opened up the record commenting facility to a wider audience.  By including a wider part of the biodiversity community we can help data collectors and providers correct faulty records and remove erroneous data.
In this document we wish to introduce how users can make use of the commenting facility, and how dataset administrators can work with comments and use them as part of improving data quality.

#Viewing Records and Comments		
The ability to view and comment on records **is available through the interactive map section** of the NBN Gateway. In this example we will view record comments for the species 'Blue Tit' (Cyanistes caeruleus) on the National Biodiversity Network Trust's 'Demonstration dataset for record commenting on the NBN Gateway'.

Note:  Records and comments can only be viewed on datasets where the user has the right to Download Raw Data.

1. Browse to the interactive map for Blue Tit data ([how to use the search engine](../How_to_use_the_Search_Engine)). Go to working example [here](http://data.nbn.org.uk/interactive/map.jsp?srchSp=NHMSYS0001688296)
2. Second deselect all datasets except our target dataset, the 'Demonstration dataset for record commenting on the NBN Gateway' and refresh the map.
3. Select all the Blue Tit records in the dataset and query them.
4. A page listing all the selected records appears. If there are any comments for any records they appear below the record in question (Figure 1).  
	Each comment has various properties. The first of these is what the commenter feels the record is, this has three possible states *CORRECT*, *DUBIOUS* and *INCORRECT*.

	Secondly is the resolution that the commenter could see when they made the comment, and if this was a blurred record. Remember the NBN Gateway allows dataset administrators to blur records, the resolution you see the record at may be more accurate than the resolution the commenter saw the record at. The text of the comment appears next.

	On the second line of each comment we have the name of the commenter, the date the comment was made followed by any action the dataset administrators have taken with this comment.

![Blue Tit records from Demonstration Dataset showing attached comments](images/comment-screenshot-1.gif)

5. If the comment has been accepted by the dataset administrators it will appear in bold text, if it has been rejected, it will appear greyed out.
6. For the convenience of users, individual records have been shaded based on comments made and accepted by dataset administrators.

Records with comments have been shaded a light grey. If a dataset administrator has accepted a comment where the commenter thinks the record is CORRECT, then the record is shaded green. If a dataset administrator has accepted a comment where the commenter thinks the record is DUBIOUS, then the record is shaded orange. If a dataset administrator has accepted a comment where the commenter thinks the record is INCORRECT, then the record is shaded red.

#Making Record Comments

On the same page where users can see records they can create comments for these records. In this example we will login and comment on records for the species 'Blue Tit' (Cyanistes caeruleus) on the National Biodiversity Network Trust's 'Demonstration dataset for record commenting on the NBN Gateway'.

Note:  Comments can *only be created on records from datasets where the user has the right to Download Raw Data*, and the dataset administrators have not restricted the ability the create comments on their datasets.  Only logged in users may create comments. *Comments cannot be left anonymously.*

![Record commenting pop up form](images/comment-screenshot-2.gif)

1. First login to the NBN Gateway using your username and password. If you do not yet have an account now is an excellent opportunity to register for one.
2.Then browse to the interactive map for Blue Tit data <http://data.nbn.org.uk/interactive/map.jsp?srchSp=NHMSYS0001688296>
3. Deselect all datasets except our target dataset, the 'Demonstration dataset for record commenting on the NBN Gateway' and refresh the map.
4. Select all the Blue Tit records in the dataset and query them.
5. A page listing all the selected records appears. If there are any comments for any records they appear below the record in question.
6. Tick any records you wish to comment on. In this example we shall just tick the fourth record (Monks Wood, TL203801, on 28th February 2008). Then click on the 'Comment On Selected Records' button.
7. A pop up window (or a new tab) will appear detailing the record we wish to comment on. It lists our name the record number at the best resolution we can see. If this is a blurred record it will indicate that fact. (Figure 2)
A drop down box allows us to select what status we this the record holds. The choices are CORRECT, DUBIOUS and INCORRECT. We can then leave our comment in the free text box below, and click 'save'.
8. Close the pop up page (or tab) and refresh the page containing the records. Our new comment should appear once the page has been refreshed.

#Dataset Administrator Controls

Dataset administrators have a number of controls that allow them to manage comments. These include accepting, rejecting, and deleting comments, disallowing all comments on a dataset, and banning users from commenting on a dataset 
![Administrator commenting page](images/comment-screenshot-3.gif)

##Performing Actions on Comments
Three actions are available for each comment 'Accept', 'Reject' and 'Delete'. These options are available on the comment management page.

1. Log in to the NBN Gateway and click 'My Account'
2. On the left hand menu select 'you manage' under 'species datasets'.
3. This gives you a selection of options for each dataset you manage. Select the manage link in the comments column for the dataset whose comments you wish to manage.
4. On the lower part of the page a table listing each comment appears. In the final column there are three buttons, 'Accept', 'Reject' and 'Delete'.

Accepting a comment means that you agree with the comment and will cause the record to become shaded by a colour depending on the type of comment made.

Rejecting a comment means that you disagree with the comment and will cause it to appear in light grey text on the records page.

Deleting a comment removes the comment entirely.

##Download record comments
List of comments may be downloaded as either as an Excel Spreadsheet, comma separated text file or Recorder External filer .REF file. To download the list of commnts

1. Log in to the NBN Gateway and click 'My Account'
2. On the left hand menu select 'you manage' under 'species datasets'.
3. This gives you a selection of options for each dataset you manage. Select the manage link in the comments column for the dataset whose comments you wish to manage.
4. Links are provided above the list of comments to download this list in one of the three formats.  Click on the preferred link

##Turn Off or On Commenting on a Dataset
Dataset administrators are able to disable commenting on the entire dataset. This means that no users will be able to comment on that dataset's records.

1. Log in to the NBN Gateway and click 'My Account'
2. On the left hand menu select 'you manage' under 'species datasets'.
3. This gives you a selection of options for each dataset you manage. Select the manage link in the comments column for the dataset you wish to alter.
4. Untick (or tick to re-enable) the box labelled 'Allow records comments'
5. Click on the 'Update Settings' button to save the changes

##Ban a User from Commenting on a Dataset
Dataset administrators are able to ban problematic users from commenting on a dataset. This functionality is exposed through the manage comments and manage users interface.
Note: Banning and unbanning a user occurs silently. No email is sent to the user informing them of the change.

1. Log in to the NBN Gateway and click 'My Account'
2. On the left hand menu select 'you manage' under 'species datasets'.
3. This gives you a selection of options for each dataset you manage. Select the manage link in the comments column for the dataset you wish to manage.
4. On the lower part of the page a table listing each comment appears. In the user column of the table is a link to each user's profile, and an additional link to ban the user from commenting on this dataset. Click the '(ban user)' link to ban a user.

##Or to ban a user who has yet to comment on a dataset:
1. Log in to the NBN Gateway and click 'My Account'
2. On the left hand menu select 'you manage' under 'species datasets'.
3. This gives you a selection of options for each dataset you manage. Select the manage link in the users column for the dataset you wish to manage.
4. In the 'find a user' box, enter the user's surname or email address. Click the 'find user' button.
5. Click on the 'ban' link next to the user you wish to ban from commenting.

##Un-ban a User from Commenting on a Dataset
Dataset administrators are able to ban problematic users from commenting on a dataset, and sometimes this is done by mistake and the ability needs restoring. This functionality is exposed through the manage users interface.

1. Log in to the NBN Gateway and click 'My Account'
2. On the left hand menu select 'you manage' under 'species datasets'.
3. This gives you a selection of options for each dataset you manage. Select the manage link in the users column for the dataset you wish to manage.
4. If there are any banned users a table appears at the bottom of the page listing each banned user.
5. Tick any users you wish to unban, then click on the 'unban user' button.
        </nbn:markdown>
    </jsp:attribute>
</t:gatewayDocumentationPage>
