package minecraft.planner.model.maze.generator.impl;

import minecraft.planner.model.maze.generator.GrowingTreeModel;
import minecraft.planner.model.maze.generator.GrowingTreeModel$SelectionMode;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public class NewestGrowingTreeModel extends GrowingTreeModel {
   public NewestGrowingTreeModel(MazeGrid grid) {
      super(grid);
      this.mode = GrowingTreeModel$SelectionMode.Newest;
   }

   @Override
   public String getName() {
      return "Growing Tree (Newest Bias)";
   }
}
