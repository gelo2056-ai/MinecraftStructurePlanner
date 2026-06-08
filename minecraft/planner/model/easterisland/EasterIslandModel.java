package minecraft.planner.model.easterisland;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import javax.swing.ImageIcon;
import minecraft.planner.gui.displaypanels.TextureFilterChangeListener;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.util.UserConfigurationManager;

public class EasterIslandModel extends StructureModel<EasterIslandParameters> implements TextureFilterChangeListener {
   public static final String NAME = "Easter Island";
   private Collection<TextureName> textures;
   protected BufferedImage image = null;
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();

   public void setTextures(Collection<TextureName> textures) {
      this.textures = textures;
   }

   public void generate(EasterIslandParameters parameters) {
      super.generate(parameters);
      if (this.createSkinImage()) {
         this.generateModel();
      }

      super.generated();
   }

   protected void generateModel() {
      WorldGrid grid = this.getWorldGrid();
      grid.initialize(8, 8, false);
      this.head(0, 0, 0);
   }

   protected TextureName getTexture(Color color) {
      return this.textures != null && !this.textures.isEmpty()
         ? this.texturePack.findClosestMatchingTexture(color, this.textures)
         : this.texturePack.findClosestMatchingTexture(color, TextureName.texturableValuesCollection());
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Easter Island";
   }

   @Override
   public void textureFilterChanged(Collection<TextureName> textures) {
      this.textures = textures;
   }

   protected boolean createSkinImage() {
      Graphics2D g2D = null;

      try {
         ImageIcon image = this.parameters.getCharacterImage();
         this.image = new BufferedImage(image.getIconWidth(), image.getIconHeight(), 1);
         g2D = this.image.createGraphics();
         g2D.drawImage(image.getImage(), 0, 0, null);
         return true;
      } catch (Throwable t) {
         this.image = null;
      } finally {
         if (g2D != null) {
            try {
               g2D.dispose();
               Graphics2D var13 = null;
            } catch (Throwable var10) {
            }
         }
      }

      return false;
   }

   protected void head(int xOffset, int yOffset, int zOffset) {
      for (int x = 0; x <= 7; x++) {
         for (int z = 7; z >= 0; z--) {
            int rgb = this.image.getRGB(8 + x, 8 + z);
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0xFF00) >> 8;
            int b = rgb & 0xFF;
            Color color = new Color(r, g, b);
            this.grid.getCell(x + xOffset, 7 + yOffset).getStack().addZ(8 - z + zOffset, this.getTexture(color));
         }
      }

      for (int y = 0; y <= 6; y++) {
         for (int z = 7; z >= 0; z--) {
            int rgb = this.image.getRGB(y, 8 + z);
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0xFF00) >> 8;
            int b = rgb & 0xFF;
            Color color = new Color(r, g, b);
            this.grid.getCell(xOffset, y + yOffset).getStack().addZ(8 - z + zOffset, this.getTexture(color));
         }
      }

      for (int var14 = 0; var14 <= 6; var14++) {
         for (int z = 7; z >= 0; z--) {
            int rgb = this.image.getRGB(16 + var14, 8 + z);
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0xFF00) >> 8;
            int b = rgb & 0xFF;
            Color color = new Color(r, g, b);
            this.grid.getCell(7 + xOffset, 6 - var14 + yOffset).getStack().addZ(8 - z + zOffset, this.getTexture(color));
         }
      }

      for (int var12 = 1; var12 <= 6; var12++) {
         for (int z = 7; z >= 0; z--) {
            int rgb = this.image.getRGB(24 + var12, 8 + z);
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0xFF00) >> 8;
            int b = rgb & 0xFF;
            Color color = new Color(r, g, b);
            this.grid.getCell(var12 + xOffset, yOffset).getStack().addZ(8 - z + zOffset, this.getTexture(color));
         }
      }

      for (int var13 = 1; var13 <= 6; var13++) {
         for (int var15 = 1; var15 <= 6; var15++) {
            int rgb = this.image.getRGB(8 + var13, var15);
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0xFF00) >> 8;
            int b = rgb & 0xFF;
            Color color = new Color(r, g, b);
            this.grid.getCell(var13 + xOffset, var15 + yOffset).getStack().addZ(8 + zOffset, this.getTexture(color));
         }
      }
   }
}
