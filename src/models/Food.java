package models;

import enums.FoodType;
import interfaces.ITerrainObject;

/**
 * Represents a food item on the icy terrain that penguins can collect.
 * Each food item has a specific type (Krill, Crustacean, Anchovy, Squid, or Mackerel)
 * and a weight value between 1-5 units.
 *
 * <p>Food items implement ITerrainObject, allowing them to be placed on the grid.
 * When a penguin reaches a food item, the penguin collects it and adds it to
 * their inventory, contributing to their total carried weight.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class Food implements ITerrainObject {
    /** The type of food (Krill, Crustacean, Anchovy, Squid, or Mackerel) */
    private final FoodType type;

    /** The weight of this food item in units (1-5) */
    private final int weight;

    /** The current position of this food item on the grid */
    private Position position;

    /**
     * Constructs a Food item with specified type, position, and weight.
     *
     * @param type The type of food (cannot be null)
     * @param position The initial position on the grid (cannot be null)
     * @param weight The weight in units (must be between 1 and 5 inclusive)
     * @throws IllegalArgumentException if type is null
     * @throws IllegalArgumentException if position is null
     * @throws IllegalArgumentException if weight is not between 1 and 5
     */
    public Food(FoodType type, Position position, int weight) {
        if (type == null) {
            throw new IllegalArgumentException(
                    "Food Error: FoodType cannot be null."
            );
        }
        if (position == null) {
            throw new IllegalArgumentException(
                    "Food Error: Position cannot be null."
            );
        }
        if (weight < 1 || weight > 5) {
            throw new IllegalArgumentException(
                    "Food Error: Weight must be between 1 and 5 units. Received: " + weight
            );
        }

        this.type = type;
        this.weight = weight;
        this.position = position;
    }

    /**
     * Gets the type of this food item.
     *
     * @return The FoodType enum value
     */
    public FoodType getType() {
        return type;
    }

    /**
     * Gets the weight of this food item in units.
     *
     * @return The weight value (1-5)
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Gets the current position of this food item on the grid.
     *
     * @return The Position object representing the food's location
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this food item on the grid.
     *
     * @param position The new position (cannot be null)
     * @throws IllegalArgumentException if position is null
     */
    @Override
    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException(
                    "Food Error: Cannot set position to null."
            );
        }
        this.position = position;
    }

    /**
     * Returns the notation used to display this food item on the grid.
     * The notation is a 2-letter abbreviation of the food type:
     * <ul>
     *   <li>Kr - Krill</li>
     *   <li>Cr - Crustacean</li>
     *   <li>An - Anchovy</li>
     *   <li>Sq - Squid</li>
     *   <li>Ma - Mackerel</li>
     * </ul>
     *
     * @return The food type's notation string
     */
    @Override
    public String getNotation() {
        return type.getNotation();
    }

    /**
     * Returns the symbol used to represent this food item.
     * Currently identical to getNotation().
     *
     * @return The food type's symbol string
     */
    @Override
    public String getSymbol() {
        return type.getNotation();
    }

    /**
     * Returns a string representation of this food item.
     * Format: "FoodType (weight units)"
     *
     * @return A descriptive string of the food item
     */
    @Override
    public String toString() {
        return type + " (" + weight + " units)";
    }
}