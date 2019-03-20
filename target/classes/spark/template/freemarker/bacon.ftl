<#assign content>
    <form method="POST" action="/bacon/results">
        Pass bacon from
        <input type="text" name="from">
        to
        <input type="text" name="to">
        <input type="submit" value="GO">
    </form>
</#assign>

<#include "baconMain.ftl">
