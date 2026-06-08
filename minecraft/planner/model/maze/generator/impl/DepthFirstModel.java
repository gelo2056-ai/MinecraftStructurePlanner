package minecraft.planner.model.maze.generator.impl;

import minecraft.planner.model.maze.generator.AbstractMazeModel;
import minecraft.planner.model.maze.generator.grid.CellDirection;
import minecraft.planner.model.maze.generator.grid.CellType;
import minecraft.planner.model.maze.generator.grid.MazeCell;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public class DepthFirstModel extends AbstractMazeModel {
   public DepthFirstModel(MazeGrid grid) {
      super(grid);
   }

   @Override
   public String getName() {
      return "Depth First";
   }

   @Override
   public void generateMaze() {
      this.depthFirst(1, 1);
   }

   private void depthFirst(int x, int y) {
      if (this.grid.getCell(x, y) == CellType.Wall) {
         this.removeWall(x, y, CellDirection.Here);

         for (MazeCell cell : this.getNeighbors(x, y)) {
            if (this.isValidNeighbor(cell.getX(), cell.getY())) {
               this.removeWall(x, y, cell.getDirection());
               this.depthFirst(cell.getX(), cell.getY());
            }
         }
      }
   }
}
