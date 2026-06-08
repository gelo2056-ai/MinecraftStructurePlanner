package minecraft.planner.gui.displaypanels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StatisticsChangeListener;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.freeform.FreeformParameters;
import minecraft.planner.model.grid.WorldGrid$GridStatistics;

public class ThreeDimensionalManifestPanel<P extends StructureParameters>
   extends StatisticsPanel<P>
   implements StructureModelChangeListener,
   StatisticsChangeListener {
   private static final int NUMBER_OF_COLUMNS = 4;
   protected StructureModel<FreeformParameters> model;

   public ThreeDimensionalManifestPanel(StructureModel<P> model) {
      super(model);
      this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Material Requirements"));
      this.setLayout(new GridBagLayout());
      this.add(Box.createVerticalStrut(40), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 15, 3, new Insets(0, 0, 0, 0), 0, 0));
      model.getWorldGrid().getStatistics().registerStatisticsChangeListener(this);
      this.statisticsChanged(model.getWorldGrid().getStatistics());
   }

   @Override
   public void statisticsChanged(WorldGrid$GridStatistics statistics) {
      this.removeAll();
      TextureName[] names = TextureName.values();
      int count = 0;
      TextureName[] var7 = names;
      int var6 = names.length;

      for (int var5 = 0; var5 < var6; var5++) {
         TextureName name = var7[var5];
         if (name.isTexturable() && name != TextureName.None) {
            int value = statistics.getCount(name);
            if (value > 0) {
               JLabel nameLabel = new JLabel(name.getDisplayName() + ":");
               JTextField valueField = new JTextField("" + value);
               valueField.setFocusable(false);
               valueField.setHorizontalAlignment(4);
               Dimension size = new Dimension(70, 20);
               valueField.setPreferredSize(size);
               valueField.setMinimumSize(size);
               valueField.setBorder(BorderFactory.createBevelBorder(1));
               int column = count % 4 * 3;
               int row = (int)Math.floor(count / 4);
               int leftInset = 0;
               if (column == 0) {
                  leftInset = 5;
               }

               this.add(nameLabel, new GridBagConstraints(column, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, leftInset, 2, 5), 0, 0));
               this.add(valueField, new GridBagConstraints(column + 1, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 0, 2, 10), 0, 0));
               this.add(Box.createHorizontalBox(), new GridBagConstraints(column + 2, row, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 2, 0), 0, 0));
               count++;
            }
         }

         if (count == 0) {
            this.add(Box.createVerticalStrut(40), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 15, 3, new Insets(0, 0, 0, 0), 0, 0));
         }
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ThreeDimensionalManifestPanel.this.revalidate();
            ThreeDimensionalManifestPanel.this.repaint();
         }
      });
   }
}
