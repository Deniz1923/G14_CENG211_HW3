package models.penguins;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.IHazard;
import interfaces.ITerrainObject;
import models.Food;
import models.Position;

public class RockhopperPenguin extends Penguin {
    private boolean useAbilityThisTurn = false;
    private boolean canJump = false;

    public RockhopperPenguin(Position position) {
        super(PenguinType.ROCKHOPPER, position);
    }

    /**
     * Before they start sliding, they can prepare to jump over one hazard in their path. This ability
     * is not used automatically and can be wasted if there are no hazards in their path. Also, they
     * can only jump to an empty square. If the square they are about to land is not empty, they fail
     * to jump and collide with the hazard, nevertheless.
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

    @Override
    public void slide(TerrainGrid grid, Direction direction) {
        if (direction == null || grid == null) {
            return;
        }

        System.out.println(getNotation() + " starts sliding " + getDirectionName(direction) + "!");

        boolean isMoving = true;

        while (isMoving) {
            int nextY = getPosition().getY();
            int nextX = getPosition().getX();

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

            if (obstacle == null) {
                updatePositionOnGrid(grid, nextPos);
            } else if (obstacle instanceof Food) {
                Food food = (Food) obstacle;
                grid.removeObject(nextPos);
                updatePositionOnGrid(grid, nextPos);
                pickupFood(food);
                isMoving = false;
            } else if (obstacle instanceof IHazard && canJump && useAbilityThisTurn) {
                // Try to jump over the hazard
                IHazard hazard = (IHazard) obstacle;
                System.out.println(getNotation() + " attempts to jump over " + hazard.getNotation() + "!");

                // Calculate landing position (one square beyond hazard)
                int landY = nextY;
                int landX = nextX;

                switch (direction) {
                    case UP: landY--; break;
                    case DOWN: landY++; break;
                    case LEFT: landX--; break;
                    case RIGHT: landX++; break;
                }

                Position landPos = new Position(landX, landY);

                // Check if landing position is valid and empty
                if (landX < 0 || landY < 0 || landY >= 10 || landX >= 10) {
                    System.out.println(getNotation() + " fails to jump and falls into water!");
                    grid.removeObject(getPosition());
                    setPosition(null);
                    canJump = false;
                    useAbilityThisTurn = false;
                    return;
                }

                ITerrainObject landingObstacle = grid.getObjectAt(landPos);

                if (landingObstacle == null) {
                    // Successful jump
                    System.out.println(getNotation() + " successfully jumps over " + hazard.getNotation() + "!");
                    updatePositionOnGrid(grid, landPos);
                    canJump = false;
                    useAbilityThisTurn = false;
                } else {
                    // Landing spot not empty, fail to jump
                    System.out.println(getNotation() + " fails to jump - landing spot is not empty!");
                    hazard.onCollision(this, grid);

                    if (hazard.canSlide()) {
                        grid.removeObject(hazard.getPosition());
                        slideHazard(grid, hazard, direction);
                    }

                    canJump = false;
                    useAbilityThisTurn = false;
                    isMoving = false;
                }
            } else if (obstacle instanceof Penguin otherPenguin) {
                System.out.println(getNotation() + " collides with " + otherPenguin.getNotation() + "!");
                System.out.println(otherPenguin.getNotation() + " starts sliding instead!");
                isMoving = false;
                otherPenguin.slide(grid, direction);
            } else if (obstacle instanceof IHazard hazard) {
                System.out.println(getNotation() + " collides with " + hazard.getNotation() + "!");

                hazard.onCollision(this, grid);

                if (hazard.canSlide()) {
                    grid.removeObject(hazard.getPosition());
                    slideHazard(grid, hazard, direction);
                }

                isMoving = false;
            }
        }

        useAbilityThisTurn = false;
        canJump = false;
    }

    private void slideHazard(TerrainGrid grid, IHazard hazard, Direction direction) {
        // Similar to Penguin's slideHazard implementation
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
            } else {
                hazard.setPosition(currentPos);
                grid.placeObject(currentPos, (ITerrainObject) hazard);
                hazardMoving = false;
            }
        }
    }

    private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
        grid.removeObject(getPosition());
        setPosition(newPosition);
        grid.placeObject(newPosition, this);
    }

    private String getDirectionName(Direction dir) {
        return switch (dir) {
            case UP -> "UPWARDS";
            case DOWN -> "DOWNWARDS";
            case LEFT -> "to the LEFT";
            case RIGHT -> "to the RIGHT";
        };
    }
}