package minecraft.planner.gui.displaypanels;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.grid.attributes.Orientation;

public class LayerPainter<P extends StructureParameters> {
   private static final Composite TRANSPARENCY_COMPOSITE = AlphaComposite.getInstance(3, 0.25F);
   private static final Color LIGHT_GRAY = new Color(220, 220, 220);
   private static final Color HIGHLIGHT = new Color(179, 220, 255);
   private final StructureModel<P> model;
   private final TexturePack texturePack;
   private final boolean highlightingEnabled;
   private final boolean filledBackground;
   private final boolean lowerLayerDisplayed;
   private int xHighlight;
   private int yHighlight;

   public LayerPainter(StructureModel<P> model, TexturePack texturePack) {
      this(model, texturePack, false);
   }

   public LayerPainter(StructureModel<P> model, TexturePack texturePack, boolean highlightingEnabled) {
      this(model, texturePack, highlightingEnabled, true);
   }

   public LayerPainter(StructureModel<P> model, TexturePack texturePack, boolean highlightingEnabled, boolean filledBackground) {
      this(model, texturePack, highlightingEnabled, filledBackground, true);
   }

   public LayerPainter(StructureModel<P> model, TexturePack texturePack, boolean highlightingEnabled, boolean filledBackground, boolean lowerLayerDisplayed) {
      this.model = model;
      this.texturePack = texturePack;
      this.highlightingEnabled = highlightingEnabled;
      this.filledBackground = filledBackground;
      this.lowerLayerDisplayed = lowerLayerDisplayed;
   }

   public final void setHighlight(int x, int y) {
      this.xHighlight = x;
      this.yHighlight = y;
   }

   public final void paintLayer(Graphics2D g, int layer, int cellSize) {
      this.paintLayer(g, layer, cellSize, 0, 0);
   }

   public final void paintLayer(Graphics2D g, int layer, int cellSize, int xOffset, int yOffset) {
      WorldGrid grid = this.model.getWorldGrid();

      for (int xLoop = 0; xLoop < grid.getWidth(); xLoop++) {
         for (int yLoop = 0; yLoop < grid.getHeight(); yLoop++) {
            this.drawSquare(g, grid.getCell(xLoop, yLoop), cellSize, layer, xOffset, yOffset);
         }
      }
   }

   private void drawSquare(Graphics g, WorldGrid$Cell cell, int cellSize, int layer, int xOffset, int yOffset) {
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
                  g.fillRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
               } else {
                  TextureName texture = cellValue.getTexture();
                  if (texture != null) {
                     Image cellImage = texture.getPlanViewImageIcon(this.texturePack).getImage();
                     int imageWidth = cellSize;
                     int imageHeight = cellSize;
                     g2.drawImage(cellImage, x * cellSize + xOffset, y * cellSize + yOffset, imageWidth, imageHeight, null);
                     if (texture.hasOrientation()) {
                        Orientation o = cellValue.getOrientation();
                        Image arrowImage = null;
                        switch (o) {
                           case North:
                              arrowImage = TextureName.ArrowNorth.getImageIcon(this.texturePack).getImage();
                              break;
                           case East:
                              arrowImage = TextureName.ArrowEast.getImageIcon(this.texturePack).getImage();
                              break;
                           case South:
                              arrowImage = TextureName.ArrowSouth.getImageIcon(this.texturePack).getImage();
                              break;
                           case West:
                              arrowImage = TextureName.ArrowWest.getImageIcon(this.texturePack).getImage();
                        }

                        if (arrowImage != null) {
                           g2.drawImage(arrowImage, x * cellSize + xOffset, y * cellSize + yOffset, imageWidth, imageHeight, null);
                        }
                     }

                     if (imageWidth < cellSize || imageHeight < cellSize) {
                        invertBorder = false;
                     }
                  }
               }
            }

            if (invertBorder) {
               if (!this.highlightingEnabled || x != this.xHighlight && y != this.yHighlight) {
                  g.setColor(Color.WHITE);
               } else {
                  g.setColor(HIGHLIGHT);
               }

               g.drawRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
            }
         } else if (this.lowerLayerDisplayed && cell.getStack().containsZValue(layer - 1)) {
            WorldGrid$Cell.ZValue value = cell.getStack().getZValue(layer - 1);
            boolean invertBorder = true;
            TextureName lowerTexture = value.getTexture();
            if (lowerTexture != null) {
               if (lowerTexture == TextureName.None) {
                  g.setColor(Color.LIGHT_GRAY);
                  g.fillRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
               } else {
                  Image cellImage = lowerTexture.getPlanViewImageIcon(this.texturePack).getImage();
                  int imageWidth = cellSize;
                  int imageHeight = cellSize;
                  Composite originalComposite = g2.getComposite();
                  g2.setComposite(TRANSPARENCY_COMPOSITE);
                  g2.drawImage(cellImage, x * cellSize + xOffset, y * cellSize + yOffset, imageWidth, imageHeight, null);
                  if (lowerTexture.hasOrientation()) {
                     Orientation o = value.getOrientation();
                     Image arrowImage = null;
                     switch (o) {
                        case North:
                           arrowImage = TextureName.ArrowNorth.getImageIcon(this.texturePack).getImage();
                           break;
                        case East:
                           arrowImage = TextureName.ArrowEast.getImageIcon(this.texturePack).getImage();
                           break;
                        case South:
                           arrowImage = TextureName.ArrowSouth.getImageIcon(this.texturePack).getImage();
                           break;
                        case West:
                           arrowImage = TextureName.ArrowWest.getImageIcon(this.texturePack).getImage();
                     }

                     if (arrowImage != null) {
                        g2.drawImage(arrowImage, x * cellSize + xOffset, y * cellSize + yOffset, imageWidth, imageHeight, null);
                     }
                  }

                  g2.setComposite(originalComposite);
                  if (imageWidth < cellSize || imageHeight < cellSize) {
                     invertBorder = false;
                  }
               }
            }

            if (invertBorder) {
               g.setColor(Color.WHITE);
               g.drawRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
            }
         } else if (!this.highlightingEnabled || x != this.xHighlight && y != this.yHighlight) {
            if (this.filledBackground) {
               g.setColor(Color.WHITE);
               g.fillRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
            }

            g.setColor(Color.BLACK);
            g.drawRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
         } else {
            g.setColor(HIGHLIGHT);
            g.fillRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
            g.setColor(Color.BLACK);
            g.drawRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
         }

         g2.dispose();
      }
   }
}
