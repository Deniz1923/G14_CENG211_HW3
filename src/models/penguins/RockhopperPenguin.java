package models.penguins;

import enums.PenguinType;
import models.Position;

public class RockhopperPenguin extends Penguin {
  public RockhopperPenguin(Position position) {
    super(PenguinType.ROCKHOPPER,position);
  }

  @Override
  void ability1() {}
}
