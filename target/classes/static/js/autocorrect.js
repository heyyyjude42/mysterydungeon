// Waits for DOM to load before running
$(document).ready(() => {
    let currWord = "";
    const $userInput = $("#userInput")[0];
    const $suggestions = $("#suggestions");

    $userInput.oninput = () => {
        currWord = $userInput.value;
        const postParameters = {word: currWord};

        $.post("/validate", postParameters, responseJSON => {
            const responseObject = JSON.parse(responseJSON);

            $suggestions.empty();
            const suggestions = responseObject.suggestions;

            for (let i = 0; i < suggestions.length; i++) {
                $suggestions.append("<li>" + suggestions[i] + "</li>");
            }

        });
    };
});