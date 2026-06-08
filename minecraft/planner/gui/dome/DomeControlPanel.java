package minecraft.planner.gui.dome;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.dome.DomeParameters;

public class DomeControlPanel extends StructureControlPanel<DomeParameters> {
   private JTextField width = new JTextField();
   private JTextField height = new JTextField();
   private JTextField vertical = new JTextField();
   private JTextField baseHeight = new JTextField();
   private JCheckBox xInterpolate = new JCheckBox("X Interpolate");
   private JCheckBox yInterpolate = new JCheckBox("Y Interpolate");
   private JCheckBox squareBase = new JCheckBox("Square the base");
   private JButton calculate = new JButton("Generate Dome");

   public DomeControlPanel(GenerateListener<DomeParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.width, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(new JLabel("Depth:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.height, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(new JLabel("Dome Rise:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.vertical, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(new JLabel("Base Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.baseHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.xInterpolate, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.yInterpolate, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.squareBase, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.addGenerateButton(row);
      this.xInterpolate.setSelected(true);
      this.yInterpolate.setSelected(true);
      this.squareBase.setSelected(true);
      this.xInterpolate.setSelected(true);
      this.yInterpolate.setSelected(true);
      this.squareBase.setSelected(true);
      this.xInterpolate.addActionListener(this.generateActionListener);
      this.yInterpolate.addActionListener(this.generateActionListener);
      this.squareBase.addActionListener(this.generateActionListener);
      this.calculate.addActionListener(this.generateActionListener);
   }

   protected DomeParameters generateParameters() {
      int width = this.getInt(this.width);
      int height = this.getInt(this.height);
      int vertical = this.getInt(this.vertical);
      int baseHeight = this.getInt(this.baseHeight);
      return new DomeParameters(
         width, height, vertical, baseHeight, this.xInterpolate.isSelected(), this.yInterpolate.isSelected(), this.squareBase.isSelected()
      );
   }
}
