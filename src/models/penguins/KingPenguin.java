package models.penguins;

import enums.PenguinType;
import models.Position;

public class KingPenguin extends Penguin {
  int carriedWeight = 0;

  public KingPenguin(Position position) {
    super(PenguinType.KING,position);
  }

  private int measureInventory() {
    return 0;
  }

  @Override
  void ability1() {}
}
