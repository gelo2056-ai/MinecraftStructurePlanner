package minecraft.planner.gui.displaypanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;

public class ThreeDimensionalStatisticsPanel<P extends StructureParameters> extends StatisticsPanel<P> implements StructureModelChangeListener {
   private JLabel blockCount = new JLabel();
   private JLabel visibleCount = new JLabel();

   public ThreeDimensionalStatisticsPanel(StructureModel<P> model) {
      super(model);
      int row = 0;
      String blocksLabel = null;
      if (!this.model.includeVisibleBlocksParameter()) {
         blocksLabel = "Total number of blocks:";
      } else {
         blocksLabel = "Blocks if structure is solid:";
      }

      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Structure Statistics"));
      this.add(new JLabel(blocksLabel), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.blockCount, new GridBagConstraints(1, row++, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 5), 0, 0));
      if (this.model.includeVisibleBlocksParameter()) {
         this.add(new JLabel("Blocks if structure is hollow:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
         this.add(this.visibleCount, new GridBagConstraints(1, row++, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 5), 0, 0));
      }

      this.add(Box.createVerticalBox(), new GridBagConstraints(2, 0, 1, row, 1.0, 1.0, 15, 1, new Insets(0, 0, 0, 0), 0, 0));
   }

   @Override
   public void structureModelChanged(Object originator) {
      super.structureModelChanged(originator);
      if (this.model != null && this.model.getWorldGrid() != null) {
         this.blockCount.setText("" + this.model.getWorldGrid().getTotalBlocks());
         if (this.model.includeVisibleBlocksParameter()) {
            this.blockCount.setText("" + this.model.getWorldGrid().getTotalBlocks());
            this.visibleCount.setText("" + this.model.getWorldGrid().getVisibleBlocks());
         } else {
            this.blockCount.setText("" + this.model.getWorldGrid().getVisibleBlocks());
         }
      }
   }
}
