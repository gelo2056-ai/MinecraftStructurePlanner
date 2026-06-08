package minecraft.planner.gui.displaypanels;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.grid.UndoBuffer;

public abstract class StructureDisplayPanel<P extends StructureParameters> extends JPanel {
   protected StatisticsPanel<P> statisticsPanel;
   protected StructureModel<P> model;

   public StructureDisplayPanel(StructureModel<P> model) {
      this.model = model;
      this.setLayout(new BorderLayout());
   }

   public abstract void dispose();

   public UndoBuffer getUndoBuffer() {
      return null;
   }

   public void undo() {
   }
}
