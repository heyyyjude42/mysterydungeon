// Waits for DOM to load before running
$(document).ready(() => {
    const $userInput = $("#console");

    $userInput.keydown((e) => {
        if (e.keyCode === 13) {
            query($userInput[0].value);
        }
    });
});

function query(line) {
    const postParameters = {input: line};
    const output = $("#output");

    output.append("<li>" + line + "</li>");

    $.post("/query", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);
        const result = responseObject.result;

        console.log(result);

        for (let i = 0; i < result.results.length; i++) {
            console.log(result.results[i]);
            output.append("<li>" + result.results[i] + "</li>");
        }
    });
}