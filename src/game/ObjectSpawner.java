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

/**
 * Responsible for generating and placing initial game objects onto the terrain grid.
 * This includes penguins, hazards, and food items according to the game rules.
 *
 * This class ensures that objects are spawned in valid locations and do not
 * overlap where prohibited.
 */
public class ObjectSpawner {
  private final int GRID_SIZE = TerrainGrid.GRID_SIZE;
  private final int PENGUIN_COUNT = 3;
  private final int HAZARD_COUNT = 15;
  private final int FOOD_COUNT = 20;

  /**
   * Orchestrates the spawning of all game entities.
   * Calls specific methods to place penguins, hazards, and food on the provided grid.
   *
   * @param grid The TerrainGrid where objects will be placed.
   */
  public void spawnObjects(TerrainGrid grid) {
    spawnPenguins(grid);
    spawnHazards(grid);
    spawnFood(grid);
  }

  /**
   * Generates and places 3 penguins on random edge coordinates of the grid.
   * It ensures that no two penguins occupy the same square.
   * It also assigns IDs P1, P2, and P3 to the penguins.
   *
   * This method includes a safety mechanism to prevent infinite loops if valid
   * positions cannot be found within a reasonable number of attempts.
   *
   * @param grid The grid to place penguins on.
   * @throws RuntimeException If penguins cannot be spawned after 1000 attempts.
   */
  private void spawnPenguins(TerrainGrid grid) {
    int penguinsSpawned = 0;
    List<Penguin> penguinList = new ArrayList<>();
    int attempts = 0;

    while (penguinsSpawned < PENGUIN_COUNT && attempts < 1000) {
      attempts++;
      Position position = getRandomEdgePosition();

      if (grid.getObjectAt(position) == null) {
        Penguin penguin = generateRandomPenguin(position);

        penguin.setPenguinID("P" + (penguinsSpawned + 1));

        grid.placeObject(position, penguin);
        penguinList.add(penguin);
        penguinsSpawned++;
      }
    }
    if (attempts >= 1000) throw new RuntimeException("Failed to spawn penguins.");
  }

  /**
   * Generates and places 15 hazards randomly across the grid.
   * Ensures hazards do not overlap with existing objects.
   *
   * @param grid The grid to place hazards on.
   */
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

  /**
   * Generates and places 20 food items randomly across the grid.
   * Ensures food items do not overlap with existing objects.
   *
   * @param grid The grid to place food on.
   */
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

  /**
   * Calculates a random position situated on the perimeter of the grid.
   * Used specifically for penguin placement rules.
   *
   * @return A Position object located on one of the four edges.
   */
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

  /**
   * Generates a random coordinate pair within the grid boundaries.
   *
   * @return A random Position object.
   */
  private Position getRandomPosition() {
    return new Position(RandUtil.getRandomInt(GRID_SIZE), RandUtil.getRandomInt(GRID_SIZE));
  }

  /**
   * Factory method that creates a specific type of penguin based on random selection.
   *
   * @param pos The position to assign to the new penguin.
   * @return A new instance of a Royal, Emperor, King, or Rockhopper penguin.
   */
  private Penguin generateRandomPenguin(Position pos) {

    return switch (RandUtil.getRandomPenguin()) {
      case ROYAL -> new RoyalPenguin(pos);
      case EMPEROR -> new EmperorPenguin(pos);
      case KING -> new KingPenguin(pos);
      case ROCKHOPPER -> new RockhopperPenguin(pos);
    };
  }

  /**
   * Factory method that creates a specific type of hazard based on random selection.
   *
   * @param position The position to assign to the new hazard.
   * @return A new instance of LightIceBlock, HeavyIceBlock, SeaLion, or HoleInIce.
   */
  private ITerrainObject generateRandomHazard(Position position) {

    // Default is not needed as Rand.Util.getRandomHazard can only return a valid HazardType
    return switch (RandUtil.getRandomHazard()) {
      case LIGHT_ICE_BLOCK -> new models.hazards.LightIceBlock(position);
      case HEAVY_ICE_BLOCK -> new models.hazards.HeavyIceBlock(position);
      case SEA_LION -> new models.hazards.SeaLion(position);
      case HOLE_IN_ICE -> new models.hazards.HoleInIce(position);
    };
  }

  /**
   * Factory method that creates a food object with a random type and weight.
   *
   * @param position The position to assign to the new food.
   * @return A new Food instance.
   */
  private Food generateRandomFood(Position position) {

    return new Food(RandUtil.getRandomFood(), position, RandUtil.getFoodWeight());
  }
}