package models.penguins;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.IHazard;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;
import models.hazards.HoleInIce;
import models.hazards.SeaLion;

import java.util.ArrayList;
import java.util.List;

import static game.TerrainGrid.GRID_SIZE;

/**
 * Abstract base class for all penguin types in the Sliding Penguins game.
 * Penguins are the main characters that slide across the icy terrain,
 * collect food, and compete to have the highest total food weight.
 *
 * <p>There are four types of penguins, each with unique special abilities:</p>
 * <ul>
 *   <li>KingPenguin - Can stop at the 5th square while sliding</li>
 *   <li>EmperorPenguin - Can stop at the 3rd square while sliding</li>
 *   <li>RoyalPenguin - Can safely move one square before sliding</li>
 *   <li>RockhopperPenguin - Can jump over one hazard in their path</li>
 * </ul>
 *
 * <p><b>Game mechanics:</b></p>
 * <ul>
 *   <li>Penguins slide continuously until hitting an object or edge</li>
 *   <li>Food is automatically collected when reached (penguin stops)</li>
 *   <li>Colliding with another penguin transfers movement</li>
 *   <li>Hazards have various effects (stun, remove food, eliminate, bounce)</li>
 *   <li>Falling into water eliminates penguin but food counts in scoring</li>
 * </ul>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public abstract class Penguin implements ITerrainObject {
    /**
     * List of food items collected by this penguin
     */
    private final ArrayList<Food> inventory;

    /**
     * The type of this penguin (King, Emperor, Royal, or Rockhopper)
     */
    private final PenguinType type;

    /**
     * Current position on the grid (null if eliminated)
     */
    private Position position;

    /**
     * Total weight of food currently carried
     */
    private int carriedWeight = 0;

    /**
     * Whether this penguin is stunned and will skip their next turn
     */
    private boolean stunned = false;

    /**
     * Unique identifier (P1, P2, or P3)
     */
    private String penguinID = "??";

    /**
     * Whether this penguin is controlled by the human player
     */
    private boolean isPlayer = false;

    /**
     * Whether this penguin has used their special ability
     */
    private boolean abilityUsed = false;

    /**
     * Constructs a Penguin with specified type and position.
     *
     * @param penguinType The type of penguin (King, Emperor, Royal, Rockhopper)
     * @param position    The initial position on the grid (must be on edge)
     * @throws IllegalArgumentException if penguinType is null
     * @throws IllegalArgumentException if position is null
     */
    public Penguin(PenguinType penguinType, Position position) {
        if (penguinType == null) {
            throw new IllegalArgumentException(
                    "Penguin Error: Type cannot be null."
            );
        }
        if (position == null) {
            throw new IllegalArgumentException(
                    "Penguin Error: Initial Position cannot be null."
            );
        }

        this.type = penguinType;
        this.position = position;
        this.inventory = new ArrayList<>();
    }

    /**
     * Constructs a Penguin with specified type at default position (0,0).
     *
     * @param penguinType The type of penguin
     * @throws IllegalArgumentException if penguinType is null
     */
    public Penguin(PenguinType penguinType) {
        this(penguinType, new Position());
    }

    /**
     * Sets the unique identifier for this penguin (P1, P2, or P3).
     *
     * @param id The penguin's identifier
     * @throws IllegalArgumentException if id is null or empty
     */
    public void setPenguinID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Penguin Error: ID cannot be null or empty."
            );
        }
        this.penguinID = id;
    }

    /**
     * Removes the lightest food item from this penguin's inventory.
     * This occurs when colliding with a HeavyIceBlock.
     * If inventory is empty, no action is taken.
     */
    public void removeLightestFood() {
        try {
            if (!inventory.isEmpty()) {
                int min = Integer.MAX_VALUE;
                int minIndex = 0;

                // Find the lightest food item
                for (int i = 0; i < inventory.size(); i++) {
                    if (inventory.get(i).getWeight() < min) {
                        min = inventory.get(i).getWeight();
                        minIndex = i;
                    }
                }

                // Remove and announce the loss
                Food removed = inventory.remove(minIndex);
                System.out.println(getNotation() + " loses " +
                        removed.getNotation() + " (" + removed.getWeight() +
                        " units) due to collision!");
            }
        } catch (Exception e) {
            System.err.println("Error removing lightest food: " + e.getMessage());
        }
    }

    /**
     * Calculates and returns the total weight of all food in inventory.
     * Updates the carriedWeight field with the current total.
     *
     * @return The total weight of all collected food items
     */
    public int measureInventory() {
        int sum = 0;
        try {
            for (Food food : inventory) {
                sum += food.getWeight();
            }
            carriedWeight = sum;
        } catch (Exception e) {
            System.err.println("Error measuring inventory: " + e.getMessage());
        }
        return sum;
    }

    /**
     * Activates this penguin's special ability.
     * Each penguin type has a unique ability that can be used once per game.
     * Subclasses must implement their specific ability behavior.
     */
    public abstract void specialAbility();

    /**
     * Collects a food item and adds it to this penguin's inventory.
     * Prints a message announcing the collection.
     *
     * @param food The food item to collect
     */
    public void pickupFood(Food food) {
        if (food != null) {
            try {
                inventory.add(food);
                System.out.println(getNotation() + " takes the " +
                        food.getType().toString() + " on the ground. (Weight=" +
                        food.getWeight() + " units)");
            } catch (Exception e) {
                System.err.println("Error picking up food: " + e.getMessage());
            }
        }
    }

    /**
     * Slides this penguin in the specified direction.
     * The penguin continues sliding until it hits an obstacle, food, or edge.
     *
     * <p>Sliding behavior:</p>
     * <ul>
     *   <li>Empty squares - continue sliding</li>
     *   <li>Food - collect and stop</li>
     *   <li>Another penguin - stop and transfer movement to them</li>
     *   <li>SeaLion - bounce back in opposite direction</li>
     *   <li>Other hazards - collision effects apply</li>
     *   <li>Grid edge - fall into water (eliminated)</li>
     * </ul>
     *
     * @param grid      The terrain grid
     * @param direction The direction to slide (UP, DOWN, LEFT, RIGHT)
     * @throws IllegalArgumentException if grid or direction is null
     */
    public void slide(TerrainGrid grid, Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException(
                    "Penguin Error: Direction cannot be null."
            );
        }
        if (grid == null) {
            throw new IllegalArgumentException(
                    "Penguin Error: TerrainGrid cannot be null."
            );
        }

        try {
            System.out.println(getNotation() + " starts sliding " +
                    getDirectionName(direction) + "!");

            boolean isMoving = true;

            while (isMoving) {
                int nextY = position.getY();
                int nextX = position.getX();

                // Calculate next position based on direction
                switch (direction) {
                    case UP:
                        nextY--;
                        break;
                    case DOWN:
                        nextY++;
                        break;
                    case LEFT:
                        nextX--;
                        break;
                    case RIGHT:
                        nextX++;
                        break;
                }

                Position nextPos = new Position(nextX, nextY);

                // Check if falling into water
                if (nextX < 0 || nextY < 0 || nextY >= GRID_SIZE || nextX >= GRID_SIZE) {
                    System.out.println(getNotation() + " falls into the water!");
                    grid.removeObject(position);
                    this.position = null;
                    isMoving = false;
                    break;
                }

                ITerrainObject obstacle = grid.getObjectAt(nextPos);

                switch (obstacle) {
                    case null ->
                        // Empty square, continue sliding
                            updatePositionOnGrid(grid, nextPos);
                    case Food food -> {
                        // Food item found - collect and stop
                        grid.removeObject(nextPos);
                        updatePositionOnGrid(grid, nextPos);
                        pickupFood(food);
                        isMoving = false;
                    }
                    case Penguin otherPenguin -> {
                        // Collision with another penguin - transfer movement
                        System.out.println(getNotation() + " collides with " +
                                otherPenguin.getNotation() + "!");
                        System.out.println(otherPenguin.getNotation() +
                                " starts sliding instead!");
                        isMoving = false;
                        otherPenguin.slide(grid, direction);
                    }
                    case SeaLion seaLion -> {
                        // Special case for SeaLion - penguin bounces back
                        System.out.println(getNotation() + " collides with " +
                                seaLion.getNotation() + " and bounces back!");

                        // Remove SeaLion from current position
                        grid.removeObject(seaLion.getPosition());

                        // SeaLion starts sliding in original direction
                        slideHazard(grid, seaLion, direction);

                        // Penguin bounces back (opposite direction)
                        Direction oppositeDir = direction.opposite();
                        System.out.println(getNotation() + " bounces " +
                                getDirectionName(oppositeDir) + "!");
                        isMoving = false;

                        // Penguin slides in opposite direction
                        slide(grid, oppositeDir);
                    }
                    case IHazard hazard -> {
                        // Collision with other hazards
                        System.out.println(getNotation() + " collides with " +
                                hazard.getNotation() + "!");

                        hazard.onCollision(this, grid);

                        // Check if penguin is still on grid after collision
                        if (this.position == null) {
                            return;
                        }

                        // If hazard can slide, make it slide
                        if (hazard.canSlide()) {
                            grid.removeObject(hazard.getPosition());
                            slideHazard(grid, hazard, direction);
                        }

                        isMoving = false;
                    }
                    default -> {
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during penguin slide: " + e.getMessage());
        }
    }

    /**
     * Slides a hazard in the specified direction after collision.
     * Hazards continue sliding until hitting an obstacle or falling off the grid.
     *
     * @param grid      The terrain grid
     * @param hazard    The hazard to slide
     * @param direction The direction to slide
     */
    private void slideHazard(TerrainGrid grid, IHazard hazard, Direction direction) {
        try {
            Position currentPos = hazard.getPosition();
            boolean hazardMoving = true;

            while (hazardMoving) {
                int nextY = currentPos.getY();
                int nextX = currentPos.getX();

                switch (direction) {
                    case UP:
                        nextY--;
                        break;
                    case DOWN:
                        nextY++;
                        break;
                    case LEFT:
                        nextX--;
                        break;
                    case RIGHT:
                        nextX++;
                        break;
                }

                Position nextPos = new Position(nextX, nextY);

                // Check if hazard falls off grid
                if (nextX < 0 || nextY < 0 || nextY >= GRID_SIZE || nextX >= GRID_SIZE) {
                    System.out.println(hazard.getNotation() + " falls into the water!");
                    hazardMoving = false;
                    break;
                }

                ITerrainObject obstacle = grid.getObjectAt(nextPos);

                switch (obstacle) {
                    case null -> {
                        // Empty square
                        hazard.setPosition(nextPos);
                        grid.placeObject(nextPos, (ITerrainObject) hazard);
                        currentPos = nextPos;
                    }
                    case Food food -> {
                        // Remove food and continue
                        System.out.println(hazard.getNotation() + " destroys " +
                                obstacle.getNotation() + "!");
                        grid.removeObject(nextPos);
                        hazard.setPosition(nextPos);
                        grid.placeObject(nextPos, (ITerrainObject) hazard);
                        currentPos = nextPos;
                    }
                    case Penguin penguin -> {
                        // Stop when hitting another penguin
                        hazard.setPosition(currentPos);
                        grid.placeObject(currentPos, (ITerrainObject) hazard);
                        System.out.println(hazard.getNotation() + " collides with " +
                                obstacle.getNotation() + " and stops!");
                        hazardMoving = false;
                    }
                    case HoleInIce hole -> {
                        // Hazard plugs the hole
                        System.out.println(hazard.getNotation() + " falls into " +
                                hole.getNotation() + " and plugs it!");
                        hole.plug();
                        hazardMoving = false;
                    }
                    default -> {
                        // Hit another hazard
                        hazard.setPosition(currentPos);
                        grid.placeObject(currentPos, (ITerrainObject) hazard);
                        hazardMoving = false;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error sliding hazard: " + e.getMessage());
        }
    }

    /**
     * Converts a direction enum to a readable string.
     *
     * @param dir The direction
     * @return A formatted direction name
     */
    private String getDirectionName(Direction dir) {
        return switch (dir) {
            case UP -> "UPWARDS";
            case DOWN -> "DOWNWARDS";
            case LEFT -> "to the LEFT";
            case RIGHT -> "to the RIGHT";
        };
    }

    // Getters and setters with documentation

    /**
     * Gets the type name of this penguin in lowercase.
     *
     * @return The penguin type as a string ("king", "emperor", "royal", "rockhopper")
     */
    public String getType() {
        return type.getDisplayName();
    }

    /**
     * Gets the total weight of food currently carried.
     * Automatically recalculates from inventory.
     *
     * @return The total carried weight in units
     */
    public int getCarriedWeight() {
        measureInventory();
        return carriedWeight;
    }

    /**
     * Checks if this penguin is currently stunned.
     *
     * @return true if stunned (will skip next turn), false otherwise
     */
    public boolean isStunned() {
        return stunned;
    }

    /**
     * Sets the stunned status of this penguin.
     *
     * @param b true to stun the penguin, false to un-stun
     */
    public void setStunned(boolean b) {
        stunned = b;
    }

    /**
     * Checks if this penguin is controlled by the human player.
     *
     * @return true if this is the player's penguin, false if AI-controlled
     */
    public boolean isPlayer() {
        return isPlayer;
    }

    /**
     * Sets whether this penguin is controlled by the player.
     *
     * @param isPlayer true for player-controlled, false for AI
     */
    public void setPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    /**
     * Checks if this penguin has used their special ability.
     *
     * @return true if ability has been used, false otherwise
     */
    public boolean isAbilityUsed() {
        return abilityUsed;
    }

    /**
     * Sets whether the special ability has been used.
     *
     * @param used true if ability is used, false to reset
     */
    public void setAbilityUsed(boolean used) {
        this.abilityUsed = used;
    }

    /**
     * Gets the list of food items in this penguin's inventory.
     *
     * @return An unmodifiable view of the inventory
     */
    public List<Food> getInventory() {
        return inventory;
    }

    /**
     * Gets the current position of this penguin.
     * Returns null if the penguin has been eliminated.
     *
     * @return A copy of the current position, or null if eliminated
     */
    @Override
    public Position getPosition() {
        if (position == null) {
            return null;
        }
        return position.getPosition();
    }

    /**
     * Sets the position of this penguin.
     * Set to null when penguin is eliminated from the game.
     *
     * @param position The new position, or null for elimination
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Updates this penguin's position on the grid by removing from old
     * position and placing at new position.
     *
     * @param grid        The terrain grid
     * @param newPosition The new position to move to
     */
    private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
        grid.removeObject(position);
        this.position = newPosition;
        grid.placeObject(newPosition, this);
    }

    /**
     * Returns the notation (identifier) for this penguin.
     *
     * @return The penguin ID (P1, P2, or P3)
     */
    @Override
    public String getNotation() {
        return penguinID;
    }

    /**
     * Returns a string representation of this penguin.
     *
     * @return A descriptive string including type, ID, and position
     */
    @Override
    public String toString() {
        String posStr = (position != null) ? position.displayPosition() : "Eliminated";
        return type.toString() + " Penguin (" + penguinID + ") at " + posStr;
    }
}