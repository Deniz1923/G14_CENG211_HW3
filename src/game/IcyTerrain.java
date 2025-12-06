package game;


public class IcyTerrain {
  private TerrainGrid gameGrid = new TerrainGrid();
  private InputMaster inputMaster = new InputMaster();

  public TerrainGrid getGameGrid() {
    return gameGrid;
  }

  public void setGameGrid(TerrainGrid gameGrid) {
    this.gameGrid = gameGrid;
  }




    public void closeInputMaster(){
      inputMaster.close();
    }
}
