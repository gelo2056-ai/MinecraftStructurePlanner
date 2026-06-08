package minecraft.planner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JErrorDialog extends JDialog {
   public JErrorDialog(JFrame parentFrame, String errorMessage, Throwable t) {
      super(parentFrame, "Error within Minecraft Structure Planner", true);
      this.setDefaultCloseOperation(2);
      this.setLayout(new GridBagLayout());
      Dimension size = new Dimension(640, 480);
      this.setSize(size);
      this.setPreferredSize(size);
      this.setResizable(false);
      if (parentFrame != null) {
         this.setLocation(
            (int)parentFrame.getLocation().getX() + (parentFrame.getWidth() - 640) / 2,
            (int)parentFrame.getLocation().getY() + (parentFrame.getHeight() - 480) / 2
         );
      }

      int row = 0;
      this.add(this.leftAlignedLabel("Error:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 11, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(this.leftAlignedLabel(errorMessage, Color.RED), new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 11, 2, new Insets(5, 0, 5, 5), 0, 0));
      this.add(this.leftAlignedLabel("Exception:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 11, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(this.leftAlignedLabel(t.toString(), Color.RED), new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 11, 2, new Insets(5, 0, 5, 5), 0, 0));
      JTextArea textArea = new JTextArea();
      textArea.setEditable(false);
      textArea.setBorder(BorderFactory.createBevelBorder(1));
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      t.printStackTrace(printWriter);
      textArea.append(stringWriter.toString());
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.getViewport().add(textArea);
      scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Stack Trace"));
      this.add(scrollPane, new GridBagConstraints(0, row++, 2, 1, 1.0, 1.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
      JButton okButton = new JButton("OK");
      this.add(okButton, new GridBagConstraints(0, row, 2, 1, 1.0, 0.0, 15, 0, new Insets(5, 5, 5, 5), 0, 0));
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            JErrorDialog.this.dispose();
         }
      });
      this.setVisible(true);
   }

   private JLabel leftAlignedLabel(String text) {
      return this.leftAlignedLabel(text, Color.BLACK);
   }

   private JLabel leftAlignedLabel(String text, Color color) {
      JLabel label = new JLabel(text);
      label.setForeground(color);
      label.setHorizontalTextPosition(2);
      return label;
   }
}
