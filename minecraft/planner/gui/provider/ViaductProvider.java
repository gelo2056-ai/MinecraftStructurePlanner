package minecraft.planner.gui.provider;

import minecraft.planner.gui.viaduct.ViaductControlPanel;
import minecraft.planner.gui.viaduct.ViaductDisplayPanel;
import minecraft.planner.model.viaduct.ViaductModel;
import minecraft.planner.model.viaduct.ViaductParameters;

public class ViaductProvider extends StructureProvider<ViaductParameters> {
   private ViaductModel model = new ViaductModel();
   private ViaductControlPanel controlPanel = new ViaductControlPanel(this.model);
   private ViaductDisplayPanel displayPanel = new ViaductDisplayPanel(this.model);

   public ViaductControlPanel getControlPanel() {
      return this.controlPanel;
   }

   public ViaductModel getModel() {
      return this.model;
   }

   public ViaductDisplayPanel getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new ViaductModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
