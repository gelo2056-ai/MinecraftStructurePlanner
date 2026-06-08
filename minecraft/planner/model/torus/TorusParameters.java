package minecraft.planner.model.torus;

import minecraft.planner.model.StructureParameters;

public class TorusParameters extends StructureParameters {
   private int majorRadius;
   private int minorRadius;
   private int tolerance;

   public TorusParameters(int majorRadius, int minorRadius) {
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
   }

   public int getMajorRadius() {
      return this.majorRadius;
   }

   public int getMinorRadius() {
      return this.minorRadius;
   }
}
