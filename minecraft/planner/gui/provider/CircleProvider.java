package minecraft.planner.gui.provider;

import minecraft.planner.gui.circle.CircleControlPanel;
import minecraft.planner.gui.circle.CircleDisplayPanel;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.circle.CircleModel;
import minecraft.planner.model.circle.CircleParameters;

public class CircleProvider extends StructureProvider<CircleParameters> {
   private CircleModel model = new CircleModel();
   private CircleControlPanel controlPanel = new CircleControlPanel(this.model);
   private CircleDisplayPanel displayPanel = new CircleDisplayPanel(this.model);

   @Override
   public StructureModel<CircleParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<CircleParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<CircleParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new CircleModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
