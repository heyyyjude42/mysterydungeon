

$(document).ready(() => {

    $(document).on("click", () => {

        const postParameters = {width: 100, height: 100, avgRoomSize: "small"};

        $.post("", postParameters, responseJSON => {
            const responseObject = JSON.parse(responseJSON);

            console.log(responseObject);
        });
    });
});