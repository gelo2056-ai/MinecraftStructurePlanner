package minecraft.planner.gui.bridge;

import minecraft.planner.gui.displaypanels.ThreeDimensionalStructureDisplayPanel;
import minecraft.planner.model.bridge.BridgeModel;
import minecraft.planner.model.bridge.BridgeParameters;

public class BridgeDisplayPanel extends ThreeDimensionalStructureDisplayPanel<BridgeParameters> {
   public BridgeDisplayPanel(BridgeModel model) {
      super(model);
   }
}
