###############################################################################
# The following script will orchestrate the deployment of builds from one 
# server to another.
# 
# N.B It is expected that the username and password which you supply to this
# 	powershell script are valid for both the [$to] and [$from] server
# @author Christopher Johnson
###############################################################################
Param(
	[parameter(mandatory=$true, position=0, HelpMessage="Which server do you want to mirror?")][string]$from, 
	[parameter(mandatory=$true, position=1, HelpMessage="Which server do you want to deploy to?")][string]$to,
	[parameter(mandatory=$true, position=2)][string]$Username,
	[parameter(mandatory=$true, position=3)][Security.SecureString]$Password
);

#Parse the input credentials
$cred = New-Object System.Management.Automation.PSCredential -ArgumentList $Username,$Password;

Write-Host "Connecting to $from to get currently deployed builds" -ForegroundColor Magenta
#Obtain the deployed versions on $from
$toDeploy = Invoke-Command -ConnectionURI ("http://{0}:5985/WSMAN" -f $from) `
                           -Credential $cred `
                           -ScriptBlock { Import-Clixml "d:\deployed.xml" }

Write-Host "Success. Instructing $to to obtain these builds and deploy" -ForegroundColor Magenta
#Push these build versions to $to                  
Invoke-Command -ConnectionURI ("http://{0}:5985/WSMAN" -f $to) `
                -Credential $cred `
                -FilePath .\deploy.ps1 `
                -ArgumentList @($toDeploy)
				
Write-Host "Mirror deploy complete $from and $to are running equivalent builds" -ForegroundColor Magenta