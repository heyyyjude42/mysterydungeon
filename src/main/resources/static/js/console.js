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


    output.append("<p>" + line + "</p>");

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result.results; // this gets the actual data properties
        const prettified = responseObject.prettified;

        for (let i = 0; i < prettified.length; i++) {
            output.append("<p>" + prettified[i] + "</p>");
            resultList.push(prettified[i]);
        }
        resultIndex++;
        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}