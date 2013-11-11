<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Acknowledgment for ${url}</title>
        <#if externalCss??>
            <link href="${externalCss}" rel="stylesheet" type="text/css">
        <#else>
            <style><#include "acknowledgementDefaultStyle.css"/></style>
        </#if>
    </head>
    <body>
        <table class="nbn-acknowledgment-table">
            <tr class="nbn-acknowledgment-headerRow">
                <th class="nbn-acknowledgment-headerDatasetProvider">Dataset Provider</th>
                <th class="nbn-acknowledgment-headerDataset">Dataset</th>
            </tr>  
            <#list providers as provider>
                <#list provider.datasetsWithQueryStats as datasetStats>
                    <tr class="nbn-acknowledgment-entryRow">
                        <#if datasetStats_index == 0>
                            <td class="nbn-acknowledgment-datasetProvider" rowspan="${provider.datasetsWithQueryStats?size}">
                                <a href="${provider.organisation.href}">${provider.organisation.name}</a>
                            </td>
                        </#if>
                        <td class="nbn-acknowledgment-datasetTitle">
                            <a href="${datasetStats.taxonDataset.href}">${datasetStats.taxonDataset.title}</a>
                        </td>
                    </tr>
                </#list>
            </#list>
        </table>
    </body>
</html>