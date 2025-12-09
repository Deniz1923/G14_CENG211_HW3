package models.penguins;

import static game.TerrainGrid.GRID_SIZE;

import enums.Direction;
import enums.PenguinType;
import game.TerrainGrid;
import interfaces.IHazard;
import interfaces.ITerrainObject;
import java.util.ArrayList;
import java.util.List;
import models.Food;
import models.Position;
import models.hazards.HoleInIce;
import models.hazards.SeaLion;

public abstract class Penguin implements ITerrainObject {
    private final ArrayList<Food> inventory;
    private final PenguinType type;
    private Position position;
    private int carriedWeight = 0;
    private boolean stunned = false;
    private String penguinID = "??";
    private boolean isPlayer = false;
    private boolean abilityUsed = false;

    public Penguin(PenguinType penguinType, Position position) {
        if (penguinType == null) {
            throw new IllegalArgumentException("Penguin Error: Type cannot be null.");
        }
        if (position == null) {
            throw new IllegalArgumentException("Penguin Error: Initial Position cannot be null.");
        }

        this.type = penguinType;
        this.position = position;
        this.inventory = new ArrayList<>();
    }

    public Penguin(PenguinType penguinType) {
        this(penguinType, new Position());
    }

    public void setPenguinID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Penguin Error: ID cannot be null or empty.");
        }
        this.penguinID = id;
    }

    public void removeLightestFood() {
        if (!inventory.isEmpty()) {
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).getWeight() < min) {
                    min = inventory.get(i).getWeight();
                    minIndex = i;
                }
            }
            Food removed = inventory.remove(minIndex);
            System.out.println(getNotation() + " loses " + removed.getNotation() + " (" + removed.getWeight() + " units) due to collision!");
        }
    }

    public int measureInventory() {
        int sum = 0;
        for (Food food : inventory) {
            sum += food.getWeight();
        }
        carriedWeight = sum;
        return sum;
    }

    public abstract void specialAbility();

    public void pickupFood(Food food) {
        if (food != null) {
            inventory.add(food);
            System.out.println(getNotation() + " takes the " + food.getType().toString() + " on the ground. (Weight=" + food.getWeight() + " units)");
        }
    }

    public void slide(TerrainGrid grid, Direction direction) {
        if (direction == null || grid == null) {
            return;
        }
        System.out.println(getNotation() + " starts sliding " + getDirectionName(direction) + "!");

        boolean isMoving = true;

        while (isMoving) {
            int nextY = position.getY();
            int nextX = position.getX();

            switch (direction) {
                case UP: nextY--; break;
                case DOWN: nextY++; break;
                case LEFT: nextX--; break;
                case RIGHT: nextX++; break;
            }

            Position nextPos = new Position(nextX, nextY);

            // Check if falling into water
            if (nextX < 0 || nextY < 0 || nextY >= GRID_SIZE || nextX >= GRID_SIZE) {
                System.out.println(getNotation() + " falls into the water!");
                grid.removeObject(position);
                this.position = null;
                isMoving = false;
                break;
            }

            ITerrainObject obstacle = grid.getObjectAt(nextPos);

            if (obstacle == null) {
                // Empty square, continue sliding
                updatePositionOnGrid(grid, nextPos);
            } else if (obstacle instanceof Food) {
                // Food item found
                Food food = (Food) obstacle;
                grid.removeObject(nextPos);
                updatePositionOnGrid(grid, nextPos);
                pickupFood(food);
                isMoving = false;
            } else if (obstacle instanceof Penguin) {
                // Collision with another penguin
                Penguin otherPenguin = (Penguin) obstacle;
                System.out.println(getNotation() + " collides with " + otherPenguin.getNotation() + "!");
                System.out.println(otherPenguin.getNotation() + " starts sliding instead!");
                isMoving = false;
                // The other penguin starts sliding
                otherPenguin.slide(grid, direction);
            } else if (obstacle instanceof SeaLion) {
                // Special case for SeaLion - penguin bounces back
                SeaLion seaLion = (SeaLion) obstacle;
                System.out.println(getNotation() + " collides with " + seaLion.getNotation() + " and bounces back!");

                // Remove SeaLion from current position
                grid.removeObject(seaLion.getPosition());

                // SeaLion starts sliding in original direction
                slideHazard(grid, seaLion, direction);

                // Penguin bounces back (opposite direction)
                Direction oppositeDir = direction.opposite();
                System.out.println(getNotation() + " bounces " + getDirectionName(oppositeDir) + "!");
                isMoving = false;

                // Penguin slides in opposite direction
                slide(grid, oppositeDir);
            } else if (obstacle instanceof IHazard) {
                // Collision with other hazards
                IHazard hazard = (IHazard) obstacle;
                System.out.println(getNotation() + " collides with " + hazard.getNotation() + "!");

                hazard.onCollision(this, grid);

                // Check if penguin is still on grid after collision
                if (this.position == null) {
                    return;
                }

                if (hazard.canSlide()) {
                    // Hazard starts sliding
                    grid.removeObject(hazard.getPosition());
                    slideHazard(grid, hazard, direction);
                }

                isMoving = false;
            }
        }
    }

    private void slideHazard(TerrainGrid grid, IHazard hazard, Direction direction) {
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

            // Check if hazard falls off grid
            if (nextX < 0 || nextY < 0 || nextY >= GRID_SIZE || nextX >= GRID_SIZE) {
                System.out.println(hazard.getNotation() + " falls into the water!");
                hazardMoving = false;
                break;
            }

            ITerrainObject obstacle = grid.getObjectAt(nextPos);

            if (obstacle == null) {
                // Empty square
                hazard.setPosition(nextPos);
                grid.placeObject(nextPos, (ITerrainObject) hazard);
                currentPos = nextPos;
            } else if (obstacle instanceof Food) {
                // Remove food and continue
                System.out.println(hazard.getNotation() + " destroys " + obstacle.getNotation() + "!");
                grid.removeObject(nextPos);
                hazard.setPosition(nextPos);
                grid.placeObject(nextPos, (ITerrainObject) hazard);
                currentPos = nextPos;
            } else if (obstacle instanceof Penguin) {
                // Stop when hitting another penguin
                hazard.setPosition(currentPos);
                grid.placeObject(currentPos, (ITerrainObject) hazard);
                System.out.println(hazard.getNotation() + " collides with " + obstacle.getNotation() + " and stops!");
                hazardMoving = false;
            } else if (obstacle instanceof HoleInIce) {
                // Hazard plugs the hole
                HoleInIce hole = (HoleInIce) obstacle;
                System.out.println(hazard.getNotation() + " falls into " + hole.getNotation() + " and plugs it!");
                hole.plug();
                hazardMoving = false;
            } else {
                // Hit another hazard
                hazard.setPosition(currentPos);
                grid.placeObject(currentPos, (ITerrainObject) hazard);
                hazardMoving = false;
            }
        }
    }

    private String getDirectionName(Direction dir) {
        return switch (dir) {
            case UP -> "UPWARDS";
            case DOWN -> "DOWNWARDS";
            case LEFT -> "to the LEFT";
            case RIGHT -> "to the RIGHT";
        };
    }

    public String getType() {
        return type.toString().toLowerCase();
    }

    public int getCarriedWeight() {
        measureInventory();
        return carriedWeight;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void setStunned(boolean b) {
        stunned = b;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public boolean isAbilityUsed() {
        return abilityUsed;
    }

    public void setAbilityUsed(boolean used) {
        this.abilityUsed = used;
    }

    public List<Food> getInventory() {
        return inventory;
    }

    public Position getPosition() {
        if (position == null) {
            return null;
        }
        return position.getPosition();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private void updatePositionOnGrid(TerrainGrid grid, Position newPosition) {
        grid.removeObject(position);
        this.position = newPosition;
        grid.placeObject(newPosition, this);
    }

    @Override
    public String getNotation() {
        return penguinID;
    }

    @Override
    public String getSymbol() {
        return penguinID;
    }
}