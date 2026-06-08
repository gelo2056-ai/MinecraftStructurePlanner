package minecraft.planner.gui.displaypanels;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import minecraft.planner.gui.provider.StructureProvider;

public class StructureProviderPanel extends JPanel {
   private StructureProvider<?> provider;

   public StructureProviderPanel(StructureProvider<?> provider) {
      this.setLayout(new BorderLayout());
      this.add(provider.getStructurePanel(), "Center");
      this.add(provider.getControlPanel(), "East");
      this.provider = provider;
   }

   public StructureProvider<?> getProvider() {
      return this.provider;
   }
}
