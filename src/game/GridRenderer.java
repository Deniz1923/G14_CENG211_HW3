package game;

import interfaces.ITerrainObject;
import models.Position;

public class GridRenderer {
  private static final int CELL_WIDTH = 4; // Width of content inside the cell

  /**
   * Renders the current state of the TerrainGrid to the console.
   *
   * @param grid The game grid to render.
   */
  public void renderState(TerrainGrid grid) {
    if (grid == null) {
      throw new IllegalArgumentException("Renderer Error: Cannot render a null TerrainGrid.");
    }

    StringBuilder sb = new StringBuilder();
    String horizontalBorder = buildHorizontalBorder();

    // Top Border
    sb.append(horizontalBorder).append("\n");

    // Loop through Y (Rows)
    for (int y = 0; y < 10; y++) {
      sb.append("|"); // Start of row

      // Loop through X (Cols)
      for (int x = 0; x < 10; x++) {
        ITerrainObject obj = grid.getObjectAt(new Position(x, y));
        String symbol = getDisplaySymbol(obj);

        // Center and append symbol
        sb.append(centerString(symbol)).append("|");
      }
      sb.append("\n"); // End of row content
      sb.append(horizontalBorder).append("\n"); // Bottom border for this row
    }

    // Print everything at once
    System.out.print(sb);
  }

  /** Generates the horizontal border string "+----+----+" */
  private String buildHorizontalBorder() {
    // Must match CELL_WIDTH (4 dashes)
    return "+" + "----+".repeat(10);
  }

  /** Securely extracts the symbol. */
  private String getDisplaySymbol(ITerrainObject obj) {
    if (obj == null) {
      return ""; // Return empty string to be padded by centerString
    }
    String symbol = obj.getNotation();
    return (symbol != null) ? symbol : "??";
  }

  /** Centers a string within the defined CELL_WIDTH. */
  private String centerString(String s) {
    if (s == null) s = "";

    int width = CELL_WIDTH;

    // If the string is too long, trim it (Security against layout breaking)
    if (s.length() > width) {
      return s.substring(0, width);
    }

    int padSize = width - s.length();
    int padStart = s.length() + padSize / 2;

    // Pad Start
    s = String.format("%" + padStart + "s", s);
    // Pad End (to ensure full cell width)
    s = String.format("%-" + width + "s", s);

    return s;
  }
}
