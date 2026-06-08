package minecraft.planner.gui.displaypanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.AttributedString;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import minecraft.planner.gui.displaypanels.LayerPanel.1.1;
import minecraft.planner.gui.displaypanels.LayerPanel.LayerGridPanel.2;
import minecraft.planner.gui.displaypanels.LayerPanel.LayerGridPanel.3;
import minecraft.planner.gui.displaypanels.LayerPanel.LayerGridPanel.4;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.ModelSaveInformation;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.grid.WorldGrid$GridStatistics;
import minecraft.planner.util.UserConfigurationManager;

public class LayerPanel<P extends StructureParameters> extends JPanel implements StructureModelChangeListener, FocusListener, Printable {
   private final LayerPainter<P> painter;
   private static final int DEFAULT_SQUARE_SIZE = 20;
   private int initialCellSize;
   private int cellSize;
   private StructureModel<P> model;
   private int layer;
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private LayerPanel<P>.LayerGridPanel layerGrid = new LayerPanel.LayerGridPanel();
   private JSlider scaleSlider = new JSlider(0);
   private JSlider layerSlider = new JSlider(1);
   private JScrollPane scrollPane = new JScrollPane();
   private TitledBorder border = null;
   private boolean isInitialized = false;

   private String getLayerName() {
      return "Layer " + this.layer;
   }

   public LayerPanel(StructureModel<P> model) {
      this(model, 20);
   }

   public LayerPanel(StructureModel<P> model, int cellSize) {
      this.setFocusable(true);
      this.addFocusListener(this);
      this.isInitialized = false;
      this.initialCellSize = cellSize;
      this.cellSize = cellSize;
      this.model = model;
      this.painter = new LayerPainter<>(model, this.texturePack);
      this.layer = 1;
      this.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), this.getLayerName());
      this.setBorder(this.border);
      this.setLayout(new BorderLayout());
      this.add(this.layerSlider, "West");
      this.add(this.scaleSlider, "South");
      this.scrollPane.getViewport().add(this.layerGrid);
      this.scrollPane.setBorder(BorderFactory.createBevelBorder(1));
      this.scrollPane.setHorizontalScrollBarPolicy(30);
      this.scrollPane.setVerticalScrollBarPolicy(20);
      this.add(this.scrollPane, "Center");
      this.layerSlider.setMinimum(1);
      this.layerSlider.setMaximum(1);
      this.layerSlider.setValue(this.layer);
      this.layerSlider.setPaintLabels(true);
      this.layerSlider.setPaintTicks(true);
      this.layerSlider.setMajorTickSpacing(5);
      this.layerSlider.setMinorTickSpacing(1);
      this.layerSlider.setSnapToTicks(true);
      this.layerSlider.setEnabled(false);
      this.layerSlider.setFocusable(false);
      this.scaleSlider.setMinimum(5);
      this.scaleSlider.setMaximum(2 * cellSize);
      this.scaleSlider.setValue(cellSize);
      this.scaleSlider.setEnabled(false);
      this.scaleSlider.setSnapToTicks(true);
      this.scaleSlider.setFocusable(false);
      Hashtable<Integer, JLabel> labels = new Hashtable<>();
      labels.put(5, new JLabel("0.25x"));
      labels.put(this.initialCellSize, new JLabel("1x"));
      labels.put(2 * cellSize, new JLabel("2x"));
      this.scaleSlider.setLabelTable(labels);
      this.scaleSlider.setPaintLabels(true);
      this.model.registerListener(this);
      this.scaleSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            LayerPanel.this.cellSize = LayerPanel.this.scaleSlider.getValue();
            LayerPanel.this.layerGrid.adjustSize();
            SwingUtilities.invokeLater(new 1(this));
         }
      });
      this.layerSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            LayerPanel.this.layer = LayerPanel.this.layerSlider.getValue();
            LayerPanel.this.border.setTitle(LayerPanel.this.getLayerName());
            LayerPanel.this.repaint();
            LayerPanel.this.layerGrid.repaint();
         }
      });
      this.layerGrid.addMouseWheelListener(new MouseWheelListener() {
         @Override
         public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getScrollType() == 0) {
               if (e.isShiftDown()) {
                  int newLayer = LayerPanel.this.layerSlider.getValue() - e.getWheelRotation();
                  if (newLayer > 0 && newLayer <= LayerPanel.this.layerSlider.getMaximum()) {
                     LayerPanel.this.layerSlider.setValue(newLayer);
                  }
               } else {
                  LayerPanel.this.scaleSlider.setValue(LayerPanel.this.scaleSlider.getValue() - e.getScrollAmount() * e.getWheelRotation());
               }
            }
         }
      });
   }

   @Override
   public void structureModelChanged(Object originator) {
      if (!this.isInitialized) {
         this.isInitialized = true;
         this.scaleSlider.setEnabled(true);
         this.layerSlider.setEnabled(true);
         this.layerSlider.setValue(1);
      }

      WorldGrid grid = this.model.getWorldGrid();
      int maxHeight = grid.getMaxHeight();
      this.layerSlider.setMaximum(maxHeight);
      if (this.layer > maxHeight) {
         this.layer = maxHeight;
         this.layerSlider.setValue(this.layer);
      }

      Dimension gridSize = new Dimension(grid.getWidth() * this.cellSize, grid.getHeight() * this.cellSize);
      this.layerGrid.setPreferredSize(gridSize);
      this.layerGrid.setSize(gridSize);
      this.layerSlider.validate();
      this.layerGrid.validate();
      this.scrollPane.validate();
      this.layerGrid.repaint();
      this.layerGrid.requestFocusInWindow();
   }

   @Override
   public void focusGained(FocusEvent arg0) {
      Container parent = this.getParent();
      System.out.println("LayerPanel focus gained:  Parent of " + this + " is " + parent);
   }

   @Override
   public void focusLost(FocusEvent arg0) {
      System.out.println("LayerPanel focus lost:  " + arg0.getComponent());
   }

   @Override
   public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
      int maxPage = this.layerSlider.getMaximum();
      if (pageIndex == 0) {
         return this.printManifest(g, pageFormat);
      } else {
         return pageIndex >= 1 && pageIndex <= maxPage ? this.printPage(g, pageFormat, pageIndex) : 1;
      }
   }

   private int printManifest(Graphics g, PageFormat pageFormat) {
      Graphics2D g2d = (Graphics2D)g;
      double width = pageFormat.getImageableX();
      double height = pageFormat.getImageableY();
      g2d.translate(width, height);
      WorldGrid grid = this.model.getWorldGrid();
      ModelSaveInformation info = this.model.getModelSaveInfo();
      String modelName = this.model.getStructureName();
      String fileName = "";
      String lastModified = "";
      if (info != null && info.getFile() != null) {
         fileName = info.getFile().getAbsolutePath();
         lastModified = new Date(info.getFile().lastModified()).toString();
         modelName = modelName + " - " + info.getFile().getName();
      }

      modelName = modelName + ":  Plans & Manifest";
      Font headerFont = this.getFont().deriveFont(1, 18.0F);
      AttributedString attributedString = new AttributedString(modelName);
      attributedString.addAttribute(TextAttribute.FONT, headerFont);
      attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      g2d.drawString(attributedString.getIterator(), 0, 20);
      int rowOffset = 40;
      int rowHeight = 15;
      int rowCount = 0;
      this.printString(g2d, "File Name:  " + fileName, 0, rowOffset + rowCount++ * rowHeight);
      this.printString(g2d, "Last Modified:  " + lastModified, 0, rowOffset + rowCount++ * rowHeight);
      this.printString(g2d, "Dimensions:  " + grid.getWidth() + " x " + grid.getHeight(), 0, rowOffset + rowCount++ * rowHeight);
      this.printString(g2d, "Layers:  " + this.layerSlider.getMaximum(), 0, rowOffset + rowCount++ * rowHeight);
      attributedString = new AttributedString("Materials Manifest");
      attributedString.addAttribute(TextAttribute.FONT, headerFont);
      attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      rowOffset = rowOffset + rowCount * rowHeight + 40;
      g2d.drawString(attributedString.getIterator(), 0, rowOffset);
      rowOffset += 40;
      rowCount = 0;
      WorldGrid$GridStatistics stats = grid.getStatistics();
      TextureName[] var22;
      int var21 = (var22 = TextureName.values()).length;

      for (int var20 = 0; var20 < var21; var20++) {
         TextureName texture = var22[var20];
         int count = stats.getCount(texture);
         int xOffset = 0;
         if (count > 0) {
            short var34;
            if (rowCount % 2 == 1) {
               var34 = 200;
            } else {
               var34 = 0;
            }

            if (texture != TextureName.None) {
               this.printString(g2d, texture.toString() + ":  " + count, var34, rowOffset + rowCount++ / 2 * rowHeight);
            } else {
               this.printString(g2d, "Blocks:  " + count, var34, rowOffset + rowCount++ / 2 * rowHeight);
            }
         }
      }

      return 0;
   }

   private void printString(Graphics2D g, String string, int x, int y) {
      Font infoFont = this.getFont().deriveFont(0, 12.0F);
      AttributedString attributedString = new AttributedString(string);
      attributedString.addAttribute(TextAttribute.FONT, infoFont);
      g.drawString(attributedString.getIterator(), x, y);
   }

   private int printPage(Graphics g, PageFormat pageFormat, int pageNumber) {
      Graphics2D g2d = (Graphics2D)g;
      if (pageFormat != null) {
         g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      }

      WorldGrid grid = this.model.getWorldGrid();
      ModelSaveInformation info = this.model.getModelSaveInfo();
      String modelName = this.model.getStructureName();
      if (info != null && info.getFile() != null) {
         modelName = modelName + " - " + info.getFile().getName();
      }

      modelName = modelName + ":  Layer " + pageNumber;
      Font headerFont = this.getFont().deriveFont(1, 18.0F);
      AttributedString attributedString = new AttributedString(modelName);
      attributedString.addAttribute(TextAttribute.FONT, headerFont);
      attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      g2d.drawString(attributedString.getIterator(), 0, 20);
      double pageWidth = pageFormat.getImageableWidth();
      double pageHeight = pageFormat.getImageableHeight() - 25.0;
      int cellWidth = (int)Math.floor(pageWidth / grid.getWidth());
      int cellHeight = (int)Math.floor(pageHeight / grid.getHeight());
      int cellSize = Math.min(cellWidth, cellHeight);

      for (int x = 0; x < grid.getWidth(); x++) {
         for (int y = 0; y < grid.getHeight(); y++) {
            this.printSquare(g2d, grid.getCell(x, y), pageNumber, cellSize, 0, 25);
         }
      }

      return 0;
   }

   private void printSquare(Graphics2D g, WorldGrid$Cell cell, int layer, int cellSize, int xOffset, int yOffset) {
      if (cell != null) {
         int x = cell.getX();
         int y = cell.getY();
         if (cell.getStack().containsZValue(layer)) {
            WorldGrid$Cell.ZValue cellValue = cell.getStack().getZValue(layer);
            if (cellValue != null) {
               if (cellValue.getTexture() == TextureName.None) {
                  g.setColor(Color.BLACK);
                  g.fillRect(x * cellSize + xOffset, y * cellSize + yOffset, cellSize, cellSize);
               } else {
                  TextureName texture = cellValue.getTexture();
                  if (texture != null) {
                     Image cellImage = texture.getPlanViewImageIcon(this.texturePack).getImage();
                     int imageWidth = cellSize;
                     int imageHeight = cellSize;
                     g.drawImage(cellImage, x * cellSize + xOffset, y * cellSize + yOffset, imageWidth, imageHeight, this);
                  }
               }
            }

            g.setColor(Color.BLACK);
            g.drawRect(x * cellSize + xOffset, y * cellSize + yOffset, cellSize, cellSize);
         } else {
            g.setColor(Color.WHITE);
            g.fillRect(x * cellSize + xOffset, y * cellSize + yOffset, cellSize, cellSize);
            g.setColor(Color.BLACK);
            g.drawRect(x * cellSize + xOffset, y * cellSize + yOffset, cellSize, cellSize);
         }
      }
   }

   private class LayerGridPanel extends JPanel {
      public LayerGridPanel() {
         this.setFocusable(true);
         this.setKeyBindings();
      }

      private void setKeyBindings() {
         InputMap map = this.getInputMap(2);
         map.put(KeyStroke.getKeyStroke("UP"), "layerUp");
         map.put(KeyStroke.getKeyStroke("DOWN"), "layerDown");
         map.put(KeyStroke.getKeyStroke("LEFT"), "scaleLeft");
         map.put(KeyStroke.getKeyStroke("RIGHT"), "scaleRight");
         map = this.getInputMap(1);
         map.put(KeyStroke.getKeyStroke("UP"), "layerUp");
         map.put(KeyStroke.getKeyStroke("DOWN"), "layerDown");
         map.put(KeyStroke.getKeyStroke("LEFT"), "scaleLeft");
         map.put(KeyStroke.getKeyStroke("RIGHT"), "scaleRight");
         map = this.getInputMap(0);
         map.put(KeyStroke.getKeyStroke("UP"), "layerUp");
         map.put(KeyStroke.getKeyStroke("DOWN"), "layerDown");
         map.put(KeyStroke.getKeyStroke("LEFT"), "scaleLeft");
         map.put(KeyStroke.getKeyStroke("RIGHT"), "scaleRight");
         this.getActionMap().put("scaleLeft", new minecraft.planner.gui.displaypanels.LayerPanel.LayerGridPanel.1(this));
         this.getActionMap().put("scaleRight", new 2(this));
         this.getActionMap().put("layerUp", new 3(this));
         this.getActionMap().put("layerDown", new 4(this));
      }

      public void adjustSize() {
         WorldGrid grid = LayerPanel.this.model.getWorldGrid();
         int width = grid.getWidth() * LayerPanel.this.cellSize;
         int height = grid.getHeight() * LayerPanel.this.cellSize;
         Dimension size = new Dimension(width, height);
         this.setSize(size);
         this.setPreferredSize(size);
      }

      @Override
      public void paint(Graphics g) {
         super.paint(g);
         LayerPanel.this.painter.paintLayer((Graphics2D)g, LayerPanel.this.layer, LayerPanel.this.cellSize);
      }
   }
}
