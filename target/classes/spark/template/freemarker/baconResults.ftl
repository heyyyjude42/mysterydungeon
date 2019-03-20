<#assign content>
    <ul>
        <#list steps as step>
            <li>
                <<a href=${step.getPersonFrom().getUrl()}>${step.getPersonFrom().getName()}</a>
                passes to
                <a href=${step.getPersonTo().getUrl()}>${step.getPersonTo().getName()}</a>
                at the
                <a href=${step.getFilm().getUrl()}>${step.getFilm().getName()}</a>
                premiere
            </li>
        </#list>
    </ul>
</#assign>

<#include "baconMain.ftl">
