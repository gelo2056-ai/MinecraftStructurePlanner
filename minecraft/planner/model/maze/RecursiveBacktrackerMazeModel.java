package minecraft.planner.model.maze;

public class RecursiveBacktrackerMazeModel extends MazeModel {
   public static final String NAME = "Recursive Backtracker Maze";

   @Override
   public String getStructureName() {
      return "Recursive Backtracker Maze";
   }

   @Override
   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      new MazeModel$MazeGenerationThread(this, this, parameters).start();
   }
}
