package minecraft.planner.gui.displaypanels;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import minecraft.planner.gui.StructurePlanner;

public class ImagePreviewDialog extends JDialog {
   public ImagePreviewDialog(Image image) {
      super(StructurePlanner.getFrame(), "Image Preview", true);
      JLabel imageLabel = new JLabel(new ImageIcon(image));
      JScrollPane imageScrollPane = new JScrollPane();
      imageScrollPane.getViewport().add(imageLabel);
      new JFrame("Saved Image");
      Dimension size = new Dimension(800, 800);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2);
      this.setSize(size);
      this.setPreferredSize(size);
      this.add(imageScrollPane);
      this.setVisible(true);
   }
}
