package minecraft.planner.model.menger;

import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class MengerModel extends StructureModel<MengerParameters> {
   public static final String NAME = "Menger Sponge";

   private void menger(int x, int y, int z, int level) {
      if (level == 0) {
         WorldGrid$Cell cell = this.getWorldGrid().getCell(x, y);
         cell.getStack().addZ(z);
      } else {
         int s = (int)Math.pow(3.0, level - 1);
         int s2 = s * 2;
         int nextLevel = level - 1;
         this.menger(x, y, z, nextLevel);
         this.menger(x + s, y, z, nextLevel);
         this.menger(x + s2, y, z, nextLevel);
         this.menger(x, y + s, z, nextLevel);
         this.menger(x + s2, y + s, z, nextLevel);
         this.menger(x, y + s2, z, nextLevel);
         this.menger(x + s, y + s2, z, nextLevel);
         this.menger(x + s2, y + s2, z, nextLevel);
         this.menger(x, y, z + s, nextLevel);
         this.menger(x + s2, y, z + s, nextLevel);
         this.menger(x, y + s2, z + s, nextLevel);
         this.menger(x + s2, y + s2, z + s, nextLevel);
         this.menger(x, y, z + s2, nextLevel);
         this.menger(x + s, y, z + s2, nextLevel);
         this.menger(x + s2, y, z + s2, nextLevel);
         this.menger(x, y + s, z + s2, nextLevel);
         this.menger(x + s2, y + s, z + s2, nextLevel);
         this.menger(x, y + s2, z + s2, nextLevel);
         this.menger(x + s, y + s2, z + s2, nextLevel);
         this.menger(x + s2, y + s2, z + s2, nextLevel);
      }
   }

   @Override
   public boolean includeVisibleBlocksParameter() {
      return false;
   }

   public void generate(MengerParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      int dimension = (int)Math.pow(3.0, parameters.getLevel());
      grid.initialize(dimension, dimension);
      this.menger(0, 0, 1, parameters.getLevel());
      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Menger Sponge";
   }
}
