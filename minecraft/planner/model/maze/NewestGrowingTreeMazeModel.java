package minecraft.planner.model.maze;

public class NewestGrowingTreeMazeModel extends MazeModel {
   public static final String NAME = "Newest Growing Tree Maze";

   @Override
   public String getStructureName() {
      return "Newest Growing Tree Maze";
   }

   @Override
   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      new MazeModel$MazeGenerationThread(this, this, parameters).start();
   }
}
