package interfaces;

import models.Position;

public interface ITerrainObject {
  Position getPosition();

  void setPosition(Position position);

  String getNotation();
}
