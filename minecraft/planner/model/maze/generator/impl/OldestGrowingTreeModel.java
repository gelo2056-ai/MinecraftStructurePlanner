package minecraft.planner.model.maze.generator.impl;

import minecraft.planner.model.maze.generator.GrowingTreeModel;
import minecraft.planner.model.maze.generator.GrowingTreeModel$SelectionMode;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public class OldestGrowingTreeModel extends GrowingTreeModel {
   public OldestGrowingTreeModel(MazeGrid grid) {
      super(grid);
      this.mode = GrowingTreeModel$SelectionMode.Oldest;
   }

   @Override
   public String getName() {
      return "Growing Tree (Oldest Bias)";
   }
}
