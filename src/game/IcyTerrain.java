package game;

import game.util.GridRenderer;
import game.util.InputMaster;
import models.penguins.Penguin;

/**
 * The main container for the game environment.
 * This class initializes the game components including the TerrainGrid,
 * ObjectSpawner, and GameManager. It orchestrates the game's startup
 * sequence: spawning objects, rendering the initial state, and handing control over to
 * the GameManager for the game loop.
 */
public class IcyTerrain {
    /** Handles user input from the console. */
    private final InputMaster inputMaster;

    /** Renders the grid state to the console. */
    private final GridRenderer renderer;

    /** Handles the random generation and placement of game objects. */
    private final ObjectSpawner spawner;

    /** The 10x10 grid representing the icy terrain. */
    private TerrainGrid gameGrid;

    /** Manages the game flow, turns, and logic. */
    private GameManager gameManager;

    /**
     * Constructs a new IcyTerrain instance and immediately initializes the game.
     * This constructor sets up the input handler, renderer, object spawner, and the
     * empty terrain grid before calling initializeGame().
     */
    public IcyTerrain() {
        this.inputMaster = new InputMaster();
        this.renderer = new GridRenderer();
        this.spawner = new ObjectSpawner();
        this.gameGrid = new TerrainGrid();

        initializeGame();
    }

    /**
     * Initializes the game execution sequence.
     * This method performs the following steps:
     * Prints the welcome message.
     * Invokes the ObjectSpawner to populate the grid with Penguins, Hazards, and Food.
     * Renders the initial state of the grid using GridRenderer.
     * Displays information about the generated penguins.
     * Instantiates the GameManager and starts the main game loop.
     * Catches IllegalArgumentException for initialization errors and generic
     * Exception for unexpected runtime errors, printing appropriate error messages.
     */
    private void initializeGame() {
        try {
            System.out.println(
                    "Welcome to Sliding Penguins Puzzle Game App. An 10x10 icy terrain grid is being generated.");

            // 1. Spawn Objects securely
            System.out.print("Penguins, Hazards, and Food items are also being generated.");
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
            System.out.println("Game initialization failed.");
            System.out.println("Reason: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An exception has occurred");
            e.printStackTrace();
        }
    }

    /**
     * Scans the grid for penguins and displays their ID and Type.
     * Iterates through every cell in the TerrainGrid. If a Penguin is found,
     * it prints the penguin's ID (e.g., P1) and its specific type (e.g., Emperor Penguin).
     * This uses a switch expression to map the internal type string to a readable name.
     */
    private void displayPenguinInfo() {
        System.out.println("These are the penguins on the icy terrain:");

        for (int y = 0; y < TerrainGrid.GRID_SIZE; y++) {
            for (int x = 0; x < TerrainGrid.GRID_SIZE; x++) {
                var obj = gameGrid.getObjectAt(new models.Position(x, y));
                if (obj instanceof Penguin p) {
                    String typeName = switch (p.getType()) {
                        case "emperor" -> "Emperor Penguin";
                        case "king" -> "King Penguin";
                        case "royal" -> "Royal Penguin";
                        case "rockhopper" -> "Rockhopper Penguin";
                        default -> "Unknown Penguin";
                    };
                    System.out.println("- Penguin " + p.getNotation().substring(1) + " (" + p.getNotation() + "): " + typeName);
                }
            }
        }
    }

    /**
     * Gets the current game grid.
     *
     * @return The TerrainGrid instance containing all game objects.
     */
    public TerrainGrid getGameGrid() {
        return gameGrid;
    }

    /**
     * Sets the game grid.
     * Note: This is primarily used for testing or resetting the game state.
     * @param gameGrid The new TerrainGrid to set.
     */
    public void setGameGrid(TerrainGrid gameGrid) {
        this.gameGrid = gameGrid;
    }

    /**
     * Closes the InputMaster resource.
     * Should be called when the application is shutting down to prevent resource leaks
     * associated with the java.util.Scanner.
     */
    public void closeInputMaster() {
        inputMaster.close();
    }
}