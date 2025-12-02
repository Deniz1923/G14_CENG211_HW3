package enums;

public enum HazardType {
  LIGHT_ICE_BLOCK("LB"),
  HEAVY_ICE_BLOCK("HB"),
  SEA_LION("SL"),
  HOLE_IN_ICE("HI");

  private final String notation;

  HazardType(String notation) {
    this.notation = notation;
  }

  public String getNotation() {
    return notation;
  }
}
