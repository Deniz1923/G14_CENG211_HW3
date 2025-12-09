package game;

import static game.TerrainGrid.GRID_SIZE;

import enums.Direction;
import game.util.GridRenderer;
import game.util.InputMaster;
import game.util.RandUtil;
import interfaces.ITerrainObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import models.Food;
import models.Position;
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

    public GameManager(TerrainGrid grid, GridRenderer renderer, InputMaster inputMaster) {
        this.grid = grid;
        this.renderer = renderer;
        this.inputMaster = inputMaster;
        this.penguins = new ArrayList<>();
    }

    public void gameLoop(){
        //sorts the arraylist from 1 to 3
        sortPenguins();
        //randomly selects player penguin
        selectPlayerPenguin();
        //main game loop
        // FIX: Changed from < to <= to run exactly 4 turns
        for(int turn = 1; turn <= MAX_TURNS; turn++){
            System.out.println("\n***Turn " + turn + "***");

            for(Penguin p : penguins){
                //Skip if eliminated
                if(p.getPosition() == null){
                    continue;
                }

                if(p.isStunned()){
                    System.out.println(p.getNotation() + " is stunned and skips this round!");
                    p.setStunned(false);
                    continue;
                }
                processTurn(p);
                //Render grid after every turn
                renderer.renderState(grid);
            }
        }
        System.out.println("***** GAME OVER *****");
        calculateScore();
    }

    private void selectPlayerPenguin(){
        // FIX: Changed from getRandomInt(4)+1 to getRandomInt(3)
        // List has 3 penguins with indices 0-2
        int temp = RandUtil.getRandomInt(3);
        penguins.get(temp).setPlayer(true);
    }

    private void calculateScore() {
        System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");
        penguins.sort((p1, p2) -> Integer.compare(p2.getCarriedWeight(), p1.getCarriedWeight()));
        for(int i = 0; i < penguins.size() ; i++){
            Penguin p = penguins.get(i);
            int rank = i + 1;
            String suffix = "";

            //Generate suffix (1st,2nd,3rd,th)
            switch(rank){
                case 1 -> suffix = "st";
                case 2 -> suffix = "nd";
                case 3 -> suffix = "rd";
                default -> suffix = "th";
            }
            String header = p.getNotation();
            if(p.isPlayer()){
                header += " (Your Penguin)";
            }

            System.out.println("* " + rank + suffix + " place: " + header);


            StringBuilder foodInfo = new StringBuilder();
            List<Food> items = p.getInventory(); // inventory is arraylist

            if(items.isEmpty()){
                foodInfo.append("None");
            }
            else{
                for (int j = 0; j < items.size(); j++) {
                    Food f = items.get(j);
                    // format: "Kr (5 units)"
                    foodInfo.append(f.getNotation())
                            .append(" (")
                            .append(f.getWeight())
                            .append(" units)");

                    // Add comma if not the last item
                    if (j < items.size() - 1) {
                        foodInfo.append(", ");
                    }
                }
            }

            System.out.println("  |---> Food Items: " + foodInfo.toString());
            System.out.println("  |---> Total weight: " + p.measureInventory() + " units");

        }
    }

    private void processTurn(Penguin p){
        System.out.println(p.getNotation() + " (" + p.getType() + ") is preparing to move.");

        if(p.isPlayer()){
            System.out.println("YOUR PENGUIN");

            boolean useAbility = inputMaster.getYesNoInput("Will " + p.getNotation() + " use its special action? Answer with Y or N: ");
            if(useAbility){
                p.specialAbility();
                //..................
            }
            // FIX: Changed hardcoded "P2" to p.getNotation()
            Direction direction = inputMaster.getDirectionInput("Which direction will " + p.getNotation() + " move? Answer with U (Up), D (Down), L (Left), R (Right): ");
            p.slide(grid, direction);
        }
        //logic of non-player penguins
        else{
            Direction direction = RandUtil.getRandomDirection();
            System.out.println(p.getNotation() + " chooses to move " + direction);
            p.slide(grid, direction);
        }
    }

    private void sortPenguins(){
        penguins.clear();
        for(int y = 0; y < GRID_SIZE; y++){
            for(int x = 0; x < GRID_SIZE; x++){
                ITerrainObject object = grid.getObjectAt(new Position(x,y));
                if(object instanceof Penguin){
                    penguins.add((Penguin) object);
                }
            }
        }
        penguins.sort(Comparator.comparing(Penguin::getNotation));
    }
}