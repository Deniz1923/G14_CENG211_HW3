import game.IcyTerrain;

/**
 * Main entry point for the Sliding Penguins Puzzle Game application.
 * This class contains the main method that initializes and runs the game.
 *
 * <p>Game overview:</p>
 * <ul>
 *   <li>3 penguins compete on a 10x10 icy terrain grid</li>
 *   <li>15 hazards and 20 food items are scattered across the grid</li>
 *   <li>One penguin is randomly assigned to the player</li>
 *   <li>Each penguin gets 4 turns to collect food and avoid hazards</li>
 *   <li>Winner is determined by total food weight collected</li>
 * </ul>
 *
 * <p>Penguin types and their special abilities:</p>
 * <ul>
 *   <li><b>King Penguin:</b> Can stop at the 5th square while sliding</li>
 *   <li><b>Emperor Penguin:</b> Can stop at the 3rd square while sliding</li>
 *   <li><b>Royal Penguin:</b> Can move one square safely before sliding</li>
 *   <li><b>Rockhopper Penguin:</b> Can jump over one hazard in their path</li>
 * </ul>
 *
 * <p>Hazard types:</p>
 * <ul>
 *   <li><b>Light Ice Block (LB):</b> Stuns penguin, can slide</li>
 *   <li><b>Heavy Ice Block (HB):</b> Immovable, removes lightest food</li>
 *   <li><b>Sea Lion (SL):</b> Bounces penguin backward, can slide</li>
 *   <li><b>Hole In Ice (HI):</b> Eliminates penguin from game</li>
 * </ul>
 *
 * <p>Game mechanics:</p>
 * <ul>
 *   <li>Penguins slide continuously until hitting obstacle or edge</li>
 *   <li>Food is collected automatically when reached</li>
 *   <li>Falling into water eliminates penguin but keeps collected food</li>
 *   <li>Each penguin's special ability can be used once per game</li>
 * </ul>
 *
 * <p><b>IMPORTANT:</b> This application requires console input for player
 * interaction. Run in a terminal or IDE console that supports System.in.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class SlidingPuzzleApp {

  /**
   * Main entry point of the Sliding Penguins Puzzle Game.
   * Creates an IcyTerrain instance which automatically:
   * <ol>
   *   <li>Generates the 10x10 grid</li>
   *   <li>Spawns 3 penguins on edge squares</li>
   *   <li>Spawns 15 hazards randomly</li>
   *   <li>Spawns 20 food items randomly</li>
   *   <li>Displays initial grid state</li>
   *   <li>Randomly assigns one penguin to the player</li>
   *   <li>Runs the 4-turn game loop</li>
   *   <li>Displays final scoreboard</li>
   * </ol>
   *
   * <p>After the game completes, the input master is properly closed
   * to release the System.in resource.</p>
   *
   * <p>Error handling:</p>
   * <ul>
   *   <li>Game initialization errors are caught and displayed</li>
   *   <li>Runtime errors are logged with stack traces</li>
   *   <li>Input master is always closed in finally block</li>
   * </ul>
   *
   * @param args Command line arguments (not used in this application)
   */
  public static void main(String[] args) {
    IcyTerrain terrain = null;

    try {
      // Initialize and run the game
      terrain = new IcyTerrain();

      // Game runs automatically in IcyTerrain constructor
      // and continues until completion or error

    } catch (Exception e) {
      // Catch any unexpected errors during game execution
      System.err.println("\n*** CRITICAL ERROR ***");
      System.err.println("The game encountered an unexpected error and must close.");
      System.err.println("Error details: " + e.getMessage());
      e.printStackTrace();

    } finally {
      // ALWAYS close the input master to release resources
      // This is critical for proper cleanup
      if (terrain != null) {
        try {
          terrain.closeInputMaster();
        } catch (Exception e) {
          System.err.println("Error closing input master: " + e.getMessage());
        }
      }
    }
  }
}