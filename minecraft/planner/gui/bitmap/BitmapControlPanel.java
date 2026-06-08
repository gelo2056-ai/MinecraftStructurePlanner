package minecraft.planner.gui.bitmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.bitmap.BitmapModel;
import minecraft.planner.model.bitmap.BitmapParameters;
import minecraft.planner.util.UserConfigurationManager;

public class BitmapControlPanel extends StructureControlPanel<BitmapParameters> {
   private JTextField width = new JTextField();
   private JTextField height = new JTextField();
   private JLabel imageWidth = new JLabel();
   private JLabel imageHeight = new JLabel();
   private JCheckBox lockAspectRatio = new JCheckBox("Lock aspect ratio");
   private JLabel image = new JLabel();
   private ImageIcon background;
   private JButton calculate = new JButton("Generate");
   private BitmapModel model;
   private double aspectRatio;
   private final Color DEFAULT_SELECTED_COLOR;
   private final Color DEFAULT_UNSELECTED_COLOR;
   protected final JFileChooser imageChooser;

   public BitmapControlPanel(BitmapModel model) {
      super(model);
      UIDefaults uiDefaults = UIManager.getDefaults();
      this.DEFAULT_SELECTED_COLOR = uiDefaults.getColor("List.selectionBackground");
      this.DEFAULT_UNSELECTED_COLOR = this.image.getBackground();
      this.model = model;
      int row = 0;
      this.add(new JLabel("Image:"), new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.image, new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.add(new JLabel("Image Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.imageWidth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      this.add(new JLabel("Image Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      this.add(this.imageHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      this.add(this.lockAspectRatio, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(new JLabel("Grid Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.width, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(new JLabel("Grid Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.height, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.lockAspectRatio.setSelected(true);
      this.imageChooser = new JFileChooser(UserConfigurationManager.getCurrentFilesystemPath());
      this.imageChooser.setMultiSelectionEnabled(false);
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "gif", "png", "bmp");
      this.imageChooser.setFileFilter(filter);
      this.image.setPreferredSize(new Dimension(200, 200));
      this.image.setOpaque(true);
      this.image.setBorder(BorderFactory.createEtchedBorder(0));
      this.image.setHorizontalAlignment(0);
      this.image.setVerticalAlignment(0);
      this.image.setToolTipText("Click here to select image file...");
      this.width.setEnabled(false);
      this.height.setEnabled(false);
      super.generate.setEnabled(false);
      this.image
         .addMouseListener(
            new MouseListener() {
               @Override
               public void mouseClicked(MouseEvent arg0) {
                  int returnValue = BitmapControlPanel.this.imageChooser.showOpenDialog(BitmapControlPanel.this);
                  if (returnValue == 0) {
                     File imageFile = BitmapControlPanel.this.imageChooser.getSelectedFile();
                     if (imageFile != null) {
                        UserConfigurationManager.setCurrentFilesystemPath(imageFile.getParentFile());
                     }

                     BitmapControlPanel.this.background = new ImageIcon(imageFile.getAbsolutePath());
                     BitmapControlPanel.this.aspectRatio = (double)BitmapControlPanel.this.background.getIconWidth()
                        / BitmapControlPanel.this.background.getIconHeight();
                     int largestDimension = Math.max(BitmapControlPanel.this.background.getIconWidth(), BitmapControlPanel.this.background.getIconHeight());
                     double scale = 200.0 / largestDimension;
                     BitmapControlPanel.this.image.setIcon(BitmapControlPanel.this.scaleImage(BitmapControlPanel.this.background.getImage(), scale));
                     BitmapControlPanel.this.imageWidth.setText(BitmapControlPanel.this.background.getIconWidth() + " pixels");
                     BitmapControlPanel.this.imageHeight.setText(BitmapControlPanel.this.background.getIconHeight() + " pixels");
                     BitmapControlPanel.this.width.setEnabled(true);
                     BitmapControlPanel.this.height.setEnabled(true);
                     BitmapControlPanel.this.generate.setEnabled(true);
                  }
               }

               @Override
               public void mouseEntered(MouseEvent arg0) {
                  BitmapControlPanel.this.image.setBackground(BitmapControlPanel.this.DEFAULT_SELECTED_COLOR);
                  BitmapControlPanel.this.image.repaint();
               }

               @Override
               public void mouseExited(MouseEvent arg0) {
                  BitmapControlPanel.this.image.setBackground(BitmapControlPanel.this.DEFAULT_UNSELECTED_COLOR);
                  BitmapControlPanel.this.image.repaint();
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
      this.width.addFocusListener(new FocusListener() {
         @Override
         public void focusLost(FocusEvent e) {
            if (BitmapControlPanel.this.lockAspectRatio.isSelected()) {
               try {
                  int w = Integer.parseInt(BitmapControlPanel.this.width.getText());
                  BitmapControlPanel.this.height.setText("" + (int)(w / BitmapControlPanel.this.aspectRatio));
               } catch (Exception var3) {
               }
            }
         }

         @Override
         public void focusGained(FocusEvent e) {
         }
      });
      this.height.addFocusListener(new FocusListener() {
         @Override
         public void focusLost(FocusEvent e) {
            if (BitmapControlPanel.this.lockAspectRatio.isSelected()) {
               try {
                  int h = Integer.parseInt(BitmapControlPanel.this.height.getText());
                  BitmapControlPanel.this.width.setText("" + (int)(h * BitmapControlPanel.this.aspectRatio));
               } catch (Exception var3) {
               }
            }
         }

         @Override
         public void focusGained(FocusEvent e) {
         }
      });
   }

   public void setParameters(BitmapParameters parameters) {
      this.width.setText("" + parameters.getWidth());
      this.height.setText("" + parameters.getHeight());
      this.image.setIcon(parameters.getBackgroundImage());
      this.lockAspectRatio.setSelected(parameters.getLockAspectRatio());
   }

   protected BitmapParameters generateParameters() {
      int width = this.getInt(this.width);
      int height = this.getInt(this.height);
      boolean lock = this.lockAspectRatio.isSelected();
      ImageIcon icon = this.background;
      return new BitmapParameters(icon, width, height, lock);
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
