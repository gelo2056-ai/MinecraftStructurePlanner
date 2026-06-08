package minecraft.planner.gui.java3d;

public class NBTKey {
   private byte block;
   private byte data;

   public NBTKey(byte block, byte data) {
      this.block = block;
      this.data = data;
   }

   public byte getBlock() {
      return this.block;
   }

   public byte getData() {
      return this.data;
   }

   @Override
   public int hashCode() {
      return this.block + this.data;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof NBTKey)) {
         return false;
      }

      NBTKey k = (NBTKey)o;
      return k.block == this.block && k.data == this.data;
   }
}
