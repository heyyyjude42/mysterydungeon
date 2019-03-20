<#assign content>
    <p id="header"> Stars </p>

    <table>
        <tr>
            <th>NAME</th>
            <th>ID</th>
        </tr>
        <#list stars as star>
            <tr>
                <td>${star.getName()}</td>
                <td>${star.getID()}</td>
            </tr>
        </#list>
    </table>
</#assign>

<#include "main.ftl">
