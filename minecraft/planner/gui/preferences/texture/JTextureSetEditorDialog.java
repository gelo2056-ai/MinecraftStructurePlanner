package minecraft.planner.gui.preferences.texture;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.util.TextureSet;

public class JTextureSetEditorDialog extends JDialog {
   private TextureSet editingTextureSet;
   private JTextureSetEditorDialog.Operation operation;
   private JTextureSetEditorDialog.Button buttonClicked;
   private JTextField nameField;
   private final JTextureSet textureSetPanel;

   public JTextureSetEditorDialog(JTextureSetEditorDialog.Operation operation, TextureSet textureSet) {
      super(StructurePlanner.getFrame(), "Edit Texture Set", true);
      this.operation = operation;
      this.editingTextureSet = null;
      this.setLayout(new BorderLayout());
      JPanel namePanel = new JPanel();
      namePanel.setLayout(new GridBagLayout());
      namePanel.add(new JLabel("Texture Set Name:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
      this.nameField = new JTextField();
      namePanel.add(this.nameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 13, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(namePanel, "North");
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridBagLayout());
      JButton okButton = new JButton("OK");
      JButton cancelButton = new JButton("Cancel");
      okButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (JTextureSetEditorDialog.this.validateFields()) {
                  if (JTextureSetEditorDialog.this.editingTextureSet != null) {
                     JTextureSetEditorDialog.this.editingTextureSet.setTextures(JTextureSetEditorDialog.this.textureSetPanel.getSelectedTextures());
                  } else {
                     JTextureSetEditorDialog.this.editingTextureSet = new TextureSet(
                        JTextureSetEditorDialog.this.nameField.getText().trim(), JTextureSetEditorDialog.this.textureSetPanel.getSelectedTextures(), true
                     );
                  }

                  JTextureSetEditorDialog.this.buttonClicked = JTextureSetEditorDialog.Button.OK;
                  JTextureSetEditorDialog.this.dispose();
               }
            }
         }
      );
      cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JTextureSetEditorDialog.this.buttonClicked = JTextureSetEditorDialog.Button.Cancel;
            JTextureSetEditorDialog.this.setVisible(false);
            JTextureSetEditorDialog.this.dispose();
         }
      });
      buttonPanel.add(okButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      buttonPanel.add(cancelButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      this.add(buttonPanel, "South");
      this.textureSetPanel = new JTextureSet(textureSet);
      this.add(this.textureSetPanel);
      if (operation == JTextureSetEditorDialog.Operation.Edit && textureSet != null) {
         this.nameField.setText(textureSet.getName());
         this.nameField.setEditable(false);
      } else {
         this.nameField.setText("");
         this.nameField.setEditable(true);
      }

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(new Dimension(800, 600));
      this.setLocation(new Point((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2));
   }

   private boolean validateFields() {
      String textureSetName = this.nameField.getText();
      if (textureSetName == null || textureSetName.trim().length() == 0) {
         JOptionPane.showMessageDialog(this, "Please enter a valid texture set name", "Texture Set Error", 0);
         return false;
      } else if (this.textureSetPanel.getSelectedTextures().isEmpty()) {
         JOptionPane.showMessageDialog(this, "Please select at least one texture", "Texture Set Error", 0);
         return false;
      } else {
         return true;
      }
   }

   public JTextureSetEditorDialog.Button getButtonClicked() {
      return this.buttonClicked;
   }

   public TextureSet getTextureSet() {
      return this.editingTextureSet;
   }

   public static void main(String[] argv) {
      JTextureSetEditorDialog dialog = new JTextureSetEditorDialog(JTextureSetEditorDialog.Operation.New, null);
      dialog.setVisible(true);
      if (dialog.getButtonClicked() == JTextureSetEditorDialog.Button.OK) {
         TextureSet foo = dialog.getTextureSet();
         System.out.println(foo.toString() + ": " + foo.getTextures().toString());
      } else {
         System.out.println("Edit canceled");
      }
   }

   public enum Button {
      OK,
      Cancel;
   }

   public enum Operation {
      New,
      Edit;
   }
}
