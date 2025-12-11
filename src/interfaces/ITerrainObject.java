package interfaces;

import models.Position;

public interface ITerrainObject {
  Position getPosition();

  void setPosition(Position position);

  /**
   * Return the Notation of the ITerrainObject.<br>
   * If it is a Penguin, returns penguinID according to the specification.
   */
  String getNotation();

}
