package minecraft.planner.gui.menger;

import minecraft.planner.gui.displaypanels.ThreeDimensionalStructureDisplayPanel;
import minecraft.planner.model.menger.MengerModel;
import minecraft.planner.model.menger.MengerParameters;

public class MengerDisplayPanel extends ThreeDimensionalStructureDisplayPanel<MengerParameters> {
   public MengerDisplayPanel(MengerModel model) {
      super(model);
   }
}
