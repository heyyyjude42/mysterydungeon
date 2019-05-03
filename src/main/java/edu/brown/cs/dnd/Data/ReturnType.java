package edu.brown.cs.dnd.Data;

/**
 * Enum representing the different command return types.
 */
public enum ReturnType {
  SPELL,
  MONSTER,
  FEAT,
  DUNGEON,
  NPC,
  ENCOUNTER,
  STRING, // for really simple things like errors and roll amounts
  NONE // empty
}
