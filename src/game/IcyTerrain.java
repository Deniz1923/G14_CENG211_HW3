package game;

public class IcyTerrain {
  private final InputMaster inputMaster;
  private final GridRenderer renderer;
  private final ObjectSpawner spawner;
  private TerrainGrid gameGrid;

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

    } catch (IllegalArgumentException e) {
      System.out.println("Game initialization failed.");
      System.out.println("Reason: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("An exception has occurred");
      e.printStackTrace();
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
