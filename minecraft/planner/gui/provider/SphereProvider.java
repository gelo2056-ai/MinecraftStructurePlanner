package minecraft.planner.gui.provider;

import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.sphere.SphereControlPanel;
import minecraft.planner.gui.sphere.SphereDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.sphere.SphereModel;
import minecraft.planner.model.sphere.SphereParameters;

public class SphereProvider extends StructureProvider<SphereParameters> {
   private SphereModel model = new SphereModel();
   private SphereControlPanel controlPanel = new SphereControlPanel(this.model);
   private SphereDisplayPanel displayPanel = new SphereDisplayPanel(this.model);

   @Override
   public StructureModel<SphereParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<SphereParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<SphereParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new SphereModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
