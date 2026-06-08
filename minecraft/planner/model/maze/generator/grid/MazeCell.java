package minecraft.planner.model.maze.generator.grid;

public class MazeCell {
   private int x;
   private int y;
   private CellDirection direction;

   public MazeCell(int x, int y) {
      this(x, y, CellDirection.Here);
   }

   public MazeCell(int x, int y, CellDirection direction) {
      this.x = x;
      this.y = y;
      this.direction = direction;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public CellDirection getDirection() {
      return this.direction;
   }

   @Override
   public String toString() {
      return "[" + this.x + "," + this.y + "," + this.direction + "]";
   }

   @Override
   public int hashCode() {
      return this.x + this.y;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof MazeCell)) {
         return false;
      }

      MazeCell c = (MazeCell)o;
      return this.x == c.x && this.y == c.y;
   }
}
