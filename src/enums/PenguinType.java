package enums;

/**
 * Enumeration representing the four distinct species of penguins. Each species possesses a unique
 * ability that can be used once per game.
 */
public enum PenguinType {
  /**
   * Emperor Penguin, Ability: When sliding, can choose to stop exactly at the 3rd square. If the
   * path has fewer than 3 squares, the ability is still consumed.
   */
  EMPEROR,

  /**
   * King Penguin, Ability: When sliding, can choose to stop exactly at the 5th square. If the path
   * has fewer than 5 squares, the ability is still consumed.
   */
  KING,

  /**
   * Rockhopper Penguin, Ability: Before sliding, can prepare to jump over one hazard in the path.
   * Must land on an empty square. If the landing spot is occupied, the jump fails. Note: Non-player
   * Rockhoppers auto-use this when facing a hazard.
   */
  ROCKHOPPER,

  /**
   * Royal Penguin, Ability: Before sliding, can choose to move 1 square to an adjacent cell (Up,
   * Down, Left, Right). Useful for safely repositioning or collecting adjacent food.
   */
  ROYAL
}
