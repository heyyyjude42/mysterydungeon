// Waits for DOM to load before running
const output = $("#output");

let resultIndex = 0;
const resultList = [];
const simpToPretty = new Map();
let resultIDList = [];
const stickyNotes = $("#drawer");
let lastLine = "";


$(document).ready(() => {
    const $userInput = $("#console");

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
    //let x = document.getElementById(id + " ID");
    let expand = simpToPretty.get(id);

    //let holder = x.innerHTML;
    let note = $("<li>" + id + "</li>");
    //Set up delete button.
    let button = document.createElement("button");
    button.innerHTML = "X";
    //Set up minimize/maximize button.
    let minButton = document.createElement("button");
    minButton.innerHTML = "▼";
    stickyNotes.append(minButton);
    stickyNotes.append(button);
    stickyNotes.append(note);
    let minimized = true;
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

function deleteEntry(removeTag) {
    console.log("Remove is triggered.")
    removeTag.remove();
}

function query(line) {
    const postParameters = {input: line};
    resultIndex = 0;
    resultIDList = [];
    output.append("<div class='query'>" + line + "</div>");
    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result.results; // this gets the actual data properties
        const prettified = responseObject.prettified;
        const simplified = responseObject.simplified;

        // if there's only one result, display the whole thing. Otherwise, display a shortened list.
        if (result.length === 1) {
            let shortcut = "<div class='displayText' id='resultIndex' onclick='clickFun(id)'>" + simplified[0] + "</div>";
            output.append(shortcut);
            //output.append("<div class='queryResults' id='explanation'>" + prettified[0] + "</div>"); // TODO: make this custom for each data type later
            simpToPretty.set(simplified[0], prettified[0]);
            //console.log(prettified[0]);
            let sLeng = simplified[0].length;
            let firstLineCut = prettified[0].split("\n");
            let end = prettified[0].substring(firstLineCut[0].length+1);
            console.log(firstLineCut[0].length);
            output.append("<div class='queryResults'>" + end + "</div>");
            document.getElementById("resultIndex").setAttribute("id", simplified[0]);
            resultIDList.push(simplified[0]);
            resultList.push(prettified[0]);
            resultIndex++;
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
                if(line.includes(("generate-encounter"))){
                    myFunction(prettified[i]);
                }
            }
            output.append("</div>");
        }
        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}

function myFunction(longInfo) {
    var table = document.getElementById("myTable");
    var row = table.insertRow(1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    let infoArray = longInfo.split("\n");
    
    cell1.innerHTML = infoArray[0];
    cell2.innerHTML = "Initiative X";
    cell3.innerHTML = infoArray[3].substring(13);
    cell4.innerHTML = infoArray[4].substring(12);
  }