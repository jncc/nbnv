# Server Specific Configuration

This directory hosts the various server specific configuration files
which can be run on a server to complete the configuration.

The following is a list of scripts and their function

* ipconfig-tomcat.ps1 - The NBN Web machine hosts multiple tomcats
	each running on their own IP Address. In windows, you can provide
	a name for a network adapter which is visible in the Control Panel.
	This script updates the various tomcat server.xml's to match the 
	currently assigned IP Address of a Network adapter with regards to
	the respective tomcat. Meaning that the portal tomcat is tied to
	the Portal Network Connection.
	
	N.b. This script has not been tested or intended to be used on 
	Multihomed network adapters

* mirror-deploy.ps1 - This script enables you to mirror the deployment
	of one server to another. In essence, this script orchestrates deployment
	by reading which builds are deployed on one server and then executing
	the deploy.ps1 script with those builds on a remote server
	
* deploy.ps1 - Executing this script on an NBN Web machine will obtain
	a the specified builds from the build server and then perform any
	service restarts required to get those builds running. A description
	of which builds have been deployed on that server will be persisted.