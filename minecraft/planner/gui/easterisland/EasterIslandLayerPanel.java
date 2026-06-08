package minecraft.planner.gui.easterisland;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.displaypanels.TexturePanel;
import minecraft.planner.gui.easterisland.EasterIslandLayerPanel.3.1;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.ModelSaveInformation;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.easterisland.EasterIslandModel;
import minecraft.planner.model.easterisland.EasterIslandParameters;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.grid.WorldGrid$GridStatistics;
import minecraft.planner.util.UserConfigurationManager;

public class EasterIslandLayerPanel extends JPanel implements StructureModelChangeListener, Printable {
   private static final Color LIGHT_GRAY = new Color(220, 255, 220);
   private static final Composite TRANSPARENCY_COMPOSITE = AlphaComposite.getInstance(3, 0.25F);
   private static final Color HIGHLIGHT = new Color(179, 220, 255);
   private static final int DEFAULT_SQUARE_SIZE = 32;
   private int initialCellSize;
   private int cellSize;
   private EasterIslandModel model;
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private TextureName texture;
   private boolean initialized = false;
   private int layer = 1;
   private EasterIslandLayerPanel.LayerGridPanel layerGrid = new EasterIslandLayerPanel.LayerGridPanel();
   private JSlider scaleSlider = new JSlider(0);
   private JSlider layerSlider = new JSlider(1);
   private JScrollPane scrollPane = new JScrollPane();
   private TitledBorder border = null;

   private int getLayer() {
      return this.layer;
   }

   private String getLayerName() {
      String layerName = "Layout";
      if (this.initialized && this.getLayer() > 0) {
         layerName = layerName + " - Layer " + this.getLayer();
      }

      return layerName;
   }

   public EasterIslandLayerPanel(EasterIslandModel model) {
      this(model, 32);
   }

   public EasterIslandLayerPanel(EasterIslandModel model, int cellSize) {
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      this.initialCellSize = 32;
      this.cellSize = cellSize;
      this.model = model;
      this.texture = TextureName.None;
      this.setLayout(new BorderLayout());
      TexturePanel texturePanel = new TexturePanel(false);
      texturePanel.addTextureFilterChangeListener(this.model);
      Dimension minimumSize = new Dimension(80, 0);
      Dimension preferredSize = new Dimension(160, 0);
      texturePanel.setMinimumSize(minimumSize);
      texturePanel.setPreferredSize(preferredSize);
      this.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Layout - Layer " + this.getLayer());
      JPanel layerPanel = new JPanel();
      layerPanel.setBorder(this.border);
      layerPanel.setLayout(new BorderLayout());
      this.scrollPane.setBorder(BorderFactory.createBevelBorder(1));
      this.scrollPane.setHorizontalScrollBarPolicy(30);
      this.scrollPane.setVerticalScrollBarPolicy(20);
      this.scrollPane.getViewport().add(this.layerGrid);
      this.layer = 1;
      this.layerSlider.setMinimum(1);
      this.layerSlider.setMaximum(1);
      this.layerSlider.setValue(1);
      this.layerSlider.setEnabled(false);
      this.layerSlider.setPaintTicks(true);
      this.layerSlider.setMajorTickSpacing(5);
      this.layerSlider.setMinorTickSpacing(1);
      this.layerSlider.setSnapToTicks(true);
      InputMap map = this.layerSlider.getInputMap();
      map.put(KeyStroke.getKeyStroke("LEFT"), "none");
      map.put(KeyStroke.getKeyStroke("RIGHT"), "none");
      this.scaleSlider.setMinimum(5);
      this.scaleSlider.setMaximum(2 * cellSize);
      this.scaleSlider.setValue(cellSize);
      this.scaleSlider.setEnabled(false);
      this.scaleSlider.setSnapToTicks(true);
      map = this.scaleSlider.getInputMap();
      map.put(KeyStroke.getKeyStroke("UP"), "none");
      map.put(KeyStroke.getKeyStroke("DOWN"), "none");
      Hashtable<Integer, JLabel> labels = new Hashtable<>();
      labels.put(5, new JLabel("0.25x"));
      labels.put(this.initialCellSize, new JLabel("1x"));
      labels.put(2 * cellSize, new JLabel("2x"));
      this.scaleSlider.setLabelTable(labels);
      this.scaleSlider.setPaintLabels(true);
      layerPanel.add(this.layerSlider, "West");
      layerPanel.add(this.scaleSlider, "South");
      layerPanel.add(this.scrollPane, "Center");
      this.model.registerListener(this);
      this.layerSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EasterIslandLayerPanel.this.layer = EasterIslandLayerPanel.this.layerSlider.getValue();
            EasterIslandLayerPanel.this.border.setTitle(EasterIslandLayerPanel.this.getLayerName());
            EasterIslandLayerPanel.this.layerGrid.repaint();
            EasterIslandLayerPanel.this.repaint();
         }
      });
      this.layerGrid
         .addMouseWheelListener(
            new MouseWheelListener() {
               @Override
               public void mouseWheelMoved(MouseWheelEvent e) {
                  if (e.getScrollType() == 0) {
                     EasterIslandLayerPanel.this.scaleSlider
                        .setValue(EasterIslandLayerPanel.this.scaleSlider.getValue() - e.getScrollAmount() * e.getWheelRotation());
                  }
               }
            }
         );
      this.scaleSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EasterIslandLayerPanel.this.cellSize = EasterIslandLayerPanel.this.scaleSlider.getValue();
            EasterIslandLayerPanel.this.layerGrid.adjustSize();
            SwingUtilities.invokeLater(new 1(this));
         }
      });
      JSplitPane split = new JSplitPane(1);
      split.setLeftComponent(texturePanel);
      split.setRightComponent(layerPanel);
      this.add(split, "Center");
   }

   private TextureName texture(WorldGrid$Cell cell) {
      if (cell != null) {
         WorldGrid$Cell.ZStack stack = cell.getStack();
         WorldGrid$Cell.ZValue value = stack.getZValue(this.layer);
         if (value != null) {
            return value.getTexture();
         }
      }

      return TextureName.None;
   }

   private void processCell(int x, int y, int layer, TextureName texture) {
      WorldGrid grid = this.model.getWorldGrid();
      int cellX = x / this.cellSize;
      int cellY = y / this.cellSize;
      boolean changed = false;
      if (cellX >= 0 && cellX < grid.getWidth() && cellY >= 0 && cellY < grid.getHeight()) {
         WorldGrid$Cell cell = grid.getCell(cellX, cellY);
         WorldGrid$Cell.ZValue zValue = cell.getStack().getZValue(layer);
         if (zValue == null) {
            if (texture != TextureName.None) {
               cell.getStack().addZ(layer, texture);
               changed = true;
            }
         } else if (texture != TextureName.None) {
            if (zValue.getTexture() != texture) {
               zValue.setTexture(texture);
               changed = true;
            }
         } else {
            cell.getStack().removeZ(layer);
            changed = true;
         }
      }

      if (changed) {
         this.layerGrid.repaint();
         this.model.notifyListenersOfChange(this);
         this.model.getModelSaveInfo().setChanged();
      }
   }

   @Override
   public void structureModelChanged(Object originator) {
      if (!this.initialized) {
         this.initialized = true;
         this.scaleSlider.setEnabled(true);
         this.layerSlider.setEnabled(true);
         this.layerSlider.setValue(1);
         StructurePlanner.getInstance().validateMenuItems();
      }

      WorldGrid grid = this.model.getWorldGrid();
      grid.updateMetrics(this.model);
      this.layerSlider.setMaximum(grid.getMaxHeight());
      EasterIslandParameters parameters = this.model.getParameters();
      Dimension gridSize = new Dimension(grid.getWidth() * this.cellSize, grid.getHeight() * this.cellSize);
      this.layerGrid.setPreferredSize(gridSize);
      this.layerGrid.setSize(gridSize);
      this.layerGrid.validate();
      this.scrollPane.validate();
      this.layerSlider.validate();
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

            this.printString(g2d, texture.toString() + ":  " + count, var34, rowOffset + rowCount++ / 2 * rowHeight);
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
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
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
            boolean invertBorder = true;
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
         } else if (cell.getStack().containsZValue(layer - 1)) {
            WorldGrid$Cell.ZValue value = cell.getStack().getZValue(layer - 1);
            TextureName lowerTexture = value.getTexture();
            if (lowerTexture != null) {
               Image cellImage = lowerTexture.getPlanViewImageIcon(this.texturePack).getImage();
               int imageWidth = cellSize;
               int imageHeight = cellSize;
               Composite originalComposite = g.getComposite();
               g.setComposite(TRANSPARENCY_COMPOSITE);
               g.drawImage(cellImage, x * cellSize + xOffset, y * cellSize + yOffset, imageWidth, imageHeight, this);
               g.setComposite(originalComposite);
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
      }

      public void adjustSize() {
         WorldGrid grid = EasterIslandLayerPanel.this.model.getWorldGrid();
         int width = grid.getWidth() * EasterIslandLayerPanel.this.cellSize;
         int height = grid.getHeight() * EasterIslandLayerPanel.this.cellSize;
         Dimension size = new Dimension(width, height);
         this.setSize(size);
         this.setPreferredSize(size);
      }

      @Override
      public void paint(Graphics g) {
         this.paint(g, EasterIslandLayerPanel.this.getLayer());
      }

      public void paint(Graphics g, int layer) {
         super.paint(g);
         WorldGrid grid = EasterIslandLayerPanel.this.model.getWorldGrid();

         for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
               this.drawSquare(g, grid.getCell(x, y), layer);
            }
         }
      }

      private void drawSquare(Graphics g, WorldGrid$Cell cell, int layer) {
         if (cell != null) {
            int x = cell.getX();
            int y = cell.getY();
            Graphics2D g2 = (Graphics2D)g.create();
            if (cell.getStack().containsZValue(layer)) {
               WorldGrid$Cell.ZValue cellValue = cell.getStack().getZValue(layer);
               boolean invertBorder = true;
               if (cellValue != null) {
                  if (cellValue.getTexture() == TextureName.None) {
                     g.setColor(Color.BLACK);
                     g.fillRect(
                        x * EasterIslandLayerPanel.this.cellSize,
                        y * EasterIslandLayerPanel.this.cellSize,
                        EasterIslandLayerPanel.this.cellSize,
                        EasterIslandLayerPanel.this.cellSize
                     );
                  } else {
                     TextureName texture = cellValue.getTexture();
                     if (texture != null) {
                        Image cellImage = texture.getPlanViewImageIcon(EasterIslandLayerPanel.this.texturePack).getImage();
                        int imageWidth = EasterIslandLayerPanel.this.cellSize;
                        int imageHeight = EasterIslandLayerPanel.this.cellSize;
                        g2.drawImage(
                           cellImage, x * EasterIslandLayerPanel.this.cellSize, y * EasterIslandLayerPanel.this.cellSize, imageWidth, imageHeight, this
                        );
                        if (imageWidth < EasterIslandLayerPanel.this.cellSize || imageHeight < EasterIslandLayerPanel.this.cellSize) {
                           invertBorder = false;
                        }
                     }
                  }
               }

               if (invertBorder) {
                  g.setColor(Color.WHITE);
                  g.drawRect(
                     x * EasterIslandLayerPanel.this.cellSize,
                     y * EasterIslandLayerPanel.this.cellSize,
                     EasterIslandLayerPanel.this.cellSize,
                     EasterIslandLayerPanel.this.cellSize
                  );
               }
            } else if (!cell.getStack().containsZValue(layer - 1)) {
               g.setColor(Color.WHITE);
               g.fillRect(
                  x * EasterIslandLayerPanel.this.cellSize,
                  y * EasterIslandLayerPanel.this.cellSize,
                  EasterIslandLayerPanel.this.cellSize,
                  EasterIslandLayerPanel.this.cellSize
               );
               g.setColor(Color.BLACK);
               g.drawRect(
                  x * EasterIslandLayerPanel.this.cellSize,
                  y * EasterIslandLayerPanel.this.cellSize,
                  EasterIslandLayerPanel.this.cellSize,
                  EasterIslandLayerPanel.this.cellSize
               );
            } else {
               WorldGrid$Cell.ZValue value = cell.getStack().getZValue(layer - 1);
               boolean invertBorder = true;
               TextureName lowerTexture = value.getTexture();
               if (lowerTexture != null) {
                  Image cellImage = lowerTexture.getPlanViewImageIcon(EasterIslandLayerPanel.this.texturePack).getImage();
                  int imageWidth = EasterIslandLayerPanel.this.cellSize;
                  int imageHeight = EasterIslandLayerPanel.this.cellSize;
                  Composite originalComposite = g2.getComposite();
                  g2.setComposite(EasterIslandLayerPanel.TRANSPARENCY_COMPOSITE);
                  g2.drawImage(cellImage, x * EasterIslandLayerPanel.this.cellSize, y * EasterIslandLayerPanel.this.cellSize, imageWidth, imageHeight, this);
                  g2.setComposite(originalComposite);
                  if (imageWidth < EasterIslandLayerPanel.this.cellSize || imageHeight < EasterIslandLayerPanel.this.cellSize) {
                     invertBorder = false;
                  }
               }

               if (invertBorder) {
                  g.setColor(Color.WHITE);
                  g.drawRect(
                     x * EasterIslandLayerPanel.this.cellSize,
                     y * EasterIslandLayerPanel.this.cellSize,
                     EasterIslandLayerPanel.this.cellSize,
                     EasterIslandLayerPanel.this.cellSize
                  );
               }
            }

            g2.dispose();
         }
      }
   }
}
