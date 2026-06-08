package minecraft.planner.gui.provider;

import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.hemisphere.HemisphereControlPanel;
import minecraft.planner.gui.hemisphere.HemisphereDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.hemisphere.HemisphereModel;
import minecraft.planner.model.hemisphere.HemisphereParameters;

public class HemisphereProvider extends StructureProvider<HemisphereParameters> {
   private HemisphereModel model = new HemisphereModel();
   private HemisphereControlPanel controlPanel = new HemisphereControlPanel(this.model);
   private HemisphereDisplayPanel displayPanel = new HemisphereDisplayPanel(this.model);

   @Override
   public StructureModel<HemisphereParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<HemisphereParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<HemisphereParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new HemisphereModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
