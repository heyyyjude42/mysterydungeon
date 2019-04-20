package edu.brown.cs.dnd.Dungeon;

public enum RoomSize {
  SMALL(.02), MEDIUM(.05), LARGE(.1);

  private final double roomRatio;

  RoomSize(double roomRatio) {
    this.roomRatio = roomRatio;
  }

  double getRoomRatio() {
    return this.roomRatio;
  }
}
