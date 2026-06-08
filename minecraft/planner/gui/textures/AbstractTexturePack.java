package minecraft.planner.gui.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import minecraft.planner.gui.java3d.TextureColorRepresentation;

public abstract class AbstractTexturePack implements TexturePack, Comparable<TexturePack>, Serializable {
   private static final long serialVersionUID = -3303434148427700913L;
   public static final int TEXTURE_GRID_WIDTH = 16;
   public static final int TEXTURE_GRID_HEIGHT = 16;
   public static final Color ALPHA = new Color(0, true);
   private String name;
   private String fileName;
   private Map<TextureName, TextureColorRepresentation> colorRepresentationMap;
   private Map<TextureName, ImageIcon> nonTexturableImages = new HashMap<>();
   private Map<TextureName, ImageIcon> planImages = new HashMap<>();

   protected AbstractTexturePack() {
   }

   protected AbstractTexturePack(String name, String fileName) {
      this.colorRepresentationMap = new HashMap<>();
      this.name = name;
      this.fileName = fileName;

      for (TextureName texture : TextureName.nonTexturableValuesCollection()) {
         ImageIcon icon = getResourceIcon(texture.getResourceName());
         this.nonTexturableImages.put(texture, icon);
      }
   }

   protected ImageIcon getNonTexturableImage(TextureName texture) {
      return this.nonTexturableImages.get(texture);
   }

   protected static ImageIcon getResourceIcon(String resourceName) {
      try {
         return new ImageIcon(AbstractTexturePack.class.getResource("/minecraft/planner/resources/" + resourceName));
      } catch (Exception e) {
         System.err.println("Could not load image icon " + resourceName);
         return null;
      }
   }

   @Override
   public synchronized TextureName findClosestMatchingTexture(Color color, Collection<TextureName> textures) {
      TextureName match = null;
      double smallestDistance = Double.MAX_VALUE;

      for (TextureName texture : textures) {
         if (texture != TextureName.None) {
            TextureColorRepresentation rep = this.colorRepresentationMap.get(texture);
            if (rep == null) {
               rep = new TextureColorRepresentation(this, texture);
               this.colorRepresentationMap.put(texture, rep);
            }

            double distance = rep.computeDistance(color);
            if (distance < smallestDistance) {
               smallestDistance = distance;
               match = texture;
            }
         }
      }

      return match;
   }

   @Override
   public ImageIcon getPlanImage(TextureName texture) {
      switch (texture) {
         case CobblestoneHalfSlab:
            return this.getHalfSlabImage(texture, TextureName.Cobblestone);
         case SandstoneHalfSlab:
            return this.getHalfSlabImage(texture, TextureName.Sandstone);
         case StoneHalfSlab:
            return this.getHalfSlabImage(texture, TextureName.StoneDoubleSlab);
         case PlankHalfSlab:
            return this.getHalfSlabImage(texture, TextureName.Plank);
         case Fencepost:
            return this.getFencepostImage(texture, TextureName.Plank);
         case CobbleStair:
            return this.getStairImage(texture, TextureName.Cobblestone);
         case WoodStair:
            return this.getStairImage(texture, TextureName.Plank);
         default:
            return this.getImage(texture);
      }
   }

   private synchronized ImageIcon getStairImage(TextureName stairTexture, TextureName baseTexture) {
      ImageIcon stairIcon = this.planImages.get(stairTexture);
      if (stairIcon == null) {
         ImageIcon baseImage = this.getImage(baseTexture);
         BufferedImage stairImage = new BufferedImage(baseImage.getIconWidth(), baseImage.getIconHeight(), 2);
         Graphics2D g = stairImage.createGraphics();
         g.setColor(ALPHA);
         g.fillRect(0, 0, 32, 32);
         g.drawImage(this.getImage(baseTexture).getImage(), 16, 0, 32, 32, 16, 0, 32, 32, null);
         g.drawImage(this.getImage(baseTexture).getImage(), 0, 16, 16, 32, 0, 16, 16, 32, null);
         stairIcon = new ImageIcon(stairImage);
         g.dispose();
         this.planImages.put(stairTexture, stairIcon);
      }

      return stairIcon;
   }

   private synchronized ImageIcon getHalfSlabImage(TextureName slabTexture, TextureName baseTexture) {
      ImageIcon slabIcon = this.planImages.get(slabTexture);
      if (slabIcon == null) {
         ImageIcon baseImage = this.getImage(baseTexture);
         BufferedImage slabImage = new BufferedImage(baseImage.getIconWidth(), baseImage.getIconHeight(), 2);
         Graphics2D g = slabImage.createGraphics();
         g.setColor(ALPHA);
         g.fillRect(0, 0, 32, 32);
         g.drawImage(baseImage.getImage(), 0, 8, 32, 24, 0, 0, 32, 16, null);
         slabIcon = new ImageIcon(slabImage);
         g.dispose();
         this.planImages.put(slabTexture, slabIcon);
      }

      return slabIcon;
   }

   private synchronized ImageIcon getFencepostImage(TextureName texture, TextureName baseTexture) {
      ImageIcon postIcon = this.planImages.get(texture);
      if (postIcon == null) {
         ImageIcon baseImage = this.getImage(baseTexture);
         BufferedImage postImage = new BufferedImage(baseImage.getIconWidth(), baseImage.getIconHeight(), 2);
         Graphics2D g = postImage.createGraphics();
         g.setColor(ALPHA);
         g.fillRect(0, 0, 32, 32);
         g.drawImage(baseImage.getImage(), 10, 10, 22, 22, 10, 10, 22, 22, null);
         g.setColor(Color.BLACK);
         g.drawRect(10, 10, 12, 12);
         postIcon = new ImageIcon(postImage);
         g.dispose();
         this.planImages.put(texture, postIcon);
      }

      return postIcon;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getFileName() {
      return this.fileName;
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }

      if (!TexturePack.class.isAssignableFrom(o.getClass())) {
         return false;
      }

      TexturePack t = (TexturePack)o;
      return this.getName().equals(t.getName());
   }

   public int compareTo(TexturePack t) {
      return this.getName().compareTo(t.getName());
   }
}
