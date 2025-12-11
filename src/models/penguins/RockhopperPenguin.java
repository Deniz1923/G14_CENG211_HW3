package models.penguins;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.IHazard;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;
import models.hazards.HoleInIce;

/**
 * Represents a Rockhopper Penguin that can jump over hazards.
 * This penguin has the special ability to prepare to jump over one hazard
 * in their path before sliding. The jump allows them to skip over a single
 * hazard and land on the square immediately beyond it.
 *
 * <p>Special ability characteristics:</p>
 * <ul>
 *   <li>Can jump over ONE hazard in their sliding path</li>
 *   <li>Must be activated before sliding begins</li>
 *   <li>Can only land on an empty square</li>
 *   <li>If landing square is occupied, jump fails and collision occurs</li>
 *   <li>Ability is wasted if no hazards are in the path</li>
 *   <li>Can still fall into water if jump lands out of bounds</li>
 * </ul>
 *
 * <p>AI behavior: RockhopperPenguins automatically use their ability the
 * first time they move toward a hazard, rather than using the standard 30%
 * chance that other penguins use.</p>
 *
 * @author CENG211 14. Group
 * @version 1.0
 * @since 2025-12-08
 */
public class RockhopperPenguin extends Penguin {
    /** Flag indicating if ability should be used this turn */
    private boolean useAbilityThisTurn = false;

    /** Flag indicating if penguin is ready to jump over a hazard */
    private boolean canJump = false;

    /**
     * Constructs a RockhopperPenguin at the specified position.
     *
     * @param position The initial position on the grid edge
     * @throws IllegalArgumentException if position is null
     */
    public RockhopperPenguin(Position position) {
        super(PenguinType.ROCKHOPPER, position);
    }

    /**
     * Activates the special jumping ability.
     * The penguin prepares to jump over the next hazard encountered.
     * This ability can only be used once per game.
     *
     * <p>Once activated, the penguin will attempt to jump over the first
     * hazard it encounters while sliding. The jump lands one square beyond
     * the hazard, but only if that square is empty.</p>
     *
     * <p>If no hazards are encountered or the landing fails, the ability
     * is still considered used and cannot be activated again.</p>
     */
    @Override
    public void specialAbility() {
        if (!isAbilityUsed()) {
            useAbilityThisTurn = true;
            canJump = true;
            setAbilityUsed(true);
            System.out.println(getNotation() + " prepares to jump over a hazard!");
        }
    }

    /**
     * Slides the penguin in the specified direction with jump capability.
     * If the jump ability is active, the penguin will attempt to jump over
     * the first hazard encountered, landing one square beyond it.
     *
     * <p>Jump mechanics:</p>
     * <ol>
     *   <li>Penguin slides until hitting a hazard</li>
     *   <li>If canJump is true, calculates landing position (one square beyond)</li>
     *   <li>Checks if landing position is valid and empty</li>
     *   <li>Success: Jumps to landing position, ability consumed</li>
     *   <li>Failure: Normal collision occurs, ability wasted</li>
     * </ol>
     *
     * @param grid The terrain grid
     * @param direction The direction to slide (UP, DOWN, LEFT, RIGHT)
     * @throws IllegalArgumentException if grid or direction is null
     */
    @Override
    public void slide(TerrainGrid grid, Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException(
                    "RockhopperPenguin Error: Direction cannot be null."
            );
        }
        if (grid == null) {
            throw new IllegalArgumentException(
                    "RockhopperPenguin Error: TerrainGrid cannot be null."
            );
        }

        try {
            System.out.println(getNotation() + " starts sliding " +
                    getDirectionName(direction) + "!");

            boolean isMoving = true;

            while (isMoving) {
                int nextY = getPosition().getY();
                int nextX = getPosition().getX();

                // Calculate next position
                switch (direction) {
                    case UP: nextY--; break;
                    case DOWN: nextY++; break;
                    case LEFT: nextX--; break;
                    case RIGHT: nextX++; break;
                }

                Position nextPos = new Position(nextX, nextY);

                // Check if falling into water
                if (nextX < 0 || nextY < 0 || nextY >= 10 || nextX >= 10) {
                    System.out.println(getNotation() + " falls into the water!");
                    grid.removeObject(getPosition());
                    setPosition(null);
                    return;
                }

                ITerrainObject obstacle = grid.getObjectAt(nextPos);

                switch (obstacle) {
                    case null ->
                        // Empty square, continue sliding
                            updatePositionOnGrid(grid, nextPos);
                    case Food food -> {
                        // Food found - collect and stop
                        grid.removeObject(nextPos);
                        updatePositionOnGrid(grid, nextPos);
                        pickupFood(food);
                        isMoving = false;
                    }
                    case IHazard hazard when canJump && useAbilityThisTurn -> {
                        // Attempt to jump over the hazard
                        System.out.println(getNotation() + " attempts to jump over " +
                                hazard.getNotation() + "!");

                        // Calculate landing position (one square beyond hazard)
                        int landY = nextY;
                        int landX = nextX;

                        switch (direction) {
                            case UP:
                                landY--;
                                break;
                            case DOWN:
                                landY++;
                                break;
                            case LEFT:
                                landX--;
                                break;
                            case RIGHT:
                                landX++;
                                break;
                        }

                        Position landPos = new Position(landX, landY);

                        // Check if landing position is valid
                        if (landX < 0 || landY < 0 || landY >= 10 || landX >= 10) {
                            System.out.println(getNotation() +
                                    " fails to jump and falls into water!");
                            grid.removeObject(getPosition());
                            setPosition(null);
                            canJump = false;
                            useAbilityThisTurn = false;
                            return;
                        }

                        ITerrainObject landingObstacle = grid.getObjectAt(landPos);

                        if (landingObstacle == null) {
                            // Successful jump!
                            System.out.println(getNotation() + " successfully jumps over " +
                                    hazard.getNotation() + "!");
                            updatePositionOnGrid(grid, landPos);
                            canJump = false;
                            useAbilityThisTurn = false;
                            // Continue sliding from landing position
                        } else {
                            // Landing spot not empty - jump fails
                            System.out.println(getNotation() +
                                    " fails to jump - landing spot is not empty!");
                            hazard.onCollision(this, grid);

                            // If penguin was eliminated, stop
                            if (getPosition() == null) {
                                return;
                            }

                            // Slide the hazard if it can slide
                            if (hazard.canSlide()) {
                                grid.removeObject(hazard.getPosition());
                                slideHazard(grid, hazard, direction);
                            }

                            canJump = false;
                            useAbilityThisTurn = false;
                            isMoving = false;
                        }
                    }
                    case Penguin otherPenguin -> {
                        // Collision with another penguin
                        System.out.println(getNotation() + " collides with " +
                                otherPenguin.getNotation() + "!");
                        System.out.println(otherPenguin.getNotation() +
                                " starts sliding instead!");
                        isMoving = false;
                        otherPenguin.slide(grid, direction);
                    }
                    case IHazard hazard -> {
                        // Normal hazard collision (no jump ability active)
                        System.out.println(getNotation() + " collides with " +
                                hazard.getNotation() + "!");

                        hazard.onCollision(this, grid);

                        // Check if penguin was eliminated
                        if (getPosition() == null) {
                            return;
                        }

                        // Slide the hazard if possible
                        if (hazard.canSlide()) {
                            grid.removeObject(hazard.getPosition());
                            slideHazard(grid, hazard, direction);
                        }

                        isMoving = false;
                    }
                    default -> {
                    }
                }
            }

            // Reset ability flags after slide completes
            useAbilityThisTurn = false;
            canJump = false;
        } catch (Exception e) {
            System.err.println("Error during RockhopperPenguin slide: " + e.getMessage());
            useAbilityThisTurn = false;
            canJump = false;
        }
    }

    /**
     * Helper method to slide a hazard after collision.
     *
     * @param grid The terrain grid
     * @param hazard The hazard to slide
     * @param direction The direction to slide
     */
    private void slideHazard(TerrainGrid grid, IHazard hazard, Direction direction) {
        try {
            Position currentPos = hazard.getPosition();
            boolean hazardMoving = true;

            while (hazardMoving) {
                int nextY = currentPos.getY();
                int nextX = currentPos.getX();

                switch (direction) {
                    case UP: nextY--; break;
                    case DOWN: nextY++; break;
                    case LEFT: nextX--; break;
                    case RIGHT: nextX++; break;
                }

                Position nextPos = new Position(nextX, nextY);

                if (nextX < 0 || nextY < 0 || nextY >= 10 || nextX >= 10) {
                    System.out.println(hazard.getNotation() + " falls into the water!");
                    hazardMoving = false;
                    break;
                }

                ITerrainObject obstacle = grid.getObjectAt(nextPos);

                switch (obstacle) {
                    case null -> {
                        hazard.setPosition(nextPos);
                        grid.placeObject(nextPos, (ITerrainObject) hazard);
                        currentPos = nextPos;
                    }
                    case Food food -> {
                        System.out.println(hazard.getNotation() + " destroys " +
                                obstacle.getNotation() + "!");
                        grid.removeObject(nextPos);
                        hazard.setPosition(nextPos);
                        grid.placeObject(nextPos, (ITerrainObject) hazard);
                        currentPos = nextPos;
                    }
                    case HoleInIce hole -> {
                        System.out.println(hazard.getNotation() + " falls into " +
                                hole.getNotation() + " and plugs it!");
                        hole.plug();
                        hazardMoving = false;
                    }
                    default -> {
                        hazard.setPosition(currentPos);
                        grid.placeObject(currentPos, (ITerrainObject) hazard);
                        hazardMoving = false;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error sliding hazard: " + e.getMessage());
        }
    }

    /**
     * Updates the penguin's position on the grid.
     *
     * @param grid The terrain grid
     * @param newPosition The new position
     */
    private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
        grid.removeObject(getPosition());
        setPosition(newPosition);
        grid.placeObject(newPosition, this);
    }

    /**
     * Returns a readable direction name.
     *
     * @param dir The direction enum
     * @return The formatted direction string
     */
    private String getDirectionName(Direction dir) {
        return switch (dir) {
            case UP -> "UPWARDS";
            case DOWN -> "DOWNWARDS";
            case LEFT -> "to the LEFT";
            case RIGHT -> "to the RIGHT";
        };
    }
}