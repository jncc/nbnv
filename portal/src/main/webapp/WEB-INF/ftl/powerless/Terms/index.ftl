<@template.master title="NBN Gateway - General Terms and Conditions">
    
    <h1>NBN Gateway Terms and Conditions</h1>
    <strong><@markdown file="Important Statement.md"/></strong>

    <@markdown>
We may change these *Gateway Terms & Conditions*, or our *Privacy Policy* at any 
time without giving you notice, so please check them each time you visit this 
website. Additionally, any *Data Provider* may add, change or remove 
*Specific Terms of Access and/or Use* for a dataset they administer on the NBN Gateway 
at any time without giving you notice, so please check these within the *Metadata*
provided for any dataset each time you access and use them.

-------------------------------------------------------------------------------
    </@markdown>

    <#assign contents=[
        {   "title":"Use of the NBN Gateway", 
            "file":"Use of the Gateway.md",                         
            "description":"Rules governing your use of the NBN Gateway website service ( data.nbn.org.uk and associated web-services)."},
        {   "title":"Use of material, data and/or information", 
            "file":"Use of Material.md",                            
            "description":"Rules governing your use of any material, data and/or information made available to you through the NBN Gateway website services."},
        {   "title":"Accuracy of Information and Disclaimer", 
            "file":"Accuracy of Information and Disclaimer.md",     
            "description":"Data accuracy and limitation warnings."},
        {   "title":"Data Protection and Privacy", 
            "file":"Data Protection and Privacy.md",                
            "description":"The collection, use and disclosure of personal data."},
        {   "title":"Third Party Goods and Services and Links", 
            "file":"Third Party Goods and Services and Links.md",   
            "description":"The material, data and/or information that others make available or link to through our website."},
        {   "title":"Our Liability", 
            "file":"Our Liability.md",                              
            "description":"The extent of our liability and that of our data providers."},
        {   "title":"General", 
            "file":"General Information.md",                        
            "description":"Legal enforcement and jurisdiction."},
        {   "title":"Further Information", 
            "file":"Further Information.md",                        
            "description":"Who you can contact for further information or help."}
    ]>

    <h1>Contents</h1>
    <ol>
        <#list contents as currSection>
            <li><a title="${currSection.description}" href="#section-${currSection_index}">${currSection.title}</a></li>
        </#list>
    </ol>

    <#list contents as currSection>
        <h2 id="section-${currSection_index}">${currSection.title}</h2>
        <@markdown file="${currSection.file}"/>
    </#list>
</@template.master>