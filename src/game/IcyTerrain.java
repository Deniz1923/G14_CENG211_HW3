package game;

import game.util.GridRenderer;
import game.util.InputMaster;
import models.penguins.Penguin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main game controller that initializes and manages the Sliding Penguins game.
 * This class handles the complete game lifecycle from initialization to completion.
 * <p>
 * Initialization sequence:
 * 1. Create input master for player interaction
 * 2. Create grid renderer for displaying game state
 * 3. Create object spawner for populating the grid
 * 4. Create the 10x10 terrain grid
 * 5. Spawn all game objects (penguins, hazards, food)
 * 6. Display initial grid state
 * 7. Show penguin information
 * 8. Start game manager and run game loop
 * <p>
 * This class also handles error management and provides graceful
 * failure messages if initialization fails.
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class IcyTerrain {
    private static final Logger LOGGER = Logger.getLogger(IcyTerrain.class.getName());

    /**
     * Handles user input from console
     */
    private final InputMaster inputMaster;

    /**
     * Renders the grid to console display
     */
    private final GridRenderer renderer;

    /**
     * Spawns penguins, hazards, and food on the grid
     */
    private final ObjectSpawner spawner;

    /**
     * The 10x10 game grid containing all objects
     */
    private TerrainGrid gameGrid;

    /**
     * Manages game flow and turn processing
     */
    private GameManager gameManager;

    /**
     * Constructs an IcyTerrain and initializes the game.
     * This constructor automatically starts the complete game initialization
     * process, including grid creation, object spawning, and game execution.
     * If any errors occur during initialization, they are caught and
     * displayed with appropriate error messages.
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
     * 1. Display welcome message
     * 2. Spawn all game objects on the grid
     * 3. Render initial grid state
     * 4. Display penguin type information
     * 5. Create game manager and start game loop
     * <p>
     * Error handling:
     * - IllegalArgumentException - Displays reason for initialization failure
     * - General Exception - Displays error message and stack trace
     */
    private void initializeGame() {
        try {
            System.out.println(
                    "Welcome to Sliding Penguins Puzzle Game App. " +
                            "An 10x10 icy terrain grid is being generated."
            );

            System.out.print(
                    "Penguins, Hazards, and Food items are also being generated."
            );
            spawner.spawnObjects(gameGrid);

            System.out.println(" The initial icy terrain grid:");
            renderer.renderState(gameGrid);

            displayPenguinInfo();

            gameManager = new GameManager(gameGrid, renderer, inputMaster);
            gameManager.gameLoop();

        } catch (IllegalArgumentException e) {
            System.out.println("Game initialization failed.");
            System.out.println("Reason: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Initialization error", e);
        } catch (RuntimeException e) {
            // FIX: Catch runtime exceptions separately
            System.out.println("A critical runtime error occurred during game initialization.");
            System.out.println("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Runtime error during initialization", e);
            throw e; // Re-throw to stop execution
        } catch (Exception e) {
            System.out.println("An unexpected exception has occurred during game initialization.");
            LOGGER.log(Level.SEVERE, "Unexpected error during game initialization", e);
        }
    }

    /**
     * Scans the grid for penguins, sorts them by ID (P1, P2, P3), and displays their info.
     */
    private void displayPenguinInfo() {
        System.out.println("These are the penguins on the icy terrain:");

        java.util.List<Penguin> foundPenguins = new java.util.ArrayList<>();

        // 1. Collect all penguins from the grid
        for (int y = 0; y < TerrainGrid.GRID_SIZE; y++) {
            for (int x = 0; x < TerrainGrid.GRID_SIZE; x++) {
                var obj = gameGrid.getObjectAt(new models.Position(x, y));
                if (obj instanceof Penguin p) {
                    foundPenguins.add(p);
                }
            }
        }

        // 2. Sort them by ID (Notation) to ensure P1, P2, P3 order
        foundPenguins.sort(java.util.Comparator.comparing(Penguin::getNotation));

        // 3. Print them in order
        for (Penguin p : foundPenguins) {
            String typeName = switch (p.getType()) {
                case "Emperor" -> "Emperor Penguin";
                case "King" -> "King Penguin";
                case "Royal" -> "Royal Penguin";
                case "Rockhopper" -> "Rockhopper Penguin";
                default -> p.getType() + " Penguin";
            };
            System.out.println("- Penguin " + p.getNotation().substring(1) + " (" + p.getNotation() + "): " + typeName);
        }
    }

    /**
     * Gets the current game grid.
     * Used primarily for testing or external grid access.
     * <p>
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
     * <p>
     * Call this in the main method after the game completes:
     * IcyTerrain terrain = new IcyTerrain();
     * terrain.closeInputMaster(); // Always close!
     */
    public void closeInputMaster() {
        if (inputMaster != null) {
            try {
                inputMaster.close();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing input master", e);
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