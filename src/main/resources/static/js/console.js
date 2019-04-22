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
        }  else {
            console.log("Storing result.");
            //stickyTrack = resultIndex-1;
            const removeTag  = $("<li>" + resultList[resultIndex - 1] + "</li>");
            stickyNotes.append(removeTag);
            let button = document.createElement("button");
            button.innerHTML = "X";
            //button.addEventListener("click", deleteEntry(removeTag));
            //button.addEventListener("click", onclick());
            button.addEventListener ("click", function() {
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

function deleteEntry(removeTag){
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
            output.append("<p>" + prettified[0] + "</p>"); // TODO: make this custom for each data type later
            resultList.push(prettified[0]);
            resultIndex++;
        } else {
            for (let i = 0; i < prettified.length; i++) {
                output.append("<div class='tooltip'>" + "<div class='displayText'>" + simplified[i] + "</div>" +
                    "<div class='right'><div class='tooltipText'>" + prettified[i] + "</div><i></i></div>" +
                    "</div></br>");
                resultList.push(prettified[i]);
                resultIndex++;
            }
        }        
        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}