package minecraft.planner.gui.preferences.texturepack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.preferences.texturepack.JTexturePackLoaderDialog.4.1;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.UserConfigurationManager;

public class JTexturePackLoaderDialog extends JDialog {
   private TexturePack texturePack;
   private JTexturePackLoaderDialog.Button buttonClicked;
   private final JTextField texturePackName = new JTextField();
   private final JTextField texturePackFile = new JTextField();
   private final JLabel texturePackIcon;
   private final JTextArea texturePackNotes;
   private File chosenFile = null;
   private final JButton okButton;

   public JTexturePackLoaderDialog() {
      super(StructurePlanner.getFrame(), "Load Texture Pack", true);
      this.setLayout(new BorderLayout());
      JPanel texturePackPanel = new JPanel();
      JButton getFile = new JButton("Choose File...");
      this.texturePackIcon = new JLabel(getResourceIcon("no-pack.png"));
      this.texturePackIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      this.texturePackNotes = new JTextArea();
      this.texturePackNotes.setEditable(false);
      this.texturePackNotes.setBorder(BorderFactory.createBevelBorder(1));
      this.texturePackFile.setEditable(false);
      texturePackPanel.setLayout(new GridBagLayout());
      texturePackPanel.add(new JLabel("Texture Pack Name:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
      texturePackPanel.add(this.texturePackName, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, 13, 2, new Insets(5, 5, 5, 5), 0, 0));
      texturePackPanel.add(new JLabel("Texture Pack File:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
      texturePackPanel.add(this.texturePackFile, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 17, 2, new Insets(5, 5, 5, 5), 0, 0));
      texturePackPanel.add(getFile, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
      JPanel texturePackInfoPanel = new JPanel();
      texturePackInfoPanel.setLayout(new GridBagLayout());
      texturePackInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Texture Pack Info"));
      texturePackInfoPanel.add(this.texturePackIcon, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 20, 15, 15), 0, 0));
      texturePackInfoPanel.add(this.texturePackNotes, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 13, 1, new Insets(5, 5, 15, 15), 0, 0));
      texturePackPanel.add(texturePackInfoPanel, new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0, 15, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.add(texturePackPanel, "Center");
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridBagLayout());
      this.okButton = new JButton("OK");
      JButton cancelButton = new JButton("Cancel");
      this.okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JTexturePackLoaderDialog.this.buttonClicked = JTexturePackLoaderDialog.Button.OK;
            JTexturePackLoaderDialog.this.dispose();
         }
      });
      cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JTexturePackLoaderDialog.this.buttonClicked = JTexturePackLoaderDialog.Button.Cancel;
            JTexturePackLoaderDialog.this.setVisible(false);
            JTexturePackLoaderDialog.this.dispose();
         }
      });
      this.texturePackName.addKeyListener(new KeyListener() {
         @Override
         public void keyTyped(KeyEvent e) {
         }

         @Override
         public void keyPressed(KeyEvent e) {
            JTexturePackLoaderDialog.this.validateOKButton();
         }

         @Override
         public void keyReleased(KeyEvent e) {
         }
      });
      getFile.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(UserConfigurationManager.getCurrentFilesystemPath());
            fileChooser.setFileFilter(new 1(this));
            fileChooser.setMultiSelectionEnabled(false);
            int result = fileChooser.showOpenDialog(JTexturePackLoaderDialog.this);
            if (result == 0) {
               JTexturePackLoaderDialog.this.chosenFile = fileChooser.getSelectedFile();
               if (JTexturePackLoaderDialog.this.chosenFile != null) {
                  UserConfigurationManager.setCurrentFilesystemPath(JTexturePackLoaderDialog.this.chosenFile.getParentFile());
               }

               JTexturePackLoaderDialog.this.updateTexturePackInfo(JTexturePackLoaderDialog.this.chosenFile);
               JTexturePackLoaderDialog.this.validateOKButton();
            }
         }
      });
      buttonPanel.add(this.okButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      buttonPanel.add(cancelButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      this.add(buttonPanel, "South");
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(new Dimension(640, 380));
      this.setLocation(new Point((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2));
      this.validateOKButton();
   }

   private void updateTexturePackInfo(File texturePack) {
      this.chosenFile = texturePack;
      this.texturePackFile.setText(texturePack.getAbsolutePath().toString());
      ZipFile zipFile = null;

      try {
         zipFile = new ZipFile(texturePack);
         ZipEntry packImageFile = zipFile.getEntry("pack.png");
         if (packImageFile != null) {
            InputStream is = zipFile.getInputStream(packImageFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] imageData = new byte[(int)packImageFile.getSize()];
            bis.read(imageData);
            this.texturePackIcon.setIcon(new ImageIcon(imageData));
         }

         ZipEntry packNotesFile = zipFile.getEntry("pack.txt");
         if (packNotesFile != null) {
            InputStream is = zipFile.getInputStream(packNotesFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            this.texturePackNotes.setText("");
            String line = null;

            do {
               line = reader.readLine();
               if (line != null) {
                  this.texturePackNotes.append(line + "\n");
               }
            } while (line != null);
         }
      } catch (Exception var16) {
      } finally {
         try {
            zipFile.close();
         } catch (Exception var15) {
         }
      }
   }

   private void validateOKButton() {
      this.okButton.setEnabled(this.texturePackName.getText().trim().length() > 0 && this.texturePackFile.getText().trim().length() > 0);
   }

   private static ImageIcon getResourceIcon(String resourceName) {
      try {
         return new ImageIcon(JTexturePackLoaderDialog.class.getResource("/minecraft/planner/resources/" + resourceName));
      } catch (Exception e) {
         System.err.println("Could not load image icon " + resourceName);
         return null;
      }
   }

   public JTexturePackLoaderDialog.Button getButtonClicked() {
      return this.buttonClicked;
   }

   public String getTexturePackName() {
      return this.texturePackName.getText().trim();
   }

   public File getTexturePackFile() {
      return this.chosenFile;
   }

   public enum Button {
      OK,
      Cancel;
   }
}
