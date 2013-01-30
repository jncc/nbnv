<#assign statistics = json.readURL("${api}/statistics")/>

<@template.master title="National Biodiversity Network Gateway"
        csss=["/css/homepage.css"]>
    <h1>Welcome to the NBN Gateway</h1>


    <div id="slidorion">
	<div id="slider">
            <div class="slide welcome">
                <img src="/img/logo.jpg" width="700px" height=400px" />
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
                <@markdown>
This is the National Biodiversity Network's Gateway. 

Use it to explore UK biodiversity data, as contributed by participating [data providers](/Organisations).
                </@markdown>
            </div>

            <div class="link-header">Interactive Map Tool</div>
            <div class="link-content">
                <@markdown>
The [Interactive Map Tool](/imt) provides new ways to explore species records.
                </@markdown>
            </div>

            <div class="link-header">Search by Location</div>
            <div class="link-content">
                <@markdown>
You can use the NBN gateway to find where species have been recorded using grid 
squares or sites.
                </@markdown>
            </div>

            <div class="link-header">Grid Map</div>
            <div class="link-content">
                <@markdown>
The Grid Map product allows you to view the grid squares which have been recorded against.

You can get to this by searching for a [species](/Taxa) or searching for a 
[dataset](/Datasets)
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