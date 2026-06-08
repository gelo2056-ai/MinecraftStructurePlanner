package minecraft.planner.model.circle;

import java.util.HashSet;
import java.util.Set;
import minecraft.planner.gui.Voxel;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class CircleModel extends StructureModel<CircleParameters> {
   public static final String NAME = "Circle";

   private boolean isWithinCircle(int origin, int radius, int x, int y) {
      int xSquared = (x - origin) * (x - origin);
      int ySquared = (y - origin) * (y - origin);
      double distance = Math.sqrt(xSquared + ySquared);
      return Math.round(distance) <= radius;
   }

   public void generate(CircleParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      int radius = parameters.getRadius();
      int gridDimension = radius * 2 + 1;
      grid.initialize(gridDimension, gridDimension);
      int origin = (int)Math.floor(gridDimension / 2.0);

      for (int xLoop = 0; xLoop < gridDimension; xLoop++) {
         for (int yLoop = 0; yLoop < gridDimension; yLoop++) {
            if (this.isWithinCircle(origin, radius, xLoop, yLoop)) {
               grid.getCell(xLoop, yLoop).getStack().addZ(1);
            }
         }
      }

      if (!parameters.isSolid()) {
         this.removeHiddenVoxels();
      }

      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Circle";
   }

   @Override
   protected boolean hiddenVoxel(int x, int y, int z) {
      WorldGrid$Cell left = this.grid.getCell(x - 1, y);
      WorldGrid$Cell right = this.grid.getCell(x + 1, y);
      WorldGrid$Cell back = this.grid.getCell(x, y + 1);
      WorldGrid$Cell front = this.grid.getCell(x, y - 1);
      return left != null
         && right != null
         && front != null
         && back != null
         && left.getStack().containsZValue(z)
         && right.getStack().containsZValue(z)
         && back.getStack().containsZValue(z)
         && front.getStack().containsZValue(z);
   }

   @Override
   protected void removeHiddenVoxels() {
      Set<Voxel> hiddenVoxels = new HashSet<>();

      for (int x = 1; x <= this.grid.getWidth(); x++) {
         for (int y = 1; y <= this.grid.getHeight(); y++) {
            WorldGrid$Cell cell = this.grid.getCell(x, y);
            if (cell != null && cell.getStack().containsZValue(1) && this.hiddenVoxel(x, y, 1)) {
               hiddenVoxels.add(new Voxel(x, y, 1));
            }
         }
      }

      for (Voxel voxel : hiddenVoxels) {
         this.grid.getCell(voxel.getX(), voxel.getY()).getStack().removeZ(voxel.getZ());
      }
   }

   @Override
   public boolean includeVisibleBlocksParameter() {
      return false;
   }
}
