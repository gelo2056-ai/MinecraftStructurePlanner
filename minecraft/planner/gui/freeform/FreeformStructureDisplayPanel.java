package minecraft.planner.gui.freeform;

import javax.swing.JTabbedPane;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.displaypanels.GridPanel;
import minecraft.planner.gui.displaypanels.NotesPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.model.freeform.FreeformModel;
import minecraft.planner.model.freeform.FreeformParameters;
import minecraft.planner.model.grid.UndoBuffer;

public class FreeformStructureDisplayPanel extends StructureDisplayPanel<FreeformParameters> {
   private GridPanel<FreeformParameters> gridPanel;
   private FreeformLayerPanel layerPanel;
   private Freeform3DPanel viewPanel;
   private FreeformStatisticsPanel statisticsPanel;
   private NotesPanel<FreeformParameters> notesPanel;
   private JTabbedPane tabs = null;

   public FreeformLayerPanel getLayerPanel() {
      return this.layerPanel;
   }

   public FreeformStructureDisplayPanel(FreeformModel model) {
      super(model);
      this.gridPanel = new GridPanel<>(model);
      this.layerPanel = new FreeformLayerPanel(model, 32);
      this.notesPanel = new NotesPanel<>(model);
      if (StructurePlanner.is3DEnabled()) {
         this.viewPanel = new Freeform3DPanel(model);
      } else {
         this.viewPanel = null;
      }

      this.tabs = new JTabbedPane();
      this.tabs.add("Plan View", this.layerPanel);
      if (this.viewPanel != null) {
         this.tabs.add("3D Projection", this.viewPanel);
      }

      this.tabs.add("Height Map", this.gridPanel);
      this.tabs.add("Notes", this.notesPanel);
      this.tabs.setSelectedIndex(0);
      this.add(this.tabs, "Center");
      this.statisticsPanel = new FreeformStatisticsPanel(model);
      this.add(this.statisticsPanel, "South");
   }

   @Override
   public UndoBuffer getUndoBuffer() {
      return this.layerPanel.getUndoBuffer();
   }

   @Override
   public void undo() {
      UndoBuffer buffer = this.layerPanel.getUndoBuffer();
      this.model.undo(buffer);
      buffer.clearBuffer();
      this.layerPanel.repaint();
      this.gridPanel.repaint();
      this.viewPanel.repaint();
      this.forceUpdate();
   }

   public void forceUpdate() {
      this.viewPanel.forceUpdate();
   }

   @Override
   public void dispose() {
      if (this.tabs != null) {
         this.tabs.removeAll();
         if (this.viewPanel != null) {
            this.viewPanel.dispose();
         }

         this.gridPanel = null;
         this.layerPanel = null;
         this.viewPanel = null;
      }
   }
}
