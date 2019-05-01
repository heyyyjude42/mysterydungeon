package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.QueryResult;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.REPL.*;
import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Dungeon.Dungeon;
import edu.brown.cs.dnd.Dungeon.Rooms.RoomSize;

import java.util.List;
import java.util.ArrayList;

/**
 * Class representing a handler to support generating a dungeon based on
 * user input.
 */
public class GenerateDungeonHandler implements Handler {

  private String ARGS_ERROR_MESSAGE = "ERROR: Invalid args";

  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("generate-dungeon", new GenerateDungeon());
  }


  /**
   * Class representing the generate-dungeon command.
   */
  private class GenerateDungeon implements Command {

    @Override
    public Result run(String[] args) throws InvalidInputException {
      List<QueryResult> result = new ArrayList<>();
      if (args.length != 4) {
        throw new InvalidInputException(ARGS_ERROR_MESSAGE);
      }

      String avgRoomSize; Dungeon d; int dungeonWidth, dungeonHeight = 0;

      // Sanitize args
      try {
        dungeonWidth = Integer.parseInt(args[1]);
        dungeonHeight = Integer.parseInt(args[2]);
        avgRoomSize = args[3];

        d = sanitizeDungeon(dungeonWidth, dungeonHeight, avgRoomSize);
      } catch (NumberFormatException | InvalidInputException e) {
        throw new InvalidInputException(ARGS_ERROR_MESSAGE);
      }

      result.add(d);
      return new Result(ReturnType.DUNGEON, result);
    }

    /**
     * Method checks for valid dungeon parameters and constructs a dungeon based
     * on them, throwing an error if they are not valid.
     * @param dungeonWidth    An int that is the specified dungeon width
     * @param dungeonHeight   An int that is the specified dungeon height
     * @param avgRoomSize   A String that is the specified average room size
     * @return
     * @throws InvalidInputException
     */
    private Dungeon sanitizeDungeon(int dungeonWidth,
                                    int dungeonHeight,
                                    String avgRoomSize)
            throws InvalidInputException {
      if (dungeonWidth < 0 || dungeonHeight < 0
              || (!avgRoomSize.equals("small")
              && !avgRoomSize.equals("medium")
              && !avgRoomSize.equals("large"))) {
        throw new InvalidInputException(ARGS_ERROR_MESSAGE);
      }

      RoomSize size;

      if (avgRoomSize.equals("small")) {
        size = RoomSize.SMALL;
      } else if (avgRoomSize.equals("medium")) {
        size = RoomSize.MEDIUM;
      } else {
        size = RoomSize.LARGE;
      }

      return new Dungeon(dungeonWidth, dungeonHeight, size);
    }
  }

}
