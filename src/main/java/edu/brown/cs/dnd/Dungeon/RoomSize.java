package edu.brown.cs.dnd.Dungeon;

public enum RoomSize {
  SMALL(.01), MEDIUM(.02), LARGE(.05);

  private final double roomRatio;

  RoomSize(double roomRatio) {
    this.roomRatio = roomRatio;
  }

  double getRoomRatio() {
    return this.roomRatio;
  }
}
