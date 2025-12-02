package enums;

public enum FoodType {
  KRILL("Kr"),
  CRUSTACEAN("Cr"),
  ANCHOVY("An"),
  SQUID("Sq"),
  MACKEREL("Ma");

  private final String notation;

  FoodType(String notation) {
    this.notation = notation;
  }

  public String getNotation() {
    return notation;
  }
}
