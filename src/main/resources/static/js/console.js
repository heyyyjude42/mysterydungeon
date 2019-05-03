// Waits for DOM to load before running
const output = $("#output");

//Trackers for the Sticky notes.
let resultIndex = 0;
const resultList = [];
const simpToPretty = new Map();
let resultIDList = [];
const stickyNotes = $("#drawer");
let lastLine = "";

//Trackers for Encounter Manager.
let encounter = false;
let rowCount = 0;

/**
 * Setup these variables for user interaction on the landing page.
 * $userInput - On document ready setup the console reader
 * toggle - Toggle button for encounter manager.
 * drawerHandle - Sticky notes functionality.
 */
$(document).ready(() => {
    const $userInput = $("#console");

    //Check to see if user hits enter or up arrow key.
    //If enter, then query.
    $userInput.keydown((e) => {
        switch (e.keyCode) {
            case 13:
                lastLine = $userInput[0].value;
                query(lastLine);
                $userInput[0].value = "";
                break;
            case 38:
                $userInput[0].value = lastLine;
                break;
        }
    });
    
    //Hide the toggleable Battle Manager
    $("#box").slideToggle();
    const toggle = $('#toggle');
    toggle.click(() => {
        if(toggle.html() == "+"){
            toggle.html("-");
        }
        else{
            toggle.html("+");
        }
        $("#box").slideToggle();
    });
    const drawerHandle = $("#pushSide");
    drawerHandle.click(() => {
        console.log(output.length);
        if (resultIndex == 0) {
            console.log("Nothing to add.");
        } else {
            console.log("Storing result.");
            for(let v = 0; v<resultIndex; v++){
                console.log(resultIDList[v]);
                clickFun(resultIDList[v]);
            }
        }
        //stickyNotes.append("<p>" + resultList[resultIndex - 1] + "</p>");
        console.log("appending:");
        console.log(resultList);
    });
});

//Main clicking function after list has been retrieved.
function clickFun(id) {
    console.log("Clicking on list text works.");

    //Find the relevant text that is spit out by prettified.
    let expand = simpToPretty.get(id);

    //Id is the minimized text version
    let note = $("<li>" + id + "</li>");
    //Set up delete button.
    let button = document.createElement("button");
    button.setAttribute("class", "delBut");
    button.innerHTML = "X";
    //Set up minimize/maximize button.
    let minButton = document.createElement("button");
    minButton.setAttribute("class", "minBut");
    minButton.innerHTML = "▼";
    //Add the buttons for each sticky.
    stickyNotes.append(minButton);
    stickyNotes.append(button);
    stickyNotes.append(note);
    let minimized = true;
    //Button functions.
    minButton.addEventListener("click", function () {
        if (minimized) {
            minButton.innerHTML = "▲"
            note.html((expand));
            minimized = false;
        } else {
            minimized = true;
            minButton.innerHTML = "▼"
            note.html(id);
        }
    });
    button.addEventListener("click", function () {
        deleteEntry(note);
        button.remove();
        minButton.remove();
    });

}

/**
 * Deleting from the drawer.
 * @param {*} removeTag 
 *  The sticky to be removed.
 */
function deleteEntry(removeTag) {
    console.log("Remove is triggered.")
    removeTag.remove();
}

/**
 * Method to query the backend for the repl searches.
 * @param {*} line 
 *  The lin
 */
function query(line) {
    const postParameters = {input: line};
    resultIndex = 0;
    resultIDList = [];
    //Clear the encounter table if you're making another one.
    if(line.includes(("generate-encounter"))){
        if(encounter){
            deleteRows();
        }
    }
    output.append("<div class='query'>" + line + "</div>");

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result.results; // this gets the actual data properties
        const prettified = responseObject.prettified;
        const simplified = responseObject.simplified;

        // if there's only one result, display the whole thing. Otherwise, display a shortened list.
        if (result.length === 1) {
            if(line.includes("/help")){
                //Filter for the angle brackets, and replace them.
                while(simplified[0].includes("<")){
                   simplified[0] = simplified[0].replace("<", "&lt;");
                   simplified[0] = simplified[0].replace(">", "&gt;");
                }
                //Display the help response.
                let shortcut = "<div class='displayText'>" + simplified[0] + "</div>";
                output.append(shortcut);
            }else{
                //Display the clickable sticky.
                let shortcut = "<div class='displayText' id='resultIndex' onclick='clickFun(id)'>" + simplified[0] + "</div>";
                output.append(shortcut);
                simpToPretty.set(simplified[0], prettified[0]);
                let firstLineCut = prettified[0].split("\n");
                let end = prettified[0].substring(firstLineCut[0].length+1);
                //Generate dungeon has whitespace that will be simplified away, so we use <pre>
                if(line.includes("generate-dungeon")){
                    output.append("<div class='queryResults'><pre>" + end + "</pre></div>"); 
                }else{
                    output.append("<div class='queryResults'>" + end + "</div>");
                }
                document.getElementById("resultIndex").setAttribute("id", simplified[0]);
                resultIDList.push(simplified[0]);
                resultList.push(prettified[0]);
                resultIndex++;
            }
        } else {
            output.append("<div class='queryResults'>");
            for (let i = 0; i < prettified.length; i++) {
                output.append("<div class='tooltip'>" + "<div class='displayText' id='resultIndex' onclick='clickFun(id)'>" + simplified[i] + "</div>" +
                    "<div class='right'><div class='tooltipText'>" + prettified[i] + "</div><i></i></div>" +
                    "</div></br>");

                document.getElementById("resultIndex").setAttribute("id", simplified[i]);
                simpToPretty.set(simplified[i], prettified[i]);
                resultIDList.push(simplified[i]);
                resultList.push(prettified[i]);
                resultIndex++;
                console.log(prettified[i]);
                if(line.includes(("generate-encounter"))){
                    addRows(responseObject.result.results[i]);
                    rowCount++;
                }
            }
            output.append("</div>");
            //Setup the flag for clearing out the table after an encounter is made.
            if(line.includes(("generate-encounter"))){
                encounter = true;
            }
        }
        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}

/**
 * Method for deleting rows from the battle manager.
 */
function deleteRows(){
    for(let i = 0; i<rowCount; i++){
        document.getElementById("myTable").deleteRow(1);
    }
    rowCount = 0;
}

/**
 * Method to add rows to the battle manager.
 * @param {*} monster 
 *  Information to be added to each row in the encounter manager.
 */
function addRows(monster) {
    const table = document.getElementById("myTable");
    const row = table.insertRow(1);
    const cell1 = row.insertCell(0);
    const cell2 = row.insertCell(1);
    const cell3 = row.insertCell(2);
    const cell4 = row.insertCell(3);

    const initMod = Math.floor(monster.dex / 2) - 5;
    const postParameters = { input: "roll 1d20 + " + initMod };
    if (initMod < 0) {
        postParameters.input = "roll 1d20 " + initMod;
    }

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        cell2.innerHTML = responseObject.prettified[0].split(" ")[1];
    });

    cell1.innerHTML = monster.name;
    cell3.innerHTML = monster.ac;
    cell4.innerHTML = monster.hp;
  }