package minecraft.planner.model.freeform;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Binvox {
   private int version;
   private int width;
   private int depth;
   private int height;
   private byte[] voxels;

   public Binvox(File binvoxFile) {
      try {
         this.initialize(binvoxFile);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public int getVersion() {
      return this.version;
   }

   public int getWidth() {
      return this.width;
   }

   public int getDepth() {
      return this.depth;
   }

   public int getHeight() {
      return this.height;
   }

   public int getSize() {
      return this.voxels.length;
   }

   public int getValue(int index) {
      return this.voxels[index];
   }

   public int getValue(int x, int y, int z) {
      if (x < 0 || x >= this.width) {
         throw new IndexOutOfBoundsException("X index out of bounds");
      } else if (y < 0 || y >= this.depth) {
         throw new IndexOutOfBoundsException("Y index out of bounds");
      } else if (z >= 0 && z < this.height) {
         int layerSize = this.width * this.depth;
         int index = x * layerSize + y * this.width + z;
         return this.voxels[index];
      } else {
         throw new IndexOutOfBoundsException("Z index out of bounds");
      }
   }

   private void initialize(File file) throws IOException {
      DataInputStream dis = new DataInputStream(new FileInputStream(file));

      String line;
      do {
         line = dis.readLine();
         this.parseHeaderLine(line);
      } while (line != null && !line.equalsIgnoreCase("data"));

      this.voxels = new byte[this.width * this.depth * this.height];
      int voxelIndex = 0;
      int readBytes = 0;
      int readVoxels = 0;
      int setVoxels = 0;

      while (dis.available() > 0) {
         int value = dis.readUnsignedByte();
         int count = dis.readUnsignedByte();
         readVoxels += count;

         for (int i = 0; i < count; i++) {
            if (voxelIndex < this.voxels.length) {
               this.voxels[voxelIndex++] = (byte)value;
            } else {
               System.out.println("Voxels read doesn't match size of voxel array");
            }

            if (value != 0) {
               setVoxels++;
            }
         }
      }

      dis.close();
   }

   private void parseHeaderLine(String line) {
      line = line.toLowerCase();
      if (line.startsWith("#binvox ")) {
         this.version = Integer.parseInt(line.substring(8));
      } else if (line.startsWith("dim")) {
         String[] fields = line.split(" ");
         this.width = Integer.parseInt(fields[1]);
         this.depth = Integer.parseInt(fields[2]);
         this.height = Integer.parseInt(fields[3]);
      }
   }
}
