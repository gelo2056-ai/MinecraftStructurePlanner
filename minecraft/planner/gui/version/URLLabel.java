package minecraft.planner.gui.version;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Desktop$Action;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URL;
import javax.swing.JLabel;

public class URLLabel extends JLabel implements MouseListener {
   private boolean entered = false;
   private boolean mousehover;
   private URI uri;

   public URLLabel(URL url) {
      this(url, url.toString());
   }

   public URLLabel(URL url, String str) {
      super(str);
      this.setForeground(Color.blue);
      this.mousehover = false;

      try {
         this.uri = new URI(url.toString());
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.addMouseListener(this);
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      this.setCursor(Cursor.getPredefinedCursor(12));
      this.entered = true;
      if (this.mousehover) {
         this.repaint();
      }
   }

   @Override
   public void mouseExited(MouseEvent e) {
      this.setCursor(Cursor.getDefaultCursor());
      this.entered = false;
      if (this.mousehover) {
         this.repaint();
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {
   }

   @Override
   public void mouseReleased(MouseEvent e) {
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      if (this.entered || !this.mousehover) {
         Rectangle r = g.getClipBounds();
         g.drawLine(
            0,
            r.height - this.getFontMetrics(this.getFont()).getDescent(),
            this.getFontMetrics(this.getFont()).stringWidth(this.getText()),
            r.height - this.getFontMetrics(this.getFont()).getDescent()
         );
      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      this.browseURL();
   }

   public void browseURL() {
      try {
         Desktop desktop = Desktop.getDesktop();
         if (desktop.isSupported(Desktop$Action.BROWSE)) {
            desktop.browse(this.uri);
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
