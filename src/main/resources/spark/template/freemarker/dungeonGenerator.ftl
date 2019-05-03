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
            <option value="sidepath.png">Side Path</option>
            <option value="forestpath.png">Forest Path</option>
            <option value="snowpointpass.png">Snowpoint Pass</option>
            <option value="shimmerdesert.png">Shimmer Desert</option>
            <option value="mtbristle.png">Mt Bristle</option>
            <option value="quicksandcave.png">Quicksand Cave</option>
            <option value="foggyforest.png">Foggy Forest</option>
            <option value="tinywoods.png">Tiny Woods</option>
            <option value="skytower.png">Sky Tower</option>
            <option value="greatcanyon.png">Great Canyon</option>
            <option value="iceaegis.png">Ice Aegis</option>
            <option value="northerndesert.png">Northern Desert</option>
            <option value="brinecave.png">Brine Cave</option>
            <option value="temporaltower.png">Temporal Tower</option>
            <option value="concealedruins.png">Concealed Ruins</option>
            <option value="steamcave.png">Steam Cave</option>
            <option value="mystifyingforest.png">Mystifying Forest</option>
            <option value="thenightmare.png">The Nightmare</option>
        </select> <br/><br/>

        <button id="generateButton" class="primaryButton">Generate!</button><br/><br/>

        <input type="checkbox" id="genTraps" name="genTraps" value="true">
        <label for="genTraps">Draw traps</label><br/>
        <input type="checkbox" id="genLoot" name="genLoot" value="true">
        <label for="genLoot">Draw loot</label><br/><br/><br/><br/>

        <button id="importButton" class="secondaryButton">Import</button><br/>
        <button id="exportButton" class="secondaryButton">Export</button>
        <div id="exportMessage">Copied to clipboard!</div>
    </div>


</#assign>

<#include "dungeonMain.ftl">
