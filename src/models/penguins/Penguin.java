package models.penguins;

import enums.PenguinType;
import models.Food;
import models.Position;

import java.util.ArrayList;

public abstract class Penguin{
    private PenguinType type;
    private Position position;
    private int carriedWeight = 0;
    private final ArrayList<Food> inventory;

    public Penguin(PenguinType penguinType){
        if(penguinType != null){
            this.type = penguinType;
        }

        this.inventory = new ArrayList<>();

    }
    public void removeLightestFood(){
        if(!inventory.isEmpty()){
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            for(int i = 0; i < inventory.size() ; i++){
                if (inventory.get(i).getWeight() < min){
                    min = inventory.get(i).getWeight();
                    minIndex = i;
                }
            }
            inventory.remove(minIndex);
        }
    }
    private int measureInventory() //measure the weight of each food in the inventory
    {
        int sum = 0;
        for(Food food : inventory){
            sum += food.getWeight();
        }
        return sum;
    }

    abstract void ability1();

    private void pickupFood(Food food) {
        if(food != null){
            inventory.add(food);
        }
    }

    private void slide(){
        //sliding function
    }

    public String getType(){
        return type.toString().toLowerCase();
    }

    public int getCarriedWeight(){
        return carriedWeight;
    }
}
