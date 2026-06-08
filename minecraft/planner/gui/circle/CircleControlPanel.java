package minecraft.planner.gui.circle;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.circle.CircleParameters;

public class CircleControlPanel extends StructureControlPanel<CircleParameters> {
   private JTextField radius = new JTextField();
   private JCheckBox isSolid = new JCheckBox("Solid");
   private JButton calculate = new JButton("Generate Sphere");

   public CircleControlPanel(GenerateListener<CircleParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Radius:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.radius, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(this.isSolid, new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.isSolid.addActionListener(this.generateActionListener);
      this.addGenerateButton(row);
      this.calculate.addActionListener(this.generateActionListener);
   }

   protected CircleParameters generateParameters() {
      int radius = this.getInt(this.radius);
      boolean isSolid = this.isSolid.isSelected();
      return new CircleParameters(radius, isSolid);
   }
}
