<#assign statistics = json.readURL("${api}/statistics")/>
<#assign welcomeText = welcomeTextReader.read()/>

<@template.master title="National Biodiversity Network Gateway"
        csss=["/css/homepage.css"]>
    <h1>Welcome to the NBN Gateway</h1>

    <#if welcomeText?has_content>
        <div class="welcometext">
            ${welcomeText}
        </div>
    </#if>

    <div id="slidorion">
	<div id="slider">
            <div class="slide welcome">
                <img src="/img/slides/welcome_special.jpg" width="700px" height=400px" />
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
<p>This is the National Biodiversity Network's Gateway. Use it to explore UK biodiversity data, as contributed by participating <a href="/Organisations">data providers</a>.</p>
<p>The previous version of the NBN Gateway is no longer being updated but can still be accessed <a href="http://old-data.nbn.org.uk">here</a>.</p>
<p>Please post your comments and suggestions on the <a href="http://forums.nbn.org.uk/viewforum.php?id=22">NBN Forum</a>.</p>

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
            <tr>
                <td colspan="2"><a href="/AccessRequest/Create">Request Better Access</a></td>
            </tr>
        </table>
    </div>

    <div id="news-container">
        <h1><a title="RSS" href="${api}/datasets/latest.rss">RSS</a><a href="/Datasets">Latest Datasets</a></h1>
        <ul class="news-ticker">
            <#list json.readURL("${api}/datasets/latest") as dataset>
                <#assign organisation = json.readURL(dataset.organisationHref)>

                <li>
                    
                        <#if organisation.smallLogo??>
                            <img style="float:left; padding-right:5px;" src="${organisation.smallLogo}"> 
                        </#if>
                    <h3>${organisation.name} Dataset Updated: ${dataset.formattedDateUploaded}</h3>
                    <a href="${dataset.href}">${dataset.title}</a> 
                </li>

            </#list>
        </ul>
    </div>
</@template.master>