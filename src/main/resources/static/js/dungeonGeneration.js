let existingTileStyles = {};

$(document).ready(() => {

    $(document).on("click", () => {

        const postParameters = {width: 100, height: 100, avgRoomSize: "medium"};

        $.post("/dungeon", postParameters, responseJSON => {
            const responseObject = JSON.parse(responseJSON);

            let dungeon = responseObject.dungeon;
            let rooms = dungeon.rooms;

            console.log(dungeon);
        });
    });
});

function getRowHTML(neighborsData) {
    let rowHTML = "<div class='dungeonRow'>";

    neighborsData.forEach(n => {
       rowHTML += "<div class='tile meadow-" + neighborsToCssClass(n) + "'/>";
    });

    rowHTML += "</div>";
    return rowHTML;
}

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