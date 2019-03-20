<#assign content>
<p id="header"> Stars </p>
<p> FIND ALL STARS...
<form method="POST" action="/results">
  <input type="number" name="number" placeholder="#">

  <select name="command">
    <option value="neighbors">neighbors away from</option>
    <option value="radius">units away from</option>
  </select>

  <select name="locationType">
    <option value="name">the star named</option>
    <option value="coordinate">the coordinate</option>
  </select>

  <input type="text" name="location" placeholder="name or x y z">

  <br><br><br><br>
  <input type="submit" value="SEARCH">
</form>
</p>

</#assign>
<#include "main.ftl">
