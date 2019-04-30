<#assign content>
    <div id="map"></div>
    <div id="sidebar">
        <h3>DUNGEON MAKER</h3><br/>
        Width <br/>
        <input type="number" id="widthForm" value="60"><br/><br/>

        Height <br/>
        <input type="number" id="heightForm" value="60"><br/><br/>

        Room Size <br/>
        <form id="roomSizeForm">
            <input type="radio" id="sizeSmall" name="sizeForm" value="small"><label for="sizeSmall">Small</label><br/>
            <input type="radio" id="sizeMedium" name="sizeForm" value="medium" checked="checked"><label for="sizeMedium">Medium</label><br/>
            <input type="radio" id="sizeLarge" name="sizeForm" value="large"><label for="sizeLarge">Large</label><br/>
        </form><br/>

        Terrain <br/>
        <select id="terrainForm">
            <option value="meadow">Meadow</option>
        </select> <br/><br/>

        <button id="generateButton">Generate!</button>
    </div>


</#assign>

<#include "dungeonMain.ftl">
