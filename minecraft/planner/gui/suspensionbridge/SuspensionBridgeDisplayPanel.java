package minecraft.planner.gui.suspensionbridge;

import minecraft.planner.gui.displaypanels.ThreeDimensionalManifestPanel;
import minecraft.planner.gui.displaypanels.ThreeDimensionalStructureDisplayPanel;
import minecraft.planner.model.suspensionbridge.SuspensionBridgeModel;
import minecraft.planner.model.suspensionbridge.SuspensionBridgeParameters;

public class SuspensionBridgeDisplayPanel extends ThreeDimensionalStructureDisplayPanel<SuspensionBridgeParameters> {
   public SuspensionBridgeDisplayPanel(SuspensionBridgeModel model) {
      super(model, false);
      this.remove(this.statisticsPanel);
      this.statisticsPanel = new ThreeDimensionalManifestPanel<>(model);
      this.add(this.statisticsPanel, "South");
   }
}
