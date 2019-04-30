// Waits for DOM to load before running
const output = $("#output");

let resultIndex = 0;
const resultList = [];
const stickyNotes = $("#drawer");
let lastLine = "";
let existingTileStyles = {};

$(document).ready(() => {
    const $userInput = $("#console");
    populateExistingTiles();

    $userInput.keydown((e) => {
        switch (e.keyCode) {
            case 13:
                // hack to test
                if ($userInput[0].value === "test") {

                    let rowHTML = "<div class='dungeonRow'>";

                    let one = neighborsToCssClass([false, false, false, false, false, true, false, true, true]);
                    let two = neighborsToCssClass([false, false, false, false, true, true, true, true, true]);
                    let three = neighborsToCssClass([false, false, false, true, true, false, true, true, false]);
                    let four = neighborsToCssClass([false, false, false, true, false, false, true, false, false]);
                    rowHTML += "<div class='tile meadow-" + one + "'/>";
                    rowHTML += "<div class='tile meadow-" + two + "'/>";
                    rowHTML += "<div class='tile meadow-" + three + "'/>";
                    rowHTML += "<div class='tile meadow-" + four + "'/>";

                    rowHTML += "</div>";
                    output.append(rowHTML);

                    rowHTML = "<div class='dungeonRow'>";

                    one = neighborsToCssClass([false, false, true, false, true, true, false, false, true]);
                    two = neighborsToCssClass([false, true, true, true, true, true, false, true, false]);
                    three = neighborsToCssClass([true, true, false, true, true, false, true, false, false]);
                    four = neighborsToCssClass([true, false, false, true, false, false, false, false, false]);
                    rowHTML += "<div class='tile meadow-" + one + "'/>";
                    rowHTML += "<div class='tile meadow-" + two + "'/>";
                    rowHTML += "<div class='tile meadow-" + three + "'/>";
                    rowHTML += "<div class='tile meadow-" + four + "'/>";

                    rowHTML += "</div>";
                    output.append(rowHTML);

                    rowHTML = "<div class='dungeonRow'>";

                    one = neighborsToCssClass([false, true, true, false, false, true, false, false, false]);
                    two = neighborsToCssClass([true, true, true, false, true, false, false, false, false]);
                    three = neighborsToCssClass([true, true, false, true, false, false, false, false, false]);
                    four = neighborsToCssClass([true, false, false, false, false, false, false, false, false]);
                    rowHTML += "<div class='tile meadow-" + one + "'/>";
                    rowHTML += "<div class='tile meadow-" + two + "'/>";
                    rowHTML += "<div class='tile meadow-" + three + "'/>";
                    rowHTML += "<div class='tile meadow-" + four + "'/>";

                    rowHTML += "</div>";
                    output.append(rowHTML);

                } else {
                    lastLine = $userInput[0].value;
                    query(lastLine);
                    $userInput[0].value = "";
                }
                break;
            case 38:
                $userInput[0].value = lastLine;
                break;
        }
    });
    const drawerHandle = $("#pushSide");
    drawerHandle.click(() => {
        console.log(output.length);
        if (resultIndex == 0) {
            console.log("Nothing to add.");
        } else {
            console.log("Storing result.");
            //stickyTrack = resultIndex-1;
            const removeTag = $("<li>" + resultList[resultIndex - 1] + "</li>");
            stickyNotes.append(removeTag);
            let button = document.createElement("button");
            button.innerHTML = "X";
            button.addEventListener("click", function () {
                deleteEntry(removeTag);
                button.remove();
            });
            stickyNotes.append(button);
            console.log(stickyNotes);
        }
        //stickyNotes.append("<p>" + resultList[resultIndex - 1] + "</p>");
        console.log("appending:");
        console.log(resultList);
    });


});

function populateExistingTiles() {
    let sheet;

    for (let i=0; i<document.styleSheets.length; i++) {
        let s = document.styleSheets[i];
        if (s.title === "tiles") {
            sheet = s;
        }
    }

    for (let i = 2; i < sheet.cssRules.length; i++) {
        existingTileStyles[sheet.cssRules[i].selectorText.split("-")[1]] = true;
    }
}

/**
 * Neighbors is a 3x3 array with boolean values, with T indicating it's traversable
 * @param neighbors
 */
function neighborsToCssClass(neighbors) {
    // first try to see if the whole thing exists
    let str = "";
    for (let i = 0; i < neighbors.length; i++) {
        str += boolToLetter(neighbors[i]);
    }

    if (existingTileStyles[str] != null) {
        return str;
    }

    // otherwise, extrapolate a heuristic from only the direct top/bottom/left/right
    const top = boolToLetter(neighbors[1]);
    const left = boolToLetter(neighbors[3]);
    const right = boolToLetter(neighbors[5]);
    const bottom = boolToLetter(neighbors[7]);
    const center = boolToLetter(neighbors[4]);
    let topLeft, topRight, bottomLeft, bottomRight;

    if (neighbors[4]) {
        // path
        topLeft = topRight = bottomLeft = bottomRight = false;
    } else {
        // wall
        topLeft = topRight = bottomLeft = bottomRight = true;
    }

    if (top === right && right === center) {
        topRight = !topRight;
    }
    if (top === left && left === center) {
        topLeft = !topLeft;
    }
    if (bottom === right && right === center) {
        bottomRight = !bottomRight;
    }
    if (bottom === left && left === center) {
        bottomLeft = !bottomLeft;
    }

    const tr = boolToLetter(topRight);
    const tl = boolToLetter(topLeft);
    const br = boolToLetter(bottomRight);
    const bl = boolToLetter(bottomLeft);

    return tl + top + tr + left + center + right + bl + bottom + br;
}

function boolToLetter(b) {
    if (b) {
        return "T";
    } else {
        return "F";
    }
}

//Main clicking function after list has been retrieved.
function clickFun(id) {
    console.log("Clicking on list text works.");

    //Find the relevant text that is spit out by prettified.
    let x = document.getElementById(id + " ID");
    let holder = x.innerHTML;
    let note = $("<li>" + holder + "</li>");
    //Set up delete button.
    let button = document.createElement("button");
    button.innerHTML = "X";
    //Set up minimize/maximize button.
    let minButton = document.createElement("button");
    minButton.innerHTML = "▲";
    stickyNotes.append(minButton);
    stickyNotes.append(button);
    stickyNotes.append(note);
    let minimized = false;
    minButton.addEventListener("click", function () {
        if (minimized) {
            minButton.innerHTML = "▲"
            note.html((holder));
            minimized = false;
        } else {
            //console.log(note);
            minimized = true;
            minButton.innerHTML = "▼"
            //deleteEntry(note);
            //note = $("<li>" + id + "</li>");
            note.html(id);
            //console.log(note);
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

    output.append("<div class='query'>" + line + "</div>");

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result.results; // this gets the actual data properties
        const prettified = responseObject.prettified;
        const simplified = responseObject.simplified;

        // if there's only one result, display the whole thing. Otherwise, display a shortened list.
        if (result.length === 1) {
            output.append("<div class='queryResults'>" + prettified[0] + "</div>"); // TODO: make this custom for each data type later
            resultList.push(prettified[0]);
            resultIndex++;
        } else {
            output.append("<div class='queryResults'>");
            for (let i = 0; i < prettified.length; i++) {
                output.append("<div class='tooltip'>" + "<div class='displayText' id='resultIndex' onclick='clickFun(id)'>" + simplified[i] + "</div>" +
                    "<div class='right'><div class='tooltipText' id='toolTextID'>" + prettified[i] + "</div><i></i></div>" +
                    "</div></br>");
                document.getElementById("resultIndex").setAttribute("id", simplified[i]);
                document.getElementById("toolTextID").setAttribute("id", simplified[i] + " ID");
                resultList.push(prettified[i]);
                resultIndex++;
            }
            output.append("</div>");
        }
        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}

