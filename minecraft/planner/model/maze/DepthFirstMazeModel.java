package minecraft.planner.model.maze;

public class DepthFirstMazeModel extends MazeModel {
   public static final String NAME = "Depth First Maze";

   @Override
   public String getStructureName() {
      return "Depth First Maze";
   }

   @Override
   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      new MazeModel$MazeGenerationThread(this, this, parameters).start();
   }
}
