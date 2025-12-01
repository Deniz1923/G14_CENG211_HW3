package models;

import enums.FoodType;

import java.util.Random;

public class Food {
    private FoodType type;
    private int weight;
    private Position position;
    Random random = new Random();

    public Food(FoodType type) {
        this.type = type;
        weight = random.nextInt(1,6);
    }

    public FoodType getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getSymbol() {
        switch(type) {
            case KRILL: return "Kr";
            case CRUSTACEAN: return "Cr";
            case ANCHOVY: return "An";
            case SQUID: return "Sq";
            case MACKEREL: return "Ma";
            default: return "??";
        }
    }
}