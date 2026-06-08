package minecraft.planner.model.freeform;

import minecraft.planner.model.StructureParameters;

public class FreeformParameters extends StructureParameters {
   private int x;
   private int y;
   private int maxHeight;

   public FreeformParameters(int x, int y, int maxHeight) {
      this.x = x;
      this.y = y;
      this.maxHeight = maxHeight;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setMaxHeight(int height) {
      this.maxHeight = height;
   }
}
