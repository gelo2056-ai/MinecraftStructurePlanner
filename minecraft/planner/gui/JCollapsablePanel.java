package minecraft.planner.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class JCollapsablePanel extends JPanel implements MouseListener, MouseMotionListener {
   private JPanel contentPanel;
   private boolean expanded;
   private boolean collapsedButtonIsUpArrow;
   private String panelTitle;
   private Rectangle arrowBox;
   private Rectangle textBox;
   private TitledBorder border;
   private Color titleColor;
   private static final Component verticalSpacer = Box.createVerticalStrut(5);
   private static final GridBagConstraints contentPanelConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0);
   private boolean tracking = false;

   public JCollapsablePanel(String panelTitle) {
      this(panelTitle, true, true);
   }

   public JCollapsablePanel(String panelTitle, boolean expanded, boolean collapsedButtonIsUpArrow) {
      this.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), " ");
      this.setBorder(this.border);
      this.titleColor = this.border.getTitleColor();
      this.panelTitle = panelTitle;
      this.setLayout(new GridBagLayout());
      this.contentPanel = new JPanel();
      this.expanded = expanded;
      this.collapsedButtonIsUpArrow = collapsedButtonIsUpArrow;
      if (expanded) {
         this.add(this.contentPanel, contentPanelConstraints);
      } else {
         this.add(verticalSpacer, contentPanelConstraints);
      }

      this.arrowBox = new Rectangle(0, 0, 0, 0);
      this.textBox = new Rectangle(0, 0, 0, 0);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
   }

   public Container getContentPane() {
      return this.contentPanel;
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      if (g instanceof Graphics2D) {
         Graphics2D g2 = (Graphics2D)g;
         Font font = this.border.getTitleFont();
         FontMetrics metrics = g2.getFontMetrics(font);
         Rectangle2D textRectangle = metrics.getStringBounds(this.panelTitle, g2);
         g2.setColor(this.getBackground());
         int textWidth = (int)Math.ceil(textRectangle.getWidth());
         int textHeight = (int)Math.ceil(textRectangle.getHeight());
         g2.fillRect(8, 2, textWidth + 4, textHeight + 2);
         g2.setColor(this.titleColor);
         g2.setFont(font);
         g2.drawString(this.panelTitle, 10.0F, 14.0F);
         this.textBox.setRect(8.0, 2.0, textWidth + 4, textHeight + 2);
         g2.setColor(this.getBackground());
         int arrowX = textWidth + 12;
         int arrowY = 13 - textHeight / 2;
         this.arrowBox.setRect(arrowX - 2, arrowY - 2, 14.0, 14.0);
         g2.fillRect(arrowX - 2, arrowY - 2, 14, 14);
         g2.setColor(this.titleColor);
         g2.fillPolygon(this.createArrow(arrowX, arrowY, this.collapsedButtonIsUpArrow ? !this.expanded : this.expanded));
      }
   }

   private Polygon createArrow(int x, int y, boolean up) {
      Polygon triangle = new Polygon();
      if (up) {
         triangle.addPoint(x, y + 10);
         triangle.addPoint(x + 10, y + 10);
         triangle.addPoint(x + 5, y);
      } else {
         triangle.addPoint(x, y);
         triangle.addPoint(x + 10, y);
         triangle.addPoint(x + 5, y + 10);
      }

      return triangle;
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (this.textBox.contains(e.getPoint()) || this.arrowBox.contains(e.getPoint())) {
         this.expanded = !this.expanded;
         if (this.expanded) {
            this.remove(verticalSpacer);
            this.add(this.contentPanel, contentPanelConstraints);
         } else {
            this.remove(this.contentPanel);
            this.add(verticalSpacer, contentPanelConstraints);
         }

         this.titleColor = this.border.getTitleColor();
         this.revalidate();
      }
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      this.tracking = true;
   }

   @Override
   public void mouseExited(MouseEvent e) {
      this.tracking = false;
   }

   @Override
   public void mousePressed(MouseEvent arg0) {
   }

   @Override
   public void mouseReleased(MouseEvent arg0) {
   }

   @Override
   public void mouseDragged(MouseEvent arg0) {
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      if (this.tracking) {
         if (!this.textBox.contains(e.getPoint()) && !this.arrowBox.contains(e.getPoint())) {
            if (this.titleColor != this.border.getTitleColor()) {
               this.titleColor = this.border.getTitleColor();
               this.repaint();
            }
         } else if (this.titleColor != Color.BLUE) {
            this.titleColor = Color.BLUE;
            this.repaint();
         }
      }
   }
}
