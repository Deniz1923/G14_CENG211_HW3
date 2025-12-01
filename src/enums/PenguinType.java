package enums;

import models.penguins.EmperorPenguin;
import models.penguins.KingPenguin;
import models.penguins.RockhopperPenguin;
import models.penguins.RoyalPenguin;

import java.util.Random;

public enum PenguinType {
    EMPEROR,
    KING,
    ROCKHOPPER,
    ROYAL;

    private static final Random RANDOM = new Random();

    public static PenguinType getRandomType() {
        PenguinType[] types = values();
        return types[RANDOM.nextInt(types.length)];
    }

}
