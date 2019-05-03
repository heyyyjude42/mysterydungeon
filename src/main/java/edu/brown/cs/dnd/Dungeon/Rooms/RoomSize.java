package edu.brown.cs.dnd.Dungeon.Rooms;

/**
 * Enum representing room size options.
 */
public enum RoomSize {
  SMALL(.001), MEDIUM(.003), LARGE(.005);

  private final double roomRatio;

  RoomSize(double roomRatio) {
    this.roomRatio = roomRatio;
  }

  /**
   * Method gets the room ratio.
   * @return    A double that is the room ratio
   */
  public double getRoomRatio() {
    return this.roomRatio;
  }
}
