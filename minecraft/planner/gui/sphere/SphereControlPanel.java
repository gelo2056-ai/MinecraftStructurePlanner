package minecraft.planner.gui.sphere;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.sphere.SphereParameters;

public class SphereControlPanel extends StructureControlPanel<SphereParameters> {
   private JTextField radius = new JTextField();
   private JButton calculate = new JButton("Generate Sphere");

   public SphereControlPanel(GenerateListener<SphereParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Radius:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.radius, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.addGenerateButton(row);
      this.calculate.addActionListener(this.generateActionListener);
   }

   protected SphereParameters generateParameters() {
      int radius = this.getInt(this.radius);
      return new SphereParameters(radius);
   }
}
