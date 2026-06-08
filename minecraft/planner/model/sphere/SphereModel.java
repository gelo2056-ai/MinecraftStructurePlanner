package minecraft.planner.model.sphere;

import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class SphereModel extends StructureModel<SphereParameters> {
   public static final String NAME = "Sphere";

   private boolean isWithinSphere(int origin, int radius, int x, int y) {
      int xSquared = (x - origin) * (x - origin);
      int ySquared = (y - origin) * (y - origin);
      double distance = Math.sqrt(xSquared + ySquared);
      return Math.round(distance) <= radius;
   }

   private int heightOfSphere(int origin, int radius, int x, int y) {
      if (this.isWithinSphere(origin, radius, x, y)) {
         int xSquared = (x - origin) * (x - origin);
         int ySquared = (y - origin) * (y - origin);
         int rSquared = radius * radius;
         return (int)Math.round(Math.sqrt(rSquared - xSquared - ySquared));
      } else {
         return 0;
      }
   }

   public void generate(SphereParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      int radius = parameters.getRadius();
      int gridDimension = radius * 2 + 1;
      grid.initialize(gridDimension, gridDimension);
      int origin = (int)Math.floor(gridDimension / 2.0);

      for (int xLoop = 0; xLoop < gridDimension; xLoop++) {
         for (int yLoop = 0; yLoop < gridDimension; yLoop++) {
            int cellHeight = this.heightOfSphere(origin, radius, xLoop, yLoop);
            grid.getCell(xLoop, yLoop).getStack().addZ(cellHeight);
         }
      }

      for (int xLoop = 0; xLoop < gridDimension; xLoop++) {
         for (int yLoop = 0; yLoop < gridDimension; yLoop++) {
            WorldGrid$Cell cell = this.grid.getCell(xLoop, yLoop);
            int cellHeight = cell.getStack().getMaxHeight();
            if (cellHeight > 0) {
               int visibleHeight = this.calculateVisibleHeight(cell);
               cell.getStack().addZ(visibleHeight, cellHeight);
            }
         }
      }

      for (int xLoop = 0; xLoop < gridDimension; xLoop++) {
         for (int yLoop = 0; yLoop < gridDimension; yLoop++) {
            WorldGrid$Cell cell = this.grid.getCell(xLoop, yLoop);
            WorldGrid$Cell.ZStack stack = cell.getStack();
            int cellHeight = stack.getMaxHeight();
            if (cellHeight > 0) {
               stack.mirrorZ(radius);
            }
         }
      }

      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Sphere";
   }
}
