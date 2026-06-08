package minecraft.planner.model.maze.generator.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import minecraft.planner.model.maze.generator.AbstractMazeModel;
import minecraft.planner.model.maze.generator.grid.CellDirection;
import minecraft.planner.model.maze.generator.grid.CellType;
import minecraft.planner.model.maze.generator.grid.MazeCell;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public class PrimModel extends AbstractMazeModel {
   private List<MazeCell> wallList = new ArrayList<>();

   public PrimModel(MazeGrid grid) {
      super(grid);
   }

   @Override
   public String getName() {
      return "Prim";
   }

   @Override
   public void generateMaze() {
      this.prim();
   }

   private void prim() {
      MazeCell start = this.pickRandomStartingPoint();
      this.removeWall(start.getX(), start.getY(), CellDirection.Here);
      this.addWallsToList(start.getX(), start.getY());

      while (!this.wallList.isEmpty()) {
         MazeCell wallCell = this.wallList.get(0);
         Collection<MazeCell> neighboringPassages = this.findNeighboringPassages(wallCell);
         MazeCell chosenNeighbor = neighboringPassages.iterator().next();
         if (chosenNeighbor != null) {
            CellType wallType = this.grid.getCell(wallCell.getX(), wallCell.getY());
            if (wallType == CellType.Wall) {
               this.removeWall(wallCell.getX(), wallCell.getY(), CellDirection.Here);
            }

            this.removeWall(wallCell.getX(), wallCell.getY(), chosenNeighbor.getDirection());
            if (wallType == CellType.Wall) {
               this.addWallsToList(wallCell.getX(), wallCell.getY());
            }
         }

         this.wallList.remove(wallCell);
      }
   }

   private Collection<MazeCell> findNeighboringPassages(MazeCell cell) {
      ArrayList<MazeCell> neighboringPassages = new ArrayList<>();
      int x = cell.getX();
      int y = cell.getY();
      if (x - 2 > 0 && this.grid.getCell(x - 2, y) == CellType.Passage) {
         neighboringPassages.add(new MazeCell(x - 2, y, CellDirection.West));
      }

      if (x + 2 < this.grid.getWidth() && this.grid.getCell(x + 2, y) == CellType.Passage) {
         neighboringPassages.add(new MazeCell(x + 2, y, CellDirection.East));
      }

      if (y - 2 > 0 && this.grid.getCell(x, y - 2) == CellType.Passage) {
         neighboringPassages.add(new MazeCell(x, y - 2, CellDirection.North));
      }

      if (y + 2 < this.grid.getDepth() && this.grid.getCell(x, y + 2) == CellType.Passage) {
         neighboringPassages.add(new MazeCell(x, y + 2, CellDirection.South));
      }

      Collections.shuffle(neighboringPassages);
      return neighboringPassages;
   }

   private void addWallsToList(int x, int y) {
      if (x - 2 > 0 && this.isWall(this.grid.getCell(x - 2, y))) {
         this.addWallIfNotPresent(new MazeCell(x - 2, y));
      }

      if (x + 2 < this.grid.getWidth() && this.isWall(this.grid.getCell(x + 2, y))) {
         this.addWallIfNotPresent(new MazeCell(x + 2, y));
      }

      if (y - 2 > 0 && this.isWall(this.grid.getCell(x, y - 2))) {
         this.addWallIfNotPresent(new MazeCell(x, y - 2));
      }

      if (y + 2 < this.grid.getDepth() && this.isWall(this.grid.getCell(x, y + 2))) {
         this.addWallIfNotPresent(new MazeCell(x, y + 2));
      }

      Collections.shuffle(this.wallList);
   }

   private void addWallIfNotPresent(MazeCell cell) {
      if (!this.wallList.contains(cell)) {
         this.wallList.add(cell);
      }
   }
}
