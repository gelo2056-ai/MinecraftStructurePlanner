package minecraft.planner.model.circle;

import minecraft.planner.model.StructureParameters;

public class CircleParameters extends StructureParameters {
   private int radius;
   private boolean isSolid;

   public CircleParameters(int radius, boolean isSolid) {
      this.radius = radius;
      this.isSolid = isSolid;
   }

   public int getRadius() {
      return this.radius;
   }

   public boolean isSolid() {
      return this.isSolid;
   }
}
