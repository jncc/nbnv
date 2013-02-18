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