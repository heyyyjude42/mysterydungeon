// Waits for DOM to load before running
$(document).ready(() => {
    const $userInput = $("#console");

    $userInput.keydown((e) => {
        if (e.keyCode === 13) {
            query($userInput[0].value);
            $userInput[0].value = "";
        }
    });
});

function query(line) {
    const postParameters = {input: line};
    const output = $("#output");

    output.append("<p>" + line + "</p>");

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result.results; // this gets the actual data properties
        const prettified = responseObject.prettified;

        for (let i = 0; i < prettified.length; i++) {
            output.append("<p>" + prettified[i] + "</p>");
        }

        // scroll to bottom
        output.scrollTop(output[0].scrollHeight);
    });
}