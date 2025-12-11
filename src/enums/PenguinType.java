package enums;

/**
 * Enumeration representing the four distinct species of penguins. Each species possesses a unique
 * ability that can be used once per game.
 */
public enum PenguinType {
    EMPEROR("Emperor"),
    KING("King"),
    ROCKHOPPER("Rockhopper"),
    ROYAL("Royal");

    private final String displayName;

    PenguinType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
