package minecraft.planner.gui.displaypanels;

import javax.swing.JTabbedPane;
import minecraft.planner.gui.provider.StructureProvider;

public class StructureProviderTabs extends JTabbedPane {
   public void addProvider(StructureProvider<?> provider) {
      this.addTab(provider.getModel().getStructureName(), new StructureProviderPanel(provider));
   }
}
