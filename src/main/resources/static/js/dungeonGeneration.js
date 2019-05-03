let existingTileStyles = {};
let currDungeon = {};

$(document).ready(() => {
    $("#importButton").on("click", importPressed);
    $("#exportButton").on("click", exportPressed);
    $("#generateButton").on("click", generatePressed);
    $("#terrainForm").on("change", terrainChanged);
});

function terrainChanged() {
    drawDungeon();
}

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

function exportPressed() {
    const ta = document.createElement('textarea');
    ta.value = JSON.stringify(currDungeon);
    document.body.appendChild(ta);
    ta.select();
    document.execCommand("copy");
    document.body.removeChild(ta);

    $('#exportMessage').show().delay(700).fadeOut();
}

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

function drawDungeon() {
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

    drawMap(cells, terrain);


    if (drawTraps) {
        drawAllTraps(dungeon.rooms.filter(room => {
            let hasTrap = false;
            if (room.elements.length > 0) {
                room.elements.forEach(e => {
                    if (e.damage != null) {
                        hasTrap = true;
                    }
                });
            }
            return hasTrap;
        }));
    }

    if (drawLoot) {
        drawAllLoot(dungeon.rooms.filter(room => {
            let hasloot = false;
            if (room.elements.length > 0) {
                room.elements.forEach(e => {
                    if (e.contents != null) {
                        hasloot = true;
                    }
                });
            }
            return hasloot;
        }));
    }
}

function drawAllLoot(rooms) {
    rooms.forEach(room => {
        const loot = room.elements.filter(e => {
            if (e.contents != null) {
                return true;
            }
            return false;
        });

        loot.forEach(l => {
            const pos = {
                x: room.topLeftCorner.x + l.position.x,
                y: room.topLeftCorner.y - l.position.y
            }

            drawLoot(l, pos);
        });
    });
}

function drawAllTraps(rooms) {
    rooms.forEach(room => {
        const traps = room.elements.filter(e => {
            if (e.damage != null) {
                return true;
            }
            return false;
        });
        traps.forEach(trap => {
            const pos = {
                x: room.topLeftCorner.x + trap.position.x,
                y: room.topLeftCorner.y - trap.position.y
            };

            drawTrap(trap, pos);
        });
    });
}

function drawLoot(loot, pos) {
    const $map = $("#map");
    const top = pos.y * 24;
    const left = pos.x * 24;

    console.log(loot);

    let tooltipText = "Platinum: " + loot.contents[0].platinum + "<br/>";
    tooltipText += "Gold: " + loot.contents[0].gold + "<br/>";
    tooltipText += "Silver: " + loot.contents[0].silver + "<br/>";
    tooltipText += "Copper: " + loot.contents[0].copper;

    let lootHTML = "<div class='tooltip' style='position:absolute;top:" + top + "px;left:" + left + "px;'>" + "<div class='displayText' style='border-bottom:0;'>";
    lootHTML += "<div class='loot'></div>";
    lootHTML += "</div>" + "<div class='right'><div class='tooltipText'>" + tooltipText + "</div><i></i></div>" + "</div>";

    $map.append(lootHTML);
}

function drawTrap(trap, pos) {
    const $map = $("#map");
    const top = pos.y * 24;
    const left = pos.x * 24;

    let tooltipText = "Detection DC: " + trap.detectionDC + "<br/>";
    tooltipText += "Disarm DC: " + trap.disableDC + "<br/>";
    tooltipText += "Save DC: " + trap.saveDC + "<br/>";
    tooltipText += "Damage: " + trap.damage;

    let trapHTML = "<div class='tooltip' style='position:absolute;top:" + top + "px;left:" + left + "px;'>" + "<div class='displayText' style='border-bottom:0;'>";
    trapHTML += "<div class='trap'></div>";
    trapHTML += "</div>" + "<div class='right'><div class='tooltipText'>" + tooltipText + "</div><i></i></div>" + "</div>";

    $map.append(trapHTML);
}

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
    return allCells[row][col];
}

function getRowHTML(neighborsData) {
    let rowHTML = "<div class='dungeonRow'>";

    neighborsData.forEach(n => {
        const cssStyle = existingTileStyles[neighborsToCssClass(n)];
        rowHTML += "<div class='tile' style=\"" + cssStyle + "\"/>";
    });

    rowHTML += "</div>";
    return rowHTML;
}

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