package minecraft.planner.gui.bitmap;

import javax.swing.JTabbedPane;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.displaypanels.NotesPanel;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.displaypanels.View3DPanel;
import minecraft.planner.model.bitmap.BitmapModel;
import minecraft.planner.model.bitmap.BitmapParameters;
import minecraft.planner.model.grid.UndoBuffer;

public class BitmapStructureDisplayPanel extends StructureDisplayPanel<BitmapParameters> {
   private BitmapLayerPanel layerPanel;
   private View3DPanel<BitmapParameters> viewPanel;
   private BitmapStatisticsPanel statisticsPanel;
   private NotesPanel<BitmapParameters> notesPanel;
   private JTabbedPane tabs = null;

   public BitmapLayerPanel getLayerPanel() {
      return this.layerPanel;
   }

   public BitmapStructureDisplayPanel(BitmapModel model) {
      super(model);
      this.layerPanel = new BitmapLayerPanel(model, 32);
      if (StructurePlanner.is3DEnabled()) {
         this.viewPanel = new View3DPanel<>(model, false, true);
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
      this.statisticsPanel = new BitmapStatisticsPanel(model);
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

         this.layerPanel = null;
         this.viewPanel = null;
      }
   }
}
