package models.penguins;

import enums.PenguinType;
import models.Position;

public class RoyalPenguin extends Penguin {

  public RoyalPenguin(Position position) {
    super(PenguinType.ROYAL, position);
  }

  /**
   * RoyalPenguin: Before they start sliding, they can choose to safely move into an adjacent square
   * (only horizontally and vertically). It is possible to accidentally step out of the grid and
   * fall into water while using this ability (or other similar accidents).
   */
  @Override
  void specialAbility() {}
}
