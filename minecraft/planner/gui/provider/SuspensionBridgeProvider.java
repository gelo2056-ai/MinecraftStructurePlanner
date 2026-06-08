package minecraft.planner.gui.provider;

import minecraft.planner.gui.suspensionbridge.SuspensionBridgeControlPanel;
import minecraft.planner.gui.suspensionbridge.SuspensionBridgeDisplayPanel;
import minecraft.planner.model.suspensionbridge.SuspensionBridgeModel;
import minecraft.planner.model.suspensionbridge.SuspensionBridgeParameters;

public class SuspensionBridgeProvider extends StructureProvider<SuspensionBridgeParameters> {
   private SuspensionBridgeModel model = new SuspensionBridgeModel();
   private SuspensionBridgeControlPanel controlPanel = new SuspensionBridgeControlPanel(this.model);
   private SuspensionBridgeDisplayPanel displayPanel = new SuspensionBridgeDisplayPanel(this.model);

   public SuspensionBridgeControlPanel getControlPanel() {
      return this.controlPanel;
   }

   public SuspensionBridgeModel getModel() {
      return this.model;
   }

   public SuspensionBridgeDisplayPanel getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new SuspensionBridgeModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
