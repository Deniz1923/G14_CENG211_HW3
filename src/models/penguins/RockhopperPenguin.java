package models.penguins;

import enums.PenguinType;

public class RockhopperPenguin extends Penguin {
  public RockhopperPenguin() {
    super(PenguinType.EMPEROR);
  }

  /**
   * Before they start sliding, they can prepare to jump over one hazard in their path. This ability
   * is not used automatically and can be wasted if there are no hazards in their path. Also, they
   * can only jump to an empty square. If the square they are about to land is not empty, they fail
   * to jump and collide with the hazard, nevertheless.
   */
  @Override
  void specialAbility() {}
}
