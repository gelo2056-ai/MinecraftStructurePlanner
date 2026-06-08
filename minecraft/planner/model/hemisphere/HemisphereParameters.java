package minecraft.planner.model.hemisphere;

import minecraft.planner.model.StructureParameters;

public class HemisphereParameters extends StructureParameters {
   private int radius;
   private int maxVertical;
   private int baseHeight;

   public HemisphereParameters(int radius, int maxVertical, int baseHeight) {
      this.radius = radius;
      this.maxVertical = maxVertical;
      this.baseHeight = baseHeight;
   }

   public int getRadius() {
      return this.radius;
   }

   public int getMaxVertical() {
      return this.maxVertical;
   }

   public int getBaseHeight() {
      return this.baseHeight;
   }
}
