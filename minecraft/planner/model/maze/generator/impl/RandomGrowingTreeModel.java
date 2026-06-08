package minecraft.planner.model.maze.generator.impl;

import minecraft.planner.model.maze.generator.GrowingTreeModel;
import minecraft.planner.model.maze.generator.GrowingTreeModel$SelectionMode;
import minecraft.planner.model.maze.generator.grid.MazeGrid;

public class RandomGrowingTreeModel extends GrowingTreeModel {
   public RandomGrowingTreeModel(MazeGrid grid) {
      super(grid);
      this.mode = GrowingTreeModel$SelectionMode.Random;
   }

   @Override
   public String getName() {
      return "Growing Tree (Random)";
   }
}
