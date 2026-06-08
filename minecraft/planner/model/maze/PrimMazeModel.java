package minecraft.planner.model.maze;

public class PrimMazeModel extends MazeModel {
   public static final String NAME = "Prim Maze";

   @Override
   public String getStructureName() {
      return "Prim Maze";
   }

   @Override
   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      new MazeModel$MazeGenerationThread(this, this, parameters).start();
   }
}
