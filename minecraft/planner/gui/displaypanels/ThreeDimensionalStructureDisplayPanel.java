package minecraft.planner.gui.displaypanels;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JTabbedPane;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureParameters;

public class ThreeDimensionalStructureDisplayPanel<P extends StructureParameters> extends StructureDisplayPanel<P> implements Printable {
   private GridPanel<P> gridPanel;
   private LayerPanel<P> layerPanel;
   private View3DPanel<P> viewPanel;
   private NotesPanel<P> notesPanel;
   private JTabbedPane tabs;

   public ThreeDimensionalStructureDisplayPanel(StructureModel<P> model) {
      this(model, true);
   }

   public ThreeDimensionalStructureDisplayPanel(StructureModel<P> model, boolean includeTextures) {
      super(model);
      this.gridPanel = new GridPanel<>(model);
      this.layerPanel = new LayerPanel<>(model);
      if (StructurePlanner.is3DEnabled()) {
         this.viewPanel = new View3DPanel<>(model, includeTextures);
      } else {
         this.viewPanel = null;
      }

      this.notesPanel = new NotesPanel<>(model);
      this.tabs = new JTabbedPane();
      this.tabs.add("Height Map", this.gridPanel);
      this.tabs.add("Plan View", this.layerPanel);
      if (this.viewPanel != null) {
         this.tabs.add("3D Projection", this.viewPanel);
      }

      this.tabs.add("Notes", this.notesPanel);
      this.tabs.setSelectedIndex(0);
      this.add(this.tabs, "Center");
      this.statisticsPanel = new ThreeDimensionalStatisticsPanel<>(model);
      this.add(this.statisticsPanel, "South");
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
         this.notesPanel = null;
      }
   }

   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      return this.layerPanel.print(graphics, pageFormat, pageIndex);
   }
}
