package minecraft.planner.model.grid;

import java.util.HashSet;
import java.util.Set;
import minecraft.planner.gui.textures.TextureName;

public class UndoBuffer {
   private final Set<UndoBuffer.UndoCell> buffer = new HashSet<>();

   public void addCell(int x, int y, int z, TextureName texture, minecraft.planner.model.grid.attributes.Orientation orientation) {
      UndoBuffer.UndoCell cell = new UndoBuffer.UndoCell(x, y, z, texture, orientation);
      this.buffer.add(cell);
   }

   public Set<UndoBuffer.UndoCell> getBuffer() {
      return this.buffer;
   }

   public void undo(WorldGrid grid) {
      for (UndoBuffer.UndoCell cell : this.buffer) {
         TextureName texture = cell.texture;
         if (cell.texture == TextureName.None) {
            grid.getCell(cell.x, cell.y).getStack().removeZ(cell.z);
         } else {
            grid.getCell(cell.x, cell.y).getStack().addZ(cell.z, cell.texture, cell.orientation);
         }
      }
   }

   public boolean isEmpty() {
      return this.buffer.isEmpty();
   }

   public boolean contains(int x, int y, int z) {
      for (UndoBuffer.UndoCell cell : this.buffer) {
         if (cell.x == x && cell.y == y && cell.z == z) {
            return true;
         }
      }

      return false;
   }

   public void clearBuffer() {
      this.buffer.clear();
   }

   private class UndoCell {
      private int x;
      private int y;
      private int z;
      private TextureName texture;
      private minecraft.planner.model.grid.attributes.Orientation orientation;

      public UndoCell(int x, int y, int z, TextureName texture, minecraft.planner.model.grid.attributes.Orientation orientation) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.texture = texture;
         this.orientation = orientation;
      }

      @Override
      public int hashCode() {
         return this.x + this.y + this.z;
      }

      @Override
      public boolean equals(Object o) {
         if (!(o instanceof UndoBuffer.UndoCell)) {
            return false;
         }

         UndoBuffer.UndoCell c = (UndoBuffer.UndoCell)o;
         return this.x == c.x && this.y == c.y && this.z == c.z;
      }
   }
}
