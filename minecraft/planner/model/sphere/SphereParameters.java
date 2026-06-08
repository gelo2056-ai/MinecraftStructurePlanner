package minecraft.planner.model.sphere;

import minecraft.planner.model.StructureParameters;

public class SphereParameters extends StructureParameters {
   private int radius;

   public SphereParameters(int radius) {
      this.radius = radius;
   }

   public int getRadius() {
      return this.radius;
   }
}
