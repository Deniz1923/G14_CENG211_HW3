package game;

import enums.FoodType;
import enums.PenguinType;
import interfaces.ITerrainObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.Food;
import models.Position;
import models.hazards.Hazard;
import models.penguins.EmperorPenguin;
import models.penguins.KingPenguin;
import models.penguins.Penguin;
import models.penguins.RockhopperPenguin;
import models.penguins.RoyalPenguin;

public class ObjectSpawner {
  private final Random random = new Random();
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

  private Position getRandomEdgePosition() {
    int side = random.nextInt(4);
    int x = 0;
    int y = 0;
    switch (side) {
      // top edge
      case 0:
        x = random.nextInt(GRID_SIZE);
        y = 0;
        break;
      // bottom edge
      case 1:
        x = random.nextInt(GRID_SIZE);
        y = GRID_SIZE - 1; // 9 in this case
        break;
      // left edge
      case 2:
        x = 0;
        y = random.nextInt(GRID_SIZE);
        break;
      // right edge
      case 3:
        x = GRID_SIZE - 1;
        y = random.nextInt(GRID_SIZE);
        break;
    }
    return new Position(x, y);
  }

  private Position getRandomPosition() {
    return new Position(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
  }

  private Penguin generateRandomPenguin(Position pos) {
    // enum .values()
    PenguinType[] types = PenguinType.values();
    PenguinType selected = types[random.nextInt(types.length)];

    return switch (selected) {
      case ROYAL -> new RoyalPenguin(pos);
      // Assuming other constructors exist similarly
      case EMPEROR -> new EmperorPenguin(pos);
      case KING -> new KingPenguin(pos);
      case ROCKHOPPER -> new RockhopperPenguin(pos);
      default -> new RoyalPenguin(pos);
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
    FoodType[] types = FoodType.values();
    FoodType selectedType = types[random.nextInt(types.length)];

    return new Food(selectedType, position, RandUtil.getFoodWeight());
  }
}
