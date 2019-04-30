let existingTileStyles = {};

$(document).ready(() => {
    populateExistingTiles();
    $("#generateButton").on("click", () => {
        let width = $("#widthForm").val();
        let height = $("#heightForm").val();
        let size = $("input[type='radio']:checked").val();
        let terrain = $("#terrainForm").val();

        // nullcheck
        if (width == null) {
            width = 60;
        }
        if (height == null) {
            height = 60;
        }
        if (size == null) {
            size = "medium";
        }
        if (terrain == null) {
            terrain = "meadow";
        }

        const postParameters = {
            width: width,
            height: height,
            avgRoomSize: size
        };

        $.post("/dungeon", postParameters, responseJSON => {
            const responseObject = JSON.parse(responseJSON);

            let dungeon = responseObject.dungeon;
            let cells = dungeon.occupiedCells;
            drawMap(cells, terrain);
        });
    });
});

function drawMap(cells, terrain) {
    const $map = $("#map");
    $map.empty();

    for (let i = 0; i < cells.length; i++) {
        const row = cells[i];
        let rowNeighbors = [];

        for (let col = 0; col < row.length; col++) {
            rowNeighbors.push(getNeighbors(cells, i, col));
        }

        const rowHTML = getRowHTML(rowNeighbors, terrain);
        $map.append(rowHTML);
    }
}

function getNeighbors(allCells, row, col) {
    let neighbors = [];
    for (let rowOffset = -1; rowOffset < 2; rowOffset++) {
        for (let colOffset = -1; colOffset < 2; colOffset++) {
            neighbors.push(getTraversable(allCells, row + rowOffset, col + colOffset));
        }
    }

    return neighbors;
}

function getTraversable(allCells, row, col) {
    if (row < 0 || row >= allCells.length || col < 0 || col >= allCells[0].length) {
        return false;
    }

    // if there is something in the array, then it's a room, so it's traversable.
    return allCells[row][col] != null;
}

function getRowHTML(neighborsData, terrain) {
    let rowHTML = "<div class='dungeonRow'>";

    neighborsData.forEach(n => {
        rowHTML += "<div class='tile " + terrain + "-" + neighborsToCssClass(n) + "'/>";
    });

    rowHTML += "</div>";
    return rowHTML;
}

function populateExistingTiles() {
    let sheet;

    for (let i = 0; i < document.styleSheets.length; i++) {
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
        return flavorTileWithAlts(str);
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
        topRight = neighbors[2];
    }
    if (top === left && left === center) {
        topLeft = neighbors[0];
    }
    if (bottom === right && right === center) {
        bottomRight = neighbors[8];
    }
    if (bottom === left && left === center) {
        bottomLeft = neighbors[6];
    }

    const tr = boolToLetter(topRight);
    const tl = boolToLetter(topLeft);
    const br = boolToLetter(bottomRight);
    const bl = boolToLetter(bottomLeft);

    return flavorTileWithAlts(tl + top + tr + left + center + right + bl + bottom + br);
}

// randomize alts for flavor!
function flavorTileWithAlts(concat) {
    const rand = Math.floor((Math.random() * 100) + 1);
    if (concat === "FFFFFFFFF") {
        if (rand <= 3) {
            return concat + "-alt2";
        } else if (rand <= 6) {
            return concat + "-alt";
        } else {
            return concat;
        }
    } else if (concat === "TTTTTTTTT") {
        if (rand <= 5) {
            return concat + "-alt2";
        } else if (rand <= 12) {
            return concat + "-alt";
        } else {
            return concat;
        }
    } else if (concat === "TFFTFFTFF" || concat === "FFTFFTFFT" || concat === "FFFFFFTTT" || concat === "TTTFFFFFF") {
        if (rand <= 28) {
            return concat + "-alt";
        } else if (rand <= 60) {
            return concat + "-alt2";
        } else {
            return concat;
        }
    } else {
        return concat;
    }
}

function boolToLetter(b) {
    if (b) {
        return "T";
    } else {
        return "F";
    }
}