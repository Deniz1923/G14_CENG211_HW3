package models.penguins;

import enums.PenguinType;
import models.Position;

public class RoyalPenguin extends Penguin {

  public RoyalPenguin(Position position) {
    super(PenguinType.ROYAL,position);
  }

  @Override
  void ability1() {}
}
