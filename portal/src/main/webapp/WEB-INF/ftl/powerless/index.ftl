<#assign statistics = json.readURL("${api}/statistics")/>

<@template.master title="National Biodiversity Network Gateway"
        csss=["/css/homepage.css"]>
    <h1>Welcome to the NBN Gateway</h1>


    <div id="slidorion">
	<div id="slider">
            <div class="slide welcome">
                <img src="/img/slides/welcome.jpg" width="700px" height=400px" />
            </div>
            
            <div class="slide imt">
                <a href="/imt"><img src="/img/slides/imt.jpg"></a>
            </div>
            
            <div class="slide location-search">
                <div class="site">
                    <h1>Search by a Site</h1>
                    <img src="/img/slides/site.png">
                    <p class="copyright">&copy; Crown copyright and database rights 2011 Ordnance Survey [100017955]</p>
                </div>
                <div class="grid-square">
                    <h1>Search by Grid Square</h1>
                    <@image_map.hundredKM/>
                </div>
            </div>
            
            <div class="slide grid-map">
                <a href="/Taxa/NBNSYS0000002854" title="Danish scurvygrass"><img src="/img/slides/scurvygrass.png"></a>
                <a href="/Taxa/NBNSYS0000003278" title="Hippocrepis comosa"><img src="/img/slides/comosa.png"></a>
                <a href="/Taxa/NHMSYS0000712592" title="Harlequin ladybird"><img src="/img/slides/ladybird.png"></a>
            </div>
	</div>

	<div id="accordion">
            <div class="link-header">Welcome</div>
            <div class="link-content">
<p>This is the first release of the new version of the NBN Gateway.  Whilst it is available for you to try out, it is not a fully functioning site and we are currently working to resolve some technical issues. </p>
<p>If you want to access or use data please use the <a href="http://data.nbn.org.uk/">current NBN Gateway</a></p>
<p><h3>Your views</h3></p>
<p>As part of the technical work is to address <a href="http://data.nbn.org.uk/Documentation/NBN_Gateway_Documentation/NBNGatewayVIssues.pdf">known issues</a> and implement planned functionality and enhancements, we would be very interested in your views on how the site could be improved. Please send comments and suggestions to <a href="mailto:access@nbn.org.uk">access@nbn.org.uk</a>.</p>

            </div>

            <div class="link-header">Interactive Map Tool</div>
            <div class="link-content">
                <@markdown>
Explore distributions of species records, whole datasets, protected sites and habitats using the [Interactive Map Tool](/imt).
                </@markdown>
            </div>

            <div class="link-header">Search by Location</div>
            <div class="link-content">
                <@markdown>
Explore species lists and records for a particular [site or grid square](/Site_Datasets).
                </@markdown>
            </div>

            <div class="link-header">Grid Map</div>
            <div class="link-content">
                <@markdown>
Explore and map grid squares for a particular [species](/Taxa).
                </@markdown>
            </div>
        </div>
    </div>

    <div id="statistics">
        <h1>Gateway Statistics</h1>
        <table>
            <#list statistics?keys as stat>
                <tr>
                    <td>${stat}</td>
                    <td>${statistics[stat]}</td>
                </tr>
            </#list>
        </table>
    </div>

    <div id="news-container">
        <h1><a title="RSS" href="${api}/datasets/latest.rss">RSS</a><a href="/Datasets">Latest Datasets</a></h1>
        <ul class="news-ticker">
            <#list json.readURL("${api}/datasets/latest") as dataset>
                <#assign organisation = json.readURL(dataset.organisationHref)>

                <li>
                    <h3>
                        <#if organisation.smallLogo??>
                            <img src="${organisation.smallLogo}"> 
                        </#if>
                        ${organisation.name} Dataset Updated: ${dataset.formattedDateUploaded}</h3>
                    <a href="${dataset.href}">${dataset.title}</a> 
                </li>

            </#list>
        </ul>
    </div>
</@template.master>