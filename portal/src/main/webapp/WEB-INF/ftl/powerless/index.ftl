<#assign statistics = json.readURL("${api}/statistics")/>

<@template.master title="National Biodiversity Network Gateway"
        csss=["/css/homepage.css"]>
    <h1>Welcome to the NBN Gateway</h1>


    <div id="slidorion">
	<div id="slider">
            <div class="slide" style="background-color:white;">
                <p style="padding:2em; font-size: x-large;">This is the National Biodiversity Network's Gateway. Use it to explore UK 
                biodiversity data, as contributed by participating data providers.</p>
            </div>
            <div class="slide"><img src="http://data.nbn.org.uk/images/index_homepage/imt/gallery/imtEmpty.jpg"></div>
            <div class="slide"><img src="http://data.nbn.org.uk/images/index_homepage/imt/gallery/youtube.jpg"></div>
	</div>

	<div id="accordion">
            <div class="link-header">Welcome</div>
            <div class="link-content">All systems go for NBN Gateway </div>

            <div class="link-header">Interactive Map Tool</div>
            <div class="link-content">The new Interactive Map provides new ways to explore species records.</div>

            <div class="link-header">Hmm</div>
            <div class="link-content">
                The above map shows an individual moth species on the UK BAP 
                list – the white-spotted pinion (cosmia diffinis) with records 
                supplied by Butterfly Conservation’s National Moth Recording Scheme. 
                SSSI boundaries have also been selected for comparison
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
                    <h3><img src="${organisation.smallLogo!""}"> ${organisation.name} Dataset Updated: ${dataset.formattedDateUploaded}</h3>
                    <a href="${dataset.href}">${dataset.title}</a> 
                </li>

            </#list>
        </ul>
    </div>
</@template.master>