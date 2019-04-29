

$(document).ready(() => {

    $(document).on("click", () => {

        const postParameters = {width: 100, height: 100, avgRoomSize: "small"};

        $.post("/dungeon", postParameters, responseJSON => {
            const responseObject = JSON.parse(responseJSON);

            let dungeon = responseObject.dungeon;
            let rooms = dungeon.rooms;

            console.log(dungeon);
        });
    });
});