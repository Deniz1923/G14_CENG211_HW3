package game;

public class IcyTerrain {
  private final InputMaster inputMaster = new InputMaster();
  private TerrainGrid gameGrid = new TerrainGrid();

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
