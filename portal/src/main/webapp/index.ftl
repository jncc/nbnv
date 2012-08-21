<@template.master title="National Biodiversity Network Gateway">
    <@markdown>
#Welcome to the NBN Gateway

This server has been configured to run with ${api}

The following pages will demonstrate the development of the NBN Gateway.
    </@markdown>
    Logged in as = ${json.readURL("${api}/user")("toString")}
    <@image_map.hundredKM/>
</@template.master>