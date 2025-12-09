package models.penguins;

import enums.PenguinType;
import models.Position;

public class EmperorPenguin extends Penguin {

  public EmperorPenguin(Position position) {
    super(PenguinType.EMPEROR, position);
  }

  /**
   * When sliding they can choose to stop at the third square they slide into. If the direction they
   * choose has less than three free squares, this ability is still considered used.
   */
  @Override
  public void specialAbility() {}
}
