package minecraft.planner.gui.provider;

import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureParameters;

public abstract class StructureProvider<P extends StructureParameters> {
   public abstract void inititalize();

   public abstract StructureDisplayPanel<P> getStructurePanel();

   public abstract StructureControlPanel<P> getControlPanel();

   public abstract StructureModel<P> getModel();
}
