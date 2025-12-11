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
import java.util.concurrent.TimeUnit;

import models.Food;
import models.Position;
import models.penguins.Penguin;
import models.penguins.RockhopperPenguin;
import models.penguins.RoyalPenguin;

/**
 * Manages the game flow, turns, and player interactions.
 */
public class GameManager {
    private static final int MAX_TURNS = 4;
    private final TerrainGrid grid;
    private final GridRenderer renderer;
    private final InputMaster inputMaster;
    private final List<Penguin> penguins;

    public GameManager(TerrainGrid grid, GridRenderer renderer, InputMaster inputMaster) {
        this.grid = grid;
        this.renderer = renderer;
        this.inputMaster = inputMaster;
        this.penguins = new ArrayList<>();
    }

    /**
     * Main game loop - runs the entire game from start to finish
     */
    public void gameLoop() {
        // Initialize penguins list from grid
        sortPenguins();

        // Randomly select player penguin
        selectPlayerPenguin();

        // Main game loop - 4 turns
        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            System.out.println("\n***Turn " + turn + "***");

            // Each penguin takes their turn
            for (Penguin p : penguins) {
                // Skip if penguin has been eliminated
                if (p.getPosition() == null) {
                    continue;
                }

                // Skip if penguin is stunned
                if (p.isStunned()) {
                    System.out.println("\n*** Turn " + turn + " - " + p.getNotation() + ":");
                    System.out.println(p.getNotation() + " is stunned and skips this turn!");
                    p.setStunned(false);
                    continue;
                }

                // Process the penguin's turn
                processTurn(p, turn);

                // Render grid after each penguin's move
                System.out.println("\nNew state of the grid:");
                renderer.renderState(grid);
                sleep(2);
            }
        }

        // Game over - show results
        System.out.println("\n***** GAME OVER *****");
        calculateScore();
    }

    /**
     * Randomly selects one penguin to be controlled by the player
     */
    private void selectPlayerPenguin() {
        if (penguins.isEmpty()) {
            return;
        }

        // Random index between 0 and penguins.size()-1
        int randomIndex = RandUtil.getRandomInt(penguins.size());
        penguins.get(randomIndex).setPlayer(true);

        System.out.println("\n" + penguins.get(randomIndex).getNotation() + " is YOUR PENGUIN!");
    }

    /**
     * Calculates and displays final scores
     */
    private void calculateScore() {
        System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");

        // Sort penguins by carried weight (descending order)
        penguins.sort((p1, p2) -> Integer.compare(p2.getCarriedWeight(), p1.getCarriedWeight()));

        for (int i = 0; i < penguins.size(); i++) {
            Penguin p = penguins.get(i);
            int rank = i + 1;
            String suffix = getSuffix(rank);

            // Build header
            String header = p.getNotation();
            if (p.isPlayer()) {
                header += " (Your Penguin)";
            }

            System.out.println("* " + rank + suffix + " place: " + header);

            // Display food items
            StringBuilder foodInfo = new StringBuilder();
            List<Food> items = p.getInventory();

            if (items.isEmpty()) {
                foodInfo.append("None");
            } else {
                for (int j = 0; j < items.size(); j++) {
                    Food f = items.get(j);
                    foodInfo.append(f.getNotation())
                            .append(" (")
                            .append(f.getWeight())
                            .append(" units)");

                    if (j < items.size() - 1) {
                        foodInfo.append(", ");
                    }
                }
            }

            System.out.println("  |---> Food Items: " + foodInfo);
            System.out.println("  |---> Total weight: " + p.measureInventory() + " units");
        }
    }

    /**
     * Returns the appropriate suffix for rankings (1st, 2nd, 3rd, etc.)
     */
    private String getSuffix(int rank) {
        return switch (rank) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    /**
     * Processes a single penguin's turn
     */
    private void processTurn(Penguin p, int turnNumber) {
        System.out.println("\n*** Turn " + turnNumber + " - " + p.getNotation() + ":");
        System.out.println(p.getNotation() + " (" + capitalizeFirst(p.getType()) + " Penguin) is preparing to move.");

        if (p.isPlayer()) {
            // Player-controlled penguin
            handlePlayerTurn(p);
        } else {
            // AI-controlled penguin
            handleAITurn(p);
        }
    }

    /**
     * Handles the player's turn with input prompts
     */
    private void handlePlayerTurn(Penguin p) {
        boolean useAbility = false;
        System.out.println("YOUR PENGUIN");

        if(!p.isAbilityUsed()){
            useAbility = inputMaster.getYesNoInput("Will " + p.getNotation() + " use its special action? Answer with Y or N: ");
        }
        else{
            System.out.println(p.getNotation() + " has already used its special skill!");
        }


        if (useAbility && !p.isAbilityUsed()) {
            System.out.println("[DEBUG] Inside Ability Block"); // DEBUG 1

            System.out.println(p.getNotation() + " chooses to USE its special action.");

            // CAREFUL: Ensure p.specialAbility() doesn't have a while(true) loop!
            p.specialAbility();
            p.setAbilityUsed(true);

            System.out.println("[DEBUG] Ability set to used. Checking if Royal..."); // DEBUG 2

            if (p instanceof RoyalPenguin royal) {
                System.out.println("[DEBUG] It is a Royal Penguin. Waiting for input..."); // DEBUG 3

                // IF IT HANGS HERE: It's your InputMaster/Scanner
                Direction specialDir = inputMaster.getDirectionInput("Which direction for the special move? Answer with U (Up), D (Down), L (Left), R (Right): ");

                System.out.println("[DEBUG] Input received: " + specialDir + ". Performing move..."); // DEBUG 4

                // IF IT HANGS HERE: It's an infinite loop in performSpecialMove
                royal.performSpecialMove(grid, specialDir);

                System.out.println("[DEBUG] Move performed."); // DEBUG 5

                if (p.getPosition() == null) {
                    System.out.println("*** " + p.getNotation() + " IS REMOVED FROM THE GAME!");
                    return;
                }
            }
        } else {
            System.out.println(p.getNotation() + " does NOT use its special action.");
        }

        // Standard Movement
        Direction direction = inputMaster.getDirectionInput("Which direction will " + p.getNotation() + " move? Answer with U (Up), D (Down), L (Left), R (Right): ");
        p.slide(grid, direction);

        if (p.getPosition() == null) {
            System.out.println("*** " + p.getNotation() + " IS REMOVED FROM THE GAME!");
        }
    }

    /**
     * Handles AI penguin turn with random decisions
     */
    private void handleAITurn(Penguin p) {
        // Determine if AI will use special ability
        boolean useAbility = false;

        // RockhopperPenguin automatically uses ability when moving toward hazard
        // For others, 30% chance
        if (!(p instanceof RockhopperPenguin)) {
            useAbility = RandUtil.getRandomInt(TerrainGrid.GRID_SIZE) < 3; // 30% chance (0-2 out of 0-9)
        }

        if (useAbility && !p.isAbilityUsed()) {
            System.out.println(p.getNotation() + " chooses to USE its special action.");
            p.specialAbility();

            // Special handling for RoyalPenguin
            if (p instanceof RoyalPenguin royal) {
                Direction specialDir = RandUtil.getRandomDirection();
                royal.performSpecialMove(grid, specialDir);

                // Check if penguin was eliminated
                if (p.getPosition() == null) {
                    System.out.println("*** " + p.getNotation() + " IS REMOVED FROM THE GAME!");
                    return;
                }
            }
        } else {
            System.out.println(p.getNotation() + " does NOT use its special action.");
        }

        // Choose random direction
        Direction direction = RandUtil.getRandomDirection();
        System.out.println(p.getNotation() + " chooses to move " + direction + ".");
        p.slide(grid, direction);

        // Check if penguin was eliminated
        if (p.getPosition() == null) {
            System.out.println("*** " + p.getNotation() + " IS REMOVED FROM THE GAME!");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Collects all penguins from the grid and sorts them by notation (P1, P2, P3)
     */
    private void sortPenguins() {
        penguins.clear();

        // Scan entire grid for penguins
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                ITerrainObject object = grid.getObjectAt(new Position(x, y));
                if (object instanceof Penguin) {
                    penguins.add((Penguin) object);
                }
            }
        }

        // Sort by notation (P1, P2, P3)
        penguins.sort(Comparator.comparing(Penguin::getNotation));
    }

    /**
     * Capitalizes the first letter of a string
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void sleep(int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}