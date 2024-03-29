let existingTileStyles = {};
let currDungeon = {};

/**
 * Runs when the page loads: attaches handlers on the various buttons and options.
 */
$(document).ready(() => {
    $("#importButton").on("click", importPressed);
    $("#exportButton").on("click", exportPressed);
    $("#generateButton").on("click", generatePressed);
    $("#terrainForm").on("change", terrainChanged);
});

/**
 * Called when the terrain selection is changed. Redraws the dungeon as-is to reskin.
 */
function terrainChanged() {
    drawDungeon();
}

/**
 * Called when the import button is pressed. Opens a prompt for the user to copy
 * in the dungeon data and attempts to parse it. If it can't be parsed, then
 * reports an error message and keeps the old dungeon.
 */
function importPressed() {
    const val = prompt("Paste dungeon object here:");
    const cachedDungeon = currDungeon;
    try {
        currDungeon = JSON.parse(val);
        drawDungeon();
    } catch (e) {
        currDungeon = cachedDungeon;
        alert("could not parse object :(");
    }
}

/**
 * Called when export is pressed. Copies the dungeon to the clipboard and
 * displays a quick message.
 */
function exportPressed() {
    const ta = document.createElement('textarea');
    ta.value = JSON.stringify(currDungeon);
    document.body.appendChild(ta);
    ta.select();
    document.execCommand("copy");
    document.body.removeChild(ta);

    $('#exportMessage').show().delay(700).fadeOut();
}

/**
 * Called when generate is pressed. Takes in the user's width/height/size
 * selections and sends a POST request to the backend. Displays the result.
 */
function generatePressed() {
    let width = $("#widthForm").val();
    let height = $("#heightForm").val();
    let size = $("input[type='radio']:checked").val();

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

    const postParameters = {
        width: width,
        height: height,
        avgRoomSize: size
    };

    $.post("/dungeon", postParameters, responseJSON => {
        const responseObject = JSON.parse(responseJSON);

        responseObject.dungeon.occupiedCells = responseObject.dungeon.occupiedCells.map(row => {
            return row.map(cell => {
                return cell != null;
            });
        });

        currDungeon = responseObject;
        drawDungeon();
    });
}

/**
 * Draws the dungeon!
 */
function drawDungeon() {
    // get the currently selected terrain, and whether we draw loot/traps
    let terrain = $("#terrainForm").val();
    let drawTraps = $.map($("input[name='genTraps']:checked"), t => {
        return t.value;
    }).length > 0;
    let drawLoot = $.map($("input[name='genLoot']:checked"), t => {
        return t.value;
    }).length > 0;

    if (terrain == null) {
        terrain = "sidepath";
    }
    populateExistingTiles(terrain);

    let dungeon = currDungeon.dungeon;
    let cells = dungeon.occupiedCells;

    // draw the map based on the cells and terrain
    drawMap(cells, terrain);

    // check to see if we draw traps on top
    if (drawTraps) {
        drawAllTraps(dungeon.rooms);
    }

    // check to see if we draw loot on top
    if (drawLoot) {
        drawAllLoot(dungeon.rooms);
    }
}

/**
 * Draws any loot in the given rooms. If no traps exist then nothing happens.
 * @param rooms Rooms to draw loot from
 */
function drawAllLoot(rooms) {
    rooms.forEach(room => {
        if (room.elements.length > 0) {
            const loot = room.elements.filter(e => {
                return e.contents != null;

            });

            loot.forEach(l => {
                const pos = {
                    x: room.topLeftCorner.x + l.position.x,
                    y: room.topLeftCorner.y - l.position.y
                };

                drawLoot(l, pos);
            });
        }
    });
}

/**
 * Draws any traps in the given rooms. If no loot exists then nothing happens.
 * @param rooms Rooms to find loot in.
 */
function drawAllTraps(rooms) {
    rooms.forEach(room => {
        if (room.elements.length > 0) {
            const traps = room.elements.filter(e => {
                return e.damage != null;
            });
            traps.forEach(trap => {
                const pos = {
                    x: room.topLeftCorner.x + trap.position.x,
                    y: room.topLeftCorner.y - trap.position.y
                };

                drawTrap(trap, pos);
            });
        }
    });
}

/**
 * Draws a single loot element on the map.
 * @param loot The loot object
 * @param pos The position of the loot
 */
function drawLoot(loot, pos) {
    let tooltipText = "Platinum: " + loot.contents[0].platinum + "<br/>";
    tooltipText += "Gold: " + loot.contents[0].gold + "<br/>";
    tooltipText += "Silver: " + loot.contents[0].silver + "<br/>";
    tooltipText += "Copper: " + loot.contents[0].copper;

    drawDungeonElement("loot", tooltipText, pos);
}

/**
 * Draws a single trap element on the map.
 * @param trap the trap object
 * @param pos the position of the trap
 */
function drawTrap(trap, pos) {
    let tooltipText = "Detection DC: " + trap.detectionDC + "<br/>";
    tooltipText += "Disarm DC: " + trap.disableDC + "<br/>";
    tooltipText += "Save DC: " + trap.saveDC + "<br/>";
    tooltipText += "Damage: " + trap.damage;

    drawDungeonElement("trap", tooltipText, pos);
}

/**
 * Draws a dungeon element: trap or loot, or monsters later to come.
 * @param cssClass The css class this element should have.
 * @param tooltipText What to display on the hover text
 * @param pos The position (in tile coordinates)
 */
function drawDungeonElement(cssClass, tooltipText, pos) {
    const $map = $("#map");
    const top = pos.y * 24;
    const left = pos.x * 24;

    let html = "<div class='tooltip' style='position:absolute;top:" + top + "px;left:" + left + "px;'>" + "<div class='displayText' style='border-bottom:0;'>";
    html += "<div class='" + cssClass + "'></div>";
    html += "</div>" + "<div class='right'><div class='tooltipText'>" + tooltipText + "</div><i></i></div>" + "</div>";

    $map.append(html);
}

/**
 * Draws the whole map.
 * @param cells Occupied cells.
 * @param terrain Terrain to draw with.
 */
function drawMap(cells, terrain) {
    const $map = $("#map");
    $map.empty();

    for (let i = 0; i < cells.length; i++) {
        const row = cells[i];
        let rowNeighbors = [];

        for (let col = 0; col < row.length; col++) {
            rowNeighbors.push(getNeighbors(cells, i, col));
        }

        const rowHTML = getRowHTML(rowNeighbors);
        $map.append(rowHTML);
    }
}

/**
 * Returns a length-9 array of this cell's surrounding traversableness.
 * @param allCells 2D array of the whole map's cell occupations.
 * @param row The row to find neighbors of.
 * @param col The column to find neighbors of.
 * @returns {Array} The array. i.e. TTTTTTTT means the cell and the cells around it are traversable.
 */
function getNeighbors(allCells, row, col) {
    let neighbors = [];
    for (let rowOffset = -1; rowOffset < 2; rowOffset++) {
        for (let colOffset = -1; colOffset < 2; colOffset++) {
            neighbors.push(getTraversable(allCells, row + rowOffset, col + colOffset));
        }
    }

    return neighbors;
}

/**
 * Returns whether this cell is traversable.
 * @param allCells 2D array of the whole map's cell occupations.
 * @param row The row to find traversability of.
 * @param col The column.
 * @returns True or false.
 */
function getTraversable(allCells, row, col) {
    if (row < 0 || row >= allCells.length || col < 0 || col >= allCells[0].length) {
        return false;
    }

    // if there is something in the array, then it's a room, so it's traversable.
    return allCells[row][col];
}

/**
 * Gets the HTML for a whole row.
 * @param neighborsData The neighbors data for the whole row, one for each cell in the row.
 * @returns {string} An HTML string.
 */
function getRowHTML(neighborsData) {
    let rowHTML = "<div class='dungeonRow'>";

    neighborsData.forEach(n => {
        // for each cell, get what its corresponding CSS location would be.
        const cssStyle = existingTileStyles[neighborsToCssClass(n)];
        rowHTML += "<div class='tile' style=\"" + cssStyle + "\"/>";
    });

    rowHTML += "</div>";
    return rowHTML;
}

/**
 * Based on the current terrain, populate the dictionary of what the CSS style
 * for each T/F neighbor combination should be.
 * @param terrain The terrain.
 */
function populateExistingTiles(terrain) {
    let sheet;

    for (let i = 0; i < document.styleSheets.length; i++) {
        let s = document.styleSheets[i];
        if (s.title === "tiles") {
            sheet = s;
        }
    }

    for (let i = 2; i < sheet.cssRules.length; i++) {
        const tileStyleName = sheet.cssRules[i].selectorText.split(".")[1];
        let tileStyle = sheet.cssRules[i].cssText;
        tileStyle = tileStyle.split("{ ")[1].split(" }")[0];
        //tileStyle = tileStyle.replace("background:", "background: url('https://www.spriters-resource.com/resources/sheets/82/84825.png')");
        tileStyle = tileStyle.replace("background:", "background: url('css/spritepacks\/" + terrain + "')");
        existingTileStyles[tileStyleName] = tileStyle;
    }
}

/**
 * Neighbors is a 3x3 array with boolean values, with T indicating it's traversable.
 * @param neighbors The 3x3 array.
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

/**
 * Randomize some tiles with alts for flavor/freshness.
 * @param concat
 * @returns {*}
 */
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

/**
 * Turns true to T and false to F.
 * @param b the boolean
 * @returns {string} T or F
 */
function boolToLetter(b) {
    if (b) {
        return "T";
    } else {
        return "F";
    }
}