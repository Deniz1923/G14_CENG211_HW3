import game.IcyTerrain;

/**
 * Main entry point for the Sliding Penguins Puzzle Game application.
 * This class contains the main method that initializes and runs the game.
 *
 * Game overview:
 * - 3 penguins compete on a 10x10 icy terrain grid
 * - 15 hazards and 20 food items are scattered across the grid
 * - One penguin is randomly assigned to the player
 * - Each penguin gets 4 turns to collect food and avoid hazards
 * - Winner is determined by total food weight collected
 *
 * Penguin types and their special abilities:
 * - King Penguin: Can stop at the 5th square while sliding
 * - Emperor Penguin: Can stop at the 3rd square while sliding
 * - Royal Penguin: Can move one square safely before sliding
 * - Rockhopper Penguin: Can jump over one hazard in their path
 *
 * Hazard types:
 * - Light Ice Block (LB): Stuns penguin, can slide
 * - Heavy Ice Block (HB): Immovable, removes lightest food
 * - Sea Lion (SL): Bounces penguin backward, can slide
 * - Hole In Ice (HI): Eliminates penguin from game
 *
 * Game mechanics:
 * - Penguins slide continuously until hitting obstacle or edge
 * - Food is collected automatically when reached
 * - Falling into water eliminates penguin but keeps collected food
 * - Each penguin's special ability can be used once per game
 *
 * <p>
 * Note: This application requires a console environment supporting {@code System.in}.
 * </p>
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class SlidingPuzzleApp {

    /**
     * Main entry point of the Sliding Penguins Puzzle Game.
     * Creates an IcyTerrain instance which automatically:
     * 1. Generates the 10x10 grid
     * 2. Spawns 3 penguins on edge squares
     * 3. Spawns 15 hazards randomly
     * 4. Spawns 20 food items randomly
     * 5. Displays initial grid state
     * 6. Randomly assigns one penguin to the player
     * 7. Runs the 4-turn game loop
     * 8. Displays final scoreboard
     *
     * After the game completes, the input master is properly closed
     * to release the System.in resource.
     *
     * Error handling:
     * - Game initialization errors are caught and displayed
     * - Runtime errors are logged with stack traces
     * - Input master is always closed in finally block
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
            // Ensure the input scanner is closed to prevent resource leaks
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