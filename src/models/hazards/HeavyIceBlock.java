package models.hazards;

import models.penguins.Penguin;
import game.TerrainGrid;

public class HeavyIceBlock extends Hazard {

    public HeavyIceBlock() {
        super(false);
    }

    @Override
    public void onCollision(Penguin penguin, TerrainGrid grid) {
        penguin.removeLightestFood();
    }

    @Override
    public String getSymbol() {
        return "HB";
    }
}