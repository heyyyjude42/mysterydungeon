// Waits for DOM to load before running
const output = $("#output");

let resultIndex = 0;
const resultList = [];

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
    const drawerHandle = $("#pushSide");
    const stickyNotes = $("#drawer");
    drawerHandle.click(() => {
        console.log(output.length);
        if (resultIndex == 0) {
            console.log("Nothing to add.");
        } else {
            console.log("Storing result.");
        }
        stickyNotes.append("<p>" + resultList[resultIndex - 1] + "</p>");
        console.log("appending:");
        console.log(resultList);
    });

    //drawerHandle.addEventListener("click", onclick);

});

function query(line) {
    const postParameters = {input: line};

    output.append("<div class='query'>" + line + "</div>");

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result.results; // this gets the actual data properties
        const prettified = responseObject.prettified;
        const simplified = responseObject.simplified;

        // if there's only one result, display the whole thing. Otherwise, display a shortened list.
        let append = "<div class='queryResults'>";
        if (result.length === 1) {
            append += "<div>" + prettified[0] + "</div>"; // TODO: make this custom for each data type later
        } else if (result.length === 0) {
            append += "<div>Didn't find anything :(</div>";
        } else {
            for (let i = 0; i < prettified.length; i++) {
                append += "<div class='tooltip'>" + "<div class='displayText'>" + simplified[i] + "</div>" +
                    "<div class='right'><div class='tooltipText'>" + prettified[i] + "</div><i></i></div>" +
                    "</div></br>";
                resultList.push(prettified[i]);
            }
        }
        append += "</div>";
        output.append(append);

        resultIndex++;
        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}