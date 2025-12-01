package terrainObjects.foods;
import java.util.Random;

public abstract class food {
    private int weight;
    Random random = new Random();
    public food(){
        weight = 0;
    }

    private int randomizeWeight(){
        weight = random.nextInt(5) + 1;
        return weight;

    }
}
