This is an automatically generated message from the NBN Gateway - PLEASE DO NOT REPLY. 

Dear ${name}

You are receiving this email as you are the administrator of a dataset on the NBN Gateway, the following are monthly download statistics for the dataset ${datasetName};

Total Downloads:                                        ${totalDownloads}
Total Records Downloaded:                               ${totalRecordsDownloaded}
Records Downloaded for purpose                          
<#if R1??>    Personal interest                                    ${R1} (${P1}%)</#if>
<#if R2??>     Educational purposes                                ${R2} (${P2}%)</#if>
<#if R3??>     Research and scientific analysis                    ${R3} (${P3}%)</#if>
<#if R4??>     Media publication                                   ${R4} (${P4}%)</#if>
<#if R5??>     Commercial and consultancy work                     ${R5} (${P5}%)</#if>
<#if R6??>     Professional land management                        ${R6} (${P6}%)</#if>
<#if R7??>     Data provision and interpretation (commercial)      ${R7} (${P7}%)</#if>
<#if R8??>     Data provision and interpretation (non-profit)      ${R8} (${P8}%)</#if>
<#if R9??>     Statutory work                                      ${R9} (${P9}%)</#if>

Top 5 User Downloaders
<#list users as user>
${user.name}    ${user.total} Records   ${user.totalAlt} Downloads
</#list>

Top 5 Organisation Downloaders
<#list orgs as org>
${org.name}    ${org.total} Records   ${org.totalAlt} Downloads
</#list>

You can see further statistics about individual downloads against this dataset on the download report page at;

${portal}/Reports/Download/${dataset}

Best wishes, 
NBN Gateway Team 