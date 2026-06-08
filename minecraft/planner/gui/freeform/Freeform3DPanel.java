package minecraft.planner.gui.freeform;

import minecraft.planner.gui.displaypanels.View3DPanel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.freeform.FreeformModel;
import minecraft.planner.model.freeform.FreeformParameters;

public class Freeform3DPanel extends View3DPanel<FreeformParameters> implements StructureModelChangeListener {
   public Freeform3DPanel(FreeformModel model) {
      super(model, false, true);
   }
}
