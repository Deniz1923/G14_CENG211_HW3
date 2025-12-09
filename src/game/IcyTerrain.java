package game;

import game.util.GridRenderer;
import game.util.InputMaster;

public class IcyTerrain {
    private final InputMaster inputMaster;
    private final GridRenderer renderer;
    private final ObjectSpawner spawner;
    private TerrainGrid gameGrid;
    private GameManager gameManager;

    public IcyTerrain() {
        this.inputMaster = new InputMaster();
        this.renderer = new GridRenderer();
        this.spawner = new ObjectSpawner();
        this.gameGrid = new TerrainGrid();

        initializeGame();
    }

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

    private void displayPenguinInfo() {
        System.out.println("These are the penguins on the icy terrain:");

        for (int y = 0; y < TerrainGrid.GRID_SIZE; y++) {
            for (int x = 0; x < TerrainGrid.GRID_SIZE; x++) {
                var obj = gameGrid.getObjectAt(new models.Position(x, y));
                if (obj instanceof models.penguins.Penguin) {
                    models.penguins.Penguin p = (models.penguins.Penguin) obj;
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

    public TerrainGrid getGameGrid() {
        return gameGrid;
    }

    public void setGameGrid(TerrainGrid gameGrid) {
        this.gameGrid = gameGrid;
    }

    public void closeInputMaster() {
        inputMaster.close();
    }
}