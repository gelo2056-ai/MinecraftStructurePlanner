package minecraft.planner.gui.hemisphere;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.hemisphere.HemisphereParameters;

public class HemisphereControlPanel extends StructureControlPanel<HemisphereParameters> {
   private JTextField radius = new JTextField();
   private JTextField vertical = new JTextField();
   private JTextField baseHeight = new JTextField();
   private JButton calculate = new JButton("Generate Hemisphere");

   public HemisphereControlPanel(GenerateListener<HemisphereParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Radius:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.radius, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(new JLabel("Hemisphere Rise:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.vertical, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(new JLabel("Base Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.baseHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.addGenerateButton(row);
      this.calculate.addActionListener(this.generateActionListener);
   }

   protected HemisphereParameters generateParameters() {
      int radius = this.getInt(this.radius);
      int vertical = this.getInt(this.vertical);
      int baseHeight = this.getInt(this.baseHeight);
      return new HemisphereParameters(radius, vertical, baseHeight);
   }
}
