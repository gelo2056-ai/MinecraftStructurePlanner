package minecraft.planner.gui.java3d;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;

public class TextureColorRepresentation {
   private final TextureName texture;
   private final TexturePack texturePack;
   private int red;
   private int green;
   private int blue;
   private java.awt.Color color;

   public TextureColorRepresentation(TexturePack texturePack, TextureName name) {
      this.texturePack = texturePack;
      this.texture = name;

      try {
         ImageIcon icon = name.getImageIcon(texturePack);
         Image image = icon.getImage();
         BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
         Graphics2D g2D = bufferedImage.createGraphics();
         g2D.drawImage(image, 0, 0, null);
         g2D.dispose();
         int a = 0;
         int r = 0;
         int g = 0;
         int b = 0;
         int width = icon.getIconWidth();
         int height = icon.getIconHeight();
         int count = 0;

         for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
               int rgb = bufferedImage.getRGB(x, y);
               a = (rgb & 0xFF000000) >> 24;
               if (a != 0) {
                  r += (rgb & 0xFF0000) >> 16;
                  g += (rgb & 0xFF00) >> 8;
                  b += rgb & 0xFF;
                  count++;
               }
            }
         }

         this.red = r / count;
         this.green = g / count;
         this.blue = b / count;
         this.color = new java.awt.Color(this.red, this.green, this.blue);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public int getRed() {
      return this.red;
   }

   public int getGreen() {
      return this.green;
   }

   public int getBlue() {
      return this.blue;
   }

   public TexturePack getTexturePack() {
      return this.texturePack;
   }

   public TextureName getTextureName() {
      return this.texture;
   }

   public java.awt.Color getColor() {
      return this.color;
   }

   public double computeDistance(java.awt.Color color) {
      return this.computeDistance(color.getRed(), color.getGreen(), color.getBlue());
   }

   public double computeDistance(TextureColorRepresentation colorRepresentation) {
      return this.computeDistance(colorRepresentation.red, colorRepresentation.green, colorRepresentation.blue);
   }

   public double computeDistance(int colorRed, int colorGreen, int colorBlue) {
      int redDiff = this.red - colorRed;
      int greenDiff = this.green - colorGreen;
      int blueDiff = this.blue - colorBlue;
      return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
   }
}
