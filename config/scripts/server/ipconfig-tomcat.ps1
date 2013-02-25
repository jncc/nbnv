###############################################################################
# The following powershell script will allow you to update the various tomats
# to respond on given hostnames addresses. Only the server.xmls will be updated
# no services will be restarted
###############################################################################
$tomcats = @("api","data","gis")

Function Set-Address { 
	param($domainname);
	$ipaddress = [System.Net.Dns]::GetHostAddresses($domainname)[0].IPAddressToString;
	$input  -replace 'address="[^"]*"',('address="{0}"' -f $ipaddress )
}

$tomcats | % {
	$path = "D:\Tomcat7\{0}\conf\server.xml" -f $_	#Update the server.xml
	$domainname = Read-Host "Enter the tomcat address for $_"
	cat $path | Set-Address -domainname $domainname | Set-Content $path
}