package game;

import game.util.GridRenderer;
import game.util.InputMaster;

/**
 * Main game controller that initializes and manages the Sliding Penguins game.
 * This class handles the complete game lifecycle from initialization to completion.
 *
 * <p>Initialization sequence:</p>
 * <ol>
 *   <li>Create input master for player interaction</li>
 *   <li>Create grid renderer for displaying game state</li>
 *   <li>Create object spawner for populating the grid</li>
 *   <li>Create the 10x10 terrain grid</li>
 *   <li>Spawn all game objects (penguins, hazards, food)</li>
 *   <li>Display initial grid state</li>
 *   <li>Show penguin information</li>
 *   <li>Start game manager and run game loop</li>
 * </ol>
 *
 * <p>This class also handles error management and provides graceful
 * failure messages if initialization fails.</p>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class IcyTerrain {
    /** Handles user input from console */
    private final InputMaster inputMaster;

    /** Renders the grid to console display */
    private final GridRenderer renderer;

    /** Spawns penguins, hazards, and food on the grid */
    private final ObjectSpawner spawner;

    /** The 10x10 game grid containing all objects */
    private TerrainGrid gameGrid;

    /** Manages game flow and turn processing */
    private GameManager gameManager;

    /**
     * Constructs an IcyTerrain and initializes the game.
     * This constructor automatically starts the complete game initialization
     * process, including grid creation, object spawning, and game execution.
     *
     * <p>If any errors occur during initialization, they are caught and
     * displayed with appropriate error messages.</p>
     */
    public IcyTerrain() {
        this.inputMaster = new InputMaster();
        this.renderer = new GridRenderer();
        this.spawner = new ObjectSpawner();
        this.gameGrid = new TerrainGrid();

        initializeGame();
    }

    /**
     * Initializes and runs the complete game sequence.
     * This method handles all stages of game setup and execution:
     * <ol>
     *   <li>Display welcome message</li>
     *   <li>Spawn all game objects on the grid</li>
     *   <li>Render initial grid state</li>
     *   <li>Display penguin type information</li>
     *   <li>Create game manager and start game loop</li>
     * </ol>
     *
     * <p>Error handling:</p>
     * <ul>
     *   <li>IllegalArgumentException - Displays reason for initialization failure</li>
     *   <li>General Exception - Displays error message and stack trace</li>
     * </ul>
     */
    private void initializeGame() {
        try {
            // Display welcome message
            System.out.println(
                    "Welcome to Sliding Penguins Puzzle Game App. " +
                            "An 10x10 icy terrain grid is being generated."
            );

            // 1. Spawn Objects securely
            System.out.print(
                    "Penguins, Hazards, and Food items are also being generated."
            );
            spawner.spawnObjects(gameGrid);

            // 2. Render the initial state
            System.out.println(" The initial icy terrain grid:");
            renderer.renderState(gameGrid);

            // 3. Display penguin types
            displayPenguinInfo();

            // 4. Start the game
            gameManager = new GameManager(gameGrid, renderer, inputMaster);
            gameManager.gameLoop();

        } catch (IllegalArgumentException e) {
            // Specific handling for argument validation errors
            System.out.println("Game initialization failed.");
            System.out.println("Reason: " + e.getMessage());
        } catch (Exception e) {
            // General exception handling
            System.out.println("An exception has occurred during game initialization.");
            System.err.println("Error details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays information about all penguins on the terrain.
     * Scans the grid and prints each penguin's ID and type name.
     *
     * <p>Output format:</p>
     * <pre>
     * These are the penguins on the icy terrain:
     * - Penguin 1 (P1): Emperor Penguin
     * - Penguin 2 (P2): King Penguin
     * - Penguin 3 (P3): Royal Penguin
     * </pre>
     *
     * <p>The type names are converted to human-readable format:
     * emperor → Emperor Penguin, king → King Penguin, etc.</p>
     */
    private void displayPenguinInfo() {
        try {
            System.out.println("These are the penguins on the icy terrain:");

            // Scan entire grid for penguins
            for (int y = 0; y < TerrainGrid.GRID_SIZE; y++) {
                for (int x = 0; x < TerrainGrid.GRID_SIZE; x++) {
                    var obj = gameGrid.getObjectAt(new models.Position(x, y));

                    if (obj instanceof models.penguins.Penguin) {
                        models.penguins.Penguin p = (models.penguins.Penguin) obj;

                        // Determine penguin type by class instance
                        String typeName;
                        if (p instanceof models.penguins.EmperorPenguin) {
                            typeName = "Emperor Penguin";
                        } else if (p instanceof models.penguins.KingPenguin) {
                            typeName = "King Penguin";
                        } else if (p instanceof models.penguins.RoyalPenguin) {
                            typeName = "Royal Penguin";
                        } else if (p instanceof models.penguins.RockhopperPenguin) {
                            typeName = "Rockhopper Penguin";
                        } else {
                            typeName = "Unknown Penguin";
                            System.err.println("Warning: Unknown penguin type detected: " +
                                    p.getClass().getSimpleName());
                        }

                        // Extract number from notation (P1 → 1)
                        String penguinNumber = p.getNotation().substring(1);

                        System.out.println("- Penguin " + penguinNumber +
                                " (" + p.getNotation() + "): " + typeName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error displaying penguin info: " + e.getMessage());
        }
    }

    /**
     * Gets the current game grid.
     * Used primarily for testing or external grid access.
     *
     * @return The TerrainGrid instance
     */
    public TerrainGrid getGameGrid() {
        return gameGrid;
    }

    /**
     * Sets a new game grid.
     * This can be used to load a saved game or for testing purposes.
     *
     * @param gameGrid The new terrain grid
     * @throws IllegalArgumentException if gameGrid is null
     */
    public void setGameGrid(TerrainGrid gameGrid) {
        if (gameGrid == null) {
            throw new IllegalArgumentException(
                    "IcyTerrain Error: Cannot set game grid to null."
            );
        }
        this.gameGrid = gameGrid;
    }

    /**
     * Closes the input master's scanner resource.
     * This MUST be called when the game ends to properly release
     * the System.in resource and prevent resource leaks.
     *
     * <p>Call this in the main method after the game completes:</p>
     * <pre>
     * IcyTerrain terrain = new IcyTerrain();
     * terrain.closeInputMaster(); // Always close!
     * </pre>
     */
    public void closeInputMaster() {
        if (inputMaster != null) {
            try {
                inputMaster.close();
            } catch (Exception e) {
                System.err.println("Error closing input master: " + e.getMessage());
            }
        }
    }

    /**
     * Gets the game manager instance.
     * Used for testing or accessing game state.
     *
     * @return The GameManager instance, or null if game hasn't started
     */
    public GameManager getGameManager() {
        return gameManager;
    }
}