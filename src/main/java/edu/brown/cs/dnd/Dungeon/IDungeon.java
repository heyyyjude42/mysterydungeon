package edu.brown.cs.dnd.Dungeon;
import java.util.List;
/**
 * The required functionality for a Dungeon, which is nothing more than the
 * ability to get the rooms of the dungeon.
 */
public interface IDungeon {
    /**
     * Gets the rooms that make up this dungeon.
     * @return - the list of rooms.
     */
  List<Room> getRooms();

  int getArea();


}
