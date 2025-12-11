package game;

import game.util.RandUtil;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;
import models.penguins.*;

/**
 * Handles spawning of all game objects (penguins, hazards, and food) on the grid.
 * This class ensures proper distribution and placement of objects according to
 * game rules.
 * <p>
 * Spawning rules:
 * - 3 penguins - must be on edge squares, no overlapping
 * - 15 hazards - randomly placed, cannot overlap with penguins
 * - 20 food items - randomly placed, cannot overlap with penguins or hazards
 * <p>
 * Each object type is spawned sequentially to ensure proper placement
 * without conflicts. The spawner continues attempting placements until
 * all required objects are successfully placed.
 *
 * @author CENG211 14. Group
 * @version 1.0
 * @since 2025-12-08
 */
public class ObjectSpawner {
    /**
     * Size of the grid (10x10)
     */
    private final int GRID_SIZE = 10;

    /**
     * Number of penguins to spawn
     */
    private final int PENGUIN_COUNT = 3;

    /**
     * Number of hazards to spawn
     */
    private final int HAZARD_COUNT = 15;

    /**
     * Number of food items to spawn
     */
    private final int FOOD_COUNT = 20;

    /**
     * Spawns all game objects on the provided grid.
     * Objects are spawned in this order:
     * 1. Penguins (on edges)
     * 2. Hazards (anywhere except penguin locations)
     * 3. Food (anywhere except penguin and hazard locations)
     *
     * @param grid The terrain grid to populate
     * @throws IllegalArgumentException if grid is null
     */
    public void spawnObjects(TerrainGrid grid) {
        if (grid == null) {
            throw new IllegalArgumentException(
                    "ObjectSpawner Error: Cannot spawn objects on null grid."
            );
        }

        try {
            spawnPenguins(grid);
            spawnHazards(grid);
            spawnFood(grid);
        } catch (Exception e) {
            System.err.println("Error during object spawning: " + e.getMessage());
            throw new RuntimeException("Failed to spawn objects", e);
        }
    }

    /**
     * Spawns three penguins on random edge positions.
     * Each penguin is assigned an ID (P1, P2, P3) and placed on an
     * unoccupied edge square of the grid.
     * <p>
     * Edge squares are those where x=0, x=9, y=0, or y=9.
     * <p>
     * Process:
     * 1. Generate random edge position
     * 2. Check if position is empty
     * 3. Create random penguin type
     * 4. Assign penguin ID (P1, P2, or P3)
     * 5. Place penguin on grid
     * 6. Repeat until all 3 penguins are placed
     *
     * @param grid The terrain grid
     * @throws RuntimeException if unable to spawn all penguins after many attempts
     */
    private void spawnPenguins(TerrainGrid grid) {
        int penguinsSpawned = 0;
        //List<Penguin> penguinList = new ArrayList<>();
        int attempts = 0;
        int maxAttempts = 1000; // Prevent infinite loops

        try {
            while (penguinsSpawned < PENGUIN_COUNT) {
                if (attempts++ > maxAttempts) {
                    throw new RuntimeException(
                            "Failed to spawn all penguins after " + maxAttempts + " attempts"
                    );
                }

                Position position = getRandomEdgePosition();

                // Check if position is empty
                if (grid.getObjectAt(position) == null) {
                    // Generate random penguin type
                    Penguin penguin = generateRandomPenguin(position);

                    // Assign penguin ID (P1, P2, P3)
                    penguin.setPenguinID("P" + (penguinsSpawned + 1));

                    // Place on grid
                    grid.placeObject(position, penguin);
                    //penguinList.add(penguin);
                    penguinsSpawned++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error spawning penguins: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Spawns 15 hazards on random grid positions.
     * Hazards cannot be placed on squares already occupied by penguins.
     * <p>
     * Each hazard is randomly assigned one of four types:
     * - LightIceBlock - can slide, stuns penguins
     * - HeavyIceBlock - immovable, removes food
     * - SeaLion - can slide, bounces penguins
     * - HoleInIce - immovable, eliminates penguins
     *
     * @param grid The terrain grid
     * @throws RuntimeException if unable to spawn all hazards
     */
    private void spawnHazards(TerrainGrid grid) {
        int hazardsSpawned = 0;
        int attempts = 0;
        int maxAttempts = 1000;

        try {
            while (hazardsSpawned < HAZARD_COUNT) {
                if (attempts++ > maxAttempts) {
                    throw new RuntimeException(
                            "Failed to spawn all hazards after " + maxAttempts + " attempts"
                    );
                }

                Position position = getRandomPosition();

                // Check if position is empty
                if (grid.getObjectAt(position) == null) {
                    ITerrainObject hazard = generateRandomHazard(position);
                    grid.placeObject(position, hazard);
                    hazardsSpawned++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error spawning hazards: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Spawns 20 food items on random grid positions.
     * Food items cannot be placed on squares occupied by penguins or hazards.
     * <p>
     * Each food item is randomly assigned:
     * - Food type (Krill, Crustacean, Anchovy, Squid, Mackerel)
     * - Weight (1-5 units, randomly determined)
     *
     * @param grid The terrain grid
     * @throws RuntimeException if unable to spawn all food items
     */
    private void spawnFood(TerrainGrid grid) {
        int foodSpawned = 0;
        int attempts = 0;
        int maxAttempts = 1000;

        try {
            while (foodSpawned < FOOD_COUNT) {
                if (attempts++ > maxAttempts) {
                    throw new RuntimeException(
                            "Failed to spawn all food after " + maxAttempts + " attempts"
                    );
                }

                Position position = getRandomPosition();

                // Check if position is empty
                if (grid.getObjectAt(position) == null) {
                    ITerrainObject food = generateRandomFood(position);
                    grid.placeObject(position, food);
                    foodSpawned++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error spawning food: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Generates a random edge position on the grid.
     * Edge positions are those where at least one coordinate is 0 or 9.
     * <p>
     * Four possible edges:
     * - Top edge: y=0, x=0-9
     * - Bottom edge: y=9, x=0-9
     * - Left edge: x=0, y=0-9
     * - Right edge: x=9, y=0-9
     *
     * @return A random position on the grid edge
     */
    private Position getRandomEdgePosition() {
        try {
            int side = RandUtil.getRandomInt(4);
            int x = 0;
            int y = switch (side) {
                case 0 -> { // Top edge
                    x = RandUtil.getRandomInt(GRID_SIZE);
                    yield 0;
                }
                case 1 -> { // Bottom edge
                    x = RandUtil.getRandomInt(GRID_SIZE);
                    yield GRID_SIZE - 1;
                }
                case 2 -> { // Left edge
                    x = 0;
                    yield RandUtil.getRandomInt(GRID_SIZE);
                }
                case 3 -> { // Right edge
                    x = GRID_SIZE - 1;
                    yield RandUtil.getRandomInt(GRID_SIZE);
                }
                default -> 0;
            };
            return new Position(x, y);
        } catch (Exception e) {
            System.err.println("Error generating edge position: " + e.getMessage());
            return new Position(0, 0); // Fallback to origin
        }
    }

    /**
     * Generates a random position anywhere on the grid.
     *
     * @return A random position with coordinates between (0,0) and (9,9)
     */
    private Position getRandomPosition() {
        return new Position(
                RandUtil.getRandomInt(GRID_SIZE),
                RandUtil.getRandomInt(GRID_SIZE)
        );
    }

    /**
     * Creates a random penguin of one of the four types.
     * Each type has equal probability (25% each).
     *
     * @param pos The position for the new penguin
     * @return A new Penguin instance of random type
     * @throws IllegalArgumentException if position is null
     */
    private Penguin generateRandomPenguin(Position pos) {
        if (pos == null) {
            throw new IllegalArgumentException(
                    "ObjectSpawner Error: Cannot create penguin with null position."
            );
        }

        try {
            return switch (RandUtil.getRandomPenguin()) {
                case ROYAL -> new RoyalPenguin(pos);
                case EMPEROR -> new EmperorPenguin(pos);
                case KING -> new KingPenguin(pos);
                case ROCKHOPPER -> new RockhopperPenguin(pos);
            };
        } catch (Exception e) {
            System.err.println("Error generating penguin: " + e.getMessage());
            return new EmperorPenguin(pos); // Fallback to Emperor
        }
    }

    /**
     * Creates a random hazard of one of the four types.
     * Each type has equal probability (25% each).
     *
     * @param position The position for the new hazard
     * @return A new ITerrainObject representing the hazard
     * @throws IllegalArgumentException if position is null
     */
    private ITerrainObject generateRandomHazard(Position position) {
        if (position == null) {
            throw new IllegalArgumentException(
                    "ObjectSpawner Error: Cannot create hazard with null position."
            );
        }

        try {
            return switch (RandUtil.getRandomHazard()) {
                case LIGHT_ICE_BLOCK -> new models.hazards.LightIceBlock(position);
                case HEAVY_ICE_BLOCK -> new models.hazards.HeavyIceBlock(position);
                case SEA_LION -> new models.hazards.SeaLion(position);
                case HOLE_IN_ICE -> new models.hazards.HoleInIce(position);
            };
        } catch (Exception e) {
            System.err.println("Error generating hazard: " + e.getMessage());
            return new models.hazards.HeavyIceBlock(position); // Fallback
        }
    }

    /**
     * Creates a random food item with random type and weight.
     * Food type is randomly selected from the five available types.
     * Weight is randomly assigned between 1-5 units.
     *
     * @param position The position for the new food item
     * @return A new Food instance
     * @throws IllegalArgumentException if position is null
     */
    private Food generateRandomFood(Position position) {
        if (position == null) {
            throw new IllegalArgumentException(
                    "ObjectSpawner Error: Cannot create food with null position."
            );
        }

        try {
            return new Food(
                    RandUtil.getRandomFood(),
                    position,
                    RandUtil.getFoodWeight()
            );
        } catch (Exception e) {
            System.err.println("Error generating food: " + e.getMessage());
            throw e;
        }
    }
}