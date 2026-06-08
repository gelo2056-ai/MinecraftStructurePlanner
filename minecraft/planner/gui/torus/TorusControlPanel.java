package minecraft.planner.gui.torus;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.torus.TorusParameters;

public class TorusControlPanel extends StructureControlPanel<TorusParameters> {
   private JTextField majorRadius = new JTextField();
   private JTextField minorRadius = new JTextField();
   private JButton calculate = new JButton("Generate Torus");

   public TorusControlPanel(GenerateListener<TorusParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Major radius:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.majorRadius, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(new JLabel("Minor Radius:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.minorRadius, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.addGenerateButton(row);
      this.calculate.addActionListener(this.generateActionListener);
   }

   protected TorusParameters generateParameters() {
      int majorRadius = this.getInt(this.majorRadius);
      int minorRadius = this.getInt(this.minorRadius);
      return new TorusParameters(majorRadius, minorRadius);
   }
}
