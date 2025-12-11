package enums;

/**
 * Represents the different types of hazards present on the IcyTerrain. Each hazard has a unique
 * interaction when a penguin collides with it.
 */
public enum HazardType {
    /**
     * Light Ice Block (LB): Starts moving when hit.
     * <p>
     * - If hit by a penguin: The penguin is stunned (skips next turn).
     * - Can slide and fall off the grid.
     * - Stops if it hits a SeaLion (transmitting movement).
     */
    LIGHT_ICE_BLOCK("LB"),

    /**
     * Heavy Ice Block (HB): Cannot be moved.
     * <p>
     * - Acts as a wall.
     * - Penalty: The colliding penguin loses their lightest food item.
     */
    HEAVY_ICE_BLOCK("HB"),

    /**
     * Sea Lion (SL): Elastic collision behavior.
     * <p>
     * - If hit: The penguin bounces back in the opposite direction.
     * - The Sea Lion starts sliding in the penguin's original direction.
     */
    SEA_LION("SL"),

    /**
     * Hole In Ice (HI): A trap that removes objects.
     * <p>
     * - Penguins falling in are removed from the game (food is still counted).
     * - Sliding objects (LB, SL) falling in will "Plug" the hole, making it safe.
     */
    HOLE_IN_ICE("HI");

    private final String notation;

    /**
     * Constructor for HazardType.
     *
     * @param notation The shorthand string representation for the menu (e.g., LB, SL).
     */
    HazardType(String notation) {
        this.notation = notation;
    }

    /**
     * Gets the string notation used for rendering the map.
     *
     * @return The 2-character notation string.
     */
    public String getNotation() {
        return notation;
    }
}