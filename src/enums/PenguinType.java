package enums;

public enum PenguinType {
  EMPEROR("EM"),
  KING("KI"),
  ROCKHOPPER("RH"),
  ROYAL("RO");
  //notations won't be used since penguins are displayed as P1,P2,P3
  private final String notation;

  PenguinType(String notation) {
    this.notation = notation;
  }

  public String getNotation() {
    return notation;
  }
}
