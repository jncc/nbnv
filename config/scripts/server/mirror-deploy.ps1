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
	[parameter(mandatory=$true, position=3)][Security.SecureString]$Password,
	[Parameter(mandatory=$false)][switch]$force
);
#Parse the input credentials
$cred = New-Object System.Management.Automation.PSCredential -ArgumentList $Username,$Password;

Write-Host "Connecting to $from to get currently deployed builds" -ForegroundColor Magenta
$toDeploy = Invoke-Command -ConnectionURI ("http://{0}:5985/WSMAN" -f $from) `
                           -Credential $cred `
                           -ScriptBlock { Import-Clixml "d:\deployed.xml" }

Write-Host "Obtained current builds from $from" -ForegroundColor Magenta
$toSession = New-PSSession -ConnectionURI ("http://{0}:5985/WSMAN" -f $to) -Credential $cred 
					   
if($force -ne $true) {
	Write-Host "Obtaining the current builds deployed on $to. Only the builds not currently deployed will be obtained" -ForegroundColor Magenta
	$deployedOnTo = Invoke-Command -Session $toSession -ScriptBlock { Import-Clixml "d:\deployed.xml" }
	@($toDeploy.GetEnumerator()) | Where-Object { $deployedOnTo[$_.key] -eq $_.value } | % { $toDeploy.Remove($_.key) }
}

if($toDeploy.Count -ne 0) { #Check if there are builds to deploy
	Write-Host "Builds have been decided. Instructing $to to obtain these builds and deploy" -ForegroundColor Magenta               
	Invoke-Command -Session $toSession -FilePath .\deploy.ps1 -ArgumentList @($toDeploy)
	Write-Host "Mirror deploy complete $from and $to are running equivalent builds" -ForegroundColor Magenta
}
else { Write-Host "There is nothing to Deploy. You may wish to -force a deploy" -ForegroundColor Yellow }
Remove-PSSession $toSession