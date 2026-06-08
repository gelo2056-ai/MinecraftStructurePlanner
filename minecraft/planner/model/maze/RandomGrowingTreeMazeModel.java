package minecraft.planner.model.maze;

public class RandomGrowingTreeMazeModel extends MazeModel {
   public static final String NAME = "Random Growing Tree Maze";

   @Override
   public String getStructureName() {
      return "Random Growing Tree Maze";
   }

   @Override
   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      new MazeModel$MazeGenerationThread(this, this, parameters).start();
   }
}
