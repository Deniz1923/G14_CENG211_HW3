package game.util;

import enums.Direction;
import enums.FoodType;
import enums.HazardType;
import enums.PenguinType;
import java.security.SecureRandom;

/** Use for anything Random */
public class RandUtil {
  private static final SecureRandom random = new SecureRandom();

  // Cache the values array for performance
  private static final PenguinType[] PENGUIN_TYPES = PenguinType.values();
  private static final Direction[] DIRECTIONS = Direction.values();
  private static final FoodType[] FOOD_TYPES = FoodType.values();
  private static final HazardType[] HAZARD_TYPES = HazardType.values();

  public static int getFoodWeight() {
    return random.nextInt(1, 6);
  }

  public static FoodType getRandomFood() {
    return FOOD_TYPES[random.nextInt(FOOD_TYPES.length)];
  }

  public static Direction getRandomDirection() {
    return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
  }

  public static PenguinType getRandomPenguin() {
    return PENGUIN_TYPES[random.nextInt(PENGUIN_TYPES.length)];
  }

  public static HazardType getRandomHazard() {
    return HAZARD_TYPES[random.nextInt(HAZARD_TYPES.length)];
  }

  public static int getRandomInt(int upper_bound) {
    return random.nextInt(upper_bound);
  }
}
