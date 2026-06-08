package minecraft.planner.gui.provider;

import minecraft.planner.gui.torus.TorusControlPanel;
import minecraft.planner.gui.torus.TorusDisplayPanel;
import minecraft.planner.model.torus.TorusModel;
import minecraft.planner.model.torus.TorusParameters;

public class TorusProvider extends StructureProvider<TorusParameters> {
   private TorusModel model = new TorusModel();
   private TorusControlPanel controlPanel = new TorusControlPanel(this.model);
   private TorusDisplayPanel displayPanel = new TorusDisplayPanel(this.model);

   public TorusControlPanel getControlPanel() {
      return this.controlPanel;
   }

   public TorusModel getModel() {
      return this.model;
   }

   public TorusDisplayPanel getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new TorusModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
