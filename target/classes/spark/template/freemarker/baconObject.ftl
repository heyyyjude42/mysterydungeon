<#assign content>
    <h5>${title}</h5>
    <h6>${subtitle}</h6>

    <#list list as result>
        <p><a href=${result.getUrl()}>${result.getName()}</a></p>
    </#list>
</#assign>

<#include "baconMain.ftl">
