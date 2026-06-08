package minecraft.planner.gui.version;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import minecraft.planner.gui.java3d.TextureCache;

public class TitlePanel extends JPanel {
   private static final Font TITLE_FONT = new Font("Serif", 1, 30);
   private static final Font SUBTITLE_FONT = new Font("Serif", 1, 20);
   private ImageIcon icon;
   private String title;
   private String subTitle;

   public TitlePanel(String iconName, String titleText) {
      this.icon = new ImageIcon(TextureCache.class.getClass().getResource("/minecraft/planner/resources/" + iconName));
      this.title = titleText;
      this.subTitle = null;
      Dimension size = new Dimension(this.icon.getIconWidth(), this.icon.getIconHeight());
      this.setMinimumSize(size);
      this.setMaximumSize(size);
      this.setPreferredSize(size);
   }

   public TitlePanel(String iconName, String titleText, String subTitleText) {
      this.icon = new ImageIcon(TextureCache.class.getClass().getResource("/minecraft/planner/resources/" + iconName));
      this.title = titleText;
      this.subTitle = subTitleText;
      this.setBorder(BorderFactory.createEtchedBorder(0));
      Dimension size = new Dimension(this.icon.getIconWidth(), this.icon.getIconHeight());
      this.setMinimumSize(size);
      this.setMaximumSize(size);
      this.setPreferredSize(size);
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.drawImage(this.icon.getImage(), 3, 3, this.getWidth() - 5, this.getHeight() - 5, Color.WHITE, this);
      if (this.subTitle == null) {
         g2.setFont(TITLE_FONT);
         g2.setColor(Color.WHITE);
         FontRenderContext context = g2.getFontRenderContext();
         Rectangle2D bounds = TITLE_FONT.getStringBounds(this.title, context);
         g2.drawString(this.title, (int)((this.getWidth() - bounds.getWidth()) / 2.0), (int)(bounds.getHeight() * 2.0));
      } else {
         g2.setFont(TITLE_FONT);
         g2.setColor(Color.WHITE);
         FontRenderContext context = g2.getFontRenderContext();
         Rectangle2D titleBounds = TITLE_FONT.getStringBounds(this.title, context);
         Rectangle2D subtitleBounds = SUBTITLE_FONT.getStringBounds(this.title, context);
         int mainHeight = (int)(titleBounds.getHeight() * 2.0);
         g2.drawString(this.title, (int)((this.getWidth() - titleBounds.getWidth()) / 2.0), mainHeight);
         g2.drawString(this.subTitle, (int)((this.getWidth() - subtitleBounds.getWidth()) / 2.0), (int)(mainHeight + titleBounds.getHeight() + 10.0));
      }
   }
}
