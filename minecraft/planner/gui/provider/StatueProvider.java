package minecraft.planner.gui.provider;

import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.easterisland.EasterIslandControlPanel;
import minecraft.planner.gui.easterisland.EasterIslandDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.easterisland.EasterIslandParameters;
import minecraft.planner.model.statue.StatueModel;

public class StatueProvider extends StructureProvider<EasterIslandParameters> {
   private StatueModel model = new StatueModel();
   private EasterIslandControlPanel controlPanel = new EasterIslandControlPanel(this.model);
   private EasterIslandDisplayPanel displayPanel = new EasterIslandDisplayPanel(this.model);

   @Override
   public StructureModel<EasterIslandParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<EasterIslandParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<EasterIslandParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new StatueModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
