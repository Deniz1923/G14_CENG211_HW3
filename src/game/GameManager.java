package game;

import java.util.ArrayList;
import java.util.List;
import models.penguins.Penguin;

/**
 * Manages the game flow, turns, and player interactions.
 */
public class GameManager {
    private static final int MAX_TURNS = 4;
    private final TerrainGrid grid;
    private final GridRenderer renderer;
    private final InputMaster inputMaster;
    private final List<Penguin> penguins;
    private Penguin playerPenguin;
    private int currentTurn = 1;

    public GameManager(TerrainGrid grid, GridRenderer renderer, InputMaster inputMaster) {
        this.grid = grid;
        this.renderer = renderer;
        this.inputMaster = inputMaster;
        this.penguins = new ArrayList<>();
    }

}