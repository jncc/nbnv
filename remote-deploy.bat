@ECHO OFF

REM The following batch file will call Powershell and execute
REM the deployment code on the remote server
REM
REM usage : [username] [password] [server.to.deployto.com] [BUILDPLAN:BUILD-NUMBER] ... [ANOTHERBUILDPLAN:BUILD-NUMBER]

for /f "tokens=1-3*" %%a in ("%*") do (
    set USERNAME=%%a
    set PASSWORD=%%b
    set SERVER=%%c
    set BUILD_PLANS=%%d
)
 
set POWERSHELL_PUBLISH=$deploymentParams = @{}; ^
	$pass = ConvertTo-SecureString -AsPlainText '%PASSWORD%' -Force; ^
	$Cred = New-Object System.Management.Automation.PSCredential -ArgumentList '%USERNAME%',$pass; ^
	'%BUILD_PLANS%'.split(' ') ^| %% { $parts = $_.split(':'); $deploymentParams += @{$parts[0]=$parts[1]}}; ^
	Invoke-Command ^
		-FilePath .\config\scripts\server\deploy.ps1 ^
		-ArgumentList @($deploymentParams) ^
		-ConnectionURI http://%SERVER%:5985/WSMAN ^
		-Credential $Cred;

powershell.bat "%POWERSHELL_PUBLISH%"