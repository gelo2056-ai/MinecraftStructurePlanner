package minecraft.planner.gui.textures;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JTexturePackPanel extends JPanel {
   public JTexturePackPanel() {
      this.setLayout(new GridLayout(16, 16));
   }

   public void displayTexturePack(TexturePack texturePack) {
      for (int y = 0; y < 16; y++) {
         for (int x = 0; x < 16; x++) {
            ImageIcon imageIcon = texturePack.getImage(x, y);
            JLabel icon = new JLabel(imageIcon);
            icon.setBorder(BorderFactory.createBevelBorder(1));
            this.add(icon);
         }
      }
   }
}
