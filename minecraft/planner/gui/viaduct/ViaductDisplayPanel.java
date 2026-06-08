package minecraft.planner.gui.viaduct;

import minecraft.planner.gui.displaypanels.ThreeDimensionalManifestPanel;
import minecraft.planner.gui.displaypanels.ThreeDimensionalStructureDisplayPanel;
import minecraft.planner.model.viaduct.ViaductModel;
import minecraft.planner.model.viaduct.ViaductParameters;

public class ViaductDisplayPanel extends ThreeDimensionalStructureDisplayPanel<ViaductParameters> {
   public ViaductDisplayPanel(ViaductModel model) {
      super(model, false);
      this.remove(this.statisticsPanel);
      this.statisticsPanel = new ThreeDimensionalManifestPanel<>(model);
      this.add(this.statisticsPanel, "South");
   }
}
