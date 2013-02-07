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

set POWERSHELL_PUBLISH=$pass = ConvertTo-SecureString -AsPlainText '%PASSWORD%' -Force; ^
	$Cred = New-Object System.Management.Automation.PSCredential -ArgumentList '%USERNAME%',$pass; ^
	Invoke-Command ^
		-FilePath .\deploy.ps1 ^
		-ArgumentList '%BUILD_PLANS%'.split(' ') ^
		-ConnectionURI http://%SERVER%:5985/WSMAN ^
		-Authentication basic ^
		-Credential $Cred;

powershell -command "%POWERSHELL_PUBLISH%" < NUL