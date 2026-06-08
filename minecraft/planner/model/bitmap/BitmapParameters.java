package minecraft.planner.model.bitmap;

import javax.swing.ImageIcon;
import minecraft.planner.model.StructureParameters;

public class BitmapParameters extends StructureParameters {
   private ImageIcon backgroundImage;
   private int width;
   private int height;
   private boolean lockAspectRatio;

   public BitmapParameters(ImageIcon backgroundImage, int width, int height, boolean lockAspectRatio) {
      this.width = width;
      this.height = height;
      this.lockAspectRatio = lockAspectRatio;
      this.backgroundImage = backgroundImage;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public ImageIcon getBackgroundImage() {
      return this.backgroundImage;
   }

   public boolean getLockAspectRatio() {
      return this.lockAspectRatio;
   }

   public void setBackgroundImage(ImageIcon backgroundImage) {
      this.backgroundImage = backgroundImage;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public void setLockAspectRatio(boolean lock) {
      this.lockAspectRatio = lock;
   }
}
