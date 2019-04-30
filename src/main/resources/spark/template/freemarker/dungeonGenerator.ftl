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
            <option value="concealedruins">Concealed Ruins</option>
            <option value="forestpath">Forest Path</option>
            <option value="foggyforest">Foggy Forest</option>
            <option value="brinecave">Brine Cave</option>
            <option value="qscave">Quicksand Cave</option>
            <option value="ndesert">Northern Desert</option>
            <option value="sidepath">Side Path</option>
            <option value="tower">Temporal Tower</option>
            <option value="bristle">Mt Bristle</option>
            <option value="nightmare">The Nightmare</option>
            <option value="desert">Shimmer Desert</option>
            <option value="meadow">Tiny Meadow</option>
            <option value="mystforest">Mystifying Forest</option>
            <option value="steamcave">Steam Cave</option>
            <option value="aegis">Ice Aegis</option>
        </select> <br/><br/>

        <button id="generateButton">Generate!</button>
    </div>


</#assign>

<#include "dungeonMain.ftl">
