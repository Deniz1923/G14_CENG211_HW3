package enums;

/**
 * Represents the different types of food available on the terrain. Each food item has a type and a
 * random weight (1-5 units) assigned at generation.
 */
public enum FoodType {
    KRILL("Kr"),
    CRUSTACEAN("Cr"),
    ANCHOVY("An"),
    SQUID("Sq"),
    MACKEREL("Ma");

    private final String notation;

    /**
     * Constructor for FoodType.
     *
     * @param notation The 2-letter abbreviation for the menu (e.g., "Kr", "Sq").
     */
    FoodType(String notation) {
        this.notation = notation;
    }

    /**
     * Gets the notation for the console display.
     *
     * @return The notation string.
     */
    public String getNotation() {
        return notation;
    }
}
