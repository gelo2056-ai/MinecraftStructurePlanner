package minecraft.planner.model.torus;

import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;

public class TorusModel extends StructureModel<TorusParameters> {
   public static final String NAME = "Torus";

   private static boolean isWithinTorus(int xyOrigin, int zOrigin, int majorRadius, int minorRadius, int x, int y, int z) {
      x -= xyOrigin;
      y -= xyOrigin;
      z -= zOrigin;
      int xSquared = x * x;
      int ySquared = y * y;
      int zSquared = z * z;
      int majorSquared = majorRadius * majorRadius;
      int minorSquared = minorRadius * minorRadius;
      int quarticLeft = xSquared + ySquared + zSquared + majorSquared - minorSquared;
      quarticLeft *= quarticLeft;
      int quarticRight = 4 * majorSquared * (xSquared + ySquared);
      return quarticRight - quarticLeft > 0;
   }

   public void generate(TorusParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      int majorRadius = parameters.getMajorRadius();
      int minorRadius = parameters.getMinorRadius();
      int gridDimension = 2 * majorRadius + 2 * minorRadius - 1;
      grid.initialize(gridDimension, gridDimension);
      int xyOrigin = (int)Math.floor(gridDimension / 2.0);
      int zOrigin = minorRadius + 1;

      for (int x = 0; x < gridDimension; x++) {
         for (int y = 0; y < gridDimension; y++) {
            for (int z = 0; z <= 2 * minorRadius; z++) {
               if (isWithinTorus(xyOrigin, zOrigin, majorRadius, minorRadius, x, y, z)) {
                  grid.getCell(x, y).getStack().addZ(z - 1);
               }
            }
         }
      }

      grid.updateMetrics(this);
      super.removeHiddenVoxels();
      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Torus";
   }
}
