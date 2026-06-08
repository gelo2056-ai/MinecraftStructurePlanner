package minecraft.planner.gui.easterisland;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.easterisland.EasterIslandModel;
import minecraft.planner.model.easterisland.EasterIslandParameters;
import minecraft.planner.util.UserConfigurationManager;

public class EasterIslandControlPanel extends StructureControlPanel<EasterIslandParameters> {
   private JLabel image = new JLabel();
   private ImageIcon character;
   private JButton calculate = new JButton("Generate");
   private EasterIslandModel model;
   private final Color DEFAULT_SELECTED_COLOR;
   private final Color DEFAULT_UNSELECTED_COLOR;
   protected final JFileChooser imageChooser;

   public EasterIslandControlPanel(EasterIslandModel model) {
      super(model);
      UIDefaults uiDefaults = UIManager.getDefaults();
      this.DEFAULT_SELECTED_COLOR = uiDefaults.getColor("List.selectionBackground");
      this.DEFAULT_UNSELECTED_COLOR = this.image.getBackground();
      this.model = model;
      int row = 0;
      this.add(new JLabel("Minecraft Skin:"), new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.image, new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.imageChooser = new JFileChooser(UserConfigurationManager.getCurrentFilesystemPath());
      this.imageChooser.setMultiSelectionEnabled(false);
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "gif", "png", "bmp");
      this.imageChooser.setFileFilter(filter);
      this.image.setPreferredSize(new Dimension(200, 200));
      this.image.setOpaque(true);
      this.image.setBorder(BorderFactory.createEtchedBorder(0));
      this.image.setHorizontalAlignment(0);
      this.image.setVerticalAlignment(0);
      this.image.setToolTipText("Click here to select skin file...");
      super.generate.setEnabled(false);
      this.image
         .addMouseListener(
            new MouseListener() {
               @Override
               public void mouseClicked(MouseEvent arg0) {
                  int returnValue = EasterIslandControlPanel.this.imageChooser.showOpenDialog(EasterIslandControlPanel.this);
                  if (returnValue == 0) {
                     File imageFile = EasterIslandControlPanel.this.imageChooser.getSelectedFile();
                     if (imageFile != null) {
                        UserConfigurationManager.setCurrentFilesystemPath(imageFile.getParentFile());
                     }

                     EasterIslandControlPanel.this.character = new ImageIcon(imageFile.getAbsolutePath());
                     if (EasterIslandControlPanel.this.character.getIconWidth() == 64 && EasterIslandControlPanel.this.character.getIconHeight() == 32) {
                        Graphics2D g2D = null;

                        try {
                           BufferedImage bufferedImage = new BufferedImage(200, 200, 1);
                           g2D = bufferedImage.createGraphics();
                           g2D.drawImage(EasterIslandControlPanel.this.character.getImage(), 0, 0, 199, 199, 8, 8, 16, 16, null);
                           EasterIslandControlPanel.this.image.setIcon(new ImageIcon(bufferedImage));
                           EasterIslandControlPanel.this.generate.setEnabled(true);
                        } catch (Throwable var14) {
                        } finally {
                           if (g2D != null) {
                              try {
                                 g2D.dispose();
                                 Graphics2D var16 = null;
                              } catch (Throwable var13) {
                              }
                           }
                        }
                     } else {
                        JOptionPane.showMessageDialog(
                           StructurePlanner.getFrame(),
                           "File " + imageFile.getName() + " does not appear to be a valid Minecraft skin.",
                           "Easter Island Model",
                           0
                        );
                     }
                  }
               }

               @Override
               public void mouseEntered(MouseEvent arg0) {
                  EasterIslandControlPanel.this.image.setBackground(EasterIslandControlPanel.this.DEFAULT_SELECTED_COLOR);
                  EasterIslandControlPanel.this.image.repaint();
               }

               @Override
               public void mouseExited(MouseEvent arg0) {
                  EasterIslandControlPanel.this.image.setBackground(EasterIslandControlPanel.this.DEFAULT_UNSELECTED_COLOR);
                  EasterIslandControlPanel.this.image.repaint();
               }

               @Override
               public void mousePressed(MouseEvent arg0) {
               }

               @Override
               public void mouseReleased(MouseEvent arg0) {
               }
            }
         );
      this.addGenerateButton(row);
      this.calculate.addActionListener(this.generateActionListener);
   }

   public void setParameters(EasterIslandParameters parameters) {
      this.image.setIcon(parameters.getCharacterImage());
   }

   protected EasterIslandParameters generateParameters() {
      ImageIcon icon = this.character;
      return new EasterIslandParameters(icon);
   }

   protected final ImageIcon scaleImage(Image image, double scale) {
      int width = (int)(scale * image.getWidth(this));
      int height = (int)(scale * image.getHeight(this));
      BufferedImage destination = new BufferedImage(width, height, 1);
      Graphics2D g2 = destination.createGraphics();
      g2.drawImage(image, 0, 0, width, height, this);
      g2.dispose();
      return new ImageIcon(destination);
   }
}
