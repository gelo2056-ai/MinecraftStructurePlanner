package minecraft.planner.gui.textures;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;
import minecraft.planner.gui.JErrorDialog;
import minecraft.planner.gui.StructurePlanner;

public class FileTexturePack extends AbstractTexturePack {
   private static final long serialVersionUID = 8520398656963727780L;
   private final ImageIcon[][] icons;
   private final File file;

   public FileTexturePack(String name, File texturePack) {
      super(name, generateFileName(texturePack));
      this.file = texturePack;
      this.icons = new ImageIcon[16][16];

      try {
         ZipFile texturePackFile = new ZipFile(texturePack);
         ZipEntry terrainFile = texturePackFile.getEntry("terrain.png");
         InputStream is = texturePackFile.getInputStream(terrainFile);
         BufferedInputStream bis = new BufferedInputStream(is);
         byte[] imageData = new byte[(int)terrainFile.getSize()];
         bis.read(imageData);
         ImageIcon terrainImageIcon = new ImageIcon(imageData);
         Image terrainImage = terrainImageIcon.getImage();
         int terrainWidth = terrainImage.getWidth(null);
         int terrainHeight = terrainImage.getHeight(null);
         int imageWidth = terrainWidth / 16;
         int imageHeight = terrainHeight / 16;

         for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
               BufferedImage image = new BufferedImage(32, 32, 2);
               Graphics2D g = image.createGraphics();
               g.drawImage(terrainImage, 0, 0, 32, 32, x * imageWidth, y * imageHeight, (x + 1) * imageWidth, (y + 1) * imageHeight, null);
               this.icons[x][y] = new ImageIcon(image);
               g.dispose();
            }
         }

         this.icons[0][0] = getResourceIcon("none.png");
      } catch (Throwable t) {
         new JErrorDialog(StructurePlanner.getFrame(), "Texture pack " + texturePack.getAbsolutePath() + " does not exist.", null).setVisible(true);
      }
   }

   @Override
   public ImageIcon getImage(int x, int y) {
      return this.icons[x][y];
   }

   @Override
   public ImageIcon getImage(TextureName texture) {
      return texture.isTexturable() ? this.icons[texture.getResourceX()][texture.getResourceY()] : this.getNonTexturableImage(texture);
   }

   private static String generateFileName(File texturePackFile) {
      String fileName = texturePackFile.getName();
      int periodIndex = texturePackFile.getName().lastIndexOf(46);
      return "TexturePacks" + File.separator + fileName.substring(0, periodIndex + 1) + "tpk";
   }

   @Override
   public String toString() {
      return this.getName();
   }
}
