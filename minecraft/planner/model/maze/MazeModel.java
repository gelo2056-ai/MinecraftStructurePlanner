package minecraft.planner.model.maze;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.maze.generator.grid.CellType;
import minecraft.planner.model.maze.generator.grid.MazeGrid;
import minecraft.planner.model.maze.generator.impl.RecursiveBacktrackerModel;

public abstract class MazeModel extends StructureModel<MazeParameters> {
   @Override
   public boolean includeVisibleBlocksParameter() {
      return false;
   }

   public void generate(MazeParameters parameters) {
      super.generate(parameters);
      this.grid.initialize(parameters.getWidth(), parameters.getDepth());
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public abstract String getStructureName();

   protected void convertMazeGridToWorldGrid(MazeParameters parameters, MazeGrid mazeGrid) {
      int mazeLevel = 0;
      TextureName floorTexture = parameters.getFloorTexture();
      if (floorTexture != TextureName.None) {
         mazeLevel++;
      }

      for (int x = 0; x < mazeGrid.getWidth(); x++) {
         for (int y = 0; y < mazeGrid.getDepth(); y++) {
            WorldGrid$Cell cell = this.grid.getCell(x, y);
            CellType cellType = mazeGrid.getCell(x, y);
            if (floorTexture != TextureName.None) {
               cell.getStack().addZ(mazeLevel, floorTexture);
            }

            switch (cellType) {
               case Wall:
                  cell.getStack().addZ(mazeLevel + 1, mazeLevel + parameters.getWallHeight(), parameters.getWallTexture());
                  break;
               case Permanent_Wall:
                  cell.getStack().addZ(0, mazeLevel + parameters.getBorderHeight(), parameters.getBorderTexture());
                  break;
               case Reserved_Space:
                  if (!this.surroundedByReservedSpace(mazeGrid, x, y)) {
                     cell.getStack().addZ(mazeLevel + 1, mazeLevel + parameters.getCenterRoomHeight(), parameters.getCenterRoomTexture());
                  }
            }
         }
      }
   }

   private boolean surroundedByReservedSpace(MazeGrid grid, int x, int y) {
      return x - 1 <= 0 || x + 1 >= grid.getWidth() || y - 1 <= 0 || y + 1 >= grid.getDepth()
         ? false
         : grid.getCell(x - 1, y) == CellType.Reserved_Space
            && grid.getCell(x + 1, y) == CellType.Reserved_Space
            && grid.getCell(x, y - 1) == CellType.Reserved_Space
            && grid.getCell(x, y + 1) == CellType.Reserved_Space;
   }

   protected class MazeGenerationThread extends Thread {
      private MazeModel model;
      private MazeParameters parameters;

      public MazeGenerationThread(MazeModel model, MazeParameters parameters) {
         this.model = model;
         this.parameters = parameters;
         this.setName("Maze Generation " + this.getName());
      }

      @Override
      public void run() {
         MazeGrid mazeGrid = new MazeGrid(this.parameters.getWidth(), this.parameters.getDepth());
         RecursiveBacktrackerModel mazeModel = new RecursiveBacktrackerModel(mazeGrid);
         mazeModel.initialize(this.parameters);
         mazeModel.generateMaze();
         this.model.convertMazeGridToWorldGrid(this.parameters, mazeGrid);
         this.model.generated();
      }
   }
}
