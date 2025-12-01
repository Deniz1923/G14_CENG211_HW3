package models.hazards;

import models.penguins.Penguin;
import game.TerrainGrid;

public class LightIceBlock extends Hazard {

    public LightIceBlock() {
        super(true);
    }

    @Override
    public void onCollision(Penguin penguin, TerrainGrid grid) {
        penguin.setStunned(true);
    }

    @Override
    public String getSymbol() {
        return "LB";
    }
}