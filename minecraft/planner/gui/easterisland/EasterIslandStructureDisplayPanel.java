package minecraft.planner.gui.easterisland;

import javax.swing.JTabbedPane;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.displaypanels.NotesPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.displaypanels.View3DPanel;
import minecraft.planner.model.easterisland.EasterIslandModel;
import minecraft.planner.model.easterisland.EasterIslandParameters;

public class EasterIslandStructureDisplayPanel extends StructureDisplayPanel<EasterIslandParameters> {
   private EasterIslandLayerPanel layerPanel;
   private View3DPanel<EasterIslandParameters> viewPanel;
   private EasterIslandStatisticsPanel statisticsPanel;
   private NotesPanel<EasterIslandParameters> notesPanel;
   private JTabbedPane tabs = null;

   public EasterIslandLayerPanel getLayerPanel() {
      return this.layerPanel;
   }

   public EasterIslandStructureDisplayPanel(EasterIslandModel model) {
      super(model);
      this.layerPanel = new EasterIslandLayerPanel(model, 32);
      if (StructurePlanner.is3DEnabled()) {
         this.viewPanel = new View3DPanel<>(model, false);
      } else {
         this.viewPanel = null;
      }

      this.notesPanel = new NotesPanel<>(this.model);
      this.tabs = new JTabbedPane();
      this.tabs.add("Plan View", this.layerPanel);
      if (this.viewPanel != null) {
         this.tabs.add("3D Projection", this.viewPanel);
      }

      this.tabs.add("Notes", this.notesPanel);
      this.tabs.setSelectedIndex(0);
      this.add(this.tabs, "Center");
      this.statisticsPanel = new EasterIslandStatisticsPanel(model);
      this.add(this.statisticsPanel, "South");
   }

   @Override
   public void dispose() {
      if (this.tabs != null) {
         this.tabs.removeAll();
         if (this.viewPanel != null) {
            this.viewPanel.dispose();
         }

         this.layerPanel = null;
         this.viewPanel = null;
      }
   }
}
