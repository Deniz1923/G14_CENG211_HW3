import game.IcyTerrain;

public class SlidingPuzzleApp {
  /** This is the main entry point of the whole game */
  public static void main(String[] args) {
    IcyTerrain terrain = null;
    try {
      terrain = new IcyTerrain();
    } catch (Exception e) {
      System.err.println("An unexpected error occurred during the game execution.");
      System.err.println("Error details: " + e.getMessage());
    } finally {
      // Ensure resources are closed even if an error occurs
      if (terrain != null) {
        try {
          terrain.closeInputMaster();
        } catch (Exception e) {
          System.err.println("Error closing resources: " + e.getMessage());
        }
      }
    }
  }
}
