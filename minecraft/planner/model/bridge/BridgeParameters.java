package minecraft.planner.model.bridge;

import minecraft.planner.model.StructureParameters;

public class BridgeParameters extends StructureParameters {
   private int span;
   private int width;
   private int maxHeight;
   private int minStepWidth;
   private int wallHeight;
   private boolean fullySupported;
   private boolean inverted;

   public BridgeParameters(int span, int width, int maxHeight, int minStepWidth, int wallHeight, boolean fullySupported, boolean inverted) {
      this.span = span;
      this.width = width;
      this.maxHeight = maxHeight;
      this.minStepWidth = minStepWidth;
      this.wallHeight = wallHeight;
      this.fullySupported = fullySupported;
      this.inverted = inverted;
   }

   public int getSpan() {
      return this.span;
   }

   public int getWidth() {
      return this.width;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public int getMinStepWidth() {
      return this.minStepWidth;
   }

   public int getWallHeight() {
      return this.wallHeight;
   }

   public boolean isFullySupported() {
      return this.fullySupported;
   }

   public boolean isInverted() {
      return this.inverted;
   }
}
