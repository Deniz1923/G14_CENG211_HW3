package models.penguins;

import enums.PenguinType;

public class RoyalPenguin extends Penguin {
  public RoyalPenguin() {
    super(PenguinType.EMPEROR);
  }

  @Override
  void ability1() {}
}
