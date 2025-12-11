package game;

import enums.Direction;
import game.util.GridRenderer;
import game.util.InputMaster;
import game.util.RandUtil;
import models.Food;
import models.Position;
import models.penguins.Penguin;
import models.penguins.RockhopperPenguin;
import models.penguins.RoyalPenguin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the game flow, turns, and player interactions.
 * This is the central controller that orchestrates the entire game,
 * handling turn management, player input, AI decisions, and scoring.
 * <p>
 * Game structure:
 * - 4 turns total (MAX_TURNS = 4)
 * - 3 penguins compete (sorted by ID: P1, P2, P3)
 * - One penguin is randomly assigned to the player
 * - Each turn: P1 -> P2 -> P3 -> (repeat)
 * - Winner determined by total food weight at end
 * <p>
 * Turn sequence for each penguin:
 * 1. Check if penguin is eliminated (position == null) - skip if yes
 * 2. Check if penguin is stunned - skip turn and clear stun flag
 * 3. Ask to use special ability (player) or decide randomly (AI)
 * 4. Execute special ability if chosen
 * 5. Ask for movement direction (player) or choose randomly (AI)
 * 6. Execute slide in chosen direction
 * 7. Render updated grid state
 *
 * @author CENG211 14. Group
 * @version 1.0
 * @since 2025-12-08
 */
public class GameManager {
    private static final Logger LOGGER = Logger.getLogger(GameManager.class.getName());

    /**
     * Maximum number of turns in the game
     */
    private static final int MAX_TURNS = 4;

    /**
     * The game grid containing all objects
     */
    private final TerrainGrid grid;

    /**
     * Renders the grid to console
     */
    private final GridRenderer renderer;

    /**
     * Handles player input
     */
    private final InputMaster inputMaster;

    /**
     * List of all penguins in the game (sorted by notation)
     */
    private final List<Penguin> penguins;

    /**
     * Constructs a GameManager with the required game components.
     *
     * @param grid        The terrain grid containing all game objects
     * @param renderer    The grid renderer for displaying game state
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
     * 1. Initialize penguins list from grid
     * 2. Randomly select player penguin
     * 3. Run 4 turns with each penguin taking their turn
     * 4. Display final scoreboard
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
            LOGGER.log(Level.SEVERE, "Critical error in game loop", e);
        }
    }

    /**
     * Randomly selects one penguin to be controlled by the player.
     * All other penguins will be AI-controlled.
     * If no penguins exist, this method does nothing.
     */
    private void selectPlayerPenguin() {
        if (penguins.isEmpty()) {
            LOGGER.log(Level.WARNING, "GameManager Warning: No penguins found for player selection.");
            return;
        }

        try {
            // Random index between 0 and penguins.size()-1
            int randomIndex = RandUtil.getRandomInt(penguins.size());
            Penguin selected = penguins.get(randomIndex);
            selected.setPlayer(true);
            System.out.println("\n" + selected.getNotation() + " is YOUR PENGUIN!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error selecting player penguin", e);
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
            LOGGER.log(Level.SEVERE, "Error calculating scores", e);
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
     * @param p          The penguin taking their turn
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

            // Print penguin type info
            System.out.println(p.getNotation() + " (" + p.getType() + " Penguin) is preparing to move.");

            if (p.isPlayer()) {
                // Player-controlled penguin
                handlePlayerTurn(p);
            } else {
                // AI-controlled penguin
                handleAITurn(p);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing turn", e);
        }
    }

    /**
     * Handles the player's turn with input prompts.
     * Asks the player if they want to use special ability,
     * then asks for movement direction.
     * <p>
     * Special handling for RoyalPenguin:
     * - If ability is used, asks for special move direction
     * - Executes special move before regular slide
     * - Checks for elimination after special move
     *
     * @param p The player's penguin
     */
    private void handlePlayerTurn(Penguin p) {
        boolean useAbility = false;
        try {
            System.out.println("YOUR PENGUIN");

            // Ask if player wants to use special ability
            if (!p.isAbilityUsed()) {
                useAbility = inputMaster.getYesNoInput(
                        "Will " + p.getNotation() +
                                " use its special action? Answer with Y or N: "
                );
            }

            if (useAbility && !p.isAbilityUsed()) {
                System.out.println(p.getNotation() + " chooses to USE its special action.");
                p.specialAbility();
                p.setAbilityUsed(true);

                // Special handling for RoyalPenguin - needs direction for special move
                if (p instanceof RoyalPenguin royal) {
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
                if (p.isAbilityUsed()) {
                    System.out.println(p.getNotation() + " has already used its special action.");
                } else {
                    System.out.println(p.getNotation() + " does NOT to use its special action.");
                }

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
            LOGGER.log(Level.SEVERE, "Error handling player turn", e);
        }
    }

    /**
     * Handles AI penguin turn with SMART decision-making.
     * Priorities: 1. Food, 2. Safe Hazard/Stop, 3. Water/Hole (Last Resort)
     */
    private void handleAITurn(Penguin p) {
        // 1. Evaluate all directions based on where the slide leads
        List<Direction> foodDirs = new ArrayList<>();
        List<Direction> safeDirs = new ArrayList<>(); // Hazards (except holes) or Empty stops
        List<Direction> fatalDirs = new ArrayList<>(); // Water or Holes

        for (Direction d : Direction.values()) {
            MoveOutcome outcome = simulateMove(p, d);
            if (outcome == MoveOutcome.FOOD) {
                foodDirs.add(d);
            } else if (outcome == MoveOutcome.SAFE_OBSTACLE) {
                safeDirs.add(d);
            } else {
                fatalDirs.add(d);
            }
        }

        // 2. Choose the best direction based on priority
        Direction chosenDir;
        MoveOutcome expectedOutcome;

        if (!foodDirs.isEmpty()) {
            // Priority 1: Food
            chosenDir = foodDirs.get(RandUtil.getRandomInt(foodDirs.size()));
            expectedOutcome = MoveOutcome.FOOD;
        } else if (!safeDirs.isEmpty()) {
            // Priority 2: Safe Obstacle (Hazards, Walls, Penguins)
            chosenDir = safeDirs.get(RandUtil.getRandomInt(safeDirs.size()));
            expectedOutcome = MoveOutcome.SAFE_OBSTACLE;
        } else {
            // Priority 3: Water/Death (No choice)
            // Even if fatal, we must choose one
            if (!fatalDirs.isEmpty()) {
                chosenDir = fatalDirs.get(RandUtil.getRandomInt(fatalDirs.size()));
            } else {
                // Should theoretically not happen if lists cover all 4 directions, but fallback
                chosenDir = RandUtil.getRandomDirection();
            }
            expectedOutcome = MoveOutcome.BAD_WATER_OR_HOLE;
        }

        // 3. Determine Ability Usage
        boolean useAbility = false;

        if (p instanceof RockhopperPenguin) {
            // Rock hopper Logic: Exception to the 30% rule.
            // "The first time they decide to move in the direction of a hazard, they will automatically use their action."
            if (!p.isAbilityUsed() && isFacingHazard(p, chosenDir)) {
                useAbility = true;
                System.out.println(p.getNotation() + " will automatically USE its special action because it faces a hazard.");
            }
        } else {
            // Standard Logic: 30% chance for others (King, Emperor, Royal)
            if (!p.isAbilityUsed()) {
                useAbility = RandUtil.getRandomInt(10) < 3; // 30% chance (0, 1, 2 out of 0-9)
            }
        }

        // 4. Execute Ability Logic
        if (useAbility && !p.isAbilityUsed()) {
            // Print usage message (Rockhopper already printed its specific reason above if triggered)
            if (!(p instanceof RockhopperPenguin)) {
                System.out.println(p.getNotation() + " chooses to USE its special action.");
            }

            p.specialAbility();

            // Special handling for RoyalPenguin AI (Single Step)
            if (p instanceof RoyalPenguin royal) {
                // Royal AI Logic: "random direction that does not lead them to a Hazard or falling to water"
                Direction royalDir = getSafeRoyalMove(royal);
                royal.performSpecialMove(grid, royalDir);

                // Check if penguin was eliminated during special move
                if (p.getPosition() == null) {
                    System.out.println("*** " + p.getNotation() + " IS REMOVED FROM THE GAME!");
                    return;
                }
            }
        } else {
            // Match PDF format for non-usage
            // Rockhopper only prints this if it didn't trigger the auto-jump
            System.out.println(p.getNotation() + " does NOT use its special action.");
        }

        // 5. Execute Slide
        System.out.println(p.getNotation() + " chooses to move " + chosenDir + ".");
        p.slide(grid, chosenDir);

        // Check if penguin was eliminated after slide
        if (p.getPosition() == null) {
            System.out.println("*** " + p.getNotation() + " IS REMOVED FROM THE GAME!");
        }
    }

    /**
     * Simulates a slide in a direction to see what the penguin would hit.
     * Used to determine AI priorities.
     */
    private MoveOutcome simulateMove(Penguin p, Direction d) {
        int cx = p.getPosition().getX();
        int cy = p.getPosition().getY();

        // Simulate sliding loop
        while (true) {
            switch (d) {
                case UP -> cy--;
                case DOWN -> cy++;
                case LEFT -> cx--;
                case RIGHT -> cx++;
            }

            // Check boundaries
            if (cx < 0 || cy < 0 || cx >= TerrainGrid.GRID_SIZE || cy >= TerrainGrid.GRID_SIZE) {
                return MoveOutcome.BAD_WATER_OR_HOLE; // Falls in water
            }

            // Using fully qualified objects since Food/HoleInIce logic is internal
            interfaces.ITerrainObject obj = grid.getObjectAt(new Position(cx, cy));

            switch (obj) {
                case null -> {
                    // Empty space: do nothing, loop will naturally repeat
                }
                case Food food -> {
                    return MoveOutcome.FOOD; // Found food!
                }
                case models.hazards.HoleInIce hole -> {
                    if (!hole.isPlugged()) {
                        return MoveOutcome.BAD_WATER_OR_HOLE; // Unplugged hole is fatal
                    }
                    // If plugged: do nothing, loop repeats (treat as empty ice)
                }
                default -> {
                    // Hit a Wall (Hazard), IceBlock, SeaLion or another Penguin
                    // This is considered a "Safe Stop" compared to water
                    return MoveOutcome.SAFE_OBSTACLE;
                }
            }
        }
    }

    /**
     * Checks if the *immediate* obstacle in the sliding path is a Hazard.
     * Used specifically for Rockhopper logic.
     */
    private boolean isFacingHazard(Penguin p, Direction d) {
        int cx = p.getPosition().getX();
        int cy = p.getPosition().getY();

        // Trace path until we hit something or fall off
        while (true) {
            switch (d) {
                case UP -> cy--;
                case DOWN -> cy++;
                case LEFT -> cx--;
                case RIGHT -> cx++;
            }
            if (cx < 0 || cy < 0 || cx >= TerrainGrid.GRID_SIZE || cy >= TerrainGrid.GRID_SIZE) return false;

            interfaces.ITerrainObject obj = grid.getObjectAt(new Position(cx, cy));

            switch (obj) {
                case null -> {
                    continue;
                }
                case Food food -> {
                    continue;
                }

                // If it's a hazard (and not a plugged hole, handled generally as hazard here)
                case interfaces.IHazard iHazard -> {
                    return true;
                }

                // Penguins are not hazards for jumping purposes
                case Penguin penguin -> {
                    return false;
                }
                default -> {
                }
            }

            return false;
        }
    }

    /**
     * Determines a safe single-step move for Royal Penguin AI.
     * "random direction that does not lead them to a Hazard or falling to water"
     */
    private Direction getSafeRoyalMove(RoyalPenguin p) {
        List<Direction> safeDirs = new ArrayList<>();

        for (Direction d : Direction.values()) {
            int nx = p.getPosition().getX();
            int ny = p.getPosition().getY();
            switch (d) {
                case UP -> ny--;
                case DOWN -> ny++;
                case LEFT -> nx--;
                case RIGHT -> nx++;
            }

            // Check bounds (Water)
            if (nx < 0 || ny < 0 || nx >= TerrainGrid.GRID_SIZE || ny >= TerrainGrid.GRID_SIZE) continue;

            interfaces.ITerrainObject obj = grid.getObjectAt(new Position(nx, ny));

            // Avoid Hazards
            if (obj instanceof interfaces.IHazard) continue;

            // Empty or Food or Penguin (Safe to step onto/interact with safely)
            safeDirs.add(d);
        }

        if (!safeDirs.isEmpty()) {
            return safeDirs.get(RandUtil.getRandomInt(safeDirs.size()));
        }

        // If no safe choice, must pick random (as per "unless they have no other choice")
        return RandUtil.getRandomDirection();
    }

    /**
     * Collects all penguins from the grid and sorts them by notation (P1, P2, P3).
     * This ensures turns are processed in correct order regardless of
     * where penguins were spawned on the grid.
     * <p>
     * Process:
     * 1. Clear existing penguin list
     * 2. Scan entire grid for penguins
     * 3. Add found penguins to list
     * 4. Sort by notation (P1 < P2 < P3)
     */
    private void sortPenguins() {
        try {
            penguins.clear();

            // Scan entire grid for penguins
            for (int y = 0; y < TerrainGrid.GRID_SIZE; y++) {
                for (int x = 0; x < TerrainGrid.GRID_SIZE; x++) {
                    interfaces.ITerrainObject object = grid.getObjectAt(new Position(x, y));
                    if (object instanceof Penguin) {
                        penguins.add((Penguin) object);
                    }
                }
            }

            // Sort by notation (P1, P2, P3)
            penguins.sort(Comparator.comparing(Penguin::getNotation));

            if (penguins.isEmpty()) {
                LOGGER.log(Level.WARNING, "GameManager Warning: No penguins found on grid!");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sorting penguins", e);
        }
    }

    /**
     * Enum to classify the result of a move simulation.
     */
    private enum MoveOutcome {
        FOOD,
        SAFE_OBSTACLE,     // Hits wall, penguin, or non-fatal hazard
        BAD_WATER_OR_HOLE  // Dies
    }
}