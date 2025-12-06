package models.penguins;

import enums.PenguinType;
import models.Position;

public class KingPenguin extends Penguin {

  public KingPenguin(Position position) {
    super(PenguinType.KING, position);
  }

  private int measureInventory() {
    return 0;
  }

  /**
   * When sliding they can choose to stop at the fifth square they slide into. If the direction they
   * choose has less than five free squares, this ability is still considered used.
   */
  @Override
  void specialAbility() {}
}
