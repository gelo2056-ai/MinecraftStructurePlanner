package minecraft.planner.gui.torus;

import minecraft.planner.gui.displaypanels.ThreeDimensionalStructureDisplayPanel;
import minecraft.planner.model.torus.TorusModel;
import minecraft.planner.model.torus.TorusParameters;

public class TorusDisplayPanel extends ThreeDimensionalStructureDisplayPanel<TorusParameters> {
   public TorusDisplayPanel(TorusModel model) {
      super(model);
   }
}
