package models.hazards;

import enums.HazardType;
import game.TerrainGrid;
import models.Position;
import models.penguins.Penguin;

public class SeaLion extends Hazard {
    public SeaLion(Position position) {
        super(position, true, HazardType.SEA_LION);
    }

    @Override
    public void onCollision(Penguin penguin, TerrainGrid grid) {
        System.out.println(penguin.getNotation() + " bounces off the SeaLion!");

        // Penguin bounces back - need to determine original direction
        // This is handled by the calling code in Penguin.slide()
        // SeaLion will start sliding in the original direction
    }

}