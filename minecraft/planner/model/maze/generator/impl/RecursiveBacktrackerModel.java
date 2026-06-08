package minecraft.planner.model.maze.generator.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Stack;
import minecraft.planner.model.maze.generator.AbstractMazeModel;
import minecraft.planner.model.maze.generator.grid.CellDirection;
import minecraft.planner.model.maze.generator.grid.CellType;
import minecraft.planner.model.maze.generator.grid.MazeCell;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public class RecursiveBacktrackerModel extends AbstractMazeModel {
   private Stack<MazeCell> cellStack = new Stack<>();
   private MazeCell currentCell;

   public RecursiveBacktrackerModel(MazeGrid grid) {
      super(grid);
   }

   @Override
   public String getName() {
      return "Recursive Backtrack";
   }

   @Override
   public void generateMaze() {
      this.currentCell = new MazeCell(1, 1, CellDirection.Here);
      this.recursiveBacktracker();
   }

   private void recursiveBacktracker() {
      while (this.currentCell != null) {
         int x = this.currentCell.getX();
         int y = this.currentCell.getY();
         if (this.grid.getCell(x, y) == CellType.Wall) {
            this.removeWall(x, y, CellDirection.Here);
         }

         Collection<MazeCell> neighbors = this.getNeighbors(x, y);
         this.removeEdgeNeighbors(neighbors);
         if (!neighbors.isEmpty()) {
            this.cellStack.push(this.currentCell);
            MazeCell randomNeighbor = neighbors.iterator().next();
            this.removeWall(x, y, randomNeighbor.getDirection());
            this.currentCell = randomNeighbor;
         } else if (!this.cellStack.isEmpty()) {
            this.currentCell = this.cellStack.pop();
         } else {
            this.currentCell = null;
         }
      }
   }

   public void removeEdgeNeighbors(Collection<MazeCell> neighbors) {
      Collection<MazeCell> cellsToRemove = new HashSet<>();

      for (MazeCell cell : neighbors) {
         if (cell.getX() <= 0 || cell.getX() >= this.grid.getWidth() - 1 || cell.getY() <= 0 || cell.getY() >= this.grid.getDepth() - 1) {
            cellsToRemove.add(cell);
         }
      }

      neighbors.removeAll(cellsToRemove);
   }
}
