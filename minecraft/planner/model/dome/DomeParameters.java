package minecraft.planner.model.dome;

import minecraft.planner.model.StructureParameters;

public class DomeParameters extends StructureParameters {
   private int width;
   private int height;
   private int maxVertical;
   private int baseHeight;
   private boolean xInterpolate;
   private boolean yInterpolate;
   private boolean squareBase;

   public DomeParameters(int width, int height, int maxVertical, int baseHeight, boolean xInterpolate, boolean yInterpolate, boolean squareBase) {
      this.width = width;
      this.height = height;
      this.maxVertical = maxVertical;
      this.baseHeight = baseHeight;
      this.xInterpolate = xInterpolate;
      this.yInterpolate = yInterpolate;
      this.squareBase = squareBase;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getMaxVertical() {
      return this.maxVertical;
   }

   public int getBaseHeight() {
      return this.baseHeight;
   }

   public boolean xInterpolate() {
      return this.xInterpolate;
   }

   public boolean yInterpolate() {
      return this.yInterpolate;
   }

   public boolean squareBase() {
      return this.squareBase;
   }
}
