$ErrorActionPreference = "Stop"

$DEPLOYED_DESCRIPTOR = "D:\deployed.xml"

###############################################################################
# This function will obtain some build code from the build server
#
# @param $build, The bamboo build id of the plan to obtain an artifact from
# @param $build_number, The build number of the build id to get the artifact of
# @param $artifact, The path to the artifact to get
# @return The location of the downloaded file
###############################################################################
Function GetBuild ($build, [int] $build_number, $artifact) {	
	$source = "http://bamboo.ceh.ac.uk/browse/$build-$build_number/artifact/shared/$artifact"
	$destination = (Get-Location).Path + "\" + [system.guid]::newguid().tostring()
	
	#Download the zip from the build server
	$wc = New-Object System.Net.WebClient
	$wc.DownloadFile($source, $destination)
	return $destination
}

###############################################################################
# This function will obtain deploy a given war to a given tomcat with a certain
# context
#
# @param $war The War file to deploy
# @param $tomcat The tomcat to deploy to
# @param $context The context of the application
###############################################################################
Function Deploy($war, $tomcat, $context) {
	#Check if files exist then delete if they do
	if(Test-Path D:\Tomcat7\$tomcat\webapps\$context\) 	{ Remove-Item -r D:\Tomcat7\$tomcat\webapps\$context\ }
	if(Test-Path D:\Tomcat7\$tomcat\webapps\$context.war) { Remove-Item -r D:\Tomcat7\$tomcat\webapps\$context.war }
	#Put the new war in place
	Copy $war D:\Tomcat7\$tomcat\webapps\$context.war
}


###############################################################################
# This script defines the builds which are configured to be run by	the 
# local-deploy script.
#																	
# PLEASE NOTE: Notice the form of each of the definitions:			
#	services: This is an array of Services which the deployment is dependent on.
#		These will be stopped before [deploy] is executed and restarted
#		afterwards.							
#	obtain: This is a code block which will be called to create a local copy of 
#		code to be deployed from the build server. If any processing of the 
#		obtained code should be performed before deployment, then it should be 
#		done in this block		
#	deploy: This is a code block which should delete any existing deployments 
#		and then put in place the new code				
###############################################################################

$data = @{} #Create somewhere to store folders to delete and use between stages

$REGISTERED_BUILDS = @{ 
	"docs" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-DOCUMENTATION"] = GetBuild "NBNV-DOCUMENTATION" $build_number "NBN-Documentation-War/nbnv-documentation-1.0-SNAPSHOT.war" "war"
		};
		"deploy" = { Deploy $data["NBNV-DOCUMENTATION"] "data" "Documentation"}
	};
	"api" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-PORTAL-API"] = GetBuild "NBN-FRONTENDSNAP" $build_number "NBN-API-War/nbnv-api-0.1-SNAPSHOT.war"
		};
		"deploy" = { Deploy $data["NBNV-PORTAL-API"] "data" "api" }
	};
	"gis" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-PORTAL-GIS"] = GetBuild "NBN-FRONTENDSNAP" $build_number "NBN-GIS-War/gis.war"
		};
		"deploy" = { Deploy $data["NBNV-PORTAL-GIS"] "data" "gis" }
	};
	"imt" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-PORTAL-IMT"] = GetBuild "NBN-FRONTENDSNAP" $build_number "NBN-IMT-War/imt.war"
		};
		"deploy" = { Deploy $data["NBNV-PORTAL-IMT"] "data" "imt" }
	};
	"portal" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-PORTAL-FRONTEND"] = GetBuild "NBN-FRONTENDSNAP" $build_number "NBN-Portal-War/ROOT.war"
		};
		"deploy" = { Deploy $data["NBNV-PORTAL-FRONTEND"] "data" "ROOT" }
	};
	"solr" = @{
		"services" = @("Tomcat7-SOLR");
		"obtain" = { 
			param($build_number);
			$data["NBNV-SOLR"] = GetBuild "NBNV-SOLR" $build_number "NBN-Solr-War/nbnv-api-solr-0.1-SNAPSHOT.war"
		};
		"deploy" = { Deploy $data["NBNV-SOLR"] "solr" "solr" }
	};
	"importer.ui" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-IMPORTER-UI"] = GetBuild "NBNV-IMPORTER-FRONTEND" $build_number "Spatial-Importer-UI/importer.war"
		};
		"deploy" = { Deploy $data["NBNV-IMPORTER-UI"] "data" "importer" }
	};
	"importer.spatial.ui" = @{
		"services" = @("Tomcat7-data.nbn.org.uk");
		"obtain" = { 
			param($build_number);
			$data["NBNV-IMPORTER-SPATIAL-UI"] = GetBuild "NBNV-IMPORTER-FRONTEND" $build_number "Spatial-Importer-UI/spatial.war"
		};
		"deploy" = { Deploy $data["NBNV-IMPORTER-SPATIAL-UI"] "data" "spatial-importer" }
	}
 }

###############################################################################
# The following script will obtain code ready for publication. Stop	any 
# dependent services and then restart them once the code to bedeployed has been
# put in place. This script is capable of deploying many applications in one 
# big bulk, this will reduce downtime of the overall system.
#
# The script works in five phases:
# 	Obtain: Locate all the required files for deployment and create a local 
#		copy (Expand if nessersary)	
#	Pre-deploy: Stop all dependent services							
#	Deploy: Move the local copies in place							
#	Post-deploy: Start all the dependent services					
#	Clean-up: Remove any temporary files which were used during deployment													
#
# Usage: local-deploy "[BUILDID-1]:[XX]" ... "[BUILDID-N]:[XX]"		
# 	The values of [BUIILDID] correspond to the build plans defined in 
#	registered-builds.ps1. The values of [XX] correspond to the build number to
#	be pulled down from the build server.			
###############################################################################

#Create an array of builds to process
$build_plans = @()
$deploying = $args[0]
$deploying.Keys | % {
	if($REGISTERED_BUILDS.keys -notcontains $_) { #checking for invalid keys
		throw "The $build_request plan is not registered. Valid plans are " + $REGISTERED_BUILDS.keys 
	}
	$build_plans += @{"plan" = $REGISTERED_BUILDS[$_]; "number" = $deploying.Item($_)}
}
if($build_plans.length -eq 0) { throw "No build plans have been specified. USAGE: [BUILDID-1]:[XX] ... [BUILDID-N]:[XX]" }

#Workout the require services to stop and start
$build_plans | ForEach { $services += $_["plan"]["services"] }
$services = $services | Sort-Object | Get-Unique

#Obtain all the code from external sources before stopping anything
echo "Obtaining builds from the build server"
$build_plans | ForEach { &$_["plan"]["obtain"] $_["number"] }

#Stop Services which being used
echo "Stopping Services $services"
$services | ForEach {Stop-Service $_}

#Deploy everything
echo "Begining deployment"
$build_plans | ForEach { &$_["plan"]["deploy"] }

#Restart Services
echo "Starting Services $services"
$services | ForEach {Start-Service $_}

#delete all processed dirs
echo "Cleaning up old dirs " + $data.values
$data.values | ForEach { Remove-Item -r $_}

#Store deployed descriptor
echo "Recording the newly deployed artifacts"
$mutex = New-Object -TypeName System.Threading.Mutex -ArgumentList $false, "DEPLOYED_DESCRIPTOR_MUTEX";
$result = $mutex.WaitOne();
if(Test-Path $DEPLOYED_DESCRIPTOR) { $loaded = Import-Clixml $DEPLOYED_DESCRIPTOR } #Reading in what was deployed
($loaded.keys | ?{$deploying.keys -contains $_}) | ForEach { $loaded.remove($_) } #Remove old entries
($loaded += $deploying) | Export-Clixml $DEPLOYED_DESCRIPTOR #Merge hashes
$mutex.ReleaseMutex();
echo "As long as no one has been bypassing the deployment script, the following will be deployed"
$loaded.Keys | % { Write-Host -ForegroundColor GREEN $_ : $loaded.Item($_) }