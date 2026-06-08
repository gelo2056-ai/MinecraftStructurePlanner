package minecraft.planner.gui;

public class Voxel {
   int x;
   int y;
   int z;

   public Voxel(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   @Override
   public int hashCode() {
      return this.x + this.y + this.z;
   }

   @Override
   public boolean equals(Object o) {
      if (o instanceof Voxel) {
         Voxel voxel = (Voxel)o;
         return this.x == voxel.x && this.y == voxel.y && this.z == voxel.z;
      } else {
         return false;
      }
   }
}
