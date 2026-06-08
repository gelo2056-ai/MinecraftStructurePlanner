package minecraft.planner.model.maze.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import minecraft.planner.model.maze.MazeParameters;
import minecraft.planner.model.maze.generator.grid.CellDirection;
import minecraft.planner.model.maze.generator.grid.CellType;
import minecraft.planner.model.maze.generator.grid.MazeCell;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public abstract class AbstractMazeModel {
   protected MazeGrid grid;

   public AbstractMazeModel(MazeGrid grid) {
      this.grid = grid;
   }

   public void initialize(MazeParameters parameters) {
      for (int x = 0; x < this.grid.getWidth(); x++) {
         for (int y = 0; y < this.grid.getDepth(); y++) {
            CellType cell;
            if (x != 0 && y != 0 && x != this.grid.getWidth() - 1 && y != this.grid.getDepth() - 1) {
               cell = CellType.Wall;
            } else {
               cell = CellType.Permanent_Wall;
            }

            this.grid.setCell(x, y, cell);
         }
      }

      if (parameters.isCenterRoom()) {
         int centerX = this.grid.getWidth() / 2;
         int centerY = this.grid.getDepth() / 2;
         int roomWidth = parameters.getCenterRoomWidth() / 2;
         int roomHeight = parameters.getCenterRoomDepth() / 2;

         for (int horizontal = -roomWidth; horizontal <= roomWidth; horizontal++) {
            for (int vertical = -roomHeight; vertical <= roomHeight; vertical++) {
               this.grid.setCell(centerX + horizontal, centerY + vertical, CellType.Reserved_Space);
            }
         }
      }
   }

   public abstract void generateMaze();

   public abstract String getName();

   protected MazeCell pickRandomStartingPoint() {
      Random rnd = new Random();

      int x;
      int y;
      CellType cellType;
      do {
         x = rnd.nextInt(this.grid.getWidth() - 2) + 1;
         y = rnd.nextInt(this.grid.getDepth() - 2) + 1;
         if (x % 2 == 0) {
            x++;
         }

         if (y % 2 == 0) {
            y++;
         }

         cellType = this.grid.getCell(x, y);
      } while (cellType != CellType.Wall);

      return new MazeCell(x, y, CellDirection.Here);
   }

   protected Collection<MazeCell> getNeighbors(int x, int y) {
      List<MazeCell> neighbors = new ArrayList<>();
      if (this.isValidNeighbor(x - 2, y)) {
         neighbors.add(new MazeCell(x - 2, y, CellDirection.West));
      }

      if (this.isValidNeighbor(x + 2, y)) {
         neighbors.add(new MazeCell(x + 2, y, CellDirection.East));
      }

      if (this.isValidNeighbor(x, y - 2)) {
         neighbors.add(new MazeCell(x, y - 2, CellDirection.North));
      }

      if (this.isValidNeighbor(x, y + 2)) {
         neighbors.add(new MazeCell(x, y + 2, CellDirection.South));
      }

      Collections.shuffle(neighbors);
      return neighbors;
   }

   protected boolean isValidNeighbor(int x, int y) {
      if (x >= 0 && x < this.grid.getWidth() && y >= 0 && y < this.grid.getDepth()) {
         CellType cellType = this.grid.getCell(x, y);
         return !this.isWall(cellType)
            ? false
            : (x - 1 < 0 || this.isWall(this.grid.getCell(x - 1, y)))
               && (x + 1 >= this.grid.getWidth() || this.isWall(this.grid.getCell(x + 1, y)))
               && (y - 1 < 0 || this.isWall(this.grid.getCell(x, y - 1)))
               && (y + 1 >= this.grid.getDepth() || this.isWall(this.grid.getCell(x, y + 1)));
      } else {
         return false;
      }
   }

   protected boolean isWall(CellType cellType) {
      return cellType == CellType.Wall || cellType == CellType.Permanent_Wall;
   }

   protected void removeWall(int x, int y, CellDirection direction) {
      switch (direction) {
         case Here:
            this.grid.setCell(x, y, CellType.Passage);
            break;
         case North:
            this.grid.setCell(x, y - 1, CellType.Passage);
            break;
         case South:
            this.grid.setCell(x, y + 1, CellType.Passage);
            break;
         case East:
            this.grid.setCell(x + 1, y, CellType.Passage);
            break;
         case West:
            this.grid.setCell(x - 1, y, CellType.Passage);
      }
   }
}
