package minecraft.planner.gui.provider.maze;

import minecraft.planner.gui.maze.MazeControlPanel;
import minecraft.planner.gui.maze.MazeDisplayPanel;
import minecraft.planner.gui.provider.StructureProvider;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.maze.DepthFirstMazeModel;
import minecraft.planner.model.maze.MazeParameters;

public class DepthFirstMazeProvider extends StructureProvider<MazeParameters> {
   private DepthFirstMazeModel model = new DepthFirstMazeModel();
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
      this.model = new DepthFirstMazeModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
