package models.penguins;

import enums.PenguinType;

public class EmperorPenguin extends Penguin {

  public EmperorPenguin() {
    super(PenguinType.EMPEROR);
  }

  /**
   * When sliding they can choose to stop at the third square they slide into. If the direction they
   * choose has less than three free squares, this ability is still considered used.
   */
  @Override
  void specialAbility() {}
}
