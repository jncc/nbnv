<#assign user = json.readURL("${api}/user")/>

<@template.master title="National Biodiversity Network Gateway">
    <h1>Administration Options for ${user.forename} ${user.surname}</h1>
</@template.master>