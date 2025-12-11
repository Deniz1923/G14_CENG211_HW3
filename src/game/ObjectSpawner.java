package game;

import game.util.RandUtil;
import interfaces.ITerrainObject;
import java.util.ArrayList;
import java.util.List;
import models.Food;
import models.Position;
import models.penguins.EmperorPenguin;
import models.penguins.KingPenguin;
import models.penguins.Penguin;
import models.penguins.RockhopperPenguin;
import models.penguins.RoyalPenguin;

public class ObjectSpawner {
  private final int GRID_SIZE = 10;
  private final int PENGUIN_COUNT = 3;
  private final int HAZARD_COUNT = 15;
  private final int FOOD_COUNT = 20;

  public void spawnObjects(TerrainGrid grid) {
    spawnPenguins(grid);
    spawnHazards(grid);
    spawnFood(grid);
  }

  private void spawnPenguins(TerrainGrid grid) {
    int penguinsSpawned = 0;
    List<Penguin> penguinList = new ArrayList<>();

    while (penguinsSpawned < PENGUIN_COUNT) {
      Position position = getRandomEdgePosition();

      if (grid.getObjectAt(position) == null) {
        Penguin penguin = generateRandomPenguin(position);

        penguin.setPenguinID("P" + (penguinsSpawned + 1));

        grid.placeObject(position, penguin);
        penguinList.add(penguin);
        penguinsSpawned++;
      }
    }
  }

  private void spawnHazards(TerrainGrid grid) {
    int hazardsSpawned = 0;
    while (hazardsSpawned < HAZARD_COUNT) {
      Position position = getRandomPosition();
      if (grid.getObjectAt(position) == null) {
        ITerrainObject hazard = generateRandomHazard(position);
        grid.placeObject(position, hazard);
        hazardsSpawned++;
      }
    }
  }

  private void spawnFood(TerrainGrid grid) {
    int foodSpawned = 0;
    while (foodSpawned < FOOD_COUNT) {
      Position position = getRandomPosition();
      if (grid.getObjectAt(position) == null) {
        ITerrainObject food = generateRandomFood(position);
        grid.placeObject(position, food);
        foodSpawned++;
      }
    }
  }

  // IDE added yield, It is supposed to be identical
  private Position getRandomEdgePosition() {
    int side = RandUtil.getRandomInt(4);
    int x = 0;
    int y =
        switch (side) {
          // top edge
          case 0 -> {
            x = RandUtil.getRandomInt(GRID_SIZE);
            yield 0;
          }
          // bottom edge
          case 1 -> {
            x = RandUtil.getRandomInt(GRID_SIZE);
            yield GRID_SIZE - 1;
          }
          // left edge
          case 2 -> {
              yield RandUtil.getRandomInt(GRID_SIZE);
          }
          // right edge
          case 3 -> {
            x = GRID_SIZE - 1;
            yield RandUtil.getRandomInt(GRID_SIZE);
          }
          default -> 0;
        };
    return new Position(x, y);
  }

  private Position getRandomPosition() {
    return new Position(RandUtil.getRandomInt(GRID_SIZE), RandUtil.getRandomInt(GRID_SIZE));
  }

  private Penguin generateRandomPenguin(Position pos) {

    return switch (RandUtil.getRandomPenguin()) {
      case ROYAL -> new RoyalPenguin(pos);
      case EMPEROR -> new EmperorPenguin(pos);
      case KING -> new KingPenguin(pos);
      case ROCKHOPPER -> new RockhopperPenguin(pos);
    };
  }

  private ITerrainObject generateRandomHazard(Position position) {

    // Default is not needed as Rand.Util.getRandomHazard can only return a valid HazardType
    return switch (RandUtil.getRandomHazard()) {
      case LIGHT_ICE_BLOCK -> new models.hazards.LightIceBlock(position);
      case HEAVY_ICE_BLOCK -> new models.hazards.HeavyIceBlock(position);
      case SEA_LION -> new models.hazards.SeaLion(position);
      case HOLE_IN_ICE -> new models.hazards.HoleInIce(position);
    };
  }

  private Food generateRandomFood(Position position) {

    return new Food(RandUtil.getRandomFood(), position, RandUtil.getFoodWeight());
  }
}
