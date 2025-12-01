package models.penguins;

import enums.PenguinType;

public class KingPenguin extends Penguin{
    int carriedWeight = 0;

    public KingPenguin() {
        super(PenguinType.EMPEROR);
    }

    private int measureInventory(){
        return 0;
    }

    @Override
    void ability1() {

    }

}
