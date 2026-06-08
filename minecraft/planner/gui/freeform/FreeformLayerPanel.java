package minecraft.planner.gui.freeform;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.net.URL;
import java.text.AttributedString;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import minecraft.planner.gui.displaypanels.LayerPainter;
import minecraft.planner.gui.displaypanels.TextureChangeListener;
import minecraft.planner.gui.displaypanels.TexturePanel;
import minecraft.planner.gui.freeform.Clipboard.Clipboard;
import minecraft.planner.gui.freeform.Clipboard.CopyCell;
import minecraft.planner.gui.freeform.FreeformLayerPanel.17.1;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.10;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.11;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.12;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.13;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.14;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.2;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.3;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.4;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.5;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.6;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.7;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.8;
import minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.9;
import minecraft.planner.gui.java3d.TextureCache;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.ModelSaveInformation;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.freeform.FreeformModel;
import minecraft.planner.model.freeform.FreeformParameters;
import minecraft.planner.model.grid.UndoBuffer;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.grid.WorldGrid$GridStatistics;
import minecraft.planner.model.grid.attributes.Orientation;
import minecraft.planner.util.UserConfigurationManager;

public class FreeformLayerPanel extends JPanel implements StructureModelChangeListener, TextureChangeListener, Printable {
   private Orientation orientation = Orientation.North;
   private final UndoBuffer buffer = new UndoBuffer();
   private int highlightedRow = -1;
   private int highlightedColumn = -1;
   private JMenuItem removeRow = new JMenuItem("Delete row");
   private JMenuItem removeColumn = new JMenuItem("Delete column");
   private JMenuItem removeLayer = new JMenuItem("Delete layer");
   private JMenu orientationMenu = new JMenu("Orientation");
   private JMenu attributesMenu = new JMenu("Attributes");
   private int popupX;
   private int popupY;
   private final Map<Orientation, JMenuItem> orientationMenuMap = new HashMap<>();
   private final LayerPainter<FreeformParameters> painter;
   private final LayerPainter<FreeformParameters> printingPainter;
   private static final int DEFAULT_SQUARE_SIZE = 32;
   private int initialCellSize;
   private int cellSize;
   private FreeformModel model;
   private TexturePack texturePack;
   private TextureName texture;
   private boolean initialized = false;
   private int layer = 1;
   private FreeformLayerPanel.LayerGridPanel layerGrid = new FreeformLayerPanel.LayerGridPanel();
   private JSlider scaleSlider = new JSlider(0);
   private JSlider layerSlider = new JSlider(1);
   private JScrollPane scrollPane = new JScrollPane();
   private TitledBorder border = null;
   private JToolBar toolbar = new JToolBar("Freeform Tools", 0);
   private final FreeformLayerPanel.JToggleToolButton pencilTool;
   private final FreeformLayerPanel.JToggleToolButton fillTool;
   private FreeformLayerPanel.JToggleToolButton selectedTool;
   private Cursor drawCursor;
   private Cursor fillCursor;
   private boolean axisLocked = false;
   private Point axisLockStartingPoint = null;
   private FreeformLayerPanel.AxisLockType axisLockType = null;
   private GraphicsDevice axisLockGraphicsDevice = null;
   private Robot axisLockRobot = null;
   private static final Clipboard clipboard = new Clipboard();

   public UndoBuffer getUndoBuffer() {
      return this.buffer;
   }

   private JMenuItem createOrientationMenuItem(final Orientation orientation) {
      JMenuItem item = new JMenuItem(orientation.name());
      item.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
               WorldGrid$Cell.ZValue value = FreeformLayerPanel.this.model
                  .getWorldGrid()
                  .getCell(FreeformLayerPanel.this.popupX, FreeformLayerPanel.this.popupY)
                  .getStack()
                  .getZValue(FreeformLayerPanel.this.layer);
               if (value != null && value.getTexture().hasOrientation()) {
                  value.setOrientation(orientation);
                  FreeformLayerPanel.this.model.notifyListenersOfChange(this);
               }
            }
         }
      );
      this.orientationMenuMap.put(orientation, item);
      return item;
   }

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

   public FreeformLayerPanel(FreeformModel model) {
      this(model, 32);
   }

   public FreeformLayerPanel(FreeformModel model, int cellSize) {
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

      this.texture = TextureName.None;
      this.texturePack = UserConfigurationManager.getSelectedTexturePack();
      this.initialCellSize = 32;
      this.cellSize = cellSize;
      this.model = model;
      this.painter = new LayerPainter<>(this.model, this.texturePack, true);
      this.printingPainter = new LayerPainter<>(this.model, this.texturePack, false);
      this.setLayout(new BorderLayout());
      TexturePanel texturePanel = new TexturePanel(true);
      texturePanel.addTextureChangeListener(this);
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
      this.pencilTool = new FreeformLayerPanel.JToggleToolButton("pencil.png", "Draw") {
         @Override
         public void click() {
            this.setSelected(true);
            FreeformLayerPanel.this.fillTool.setSelected(false);
            FreeformLayerPanel.this.selectedTool = this;
         }
      };
      this.fillTool = new FreeformLayerPanel.JToggleToolButton("fill.png", "Fill") {
         @Override
         public void click() {
            this.setSelected(true);
            FreeformLayerPanel.this.pencilTool.setSelected(false);
            FreeformLayerPanel.this.selectedTool = this;
         }
      };
      this.pencilTool.setSelected(true);
      this.fillTool.setSelected(false);
      this.selectedTool = this.pencilTool;
      this.toolbar.add(this.pencilTool);
      this.toolbar.add(this.fillTool);
      this.toolbar.addSeparator();
      this.toolbar.add(new FreeformLayerPanel.JToolButton("add-col.png", "Add column") {
         @Override
         public void click() {
            FreeformLayerPanel.this.addColumn();
         }
      });
      this.toolbar.add(new FreeformLayerPanel.JToolButton("remove-col.png", "Remove column") {
         @Override
         public void click() {
            FreeformLayerPanel.this.removeColumn();
         }
      });
      this.toolbar.addSeparator();
      this.toolbar.add(new FreeformLayerPanel.JToolButton("add-row.png", "Add row") {
         @Override
         public void click() {
            FreeformLayerPanel.this.addRow();
         }
      });
      this.toolbar.add(new FreeformLayerPanel.JToolButton("remove-row.png", "Remove row") {
         @Override
         public void click() {
            FreeformLayerPanel.this.removeRow();
         }
      });
      this.toolbar.addSeparator();
      this.toolbar.add(new FreeformLayerPanel.JToolButton("add-layer.png", "Add layer") {
         @Override
         public void click() {
            FreeformLayerPanel.this.addLayer();
         }
      });
      this.toolbar.add(new FreeformLayerPanel.JToolButton("remove-layer.png", "Remove layer") {
         @Override
         public void click() {
            FreeformLayerPanel.this.removeLayer();
         }
      });
      this.toolbar.addSeparator();
      this.toolbar.add(new FreeformLayerPanel.JToolButton("trim.png", "Trim edges") {
         @Override
         public void click() {
            FreeformLayerPanel.this.trim();
         }
      });
      this.toolbar.addSeparator();
      this.toolbar.add(new FreeformLayerPanel.JToolButton("copy-layer.png", "Copy layer") {
         @Override
         public void click() {
            FreeformLayerPanel.this.copyLayer();
         }
      });
      this.toolbar.add(new FreeformLayerPanel.JToolButton("paste-layer.png", "Paste layer") {
         @Override
         public void click() {
            FreeformLayerPanel.this.pasteLayer();
         }
      });
      layerPanel.add(this.toolbar, "North");
      layerPanel.add(this.layerSlider, "West");
      layerPanel.add(this.scaleSlider, "South");
      layerPanel.add(this.scrollPane, "Center");
      this.model.registerListener(this);
      this.layerSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FreeformLayerPanel.this.layer = FreeformLayerPanel.this.layerSlider.getValue();
            FreeformLayerPanel.this.border.setTitle(FreeformLayerPanel.this.getLayerName());
            FreeformLayerPanel.this.layerGrid.repaint();
            FreeformLayerPanel.this.repaint();
         }
      });
      this.layerGrid.addMouseWheelListener(new MouseWheelListener() {
         @Override
         public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getScrollType() == 0) {
               FreeformLayerPanel.this.scaleSlider.setValue(FreeformLayerPanel.this.scaleSlider.getValue() - e.getScrollAmount() * e.getWheelRotation());
            }
         }
      });
      this.layerGrid
         .addMouseListener(
            new MouseListener() {
               @Override
               public void mouseClicked(MouseEvent e) {
               }

               @Override
               public void mouseEntered(MouseEvent e) {
                  FreeformLayerPanel.this.processHighlight(e.getX(), e.getY());
                  if (FreeformLayerPanel.this.selectedTool == FreeformLayerPanel.this.pencilTool) {
                     FreeformLayerPanel.this.layerGrid.setCursor(FreeformLayerPanel.this.drawCursor);
                  } else if (FreeformLayerPanel.this.selectedTool == FreeformLayerPanel.this.fillTool) {
                     FreeformLayerPanel.this.layerGrid.setCursor(FreeformLayerPanel.this.fillCursor);
                  }
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  FreeformLayerPanel.this.layerGrid.setCursor(Cursor.getDefaultCursor());
               }

               @Override
               public void mousePressed(MouseEvent e) {
                  boolean currentAxisLock = (e.getModifiersEx() & 64) > 0;
                  if (currentAxisLock && !FreeformLayerPanel.this.axisLocked) {
                     FreeformLayerPanel.this.axisLocked = true;
                     FreeformLayerPanel.this.axisLockStartingPoint = e.getPoint();
                     FreeformLayerPanel.this.axisLockGraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

                     try {
                        FreeformLayerPanel.this.axisLockRobot = new Robot(FreeformLayerPanel.this.axisLockGraphicsDevice);
                     } catch (Exception ex) {
                        FreeformLayerPanel.this.axisLockRobot = null;
                     }
                  }

                  FreeformLayerPanel.this.buffer.clearBuffer();
                  boolean erasing = (e.getModifiersEx() & 128) > 0;
                  if (FreeformLayerPanel.this.selectedTool == FreeformLayerPanel.this.pencilTool) {
                     if (SwingUtilities.isLeftMouseButton(e)) {
                        if (!erasing) {
                           FreeformLayerPanel.this.processCell(e.getX(), e.getY(), FreeformLayerPanel.this.layer, FreeformLayerPanel.this.texture);
                        } else {
                           FreeformLayerPanel.this.processCell(e.getX(), e.getY(), FreeformLayerPanel.this.layer, TextureName.None);
                        }
                     }
                  } else if (FreeformLayerPanel.this.selectedTool == FreeformLayerPanel.this.fillTool && SwingUtilities.isLeftMouseButton(e)) {
                     if (!erasing) {
                        FreeformLayerPanel.this.floodFill(e.getX(), e.getY(), FreeformLayerPanel.this.layer, FreeformLayerPanel.this.texture);
                     } else {
                        FreeformLayerPanel.this.floodFill(e.getX(), e.getY(), FreeformLayerPanel.this.layer, TextureName.None);
                     }
                  }
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  FreeformLayerPanel.this.axisLocked = false;
                  FreeformLayerPanel.this.axisLockStartingPoint = null;
                  FreeformLayerPanel.this.axisLockType = null;
                  FreeformLayerPanel.this.axisLockGraphicsDevice = null;
                  FreeformLayerPanel.this.axisLockRobot = null;
                  if (e.isPopupTrigger()
                     && FreeformLayerPanel.this.highlightedRow >= 0
                     && FreeformLayerPanel.this.highlightedColumn >= 0
                     && FreeformLayerPanel.this.highlightedRow < FreeformLayerPanel.this.model.getWorldGrid().getHeight()
                     && FreeformLayerPanel.this.highlightedColumn < FreeformLayerPanel.this.model.getWorldGrid().getWidth()) {
                     FreeformLayerPanel.this.popupX = FreeformLayerPanel.this.highlightedColumn;
                     FreeformLayerPanel.this.popupY = FreeformLayerPanel.this.highlightedRow;
                     FreeformLayerPanel.this.validatePopupMenu();
                     FreeformLayerPanel.this.layerGrid.designMenu.show(e.getComponent(), e.getX(), e.getY());
                  }
               }
            }
         );
      this.layerGrid.addMouseMotionListener(new MouseMotionListener() {
         @Override
         public void mouseDragged(MouseEvent e) {
            FreeformLayerPanel.this.processHighlight(e.getX(), e.getY());
            boolean erasing = (e.getModifiersEx() & 128) > 0;
            int x;
            int y;
            if (FreeformLayerPanel.this.axisLocked) {
               if (FreeformLayerPanel.this.axisLockType == null) {
                  double xDiff = Math.abs(FreeformLayerPanel.this.axisLockStartingPoint.getX() - e.getX());
                  double yDiff = Math.abs(FreeformLayerPanel.this.axisLockStartingPoint.getY() - e.getY());
                  if (xDiff > yDiff) {
                     FreeformLayerPanel.this.axisLockType = FreeformLayerPanel.AxisLockType.HORIZONTAL;
                  } else {
                     FreeformLayerPanel.this.axisLockType = FreeformLayerPanel.AxisLockType.VERTICAL;
                  }

                  StructurePlanner.setStatus("Mouse axis-locked to " + FreeformLayerPanel.this.axisLockType.name());
               }

               if (FreeformLayerPanel.this.axisLockType == FreeformLayerPanel.AxisLockType.HORIZONTAL) {
                  x = e.getX();
                  y = (int)FreeformLayerPanel.this.axisLockStartingPoint.getY();
               } else {
                  x = (int)FreeformLayerPanel.this.axisLockStartingPoint.getX();
                  y = e.getY();
               }
            } else {
               x = e.getX();
               y = e.getY();
            }

            if (FreeformLayerPanel.this.selectedTool == FreeformLayerPanel.this.pencilTool) {
               if (SwingUtilities.isLeftMouseButton(e)) {
                  if (!erasing) {
                     FreeformLayerPanel.this.processCell(x, y, FreeformLayerPanel.this.layer, FreeformLayerPanel.this.texture);
                  } else {
                     FreeformLayerPanel.this.processCell(x, y, FreeformLayerPanel.this.layer, TextureName.None);
                  }
               }

               if (FreeformLayerPanel.this.axisLockRobot != null) {
                  Point newPoint = new Point(x, y);
                  SwingUtilities.convertPointToScreen(newPoint, FreeformLayerPanel.this.layerGrid);
                  FreeformLayerPanel.this.axisLockRobot.mouseMove(newPoint.x, newPoint.y);
               }
            }
         }

         @Override
         public void mouseMoved(MouseEvent e) {
            FreeformLayerPanel.this.processHighlight(e.getX(), e.getY());
         }
      });
      this.scaleSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FreeformLayerPanel.this.cellSize = FreeformLayerPanel.this.scaleSlider.getValue();
            FreeformLayerPanel.this.layerGrid.adjustSize();
            SwingUtilities.invokeLater(new 1(this));
         }
      });
      JSplitPane split = new JSplitPane(1);
      split.setLeftComponent(texturePanel);
      split.setRightComponent(layerPanel);
      this.add(split, "Center");
   }

   private void validatePopupMenu() {
      WorldGrid grid = this.model.getWorldGrid();
      this.removeColumn.setEnabled(grid.getWidth() > 1);
      this.removeRow.setEnabled(grid.getHeight() > 1);
      this.removeLayer.setEnabled(this.layerSlider.getMaximum() > 1);
      WorldGrid$Cell.ZValue value = this.model.getWorldGrid().getCell(this.popupX, this.popupY).getStack().getZValue(this.layer);
      this.orientationMenu.setEnabled(value != null && value.getTexture() != null && value.getTexture().hasOrientation());
      if (this.orientationMenu.isEnabled()) {
         for (Orientation orientation : this.orientationMenuMap.keySet()) {
            JMenuItem menuItem = this.orientationMenuMap.get(orientation);
            menuItem.setSelected(value.getOrientation() == orientation);
         }
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

   protected void processHighlight(int gridX, int gridY) {
      boolean highlightChanged = false;
      int cellX = gridX / this.cellSize;
      int cellY = gridY / this.cellSize;
      if (cellX != this.highlightedColumn) {
         this.highlightedColumn = cellX;
         highlightChanged = true;
      }

      if (cellY != this.highlightedRow) {
         this.highlightedRow = cellY;
         highlightChanged = true;
      }

      if (highlightChanged) {
         this.layerGrid.repaint();
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
         WorldGrid$Cell.ZValue value = stack.getZValue(this.layer);
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
               WorldGrid$Cell.ZValue value = stack.getZValue(this.layer);
               TextureName oldTexture = TextureName.None;
               Orientation oldOrientation = Orientation.None;
               if (value != null) {
                  oldTexture = value.getTexture();
                  oldOrientation = value.getOrientation();
               }

               this.buffer.addCell(cell.getX(), cell.getY(), this.layer, oldTexture, oldOrientation);
            }

            stack.removeZ(this.layer);
         } else if (stack.containsZValue(this.layer)) {
            WorldGrid$Cell.ZValue zValue = stack.getZValue(this.layer);
            this.buffer.addCell(cell.getX(), cell.getY(), this.layer, zValue.getTexture(), zValue.getOrientation());
            stack.getZValue(this.layer).setTexture(texture);
         } else {
            this.buffer.addCell(cell.getX(), cell.getY(), this.layer, TextureName.None, Orientation.None);
            stack.addZ(this.layer, texture);
         }
      }
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
               if (texture.hasOrientation()) {
                  cell.getStack().addZ(layer, texture, this.orientation);
               } else {
                  cell.getStack().addZ(layer, texture);
               }

               changed = true;
            }
         } else if (texture != TextureName.None) {
            if (zValue.getTexture() != texture) {
               this.buffer.addCell(cellX, cellY, layer, zValue.getTexture(), zValue.getOrientation());
               zValue.setTexture(texture);
               changed = true;
            } else if (!this.buffer.contains(cellX, cellY, layer) && texture.hasOrientation()) {
               if (this.orientation == Orientation.West) {
                  this.orientation = Orientation.North;
               } else {
                  this.orientation = Orientation.values()[this.orientation.ordinal() + 1];
               }

               this.buffer.addCell(cellX, cellY, layer, zValue.getTexture(), zValue.getOrientation());
               zValue.setTexture(texture);
               zValue.setOrientation(this.orientation);
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
      FreeformParameters parameters = this.model.getParameters();
      if (parameters.getMaxHeight() != this.layerSlider.getMaximum()) {
         this.layerSlider.setMaximum(parameters.getMaxHeight());
      }

      if (parameters.getMaxHeight() > 0 && this.layer > parameters.getMaxHeight()) {
         this.layer = parameters.getMaxHeight();
         this.layerSlider.setValue(parameters.getMaxHeight());
      }

      Dimension gridSize = new Dimension(grid.getWidth() * this.cellSize, grid.getHeight() * this.cellSize);
      this.layerGrid.setPreferredSize(gridSize);
      this.layerGrid.setSize(gridSize);
      this.layerGrid.validate();
      this.scrollPane.validate();
      this.layerSlider.validate();
   }

   @Override
   public void textureChanged(TextureName name) {
      this.texture = name;
   }

   public void addRow() {
      this.model.getWorldGrid().addRow();
      this.model.getParameters().setY(this.model.getWorldGrid().getHeight());
      this.model.fireModelChangedEvent();
      this.layerGrid.adjustSize();
      this.repaint();
   }

   public void removeRow() {
      this.model.getWorldGrid().removeRow();
      this.model.getParameters().setY(this.model.getWorldGrid().getHeight());
      this.model.fireModelChangedEvent();
      this.layerGrid.adjustSize();
      this.repaint();
   }

   public void addColumn() {
      this.model.getWorldGrid().addColumn();
      this.model.getParameters().setX(this.model.getWorldGrid().getWidth());
      this.model.fireModelChangedEvent();
      this.layerGrid.adjustSize();
      this.repaint();
   }

   public void removeColumn() {
      this.model.getWorldGrid().removeColumn();
      this.model.getParameters().setX(this.model.getWorldGrid().getWidth());
      this.model.fireModelChangedEvent();
      this.layerGrid.adjustSize();
      this.repaint();
   }

   public void addLayer() {
      int newMaxHeight = this.layerSlider.getMaximum() + 1;
      if (this.model.getWorldGrid().canAddLayer(newMaxHeight)) {
         this.model.getParameters().setMaxHeight(newMaxHeight);
         this.layerSlider.setMaximum(newMaxHeight);
         this.model.fireModelChangedEvent();
         this.layerSlider.repaint();
      }
   }

   public void removeLayer() {
      int maxHeight = this.layerSlider.getMaximum();
      if (this.model.getWorldGrid().canRemoveLayer(maxHeight)) {
         int index = this.layerSlider.getValue();
         this.layerSlider.setMaximum(maxHeight - 1);
         this.model.getParameters().setMaxHeight(maxHeight - 1);
         this.model.fireModelChangedEvent();
         this.layerSlider.repaint();
      }
   }

   public void trim() {
      this.model.getWorldGrid().trim();
      this.model.getParameters().setX(this.model.getWorldGrid().getWidth());
      this.model.getParameters().setY(this.model.getWorldGrid().getHeight());
      this.model.fireModelChangedEvent();
      this.layerGrid.adjustSize();
      this.repaint();
      this.model.getModelSaveInfo().setChanged();
   }

   public void copyLayer() {
      clipboard.clear();
      int layer = this.layerSlider.getValue();
      WorldGrid grid = this.model.getWorldGrid();

      for (int x = 0; x < grid.getWidth(); x++) {
         for (int y = 0; y < grid.getHeight(); y++) {
            WorldGrid$Cell cell = grid.getCell(x, y);
            if (cell != null) {
               WorldGrid$Cell.ZValue value = cell.getStack().getZValue(layer);
               if (value != null) {
                  clipboard.addCell(x, y, value.getTexture());
               }
            }
         }
      }
   }

   public void pasteLayer() {
      int layer = this.layerSlider.getValue();
      WorldGrid grid = this.model.getWorldGrid();

      for (CopyCell cc : clipboard.getContents()) {
         WorldGrid$Cell cell = grid.getCell(cc.getX(), cc.getY());
         if (cell != null) {
            cell.getStack().addZ(layer, cc.getTexture());
         }
      }

      this.repaint();
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
      this.printingPainter.paintLayer(g2d, pageNumber, cellSize, 0, 25);
      return 0;
   }

   private enum AxisLockType {
      HORIZONTAL,
      VERTICAL;
   }

   private abstract class JToggleToolButton extends JToggleButton {
      public JToggleToolButton(String iconName, String tooltip) {
         super(FreeformLayerPanel.this.getIcon(iconName));
         this.setToolTipText(tooltip);
         this.addActionListener(new minecraft.planner.gui.freeform.FreeformLayerPanel.JToggleToolButton.1(this));
      }

      public abstract void click();
   }

   private abstract class JToolButton extends JButton {
      public JToolButton(String iconName, String tooltip) {
         super(FreeformLayerPanel.this.getIcon(iconName));
         this.setToolTipText(tooltip);
         this.addActionListener(new minecraft.planner.gui.freeform.FreeformLayerPanel.JToolButton.1(this));
      }

      public abstract void click();
   }

   private class LayerGridPanel extends JPanel {
      public final JPopupMenu designMenu = new JPopupMenu("Design");
      private Point dragStartPoint;

      public LayerGridPanel() {
         JMenu addRow = new JMenu("Insert row");
         JMenuItem addRowAbove = new JMenuItem("Above");
         JMenuItem addRowBelow = new JMenuItem("Below");
         addRow.add(addRowAbove);
         addRow.add(addRowBelow);
         JMenu addColumn = new JMenu("Insert column");
         JMenuItem addColumnLeft = new JMenuItem("Left");
         JMenuItem addColumnRight = new JMenuItem("Right");
         addColumn.add(addColumnLeft);
         addColumn.add(addColumnRight);
         JMenuItem addLayer = new JMenuItem("Insert layer");
         this.designMenu.add(addRow);
         this.designMenu.add(addColumn);
         this.designMenu.add(addLayer);
         this.designMenu.addSeparator();
         this.designMenu.add(FreeformLayerPanel.this.removeRow);
         this.designMenu.add(FreeformLayerPanel.this.removeColumn);
         this.designMenu.add(FreeformLayerPanel.this.removeLayer);
         this.designMenu.addSeparator();
         this.designMenu.add(FreeformLayerPanel.this.orientationMenu);
         this.designMenu.add(FreeformLayerPanel.this.attributesMenu);
         FreeformLayerPanel.this.orientationMenu.add(FreeformLayerPanel.this.createOrientationMenuItem(Orientation.North));
         FreeformLayerPanel.this.orientationMenu.add(FreeformLayerPanel.this.createOrientationMenuItem(Orientation.East));
         FreeformLayerPanel.this.orientationMenu.add(FreeformLayerPanel.this.createOrientationMenuItem(Orientation.South));
         FreeformLayerPanel.this.orientationMenu.add(FreeformLayerPanel.this.createOrientationMenuItem(Orientation.West));
         FreeformLayerPanel.this.orientationMenu.setEnabled(false);
         FreeformLayerPanel.this.attributesMenu.setEnabled(false);
         addRowAbove.addActionListener(new minecraft.planner.gui.freeform.FreeformLayerPanel.LayerGridPanel.1(this));
         addRowBelow.addActionListener(new 2(this));
         addColumnLeft.addActionListener(new 3(this));
         addColumnRight.addActionListener(new 4(this));
         FreeformLayerPanel.this.removeRow.addActionListener(new 5(this));
         FreeformLayerPanel.this.removeColumn.addActionListener(new 6(this));
         addLayer.addActionListener(new 7(this));
         FreeformLayerPanel.this.removeLayer.addActionListener(new 8(this));
         this.setFocusable(true);
         this.setKeyBindings();
      }

      private void setKeyBindings() {
         InputMap map = this.getInputMap(2);
         map.put(KeyStroke.getKeyStroke("UP"), "layerUp");
         map.put(KeyStroke.getKeyStroke("DOWN"), "layerDown");
         map.put(KeyStroke.getKeyStroke("LEFT"), "scaleLeft");
         map.put(KeyStroke.getKeyStroke("RIGHT"), "scaleRight");
         this.getActionMap().put("shiftPressed", new 9(this));
         this.getActionMap().put("shiftPressed", new 10(this));
         this.getActionMap().put("scaleLeft", new 11(this));
         this.getActionMap().put("scaleRight", new 12(this));
         this.getActionMap().put("layerUp", new 13(this));
         this.getActionMap().put("layerDown", new 14(this));
      }

      public void adjustSize() {
         WorldGrid grid = FreeformLayerPanel.this.model.getWorldGrid();
         int width = grid.getWidth() * FreeformLayerPanel.this.cellSize;
         int height = grid.getHeight() * FreeformLayerPanel.this.cellSize;
         Dimension size = new Dimension(width, height);
         this.setSize(size);
         this.setPreferredSize(size);
      }

      @Override
      public void paint(Graphics g) {
         this.paint(g, FreeformLayerPanel.this.getLayer());
      }

      public void paint(Graphics g, int layer) {
         super.paint(g);
         FreeformLayerPanel.this.painter.setHighlight(FreeformLayerPanel.this.highlightedColumn, FreeformLayerPanel.this.highlightedRow);
         FreeformLayerPanel.this.painter.paintLayer((Graphics2D)g, FreeformLayerPanel.this.layer, FreeformLayerPanel.this.cellSize);
      }
   }
}
