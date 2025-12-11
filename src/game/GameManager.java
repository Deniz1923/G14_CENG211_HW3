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
import models.penguins.RockhopperPenguin;
import models.penguins.RoyalPenguin;

/**
 * Manages the game flow, turns, and player interactions.
 * This is the central controller that orchestrates the entire game,
 * handling turn management, player input, AI decisions, and scoring.
 *
 * <p>Game structure:</p>
 * <ul>
 *   <li>4 turns total (MAX_TURNS = 4)</li>
 *   <li>3 penguins compete (sorted by ID: P1, P2, P3)</li>
 *   <li>One penguin is randomly assigned to the player</li>
 *   <li>Each turn: P1 → P2 → P3 → (repeat)</li>
 *   <li>Winner determined by total food weight at end</li>
 * </ul>
 *
 * <p>Turn sequence for each penguin:</p>
 * <ol>
 *   <li>Check if penguin is eliminated (position == null) - skip if yes</li>
 *   <li>Check if penguin is stunned - skip turn and clear stun flag</li>
 *   <li>Ask to use special ability (player) or decide randomly (AI)</li>
 *   <li>Execute special ability if chosen</li>
 *   <li>Ask for movement direction (player) or choose randomly (AI)</li>
 *   <li>Execute slide in chosen direction</li>
 *   <li>Render updated grid state</li>
 * </ol>
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class GameManager {
    /** Maximum number of turns in the game */
    private static final int MAX_TURNS = 4;

    /** The game grid containing all objects */
    private final TerrainGrid grid;

    /** Renders the grid to console */
    private final GridRenderer renderer;

    /** Handles player input */
    private final InputMaster inputMaster;

    /** List of all penguins in the game (sorted by notation) */
    private final List<Penguin> penguins;

    /**
     * Constructs a GameManager with the required game components.
     *
     * @param grid The terrain grid containing all game objects
     * @param renderer The grid renderer for displaying game state
     * @param inputMaster The input handler for player interaction
     * @throws IllegalArgumentException if any parameter is null
     */
    public GameManager(TerrainGrid grid, GridRenderer renderer, InputMaster inputMaster) {
        if (grid == null) {
            throw new IllegalArgumentException(
                    "GameManager Error: TerrainGrid cannot be null."
            );
        }
        if (renderer == null) {
            throw new IllegalArgumentException(
                    "GameManager Error: GridRenderer cannot be null."
            );
        }
        if (inputMaster == null) {
            throw new IllegalArgumentException(
                    "GameManager Error: InputMaster cannot be null."
            );
        }

        this.grid = grid;
        this.renderer = renderer;
        this.inputMaster = inputMaster;
        this.penguins = new ArrayList<>();
    }

    /**
     * Main game loop - runs the entire game from start to finish.
     * This method orchestrates the complete game flow:
     * <ol>
     *   <li>Initialize penguins list from grid</li>
     *   <li>Randomly select player penguin</li>
     *   <li>Run 4 turns with each penguin taking their turn</li>
     *   <li>Display final scoreboard</li>
     * </ol>
     */
    public void gameLoop() {
        try {
            // Initialize penguins list from grid
            sortPenguins();

            // Randomly select player penguin
            selectPlayerPenguin();

            // Main game loop - 4 turns
            for (int turn = 1; turn <= MAX_TURNS; turn++) {
                // Each penguin takes their turn
                for (Penguin p : penguins) {
                    // Skip if penguin has been eliminated
                    if (p.getPosition() == null) {
                        continue;
                    }

                    // Skip if penguin is stunned
                    if (p.isStunned()) {
                        System.out.println("\n*** Turn " + turn + " - " +
                                p.getNotation() + ":");
                        System.out.println(p.getNotation() +
                                " is stunned and skips this turn!");
                        p.setStunned(false);
                        continue;
                    }

                    // Process the penguin's turn
                    processTurn(p, turn);

                    // Render grid after each penguin's move
                    System.out.println("New state of the grid:");
                    renderer.renderState(grid);
                }
            }

            // Game over - show results
            System.out.println("\n***** GAME OVER *****");
            calculateScore();
        } catch (Exception e) {
            System.err.println("Critical error in game loop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Randomly selects one penguin to be controlled by the player.
     * All other penguins will be AI-controlled.
     * If no penguins exist, this method does nothing.
     */
    private void selectPlayerPenguin() {
        if (penguins.isEmpty()) {
            System.err.println("GameManager Warning: No penguins found for player selection.");
            return;
        }

        try {
            // Random index between 0 and penguins.size()-1
            int randomIndex = RandUtil.getRandomInt(penguins.size());
            Penguin selected = penguins.get(randomIndex);
            selected.setPlayer(true);
        } catch (Exception e) {
            System.err.println("Error selecting player penguin: " + e.getMessage());
        }
    }

    /**
     * Calculates and displays final scores in a formatted scoreboard.
     * Penguins are ranked by total food weight (descending order).
     * Displays each penguin's rank, food items, and total weight.
     * Player's penguin is marked with "(Your Penguin)".
     */
    private void calculateScore() {
        try {
            System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");

            // Sort penguins by carried weight (descending order)
            penguins.sort((p1, p2) ->
                    Integer.compare(p2.getCarriedWeight(), p1.getCarriedWeight()));

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

                System.out.println("  |---> Food items: " + foodInfo);
                System.out.println("  |---> Total weight: " +
                        p.measureInventory() + " units");
            }
        } catch (Exception e) {
            System.err.println("Error calculating scores: " + e.getMessage());
        }
    }

    /**
     * Returns the appropriate suffix for rankings (1st, 2nd, 3rd, 4th, etc.).
     *
     * @param rank The rank number (1, 2, 3, ...)
     * @return The suffix string ("st", "nd", "rd", or "th")
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
     * Processes a single penguin's turn.
     * Displays turn header and delegates to appropriate handler
     * (player or AI) based on penguin's isPlayer flag.
     *
     * @param p The penguin taking their turn
     * @param turnNumber The current turn number (1-4)
     */
    private void processTurn(Penguin p, int turnNumber) {
        try {
            // Print turn header with penguin notation on same line
            String turnHeader = "\n*** Turn " + turnNumber + " - " + p.getNotation();
            if (p.isPlayer()) {
                turnHeader += " (Your Penguin)";
            }
            turnHeader += ":";
            System.out.println(turnHeader);

            if (p.isPlayer()) {
                // Player-controlled penguin
                handlePlayerTurn(p);
            } else {
                // AI-controlled penguin
                handleAITurn(p);
            }
        } catch (Exception e) {
            System.err.println("Error processing turn: " + e.getMessage());
        }
    }

    /**
     * Handles the player's turn with input prompts.
     * Asks the player if they want to use special ability,
     * then asks for movement direction.
     *
     * <p>Special handling for RoyalPenguin:</p>
     * <ul>
     *   <li>If ability is used, asks for special move direction</li>
     *   <li>Executes special move before regular slide</li>
     *   <li>Checks for elimination after special move</li>
     * </ul>
     *
     * @param p The player's penguin
     */
    private void handlePlayerTurn(Penguin p) {
        try {
            // Ask if player wants to use special ability
            boolean useAbility = inputMaster.getYesNoInput(
                    "Will " + p.getNotation() +
                            " use its special action? Answer with Y or N: "
            );

            if (useAbility) {
                p.specialAbility();

                // Special handling for RoyalPenguin - needs direction for special move
                if (p instanceof RoyalPenguin) {
                    RoyalPenguin royal = (RoyalPenguin) p;
                    Direction specialDir = inputMaster.getDirectionInput(
                            "Which direction for the special move? " +
                                    "Answer with U (Up), D (Down), L (Left), R (Right): "
                    );
                    royal.performSpecialMove(grid, specialDir);

                    // Check if penguin was eliminated during special move
                    if (p.getPosition() == null) {
                        System.out.println("*** " + p.getNotation() +
                                " IS REMOVED FROM THE GAME!");
                        return;
                    }
                }
            } else {
                // Match PDF format: "does NOT to use" (grammatically incorrect but matches example)
                System.out.println(p.getNotation() + " does NOT to use its special action.");
            }

            // Ask for movement direction
            Direction direction = inputMaster.getDirectionInput(
                    "Which direction will " + p.getNotation() +
                            " move? Answer with U (Up), D (Down), L (Left), R (Right): "
            );
            p.slide(grid, direction);

            // Check if penguin was eliminated
            if (p.getPosition() == null) {
                System.out.println("*** " + p.getNotation() +
                        " IS REMOVED FROM THE GAME!");
            }
        } catch (Exception e) {
            System.err.println("Error handling player turn: " + e.getMessage());
        }
    }

    /**
     * Handles AI penguin turn with random decisions.
     * AI has 30% chance of using special ability (except Rockhopper).
     * RockhopperPenguins automatically use ability when moving toward hazards.
     *
     * <p>AI decision-making:</p>
     * <ul>
     *   <li>30% chance to use special ability (most penguins)</li>
     *   <li>RockhopperPenguin: Auto-use when hazard detected</li>
     *   <li>Random direction selection</li>
     *   <li>No strategic planning (simplified AI)</li>
     * </ul>
     *
     * @param p The AI-controlled penguin
     */
    private void handleAITurn(Penguin p) {
        try {
            // Determine if AI will use special ability
            boolean useAbility = false;

            // RockhopperPenguin: Check if moving toward hazard (simplified - just 30% for now)
            // In full implementation, would check the direction for hazards
            if (p instanceof RockhopperPenguin) {
                // For simplicity, 30% chance like others
                // In real game, would check if hazard is in path
                useAbility = RandUtil.getRandomInt(10) < 3;
                if (useAbility) {
                    System.out.println(p.getNotation() +
                            " will automatically USE its special action.");
                }
            } else {
                // For others, 30% chance
                useAbility = RandUtil.getRandomInt(10) < 3;
            }

            if (useAbility && !p.isAbilityUsed()) {
                p.specialAbility();

                // Special handling for RoyalPenguin
                if (p instanceof RoyalPenguin) {
                    RoyalPenguin royal = (RoyalPenguin) p;
                    Direction specialDir = RandUtil.getRandomDirection();
                    royal.performSpecialMove(grid, specialDir);

                    // Check if penguin was eliminated
                    if (p.getPosition() == null) {
                        System.out.println("*** " + p.getNotation() +
                                " IS REMOVED FROM THE GAME!");
                        return;
                    }
                }
            } else {
                // Match PDF format
                System.out.println(p.getNotation() + " does NOT to use its special action.");
            }

            // Choose random direction
            Direction direction = RandUtil.getRandomDirection();
            System.out.println(p.getNotation() + " chooses to move " +
                    direction + ".");
            p.slide(grid, direction);

            // Check if penguin was eliminated
            if (p.getPosition() == null) {
                System.out.println("*** " + p.getNotation() +
                        " IS REMOVED FROM THE GAME!");
            }
        } catch (Exception e) {
            System.err.println("Error handling AI turn: " + e.getMessage());
        }
    }

    /**
     * Collects all penguins from the grid and sorts them by notation (P1, P2, P3).
     * This ensures turns are processed in correct order regardless of
     * where penguins were spawned on the grid.
     *
     * <p>Process:</p>
     * <ol>
     *   <li>Clear existing penguin list</li>
     *   <li>Scan entire grid for penguins</li>
     *   <li>Add found penguins to list</li>
     *   <li>Sort by notation (P1 < P2 < P3)</li>
     * </ol>
     */
    private void sortPenguins() {
        try {
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

            if (penguins.isEmpty()) {
                System.err.println("GameManager Warning: No penguins found on grid!");
            }
        } catch (Exception e) {
            System.err.println("Error sorting penguins: " + e.getMessage());
        }
    }

    /**
     * Gets the list of all penguins in the game.
     * Used primarily for testing purposes.
     *
     * @return An unmodifiable view of the penguin list
     */
    public List<Penguin> getPenguins() {
        return new ArrayList<>(penguins);
    }

    /**
     * Gets the terrain grid being managed.
     *
     * @return The TerrainGrid instance
     */
    public TerrainGrid getGrid() {
        return grid;
    }
}