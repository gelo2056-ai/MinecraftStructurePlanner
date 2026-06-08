package minecraft.planner.gui.bridge;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.bridge.BridgeParameters;

public class BridgeControlPanel extends StructureControlPanel<BridgeParameters> {
   private JTextField span = new JTextField();
   private JTextField width = new JTextField();
   private JTextField maxHeight = new JTextField();
   private JTextField wallHeight = new JTextField();
   private JCheckBox fullySupported = new JCheckBox("Fully supported");

   public BridgeControlPanel(GenerateListener<BridgeParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Span:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.span, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.width, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Bridge Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.maxHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Wall Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.wallHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(this.fullySupported, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.addGenerateButton(row);
      this.fullySupported.setSelected(true);
      this.fullySupported.addActionListener(this.generateActionListener);
   }

   protected BridgeParameters generateParameters() {
      return new BridgeParameters(
         this.getInt(this.span), this.getInt(this.width), this.getInt(this.maxHeight), 1, this.getInt(this.wallHeight), this.fullySupported.isSelected(), false
      );
   }
}
