package minecraft.planner.gui.provider;

import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.menger.MengerControlPanel;
import minecraft.planner.gui.menger.MengerDisplayPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.menger.MengerModel;
import minecraft.planner.model.menger.MengerParameters;

public class MengerProvider extends StructureProvider<MengerParameters> {
   private MengerModel model = new MengerModel();
   private MengerControlPanel controlPanel = new MengerControlPanel(this.model);
   private MengerDisplayPanel displayPanel = new MengerDisplayPanel(this.model);

   @Override
   public StructureModel<MengerParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<MengerParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<MengerParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new MengerModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
