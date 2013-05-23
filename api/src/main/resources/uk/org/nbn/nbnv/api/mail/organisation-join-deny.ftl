Dear ${name}

Your request to join ${organisation} has been denied.

<#if responseReason?length == 0>
No reason was given for this action.
<#else>
The following reason was given for this denial;
${responseReason}
</#if>

Many Thanks,

The NBN Gateway Team