package minecraft.planner.gui.provider.maze;

import minecraft.planner.gui.maze.MazeControlPanel;
import minecraft.planner.gui.maze.MazeDisplayPanel;
import minecraft.planner.gui.provider.StructureProvider;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.maze.MazeParameters;
import minecraft.planner.model.maze.OldestGrowingTreeMazeModel;

public class OldestGrowingTreeMazeProvider extends StructureProvider<MazeParameters> {
   private OldestGrowingTreeMazeModel model = new OldestGrowingTreeMazeModel();
   private MazeControlPanel controlPanel = new MazeControlPanel(this.model);
   private MazeDisplayPanel displayPanel = new MazeDisplayPanel(this.model);

   public MazeControlPanel getControlPanel() {
      return this.controlPanel;
   }

   public MazeDisplayPanel getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public StructureModel<MazeParameters> getModel() {
      return this.model;
   }

   @Override
   public void inititalize() {
      this.model = new OldestGrowingTreeMazeModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
