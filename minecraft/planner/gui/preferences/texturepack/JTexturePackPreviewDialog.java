package minecraft.planner.gui.preferences.texturepack;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.textures.JTexturePackPanel;
import minecraft.planner.gui.textures.TexturePack;

public class JTexturePackPreviewDialog extends JDialog {
   public JTexturePackPreviewDialog(TexturePack pack) {
      super(StructurePlanner.getFrame(), "Texture Pack Preview:  " + pack.getName(), true);
      this.setSize(new Dimension(800, 800));
      JTexturePackPanel panel = new JTexturePackPanel();
      panel.displayTexturePack(pack);
      JScrollPane scroll = new JScrollPane();
      scroll.getViewport().add(panel);
      this.getContentPane().add(scroll);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(new Dimension(800, 800));
      this.setLocation(new Point((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2));
      this.setVisible(true);
   }
}
