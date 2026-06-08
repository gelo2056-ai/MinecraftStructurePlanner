package minecraft.planner.gui.displaypanels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel.1.1;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.StructureParameters;

public abstract class StructureControlPanel<P extends StructureParameters> extends JPanel {
   protected GenerateListener<P> listener;
   protected JButton generate;
   protected ActionListener generateActionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         StructureControlPanel.this.generate.setEnabled(false);
         new Thread(new 1(this)).start();
      }
   };

   public StructureControlPanel(GenerateListener<P> listener) {
      this.listener = listener;
      this.generate = new JButton("Generate");
      this.generate.addActionListener(this.generateActionListener);
      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Configuration"));
   }

   protected void addGenerateButton(int row) {
      this.add(this.generate, new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      Component horizontalStrut = Box.createHorizontalStrut(150);
      this.add(horizontalStrut, new GridBagConstraints(0, row++, 2, 1, 1.0, 1.0, 15, 2, new Insets(0, 0, 0, 0), 0, 0));
      this.add(Box.createVerticalBox(), new GridBagConstraints(0, row++, 2, 1, 1.0, 1.0, 15, 1, new Insets(0, 0, 0, 0), 0, 0));
   }

   protected abstract P generateParameters();

   protected int getInt(JTextField field) {
      String text = field.getText().trim();
      return text.length() == 0 ? 0 : Integer.parseInt(text);
   }
}
