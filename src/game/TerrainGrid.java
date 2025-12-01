package game;

import interfaces.ITerrainObject;

public class TerrainGrid {

    // The Game Grid Itself
    private ITerrainObject[][] terrainGrid = new ITerrainObject[10][10];




    public ITerrainObject[][] getTerrainGrid() {
        return terrainGrid;
    }

    public void setTerrainGrid(ITerrainObject[][] terrainGrid) {
        this.terrainGrid = terrainGrid;
    }
}
