###############################################################################
# The following hash defines which tomcat connects to which network connection.
# The Connection names are defined in:
#		Control Panel\Network and Internet\Network Connections
###############################################################################
$tomcatAdapters = @{
	"api"="api.nbn.org.uk Network Connection";
	"data"="data.nbn.org.uk Network Connection";
	"gis"="gis.nbn.org.uk Network Connection";
}

# Get the named adapters
$namedAdapters = @{}
$netConfig = Get-WmiObject win32_networkadapterconfiguration
Get-WmiObject win32_networkadapter | Where-Object {$_.NetConnectionID -ne $null} | % {
	$namedAdapters += @{$_.NetConnectionID=$netConfig[$_.Index]} 
}

Function Set-Address { 
	param($adapter); 
	$input -replace 'address="[^"]*"',('address="{0}"' -f $namedAdapters[$adapter].IPAddress )
}

$tomcatAdapters.getEnumerator() | % { 					
	$path = "D:\Tomcat7\{0}\conf\server.xml" -f $_.key	#Update the server.xml
	cat $path | Set-Address -adapter $_.value | Set-Content $path
}