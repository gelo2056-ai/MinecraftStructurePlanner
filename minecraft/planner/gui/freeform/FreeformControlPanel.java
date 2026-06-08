package minecraft.planner.gui.freeform;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.freeform.FreeformParameters;

public class FreeformControlPanel extends StructureControlPanel<FreeformParameters> {
   private JTextField x = new JTextField();
   private JTextField y = new JTextField();
   private JTextField maxHeight = new JTextField();
   private JButton calculate = new JButton("Generate");
   private StructureModel<FreeformParameters> model;

   public FreeformControlPanel(StructureModel<FreeformParameters> model) {
      super(model);
      this.model = model;
      int row = 0;
      this.add(new JLabel("Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.x, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(new JLabel("Depth:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.y, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(new JLabel("Max Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.maxHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.addGenerateButton(row);
      this.calculate.addActionListener(this.generateActionListener);
   }

   public void setParameters(FreeformParameters parameters) {
      this.x.setText("" + parameters.getX());
      this.y.setText("" + parameters.getY());
      this.maxHeight.setText("" + parameters.getMaxHeight());
   }

   protected FreeformParameters generateParameters() {
      int x = this.getInt(this.x);
      int y = this.getInt(this.y);
      int maxHeight = this.getInt(this.maxHeight);
      return new FreeformParameters(x, y, maxHeight);
   }
}
