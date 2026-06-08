package minecraft.planner.model.maze.generator.grid;

public class MazeGrid {
   private int width;
   private int depth;
   private CellType[][] grid;

   public MazeGrid(int width, int depth) {
      this.grid = new CellType[width][depth];
      this.width = width;
      this.depth = depth;
   }

   public int getWidth() {
      return this.width;
   }

   public int getDepth() {
      return this.depth;
   }

   public CellType getCell(int x, int y) {
      return this.grid[x][y];
   }

   public void setCell(int x, int y, CellType value) {
      this.grid[x][y] = value;
   }

   @Override
   public String toString() {
      StringBuffer buffer = new StringBuffer();

      for (int y = 0; y < this.getDepth(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            buffer.append(this.getCell(x, y).getDisplay());
         }

         buffer.append('\n');
      }

      return buffer.toString();
   }
}
