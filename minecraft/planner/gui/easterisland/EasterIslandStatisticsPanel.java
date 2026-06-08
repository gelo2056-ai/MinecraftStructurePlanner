package minecraft.planner.gui.easterisland;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.JCollapsablePanel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StatisticsChangeListener;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.easterisland.EasterIslandParameters;
import minecraft.planner.model.grid.WorldGrid$GridStatistics;

public class EasterIslandStatisticsPanel extends JCollapsablePanel implements StatisticsChangeListener {
   private static final int NUMBER_OF_COLUMNS = 4;
   protected StructureModel<EasterIslandParameters> model;
   private Map<TextureName, JTextField> textureFields;

   public EasterIslandStatisticsPanel(StructureModel<EasterIslandParameters> model) {
      super("Material Requirements", false, true);
      Container contentPane = this.getContentPane();
      contentPane.setLayout(new GridBagLayout());
      this.textureFields = new HashMap<>();
      TextureName[] names = TextureName.values();
      int count = 0;
      TextureName[] var8 = names;
      int var7 = names.length;

      for (int var6 = 0; var6 < var7; var6++) {
         TextureName name = var8[var6];
         if (name.isTexturable() && name != TextureName.None) {
            JLabel nameLabel = new JLabel(name.getDisplayName() + ":");
            JTextField valueField = new JTextField("");
            valueField.setFocusable(false);
            valueField.setHorizontalAlignment(4);
            Dimension size = new Dimension(70, 20);
            valueField.setPreferredSize(size);
            valueField.setMinimumSize(size);
            valueField.setBorder(BorderFactory.createBevelBorder(1));
            this.textureFields.put(name, valueField);
            int column = count % 4 * 3;
            int row = (int)Math.floor(count / 4);
            int leftInset = 0;
            if (column == 0) {
               leftInset = 5;
            }

            contentPane.add(nameLabel, new GridBagConstraints(column, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, leftInset, 2, 5), 0, 0));
            contentPane.add(valueField, new GridBagConstraints(column + 1, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 0, 2, 10), 0, 0));
            contentPane.add(Box.createHorizontalBox(), new GridBagConstraints(column + 2, row, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 2, 0), 0, 0));
            count++;
         }
      }

      model.getWorldGrid().getStatistics().registerStatisticsChangeListener(this);
      this.statisticsChanged(model.getWorldGrid().getStatistics());
   }

   @Override
   public void statisticsChanged(WorldGrid$GridStatistics statistics) {
      for (TextureName name : this.textureFields.keySet()) {
         JTextField valueField = this.textureFields.get(name);
         int value = statistics.getCount(name);
         if (value > 0) {
            valueField.setText("" + value);
         } else {
            valueField.setText("");
         }
      }
   }
}
