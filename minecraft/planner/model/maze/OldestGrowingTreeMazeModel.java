package minecraft.planner.model.maze;

public class OldestGrowingTreeMazeModel extends MazeModel {
   public static final String NAME = "Oldest Growing Tree Maze";

   @Override
   public String getStructureName() {
      return "Oldest Growing Tree Maze";
   }

   @Override
   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      new MazeModel$MazeGenerationThread(this, this, parameters).start();
   }
}
