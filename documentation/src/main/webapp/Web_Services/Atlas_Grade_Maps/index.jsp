<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@taglib prefix="nbn" uri="/WEB-INF/tlds/nbn-tags.tld" %>

<t:webserviceDocumentationPage>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="css/atlas-documentation.css" type="text/css" />
        <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/start/jquery-ui.css" type="text/css" />
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/jquery-ui.min.js"></script>
        <script type="text/javascript" src="js/atlas-documentation-min.js"></script>
    </jsp:attribute>
    <jsp:attribute name="body">
        <div class="nbn-atlas-documentation-section">
            <h1>Overview</h1>
            <p>The atlas grade mapping services provided by the NBN Gateway enable you to generate 10km Taxon occurrence maps which are of the quality suitable for publishing.</p>
            <p>The atlas grade mapping consists of 3 services:</p>
            <ul>
                <li><em>Map</em> which produces an atlas grade map image.</li>
                <li><em>Acknowledgment</em> which produces a acknowledgment list of datasets used and their dataset provider</li>
                <li><em>Legend</em> which produces a legend graphic which corresponds to a <em>Map</em> image request</li>
            </ul>
            <p>The URL to an atlas grade mapping service is in the form:</p>
            <nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/SingleSpecies/&lt;TAXONVERSIONKEY&gt;/&lt;service&gt;</nbn:prettyprint-code>
            <p>"&lt;TAXONVERSIONKEY&gt;" is the Taxon Version Key for a Taxon which is to be mapped and "&lt;service&gt;" is the requested service. Taxon Version Keys for a required Taxon can be obtained from the NBN Web services, specifically the <a href="../Web_Services-SOAP/Actual_Services/Taxonomy_and_Species_Search/">Taxonomy & Species Search</a> web service.</p>
            <p>Each of these services allows for control in the form of HTTP parameter arguments. The parameters which are acceptable differ depending on which service is being addressed. The details of valid parameters for each service are documented below.</p>
            <p>The valid values for "&lt;service&gt;" are map, legend and acknowledgment.</p>
        </div>

        <div class="nbn-atlas-documentation-section">
            <h1>Parameters</h1>
            <p>Below is a list of the parameters which are valid for each given atlas mapping service. Not all of the parameters are compatible with all of the Atlas Mapping services. Therefore, below in the description of each parameter, it is stated what services the parameter is applicable to.</p>
            <ul class="section-parameters-list">
                <li class="parameter-section">
                    <h2>background</h2>
                    <div class="nbn-atlas-applicable">map</div>
                    <p>Valid values are:</p>
                    <ul>
                        <li><em>gb</em></li>
                        <li><em>gbi</em></li>
                        <li><em>gb100kgrid</em></li>
                        <li><em>gbi100kgrid</em></li>
                        <li><em>vicecounty</em></li>
                    </ul>
                </li>
                <li class="parameter-section">
                    <h2>datasets</h2>
                    <div class="nbn-atlas-applicable">map and acknowledgement</div>
                    <p>The datasets parameter can take a comma separated value of dataset keys which limit the data used to create the map or acknowledgement list to the corresponding specified datasets. A datasets dataset key can be obtained from the NBN Gateway metadata page for the specified dataset. For example the dataset key for <a href="http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?refID=5&list=1&sort=0&dsKey=GA000466">Demonstration dataset for record access on the NBN Gateway</a> is GA000466.</p>
                    <p>The following example is a map service request for "Canadian Goose" limited to the "Biodiversity in Glasgow (BiG) Project" and "Suffolk Biological Records Centre (SBRC) dataset" provided by "British Trust for Ornithology" and "Suffolk Biological Records Centre" respectively.</p>
                    <nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/SingleSpecies/NBNSYS0000000009/map?datasets=GA000623,GA000652</nbn:prettyprint-code>
                </li>
                <li class="parameter-section">
                    <h2>startyear & end year</h2>
                    <div class="nbn-atlas-applicable">map</div>
                    <ul>
                        <li><em>startyear</em> - defaults to 1800</li>
                        <li><em>endyear</em> - defaults to current year</li>
                    </ul>
                    <p>The start year and end year parameters allow for temporal filtering of records. In order to guarantee a valid response the value for both of these parameters must be an integer between 1800 to the current year, where the startyear is less than or equal to the endyear.</p>
                    <p>For more information in this area, please view the Date Filtering document which can be found <a href="http://www.nbn.org.uk/Guidebooks/Web-services-documentation/Resources/download-documents.aspx">here</a></p>
                </li>
                <li class="parameter-section">
                    <h2>username & userkey</h2>
                    <div class="nbn-atlas-applicable">map and acknowledgement</div>
                    <p>In order to be able to utilise records which are available to your specific level of user access on the NBN Gateway, you would need to login. Since the atlas grade mapping is a restful service there is no logging in mechanism in the traditional sense. Instead, to obtain records which are available to your specific user access level, then you must provide a username and an MD5 hash of your password as parameters in the URL.</p>
                    <p>We have provided a tool to generate the MD5 hash of your password. This can be found in the <a href="#interactive-md5Generator">interactive tools</a> section of this page.</p>
                    <p>Since MD5 is a common and well understood hashing algorithm, many implementations of it exist. This means that you can generate userkey hashes of your own password in your own environment. Below represents the Java code which will generate a userkey for a given password.</p>
                    <nbn:prettyprint-code lang="java">
private static String getHashText(String password) throws NoSuchAlgorithmException{
    MessageDigest m = MessageDigest.getInstance("MD5");
    m.reset();
    m.update(password.getBytes());
    BigInteger bigInt = new BigInteger(1, m.digest());
    String hashtext = bigInt.toString(16);
    while(hashtext.length() < 32 ){
        hashtext = "0"+hashtext;
    }
    return hashtext;
}
                    </nbn:prettyprint-code>
                </li>
                <li class="parameter-section">
                    <h2 id="param-colour">fillcolour & outlinecolour</h2>
                    <div class="nbn-atlas-applicable">map and legend</div>
                    <p>The fillcolour and outlinecolour parameters alter the fill and outline colour of a symbol used in the atlas map service.</p>
                    <p>Valid values for these parameters are 6 digit RGB hex codes (000000 - FFFFFF). Below is an example call of these being used.</p>
                    <nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/map?fillcolour=00ffff&outlinecolour=e2e2e2</nbn:prettyprint-code>
                    <p>The default value for both parameters is '000000' the hex value for black. You may use the interactive <a href="#interactive-colourGenerator">colour picker</a> to generate a colour.</p>
                </li>
                <li class="parameter-section">
                    <h2>imagesize</h2>
                    <div class="nbn-atlas-applicable">map</div>
                    <p>The imagesize parameter allows for different sized images to be generated by the atlas map service. Since the atlas grade map service guarantees that the resultant map will have no distortion for symbols on the British National Grid, we cannot allow arbitrary height and width resolutions. Therefore we have defined a range of 'imagesizes' which map to fixed resolutions.</p>
                    <p>A valid value for the image size parameter is an integer from 1-15 inclusive. Each of these images size produces a fixed resolution image. A table of image size to resolution is provided below. Please note that the images in the table below are a third are their actual size.</p>
                    <table class="attributeTable nbn-atlas-imagesizeTbl">
                        <tr class="head"><th>Imagesize Value</th><th>Height</th><th>Width</th><th>Image</th></tr>
                        <tr>
                            <td>1</td>
                            <td>135</td>
                            <td>100</td>
                            <td rowspan="15" class="images">
                                <img class="imagesize-15" src="images/resize/15.png"/>
                                <img class="imagesize-12" src="images/resize/12.png"/>
                                <img class="imagesize-9" src="images/resize/9.png"/>
                                <img class="imagesize-6" src="images/resize/6.png"/>
                                <img class="imagesize-3" src="images/resize/3.png"/>
                            </td>
                        </tr>
                        <tr><td>2</td><td>270</td><td>200</td></tr>
                        <tr class="imagesize-3"><td>3</td><td>405</td><td>300</td></tr>
                        <tr><td>4</td><td>540</td><td>400</td></tr>
                        <tr><td>5</td><td>675</td><td>500</td></tr>
                        <tr class="imagesize-6"><td>6</td><td>810</td><td>600</td></tr>
                        <tr><td>7</td><td>945</td><td>700</td></tr>
                        <tr><td>8</td><td>1080</td><td>800</td></tr>
                        <tr class="imagesize-9"><td>9</td><td>1215</td><td>900</td></tr>
                        <tr><td>10</td><td>1350</td><td>1000</td></tr>
                        <tr><td>11</td><td>1485</td><td>1100</td></tr>
                        <tr class="imagesize-12"><td>12</td><td>1620</td><td>1200</td></tr>
                        <tr><td>13</td><td>1755</td><td>1300</td></tr>
                        <tr><td>14</td><td>1890</td><td>1400</td></tr>
                        <tr class="imagesize-15"><td>15</td><td>2025</td><td>1500</td></tr>
                    </table>
                    <p>The default imagesize value is 10</p>
                </li>
                <li class="parameter-section">
                    <h2>format</h2>
                    <div class="nbn-atlas-applicable">map</div>
                    <p>The format parameter specifies the type of image which will be generated from the Atlas Map service, currently there are only three valid outputs. These are:</p>
                    <ul>
                        <li><em>png</em> - Portable network graphics image</li>
                        <li><em>gif</em> - Graphics Interchange format</li>
                        <li><em>jpeg</em> - JPEG image</li>
                    </ul>
                    <p class="warning">Please note that JPEG is a lossy compression format, and as such may produce artefacts which are unacceptable for publication. To avoid this, it is recommended that the requested format is 'png'</p>
                    <p>The following is a request for a map image of Hippocrepis in the gif format</p>
                    <nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/map?format=gif</nbn:prettyprint-code>
                </li>
                <li class="parameter-section">
                    <h2>css</h2>
                    <div class="nbn-atlas-applicable">acknowledgement</div>
                    <p>The css parameter which is offered by the acknowledgment atlas map service enables custom styling to be applied to the resultant html of the acknowledgment service. The value of the parametermust point to the URL of the Cascading Style Sheet (CSS) which is to be used for styling the resultant html. In order to create a CSS file, knowledge of web styling is necessary.</p>
                    <p>The following is a snippet of HTML which shows the structure and attached style classes of which should be expected from the acknowledgement service.</p>
                    <nbn:prettyprint-code lang="xml">
<body>
    <table class="nbn-acknowledgment-table">
        <tr class="nbn-acknowledgment-headerRow">
            <th class="nbn-acknowledgment-headerDatasetProvider">
                Dataset Provider
            </th>
            <th class="nbn-acknowledgment-headerDataset">
                Dataset
            </th>
        </tr>
        <tr class="nbn-acknowledgment-entryRow">
            <td class="nbn-acknowledgment-datasetProvider" rowspan="2">
                <a href="http://data.nbn.org.uk/organisation/organisation.jsp?orgKey=3">
                    Botanical Society of the British Isles
                </a>
            </td>
            <td class="nbn-acknowledgment-datasetTitle">
                <a href="http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=GA000091">
                    Vascular Plants Database
                </a>
            </td>
        </tr>
        <tr class="nbn-acknowledgment-entryRow">
            <td class="nbn-acknowledgment-datasetTitle">
                <a href="http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=GA000477">
                    Vascular Plants Database additions since 2000
                </a>
            </td>
        </tr>
    </table>
</body>
                    </nbn:prettyprint-code>
                    <p>You can see the default view of the acknowledgment service when no css is provided in the following sample</p>
                    <iframe src="http://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/acknowledgement" height="300" width="500"></iframe>
                </li>
            </ul>
        </div>

        <div class="nbn-atlas-documentation-section">
            <h1>Interactive Tools</h1>
            <noscript>In order to be able to use the interactive tools, you must have javascript enabled</noscript>
            <div id="interactive-md5Generator">
                <h2>Login: userkey parameter</h2>
                <p>By entering your NBN Gateway login details into the tool below, you will be able to generate the values required for userkey and username parameters.</p>
            </div>
            <div id="interactive-colourGenerator">
                <h2>Colour: Hex Colour selector</h2>
                <p>You can use the colour picker and get the RGB hex value for it. This can then be used for either the <a href="#param-colour">fillcolour or outlinecolour</a> parameters</p>
            </div>
        </div>

        <div class="nbn-atlas-documentation-section">
            <h1>Examples</h1>
            <p>The following are some complete example URL calls to the Atlas map service, along with a description and what they return.</p>
            <div>
                <nbn:prettyprint-code lang="url">http://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/map?background=gb100kgrid&startyear=1990&endyear=2000&fillcolour=00ffff&outlinecolour=000000&imagesize=14&format=gif&datasets=GA000091,GA000482</nbn:prettyprint-code>
                <p class="description"></p>
                <img height="405" width="300" src="http://gis.nbn.org.uk/SingleSpecies/NHMSYS0000459714/map?background=gb100kgrid&startyear=1990&endyear=2000&fillcolour=00ffff&outlinecolour=000000&imagesize=9&format=gif&datasets=GA000091,GA000482SingleSpecies