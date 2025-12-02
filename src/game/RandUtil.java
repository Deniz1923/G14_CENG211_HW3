package game;

import enums.Direction;
import enums.PenguinType;
import java.security.SecureRandom;

/** Use for anything Random */
public class RandUtil {
  private static final SecureRandom random = new SecureRandom();

  // Cache the values array for performance
  private static final PenguinType[] PENGUIN_TYPES = PenguinType.values();
  private static final Direction[] DIRECTIONS = Direction.values();

  public static int getFoodWeight() {
    return random.nextInt(1, 6);
  }

  public static PenguinType getFoodType() {
    return PENGUIN_TYPES[random.nextInt(PENGUIN_TYPES.length)];
  }

  public static Direction randomDirection() {
    return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
  }
}
