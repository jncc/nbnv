<H1>GetSpeciesList Example</H1>

<?php
/*
 *    NBN Gateway SOAP Client Sample: 
 *    Author: James Perrins, 
 *    exeGesIS SDM
 *    22nd February 2006
 */
 
require_once('lib/nusoap.php');

//pass in details of any proxy server
$proxyhost = isset($_POST['proxyhost']) ? $_POST['proxyhost'] : '';
$proxyport = isset($_POST['proxyport']) ? $_POST['proxyport'] : '';
$proxyusername = isset($_POST['proxyusername']) ? $_POST['proxyusername'] : '';
$proxypassword = isset($_POST['proxypassword']) ? $_POST['proxypassword'] : '';

$client = new soapclient("http://212.219.37.104/NBNWebServices/ws/WSDL", 'true', $proxyhost, $proxyport, $proxyusername, $proxypassword);
// Set timeouts, nusoap default is 30 
$client->timeout = 500; 
$client->response_timeout = 500; 
$err = $client->getError();
if ($err) {
	echo '<h2>Constructor error</h2><pre>' . $err . '</pre>';
}


//define request - buffer search
$query1 = '<SpeciesListRequest xmlns="http://webservices.searchnbn.net/query">
		<GeographicalFilter>
			<Buffer xmlns="http://webservices.searchnbn.net/data">
			<Point srs="osgb_BNG" x="296652" y="810964" />
			<Distance>100</Distance></Buffer>
		</GeographicalFilter>
		<TaxonReportingCategoryKey>NHMSYS0000080085</TaxonReportingCategoryKey>
	</SpeciesListRequest>';


//IMPORTANT - currently Dataset filter seems to fail on the gateway - they are looking at it	
// alternative search using polygon	
//$polygon = '<v x="495180" y="297473"/>
//            <v x="495263" y="297630"/>
//            <v x="495339" y="297466"/>
//            <v x="495180" y="297473"/>';	
//$query1 = '<SpeciesListRequest xmlns="http://webservices.searchnbn.net/query">
//	<GeographicalFilter>
//		<OverlayRule>overlaps</OverlayRule>
//		<MinimumResolution>_1km</MinimumResolution>
//		<nbn:MultiPartPolygon srs="EPSG_27700">
//			<nbn:Polygon>
//				<nbn:Boundary>
//					<nbn:Ring>
//						' . $polygon . '
//					</nbn:Ring>
//				</nbn:Boundary>
//			</nbn:Polygon>
//		</nbn:MultiPartPolygon>
//	</GeographicalFilter>
//	<TaxonGroupKey>NHMSYS0000080085</TaxonGroupKey>
//	<DatasetList>
//		<DatasetKey>GA000074</DatasetKey>
//	</DatasetList>
//</SpeciesListRequest>';


$result = $client->call("GetSpeciesList", $query1);
$debug = $client->getDebug();


print '<h2>Results</h2>';

//the xml returned from the gateway appears to be missing the xml header which upsets PHP - so fix it first
$obj = simplexml_load_string(fixupxml($client->response));
//$obj = simplexml_load_string($client->response);

print ("<hr><table>");

foreach ($obj->SpeciesList->Species as $Species) {
   echo '<tr><td><i>', $Species->ScientificName, '</i></td><td>', $Species->CommonName, '</td></tr>';
}
print ("</table><hr>");

foreach ($obj->DatasetSummaryList->DatasetSummary->ProviderMetadata as $DataSet) {
   echo '<b>', $DataSet->DatasetTitle,'</b><br>',$DataSet->DatasetProvider, '<br />';
}
print ("<hr>");

//uncomment this to see the xml to see what data is returned
print '<h2>Fixed up XML</h2>';
echo '<xmp>'.fixupxml($client->response).'</xmp>';



print '<h2>Request</h2>';
echo '<xmp>'.$client->request.'</xmp>';
print ("<hr>");
print '<h2>Response</h2>';
echo '<xmp>'.$client->response.'</xmp>';
echo '<hr><h2>Debug</h2><pre>' . $debug . '</pre><hr>';


//this function fixes up the xml from the gateway so that PHP can turn it into an object
  function fixupxml($xml) {
	$pos = strpos($xml, '<SOAP-ENV:Body');   // Find the start of the <SOAP-ENV:Body tag
    $xml = substr($xml,$pos);                // Discard everything before it
    $pos = strpos($xml, '>');                // Find the close of the <SOAP-ENV:Body tag
    $xml = substr($xml,$pos+1);              // Discard the remainder of the SOAP-ENV:Body tag
    $pos = strpos($xml, '</SOAP-ENV:Body>'); // Find the start of the </SOAP-ENV:Body> tag
    $xml = substr($xml,0,$pos);              // Discard the </SOAP-ENV:Body> tag and everything after it
    $xml = '<?xml version="1.0" encoding="utf-8"?>'.$xml; // Add the missing xml header
	return $xml;
  }

?>