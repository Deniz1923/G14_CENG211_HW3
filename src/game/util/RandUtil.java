package game.util;

import enums.Direction;
import enums.FoodType;
import enums.HazardType;
import enums.PenguinType;
import java.security.SecureRandom;

/**
 * Utility class for generating cryptographically secure random values.
 * This class provides methods to generate random game elements including
 * penguins, hazards, food items, directions, and numeric values.
 *
 * Uses SecureRandom instead of standard java.util.Random
 * for better randomness and security. This is important for fairness in
 * game mechanics and prevents predictable patterns.
 *
 * All methods are static and thread-safe. The class caches enum value
 * arrays for performance optimization.
 *
 * Random distributions:
 * - All penguin types: equal probability (25% each)
 * - All hazard types: equal probability (25% each)
 * - All food types: equal probability (20% each)
 * - Food weights: uniform distribution 1-5 units
 * - Directions: equal probability (25% each)
 *
 * @author CENG211 Group
 * @version 1.0
 * @since 2025-12-08
 */
public class RandUtil {
  /** Secure random number generator for all random operations */
  private static final SecureRandom random = new SecureRandom();

  // Cache enum values arrays for performance
  /** Cached array of all penguin types */
  private static final PenguinType[] PENGUIN_TYPES = PenguinType.values();

  /** Cached array of all direction values */
  private static final Direction[] DIRECTIONS = Direction.values();

  /** Cached array of all food types */
  private static final FoodType[] FOOD_TYPES = FoodType.values();

  /** Cached array of all hazard types */
  private static final HazardType[] HAZARD_TYPES = HazardType.values();

  /**
   * Private constructor to prevent instantiation.
   * This is a utility class with only static methods.
   */
  private RandUtil() {
    throw new UnsupportedOperationException(
            "RandUtil is a utility class and should not be instantiated."
    );
  }

  /**
   * Generates a random food weight between 1 and 5 units (inclusive).
   * All weights have equal probability of being selected.
   *
   * Weight distribution:
   * - 1 unit: 20%
   * - 2 units: 20%
   * - 3 units: 20%
   * - 4 units: 20%
   * - 5 units: 20%
   *
   * @return A random integer between 1 and 5 (inclusive)
   */
  public static int getFoodWeight() {
    try {
      // nextInt(1, 6) generates values from 1 to 5 inclusive
      return random.nextInt(1, 6);
    } catch (Exception e) {
      System.err.println("Error generating food weight: " + e.getMessage());
      return 3; // Default to middle weight
    }
  }

  /**
   * Generates a random food type.
   * All five food types have equal probability (20% each).
   *
   * Possible returns:
   * - FoodType.KRILL
   * - FoodType.CRUSTACEAN
   * - FoodType.ANCHOVY
   * - FoodType.SQUID
   * - FoodType.MACKEREL
   *
   * @return A randomly selected FoodType
   */
  public static FoodType getRandomFood() {
    try {
      return FOOD_TYPES[random.nextInt(FOOD_TYPES.length)];
    } catch (Exception e) {
      System.err.println("Error generating random food: " + e.getMessage());
      return FoodType.KRILL; // Default fallback
    }
  }

  /**
   * Generates a random direction.
   * All four directions have equal probability (25% each).
   *
   * Possible returns:
   * - Direction.UP
   * - Direction.DOWN
   * - Direction.LEFT
   * - Direction.RIGHT
   *
   * Used for AI penguin movement decisions.
   *
   * @return A randomly selected Direction
   */
  public static Direction getRandomDirection() {
    try {
      return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
    } catch (Exception e) {
      System.err.println("Error generating random direction: " + e.getMessage());
      return Direction.UP; // Default fallback
    }
  }

  /**
   * Generates a random penguin type.
   * All four penguin types have equal probability (25% each).
   *
   * Possible returns:
   * - PenguinType.EMPEROR - Can stop at 3rd square
   * - PenguinType.KING - Can stop at 5th square
   * - PenguinType.ROYAL - Can move one square safely
   * - PenguinType.ROCKHOPPER - Can jump over hazards
   *
   * @return A randomly selected PenguinType
   */
  public static PenguinType getRandomPenguin() {
    try {
      return PENGUIN_TYPES[random.nextInt(PENGUIN_TYPES.length)];
    } catch (Exception e) {
      System.err.println("Error generating random penguin: " + e.getMessage());
      return PenguinType.EMPEROR; // Default fallback
    }
  }

  /**
   * Generates a random hazard type.
   * All four hazard types have equal probability (25% each).
   *
   * Possible returns:
   * - HazardType.LIGHT_ICE_BLOCK - Can slide, stuns penguins
   * - HazardType.HEAVY_ICE_BLOCK - Immovable, removes food
   * - HazardType.SEA_LION - Can slide, bounces penguins
   * - HazardType.HOLE_IN_ICE - Eliminates penguins
   *
   * @return A randomly selected HazardType
   */
  public static HazardType getRandomHazard() {
    try {
      return HAZARD_TYPES[random.nextInt(HAZARD_TYPES.length)];
    } catch (Exception e) {
      System.err.println("Error generating random hazard: " + e.getMessage());
      return HazardType.HEAVY_ICE_BLOCK; // Default fallback
    }
  }

  /**
   * Generates a random integer between 0 (inclusive) and upper_bound (exclusive).
   * This is a general-purpose random integer generator used throughout the game.
   *
   * Range: [0, upper_bound]
   *
   * Example usage:
   * int randomIndex = getRandomInt(3);  // Returns 0, 1, or 2
   * int randomX = getRandomInt(10);     // Returns 0-9 for grid position
   *
   * @param upper_bound The exclusive upper limit (must be positive)
   * @return A random integer from 0 to upper_bound-1
   * @throws IllegalArgumentException if upper_bound is not positive
   */
  public static int getRandomInt(int upper_bound) {
    if (upper_bound <= 0) {
      throw new IllegalArgumentException(
              "RandUtil Error: Upper bound must be positive. Received: " + upper_bound
      );
    }

    try {
      return random.nextInt(upper_bound);
    } catch (Exception e) {
      System.err.println("Error generating random int: " + e.getMessage());
      return 0; // Default to zero
    }
  }

  /**
   * Generates a random integer within a specified range (inclusive).
   * Convenience method for generating values in a specific range.
   *
   * Range: [min, max] (both inclusive)
   *
   * Example:
   * int dice = getRandomIntInRange(1, 6);  // Simulates dice roll
   *
   * @param min The minimum value (inclusive)
   * @param max The maximum value (inclusive)
   * @return A random integer between min and max
   * @throws IllegalArgumentException if min > max
   */
  public static int getRandomIntInRange(int min, int max) {
    if (min > max) {
      throw new IllegalArgumentException(
              "RandUtil Error: Min (" + min + ") cannot be greater than max (" + max + ")."
      );
    }

    try {
      return random.nextInt(min, max + 1);
    } catch (Exception e) {
      System.err.println("Error generating random int in range: " + e.getMessage());
      return min; // Default to minimum value
    }
  }

  /**
   * Generates a random boolean value.
   * Has 50% chance of returning true, 50% chance of returning false.
   *
   * @return true or false with equal probability
   */
  public static boolean getRandomBoolean() {
    try {
      return random.nextBoolean();
    } catch (Exception e) {
      System.err.println("Error generating random boolean: " + e.getMessage());
      return false; // Default to false
    }
  }

  /**
   * Returns true with the specified probability.
   * Useful for implementing percentage-based decisions (like 30% AI ability use).
   *
   * Example:
   * if (getRandomBooleanWithProbability(0.30)) {
   * // This happens 30% of the time
   * }
   *
   * @param probability The probability of returning true (0.0 to 1.0)
   * @return true with given probability, false otherwise
   * @throws IllegalArgumentException if probability not in [0.0, 1.0]
   */
  public static boolean getRandomBooleanWithProbability(double probability) {
    if (probability < 0.0 || probability > 1.0) {
      throw new IllegalArgumentException(
              "RandUtil Error: Probability must be between 0.0 and 1.0. Received: " +
                      probability
      );
    }

    try {
      return random.nextDouble() < probability;
    } catch (Exception e) {
      System.err.println("Error generating probability boolean: " + e.getMessage());
      return false;
    }
  }
}