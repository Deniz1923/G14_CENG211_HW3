package models.penguins;

import enums.PenguinType;
import models.Position;

public class EmperorPenguin extends Penguin {

  public EmperorPenguin(Position position) {
    super(PenguinType.EMPEROR, position);
  }

  int measureInventory() {

    return 0;
  }

  @Override
  void ability1() {}
}
