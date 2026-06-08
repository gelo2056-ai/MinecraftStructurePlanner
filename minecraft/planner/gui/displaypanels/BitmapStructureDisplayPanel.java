package minecraft.planner.gui.displaypanels;

import javax.swing.JTabbedPane;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureParameters;

public class BitmapStructureDisplayPanel<P extends StructureParameters> extends StructureDisplayPanel<P> {
   private BitmapPanel<P> bitMapPanel;

   public BitmapStructureDisplayPanel(StructureModel<P> model) {
      super(model);
      this.bitMapPanel = new BitmapPanel<>(model);
      JTabbedPane tabs = new JTabbedPane();
      tabs.add("Bitmap View", this.bitMapPanel);
      tabs.setSelectedIndex(0);
      this.add(tabs, "Center");
   }

   @Override
   public void dispose() {
   }
}
