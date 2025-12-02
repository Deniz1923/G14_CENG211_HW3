package models.penguins;

import enums.PenguinType;

public class EmperorPenguin extends Penguin {
  int carriedWeight = 0;
  private String type;

  public EmperorPenguin() {
    super(PenguinType.EMPEROR);
  }

  int measureInventory() {

    return 0;
  }

  @Override
  void ability1() {}
}
