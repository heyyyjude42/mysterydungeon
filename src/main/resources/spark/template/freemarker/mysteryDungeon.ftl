<#assign content>
<div>
    <div id="output">
    </div>
    <div id="drawer">
    </div>
    <div id="window">
        <input type="button" id="toggle" value="Battle Manager">
        <div id="title_bar">
        </div>
        <div id="box">
            <table id="myTable" style="width:100%" contenteditable="true">
                <tr id='header'>
                    <th>Name</th>
                    <th>Initiative</th>
                    <th>AC</th>
                    <th>Health</th>
                </tr>
            </table>
        </div>
    </div>

    <input type="text" id="console">
    <input type="button" id="pushSide" value="Sticky Last Result(s)">
    <div id="dungeonButton" value="Dungeon Maker"><a href="/dungeonmaker"
                                                     target="_blank">Dungeon
            Maker</a></div>

    </#assign>

    <#include "main.ftl">
