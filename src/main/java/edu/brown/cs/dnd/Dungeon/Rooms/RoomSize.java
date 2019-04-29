package edu.brown.cs.dnd.Dungeon.Rooms;

public enum RoomSize {
  SMALL(.001), MEDIUM(.003), LARGE(.005);

  private final double roomRatio;

  RoomSize(double roomRatio) {
    this.roomRatio = roomRatio;
  }

  public double getRoomRatio() {
    return this.roomRatio;
  }
}
