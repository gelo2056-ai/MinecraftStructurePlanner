package minecraft.planner.gui.maze;

import minecraft.planner.gui.displaypanels.ThreeDimensionalManifestPanel;
import minecraft.planner.gui.displaypanels.ThreeDimensionalStructureDisplayPanel;
import minecraft.planner.model.maze.MazeModel;
import minecraft.planner.model.maze.MazeParameters;

public class MazeDisplayPanel extends ThreeDimensionalStructureDisplayPanel<MazeParameters> {
   public MazeDisplayPanel(MazeModel model) {
      super(model, false);
      this.remove(this.statisticsPanel);
      this.statisticsPanel = new ThreeDimensionalManifestPanel<>(model);
      this.add(this.statisticsPanel, "South");
   }
}
