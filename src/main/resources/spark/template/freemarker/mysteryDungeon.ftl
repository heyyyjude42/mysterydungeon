<#assign content>
    <div>
        <div id="output">
        </div>
        <div id="drawer">
        </div>
        <div id="window" contenteditable = "true">
        <input type="button" id="toggle" value = "+">
            <div id="title_bar">
            </div>
            <div id="box" contenteditable = "true">    
                <table id="myTable" style="width:100%">
                    <tr id = 'header'>
                        <th>Name</th>
                        <th>Initiative</th> 
                        <th>Armor Class</th>
                        <th>Health</th>
                    </tr>
                </table>    
            </div> 
    </div>

    <input type="text" id="console">
    <input type="button" id="pushSide" value = "Sticky Last Result(s)">
    
    
</#assign>

<#include "main.ftl">
