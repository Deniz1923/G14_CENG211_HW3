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
 * EmperorPenguin class represents a penguin that can stop at the third square while sliding.
 * This penguin has the special ability to choose to stop at the third square they slide into.
 * If the direction has less than three free squares, the ability is still considered used.
 */
public class EmperorPenguin extends Penguin {
    private static final int TARGET_SQUARE = 3;
    private boolean useAbilityThisTurn = false;

    /**
     * Constructs an EmperorPenguin at the specified position.
     *
     * @param position The initial position of the penguin
     * @throws IllegalArgumentException if position is null
     */
    public EmperorPenguin(Position position) {
        super(PenguinType.EMPEROR, position);
    }

    /**
     * Activates the special ability to stop at the third square.
     * This ability can only be used once per game.
     * After activation, the penguin will attempt to stop at the third square
     * when sliding in the next move.
     */
    @Override
    public void specialAbility() {
        if (!isAbilityUsed()) {
            useAbilityThisTurn = true;
            setAbilityUsed(true);
            System.out.println(getNotation() + " will stop at the 3rd square!");
        }
    }

    /**
     * Slides the penguin in the specified direction.
     * If the special ability is active, attempts to stop at the third square.
     * Otherwise, uses the standard sliding behavior.
     *
     * @param grid      The terrain grid
     * @param direction The direction to slide
     * @throws IllegalArgumentException if grid or direction is null
     */
    @Override
    public void slide(TerrainGrid grid, Direction direction) {
        if (direction == null || grid == null) {
            throw new IllegalArgumentException("Grid and direction cannot be null");
        }

        try {
            if (useAbilityThisTurn) {
                slideWithAbility(grid, direction, TARGET_SQUARE);
                useAbilityThisTurn = false;
            } else {
                super.slide(grid, direction);
            }
        } catch (Exception e) {
            System.err.println("Error during Emperor Penguin slide: " + e.getMessage());
            useAbilityThisTurn = false;
        }
    }

    /**
     * Slides the penguin with the special ability active.
     * Attempts to stop at the specified target square.
     *
     * @param grid      The terrain grid
     * @param direction The direction to slide
     * @param stopAt    The target square number to stop at
     */
    private void slideWithAbility(TerrainGrid grid, Direction direction, int stopAt) {
        System.out.println(getNotation() + " starts sliding " + getDirectionName(direction) + "!");

        int stepCount = 0;
        boolean isMoving = true;

        while (isMoving && stepCount < stopAt) {
            stepCount++;

            int nextY = getPosition().getY();
            int nextX = getPosition().getX();

            switch (direction) {
                case UP -> nextY--;
                case DOWN -> nextY++;
                case LEFT -> nextX--;
                case RIGHT -> nextX++;
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

            if (obstacle == null) {
                // Empty square - continue sliding
                updatePositionOnGrid(grid, nextPos);
                if (stepCount == stopAt) {
                    System.out.println(getNotation() + " stops at an empty square using its special action.");
                    isMoving = false;
                }
            } else if (obstacle instanceof Food) {
                // Food found - collect and stop
                Food food = (Food) obstacle;
                grid.removeObject(nextPos);
                updatePositionOnGrid(grid, nextPos);
                pickupFood(food);
                isMoving = false;
            } else {
                // Hit an obstacle before reaching target
                if (stepCount < stopAt) {
                    System.out.println(getNotation() + " couldn't reach the 3rd square.");
                }
                handleObstacleCollision(grid, obstacle, direction);
                isMoving = false;
            }
        }
    }

    /**
     * Handles collision with obstacles (penguins or hazards).
     *
     * @param grid      The terrain grid
     * @param obstacle  The obstacle that was hit
     * @param direction The direction of movement
     */
    private void handleObstacleCollision(TerrainGrid grid, ITerrainObject obstacle, Direction direction) {
        try {
            if (obstacle instanceof Penguin) {
                Penguin otherPenguin = (Penguin) obstacle;
                System.out.println(getNotation() + " collides with " + otherPenguin.getNotation() + "!");
                System.out.println(otherPenguin.getNotation() + " starts sliding instead!");
                otherPenguin.slide(grid, direction);
            } else if (obstacle instanceof IHazard) {
                IHazard hazard = (IHazard) obstacle;
                System.out.println(getNotation() + " collides with " + hazard.getNotation() + "!");
                hazard.onCollision(this, grid);

                if (getPosition() != null && hazard.canSlide()) {
                    grid.removeObject(hazard.getPosition());
                    slideHazardHelper(grid, hazard, direction);
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling obstacle collision: " + e.getMessage());
        }
    }

    /**
     * Slides a hazard in the specified direction after collision.
     *
     * @param grid      The terrain grid
     * @param hazard    The hazard to slide
     * @param direction The direction to slide the hazard
     */
    private void slideHazardHelper(TerrainGrid grid, IHazard hazard, Direction direction) {
        Position currentPos = hazard.getPosition();
        boolean hazardMoving = true;

        while (hazardMoving) {
            int nextY = currentPos.getY();
            int nextX = currentPos.getX();

            switch (direction) {
                case UP -> nextY--;
                case DOWN -> nextY++;
                case LEFT -> nextX--;
                case RIGHT -> nextX++;
            }

            Position nextPos = new Position(nextX, nextY);

            if (nextX < 0 || nextY < 0 || nextY >= 10 || nextX >= 10) {
                System.out.println(hazard.getNotation() + " falls into the water!");
                hazardMoving = false;
                break;
            }

            ITerrainObject obstacle = grid.getObjectAt(nextPos);

            if (obstacle == null) {
                hazard.setPosition(nextPos);
                grid.placeObject(nextPos, (ITerrainObject) hazard);
                currentPos = nextPos;
            } else if (obstacle instanceof Food) {
                System.out.println(hazard.getNotation() + " destroys " + obstacle.getNotation() + "!");
                grid.removeObject(nextPos);
                hazard.setPosition(nextPos);
                grid.placeObject(nextPos, (ITerrainObject) hazard);
                currentPos = nextPos;
            } else if (obstacle instanceof HoleInIce) {
                HoleInIce hole = (HoleInIce) obstacle;
                System.out.println(hazard.getNotation() + " falls into " + hole.getNotation() + " and plugs it!");
                hole.plug();
                hazardMoving = false;
            } else {
                hazard.setPosition(currentPos);
                grid.placeObject(currentPos, (ITerrainObject) hazard);
                hazardMoving = false;
            }
        }
    }

    /**
     * Updates the penguin's position on the grid.
     *
     * @param grid        The terrain grid
     * @param newPosition The new position
     */
    private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
        grid.removeObject(getPosition());
        setPosition(newPosition);
        grid.placeObject(newPosition, this);
    }

    /**
     * Returns the direction name in a readable format.
     *
     * @param dir The direction
     * @return The formatted direction name
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