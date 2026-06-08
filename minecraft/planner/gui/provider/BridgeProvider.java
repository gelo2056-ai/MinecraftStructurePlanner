package minecraft.planner.gui.provider;

import minecraft.planner.gui.bridge.BridgeControlPanel;
import minecraft.planner.gui.bridge.BridgeDisplayPanel;
import minecraft.planner.model.bridge.BridgeModel;
import minecraft.planner.model.bridge.BridgeParameters;

public class BridgeProvider extends StructureProvider<BridgeParameters> {
   private BridgeModel model = new BridgeModel();
   private BridgeControlPanel controlPanel = new BridgeControlPanel(this.model);
   private BridgeDisplayPanel displayPanel = new BridgeDisplayPanel(this.model);

   public BridgeControlPanel getControlPanel() {
      return this.controlPanel;
   }

   public BridgeModel getModel() {
      return this.model;
   }

   public BridgeDisplayPanel getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new BridgeModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
