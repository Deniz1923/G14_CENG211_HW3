package models.penguins;

import enums.PenguinType;

public class KingPenguin extends Penguin {

  public KingPenguin() {
    super(PenguinType.EMPEROR);
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
