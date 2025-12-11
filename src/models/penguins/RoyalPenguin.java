package models.penguins;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;

/**
 * Represents a Royal Penguin that can safely move one square before sliding.
 * This penguin has the special ability to move exactly one square in any
 * cardinal direction (horizontally or vertically) before they begin sliding.
 *
 * <p>Special ability characteristics:</p>
 * <ul>
 *   <li>Can move one square in any direction (UP, DOWN, LEFT, RIGHT)</li>
 *   <li>Movement happens before the slide begins</li>
 *   <li>Can collect food during the special move</li>
 *   <li>Can accidentally step out of grid and fall into water</li>
 *   <li>Can collide with hazards (collision effects apply)</li>
 *   <li>Used once per game before sliding</li>
 * </ul>
 *
 * <p>The special move is considered "safe" in that it's controlled, but
 * the penguin must still be careful not to step into dangerous squares.</p>
 *
 * @author CENG211 14. Group
 * @version 1.0
 * @since 2025-12-08
 */
public class RoyalPenguin extends Penguin {
    /** Flag indicating if ability should be used this turn */
    private boolean useAbilityThisTurn = false;

    /**
     * Constructs a RoyalPenguin at the specified position.
     *
     * @param position The initial position on the grid edge
     * @throws IllegalArgumentException if position is null
     */
    public RoyalPenguin(Position position) {
        super(PenguinType.ROYAL, position);
    }

    /**
     * Activates the special one-square movement ability.
     * The penguin will move exactly one square before sliding.
     * This ability can only be used once per game.
     *
     * <p>After activation, the penguin must specify a direction for the
     * special move (handled by performSpecialMove method). The move is
     * executed before the regular slide begins.</p>
     */
    @Override
    public void specialAbility() {
        if (!isAbilityUsed()) {
            useAbilityThisTurn = true;
            setAbilityUsed(true);
        }
    }

    /**
     * Performs the special one-square movement in the specified direction.
     * This must be called after specialAbility() is activated and before
     * the regular slide begins.
     *
     * <p>Movement outcomes:</p>
     * <ul>
     *   <li>Empty square - Move successfully</li>
     *   <li>Food item - Collect food and move there</li>
     *   <li>Hazard - Move there and collision effects apply</li>
     *   <li>Out of bounds - Fall into water (eliminated)</li>
     * </ul>
     *
     * <p>Unlike regular sliding, this move is exactly one square and
     * stops regardless of what's there (except elimination cases).</p>
     *
     * @param grid The terrain grid
     * @param direction The direction to move (UP, DOWN, LEFT, RIGHT)
     * @throws IllegalArgumentException if grid or direction is null
     */
    public void performSpecialMove(TerrainGrid grid, Direction direction) {
        if (!useAbilityThisTurn) {
            return;
        }

        if (direction == null) {
            throw new IllegalArgumentException(
                    "RoyalPenguin Error: Direction cannot be null for special move."
            );
        }
        if (grid == null) {
            throw new IllegalArgumentException(
                    "RoyalPenguin Error: TerrainGrid cannot be null for special move."
            );
        }

        try {
            System.out.println(getNotation() + " moves one square " +
                    getDirectionName(direction) + ".");

            int nextY = getPosition().getY();
            int nextX = getPosition().getX();

            // Calculate the single-square move
            switch (direction) {
                case UP: nextY--; break;
                case DOWN: nextY++; break;
                case LEFT: nextX--; break;
                case RIGHT: nextX++; break;
            }

            Position nextPos = new Position(nextX, nextY);

            // Check if falling into water
            if (nextX < 0 || nextY < 0 || nextY >= 10 || nextX >= 10) {
                System.out.println(getNotation() +
                        " falls into the water while using special ability!");
                grid.removeObject(getPosition());
                setPosition(null);
                useAbilityThisTurn = false;
                return;
            }

            ITerrainObject obstacle = grid.getObjectAt(nextPos);

            if (obstacle == null) {
                // Empty square - just move
                updatePositionOnGrid(grid, nextPos);
            } else if (obstacle instanceof Food food) {
                // Collect food and move there
                grid.removeObject(nextPos);
                updatePositionOnGrid(grid, nextPos);
                pickupFood(food);
            } else {
                // Hit something (hazard or penguin)
                // Move there but consequences apply
                updatePositionOnGrid(grid, nextPos);

                // Apply collision effects if it's a hazard
                if (obstacle instanceof interfaces.IHazard hazard) {
                    hazard.onCollision(this, grid);
                }
            }

            useAbilityThisTurn = false;
        } catch (Exception e) {
            System.err.println("Error during RoyalPenguin special move: " + e.getMessage());
            useAbilityThisTurn = false;
        }
    }

    /**
     * Checks if the special ability should be used this turn.
     * This is used by the game manager to determine if it needs to ask
     * for a direction for the special move.
     *
     * @return true if special move should be performed, false otherwise
     */
    public boolean shouldUseAbilityThisTurn() {
        return useAbilityThisTurn;
    }

    /**
     * Updates the penguin's position on the grid.
     * Removes penguin from old position and places at new position.
     *
     * @param grid The terrain grid
     * @param newPosition The new position to move to
     */
    private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
        grid.removeObject(getPosition());
        setPosition(newPosition);
        grid.placeObject(newPosition, this);
    }

    /**
     * Returns a readable direction name for display.
     * Formatted slightly differently than standard penguin directions.
     *
     * @param dir The direction enum
     * @return The formatted direction string
     */
    private String getDirectionName(Direction dir) {
        return switch (dir) {
            case UP -> "to the UP";
            case DOWN -> "to the DOWN";
            case LEFT -> "to the LEFT";
            case RIGHT -> "to the RIGHT";
        };
    }

    /**
     * Returns a string representation of this Royal Penguin.
     *
     * @return A descriptive string including type, ID, and position
     */
    @Override
    public String toString() {
        String posStr = (getPosition() != null) ?
                getPosition().displayPosition() : "Eliminated";
        return "Royal Penguin (" + getNotation() + ") at " + posStr +
                (useAbilityThisTurn ? " [Special Move Ready]" : "");
    }
}