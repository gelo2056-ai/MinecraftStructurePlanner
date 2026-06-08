package minecraft.planner.gui.provider;

import minecraft.planner.gui.bitmap.BitmapControlPanel;
import minecraft.planner.gui.bitmap.BitmapDisplayPanel;
import minecraft.planner.model.bitmap.BitmapModel;
import minecraft.planner.model.bitmap.BitmapParameters;

public class BitmapProvider extends StructureProvider<BitmapParameters> {
   private BitmapModel model = new BitmapModel();
   private BitmapControlPanel controlPanel = new BitmapControlPanel(this.model);
   private BitmapDisplayPanel displayPanel = new BitmapDisplayPanel(this.model);

   public BitmapControlPanel getControlPanel() {
      return this.controlPanel;
   }

   public BitmapModel getModel() {
      return this.model;
   }

   public BitmapDisplayPanel getStructurePanel() {
      return this.displayPanel;
   }

   @Override
   public void inititalize() {
      this.model = new BitmapModel();
      this.controlPanel.validate();
      this.displayPanel.validate();
   }
}
