package minecraft.planner.gui.provider;

import java.io.File;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.freeform.FreeformControlPanel;
import minecraft.planner.gui.freeform.FreeformDisplayPanel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.freeform.Binvox;
import minecraft.planner.model.freeform.FreeformModel;
import minecraft.planner.model.freeform.FreeformParameters;
import minecraft.planner.model.freeform.Ruins;
import minecraft.planner.model.util.Tag;

public class FreeformProvider extends StructureProvider<FreeformParameters> {
   private FreeformModel model;
   private FreeformControlPanel controlPanel;
   private FreeformDisplayPanel displayPanel;

   public FreeformProvider() {
      this.model = new FreeformModel();
      this.controlPanel = new FreeformControlPanel(this.model);
      this.displayPanel = new FreeformDisplayPanel(this.model);
   }

   public FreeformProvider(File file) {
      this.model = new FreeformModel(file);
      this.controlPanel = new FreeformControlPanel(this.model);
      this.displayPanel = new FreeformDisplayPanel(this.model);
   }

   public FreeformProvider(Binvox binvox, TextureName texture) {
      this.model = new FreeformModel(binvox, texture);
      this.controlPanel = new FreeformControlPanel(this.model);
      this.displayPanel = new FreeformDisplayPanel(this.model);
   }

   public FreeformProvider(Ruins ruins) {
      this.model = new FreeformModel(ruins);
      this.controlPanel = new FreeformControlPanel(this.model);
      this.displayPanel = new FreeformDisplayPanel(this.model);
   }

   public FreeformProvider(Tag schematic) {
      this.model = new FreeformModel(schematic);
      this.controlPanel = new FreeformControlPanel(this.model);
      this.displayPanel = new FreeformDisplayPanel(this.model);
   }

   @Override
   public StructureModel<FreeformParameters> getModel() {
      return this.model;
   }

   @Override
   public StructureControlPanel<FreeformParameters> getControlPanel() {
      return this.controlPanel;
   }

   @Override
   public StructureDisplayPanel<FreeformParameters> getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new FreeformModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
