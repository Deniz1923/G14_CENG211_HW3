package game.util;

import game.TerrainGrid;
import interfaces.ITerrainObject;
import models.Position;

/**
 * Renders the terrain grid to the console in a formatted ASCII table.
 * This class creates a visual representation of the game state with borders,
 * centered content, and proper spacing for readability.
 * <p>
 * Rendering format:
 * +----+----+----+...
 * | P1 |    | Kr |...
 * +----+----+----+...
 * | HB | LB |    |...
 * +----+----+----+...
 * <p>
 * Features:
 * - Fixed 4-character cell width for consistent alignment
 * - Centered notation in each cell
 * - Horizontal borders between rows
 * - Vertical separators between columns
 * - Empty cells shown as blank spaces
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class GridRenderer {
    /**
     * Width of content inside each cell (in characters)
     */
    private static final int CELL_WIDTH = 4;

    /**
     * Renders the current state of the TerrainGrid to the console.
     * Creates a formatted ASCII table showing all objects on the grid.
     * <p>
     * The grid is rendered from top (y=0) to bottom (y=9), and from
     * left (x=0) to right (x=9). Each cell displays the object's notation
     * or remains empty if no object is present.
     * <p>
     * Example output:
     * +----+----+----+----+----+----+----+----+----+----+
     * | Kr |    | P1 |    |    | LB |    | Cr |    | HB |
     * +----+----+----+----+----+----+----+----+----+----+
     * |    | Ma | SL |    | Cr |    | LB |    |    |    |
     * +----+----+----+----+----+----+----+----+----+----+
     * ...
     *
     * @param grid The game grid to render
     * @throws IllegalArgumentException if grid is null
     */
    public void renderState(TerrainGrid grid) {
        if (grid == null) {
            throw new IllegalArgumentException(
                    "GridRenderer Error: Cannot render a null TerrainGrid."
            );
        }

        try {
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
        } catch (Exception e) {
            System.err.println("Error rendering grid: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates the horizontal border string for the grid.
     * Creates a line of "+" symbols separated by dashes.
     * <p>
     * Format: +----+----+----+... (10 cells total)
     *
     * @return The complete horizontal border string
     */
    private String buildHorizontalBorder() {
        // Must match CELL_WIDTH (4 dashes per cell)
        return "+" + "----+".repeat(10);
    }

    /**
     * Safely extracts the display symbol from a terrain object.
     * Returns an empty string for null objects, otherwise returns
     * the object's notation.
     * <p>
     * Symbols include:
     * - Penguins: P1, P2, P3
     * - Food: Kr, Cr, An, Sq, Ma
     * - Hazards: LB, HB, SL, HI, PH
     *
     * @param obj The terrain object to get symbol from
     * @return The object's notation, or empty string if null
     */
    private String getDisplaySymbol(ITerrainObject obj) {
        if (obj == null) {
            return ""; // Empty cell
        }

        try {
            String symbol = obj.getNotation();
            return (symbol != null) ? symbol : "??";
        } catch (Exception e) {
            System.err.println("Error getting display symbol: " + e.getMessage());
            return "??"; // Error indicator
        }
    }

    /**
     * Centers a string within the defined CELL_WIDTH.
     * Adds padding spaces on both sides to center the content.
     * <p>
     * Centering algorithm:
     * 1. Calculate total padding needed
     * 2. Distribute padding: half before, half after
     * 3. Handle odd padding by favoring left side
     * <p>
     * Example: "P1" in 4-char cell -> " P1 "
     *
     * @param s The string to center (can be null or empty)
     * @return The centered string with exact CELL_WIDTH characters
     */
    private String centerString(String s) {
        if (s == null) {
            s = "";
        }

        int width = CELL_WIDTH;

        try {
            // If the string is too long, trim it (security against layout breaking)
            if (s.length() > width) {
                return s.substring(0, width);
            }

            // Calculate padding
            int padSize = width - s.length();
            int padStart = s.length() + padSize / 2;

            // Pad start (add spaces before content)
            s = String.format("%" + padStart + "s", s);

            // Pad end (ensure full cell width)
            s = String.format("%-" + width + "s", s);

            return s;
        } catch (Exception e) {
            System.err.println("Error centering string: " + e.getMessage());
            // Return padded empty string as fallback
            return String.format("%-" + width + "s", "");
        }
    }

    /**
     * Gets the cell width used by this renderer.
     *
     * @return The CELL_WIDTH constant (4 characters)
     */
    public int getCellWidth() {
        return CELL_WIDTH;
    }
}