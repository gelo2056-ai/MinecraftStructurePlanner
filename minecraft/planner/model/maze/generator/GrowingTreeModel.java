package minecraft.planner.model.maze.generator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import minecraft.planner.model.maze.generator.grid.CellDirection;
import minecraft.planner.model.maze.generator.grid.CellType;
import minecraft.planner.model.maze.generator.grid.MazeCell;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public abstract class GrowingTreeModel extends AbstractMazeModel {
   private LinkedList<MazeCell> passageList = new LinkedList<>();
   protected GrowingTreeModel.SelectionMode mode = GrowingTreeModel.SelectionMode.Random;

   public GrowingTreeModel(MazeGrid grid) {
      super(grid);
   }

   @Override
   public void generateMaze() {
      MazeCell start = this.pickRandomStartingPoint();
      this.carveCell(start.getX(), start.getY());

      while (!this.passageList.isEmpty()) {
         MazeCell passageCell;
         switch (this.mode) {
            case Random:
            default:
               passageCell = this.passageList.iterator().next();
               break;
            case Newest:
               passageCell = this.passageList.getFirst();
               break;
            case Oldest:
               int half = this.passageList.size() / 2;
               List<MazeCell> oldestHalf = this.passageList.subList(half, this.passageList.size());
               Collections.shuffle(oldestHalf);
               passageCell = oldestHalf.iterator().next();
         }

         Collection<MazeCell> neighboringPassages = this.getNeighbors(passageCell.getX(), passageCell.getY());
         if (!neighboringPassages.isEmpty()) {
            MazeCell newPassage = neighboringPassages.iterator().next();
            CellDirection passageDirection = passageDirection(passageCell, newPassage);
            this.removeWall(passageCell.getX(), passageCell.getY(), passageDirection);
            this.carveCell(newPassage.getX(), newPassage.getY());
         } else {
            this.passageList.remove(passageCell);
         }
      }
   }

   private static CellDirection passageDirection(MazeCell from, MazeCell to) {
      if (from.getX() == to.getX()) {
         return from.getY() < to.getY() ? CellDirection.South : CellDirection.North;
      } else {
         return from.getX() < to.getX() ? CellDirection.East : CellDirection.West;
      }
   }

   private void carveCell(int x, int y) {
      MazeCell cell = new MazeCell(x, y);
      if (this.grid.getCell(x, y) == CellType.Wall) {
         this.removeWall(x, y, CellDirection.Here);
         this.passageList.addFirst(cell);
         if (this.mode == GrowingTreeModel.SelectionMode.Random) {
            Collections.shuffle(this.passageList);
         }
      }
   }

   public enum SelectionMode {
      Random,
      Newest,
      Oldest;
   }
}
