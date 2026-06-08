package minecraft.planner.gui.displaypanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import minecraft.planner.gui.displaypanels.GridPanel.HeightGridPanel.1;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid.Cell;

public class GridPanel<P extends StructureParameters> extends JPanel implements StructureModelChangeListener {
   private static final Color LIGHT_GRAY = new Color(220, 220, 220);
   private static final int SQUARE_SIZE = 30;
   private StructureModel<P> model;
   private GridPanel<P>.HeightGridPanel heightGridPanel = new GridPanel.HeightGridPanel();
   private JScrollPane scrollPane = new JScrollPane();

   public GridPanel(StructureModel<P> model) {
      this.model = model;
      this.setLayout(new BorderLayout());
      this.scrollPane.getViewport().add(this.heightGridPanel);
      this.add(this.scrollPane, "Center");
      this.model.registerListener(this);
   }

   @Override
   public void structureModelChanged(Object originator) {
      WorldGrid grid = this.model.getWorldGrid();
      Dimension gridSize = new Dimension(grid.getWidth() * 30, grid.getHeight() * 30);
      this.heightGridPanel.setPreferredSize(gridSize);
      this.heightGridPanel.setSize(gridSize);
      this.scrollPane.validate();
      this.heightGridPanel.repaint();
   }

   private class HeightGridPanel extends JPanel {
      private Font font = this.getFont().deriveFont(1);

      public HeightGridPanel() {
         this.setFont(this.font);
         this.addMouseListener(new 1(this));
      }

      @Override
      public void paint(Graphics g) {
         super.paint(g);
         WorldGrid grid = GridPanel.this.model.getWorldGrid();
         Rectangle rect = this.getVisibleRect();
         int fromX = (int)(rect.getX() / 30.0);
         int fromY = (int)(rect.getY() / 30.0);
         int toX = fromX + (int)(rect.getWidth() / 30.0);
         int toY = fromY + (int)(rect.getHeight() / 30.0);
         if (fromX > 0) {
            fromX--;
         }

         if (fromY > 0) {
            fromY--;
         }

         if (toX < grid.getWidth()) {
            toX++;
         }

         if (toY < grid.getHeight()) {
            toY++;
         }

         for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
               this.drawSquare(g, grid.getCell(x, y));
            }
         }
      }

      private void drawSquare(Graphics g, Cell cell) {
         if (cell != null) {
            int x = cell.getX();
            int y = cell.getY();
            if (cell.isProcessed()) {
               g.setColor(Color.GREEN);
            } else if (cell.getHeight() > 0) {
               g.setColor(GridPanel.LIGHT_GRAY);
            } else {
               g.setColor(Color.WHITE);
            }

            g.fillRect(x * 30, y * 30, 30, 30);
            g.setColor(Color.BLACK);
            g.drawRect(x * 30, y * 30, 30, 30);
            if (cell != null && !cell.getStack().getZValues().isEmpty()) {
               int height = cell.getHeight();
               String text = "" + height;
               Rectangle2D textBounds = g.getFontMetrics(this.font).getStringBounds(text, g);
               g.drawString(text, x * 30 + (int)((30.0 - textBounds.getWidth()) / 2.0), (int)(y * 30 + (30.0 - textBounds.getHeight() / 2.0)));
            }
         }
      }
   }
}
