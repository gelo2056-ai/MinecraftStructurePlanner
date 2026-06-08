package minecraft.planner.gui.bitmap;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.net.URL;
import java.text.AttributedString;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.JStatusBar.Mode;
import minecraft.planner.gui.bitmap.BitmapLayerPanel.6.1;
import minecraft.planner.gui.bitmap.BitmapLayerPanel.LayerGridPanel.2;
import minecraft.planner.gui.bitmap.BitmapLayerPanel.LayerGridPanel.3;
import minecraft.planner.gui.bitmap.BitmapLayerPanel.LayerGridPanel.4;
import minecraft.planner.gui.displaypanels.LayerPainter;
import minecraft.planner.gui.displaypanels.TextureChangeListener;
import minecraft.planner.gui.displaypanels.TexturePanel;
import minecraft.planner.gui.java3d.TextureCache;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.ModelSaveInformation;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.bitmap.BitmapModel;
import minecraft.planner.model.bitmap.BitmapParameters;
import minecraft.planner.model.grid.UndoBuffer;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.grid.WorldGrid$GridStatistics;
import minecraft.planner.model.grid.attributes.Orientation;
import minecraft.planner.util.UserConfigurationManager;

public class BitmapLayerPanel extends JPanel implements StructureModelChangeListener, TextureChangeListener, Printable {
   private final UndoBuffer buffer = new UndoBuffer();
   private static final int DEFAULT_SQUARE_SIZE = 32;
   private int initialCellSize;
   private int cellSize;
   private BitmapModel model;
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private TextureName texture;
   private boolean initialized = false;
   private BitmapLayerPanel.LayerGridPanel layerGrid = new BitmapLayerPanel.LayerGridPanel();
   private JSlider scaleSlider = new JSlider(0);
   private JSlider transparencySlider = new JSlider(0);
   private JScrollPane scrollPane = new JScrollPane();
   private TitledBorder border = null;
   private int backgroundTransparency = 75;
   private JToolBar toolbar = new JToolBar("Pixel Art Tools", 0);
   private final BitmapLayerPanel.JToggleToolButton pencilTool;
   private final BitmapLayerPanel.JToggleToolButton fillTool;
   private BitmapLayerPanel.JToggleToolButton selectedTool;
   private final TexturePanel texturePanel = new TexturePanel(true, UserConfigurationManager.getTexturesFromCache("Pixel Art"));
   private final BitmapLayerPanel.JToolButton autoConvert;
   private Cursor drawCursor;
   private Cursor fillCursor;
   private final LayerPainter<BitmapParameters> painter;

   public UndoBuffer getUndoBuffer() {
      return this.buffer;
   }

   public BitmapLayerPanel(BitmapModel model) {
      this(model, 32);
   }

   public BitmapLayerPanel(BitmapModel model, int cellSize) {
      Toolkit toolkit = Toolkit.getDefaultToolkit();

      try {
         ImageIcon drawImageIcon = new ImageIcon(TextureCache.class.getClass().getResource("/minecraft/planner/resources/pointer-draw.png"));
         this.drawCursor = toolkit.createCustomCursor(drawImageIcon.getImage(), new Point(0, 31), "Pencil");
      } catch (Exception e) {
         this.drawCursor = Cursor.getDefaultCursor();
      }

      try {
         ImageIcon fillImageIcon = new ImageIcon(TextureCache.class.getClass().getResource("/minecraft/planner/resources/pointer-fill.png"));
         this.fillCursor = toolkit.createCustomCursor(fillImageIcon.getImage(), new Point(0, 31), "Paint can");
      } catch (Exception e) {
         this.drawCursor = Cursor.getDefaultCursor();
      }

      this.initialCellSize = 32;
      this.cellSize = cellSize;
      this.model = model;
      this.painter = new LayerPainter<>(this.model, this.texturePack, false, false);
      this.texture = TextureName.None;
      this.setFocusable(true);
      this.setLayout(new BorderLayout());
      this.texturePanel.addTextureChangeListener(this);
      Dimension size = new Dimension(160, 0);
      this.texturePanel.setMinimumSize(size);
      this.texturePanel.setPreferredSize(size);
      this.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Pixel Layout");
      JPanel layerPanel = new JPanel();
      layerPanel.setBorder(this.border);
      layerPanel.setLayout(new BorderLayout());
      this.scrollPane.setBorder(BorderFactory.createBevelBorder(1));
      this.scrollPane.setHorizontalScrollBarPolicy(30);
      this.scrollPane.setVerticalScrollBarPolicy(20);
      this.scrollPane.getViewport().add(this.layerGrid);
      this.transparencySlider.setMinimum(0);
      this.transparencySlider.setMaximum(100);
      this.transparencySlider.setValue(this.backgroundTransparency);
      this.transparencySlider.setPaintTicks(true);
      this.transparencySlider.setPaintLabels(true);
      this.transparencySlider.setMajorTickSpacing(10);
      this.transparencySlider.setMinorTickSpacing(5);
      this.transparencySlider.setSnapToTicks(false);
      this.scaleSlider.setMinimum(5);
      this.scaleSlider.setMaximum(2 * cellSize);
      this.scaleSlider.setValue(cellSize);
      this.scaleSlider.setEnabled(true);
      this.scaleSlider.setSnapToTicks(true);
      Hashtable<Integer, JLabel> labels = new Hashtable<>();
      labels.put(5, new JLabel("0.25x"));
      labels.put(this.initialCellSize, new JLabel("1x"));
      labels.put(2 * cellSize, new JLabel("2x"));
      this.scaleSlider.setLabelTable(labels);
      this.scaleSlider.setPaintLabels(true);
      JPanel sliderPanel = new JPanel();
      sliderPanel.setLayout(new GridBagLayout());
      sliderPanel.add(new JLabel("Background Opacity:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 5), 0, 0));
      sliderPanel.add(this.transparencySlider, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 0, 5, 5), 0, 0));
      sliderPanel.add(new JLabel("Grid Scale:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 5), 0, 0));
      sliderPanel.add(this.scaleSlider, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 0, 5, 5), 0, 0));
      this.pencilTool = new BitmapLayerPanel.JToggleToolButton("pencil.png", "Draw") {
         @Override
         public void click() {
            this.setSelected(true);
            BitmapLayerPanel.this.fillTool.setSelected(false);
            BitmapLayerPanel.this.selectedTool = this;
         }
      };
      this.fillTool = new BitmapLayerPanel.JToggleToolButton("fill.png", "Fill") {
         @Override
         public void click() {
            this.setSelected(true);
            BitmapLayerPanel.this.pencilTool.setSelected(false);
            BitmapLayerPanel.this.selectedTool = this;
         }
      };
      this.autoConvert = new BitmapLayerPanel.JToolButton("auto-convert.png", "Auto-convert") {
         @Override
         public void click() {
            BitmapLayerPanel.this.autoConvert(BitmapLayerPanel.this.texturePanel.getFilteredTextures());
         }
      };
      this.pencilTool.setSelected(true);
      this.fillTool.setSelected(false);
      this.selectedTool = this.pencilTool;
      this.toolbar.add(this.pencilTool);
      this.toolbar.add(this.fillTool);
      this.toolbar.addSeparator();
      this.toolbar.add(this.autoConvert);
      layerPanel.add(this.toolbar, "North");
      layerPanel.add(sliderPanel, "South");
      layerPanel.add(this.scrollPane, "Center");
      this.model.registerListener(this);
      this.transparencySlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            BitmapLayerPanel.this.backgroundTransparency = BitmapLayerPanel.this.transparencySlider.getValue();
            BitmapLayerPanel.this.layerGrid.repaint();
            BitmapLayerPanel.this.repaint();
         }
      });
      this.layerGrid.addMouseMotionListener(new MouseMotionListener() {
         @Override
         public void mouseDragged(MouseEvent e) {
            boolean erasing = (e.getModifiersEx() & 128) > 0;
            if (BitmapLayerPanel.this.selectedTool == BitmapLayerPanel.this.pencilTool && SwingUtilities.isLeftMouseButton(e)) {
               if (!erasing) {
                  BitmapLayerPanel.this.processCell(e.getX(), e.getY(), 1, BitmapLayerPanel.this.texture);
               } else {
                  BitmapLayerPanel.this.processCell(e.getX(), e.getY(), 1, TextureName.None);
               }
            }
         }

         @Override
         public void mouseMoved(MouseEvent e) {
         }
      });
      this.scaleSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            BitmapLayerPanel.this.cellSize = BitmapLayerPanel.this.scaleSlider.getValue();
            BitmapLayerPanel.this.layerGrid.adjustSize();
            SwingUtilities.invokeLater(new 1(this));
         }
      });
      this.layerGrid
         .addMouseWheelListener(
            new MouseWheelListener() {
               @Override
               public void mouseWheelMoved(MouseWheelEvent e) {
                  boolean transparency = (e.getModifiersEx() & 64) > 0;
                  if (e.getScrollType() == 0) {
                     if (transparency) {
                        BitmapLayerPanel.this.transparencySlider
                           .setValue(BitmapLayerPanel.this.transparencySlider.getValue() - e.getScrollAmount() * e.getWheelRotation());
                     } else {
                        BitmapLayerPanel.this.scaleSlider.setValue(BitmapLayerPanel.this.scaleSlider.getValue() - e.getScrollAmount() * e.getWheelRotation());
                     }
                  }
               }
            }
         );
      this.layerGrid.addMouseListener(new MouseListener() {
         @Override
         public void mouseClicked(MouseEvent e) {
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            if (BitmapLayerPanel.this.selectedTool == BitmapLayerPanel.this.pencilTool) {
               BitmapLayerPanel.this.layerGrid.setCursor(BitmapLayerPanel.this.drawCursor);
            } else if (BitmapLayerPanel.this.selectedTool == BitmapLayerPanel.this.fillTool) {
               BitmapLayerPanel.this.layerGrid.setCursor(BitmapLayerPanel.this.fillCursor);
            }
         }

         @Override
         public void mouseExited(MouseEvent e) {
            BitmapLayerPanel.this.layerGrid.setCursor(Cursor.getDefaultCursor());
         }

         @Override
         public void mousePressed(MouseEvent e) {
            BitmapLayerPanel.this.buffer.clearBuffer();
            if (BitmapLayerPanel.this.selectedTool == BitmapLayerPanel.this.pencilTool) {
               if (SwingUtilities.isLeftMouseButton(e)) {
                  BitmapLayerPanel.this.processCell(e.getX(), e.getY(), 1, BitmapLayerPanel.this.texture);
               } else if (SwingUtilities.isRightMouseButton(e)) {
                  BitmapLayerPanel.this.processCell(e.getX(), e.getY(), 1, TextureName.None);
               }
            } else if (BitmapLayerPanel.this.selectedTool == BitmapLayerPanel.this.fillTool) {
               if (SwingUtilities.isLeftMouseButton(e)) {
                  BitmapLayerPanel.this.floodFill(e.getX(), e.getY(), 1, BitmapLayerPanel.this.texture);
               } else if (SwingUtilities.isRightMouseButton(e)) {
                  BitmapLayerPanel.this.floodFill(e.getX(), e.getY(), 1, TextureName.None);
               }
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
         }
      });
      JSplitPane split = new JSplitPane(1);
      split.setLeftComponent(this.texturePanel);
      split.setRightComponent(layerPanel);
      this.add(split, "Center");
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
               this.buffer.addCell(cellX, cellY, layer, TextureName.None, Orientation.None);
               cell.getStack().addZ(layer, texture);
               changed = true;
            }
         } else if (texture != TextureName.None) {
            if (zValue.getTexture() != texture) {
               this.buffer.addCell(cellX, cellY, layer, zValue.getTexture(), zValue.getOrientation());
               zValue.setTexture(texture);
               changed = true;
            }
         } else {
            this.buffer.addCell(cellX, cellY, layer, zValue.getTexture(), zValue.getOrientation());
            cell.getStack().removeZ(layer);
            changed = true;
         }
      }

      if (changed) {
         this.layerGrid.repaint();
         this.model.fireModelChangedEvent();
      }
   }

   private void floodFill(int x, int y, int layer, TextureName texture) {
      WorldGrid grid = this.model.getWorldGrid();
      int cellX = x / this.cellSize;
      int cellY = y / this.cellSize;
      boolean changed = false;
      if (cellX >= 0 && cellX < grid.getWidth() && cellY >= 0 && cellY < grid.getHeight()) {
         WorldGrid$Cell cell = grid.getCell(cellX, cellY);
         WorldGrid$Cell.ZValue zValue = cell.getStack().getZValue(layer);
         TextureName replaceTexture;
         if (zValue == null) {
            replaceTexture = TextureName.None;
         } else {
            replaceTexture = zValue.getTexture();
         }

         if (replaceTexture != texture) {
            changed = this.flood(grid, cellX, cellY, replaceTexture, texture);
         }
      }

      if (changed) {
         this.layerGrid.repaint();
         this.model.fireModelChangedEvent();
      }
   }

   private boolean flood(WorldGrid grid, int x, int y, TextureName oldTexture, TextureName newTexture) {
      boolean changed = false;
      if (x >= 0 && x < grid.getWidth() && y >= 0 && y < grid.getHeight()) {
         LinkedList<WorldGrid$Cell> cellList = new LinkedList<>();
         WorldGrid$Cell thisCell = grid.getCell(x, y);
         if (this.texture(thisCell) == oldTexture) {
            cellList.addLast(thisCell);
         }

         while (!cellList.isEmpty()) {
            WorldGrid$Cell currentCell = cellList.removeFirst();
            if (this.texture(currentCell) == oldTexture) {
               changed = true;
               this.replaceTexture(currentCell, newTexture);
               int west = currentCell.getX() - 1;
               int east = currentCell.getX() + 1;
               int currentY = currentCell.getY();
               boolean headingWest = true;

               do {
                  WorldGrid$Cell westCell = grid.getCell(west, currentY);
                  if (westCell != null && this.texture(westCell) == oldTexture) {
                     west--;
                  } else {
                     headingWest = false;
                     west++;
                  }
               } while (headingWest);

               boolean headingEast = true;

               do {
                  WorldGrid$Cell eastCell = grid.getCell(east, currentY);
                  if (eastCell != null && this.texture(eastCell) == oldTexture) {
                     east++;
                  } else {
                     headingEast = false;
                     east--;
                  }
               } while (headingEast);

               for (int xLoop = west; xLoop <= east; xLoop++) {
                  WorldGrid$Cell replaceCell = grid.getCell(xLoop, currentY);
                  if (replaceCell != null) {
                     this.replaceTexture(replaceCell, newTexture);
                     WorldGrid$Cell north = grid.getCell(xLoop, currentY - 1);
                     if (north != null && this.texture(north) == oldTexture) {
                        cellList.addLast(north);
                     }

                     WorldGrid$Cell south = grid.getCell(xLoop, currentY + 1);
                     if (south != null && this.texture(south) == oldTexture) {
                        cellList.addLast(south);
                     }
                  }
               }
            }
         }
      }

      return changed;
   }

   private TextureName texture(WorldGrid$Cell cell) {
      if (cell != null) {
         WorldGrid$Cell.ZStack stack = cell.getStack();
         WorldGrid$Cell.ZValue value = stack.getZValue(1);
         if (value != null) {
            return value.getTexture();
         }
      }

      return TextureName.None;
   }

   private void replaceTexture(WorldGrid$Cell cell, TextureName texture) {
      if (cell != null) {
         WorldGrid$Cell.ZStack stack = cell.getStack();
         if (texture == TextureName.None) {
            if (stack != null) {
               WorldGrid$Cell.ZValue value = stack.getZValue(1);
               TextureName oldTexture = TextureName.None;
               Orientation oldOrientation = Orientation.None;
               if (value != null) {
                  oldTexture = value.getTexture();
                  oldOrientation = value.getOrientation();
               }

               this.buffer.addCell(cell.getX(), cell.getY(), 1, oldTexture, oldOrientation);
            }

            stack.removeZ(1);
         } else if (stack.containsZValue(1)) {
            WorldGrid$Cell.ZValue zValue = stack.getZValue(1);
            this.buffer.addCell(cell.getX(), cell.getY(), 1, zValue.getTexture(), zValue.getOrientation());
            stack.getZValue(1).setTexture(texture);
         } else {
            this.buffer.addCell(cell.getX(), cell.getY(), 1, TextureName.None, Orientation.None);
            stack.addZ(1, texture);
         }
      }
   }

   @Override
   public void structureModelChanged(Object originator) {
      if (!this.initialized) {
         this.initialized = true;
         this.transparencySlider.setEnabled(true);
         this.transparencySlider.setValue(this.backgroundTransparency);
         StructurePlanner.getInstance().validateMenuItems();
      }

      WorldGrid grid = this.model.getWorldGrid();
      BitmapParameters parameters = this.model.getParameters();
      Dimension gridSize = new Dimension(grid.getWidth() * this.cellSize, grid.getHeight() * this.cellSize);
      this.layerGrid.setPreferredSize(gridSize);
      this.layerGrid.setSize(gridSize);
      this.layerGrid.validate();
      this.scrollPane.validate();
      this.transparencySlider.validate();
   }

   @Override
   public void textureChanged(TextureName name) {
      this.texture = name;
   }

   private Icon getIcon(String name) {
      try {
         URL iconURL = System.class.getResource("/minecraft/planner/resources/buttons/" + name);
         return new ImageIcon(iconURL);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   private void autoConvert(Collection<TextureName> filteredTextures) {
      if (this.model != null) {
         this.model.getWorldGrid().getStatistics().resetStatistics();
         new BitmapLayerPanel.AutoConvertThread(filteredTextures).start();
      }
   }

   @Override
   public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
      return pageIndex == 0 ? this.printManifest(g, pageFormat) : this.printPage(g, pageFormat, pageIndex);
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
      attributedString = new AttributedString("Materials Manifest");
      attributedString.addAttribute(TextAttribute.FONT, headerFont);
      attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      rowOffset = rowOffset + rowCount * rowHeight + 30;
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
            short var33;
            if (rowCount % 2 == 1) {
               var33 = 200;
            } else {
               var33 = 0;
            }

            this.printString(g2d, texture.toString() + ":  " + count, var33, rowOffset + rowCount++ / 2 * rowHeight);
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
      if (pageNumber > 1) {
         return 1;
      }

      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      WorldGrid grid = this.model.getWorldGrid();
      ModelSaveInformation info = this.model.getModelSaveInfo();
      String modelName = this.model.getStructureName();
      if (info != null && info.getFile() != null) {
         modelName = modelName + " - " + info.getFile().getName();
      }

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
            this.printSquare(g2d, grid.getCell(x, y), cellSize, 0, 25);
         }
      }

      return 0;
   }

   private void printSquare(Graphics2D g, WorldGrid$Cell cell, int cellSize, int xOffset, int yOffset) {
      if (cell != null) {
         int x = cell.getX();
         int y = cell.getY();
         if (cell.getStack().containsZValue(1)) {
            WorldGrid$Cell.ZValue cellValue = cell.getStack().getZValue(1);
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

   private class AutoConvertThread extends Thread {
      private Collection<TextureName> textures;

      public AutoConvertThread(Collection<TextureName> textures) {
         this.setName("Auto-convert Pixel Art " + this.getName());
         this.textures = textures;
      }

      @Override
      public void run() {
         try {
            Component[] count;
            int var3 = (count = BitmapLayerPanel.this.toolbar.getComponents()).length;

            for (int maxValue = 0; maxValue < var3; maxValue++) {
               Component component = count[maxValue];
               component.setEnabled(false);
            }

            BitmapParameters parameters = BitmapLayerPanel.this.model.getParameters();
            double maxValue = parameters.getWidth() * parameters.getHeight();
            double countx = 0.0;
            StructurePlanner.setStatusMode(Mode.Percent);

            for (int x = 0; x < BitmapLayerPanel.this.model.getParameters().getWidth(); x++) {
               for (int y = 0; y < BitmapLayerPanel.this.model.getParameters().getHeight(); y++) {
                  Color cellColor = BitmapLayerPanel.this.layerGrid.computeAverageBackgroundColor(x, y);
                  TextureName closestMatch = BitmapLayerPanel.this.texturePack.findClosestMatchingTexture(cellColor, this.textures);
                  WorldGrid$Cell cell = BitmapLayerPanel.this.model.getWorldGrid().getCell(x, y);
                  BitmapLayerPanel.this.replaceTexture(cell, closestMatch);
                  countx++;
                  StructurePlanner.setPercent(countx / maxValue);
               }
            }

            BitmapLayerPanel.this.model.fireModelChangedEvent();
            BitmapLayerPanel.this.layerGrid.repaint();
         } finally {
            StructurePlanner.setStatusMode(Mode.Text);
            Component[] var15;
            int var14 = (var15 = BitmapLayerPanel.this.toolbar.getComponents()).length;

            for (int var13 = 0; var13 < var14; var13++) {
               Component component = var15[var13];
               component.setEnabled(true);
            }
         }
      }
   }

   protected abstract class JToggleToolButton extends JToggleButton {
      public JToggleToolButton(String iconName, String tooltip) {
         super(BitmapLayerPanel.this.getIcon(iconName));
         this.setToolTipText(tooltip);
         this.addActionListener(new minecraft.planner.gui.bitmap.BitmapLayerPanel.JToggleToolButton.1(this));
      }

      public abstract void click();
   }

   protected abstract class JToolButton extends JButton {
      public JToolButton(String iconName, String tooltip) {
         super(BitmapLayerPanel.this.getIcon(iconName));
         this.setToolTipText(tooltip);
         this.addActionListener(new minecraft.planner.gui.bitmap.BitmapLayerPanel.JToolButton.1(this));
      }

      public abstract void click();
   }

   private class LayerGridPanel extends JPanel {
      public LayerGridPanel() {
         this.setKeyBindings();
      }

      private void setKeyBindings() {
         InputMap map = this.getInputMap(2);
         map.put(KeyStroke.getKeyStroke("shift LEFT"), "alphaLeft");
         map.put(KeyStroke.getKeyStroke("shift RIGHT"), "alphaRight");
         map.put(KeyStroke.getKeyStroke("LEFT"), "scaleLeft");
         map.put(KeyStroke.getKeyStroke("RIGHT"), "scaleRight");
         this.getActionMap().put("scaleLeft", new minecraft.planner.gui.bitmap.BitmapLayerPanel.LayerGridPanel.1(this));
         this.getActionMap().put("scaleRight", new 2(this));
         this.getActionMap().put("alphaLeft", new 3(this));
         this.getActionMap().put("alphaRight", new 4(this));
      }

      public void adjustSize() {
         WorldGrid grid = BitmapLayerPanel.this.model.getWorldGrid();
         int width = grid.getWidth() * BitmapLayerPanel.this.cellSize;
         int height = grid.getHeight() * BitmapLayerPanel.this.cellSize;
         Dimension size = new Dimension(width, height);
         this.setSize(size);
         this.setPreferredSize(size);
      }

      public Color computeAverageBackgroundColor(int x, int y) {
         BitmapParameters parameters = BitmapLayerPanel.this.model.getParameters();
         ImageIcon background = parameters.getBackgroundImage();
         BufferedImage cellImage = new BufferedImage(background.getIconWidth(), background.getIconHeight(), 1);
         Graphics2D g2D = cellImage.createGraphics();
         g2D.drawImage(background.getImage(), 0, 0, null);
         g2D.dispose();
         double cellWidth = (double)cellImage.getWidth() / parameters.getWidth();
         double cellHeight = (double)cellImage.getHeight() / parameters.getHeight();
         int a = 0;
         int r = 0;
         int g = 0;
         int b = 0;
         int count = 0;

         for (int pixelX = (int)Math.round(cellWidth * x); pixelX < (int)Math.round(cellWidth * (x + 1)); pixelX++) {
            for (int pixelY = (int)Math.round(cellHeight * y); pixelY < (int)Math.round(cellHeight * (y + 1)); pixelY++) {
               int rgb = cellImage.getRGB(pixelX, pixelY);
               r += (rgb & 0xFF0000) >> 16;
               g += (rgb & 0xFF00) >> 8;
               b += rgb & 0xFF;
               count++;
            }
         }

         return new Color(r / count, g / count, b / count);
      }

      @Override
      public void paint(Graphics g) {
         super.paint(g);
         WorldGrid grid = BitmapLayerPanel.this.model.getWorldGrid();
         if (BitmapLayerPanel.this.model != null) {
            BitmapParameters parameters = BitmapLayerPanel.this.model.getParameters();
            if (parameters != null) {
               ImageIcon backgroundImageIcon = BitmapLayerPanel.this.model.getParameters().getBackgroundImage();
               if (backgroundImageIcon != null) {
                  Image backgroundImage = backgroundImageIcon.getImage();
                  Graphics2D g2 = (Graphics2D)g.create();
                  g2.setColor(Color.WHITE);
                  int pixelWidth = grid.getWidth() * BitmapLayerPanel.this.cellSize;
                  int pixelHeight = grid.getHeight() * BitmapLayerPanel.this.cellSize;
                  g2.fillRect(0, 0, pixelWidth, pixelHeight);
                  if (BitmapLayerPanel.this.backgroundTransparency > 0) {
                     Composite originalComposite = g2.getComposite();
                     g2.setComposite(AlphaComposite.getInstance(3, BitmapLayerPanel.this.backgroundTransparency / 100.0F));
                     g2.drawImage(backgroundImage, 0, 0, pixelWidth, pixelHeight, this);
                     g2.setComposite(originalComposite);
                  }

                  BitmapLayerPanel.this.painter.paintLayer(g2, 1, BitmapLayerPanel.this.cellSize);
                  g2.dispose();
               }
            }
         }
      }
   }
}
