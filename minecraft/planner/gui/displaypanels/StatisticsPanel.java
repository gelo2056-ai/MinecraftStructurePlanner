package minecraft.planner.gui.displaypanels;

import javax.swing.JPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;

public class StatisticsPanel<P extends StructureParameters> extends JPanel implements StructureModelChangeListener {
   protected StructureModel<P> model;

   public StatisticsPanel(StructureModel<P> model) {
      this.model = model;
      this.model.registerListener(this);
   }

   @Override
   public void structureModelChanged(Object originator) {
   }
}
