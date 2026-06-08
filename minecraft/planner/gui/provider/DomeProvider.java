package minecraft.planner.gui.provider;

import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.dome.DomeControlPanel;
import minecraft.planner.gui.dome.DomeDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.dome.DomeModel;
import minecraft.planner.model.dome.DomeParameters;

public class DomeProvider extends StructureProvider<DomeParameters> {
   private DomeModel model = new DomeModel();
   private DomeControlPanel controlPanel = new DomeControlPanel(this.model);
   private DomeDisplayPanel displayPanel = new DomeDisplayPanel(this.model);

   @Override
   public StructureModel<DomeParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<DomeParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<DomeParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new DomeModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
