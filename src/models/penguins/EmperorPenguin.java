package models.penguins;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import models.Food;
import models.Position;

public class EmperorPenguin extends Penguin {
    private boolean useAbilityThisTurn = false;
    private int targetSquare = 3;

    public EmperorPenguin(Position position) {
        super(PenguinType.EMPEROR, position);
    }

    /**
     * When sliding they can choose to stop at the third square they slide into. If the direction they
     * choose has less than three free squares, this ability is still considered used.
     */
    @Override
    public void specialAbility() {
        if (!isAbilityUsed()) {
            useAbilityThisTurn = true;
            setAbilityUsed(true);
            System.out.println(getNotation() + " will stop at the 3rd square!");
        }
    }

    @Override
    public void slide(TerrainGrid grid, Direction direction) {
        if (direction == null || grid == null) {
            return;
        }

        if (useAbilityThisTurn) {
            slideWithAbility(grid, direction, targetSquare);
            useAbilityThisTurn = false;
        } else {
            super.slide(grid, direction);
        }
    }

    private void slideWithAbility(TerrainGrid grid, Direction direction, int stopAt) {
        System.out.println(getNotation() + " starts sliding " + getDirectionName(direction) + "!");

        int stepCount = 0;
        boolean isMoving = true;

        while (isMoving && stepCount < stopAt) {
            stepCount++;

            int nextY = getPosition().getY();
            int nextX = getPosition().getX();

            switch (direction) {
                case UP: nextY--; break;
                case DOWN: nextY++; break;
                case LEFT: nextX--; break;
                case RIGHT: nextX++; break;
            }

            Position nextPos = new Position(nextX, nextY);

            if (nextX < 0 || nextY < 0 || nextY >= 10 || nextX >= 10) {
                System.out.println(getNotation() + " falls into the water!");
                grid.removeObject(getPosition());
                setPosition(null);
                return;
            }

            var obstacle = grid.getObjectAt(nextPos);

            if (obstacle == null) {
                updatePositionOnGrid(grid, nextPos);
                if (stepCount == stopAt) {
                    System.out.println(getNotation() + " stops at an empty square using its special action.");
                    isMoving = false;
                }
            } else if (obstacle instanceof Food food) {
                grid.removeObject(nextPos);
                updatePositionOnGrid(grid, nextPos);
                pickupFood(food);
                isMoving = false;
            } else {
                // Hit something before reaching target
                if (stepCount < stopAt) {
                    System.out.println(getNotation() + " couldn't reach the 3rd square.");
                }
                // Handle collision normally
                super.slide(grid, direction);
                return;
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